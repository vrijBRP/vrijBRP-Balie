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

import static nl.procura.standard.exceptions.ProExceptionSeverity.WARNING;
import static nl.procura.standard.exceptions.ProExceptionType.NO_RESULTS;

import java.util.List;

import com.vaadin.ui.Button;

import nl.procura.gba.web.modules.persoonslijst.overig.grid.RecordElementCombo;
import nl.procura.gba.web.modules.zaken.common.ZakenPage;
import nl.procura.gba.web.modules.zaken.tmv.page1.Page1Tmv;
import nl.procura.standard.exceptions.ProException;

public class Page9Tmv extends ZakenPage {

  private final Button   buttonAll  = new Button("Alles");
  private final Button   buttonNone = new Button("Niets");
  private Page9TmvForm1  form       = new Page9TmvForm1();
  private Page9TmvLayout layout     = null;

  public Page9Tmv() {

    super("Terugmelding: nieuwe melding");

    addButton(buttonPrev);
    addButton(buttonAll);
    addButton(buttonNone);
    addButton(buttonNext);

    setLayout(new Page9TmvLayout());

    addComponent(getForm());
    addExpandComponent(getLayout());
  }

  public Page9TmvForm1 getForm() {
    return form;
  }

  public void setForm(Page9TmvForm1 form) {
    this.form = form;
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
      throw new ProException(NO_RESULTS, WARNING, "Er zijn geen BRP elementen geselecteerd");
    }

    getNavigation().goToPage(new Page1Tmv(list));

    super.onNextPage();
  }
}
