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

import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.NativeSelect;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.burgerzaken.gba.core.enums.GBAGroupElements;
import nl.procura.burgerzaken.gba.core.enums.GBARecStatus;
import nl.procura.diensten.gba.ple.base.BasePLElem;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.base.BasePLSet;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.modules.persoonslijst.overig.grid.RecordElementCombo;
import nl.procura.gba.web.modules.zaken.tmv.page9.componenten.CategorieContainer;
import nl.procura.gba.web.modules.zaken.tmv.page9.componenten.SetContainer;

public class Page9TmvForm1 extends GbaForm<Page9TmvBean> {

  private static final String CAT = "categorie";
  private static final String SET = "set";

  public Page9TmvForm1() {

    setCaption("Categorie");
    setColumnWidths(WIDTH_130, "");

    setOrder(CAT, SET);
    setBean(new Page9TmvBean());

    getCategorieField().addListener((ValueChangeListener) this);
    getSetField().addListener((ValueChangeListener) this);
  }

  @Override
  public void attach() {

    if (getCategorieField().getContainerDataSource().getItemIds().isEmpty()) {
      getCategorieField().setContainerDataSource(new CategorieContainer());
      doChangeCategory();
    }

    getField(CAT).focus();

    super.attach();
  }

  public Page9TmvTable1 getTable1() {
    return ((Page9Tmv) getParent()).getLayout().getTable1();
  }

  public Page9TmvTable1 getTable2() {
    return ((Page9Tmv) getParent()).getLayout().getTable2();
  }

  @Override
  public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {

    if (event.getProperty() == getField(CAT)) {
      doChangeCategory();
      doChangeSet();
    }

    if (event.getProperty() == getField(SET)) {
      doChangeSet();
    }

    repaint();
  }

  private void addEmptySet(List<RecordElementCombo> list, GBACat cat, BasePLSet gbaSet) {

    for (GBAGroupElements.GBAGroupElem p : GBAGroupElements.getByCat(cat.getCode())) {

      BasePLRec gbaRecord = new BasePLRec(cat, gbaSet, GBARecStatus.CURRENT);
      BasePLElem gbaElement = new BasePLElem(p.getCat().getCode(), p.getElem().getCode(), "");

      list.add(new RecordElementCombo(gbaRecord, gbaElement));
    }
  }

  /**
   * Functies wordt aangeroepen als veld "CAT" wordt aangepast
   */
  private void doChangeCategory() {

    BasePLExt pl = getApplication().getServices().getPersonenWsService().getHuidige();
    getSetField().setContainerDataSource(
        new SetContainer(pl.getCat((GBACat) getCategorieField().getValue())));
  }

  /**
   * Functies wordt aangeroepen als veld "SET" wordt aangepast
   */
  private void doChangeSet() {

    BasePLExt pl = getApplication().getServices().getPersonenWsService().getHuidige();
    GBACat cat = (GBACat) getCategorieField().getValue();
    BasePLSet set = (BasePLSet) getSetField().getValue();

    if (cat == null) {
      return;
    }

    List<RecordElementCombo> list = new ArrayList<>();

    getTable1().setCategorie(cat);
    getTable1().setSet(set);
    getTable1().getRecords().clear();

    List<BasePLSet> sets = pl.getCat(cat).getSets();

    if (set != null) {
      if (sets.isEmpty()) {
        addEmptySet(list, cat, set);
      } else {
        boolean hasSets = false;
        for (BasePLSet gbaSet : sets) {
          if (gbaSet.getExtIndex() == set.getExtIndex()) {
            hasSets = true;
            BasePLRec gbaRecord = gbaSet.getLatestRec();
            for (BasePLElem gbaElement : gbaRecord.getElems()) {
              list.add(new RecordElementCombo(gbaRecord, gbaElement));
            }
          }
        }

        if (!hasSets) {
          addEmptySet(list, cat, set);
        }
      }
    }

    getTable1().getElementRecords().clear();

    for (RecordElementCombo combo : list) {
      if (!getTable2().isAdded(combo)) {
        getTable1().addElementRecord(combo);
      }
    }

    getTable1().init();
  }

  private NativeSelect getCategorieField() {
    return (NativeSelect) getField(CAT);
  }

  private NativeSelect getSetField() {
    return (NativeSelect) getField(SET);
  }
}
