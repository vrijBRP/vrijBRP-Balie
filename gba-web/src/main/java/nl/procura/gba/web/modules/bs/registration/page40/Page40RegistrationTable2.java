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

package nl.procura.gba.web.modules.bs.registration.page40;

import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.*;
import static nl.procura.gba.web.services.bs.algemeen.functies.BsDossierNaamgebruikUtils.getNameWithAge;
import static nl.procura.gba.web.services.bs.registration.RelationType.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.registration.RelationType;
import nl.procura.gba.web.services.bs.registration.relations.Relation;

public class Page40RegistrationTable2 extends Page40RegistrationTable {

  private List<DossierPersoon> people;
  private List<Relation>       relations;
  private boolean              showRegistrants;
  private boolean              showBrpPersons;
  private boolean              showNotBrpPersons;

  Page40RegistrationTable2() {
  }

  @Override
  public void setColumns() {

    setSelectable(false);
    setMultiSelect(false);

    addColumn("Nr.", 30);
    addColumn("Personen", 300).setUseHTML(true);
    addColumn("Ouders").setUseHTML(true);
    addColumn("Partners").setUseHTML(true);
    addColumn("Kinderen").setUseHTML(true);

    super.setColumns();
  }

  private String getRelations(DossierPersoon person, List<Relation> relations, RelationType... relationTypes) {
    List<String> list = new ArrayList<>();
    for (Relation relation : relations) {
      if (relation.getPerson().getCDossPers().equals(person.getCDossPers())) {
        for (RelationType type : relationTypes) {
          if (relation.getRelationType().is(type)) {
            if (type.is(ONLY_1_LEGAL_PARENT, NO_LEGAL_PARENTS)) {
              list.add(toItem(type.getOms()));
            } else {
              list.add(toItem(getNameWithAge(relation.getRelatedPerson())));
            }
          }
        }
      }
    }
    return StringUtils.join(list, "");
  }

  void update(boolean showRegistrants,
      boolean showBrpPersons,
      boolean showNotBrpPersons) {

    this.showRegistrants = showRegistrants;
    this.showBrpPersons = showBrpPersons;
    this.showNotBrpPersons = showNotBrpPersons;
    update();
  }

  void update(List<DossierPersoon> people,
      List<Relation> relations,
      boolean showRegistrants,
      boolean showBrpPersons,
      boolean showNotBrpPersons) {

    this.people = people;
    this.relations = relations;
    this.showRegistrants = showRegistrants;
    this.showBrpPersons = showBrpPersons;
    this.showNotBrpPersons = showNotBrpPersons;
    update();
  }

  void update() {
    clear();
    int nr = 0;
    for (DossierPersoon person : people) {
      DossierPersoonType dossierPersoonType = person.getDossierPersoonType();
      boolean isRegistrant = showRegistrants && dossierPersoonType.is(INSCHRIJVER);
      boolean isBrpPerson = showBrpPersons && dossierPersoonType.is(GERELATEERDE_BRP);
      boolean isNotBrpPerson = showNotBrpPersons && dossierPersoonType.is(GERELATEERDE_NIET_BRP);

      if (isRegistrant || isBrpPerson || isNotBrpPerson) {
        Record record = addRecord("");
        record.addValue(++nr);
        record.addValue(toRow(getNameWithAge(person)));
        record.addValue(toRow(getRelations(person, relations, CHILD_OF, ONLY_1_LEGAL_PARENT, NO_LEGAL_PARENTS)));
        record.addValue(toRow(getRelations(person, relations, PARTNER_OF)));
        record.addValue(toRow(getRelations(person, relations, PARENT_OF)));
      }
    }
    reloadRecords();
  }
}
