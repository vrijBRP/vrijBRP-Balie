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

package nl.procura.gba.web.modules.bs.onderzoek.page1;

import static nl.procura.gba.common.MiscUtils.setClass;
import static nl.procura.gba.web.common.misc.GbaDatumUtils.addWerkdagen;
import static nl.procura.gba.web.modules.bs.onderzoek.page1.Page1OnderzoekBean.RELATIE;
import static nl.procura.gba.web.services.bs.onderzoek.enums.OnderzoekBronType.BURGER;
import static nl.procura.standard.Globalfunctions.emp;
import static nl.procura.standard.exceptions.ProExceptionSeverity.WARNING;

import java.util.ArrayList;

import com.vaadin.ui.Button;
import com.vaadin.ui.Window;

import nl.procura.diensten.gba.ple.procura.arguments.PLEDatasource;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.components.layouts.OptieLayout;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.bs.common.pages.persoonpage.BsStatusForm;
import nl.procura.gba.web.modules.bs.common.pages.zoekpage.BsZoekWindow;
import nl.procura.gba.web.modules.bs.onderzoek.BsPageOnderzoek;
import nl.procura.gba.web.modules.bs.onderzoek.adreslayout.AdresLayout;
import nl.procura.gba.web.modules.bs.onderzoek.adreslayout.types.AanleidingAdres;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.onderzoek.enums.OnderzoekBronType;
import nl.procura.gba.web.services.bs.onderzoek.enums.VermoedAdresType;
import nl.procura.gba.web.services.gba.basistabellen.gemeente.Gemeente;
import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.VLayout;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.table.indexed.IndexedTable;

/**
 * Onderzoek - aanleiding
 */
public class Page1Onderzoek extends BsPageOnderzoek {

  private Page1OnderzoekForm1 form1;
  private Page1OnderzoekForm2 form2;
  private Page1OnderzoekForm3 form3;
  private ZoekTable           zoekTable;
  private final VLayout       burgerLayout = new VLayout();
  private AdresLayout         adresLayout;

  private final Button buttonGba  = new Button("Zoek persoon");
  private final Button buttonZoek = new Button("Persoonslijst");

  public Page1Onderzoek() {
    super("Onderzoek - aanleiding");
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      buttonPrev.setEnabled(false);
      addButton(buttonPrev);
      addButton(buttonNext);

      adresLayout = new AdresLayout(new AanleidingAdres(getZaakDossier()));

      form1 = new Page1OnderzoekForm1(getZaakDossier()) {

        @Override
        protected void onChangeBron(OnderzoekBronType bron) {
          setBron(bron);
        }
      };

      zoekTable = new ZoekTable();

      addComponent(new BsStatusForm(getDossier()));
      setInfo("Vul de aangiftegegevens in en druk op Volgende (F2) om verder te gaan.");

      addComponent(form1);
      addComponent(burgerLayout);
      addComponent(adresLayout);

      setVermoedAdres(form1.getBean().getVermoedAdres());
      setBron(getZaakDossier().getOnderzoekBron());
      zoekTable.update();
    }

