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

package nl.procura.gba.web.modules.bs.registration.page40.relations.matching;

import static nl.procura.gba.web.modules.bs.registration.page40.relations.RelationBean.F_MATCHING_RELATIVE;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.ui.Button;
import com.vaadin.ui.Field;

import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.base.BasePLSet;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.extensions.Cat2OuderExt;
import nl.procura.diensten.gba.ple.extensions.Cat9KindExt;
import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.modules.bs.registration.page40.relations.RelationBean;
import nl.procura.gba.web.services.bs.algemeen.functies.BsPersoonUtils;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.registration.RelationType;
import nl.procura.standard.Globalfunctions;
import nl.procura.vaadin.component.container.ArrayListContainer;
import nl.procura.vaadin.component.field.ProNativeSelect;
import nl.procura.vaadin.component.layout.table.TableLayout;

class RelationsMatchForm extends GbaForm<RelationBean> {

  private CompareButton            compareButton = new CompareButton();
  private List<RelationsMatchInfo> relatives     = new ArrayList<>();

  RelationsMatchForm(RelationType type, DossierPersoon person, BasePLExt relatedPerson) {
    setOrder(F_MATCHING_RELATIVE);
    RelationBean bean = new RelationBean();
    setBean(bean);
    fillPersonField(type, person, relatedPerson);
  }

  public List<RelationsMatchInfo> getRelatives() {
    return relatives;
  }

  public Optional<RelationsMatchInfo> getMatch() {
    return relatives.stream()
        .filter(RelationsMatchInfo::isMatch)
        .findFirst();
  }

  @Override
  public void afterSetColumn(TableLayout.Column column, Field field, Property property) {
    if (property.is(F_MATCHING_RELATIVE)) {
      column.addComponent(compareButton);
      field.addListener((ValueChangeListener) event -> {
        RelationsMatchInfo fv = (RelationsMatchInfo) event.getProperty().getValue();
        compareButton.setEnabled(fv != null && fv.isApplicable());
      });
    }
    super.afterSetColumn(column, field, property);
  }

  private void fillPersonField(RelationType type, DossierPersoon person, BasePLExt relatedPerson) {
    ProNativeSelect relationField = getField(F_MATCHING_RELATIVE, ProNativeSelect.class);
    switch (type) {
      case PARENT_OF:
        relationField.setCaption("Ouder(s)");
        relationField.setRequiredError("Veld ouders(s) is verplicht");
        relatives = getParents(person, relatedPerson);
        break;
      case PARTNER_OF:
        relationField.setCaption("Partner(s)");
        relationField.setRequiredError("Veld partner(s) is verplicht");
        relatives = getPartners(person, relatedPerson);
        break;
      case CHILD_OF:
        relationField.setCaption("Kind(eren)");
        relationField.setRequiredError("Veld kind(eren) is verplicht");
        relatives = getChildren(person, relatedPerson);
        break;
      case UNKNOWN:
      default:
        break;
    }
    ArrayListContainer container = new ArrayListContainer(relatives);
    relationField.setDataSource(container);

    // Select the first record that has a match
    Optional<RelationsMatchInfo> firstMatch = getMatch();
    if (firstMatch.isPresent()) {
      relationField.setValue(firstMatch.get());
      compareButton.setEnabled(true);

    } else {
      RelationsMatchInfo newInfo = new RelationsMatchInfo()
          .setApplicable(false)
          .setLabel("Niet van toepassing. Leg nieuwe categorie aan.");
      container.addItem(newInfo);

      // No relative, just select this option
      if (getRelatives().size() == 0) {
        relationField.setValue(newInfo);
      }
    }

    relationField.setNullSelectionAllowed(container.size() > 1);
    repaint();
  }

  private List<RelationsMatchInfo> getPartners(DossierPersoon person, BasePLExt relatedPerson) {
    BasePLSet set = relatedPerson.getHuwelijk().getHuwelijkSet();
    BasePLRec rec = set.getLatestRec();
    return addRelativesRecord(person, rec);
  }

  private List<RelationsMatchInfo> getParents(DossierPersoon person, BasePLExt relatedPerson) {
    List<RelationsMatchInfo> infos = new ArrayList<>();
    List<Cat2OuderExt> sets = relatedPerson.getOuders().getOuders();
    sets.forEach(rec -> infos.addAll(addRelativesRecord(person, rec.getRecord())));
    return infos;
  }

  private List<RelationsMatchInfo> getChildren(DossierPersoon person, BasePLExt relatedPerson) {
    List<RelationsMatchInfo> infos = new ArrayList<>();
    List<Cat9KindExt> sets = relatedPerson.getKinderen().getKinderen();
    sets.forEach(rec -> infos.addAll(addRelativesRecord(person, rec.getRecord())));
    return infos;
  }

  private List<RelationsMatchInfo> addRelativesRecord(DossierPersoon person, BasePLRec rec) {
    List<RelationsMatchInfo> infos = new ArrayList<>();
    // Strip the "." from lastname. Parents has a required category
    String lastname = Globalfunctions.trim(rec.getElemVal(GBAElem.GESLACHTSNAAM).getVal());
    if (!rec.isIncorrect() && StringUtils.isNotBlank(lastname)) {
      infos.add(new RelationsMatchInfo(person, BsPersoonUtils.kopieDossierPersoon(rec, new DossierPersoon())));
    }
    return infos;
  }

  private class CompareButton extends Button {

    public CompareButton() {
      super("Toon");
      setEnabled(false);
      setWidth("70px");
      addListener((Button.ClickListener) event -> {
        RelationsMatchInfo info = (RelationsMatchInfo) getField(F_MATCHING_RELATIVE).getValue();
        ((GbaApplication) getApplication()).getParentWindow().addWindow(new RelationsMatchWindow(info));
      });
    }
  }
}
