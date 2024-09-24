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

package nl.procura.gba.web.modules.persoonslijst.overig.tmv;

import static nl.procura.commons.core.exceptions.ProExceptionSeverity.WARNING;
import static nl.procura.commons.core.exceptions.ProExceptionType.NO_RESULTS;

import java.util.List;

import com.vaadin.ui.Button;

import nl.procura.diensten.gba.ple.base.BasePLElem;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.gba.web.modules.persoonslijst.overig.PlPage;
import nl.procura.gba.web.modules.persoonslijst.overig.grid.RecordElementCombo;
import nl.procura.gba.web.modules.zaken.tmv.page1.Page1Tmv;
import nl.procura.gba.web.modules.zaken.tmv.page9.Page9TmvLayout;
import nl.procura.commons.core.exceptions.ProException;

public class PlTmvPage extends PlPage {

  private final Button buttonAll  = new Button("Alles");
  private final Button buttonNone = new Button("Niets");

  private Page9TmvLayout layout    = null;
  private BasePLRec      gbaRecord = null;

  public PlTmvPage(String title, BasePLRec gbaRecord) {

    super(title + ": terugmelding");

    addButton(buttonPrev);
    addButton(buttonAll);
    addButton(buttonNone);
    addButton(buttonNext);
    getButtonLayout().setExpandRatio(buttonNext, 1f);
    getButtonLayout().setWidth("100%");

    setGbaRecord(gbaRecord);

    setLayout(new Page9TmvLayout());

    getLayout().getTable1().setCategorie(gbaRecord.getCatType());
    getLayout().getTable1().setSet(gbaRecord.getSet());

    for (BasePLElem gbaElement : getGbaRecord().getElems()) {

      getLayout().getTable1().addElementRecord(new RecordElementCombo(getGbaRecord(), gbaElement));
    }

    addExpandComponent(getLayout());
  }

  public BasePLRec getGbaRecord() {
    return gbaRecord;
  }

  public void setGbaRecord(BasePLRec gbaRecord) {
    this.gbaRecord = gbaRecord;
  }

  public Page9TmvLayout getLayout() {
    return layout;
  }

  public void setLayout(Page9TmvLayout layout) {
    this.layout = layout;
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if (button == buttonAll) {
      getLayout().selectAll(true);
    }

    if (button == buttonNone) {
      getLayout().selectAll(false);
    }

    super.handleEvent(button, keyCode);
  }

  @Override
  public void onNextPage() {

    List<RecordElementCombo> list = getLayout().getTable2().getElementRecords();

    if (list.isEmpty()) {
      throw new ProException(NO_RESULTS, WARNING, "Er zijn geen BRP-elementen geselecteerd");
    }

    getNavigation().goToPage(new Page1Tmv(list));

    super.onNextPage();
  }
}
