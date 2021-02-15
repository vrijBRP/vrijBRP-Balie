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

package nl.procura.gba.web.modules.bs.huwelijk.page90;

import static com.vaadin.event.ShortcutAction.KeyCode.F1;
import static nl.procura.gba.web.modules.bs.huwelijk.page90.Page90HuwelijkBean1.AMBTENAAR3;
import static nl.procura.gba.web.modules.bs.huwelijk.page90.Page90HuwelijkBean1.KEUZES;
import static nl.procura.standard.exceptions.ProExceptionSeverity.WARNING;

import com.vaadin.ui.Button;

import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.web.modules.bs.common.pages.printpage.BsPrintPage;
import nl.procura.gba.web.modules.bs.huwelijk.page30.ambtenaren.HuwelijkAmbtenarenWindow;
import nl.procura.gba.web.modules.bs.huwelijk.page35.Page35Huwelijk;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakregisterNavigator;
import nl.procura.gba.web.services.bs.huwelijk.DossierHuwelijk;
import nl.procura.gba.web.services.bs.huwelijk.HuwelijkService;
import nl.procura.gba.web.services.zaken.algemeen.status.ZaakStatusService;
import nl.procura.gba.web.services.zaken.documenten.DocumentType;
import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.component.layout.page.pageEvents.AfterReturn;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

/**
 * Huwelijk
 */
public class Page90Huwelijk extends BsPrintPage<DossierHuwelijk> {

  private Form1 form1 = null;

  public Page90Huwelijk() {
    super("Huwelijk/GPS - afdrukken", "Afdrukken documenten huwelijk/GPS");
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      buttonNext.setCaption("Proces voltooien (F2)");

      setModel(getZaakDossier());

      form1 = new Form1();

      addComponent(form1);
    }

    if (event.isEvent(InitPage.class, AfterReturn.class)) {
      form1.update();
    }

    super.event(event);
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if (!isKeyCode(button, keyCode, F1, buttonPrev)) {
      if (!getZaakDossier().getAmbtenaar3().isVolledig()) {
        throw new ProException(WARNING, "Selecteer eerst de definitieve ambtenaar");
      }
    }

    getApplication().getServices().getHuwelijkService().save(getDossier());

    super.handleEvent(button, keyCode);
  }

  @Override
  public void onNextPage() {

    HuwelijkService huwelijken = getApplication().getServices().getHuwelijkService();
    ZaakStatusService statussen = getApplication().getServices().getZaakStatusService();

    if (getDossier().isStatus(ZaakStatusType.INCOMPLEET)) {
      statussen.updateStatus(getDossier(), statussen.getInitieleStatus(getDossier()), "");
    }

    huwelijken.save(getDossier());

    ZaakregisterNavigator.navigatoTo(getDossier(), this, true);

    super.onNextPage();
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

    super.setButtons();
  }

  @Override
  protected DocumentType[] getDocumentTypes() {
    return new DocumentType[]{ DocumentType.HUWELIJK };
  }

  public class Form1 extends Page90HuwelijkForm1 {

    public Form1() {
      super(getZaakDossier());
      setOrder(KEUZES, AMBTENAAR3);
    }

    @Override
    protected void onClickAnders(Property property) {
      if (property.is(AMBTENAAR3)) {
        getNavigation().goToPage(new Page35Huwelijk(getZaakDossier().getAmbtenaar3()));
      }
    }

    @Override
    protected void onClickKies(Property property) {
      if (property.is(AMBTENAAR3)) {
        getWindow().addWindow(new HuwelijkAmbtenarenWindow(this, getZaakDossier().getAmbtenaar3()));
      }
    }
  }
}
