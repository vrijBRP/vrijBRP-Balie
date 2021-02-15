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

package nl.procura.gba.web.modules.zaken.tmv.page9;

import static nl.procura.standard.Globalfunctions.fil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.diensten.gba.ple.base.BasePLSet;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.persoonslijst.overig.grid.RecordElementCombo;

public class Page9TmvTable1 extends GbaTable {

  private List<RecordElementCombo> elementRecords = new ArrayList<>();
  private GBACat                   categorie      = GBACat.UNKNOWN;
  private BasePLSet                set            = new BasePLSet(GBACat.UNKNOWN, -1);

  public Page9TmvTable1() {

    setSelectable(true);
  }

  public void addElementRecord(RecordElementCombo recordElement) {

    if (!recordElement.getPleElement().isAuthentic()) {
      return;
    }

    getElementRecords().add(recordElement);
  }

  public GBACat getCategorie() {
    return categorie;
  }

  public void setCategorie(GBACat categorie) {
    this.categorie = categorie;
  }

  public List<RecordElementCombo> getElementRecords() {
    return elementRecords;
  }

  public void setElementRecords(List<RecordElementCombo> elementRecords) {
    this.elementRecords = elementRecords;
  }

  public BasePLSet getSet() {
    return set;
  }

  public void setSet(BasePLSet set) {
    this.set = set;
  }

  public boolean isAdded(RecordElementCombo r) {

    for (RecordElementCombo c : getElementRecords()) {
      if (isGroup(r, c)) {
        return true;
      }
    }

    return false;
  }

  public void removeElementRecord(RecordElementCombo recordElement) {

    getElementRecords().remove(recordElement);
  }

  @Override
  public void setColumns() {

    addColumn("Categorie", 90);
    addColumn("Groep", 90);
    addColumn("Element", 80);
    addColumn("Omschrijving", 300);
    addColumn("Set", 30);
    addColumn("Huidig");

    super.setColumns();
  }

  @Override
  public void setPageLength(int pageLength) {
    super.setPageLength(6);
  }

  @Override
  public void setRecords() {

    sort(elementRecords);

    for (RecordElementCombo recordElement : getElementRecords()) {

      Record r = super.addRecord(recordElement);
      String w = recordElement.getGbaElement().getValue().getDescr();

      r.addValue(recordElement.getGbaRecord().getCatType().getDescr());
      r.addValue(recordElement.getPleElement().getGroup().getDescr());
      r.addValue(recordElement.getPleElement().getElem().getCode());
      r.addValue(recordElement.getPleElement().getElem().getDescr());
      r.addValue(recordElement.getGbaSet().getExtIndex());
      r.addValue(fil(w) ? w : "-");
    }

    super.setRecords();
  }

  protected List<Record> getGroupRecords(Record sR) {

    List<Record> l = new ArrayList<>();

    for (Record r : getRecords()) {
      if (isGroup((RecordElementCombo) r.getObject(), (RecordElementCombo) sR.getObject())) {
        l.add(r);
      }
    }

    return l;
  }

  private boolean isGroup(RecordElementCombo r, RecordElementCombo c) {

    boolean isCat = c.getPleElement().getCat().equals(r.getPleElement().getCat());
    boolean isGroup = c.getPleElement().getGroup().equals(r.getPleElement().getGroup());
    boolean isSet = c.getGbaSet().getExtIndex() == r.getGbaSet().getExtIndex();

    return isCat && isGroup && isSet;
  }

  private void sort(List<RecordElementCombo> list) {

    list.sort(new Sorter());
  }

  private class Sorter implements Comparator<RecordElementCombo> {

    @Override
    public int compare(RecordElementCombo o1, RecordElementCombo o2) {

      int cat1 = o1.getPleElement().getCat().getCode();
      int cat2 = o2.getPleElement().getCat().getCode();

      int grp1 = o1.getPleElement().getGroup().getCode();
      int grp2 = o2.getPleElement().getGroup().getCode();

      int set1 = o1.getGbaSet().getExtIndex();
      int set2 = o2.getGbaSet().getExtIndex();

      if (cat1 > cat2) {
        return 1;
      }

      if (cat1 < cat2) {
        return -1;
      }

      if (grp1 > grp2) {
        return 1;
      }

      if (grp1 < grp2) {
        return -1;
      }

      if (set1 > set2) {
        return 1;
      }

      if (set1 < set2) {
        return -1;
      }

      return (o1.getGbaElement().getElemCode() > o2.getGbaElement().getElemCode()) ? 1 : -1;
    }
  }
}
