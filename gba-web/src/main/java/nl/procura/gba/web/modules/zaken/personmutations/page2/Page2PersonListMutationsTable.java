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

package nl.procura.gba.web.modules.zaken.personmutations.page2;

import java.util.Comparator;

import org.apache.commons.lang3.builder.CompareToBuilder;

import com.vaadin.ui.Embedded;

import nl.procura.diensten.gba.ple.base.BasePL;
import nl.procura.diensten.gba.ple.base.BasePLElem;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.gba.web.modules.zaken.personmutations.AbstractPersonMutationsTable;
import nl.procura.gba.web.services.beheer.personmutations.PersonListActionType;

public class Page2PersonListMutationsTable extends AbstractPersonMutationsTable {

  private final PersonListMutElems elementRecords = new PersonListMutElems();

  public Page2PersonListMutationsTable() {
    setSelectable(false);
    setClickable(false);
  }

  @Override
  public void setColumns() {
    addColumn("Groep", 160).setUseHTML(true);
    addColumn("Element", 400);
    addColumn("Admin.", 45).setClassType(Embedded.class);
    addColumn("Huidige waarde");

    super.setColumns();
  }

  @Override
  public void setPageLength(int pageLength) {
    super.setPageLength(6);
  }

  @Override
  public void setRecords() {
    for (PersonListMutElem recordElement : elementRecords) {
      if (recordElement.getElemType().isNational()) {
        Record record = super.addRecord(recordElement);
        record.addValue(setRowHeight(recordElement.getGroup().getDescrAndCode()));
        record.addValue(recordElement.getElemType().getDescrAndCode());
        record.addValue(getCheckbox(recordElement.getElemType().isAdmin()));
        record.addValue(recordElement.getCurrentValue().getDescr());
      }
    }

    super.setRecords();
  }

  public void update(PersonListActionType action, BasePL pl, BasePLRec record) {
    this.elementRecords.clear();
    for (BasePLElem gbaElement : record.getElems()) {
      PersonListMutElem mutElem = new PersonListMutElem(pl, record, gbaElement, action);
      if (mutElem.isVisible()) {
        elementRecords.add(mutElem);
      }
    }
    sort(elementRecords);
    init();
  }

  public PersonListMutElems getElementRecords() {
    return elementRecords;
  }

  private void sort(PersonListMutElems list) {
    list.sort(new Sorter());
  }

  private static class Sorter implements Comparator<PersonListMutElem> {

    @Override
    public int compare(PersonListMutElem o1, PersonListMutElem o2) {

      int cat1 = o1.getTypes().getCat().getCode();
      int cat2 = o2.getTypes().getCat().getCode();

      int grp1 = o1.getTypes().getGroup().getCode();
      int grp2 = o2.getTypes().getGroup().getCode();

      int set1 = o1.getSet().getExtIndex();
      int set2 = o2.getSet().getExtIndex();

      int elem1 = o1.getElem().getElemCode();
      int elem2 = o2.getElem().getElemCode();

      return new CompareToBuilder()
          .append(cat1, cat2)
          .append(grp1, grp2)
          .append(set1, set2)
          .append(elem1, elem2)
          .toComparison();
    }
  }
}
