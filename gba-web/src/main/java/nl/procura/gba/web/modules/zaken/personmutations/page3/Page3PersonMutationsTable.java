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

package nl.procura.gba.web.modules.zaken.personmutations.page3;

import com.vaadin.ui.Component;

import nl.procura.gba.web.modules.zaken.personmutations.AbstractPersonMutationsTable;
import nl.procura.gba.web.modules.zaken.personmutations.page2.PersonListMutElem;
import nl.procura.gba.web.modules.zaken.personmutations.page2.PersonListMutElems;

class Page3PersonMutationsTable extends AbstractPersonMutationsTable {

  private final PersonListMutElems list;

  public Page3PersonMutationsTable(PersonListMutElems list) {
    this.list = list;
  }

  @Override
  public void setColumns() {
    addStyleName("tmv_table");

    addColumn("Groep", 160).setUseHTML(true);
    addColumn("Element", 400);
    addColumn("Admin.", 45).setClassType(Component.class);
    addColumn("Huidige waarde", 330);
    addColumn("Nieuwe waarde").setClassType(Component.class);

    super.setColumns();
  }

  @Override
  public void setRecords() {
    try {
      if (getRecords().isEmpty()) {
        for (PersonListMutElem mutElem : list) {
          if (isShowElem(mutElem)) {
            Record record = addRecord(mutElem);
            record.addValue(setRowHeight(mutElem.getTypes().getGroup().getDescrAndCode()));
            record.addValue(mutElem.getElemType().getDescrAndCode());
            record.addValue(getCheckbox(mutElem.getElemType().isAdmin()));
            record.addValue(mutElem.getCurrentValue().getDescr());
            record.addValue(mutElem.getField());
          }
        }
      }
    } catch (Exception e) {
      getApplication().handleException(getWindow(), e);
    }
  }
}
