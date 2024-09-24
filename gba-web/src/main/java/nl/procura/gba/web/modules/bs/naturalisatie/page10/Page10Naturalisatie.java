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

package nl.procura.gba.web.modules.bs.naturalisatie.page10;

import static nl.procura.burgerzaken.gba.core.enums.GBACat.HUW_GPS;
import static nl.procura.gba.common.MiscUtils.setClass;
import static nl.procura.gba.web.modules.bs.common.pages.gerelateerdepage.PageBsGerelateerdenUtils.getTypePersonen;
import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.PARTNER;
import static nl.procura.standard.Globalfunctions.emp;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.WARNING;

import java.util.ArrayList;
import java.util.Optional;

import com.vaadin.ui.Button;
import com.vaadin.ui.Window;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.procura.arguments.PLEDatasource;
import nl.procura.gba.web.components.layouts.OptieLayout;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.bs.common.pages.zoekpage.BsZoekWindow;
import nl.procura.gba.web.modules.bs.naturalisatie.BsPageNaturalisatie;
import nl.procura.gba.web.modules.bs.naturalisatie.page20.Page20Naturalisatie;
import nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType;
import nl.procura.gba.web.services.bs.algemeen.functies.BsPersoonUtils;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page10Naturalisatie extends BsPageNaturalisatie {

  private Page10NaturalisatieForm1 form1;
  private Page10NaturalisatieForm2 form2;
  private ZoekTable                zoekTable;

  private final OptieLayout optieLayout = new OptieLayout();
  private final Button      buttonGba   = new Button("Zoek persoon");
  private final Button      buttonZoek  = new Button("Persoonslijst");

  public Page10Naturalisatie() {
    super("Nationaliteit - procedurekeuze");
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {
      buttonPrev.setEnabled(false);
      addButton(buttonPrev);
      addButton(buttonNext);

      form1 = new Page10NaturalisatieForm1();
      form2 = new Page10NaturalisatieForm2(getZaakDossier());
      zoekTable = new ZoekTable();

      optieLayout.getLeft().addComponent(zoekTable);
      optieLayout.getRight().addButton(buttonGba, this);
      optieLayout.getRight().addButton(buttonZoek, this);
      optieLayout.getRight().setWidth("130px");

      setInfo("Zoek de persoon die Nederlander wil worden en beantwoord de controlevragen. "
          + "Druk op Volgende (F2) om verder te gaan.");

      addComponent(optieLayout);
      addComponent(form1);
      addComponent(form2);

      // Als betrokkene is gevuld dan tabel updaten,
      // anders deze zoeken
      if (getZaakDossier().getAangever().isVolledig()) {
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
    form1.commit();
    form2.commit();

    if (getZaakDossier().getDossier()
        .getPersonen(DossierPersoonType.AANGEVER)
        .stream().noneMatch(DossierPersoon::isVolledig)) {
      throw new ProException(WARNING, "Geen aangever geselecteerd");
    }

    Page10NaturalisatieBean bean2 = form2.getBean();
    getZaakDossier().setBevoegdTotVerzoekType(bean2.getBevoegd());
    getZaakDossier().setBevoegdIndienenToel(bean2.getToelichting());
    getZaakDossier().setOptie(bean2.getOptie());
    getZaakDossier().setOptieToel(bean2.getToelichting2());

    getServices().getNaturalisatieService().save(getDossier());
    return true;
  }

  @Override
  public void onNextPage() {
    if (checkPage()) {
      getNavigation().goToPage(Page20Naturalisatie.class);
    }
  }

  public class ZoekTable extends GbaTable {

    public void reset() {
      init();
    }

    @Override
    public void setColumns() {
      addColumn("Naam", 200);
      addColumn("Geslacht", 60);
      addColumn("Geboortedatum", 100);
      addColumn("Adres");
      addColumn("Status", 120).setUseHTML(true);
      setSelectable(false);

      super.setColumns();
    }

    public void updateAangever() {
      update();
      form2.update(getZaakDossier().getAangever());
    }

    public void update() {
      getRecords().clear();

      DossierPersoon persoon = getZaakDossier().getAangever();

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

        BasePLExt pl = getServices().getPersonenWsService().getPersoonslijst(persoon.getAnummer().getStringValue());
        form1.updateExtraAangeverInfo(pl, updatePartnerName(persoon));
      }

      reloadRecords();
    }

    private String updatePartnerName(DossierPersoon persoon) {
      Optional<DossierPersoon> partner = getTypePersonen(getServices(), persoon, HUW_GPS).stream()
          .filter(p -> p.getDossierPersoonType().is(PARTNER))
          .findFirst();

      String naam = "N.v.t.";
      if (partner.isPresent()) {
        BsPersoonUtils.kopieDossierPersoon(partner.get(), getZaakDossier().getPartner());
        naam = partner.get().getNaam().getNaam_naamgebruik_nen_eerste_voornaam();
      } else {
        BsPersoonUtils.reset(getZaakDossier().getPartner());
      }
      return naam;
    }
  }

  private void openZoekWindow() {
    BsZoekWindow zoekWindow = new BsZoekWindow(getZaakDossier().getAangever(), new ArrayList<>());
    zoekWindow.addListener((Window.CloseListener) e -> zoekTable.updateAangever());
    getParentWindow().addWindow(zoekWindow);
  }
}
