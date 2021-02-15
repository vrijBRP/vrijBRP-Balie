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

import com.vaadin.ui.Embedded;

import nl.procura.gba.web.modules.zaken.personmutations.AbstractPersonMutationsTable;
import nl.procura.gba.web.modules.zaken.personmutations.page2.PersonListMutElem;
import nl.procura.gba.web.modules.zaken.personmutations.page2.PersonListMutElems;

class Page4PersonMutationsTable extends AbstractPersonMutationsTable {

  private final PersonListMutElems list;

  public Page4PersonMutationsTable(PersonListMutElems list) {
    this.list = list;
  }

  @Override
  public void setColumns() {

    addColumn("Groep", 160).setUseHTML(true);
    addColumn("Element", 250).setUseHTML(true);
    addColumn("Admin.", 45).setClassType(Embedded.class);
    addColumn("Set", 20).setUseHTML(true);
    addColumn("Huidige waarde").setUseHTML(true);
    addColumn("Nieuwe waarde").setUseHTML(true);

    super.setColumns();
  }

  @Override
  public void setRecords() {
    try {
      for (PersonListMutElem mutelem : list) {
        if (isShowElem(mutelem)) {
          Record record = addRecord(mutelem);
          record.addValue(change(mutelem.isChanged(), mutelem.getTypes().getGroup().getDescrAndCode()));
          record.addValue(change(mutelem.isChanged(), mutelem.getElemType().getDescrAndCode()));
          record.addValue(getCheckbox(mutelem.getElemType().isAdmin()));
          record.addValue(change(mutelem.isChanged(), mutelem.getSetIndex()));
          record.addValue(change(mutelem.isChanged(),
              getDescr(mutelem.getElem().getElemCode(),
                  mutelem.getCurrentValue().getVal(),
                  mutelem.getCurrentValue().getDescr())));
          record.addValue(change(mutelem.isChanged(),
              getDescr(mutelem.getElem().getElemCode(),
                  mutelem.getNewValue(),
                  mutelem.getNewDescription())));
        }
      }
    } catch (Exception e) {
      getApplication().handleException(getWindow(), e);
    }
  }
}
