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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.page4.zoeken;

import static nl.procura.burgerzaken.gba.core.enums.GBACat.PERSOON;
import static nl.procura.gba.common.MiscUtils.trimNr;
import static nl.procura.gba.web.modules.hoofdmenu.zakenregister.page4.zoeken.Page4ZakenBean.NR;
import static nl.procura.standard.Globalfunctions.*;
import static nl.procura.standard.exceptions.ProExceptionSeverity.INFO;
import static nl.procura.standard.exceptions.ProExceptionSeverity.WARNING;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.procura.arguments.PLEArgs;
import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.jpa.personen.dao.ZaakKey;
import nl.procura.gba.web.common.misc.ZaakPeriode;
import nl.procura.gba.web.components.TableImage;
import nl.procura.gba.web.components.layouts.tablefilter.GbaIndexedTableFilterLayout;
import nl.procura.gba.web.components.layouts.tablefilter.sort.ZaakSortField;
import nl.procura.gba.web.components.listeners.FieldChangeListener;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZakenregisterPage;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page2.Page2ZakenOpties;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page4.telling.Anders;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page7.Page7Zaken;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.quicksearch.person.QuickSearchPersonWindow;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.gba.web.services.zaken.algemeen.ZaakSortering;
import nl.procura.gba.web.services.zaken.algemeen.ZaakUtils;
import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.LoadPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.functies.VaadinUtils;
import nl.procura.validation.Bsn;

public class Page4Zaken extends ZakenregisterPage<Zaak> {

  private static final String UITGEBREID    = "Meer opties";
  private static final String SIMPEL        = "Minder opties";
  private final Button        buttonExpand  = new Button(UITGEBREID);
  private final Button        buttonTelling = new Button("Tellingen afdrukken");
  private final List<ZaakKey> zaakIds       = new ArrayList<>();
  private ZaakSortering       zaakSortering = ZaakSortering.DATUM_INGANG_NIEUW_OUD;

  private FormUitgebreid          formUitgebreid;
  private FormSimpel              formSimpel;
  private Table1                  table;
  private QuickSearchPersonWindow snelZoekWindow;
  private Page2ZakenOpties        opties;

  public Page4Zaken() {

    super(null, "Zakenregister");

    addButton(buttonSearch);
    addButton(buttonReset);
    addButton(buttonExpand);
    addButton(buttonTelling);
  }

  @Override
  public void event(PageEvent event) {

    opties = VaadinUtils.addOrReplaceComponent(getButtonLayout(), new Page2ZakenOpties(table) {

      @Override
      protected void reloadTree() {
        super.reloadTree();
        onSearch();
      }
    });

    getButtonLayout().setComponentAlignment(opties, Alignment.MIDDLE_LEFT);

    if (event.isEvent(InitPage.class)) {

      setSpacing(true);

      table = new Table1();
      formSimpel = new FormSimpel();

      formUitgebreid = new FormUitgebreid();
      formUitgebreid.setVisible(false);

      ZaakSortField sortField = new ZaakSortField();
      sortField.addListener(new FieldChangeListener<ZaakSortering>() {

        @Override
        public void onChange(ZaakSortering value) {
          Page4Zaken.this.zaakSortering = value;
          onSearch();
        }
      });

      Page4Zaken.this.zaakSortering = sortField.getValue();

      GbaIndexedTableFilterLayout filterLayout = new GbaIndexedTableFilterLayout(table, sortField);
      getButtonLayout().addComponent(filterLayout, 4);

      addComponent(formSimpel);
      addComponent(formUitgebreid);
      addExpandComponent(table);

    } else if (event.isEvent(LoadPage.class)) {
      onSearch();
    }

    super.event(event);
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if (button == buttonExpand) {

      wijzigOpties();
    } else if (button == buttonTelling) {

      if (table.size() > 0) {
        ZaakPeriode periode = getDatumInvoerPeriode();
        getNavigation().goToPage(new Page7Zaken(periode, table));
      } else {
        throw new ProException(INFO, "De tabel bevat geen zaken");
      }
    } else if (keyCode == KeyCode.F8) {

      opties.delete();
    }

    super.handleEvent(button, keyCode);
  }

  @Override
  public void onEnter() {
    onSearch();
  }

  @Override
  public void onNew() {

    formSimpel.reset();
    formUitgebreid.reset();
    table.clear();
  }

  @Override
  public void onSearch() {
    zoekZaken();
    table.init();
    super.onSearch();
  }

  private ZaakPeriode getDatumInvoerPeriode() {

    ZaakPeriode periode = new ZaakPeriode();

    formSimpel.commit();

    Page4ZakenBean bSimpel = formSimpel.getBean();

    if (bSimpel.isFilled()) {
      ZaakPeriode selPeriode = bSimpel.getDatumInvoer();
      if (periode instanceof Anders) {
        periode.setdFrom(formSimpel.getBean().getInvoerVan().getLongValue());
        periode.setdTo(formSimpel.getBean().getInvoerTm().getLongValue());
      } else {
        periode.setdFrom(selPeriode.getdFrom());
        periode.setdTo(selPeriode.getdTo());
      }
    }

    return periode;
  }

  private void setNummers(Page4ZakenBean b, ZaakArgumenten za) {

    // Zoek op A-nr / BSN

    if (fil(b.getNr()) && new Bsn(b.getNr()).isCorrect()) {

      PLEArgs args = new PLEArgs();
      args.addNummer(trimNr(b.getNr()));
      args.setShowHistory(false);
      args.setShowArchives(false);
      args.addCat(PERSOON);

      for (BasePLExt pl : getServices().getPersonenWsService()
          .getPersoonslijsten(args, false)
          .getBasisPLWrappers()) {
        za.setAnr(along(pl.getPersoon().getAnr().getVal()));
        za.setBsn(along(pl.getPersoon().getBsn().getVal()));
      }
    }
  }