    super.event(event);
  }

  private void setBron(OnderzoekBronType bron) {
    form1.getField(RELATIE).setVisible(BURGER.equals(bron));
    form1.repaint();
    form2 = null;
    form3 = null;
    burgerLayout.removeAllComponents();
    if (bron != null && !OnderzoekBronType.ONBEKEND.equals(bron)) {
      switch (bron) {
        case BURGER:
          burgerLayout.addComponent(getOptieLayout());
          break;

        case TMV:
          form2 = new Page1OnderzoekForm2(getZaakDossier(), bron);
          burgerLayout.addComponent(form2);
          break;

        case INSTANTIE:
          form2 = new Page1OnderzoekForm2(getZaakDossier(), bron);
          burgerLayout.addComponent(form2);
          break;

        case AMBTSHALVE:
          form2 = new Page1OnderzoekForm2(getZaakDossier(), bron);
          burgerLayout.addComponent(form2);
          break;
      }

      form3 = new Page1OnderzoekForm3(getZaakDossier()) {

        @Override
        protected void onChangeVermoedAdres(VermoedAdresType type) {
          setVermoedAdres(type);
        }
      };
      burgerLayout.addComponent(form3);
    }
  }

  public void setVermoedAdres(VermoedAdresType type) {
    if (VermoedAdresType.IN_GEMEENTE.equals(type)) {
      adresLayout.setForm(AdresLayout.FormType.BINNEN_GEM);
    } else if (VermoedAdresType.ANDERE_GEMEENTE.equals(type)) {
      adresLayout.setForm(AdresLayout.FormType.BUITEN_GEM);
    } else if (VermoedAdresType.BUITENLAND.equals(type)) {
      adresLayout.setForm(AdresLayout.FormType.LAND);
    } else {
      adresLayout.setForm(null);
    }
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if (button == buttonGba) {
      openZoekWindow();
    } else if (button == buttonZoek) {

      final String anr = getZaakDossier().getAangever().getAnummer().getStringValue();
      final String bsn = getZaakDossier().getAangever().getBurgerServiceNummer().getStringValue();

      if (emp(anr) && emp(bsn)) {
        throw new ProException(WARNING, "Geen burger geselecteerd.");
      }

      getApplication().goToPl(getParentWindow(), "", PLEDatasource.STANDAARD, anr, bsn);
    }

    super.handleEvent(button, keyCode);
  }

  @Override
  public boolean checkPage() {

    if (super.checkPage()) {

      form1.commit();
      adresLayout.commit();

      Page1OnderzoekBean bean1 = form1.getBean();
      getZaakDossier().setOnderzoekBron(bean1.getBron());
      getZaakDossier().setAanlRelatie(BURGER.equals(bean1.getBron()) ? bean1.getRelatie() : "");
      getZaakDossier().setAanlTmvNr("");
      getZaakDossier().setAanlInst("");
      getZaakDossier().setAanlInstAanhef("");
      getZaakDossier().setAanlInstVoorl("");
      getZaakDossier().setAanlInstNaam("");
      getZaakDossier().setAanlInstAdres("");
      getZaakDossier().setAanlInstPlaats("");
      getZaakDossier().setAanlInstPc("");
      getZaakDossier().setAanlKenmerk("");

      // Adres
      getZaakDossier().setAanleidingAdres(null);
      getZaakDossier().setAanleidingHnr("");
      getZaakDossier().setAanleidingHnrL("");
      getZaakDossier().setAanleidingHnrT("");
      getZaakDossier().setAanleidingHnrA(null);
      getZaakDossier().setAanleidingPc(null);
      getZaakDossier().setAanleidingPlaats(new FieldValue());
      getZaakDossier().setAanleidingGemeente(new FieldValue());
      getZaakDossier().setAanleidingBuitenl1("");
      getZaakDossier().setAanleidingBuitenl2("");
      getZaakDossier().setAanleidingBuitenl3("");
      getZaakDossier().setAanleidingLand(new FieldValue());
      getZaakDossier().setVermoedelijkeGemeentePostbus(new Gemeente());

      if (form2 != null) {
        form2.commit();
        Page1OnderzoekBean bean = form2.getBean();
        switch (bean1.getBron()) {
          case BURGER:
            break;
          case TMV:
            getZaakDossier().setAanlTmvNr(bean.getNr());
            getZaakDossier().setAanlKenmerk(bean.getKenmerk());
            break;
          case INSTANTIE:
            getZaakDossier().setAanlInst(bean.getInstantie());
            getZaakDossier().setAanlInstAanhef(FieldValue.from(bean.getTavAanhef()).getStringValue());
            getZaakDossier().setAanlInstVoorl(bean.getTavVoorl());
            getZaakDossier().setAanlInstNaam(bean.getTavNaam());
            getZaakDossier().setAanlInstAdres(bean.getAdres());
            getZaakDossier().setAanlInstPlaats(bean.getPlaats());
            getZaakDossier().setAanlInstPc(FieldValue.from(bean.getPc()).getStringValue());
            getZaakDossier().setAanlKenmerk(bean.getKenmerk());
            break;
          case AMBTSHALVE:
            getZaakDossier().setAanlAfdeling(bean.getAfdeling());
            getZaakDossier().setAanlKenmerk(bean.getKenmerk());
            break;
        }
      }

      if (form3 != null) {
        form3.commit();
        DateTime datumOntvangst = new DateTime(form3.getBean().getDatumOntvangst());
        DateTime datumEindeTermijn = new DateTime(addWerkdagen(datumOntvangst.getCalendar(), 5).getTime());
        getZaakDossier().setDatumOntvangstMelding(datumOntvangst);
        getZaakDossier().setDatumEindeTermijn(datumEindeTermijn);
        getZaakDossier().setOnderzoekAard(form3.getBean().getAard());
        getZaakDossier().setVermoedelijkAdres(form3.getBean().getVermoedAdres());
      }

      adresLayout.save();

      getServices().getOnderzoekService().save(getDossier());
      return true;
    }

    return false;
  }

  @Override
  public void onNextPage() {
    goToNextProces();
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

      DossierPersoon persoon = getZaakDossier().getAangever();
      if (persoon != null && persoon.isVolledig()) {

        IndexedTable.Record row = addRecord(persoon);
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

  private void openZoekWindow() {
    BsZoekWindow zoekWindow = new BsZoekWindow(getZaakDossier().getAangever(), new ArrayList<>());
    zoekWindow.addListener((Window.CloseListener) e -> zoekTable.update());
    getParentWindow().addWindow(zoekWindow);
  }

  private Fieldset getOptieLayout() {
    OptieLayout optieLayout = new OptieLayout();
    optieLayout.getLeft().addComponent(zoekTable);
    optieLayout.getRight().addButton(buttonGba, this);
    optieLayout.getRight().addButton(buttonZoek, this);
    optieLayout.getRight().setWidth("130px");
    return new Fieldset("Burger", optieLayout);
  }
}
