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

package nl.procura.gba.web.modules.bs.onderzoek.page60;

import static nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakregisterNavigator.navigatoTo;
import static nl.procura.gba.web.services.bs.onderzoek.enums.BetrokkeneType.*;
import static nl.procura.standard.Globalfunctions.along;
import static nl.procura.standard.Globalfunctions.aval;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.ui.Window;

import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.web.common.misc.Landelijk;
import nl.procura.gba.web.common.tables.GbaTables;
import nl.procura.gba.web.modules.bs.common.pages.persoonpage.BsStatusForm;
import nl.procura.gba.web.modules.bs.onderzoek.BsPageOnderzoek;
import nl.procura.gba.web.modules.bs.onderzoek.overzicht.OnderzoekOverzichtLayout;
import nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.onderzoek.DossierOnderzoek;
import nl.procura.gba.web.services.bs.onderzoek.OnderzoekService;
import nl.procura.gba.web.services.bs.onderzoek.enums.BetrokkeneType;
import nl.procura.gba.web.services.gba.ple.relatieLijst.AangifteSoort;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.status.ZaakStatusService;
import nl.procura.gba.web.services.zaken.algemeen.zaakrelaties.ZaakRelatie;
import nl.procura.gba.web.services.zaken.algemeen.zaakrelaties.ZaakRelatieService;
import nl.procura.gba.web.services.zaken.verhuizing.*;
import nl.procura.vaadin.component.dialog.ConfirmDialog;
import nl.procura.vaadin.component.dialog.OkDialog;
import nl.procura.vaadin.component.field.fieldvalues.AnrFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page60Onderzoek extends BsPageOnderzoek {

  public Page60Onderzoek() {
    super("Onderzoek - overzicht");
  }

  @Override
  public void event(PageEvent event) {

    try {

      if (event.isEvent(InitPage.class)) {

        buttonNext.setCaption("Proces voltooien (F2)");
        addButton(buttonPrev);
        addButton(buttonNext);

        addComponent(new BsStatusForm(getDossier()));
        setInfo("Controleer de gegevens. Druk op Volgende (F2) om verder te gaan.");
        addComponent(new OnderzoekOverzichtLayout(getZaakDossier()));
      }
    } finally {
      super.event(event);
    }
  }

  @Override
  public void onNextPage() {

    OnderzoekService service = getApplication().getServices().getOnderzoekService();
    ZaakStatusService statussen = getApplication().getServices().getZaakStatusService();

    if (getDossier().isStatus(ZaakStatusType.INBEHANDELING)) {
      statussen.updateStatus(getDossier(), ZaakStatusType.VERWERKT, "");
    }

    service.save(getDossier());

    checkVerhuizing(() -> navigatoTo(getDossier(), this, true));

    super.onNextPage();
  }

  @Override
  public void onPreviousPage() {
    goToPreviousProces();
  }

  private void checkVerhuizing(Runnable runnable) {
    ZaakRelatieService zaakRelatieService = getServices().getZaakRelatieService();
    Zaak verhuizing = zaakRelatieService.getGerelateerdeZaakByType(getDossier(), ZaakType.VERHUIZING, false);
    if (verhuizing == null && getZaakDossier().getResultaatOnderzoekBetrokkene().is(BINNEN, EMIGRATIE, NAAR_ONBEKEND)) {
      if (StringUtils.isBlank(getZaakDossier().getAanschrDatumEindVoornemen().toString())) {
        OkDialog window = new OkDialog("Er wordt geen verhuiszaak aangemaakt omdat " +
            "datum voornemen niet is gevuld.", 350);
        window.addListener((Window.CloseListener) closeEvent -> runnable.run());
        getApplication().getParentWindow().addWindow(window);
        return;

      } else {
        ConfirmDialog window = new ConfirmDialog("Wilt u een verhuiszaak maken op basis van dit onderzoek?", 400) {

          @Override
          public void buttonYes() {
            addVerhuizing(getZaakDossier().getResultaatOnderzoekBetrokkene());
            super.buttonYes();
          }
        };

        window.addListener((Window.CloseListener) closeEvent -> runnable.run());
        getApplication().getParentWindow().addWindow(window);
        return;
      }
    }
    runnable.run();
  }

  private Zaak addVerhuizing(BetrokkeneType betrokkeneType) {
    VerhuizingService service = getServices().getVerhuizingService();
    VerhuisAanvraag va = (VerhuisAanvraag) service.getNewZaak();

    switch (betrokkeneType) {
      case BINNEN:
        va.setTypeVerhuizing(VerhuisType.BINNENGEMEENTELIJK);
        toNieuwBinnenAdres(va, getZaakDossier());
        break;
      case EMIGRATIE:
        va.setTypeVerhuizing(VerhuisType.EMIGRATIE);
        toEmigratie(va, getZaakDossier());
        break;
      case NAAR_ONBEKEND:
        va.setTypeVerhuizing(VerhuisType.EMIGRATIE);
        toOnbekend(va);
        break;
    }

    VerhuisAangever aangever = new VerhuisAangever(va);
    aangever.setAnummer(new AnrFieldValue(-1L));
    aangever.setBurgerServiceNummer(new BsnFieldValue(-1L));
    aangever.setAmbtshalve(true);
    va.setAangever(aangever);

    // Persoon toevoegen
    for (DossierPersoon persoon : getDossier().getPersonen(DossierPersoonType.BETROKKENE)) {
      VerhuisPersoon vp = new VerhuisPersoon();
      vp.setAangifte(AangifteSoort.AMBTSHALVE);
      vp.setAnummer(new AnrFieldValue(-1L));
      vp.setBurgerServiceNummer(persoon.getBurgerServiceNummer());
      vp.setGemeenteHerkomst(persoon.getWoongemeente());
      va.getPersonen().add(vp);
    }

    // Zaak-ID zaaksysteem
    getApplication().getServices().getZaakIdentificatieService().getDmsZaakId(va);

    service.save(va);

    // Link onderzoek aan verhuizing
    ZaakRelatie relatie = new ZaakRelatie();
    relatie.setZaakId(getDossier().getZaakId());
    relatie.setZaakType(ZaakType.ONDERZOEK);
    relatie.setGerelateerdZaakId(va.getZaakId());
    relatie.setGerelateerdZaakType(ZaakType.VERHUIZING);
    getServices().getZaakRelatieService().save(relatie);

    return va;
  }

  private void toNieuwBinnenAdres(VerhuisAanvraag va, DossierOnderzoek onderzoek) {
    va.setDatumIngang(getZaakDossier().getAanschrDatumInVoornemen());
    VerhuisAanvraagAdres nieuwAdres = va.getNieuwAdres();
    nieuwAdres.setFunctieAdres(FunctieAdres.WOONADRES);
    nieuwAdres.setHnr(along(onderzoek.getResultaatHnr()));
    nieuwAdres.setHnrL(onderzoek.getResultaatHnrL());
    nieuwAdres.setHnrT(onderzoek.getResultaatHnrT());
    nieuwAdres.setHnrA("");
    nieuwAdres.setPc(new FieldValue(onderzoek.getResultaatPc()));
    nieuwAdres.setStraat(onderzoek.getResultaatAdres());
    nieuwAdres.setWoonplaats(onderzoek.getResultaatPlaats());
    nieuwAdres.setGemeente(onderzoek.getResultaatGemeente());
    nieuwAdres.setAantalPersonen(aval(onderzoek.getResultaatAantalPersonen().getBigDecimalValue()));
  }

  private void toEmigratie(VerhuisAanvraag va, DossierOnderzoek onderzoek) {
    va.setDatumIngang(getZaakDossier().getAanschrDatumInVoornemen());
    VerhuisEmigratie emigratie = va.getEmigratie();
    emigratie.setDatumVertrek(getZaakDossier().getAanschrDatumInVoornemen());
    emigratie.setAdres1(onderzoek.getResultaatBuitenl1());
    emigratie.setAdres2(onderzoek.getResultaatBuitenl2());
    emigratie.setAdres3(onderzoek.getResultaatBuitenl3());
    emigratie.setDuur("Onbekend");
    emigratie.setLand(onderzoek.getResultaatLand());
  }

  private void toOnbekend(VerhuisAanvraag va) {
    va.setDatumIngang(getZaakDossier().getAanschrDatumInVoornemen());
    VerhuisEmigratie emigratie = va.getEmigratie();
    emigratie.setDatumVertrek(getZaakDossier().getAanschrDatumInVoornemen());
    emigratie.setDuur("Onbekend");
    emigratie.setLand(GbaTables.LAND.get(Landelijk.ONBEKEND));
  }
}
