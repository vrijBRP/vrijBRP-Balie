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

package nl.procura.gba.web.modules.zaken.personmutations.page5;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.Embedded;

import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.burgerzaken.gba.core.enums.GBAGroupElements;
import nl.procura.gba.jpa.personen.db.PlMut;
import nl.procura.gba.jpa.personen.db.PlMutRec;
import nl.procura.gba.web.modules.zaken.personmutations.AbstractPersonMutationsTable;
import nl.procura.gba.web.modules.zaken.personmutations.PersonListMutationsCheckBoxType;
import nl.procura.vaadin.component.table.indexed.IndexedTable;

public class Page5PersonListMutationsTable extends AbstractPersonMutationsTable {

  private final PlMut mutation;

  public Page5PersonListMutationsTable(PlMut mutation) {
    this.mutation = mutation;
  }

  @Override
  public void setColumns() {

    addColumn("Groep", 160).setUseHTML(true);
    addColumn("Element", 250).setUseHTML(true);
    addColumn("Admin.", 45).setClassType(Embedded.class);
    addColumn("Vorige waarde").setUseHTML(true);
    addColumn("Nieuwe waarde").setUseHTML(true);

    super.setColumns();
  }

  @Override
  public void setRecords() {
    try {
      List<PlMutRec> sortedRecords = new ArrayList<>(mutation.getPlMutRecs());
      sortedRecords.sort(new PlMutRecSorter());

      for (PlMutRec mutRec : sortedRecords) {
        if (PersonListMutationsCheckBoxType.CHANGED != getFilter() || mutRec.getChanged().intValue() > 0) {
          int cat = mutation.getCat().intValue();
          int elem = mutRec.getId().getElem();
          boolean changed = mutRec.getChanged().intValue() > 0;

          IndexedTable.Record record = addRecord(mutRec);
          record.addValue(change(changed, GBAGroupElements.getByCat(cat, elem).getGroup().getDescrAndCode()));
          record.addValue(change(changed, GBAGroupElements.getByCat(cat, elem).getElem().getDescrAndCode()));
          record.addValue(getCheckbox(GBAElem.getByCode(elem).isAdmin()));
          record.addValue(change(changed, getDescr(mutRec.getId().getElem(),
              mutRec.getValOrg(), mutRec.getValOrgDescr())));
          record.addValue(change(changed, getDescr(mutRec.getId().getElem(),
              mutRec.getValNew(), mutRec.getValNewDescr())));
        }
      }
    } catch (Exception e) {
      getApplication().handleException(getWindow(), e);
    }
  }
}
