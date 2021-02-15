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

package nl.procura.diensten.gba.ple.base;

import static nl.procura.diensten.gba.ple.procura.utils.SortableObjectConverter.FORCE_NEW_SET;

import java.util.List;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.burgerzaken.gba.core.enums.GBAGroupElements;
import nl.procura.burgerzaken.gba.core.enums.GBARecStatus;
import nl.procura.diensten.gba.ple.procura.arguments.PLEDatasource;
import nl.procura.diensten.gba.ple.procura.utils.SortableObjectConverter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BasePLBuilder {

  private PLEResult    result = new PLEResult();
  private BasePLFilter filter = new BasePLFilter();

  private int lastCatCode = -1;
  private int lastIndex   = -1;

  public BasePLBuilder addNewPL(PLEDatasource datasource) {
    lastCatCode = -1;
    lastIndex = -1;
    BasePL basisPl = new BasePL();
    basisPl.getMetaInfo().put("databron", datasource.getCode());
    result.getBasePLs().add(basisPl);
    return this;
  }

  public BasePL getPL() {
    return result.getBasePLs().getLast().orElseThrow(() -> new RuntimeException("No PL SET"));
  }

  public BasePLRec addCat(GBACat cat, GBARecStatus status) {
    return addCat(cat, SortableObjectConverter.FORCE_NEW_SET, status);
  }

  public BasePLRec addCat(GBACat cat, int index, GBARecStatus status) {
    if (!getFilter().isAllowed(cat.getCode())) {
      throw new PLESkipException();
    }

    if (isNewCatType(cat.getCode())) {
      getPL().getCats().add(new BasePLCat(cat));
    }

    BasePLCat category = getCat(getPL());
    if (isNewSet(cat.getCode(), index, status)) {
      List<BasePLSet> sets = category.getSets();
      sets.add(new BasePLSet(category.getCatType(), index));
    }

    BasePLSet set = getLastSet(category);
    BasePLRec rec = new BasePLRec(cat, set, status);

    addAllElems(rec);
    set.getRecs().add(rec);

    lastCatCode = cat.getCode();
    lastIndex = index;
    return rec;
  }

  public BasePLElem setElem(GBAElem elem, BasePLValue value) {
    BasePLElem element = getElem(elem);
    if (getFilter().isAllowed(lastCatCode, elem.getCode())) {
      if (value != null) {
        element.setValue(value);
      }
    }

    return element;
  }

  public PLEResult getResult() {
    return result;
  }

  public void setResult(PLEResult zoekResultaat) {
    this.result = zoekResultaat;
  }

  public BasePLFilter getFilter() {
    return filter;
  }

  public void setFilter(BasePLFilter filter) {
    this.filter = filter;
  }

  public BasePLBuilder finishPL() {
    BasePLUtils.finishPl(this);
    return this;
  }

  private BasePLCat getCat(BasePL pl) {
    return pl.getCats().getLast().orElseThrow(() -> new RuntimeException("No categories"));
  }

  private BasePLSet getLastSet(BasePLCat cat) {
    return cat.getSets().getLast().orElseThrow(() -> new RuntimeException("No sets"));
  }

  private BasePLRec getLastRecord(BasePLSet set) {
    return set.getRecs().getLast().orElseThrow(() -> new RuntimeException("No records"));
  }

  private void addAllElems(BasePLRec record) {
    for (GBAGroupElements.GBAGroupElem e : GBAGroupElements.getByCat(record.getCatType().getCode())) {
      addElem(record, e.getElem());
    }
  }

  private BasePLElem addElem(BasePLRec record, GBAElem elem) {
    BasePLElem element = new BasePLElem(record.getCatType().getCode(), elem.getCode(), "");
    record.getElems().add(element);
    return element;
  }

  private BasePLElem getElem(GBAElem elem) {
    BasePLRec record = getLastRecord(getLastSet(getCat(getPL())));
    BasePLElem element = new BasePLElem(record.getCatType().getCode(), elem.getCode(), null);
    int index = record.getElems().indexOf(element);

    if (index < 0) {
      log.info("Unknown element: " + record.getCatType() + " = " + elem.getCode());
      return addElem(record, elem);
    }
    return record.getElems().get(index);
  }

  private boolean isNewCatType(int catCode) {
    return (lastCatCode != catCode);
  }

  /**
   * -2 is voor lok. afnemers indicaties.
   * Daarvoor moet voor elke regel een nieuw setje worden gemaakt
   */
  private boolean isNewSet(int catCode, int index, GBARecStatus status) {
    boolean isNewCat = (isNewCatType(catCode));
    boolean isNewActualSet = (index == FORCE_NEW_SET && status == GBARecStatus.CURRENT);
    boolean isNewIndex = (index != lastIndex);
    return (isNewCat || isNewActualSet || isNewIndex);
  }
}
