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

package nl.procura.gba.web.modules.zaken.personmutations.page4;

import java.util.List;

import nl.procura.gba.common.MiscUtils;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.zaken.personmutations.relatedcategories.PersonListRelationMutation;

class Page4PersonRelationMutationsTable extends GbaTable {

  private final List<PersonListRelationMutation> list;

  public Page4PersonRelationMutationsTable(List<PersonListRelationMutation> list) {
    this.list = list;
  }

  @Override
  public void setColumns() {
    addColumn("Relatie", 130);
    addColumn("Naam", 200);
    addColumn("Woongemeente", 160);
    addColumn("Te wijzigen categorie", 250);
    addColumn("Opmerking").setUseHTML(true);

    super.setColumns();
  }

  @Override
  public void setRecords() {
    try {
      for (PersonListRelationMutation mutation : list) {
        Record record = addRecord(mutation);
        record.addValue(mutation.getRelation());
        record.addValue(mutation.getName());
        record.addValue(mutation.getMunicipality());
        record.addValue(mutation.getChange());
        record.addValue(MiscUtils.setClass(false, mutation.getRemark()));
      }
    } catch (Exception e) {
      getApplication().handleException(getWindow(), e);
    }
  }
}
