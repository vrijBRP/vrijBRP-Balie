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

package nl.procura.gbaws.web.vaadin.module.auth.profile.page3;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.builder.CompareToBuilder;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.burgerzaken.gba.core.enums.GBAGroupElements;
import nl.procura.burgerzaken.gba.core.enums.GBARecStatus;
import nl.procura.gba.common.MiscUtils;
import nl.procura.gbaws.db.handlers.ProfileDao.ElementWrapper;
import nl.procura.gbaws.db.wrappers.ProfileWrapper.ProfielElement;
import nl.procura.gbaws.web.vaadin.layouts.tables.PersonenWsTable;
import nl.procura.gbaws.web.vaadin.module.auth.profile.page1.ElementsProfile;

@SuppressWarnings("serial")
public abstract class Page2AuthElementTable extends PersonenWsTable {

  private final ElementsProfile ep;
  private GBACat                categorie = null;
  private GBARecStatus          status    = null;
  private Boolean               gekoppeld = null;

  public Page2AuthElementTable(ElementsProfile ep) {
    this.ep = ep;
  }

  @Override
  public void setColumns() {

    setSelectable(true);
    setMultiSelect(true);

    addColumn("Status", 150).setUseHTML(true);
    addColumn("Categorie", 250);
    addColumn("Code", 50);
    addColumn("Naam");

    super.setColumns();
  }

  @Override
  public void setRecords() {

    List<ProfielElement> elementen = ep.getProfile().getElementen(ep.getDatabaseType().getCode(),
        ep.getRefDatabase());

    List<GBAGroupElements.GBAGroupElem> elements = GBAGroupElements.getAll();

    elements.sort(new Sorter());

    List<ElementWrapper> elems = new ArrayList<>();

    for (GBAGroupElements.GBAGroupElem et : elements) {

      if (categorie == null || categorie.getCode() == et.getCat().getCode()) {

        int catCode = et.getCat().getCode();
        int elemCode = et.getElem().getCode();
        boolean isGekoppeld = isGekoppeld(elementen, catCode, elemCode);

        elems.add(new ElementWrapper(isGekoppeld, catCode, elemCode));

        catCode = catCode + 50;
        isGekoppeld = isGekoppeld(elementen, catCode, elemCode);

        elems.add(new ElementWrapper(isGekoppeld, catCode, elemCode));
      }
    }

    for (ElementWrapper elem : elems) {

      if (categorie != null && GBACat.getByCode(elem.getCatCode()) != categorie) {
        continue;
      }

      if (status != null && status == GBARecStatus.CURRENT && elem.getCatCode() > 50) {
        continue;
      }

      if (status != null && status == GBARecStatus.HIST && elem.getCatCode() < 50) {
        continue;
      }

      if (gekoppeld != null && gekoppeld != elem.isGekoppeld()) {
        continue;
      }

      Record r = addRecord(elem);
      r.addValue(getColumn1(elem));
      r.addValue(getColumn2(elem));
      r.addValue(getColumn3(elem));
      r.addValue(getColumn4(elem));
    }

    super.setRecords();
  }

  @Override
  public void onDoubleClick(Record record) {

    ElementWrapper elem = record.getObject(ElementWrapper.class);

    koppel(elem);

    reload(asList(record));

    super.onDoubleClick(record);
  }

  public void reload(List<Record> records) {

    for (Record record : records) {

      ElementWrapper elem = record.getObject(ElementWrapper.class);

      setRecordValue(record, 0, getColumn1(elem));
      setRecordValue(record, 1, getColumn2(elem));
      setRecordValue(record, 2, getColumn3(elem));
    }
  }

  public GBACat getCategorie() {
    return categorie;
  }

  public void setCategorie(GBACat categorie) {
    this.categorie = categorie;
  }

  public GBARecStatus getStatus() {
    return status;
  }

  public void setStatus(GBARecStatus status) {
    this.status = status;
  }

  public Boolean getGekoppeld() {
    return gekoppeld;
  }

  public void setGekoppeld(Boolean gekoppeld) {
    this.gekoppeld = gekoppeld;
  }

  protected abstract void koppel(ElementWrapper elem);

  private String getColumn1(ElementWrapper elem) {

    if (elem.isGekoppeld()) {
      return MiscUtils.setClass("green", "Gekoppeld");
    }

    return MiscUtils.setClass("red", "Ontkoppeld");
  }

  private String getColumn2(ElementWrapper elem) {
    int code = elem.getCatCode();
    GBACat cat = GBACat.getByCode(code);
    String descr = cat.getDescr();
    if (code > 50) {
      descr += " (historie)";
    }
    return String.format("%02d", code) + ": " + descr;
  }

  private String getColumn3(ElementWrapper elem) {
    return String.format("%04d", elem.getElemCode());
  }

  private String getColumn4(ElementWrapper elem) {
    GBAGroupElements.GBAGroupElem element = GBAGroupElements.getByCat(elem.getCatCode(), elem.getElemCode());
    return element.getElem().getDescr();
  }

  private boolean isGekoppeld(List<ProfielElement> elementen, int catCode, int elemCode) {
    if (elementen != null) {
      return elementen.stream()
          .anyMatch(pe -> pe.getCode_cat() == catCode
              && pe.getCode_element() == elemCode);
    }
    return false;
  }

  public class Sorter implements Comparator<GBAGroupElements.GBAGroupElem> {

    @Override
    public int compare(GBAGroupElements.GBAGroupElem o1, GBAGroupElements.GBAGroupElem o2) {
      int c1 = o1.getCat().getCode();
      int c2 = o2.getCat().getCode();
      int e1 = o1.getElem().getCode();
      int e2 = o2.getElem().getCode();
      return new CompareToBuilder().append(c1, c2).append(e1, e2).build();
    }
  }
}
