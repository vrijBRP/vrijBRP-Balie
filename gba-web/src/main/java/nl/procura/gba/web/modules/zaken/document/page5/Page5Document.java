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

package nl.procura.gba.web.modules.zaken.document.page5;

import static nl.procura.burgerzaken.gba.core.enums.GBACat.*;
import static nl.procura.gba.common.MiscUtils.setClass;
import static nl.procura.standard.Globalfunctions.*;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.INFO;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.WARNING;
import static nl.procura.commons.core.exceptions.ProExceptionType.SELECT;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.Button;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.extensions.formats.Adres;
import nl.procura.diensten.gba.ple.procura.arguments.PLEArgs;
import nl.procura.diensten.gba.ple.procura.arguments.PLEDatasource;
import nl.procura.diensten.gba.ple.procura.templates.custom.CustomTemplate;
import nl.procura.gba.web.components.TableImage;
import nl.procura.gba.web.components.layouts.OptieLayout;
import nl.procura.gba.web.modules.zaken.common.ZakenPage;
import nl.procura.gba.web.modules.zaken.document.page1.Page1Document;
import nl.procura.gba.web.services.beheer.parameter.ParameterConstant;
import nl.procura.gba.web.services.gba.ple.PersonenWsService;
import nl.procura.gba.web.services.gba.ple.relatieLijst.Relatie;
import nl.procura.gba.web.services.gba.ple.relatieLijst.RelatieLijstHandler;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.vaadin.component.layout.info.InfoLayout;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;
import nl.procura.vaadin.theme.twee.Icons;

/**
 * Toevoegen persoon
 */

public class Page5Document extends ZakenPage {

  private final Adres         adres;
  private final Button        buttonSearch1 = new Button("Zoeken binnen adres (Enter)");
  private final Button        buttonSearch2 = new Button("Zoeken buiten adres");
  private Page5DocumentForm1  form1         = null;
  private Page5DocumentTable1 table1        = null;

  public Page5Document(Adres adres) {

    super("Documenten: toevoegen persoon");

    this.adres = adres;
    addButton(buttonPrev);
    addButton(buttonNext);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      form1 = new Page5DocumentForm1(adres.getAdres());
      table1 = new Page5DocumentTable1();

      OptieLayout ol = new OptieLayout();

      ol.getLeft().addComponent(form1);
      ol.getLeft().addComponent(new InfoLayout());

      ol.getRight().setWidth("200px");
      ol.getRight().setCaption("Opties");

      ol.getRight().addButton(buttonSearch1, this);
      ol.getRight().addButton(buttonSearch2, this);

      addComponent(ol);
      addComponent(table1);
    }

    super.event(event);
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if (button == buttonSearch1) {
      doSearch(true);
    } else if (button == buttonSearch2) {
      doSearch(false);
    }

