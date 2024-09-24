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

package nl.procura.gba.web.modules.bs.huwelijk.page75;

import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.WARNING;

import com.vaadin.ui.Button;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.modules.bs.common.pages.printpage.BsPrintPage;
import nl.procura.gba.web.services.bs.huwelijk.DossierHuwelijk;
import nl.procura.gba.web.services.zaken.documenten.DocumentType;
import nl.procura.standard.ProcuraDate;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.vaadin.component.dialog.ConfirmDialog;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

/**
 * Huwelijk voornemen
 */
public class Page65Huwelijk extends BsPrintPage<DossierHuwelijk> {

  protected boolean           daysConfirm;
  private Page65HuwelijkForm1 form = null;

  public Page65Huwelijk() {
    super("Huwelijk/GPS - afdrukken", "Afdrukken documenten voornemen huwelijk/GPS");
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

    long dVoornemen = new DateTime(form.getBean().getDatumVoornemen()).getLongDate();
    long dVerbind = getZaakDossier().getDatumVerbintenis().getLongDate();

    if (!checkVoornemen(button, keyCode, dVoornemen, dVerbind)) {
      return;
    }

    getZaakDossier().setDatumVoornemen(new DateTime(form.getBean().getDatumVoornemen()));
    getApplication().getServices().getHuwelijkService().save(getDossier());

    super.handleEvent(button, keyCode);
  }

  @Override
  public void onNextPage() {

    long dVoornemen = new DateTime(form.getBean().getDatumVoornemen()).getLongDate();

    if (dVoornemen <= 0) {
      throw new ProException(WARNING, "Vul de datum voornemen in");
    }

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

    form = new Page65HuwelijkForm1(getZaakDossier());

    addComponent(form);

    super.setButtons();
  }

  @Override
  protected DocumentType[] getDocumentTypes() {
    return new DocumentType[]{ DocumentType.HUWELIJK_VOORBEREIDING };
  }

  @Override
  protected String getInfo() {
    return "";
  }

  private boolean checkVoornemen(final Button button, final int keyCode, long dVoornemen, long dVerbind) {

    if (dVoornemen > 0) {

      int diff = new ProcuraDate(astr(dVoornemen)).diffInDays(astr(dVerbind));

      if (diff <= 0) {
        throw new ProException(WARNING, "De datum verbintenis kan niet op of vóór de datum voornemen liggen");
      }

      if (diff > 365) {
        throw new ProException(WARNING,
            "Er zit meer dan een jaar tussen de datum voornemen en de datum verbintenis");
      }

      if (!daysConfirm && (diff > 0 && diff < 14)) {

        getWindow().addWindow(new ConfirmDialog(
            "Er zit minder dan 14 dagen tussen de datum voornemen en datum verbintenis.<hr/>Is dit correct?") {

          @Override
          public void buttonYes() {

            closeWindow();

            daysConfirm = true;

            Page65Huwelijk.this.handleEvent(button, keyCode);
          }
        });

        return false;
      }
    }

    return true;
  }
}
