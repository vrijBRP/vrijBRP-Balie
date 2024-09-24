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

package nl.procura.gba.web.modules.bs.onderzoek.page10.adresselectie.adres;

import static nl.procura.burgerzaken.gba.core.enums.GBACat.HUW_GPS;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.INSCHR;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.PERSOON;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.VB;
import static nl.procura.gba.common.MiscUtils.setClass;
import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.BETROKKENE;
import static nl.procura.gba.web.services.bs.algemeen.functies.BsPersoonUtils.kopieDossierPersoon;
import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.INFO;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.WARNING;
import static nl.procura.commons.core.exceptions.ProExceptionType.ENTRY;
import static nl.procura.commons.core.exceptions.ProExceptionType.SELECT;

import java.util.List;

import com.vaadin.ui.Button;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.procura.arguments.PLEArgs;
import nl.procura.diensten.gba.ple.procura.arguments.PLEDatasource;
import nl.procura.diensten.gba.ple.procura.templates.custom.CustomTemplate;
import nl.procura.gba.web.common.misc.SelectListener;
import nl.procura.gba.web.components.layouts.OptieLayout;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.bs.onderzoek.page10.adresselectie.zoeken.AdresWindow;
import nl.procura.gba.web.services.beheer.bag.ProcuraInhabitantsAddress;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.gba.ple.PersonenWsService;
import nl.procura.java.collection.Collections;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.vaadin.component.dialog.ConfirmDialog;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.info.InfoLayout;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;
import nl.procura.vaadin.functies.VaadinUtils;

public class BewonerPage extends NormalPageTemplate {

  protected final Button buttonSearchBewon = new Button("Zoek");
  protected final Button buttonSelect      = new Button("Selecteer");

  private final OptieLayout                    form2Layout;
  private final Table                          table;
  private final BewonerForm1                   form1;
  private final BewonerForm2                   form2;
  private final SelectListener<DossierPersoon> listener;
  private ProcuraInhabitantsAddress            adres;

  public BewonerPage(ProcuraInhabitantsAddress address, SelectListener<DossierPersoon> listener) {
    this.adres = address;
    this.listener = listener;

    form1 = new BewonerForm1(address);
    form2 = new BewonerForm2();
    table = new Table();

    buttonNext.setCaption("Zoek een address (F2)");
    addButton(buttonNext, 1f);
    addButton(buttonClose);

    form2Layout = new OptieLayout();
    form2Layout.setVisible(false);
    form2Layout.getLeft().addComponent(new Fieldset("Zoek een specifieke bewoner van dit address"));
    form2Layout.getLeft().addComponent(new InfoLayout("", "Zoek alle bewoners door de velden leeg " +
        "te laten of zoek een specifieke persoon door middel van onderstaande velden."));
    form2Layout.getLeft().addComponent(form2);
    form2Layout.getRight().setWidth("140px");
    form2Layout.getRight().setCaption("Bewoners");
    form2Layout.getRight().addButton(buttonSearchBewon, this);

    addComponent(form1);
    addComponent(form2Layout);

    if (listener != null) {
      OptieLayout ol3 = new OptieLayout();
      ol3.getLeft().addComponent(new Fieldset("Bewoners", table));
      ol3.getRight().setWidth("140px");
      ol3.getRight().setCaption("Selectie");
      ol3.getRight().addButton(buttonSelect, this);
      addComponent(ol3);
      table.setSelectable(true);

    } else {
      addComponent(new Fieldset("Bewoners", table));
    }
  }

  @Override
  protected void initPage() {
    super.initPage();
    if (adres == null) {
      onNextPage();
    } else {
      checkForm2Layout();
    }
  }

  @Override
  public void onEnter() {
    onSearch();
  }

  @Override
  public void onClose() {
    getWindow().closeWindow();
    super.onClose();
  }

  @Override
  public void handleEvent(Button button, int keyCode) {
    if (button == buttonSearchBewon) {
      checkSearch();

    } else if (button == buttonSelect) {
      selectToest(table.getSelectedRecords());
    }

    super.handleEvent(button, keyCode);
  }

