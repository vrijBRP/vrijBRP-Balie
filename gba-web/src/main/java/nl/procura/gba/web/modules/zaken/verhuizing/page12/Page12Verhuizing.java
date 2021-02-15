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

package nl.procura.gba.web.modules.zaken.verhuizing.page12;

import static nl.procura.burgerzaken.gba.core.enums.GBACat.*;
import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.fil;
import static nl.procura.standard.exceptions.ProExceptionSeverity.INFO;
import static nl.procura.standard.exceptions.ProExceptionType.ENTRY;

import java.util.ArrayList;
import java.util.List;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.procura.arguments.PLEArgs;
import nl.procura.diensten.gba.ple.procura.arguments.PLEDatasource;
import nl.procura.diensten.gba.wk.extensions.BaseWKExt;
import nl.procura.diensten.gba.wk.procura.argumenten.ZoekArgumenten;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.layouts.OptieLayout;
import nl.procura.gba.web.modules.zaken.common.ZakenPage;
import nl.procura.gba.web.modules.zaken.verhuizing.VerhuisAdres;
import nl.procura.gba.web.modules.zaken.verhuizing.VerhuisRelatie;
import nl.procura.gba.web.modules.zaken.verhuizing.page10.Page10Verhuizing;
import nl.procura.gba.web.modules.zaken.verhuizing.page14.AdresSelectieListener;
import nl.procura.gba.web.modules.zaken.verhuizing.page14.Page14Verhuizing;
import nl.procura.gba.web.services.gba.ple.PersonenWsService;
import nl.procura.gba.web.services.gba.ple.relatieLijst.Relatie;
import nl.procura.gba.web.services.gba.ple.relatieLijst.RelatieLijst;
import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

/**
 * Toevoegen huidig adres
 */

public class Page12Verhuizing extends ZakenPage {

  private Page12VerhuizingForm1 form1       = null;
  private Page12VerhuizingForm2 form2       = null;
  private GbaNativeSelect       selectField = null;

  public Page12Verhuizing(GbaNativeSelect selectField) {

    super("Verhuizing");

    setSelectField(selectField);

    addButton(buttonPrev);
    addButton(buttonNext);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      form1 = new Page12VerhuizingForm1(getApplication());
      form2 = new Page12VerhuizingForm2();

      setInfo("Adres wijzigen",
          "Wijzig het adres indien de verhuizende personen niet op het adres van de aangever woonachtig zijn.");

      OptieLayout ol = new OptieLayout();

      ol.getLeft().addComponent(form1);
      ol.getLeft().addComponent(form2);

      ol.getRight().setWidth("200px");
      ol.getRight().setCaption("Opties");

      ol.getRight().addButton(buttonSearch, this);

      addComponent(ol);
    }

