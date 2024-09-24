/*
 * Copyright 2024 - 2025 Procura B.V.
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

package nl.procura.gba.web.modules.hoofdmenu.gv.page1;

import static nl.procura.commons.core.exceptions.ProExceptionSeverity.WARNING;
import static nl.procura.gba.common.MiscUtils.setClass;
import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.BETROKKENE;
import static nl.procura.gba.web.services.zaken.algemeen.koppelenumeratie.KoppelEnumeratieType.TK_JA_VOORWAARDELIJK;
import static nl.procura.gba.web.services.zaken.algemeen.koppelenumeratie.KoppelEnumeratieType.TK_NEE;
import static nl.procura.standard.Globalfunctions.emp;

import java.util.ArrayList;

import com.vaadin.ui.Button;
import com.vaadin.ui.Window.CloseListener;

import nl.procura.commons.core.exceptions.ProException;
import nl.procura.diensten.gba.ple.procura.arguments.PLEDatasource;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.components.dialogs.ZaakConfiguratieDialog;
import nl.procura.gba.web.components.layouts.OptieLayout;
import nl.procura.gba.web.components.layouts.form.document.PrintRecord;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.beheer.documenten.page1.tab3.Tab3DocumentWindow;
import nl.procura.gba.web.modules.bs.common.pages.zoekpage.BsZoekWindow;
import nl.procura.gba.web.modules.hoofdmenu.gv.PageGvTemplate;
import nl.procura.gba.web.services.bs.algemeen.functies.BsPersoonUtils;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.zaken.algemeen.koppelenumeratie.KoppelEnumeratieType;
import nl.procura.gba.web.services.zaken.documenten.afnemers.DocumentAfnemer;
import nl.procura.gba.web.services.zaken.gv.GegevensVerstrekkingService;
import nl.procura.gba.web.services.zaken.gv.GvAanvraag;
import nl.procura.gba.web.services.zaken.gv.GvAanvraagProces;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page1Gv extends PageGvTemplate {

  private final Button buttonGba  = new Button("Zoek persoon");
  private final Button buttonZoek = new Button("Persoonslijst");

  private final OptieLayout optieLayout = new OptieLayout();
  private final Fieldset    fieldsetGv  = new Fieldset("Documenten gegevensverstrekking");
  private final Fieldset    fieldsetBa  = new Fieldset("Documenten belangenafweging");
  private Page1GvForm1      verzoekForm = null;
  private Page1GvForm2      adresForm   = null;
  private Page1GvForm3      procesForm  = null;
  private ZoekTable         zoekTable   = null;

  public Page1Gv() {
    this(new GvAanvraag());
  }

  public Page1Gv(GvAanvraag zaak) {
    super("Gegevensverstrekking - aanvraag");
    setBetrokkene(new DossierPersoon(BETROKKENE));
    BsPersoonUtils.kopieDossierPersoon(zaak.getBasisPersoon(), getBetrokkene());
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      addButton(buttonPreview);
      addButton(buttonPrint);
      addButton(buttonReset);

      setInfo("Zoek de persoon, leg de gegevens van het verzoek vast en druk het juiste document af.");

      zoekTable = new ZoekTable();

      optieLayout.getLeft().addComponent(zoekTable);
      optieLayout.getRight().addButton(buttonGba, this);
      optieLayout.getRight().addButton(buttonZoek, this);
      optieLayout.getRight().setWidth("130px");

      addComponent(optieLayout);

      setPrintTable(new PrintTable((document, isPreSelect) -> isPreSelect));

      verzoekForm = new Page1GvForm1(getApplication()) {

        @Override
        public void wijzigAfnemer(DocumentAfnemer da) {
          Page1Gv.this.wijzigAfnemer(da);
        }

        @Override
        public void wijzigToekenning(KoppelEnumeratieType grondslag, KoppelEnumeratieType toekenning) {
          wijzigVerzoek(grondslag, toekenning);
        }

        @Override
        protected void onButtonAfnemer() {

          getWindow().addWindow(new Tab3DocumentWindow() {

            @Override
            public void closeWindow() {
              verzoekForm.updateAfnemers();
              super.closeWindow();
            }
          });
        }
      };

      adresForm = new Page1GvForm2();
      procesForm = new Page1GvForm3("Procesinformatie") {

        @Override
        public void wijzigProcesActie(KoppelEnumeratieType procesActie) {

          KoppelEnumeratieType grondslag = verzoekForm.getBean().getGrondslag();
          KoppelEnumeratieType toekenning = (KoppelEnumeratieType) verzoekForm.getToekenningVeld().getValue();

          checkPrintTable(grondslag, toekenning, procesActie, null, isEigenGemeente(),
              isVerstrekkingsBeperking());
        }
      };

      addComponent(verzoekForm);
      addComponent(adresForm);
      addComponent(fieldsetGv);
      addExpandComponent(getPrintTable());

      reset();

      // Als betrokkene is gevuld dan tabel updaten,
      // anders deze zoeken
      if (getBetrokkene().isVolledig()) {
        zoekTable.update();

      } else {
        openZoekWindow();
      }
    }

    super.event(event);
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if (button == buttonGba) {
      openZoekWindow();

    } else if (button == buttonZoek) {
      final String anr = getBetrokkene().getAnummer().getStringValue();
      final String bsn = getBetrokkene().getBurgerServiceNummer().getStringValue();

      if (emp(anr) && emp(bsn)) {
        throw new ProException(WARNING, "Geen betrokkene geselecteerd.");
      }

      getApplication().goToPl(getParentWindow(), "", PLEDatasource.STANDAARD, anr, bsn);
    }

    super.handleEvent(button, keyCode);
  }

  @Override
  public void onNew() {

    setBetrokkene(new DossierPersoon(BETROKKENE));

    reset();

    super.onNew();
  }

  @Override
  protected void validatePrint(boolean isPreview) {

    GegevensVerstrekkingService gv = getServices().getGegevensverstrekkingService();

    verzoekForm.commit();

    if (adresForm.getParent() != null) {
      adresForm.commit();
    }

    if (procesForm.getParent() != null) {
      procesForm.commit();
    }

    Page1GvBean1 verzoekBean = verzoekForm.getBean();
    GvAanvraag zaakImpl = (GvAanvraag) gv.getNewZaak();

    // Verzoek
    zaakImpl.setDatumIngang(new DateTime(verzoekBean.getDatumOntvangst()));
    zaakImpl.setAnummer(getBetrokkene().getAnummer());
    zaakImpl.setBurgerServiceNummer(getBetrokkene().getBurgerServiceNummer());
    zaakImpl.setGrondslagType(verzoekBean.getGrondslag());
    zaakImpl.setAanvrager(verzoekBean.getAfnemer().getDocumentAfn());
    zaakImpl.setToekenningType(verzoekBean.getToekenning());
    zaakImpl.setToekenningMotivering(verzoekBean.getToekenningMotivering());

    // Adres
    Page1GvBean2 adresBean = adresForm.getBean();
    zaakImpl.setTerAttentieVanAanhef(adresBean.getTavAanhef());
    zaakImpl.setTavVoorl(adresBean.getTavVoorl());
    zaakImpl.setTavNaam(adresBean.getTavNaam());
    zaakImpl.setEmail(adresBean.getEmail());
    zaakImpl.setAdres(adresBean.getAdres());
    zaakImpl.setPostcode(adresBean.getPc());
    zaakImpl.setPlaats(adresBean.getPlaats());
    zaakImpl.setKenmerk(adresBean.getKenmerk());

    // Proces
    final GvAanvraagProces zaakProcesImpl = new GvAanvraagProces();

    if (procesForm.getParent() != null) {
      Page1GvBean3 procesBean = procesForm.getBean();
      zaakProcesImpl.setProcesActieType(procesBean.getActiesoort());
      zaakProcesImpl.setDatumEindeTermijn(new DateTime(procesBean.getDatumEindeTermijn()));
    }

    // Document toevoegen
    for (PrintRecord document : getPrintTable().getSelectedValues(PrintRecord.class)) {
      if (fieldsetGv.getParent() != null) {
        zaakImpl.setDocGv(document.getDocument());
      } else {
        zaakImpl.setDocBa(document.getDocument());
      }
    }

    // Update documentafnemer
    DocumentAfnemer afnemer = verzoekForm.getBean().getAfnemer();
    afnemer.setAdres(adresForm.getBean().getAdres());
    afnemer.setPostcode(adresForm.getBean().getPc());
    afnemer.setPlaats(adresForm.getBean().getPlaats());
    getServices().getDocumentService().save(afnemer);

    if (isPreview) {
      doPrint(zaakImpl, true);

    } else {
      ZaakConfiguratieDialog.of(getApplication(), zaakImpl, getServices(), () -> {
        doPrint(zaakImpl, false);
        getApplication().getServices().getZaakIdentificatieService().getDmsZaakId(zaakImpl);
        gv.save(zaakImpl);
        gv.saveProces(zaakImpl, zaakProcesImpl);
        successMessage("De gegevens zijn opgeslagen.");
      });
    }
  }

  private boolean isLandelijkeGrondslag(KoppelEnumeratieType gs) {
    return gs == KoppelEnumeratieType.GS_LANDELIJK;
  }

  private boolean isSprakeVanBelangenafweging(KoppelEnumeratieType tk) {
    return TK_JA_VOORWAARDELIJK.is(tk);
  }

  private void openZoekWindow() {

    BsZoekWindow zoekWindow = new BsZoekWindow(getBetrokkene(), new ArrayList<>());

    zoekWindow.addListener((CloseListener) e -> {

      zoekTable.update();

      wijzigAfnemer((DocumentAfnemer) verzoekForm.getAfnemerVeld().getValue());
    });

    getParentWindow().addWindow(zoekWindow);
  }

  private void reset() {

    zoekTable.reset();
    verzoekForm.reset();
    adresForm.reset();
    procesForm.reset();

    getPrintTable().reset();
    wijzigVerzoek(null, null);
  }

  private void setFieldset(KoppelEnumeratieType gs, KoppelEnumeratieType tk) {

    if (isLandelijkeGrondslag(gs) && isSprakeVanBelangenafweging(tk)) {
      removeComponent(fieldsetGv);
      addComponent(fieldsetBa, getComponentIndex(getPrintTable()));
    } else {
      removeComponent(fieldsetBa);
      addComponent(fieldsetGv, getComponentIndex(getPrintTable()));
    }
  }

  private void wijzigAfnemer(DocumentAfnemer da) {

    verzoekForm.setGrondslag(da);
    verzoekForm.setToekennen(isVerstrekkingsBeperking(), da);
    adresForm.setAfnemer(da);
  }

  private void wijzigVerzoek(KoppelEnumeratieType grondslag, KoppelEnumeratieType toekenning) {

    verzoekForm.enableGrondslag(toekenning != null);
    verzoekForm.enableMotiveringToekenning(toekenning != null && toekenning.is(TK_NEE));

    setFieldset(grondslag, toekenning);

    verzoekForm.repaint();

    removeComponent(procesForm);

    procesForm.setKenbaarMaken(false);

    if (isLandelijkeGrondslag(grondslag)) {

      if (isSprakeVanBelangenafweging(toekenning)) {

        addComponent(procesForm, getComponentIndex(adresForm) + 1);

        procesForm.setKenbaarMaken(true);
      }
    }

    procesForm.repaint();

    KoppelEnumeratieType procesActie = null;

    if (procesForm.getParent() != null) {
      procesActie = (KoppelEnumeratieType) procesForm.getProcesactie().getValue();
    }

    checkPrintTable(grondslag, toekenning, procesActie, null, isEigenGemeente(), isVerstrekkingsBeperking());
  }

  public class ZoekTable extends GbaTable {

    public void reset() {
      init();
    }

    @Override
    public void setColumns() {

      setSelectable(true);

      addColumn("Naam", 200);
      addColumn("Geslacht", 60);
      addColumn("Geboortedatum", 100);
      addColumn("Adres");
      addColumn("Status", 120).setUseHTML(true);

      setSelectable(false);

      super.setColumns();
    }

    public void update() {

      getRecords().clear();

      DossierPersoon persoon = getBetrokkene();

      if (persoon != null && persoon.isVolledig()) {

        Record row = addRecord(persoon);

        row.addValue(persoon.getNaam().getNaam_naamgebruik_eerste_voornaam());
        row.addValue(persoon.getGeslacht().getNormaal());
        row.addValue(persoon.getGeboorte().getDatum_leeftijd());
        row.addValue(persoon.getAdres().getAdres_pc_wpl());

        if (persoon.isVerstrekkingsbeperking()) {
          row.addValue(setClass(false, "Verstrek. beperking"));
        } else {
          row.addValue(setClass(false, ""));
        }
      }

      reloadRecords();
    }
  }
}