    super.handleEvent(button, keyCode);
  }

  @Override
  public void onEnter() {
    doSearch(true);
  }

  @Override
  public void onNextPage() {

    if (table1.getSelectedRecords().isEmpty()) {
      throw new ProException(INFO, "Geen personen geselecteerd");
    }

    Page1Document page1Document = getNavigation().getPage(Page1Document.class);

    for (Relatie relatie : table1.getSelectedValues(Relatie.class)) {

      relatie.setPl(getPl(relatie.getPl().getPersoon().getAnr().getVal()));

      page1Document.addRelatie(relatie);
    }

    page1Document.reloadRecords();

    getNavigation().goBackToPreviousPage();
  }

  @Override
  public void onSearch() {
    doSearch(false);
  }

  private void doSearch(boolean binnenAdres) {

    table1.getRecords().clear();

    try {

      form1.commit();

      Page5DocumentBean1 bean = form1.getBean();

      String bsn = bean.getBsn().getStringValue();
      String geb = bean.getGeboortedatum().getStringValue();
      String gesl = bean.getGeslachtsnaam();
      String hnr = astr(adres.getHuisnummer().getValue().getVal());
      String hnrL = astr(adres.getHuisletter().getValue().getVal());
      String hnrT = astr(adres.getHuisnummertoev().getValue().getVal());
      String hnrA = astr(adres.getHuisnummeraand().getValue().getVal());
      String pc = astr(adres.getPostcode().getValue().getVal());

      PLEArgs args = new PLEArgs();

      args.addNummer(bsn);
      args.setGeboortedatum(geb);
      args.setGeslachtsnaam(gesl);

      if (binnenAdres) {
        args.setHuisnummer(hnr);
        args.setHuisletter(hnrL);
        args.setHuisnummertoevoeging(hnrT);
        args.setAanduiding(hnrA);
        args.setPostcode(pc);
      }

      args.addCat(GBACat.VB);

      if (binnenAdres && emp(bsn)) {
        args.setCustomTemplate(CustomTemplate.WK);
        args.setSearchOnAddress(true);
      } else {
        if (emp(bsn) && (emp(geb) || emp(gesl))) {
          throw new ProException(WARNING,
              "Geef een BSN of combinatie van geboortedatum en geslachtsnaam in.");
        }
      }

      List<Relatie> relaties = zoekRelaties(getPl(), args);

      for (Relatie relatie : relaties) {

        String naam = relatie.getPl().getPersoon().getNaam().getNaamNaamgebruikEersteVoornaam();
        String status = getStatus(relatie, binnenAdres);

        Record r = table1.addRecord(relatie);

        TableImage warning = new TableImage(Icons.getIcon(Icons.ICON_WARN));
        r.addValue(fil(status) ? warning : null);
        r.addValue(naam + " " + setClass("red", status));
        r.addValue(relatie.getRelatieType().getOms());
        r.addValue(relatie.getRelatieType().getAangifte());
        r.addValue(relatie.getPl().getPersoon().getGeslacht().getDescr());
        r.addValue(relatie.getPl().getPersoon().getGeboorte().getDatumLeeftijd());
      }

      if (table1.getRecords().isEmpty()) {
        throw new ProException(SELECT, "Er zijn geen bewoners gevonden.");
      }
    } finally {
      table1.reloadRecords();
    }
  }

  private String getStatus(Relatie relatie, boolean binnenAdres) {

    boolean geemigreerd = relatie.getPl().getPersoon().getStatus().isEmigratie();
    boolean rni = relatie.getPl().getPersoon().getStatus().isRni();

    StringBuilder sb = new StringBuilder();

    if (geemigreerd) {
      sb.append("Geëmigreerd, ");
    }

    if (rni) {
      sb.append("PL aangelegd in de RNI, ");
    }

    if (!binnenAdres && !isZelfdeAdres(relatie)) {
      sb.append("Niet woonachtig op zelfde adres, ");
    }

    return trim(" - " + trim(sb.toString()));
  }

  private boolean isZelfdeAdres(Relatie relatie) {
    return relatie.getPl().getVerblijfplaats().getAdres().getAdres().equals(adres.getAdres());
  }

  private List<Relatie> zoekRelaties(BasePLExt pl, PLEArgs args) {

    List<Relatie> relaties = new ArrayList<>();

    args.setDatasource(PLEDatasource.STANDAARD);
    args.setShowHistory(false);
    args.setShowArchives(false);
    args.addCat(PERSOON, INSCHR, VERW);
    args.setCat(HUW_GPS, isTru(getApplication().getServices().getGebruiker().getParameters().get(
        ParameterConstant.ZOEK_PLE_NAAMGEBRUIK).getValue()));

    PersonenWsService gbaWs = getServices().getPersonenWsService();

    List<BasePLExt> bpls = gbaWs.getPersoonslijsten(args, false).getBasisPLWrappers();

    for (BasePLExt bpl : bpls) {

      Relatie relatie = new Relatie();
      relatie.setRelatieType(RelatieLijstHandler.getRelatieType(pl, bpl));
      relatie.setPl(bpl);

      boolean isOpgeschort = bpl.getPersoon().getStatus().isOpgeschort();
      relatie.setHuisgenoot(
          bpl.getVerblijfplaats().getAdres().getAdres().equals(pl.getVerblijfplaats().getAdres().getAdres()));

      if (!isOpgeschort) {
        relaties.add(relatie);
      }
    }

    return relaties;
  }
}