    super.event(event);
  }

  public GbaNativeSelect getSelectField() {
    return selectField;
  }

  public void setSelectField(GbaNativeSelect selectField) {
    this.selectField = selectField;
  }

  @Override
  public void onEnter() {
    onSearch();
  }

  @Override
  public void onNextPage() {

    form1.commit();
    form2.commit();

    Page12VerhuizingBean1 b = form1.getBean();
    Page12VerhuizingBean2 b2 = form2.getBean();

    String bsn = astr(b2.getBsn());
    boolean isBsn = fil(bsn);
    boolean isHnr = fil(astr(b.getHnr()));
    boolean isPc = fil(astr(b.getPc()));
    boolean isStraatG = fil(astr(b.getStraatGem()));
    boolean isStraatL = fil(b.getStraatLand());

    if (b.getGegevensbron() == PLEDatasource.PROCURA) {

      if (!isBsn && !isStraatG && !isHnr && !isPc) {
        throw new ProException(ENTRY, INFO, "Geen zoekargumenten ingegeven.");
      }
    } else if (b.getGegevensbron() == PLEDatasource.GBAV) {

      if (!isBsn && !isStraatL && !isHnr && !isPc) {
        throw new ProException(ENTRY, INFO, "Geen zoekargumenten ingegeven.");
      }
    }

    List<VerhuisAdres> adr = new ArrayList<>();

    // Zoek in woningkaart als gemeentelijk is en bsn is niet ingevuld;

    if ((b.getGegevensbron() == PLEDatasource.PROCURA) && !isBsn) {

      ZoekArgumenten wkZ = new ZoekArgumenten();
      wkZ.setExtra_pl_gegevens(false);

      if (b.getStraatGem() != null) {
        wkZ.setStraatnaam(b.getStraatGem().getDescription());
      }

      wkZ.setHuisnummer(b.getHnr());

      if (b.getPc() != null) {
        wkZ.setPostcode(astr(b.getPc().getValue()));
      }

      PersonenWsService gbaWs = getServices().getPersonenWsService();

      adr = getWkAdressen(gbaWs.getAdres(wkZ, false).getBasisWkWrappers());
    }

    // Zoek in PLE als aantal 0 is.

    if (adr.isEmpty()) {

      PLEArgs args = new PLEArgs();

      if (fil(bsn)) {
        args.addNummer(bsn);
      }

      if (b.getGegevensbron() == PLEDatasource.PROCURA) {
        args.setStraat(astr(b.getStraatGem()));
      } else {
        args.setStraat(b.getStraatLand());
        args.setSearchOnAddress(b.isAdresind());
      }

      args.setHuisnummer(b.getHnr());
      args.setPostcode(astr(b.getPc().getValue()));
      args.addCat(PERSOON, VB, VERW);
      args.setDatasource(b.getGegevensbron());
      args.setShowHistory(false);
      args.setShowArchives(false);

      PersonenWsService gbaWs = getServices().getPersonenWsService();

      adr = getPlAdressen(gbaWs.getPersoonslijsten(args, false).getBasisPLWrappers());
    }

    switch (adr.size()) {

      case 0:
        throw new ProException(ENTRY, INFO, "Geen adres gevonden.");

      case 1:
        selectAdres(adr.get(0));
        return;

      default:
        Page14Verhuizing page14 = new Page14Verhuizing(adr, false);
        page14.addListener((AdresSelectieListener) event -> selectAdres(event.getAdres()));
        getNavigation().goToPage(page14);
    }
  }

  @Override
  public void onSearch() {
    onNextPage();
    super.onSearch();
  }

  /**
   * Voeg de relaties van de aangever standaard toe als verhuisrelatie.
   */
  private VerhuisAdres addRelaties(RelatieLijst relatieLijst, VerhuisAdres adres) {

    for (Relatie relatie : relatieLijst.getSortedRelaties()) {

      String relatieAdres = new VerhuisAdres(relatie.getPl(),
          getApplication().getServices().getGebruiker()).getAddressLabel();

      if (relatieAdres.equals(adres.getAddressLabel())) {

        adres.addVerhuisRelatie(new VerhuisRelatie(relatie));
      }
    }

    return adres;
  }

  /**
   * Converteer PL'en naar VerhuisAdressen
   */
  private List<VerhuisAdres> getPlAdressen(List<BasePLExt> bpls) {

    RelatieLijst relatieLijst = getApplication().getServices().getPersonenWsService().getRelatieLijst(getPl(),
        false);

    List<VerhuisAdres> l = new ArrayList<>();

    for (BasePLExt wk : bpls) {

      l.add(addRelaties(relatieLijst, new VerhuisAdres(wk, getApplication().getServices().getGebruiker())));
    }

    return l;
  }

  /**
   * Converter WK adressen naar verhuisadressen
   */
  private List<VerhuisAdres> getWkAdressen(List<BaseWKExt> wks) {

    RelatieLijst relatieLijst = getApplication().getServices().getPersonenWsService().getRelatieLijst(getPl(),
        false);

    List<VerhuisAdres> adressen = new ArrayList<>();

    for (BaseWKExt wk : wks) {

      adressen.add(addRelaties(relatieLijst, new VerhuisAdres(wk)));
    }

    return adressen;
  }

  private void selectAdres(VerhuisAdres adres) {

    selectField.getContainerDataSource().addItem(adres);
    selectField.setValue(adres);

    getNavigation().goBackToPage(Page10Verhuizing.class);
  }
}
