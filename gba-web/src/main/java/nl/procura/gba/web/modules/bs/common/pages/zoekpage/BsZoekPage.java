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

package nl.procura.gba.web.modules.bs.common.pages.zoekpage;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.extensions.PLResultComposite;
import nl.procura.diensten.gba.ple.procura.arguments.PLEArgs;
import nl.procura.gba.web.components.layouts.page.ButtonPageTemplate;
import nl.procura.gba.web.components.layouts.page.buttons.ModalCloseButton;
import nl.procura.gba.web.services.bs.algemeen.functies.BsPersoonUtils;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.interfaces.address.Address;
import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;
import nl.procura.vaadin.component.window.Message;

/**
 * Geboorte
 */

public class BsZoekPage extends ButtonPageTemplate {

  private final ModalCloseButton buttonCloseModal = new ModalCloseButton();
  private DossierPersoon         dossierPersoon   = null;
  private BsZoekForm             form             = null;
  private Table                  table            = null;

  public BsZoekPage(DossierPersoon dossierPersoon) {

    setDossierPersoon(dossierPersoon);

    addButton(buttonSearch, buttonReset, buttonCloseModal);
    setMargin(true);

    getButtonLayout().setExpandRatio(buttonReset, 1f);
    getButtonLayout().setWidth("100%");
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      setSpacing(true);

      setInfo(
          "Vul identificerende gegevens in en druk op Zoeken (Enter). Druk nogmaals op Zoeken (Enter) om het bovenste "
              + "zoekresultaat te selecteren of dubbelklik hierop. Met de cursor kan worden gebladerd binnen de resultaten.");

      setForm(new BsZoekForm(getDossierPersoon()));
      setTable(new Table());

      addComponent(getForm());
      addComponent(getTable());
    }

    super.event(event);
  }

  @Override
  public void focus() {
    // Overslaan. Zorg voor focus op BSN veld
  }

  public DossierPersoon getDossierPersoon() {
    return dossierPersoon;
  }

  public void setDossierPersoon(DossierPersoon dossierPersoon) {
    this.dossierPersoon = dossierPersoon;
  }

  public BsZoekForm getForm() {
    return form;
  }

  public void setForm(BsZoekForm form) {
    this.form = form;
  }

  public Table getTable() {
    return table;
  }

  public void setTable(Table table) {
    this.table = table;
  }

  @Override
  public void onEnter() {

    if (getTable().getRecords().isEmpty()) {

      doSearch();
    } else {

      Record record = getTable().getSelectedRecord();

      if (record != null) {

        onSearchOrClick(record);
      }
    }
  }

  @Override
  public void onNew() {

    getForm().reset();

    getTable().getRecords().clear();

    getTable().reloadRecords();

    super.onNew();
  }

  @Override
  public void onNextPage() {

    Record record = getTable().getSelectedRecord();

    if (record != null) {

      BsPersoonUtils.kopieDossierPersoon((BasePLExt) record.getObject(), getDossierPersoon());
    } else {
      warningMessage("Geen record geselecteerd.");
    }

    super.onNextPage();
  }

  @Override
  public void onSearch() {
    doSearch();
  }

  private void doSearch() {

    getForm().commit();

    getTable().getRecords().clear();
    getTable().reloadRecords();

    BsZoekBean b = getForm().getBean();

    PLEArgs args = new PLEArgs();
    args.setMaxFindCount(50);
    args.addNummer(b.getBsn());
    args.setGeboortedatum(b.getGeboortedatum().getStringValue());
    args.setHuisnummer(b.getHnr());
    args.setPostcode(b.getPostcode());
    args.setGeslachtsnaam(b.getGeslachtsnaam());
    args.setVoornaam(b.getVoornamen());

    Address adres = b.getAdres();
    if (adres != null) {
      args.setPostcode(adres.getPostalCode());
      args.setHuisnummer(adres.getHnr());
      args.setHuisletter(adres.getHnrL());
      args.setHuisnummertoevoeging(adres.getHnrT());
    }

    PLResultComposite result = getApplication().getServices().getPersonenWsService()
        .getPersoonslijsten(args, false);

    if (result.getBasisPLWrappers().size() > 0) {
      getTable().setResult(result);
      getTable().select(getTable().firstItemId());
      getTable().focus();

      if (result.getBasisPLWrappers().size() == 1) {
        onEnter();
      }
    } else {
      new Message(getWindow(), "Geen zoekresultaten", Message.TYPE_WARNING_MESSAGE);
    }
  }

  private void onSearchOrClick(Record record) {

    if (record == null) {
      warningMessage("Geen record geselecteerd.");
      return;
    }

    DossierPersoon persoon = getDossierPersoon();

    try {

      // Zoek de persoon
      BasePLExt pl = getServices().getPersonenWsService().getPersoonslijst(
          record.getObject(BasePLExt.class), true);
      BsPersoonUtils.kopieDossierPersoon(pl, persoon);

      // Controleer de persoon
      BsPersoonUtils.controleerOverlijden(pl, persoon);
      BsPersoonUtils.controleerGeslacht(persoon);
      BsPersoonUtils.controleerBurgerlijkeStaat(persoon);

      buttonCloseModal.closeWindow();
    } catch (ProException e) {
      BsPersoonUtils.reset(persoon);
      warningMessage(e.getMessage());
    }

    super.onEnter();
  }

  public class Table extends BsZoekTable {

    @Override
    public void onClick(Record record) {

      onSearchOrClick(record);

      super.onClick(record);
    }
  }
}
