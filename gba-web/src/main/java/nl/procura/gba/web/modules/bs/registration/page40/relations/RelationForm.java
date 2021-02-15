/*
 * Copyright 2021 - 2022 Procura B.V.
 *
 * In licentie gegeven krachtens de EUPL, versie 1.2
 * U mag dit werk niet gebruiken, behalve onder de voorwaarden van de licentie.
 * U kunt een kopie van de licentie vinden op:
 *
 *   https://github.com/vrijBRP/vrijBRP/blob/master/LICENSE.md
 *
 * Deze bevat zowel de Nederlandse als de Engelse tekst
 *
 * Tenzij dit op grond van toepasselijk recht vereist is of schriftelijk
 * is overeengekomen, wordt software krachtens deze licentie verspreid
 * "zoals deze is", ZONDER ENIGE GARANTIES OF VOORWAARDEN, noch expliciet
 * noch impliciet.
 * Zie de licentie voor de specifieke bepalingen voor toestemmingen en
 * beperkingen op grond van de licentie.
 */

package nl.procura.gba.web.modules.bs.registration.page40.relations;

import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.toList;
import static nl.procura.gba.web.modules.bs.registration.page40.relations.RelationBean.*;
import static nl.procura.gba.web.services.bs.algemeen.functies.BsDossierNaamgebruikUtils.getNameWithAge;

import java.util.List;

import nl.procura.gba.jpa.personen.db.DossPersRelation;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.registration.RelationService;
import nl.procura.gba.web.services.bs.registration.RelationType;
import nl.procura.vaadin.component.container.ArrayListContainer;
import nl.procura.vaadin.component.field.ProNativeSelect;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

class RelationForm extends GbaForm<RelationBean> {

  private final List<DossierPersoon>  dossPersons;
  private RelatedPersonChangeListener relatedPersonChangeListener;

  RelationForm(List<DossierPersoon> people) {
    dossPersons = unmodifiableList(people);
    setOrder(RelationBean.F_PERSON, F_RELATIONSHIP_TYPE, F_RELATED_PERSON, F_NO_RELATED_PERSON);
    final RelationBean bean = new RelationBean();
    setBean(bean);

    getField(RelationBean.F_PERSON)
        .addListener((ValueChangeListener) e -> {
          setRelationTypes();
          setRelations();
          setRelatives();
        });

    getField(F_RELATIONSHIP_TYPE)
        .addListener((ValueChangeListener) e -> {
          setRelations();
          setRelatives();
        });

    getField(F_RELATED_PERSON)
        .addListener((ValueChangeListener) e -> {
          setRelatives();
        });

    fillPersonField();
  }

  private void setRelationTypes() {
    FieldValue personValue = (FieldValue) getField(F_PERSON).getValue();
    if (personValue != null) {
      ProNativeSelect relationsField = getField(RelationBean.F_RELATIONSHIP_TYPE, ProNativeSelect.class);
      DossierPersoon person = (DossierPersoon) personValue.getValue();
      RelationService relationService = getApplication().getServices().getRelationService();
      relationsField.setContainerDataSource(new RelationTypeContainer(person, relationService));
    }
  }

  private void fillPersonField() {
    ArrayListContainer personsList = new ArrayListContainer(dossPersons.stream()
        .filter(p -> p.getDossierPersoonType() == DossierPersoonType.INSCHRIJVER)
        .map(p -> new FieldValue(p, getNameWithAge(p)))
        .collect(toList()));
    ProNativeSelect personsField = getField(RelationBean.F_PERSON, ProNativeSelect.class);
    personsField.setDataSource(personsList);
  }

  private void setRelations() {
    FieldValue personValue = (FieldValue) getField(F_PERSON).getValue();
    if (personValue != null) {
      DossierPersoon person = (DossierPersoon) personValue.getValue();
      FieldValue relationValue = (FieldValue) getField(F_RELATIONSHIP_TYPE).getValue();

      if (person != null && relationValue != null) {
        RelationType relationType = (RelationType) relationValue.getValue();
        ArrayListContainer relationList = new ArrayListContainer(dossPersons.stream()
            .filter(p -> !p.getId().equals(person.getId()))
            .map(p -> new FieldValue(p, getRelationName(person, relationType, p)))
            .collect(toList()));
        ProNativeSelect relationField = getField(F_RELATED_PERSON, ProNativeSelect.class);
        relationField.setDataSource(relationList);
      }
    }
  }

  private String getRelationName(DossierPersoon p, RelationType r, DossierPersoon rp) {
    if (p != null && r != null && rp != null) {
      RelationService relationService = getApplication().getServices().getRelationService();
      if (!relationService.getRelations(p, r, rp).isEmpty()) {
        return getNameWithAge(rp) + " (toegevoegd)";
      }
      DossPersRelation otherRelation = relationService.getInOtherRelation(p, r, rp);
      if (otherRelation != null) {
        switch (RelationType.valueOfCode(otherRelation.getRelationShipType())) {
          case PARENT_OF:
            return getNameWithAge(rp) + " (NIET MOGELIJK. Al toegevoegd als kind)";
          case CHILD_OF:
            return getNameWithAge(rp) + " (NIET MOGELIJK. Al toegevoegd als ouder)";
          case PARTNER_OF:
            return getNameWithAge(rp) + " (NIET MOGELIJK. Al toegevoegd als partner)";
          case NO_LEGAL_PARENTS:
          case ONLY_1_LEGAL_PARENT:
          case UNKNOWN:
          case NOT_SET:
            break;
        }
      }
    }
    return getNameWithAge(rp);
  }

  private void setRelatives() {
    FieldValue person = (FieldValue) getField(F_PERSON).getValue();
    FieldValue relationValue = (FieldValue) getField(F_RELATIONSHIP_TYPE).getValue();
    FieldValue relatedPerson = (FieldValue) getField(F_RELATED_PERSON).getValue();

    if (relationValue != null) {
      RelationType relationType = (RelationType) relationValue.getValue();
      if (relationType == null || relationType.isRelated()) {
        getField(F_RELATED_PERSON).setVisible(true);
        getField(F_NO_RELATED_PERSON).setVisible(false);
      } else {
        getField(F_RELATED_PERSON).setValue(null);
        getField(F_RELATED_PERSON).setVisible(false);
        getField(F_NO_RELATED_PERSON).setVisible(true);
      }
      repaint();
      relatedPersonChangeListener.onChange(relationType,
          person != null ? (DossierPersoon) person.getValue() : null,
          relatedPerson != null ? (DossierPersoon) relatedPerson.getValue() : null);
    }
  }

  public void setRelatedPersonChangeListener(RelatedPersonChangeListener relatedPersonChangeListener) {
    this.relatedPersonChangeListener = relatedPersonChangeListener;
  }

  public interface RelatedPersonChangeListener<T> {

    void onChange(RelationType relationType, DossierPersoon person, DossierPersoon relatedPerson);
  }
}