  private void wijzigOpties() {

    boolean isUitgebreid = buttonExpand.getCaption().equalsIgnoreCase(UITGEBREID);
    buttonExpand.setCaption(isUitgebreid ? SIMPEL : UITGEBREID);

    formUitgebreid.setVisible(isUitgebreid);
    formUitgebreid.repaint();

    onSearch();
  }

  private void zoekZaken() {

    zaakIds.clear();
    formSimpel.commit();
    formUitgebreid.commit();

    Page4ZakenBean bSimpel = formSimpel.getBean();
    Page4ZakenBean bUitgebreid = formUitgebreid.getBean();

    if (bSimpel.isFilled()) {

      ZaakPeriode pInvoer = bSimpel.getDatumInvoer();
      ZaakPeriode pIngang = bSimpel.getDatumIngang();
      ZaakArgumenten za = new ZaakArgumenten();
      za.setSortering(zaakSortering);

      // Simpel

      // Zoek op periode
      if (bSimpel.getDatumInvoer() != null) {
        if (bSimpel.getDatumInvoer().equals(new Anders())) {
          za.setdInvoerVanaf(along(bSimpel.getInvoerVan().getValue()));
          za.setdInvoerTm(along(bSimpel.getInvoerTm().getValue()));
        } else {
          za.setdInvoerVanaf(pInvoer.getdFrom());
          za.setdInvoerTm(pInvoer.getdTo());
        }
      }

      if (bSimpel.getDatumIngang() != null) {
        if (bSimpel.getDatumIngang().equals(new Anders())) {
          za.setdIngangVanaf(along(bSimpel.getIngangVan().getValue()));
          za.setdIngangTm(along(bSimpel.getIngangTm().getValue()));
        } else {
          za.setdIngangVanaf(pIngang.getdFrom());
          za.setdIngangTm(pIngang.getdTo());
        }
      }

      // Zoek op aanvraagnr.
      if (fil(bSimpel.getAanvraagNr())) {
        za.setZaakKey(new ZaakKey(bSimpel.getAanvraagNr()));
      }

      // Zoek op A-nr / BSN
      if (fil(bSimpel.getNr())) {
        za.setNummer(bSimpel.getNr());
      }

      setNummers(bSimpel, za);

      if (formUitgebreid.isVisible()) {

        // Gebruikers
        if (bUitgebreid.getGebruiker() != null && pos(bUitgebreid.getGebruiker().getValue())) {
          za.setCodeGebruiker(along(bUitgebreid.getGebruiker().getValue()));
        }

        // Profielen
        if (bUitgebreid.getProfiel() != null && pos(bUitgebreid.getProfiel().getValue())) {
          za.setCodeProfiel(along(bUitgebreid.getProfiel().getValue()));
        }

        // ZaakTypes
        for (FieldValue zaakType : bUitgebreid.getZaakTypes().getValues()) {
          za.addTypen((ZaakType) zaakType.getValue());
        }

        // ZaakStatussen
        for (FieldValue status : bUitgebreid.getZaakStatussen().getValues()) {
          za.addStatussen((ZaakStatusType) status.getValue());
        }

        // Indicaties
        if (bUitgebreid.getIndicatie() != null && fil(bUitgebreid.getIndicatie().getStringValue())) {
          za.addAttributen(bUitgebreid.getIndicatie().getStringValue());
        }
      }

      // Zet basisargumenten
      table.setZaakArgumenten(za);
      table.setSortering(zaakSortering);
      zaakIds.addAll(getServices().getZakenService().getZaakKeys(za));

    } else {
      throw new ProException(WARNING, "Geef minimaal een periode, zaak-id,  of A-nummer of BSN in.");
    }
  }

  public class FormSimpel extends Page4ZakenFormSimpel {

    @Override
    public void onReload() {
      onSearch();
    }

    @Override
    public void onZoekPersoon() {

      if (snelZoekWindow == null) {
        snelZoekWindow = new QuickSearchPersonWindow(pl -> {
          getField(NR).setValue(pl.getPersoon().getBsn().getVal());
          onSearch();
        });
      }

      getWindow().addWindow(snelZoekWindow);

      super.onZoekPersoon();
    }
  }

  public class FormUitgebreid extends Page4ZakenFormUitgebreid {

    @Override
    public void onReload() {
      onSearch();
    }
  }

  public class Table1 extends Page4ZakenTable {

    @Override
    public void setRecords() {

      if (zaakIds.size() > 0) {
        for (ZaakKey zaakId : zaakIds) {
          table.addRecord(zaakId).addValues(11);
        }
      }

      super.setRecords();
    }

    @Override
    protected void loadZaak(int nr, Record record, Zaak zaak) {
      record.setValue(0, nr);
      record.setValue(1, TableImage.getByCommentaarIcon(ZaakUtils.getCommentaarIcon(zaak)));
      record.setValue(2, zaak.getType().getOms());
      record.setValue(3, ZaakUtils.getOmschrijving(zaak));
      record.setValue(4, table.getIngevoerdDoor(zaak));
      record.setValue(5, ZaakUtils.getIngevoerdDoorGebruiker(zaak));
      record.setValue(6, table.getIngevoerdDoorProfielen(zaak));
      record.setValue(7, zaak.getBron());
      record.setValue(8, zaak.getLeverancier());
      record.setValue(9, ZaakUtils.getStatus(zaak.getStatus()));
      record.setValue(10, zaak.getDatumIngang());
      record.setValue(11, zaak.getDatumTijdInvoer());
    }
  }
}