  @Override
  public void onNextPage() {
    getParentWindow().addWindow(new AdresWindow(adres, newAdres -> {
      this.adres = newAdres;
      form1.update(this.adres);
      checkForm2Layout();
      buttonNext.focus();
    }));
  }

  private void checkForm2Layout() {
    form2Layout.setVisible(isGrootAantalBewoners());
    checkSearch();
  }

  @Override
  public void onSearch() {
    checkSearch();
    super.onSearch();
  }

  private void checkSearch() {
    form2.commit();
    if (isGrootAantalBewoners() && form2.getBean().isEmpty()) {
      getParentWindow().addWindow(
          new ConfirmDialog("Toon alle " + adres.getPersons().size() + " personen van dit adres?", 300) {

            @Override
            public void buttonYes() {
              search();
              super.buttonYes();
            }
          });
    } else {
      search();
    }
  }

  private void search() {
    if (adres == null) {
      throw new ProException(ENTRY, INFO, "Er is geen adres aangegeven.");
    }

    form1.commit();
    form2.commit();

    BewonerBean bean = form2.getBean();

    PLEArgs zArgs = new PLEArgs();

    // Adresvelden
    zArgs.setStraat(adres.getStreet());
    zArgs.setPostcode(adres.getPostalCode());
    zArgs.setHuisnummer(adres.getHnr());
    zArgs.setHuisletter(adres.getHnrL());
    zArgs.setHuisnummertoevoeging(adres.getHnrT());

    // Niet-adresvelden
    zArgs.addNummer(astr(bean.getBsn().getValue()));
    zArgs.setGeboortedatum(astr(bean.getGeboortedatum().getValue()));
    zArgs.setGeslachtsnaam(bean.getGeslachtsnaam());

    table.getRecords().clear();
    table.reloadRecords();

    zArgs.setCustomTemplate(CustomTemplate.PERSON);
    zArgs.setDatasource(PLEDatasource.STANDAARD);
    zArgs.setShowHistory(false);
    zArgs.setShowArchives(false);
    zArgs.addCat(PERSOON, VB, INSCHR, HUW_GPS);

    PersonenWsService gbaWs = getServices().getPersonenWsService();
    List<BasePLExt> bpls = gbaWs.getPersoonslijsten(zArgs, false).getBasisPLWrappers();

    int nr = 0;
    for (BasePLExt pl : bpls) {
      if (!pl.getPersoon().getStatus().isOpgeschort()) {
        String naam = pl.getPersoon().getNaam().getNaamNaamgebruikEersteVoornaam();
        Record r = table.addRecord(pl);
        r.addValue(++nr);
        r.addValue(naam);
        r.addValue(pl.getPersoon().getGeslacht().getDescr());
        r.addValue(pl.getPersoon().getGeboorte().getDatumLeeftijd());
        r.addValue(setClass(false, pl.getPersoon().getStatus().getOpsomming()));
      }
    }

    table.reloadRecords();
    VaadinUtils.resetHeight(getWindow());
  }

  private void selectToest(List<Record> records) {

    if (table.isSelectable()) {
      try {
        if (records.isEmpty()) {
          throw new ProException(SELECT, "Selecteer minimaal één bewoner.");
        }

        for (Record record : records) {
          DossierPersoon persoon = new DossierPersoon();
          persoon.setDossierPersoonType(BETROKKENE);
          BasePLExt pl = record.getObject(BasePLExt.class);
          if (pl.getPersoon().getStatus().isOverleden()) {
            throw new ProException(WARNING, "Deze persoon is overleden.");
          }
          listener.select(kopieDossierPersoon(pl, persoon));
        }

      } catch (Exception e) {
        getApplication().handleException(e);
      }
    }
  }

  public class Table extends GbaTable {

    public Table() {
      setMultiSelect(true);
    }

    @Override
    public void setColumns() {
      addColumn("Nr.", 50);
      addColumn("Persoon").setUseHTML(true);
      addColumn("Geslacht", 100);
      addColumn("Geboren", 110);
      addColumn("Status", 150).setUseHTML(true);

      super.setColumns();
    }

    @Override
    public void onDoubleClick(Record record) {
      selectToest(Collections.list(record));
    }
  }

  private boolean isGrootAantalBewoners() {
    return adres != null && adres.getPersons().size() > 10;
  }
}
