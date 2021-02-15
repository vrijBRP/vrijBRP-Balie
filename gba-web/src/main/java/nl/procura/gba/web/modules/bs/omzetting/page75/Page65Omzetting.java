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

package nl.procura.gba.web.modules.bs.omzetting.page75;

import com.vaadin.ui.Button;

import nl.procura.gba.web.modules.bs.common.pages.printpage.BsPrintPage;
import nl.procura.gba.web.services.bs.omzetting.DossierOmzetting;
import nl.procura.gba.web.services.zaken.documenten.DocumentType;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

/**
 * Omzetting voornemen
 */
public class Page65Omzetting extends BsPrintPage<DossierOmzetting> {

  protected boolean            daysConfirm;
  private Page65OmzettingForm1 form = null;

  public Page65Omzetting() {
    super("Omzetting GPS in huwelijk - afdrukken", "Afdrukken documenten voornemen huwelijk/GPS");
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      setModel(getZaakDossier());
    }

    super.event(event);
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    form.commit();

    getApplication().getServices().getOmzettingService().save(getDossier());

    super.handleEvent(button, keyCode);
  }

  @Override
  public void onNextPage() {
    goToNextProces();
  }

  @Override
  public void onPreviousPage() {
    goToPreviousProces();
  }

  @Override
  public void setButtons() {

    addButton(buttonPrev);
    addButton(getPrintButtons());
    addButton(buttonNext);

    form = new Page65OmzettingForm1(getZaakDossier());

    addComponent(form);

    super.setButtons();
  }

  @Override
  protected DocumentType[] getDocumentTypes() {
    return new DocumentType[]{ DocumentType.GPS_OMZETTING_VOORBEREIDING };
  }

  @Override
  protected String getInfo() {
    return "";
  }
}
