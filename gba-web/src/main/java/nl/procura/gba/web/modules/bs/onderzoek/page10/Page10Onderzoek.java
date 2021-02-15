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

package nl.procura.gba.web.modules.bs.onderzoek.page10;

import static nl.procura.standard.Globalfunctions.fil;
import static nl.procura.standard.exceptions.ProExceptionSeverity.WARNING;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.Button;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.openoffice.formats.Adresformats;
import nl.procura.diensten.gba.wk.extensions.BaseWKExt;
import nl.procura.diensten.gba.wk.procura.argumenten.ZoekArgumenten;
import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.web.components.layouts.OptieLayout;
import nl.procura.gba.web.modules.bs.onderzoek.BetrokkenenTable;
import nl.procura.gba.web.modules.bs.onderzoek.BsPageOnderzoek;
import nl.procura.gba.web.modules.bs.onderzoek.page10.adresselectie.adres.BewonerWindow;
import nl.procura.gba.web.modules.bs.onderzoek.page10.adresselectie.zoeken.SelectieAdres;
import nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType;
import nl.procura.gba.web.services.bs.algemeen.functies.BsPersoonUtils;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.onderzoek.OnderzoekService;
import nl.procura.gba.web.services.zaken.algemeen.status.ZaakStatusService;
import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.component.layout.Fieldset;

public class Page10Onderzoek extends BsPageOnderzoek {

  private final Table  table;
  private final Button buttonPersoon = new Button("Toevoegen");

  public Page10Onderzoek() {

    super("Onderzoek - betreft");

    table = new Table();

    addButton(buttonPrev);
    addButton(buttonNext);

    OptieLayout ol = new OptieLayout();
    ol.getRight().setCaption("Personen");
    ol.getRight().setWidth("160px");
    ol.getRight().addButton(buttonPersoon, this);
    ol.getRight().addButton(buttonDel, this);
    ol.getLeft().addComponent(new Fieldset("Betreffende personen", table));
    addComponent(ol);
  }

  @Override
  protected void initPage() {
    super.initPage();
    if (table.getRecords().isEmpty()) {
      openAdresWindow();
    }
  }

  @Override
  public void onEnter() {
    onSearch();
  }

  @Override
  public void onPreviousPage() {
    goToPreviousProces();
  }

  @Override
  public void onNextPage() {
    goToNextProces();
  }

  @Override
  public boolean checkPage() {
    ArrayList<DossierPersoon> personen = table.getAllValues(DossierPersoon.class);
    if (personen.isEmpty()) {
      throw new ProException(WARNING, "Voeg eerst de betreffende personen toe.");
    }

    for (DossierPersoon dossierPersoon : personen) {
      if (dossierPersoon.isVolledig()) {
        getZaakDossier().getDossier().toevoegenPersoon(dossierPersoon);
      }
    }

    OnderzoekService service = getApplication().getServices().getOnderzoekService();
    ZaakStatusService statussen = getApplication().getServices().getZaakStatusService();

    if (getDossier().isStatus(ZaakStatusType.INCOMPLEET)) {
      statussen.updateStatus(getDossier(), ZaakStatusType.INBEHANDELING, "");
    }

    // Een ID opvragen in het Zaak-DMS
    getApplication().getServices().getZaakIdentificatieService().getDmsZaakId(getDossier());

    service.save(getDossier());

    return true;
  }

  @Override
  public void onDelete() {
    List<DossierPersoon> personen = table.getSelectedValues(DossierPersoon.class);
    for (DossierPersoon persoon : personen) {
      getServices().getOnderzoekService().deleteBetrokkene(getZaakDossier(), persoon);
    }

    table.init();
    super.onDelete();
  }

  @Override
  public void handleEvent(Button button, int keyCode) {
    if (button == buttonPersoon) {
      openAdresWindow();
    }

    super.handleEvent(button, keyCode);
  }

  private void openAdresWindow() {
    SelectieAdres adres = null;
    ArrayList<DossierPersoon> personen = table.getAllValues(DossierPersoon.class);
    if (!personen.isEmpty()) {
      DossierPersoon persoon = personen.get(0);
      ZoekArgumenten args = new ZoekArgumenten();
      Adresformats adresformats = persoon.getAdres();
      args.setPostcode(adresformats.getPostcode());
      args.setHuisnummer(adresformats.getHuisnummer());
      args.setHuisletter(adresformats.getHuisletter());
      args.setHuisnummertoevoeging(adresformats.getHuisnummertoev());
      args.setDatum_einde("-1");
      List<BaseWKExt> basisWkWrappers = getServices().getPersonenWsService().getAdres(args, true).getBasisWkWrappers();
      if (!basisWkWrappers.isEmpty()) {
        adres = new SelectieAdres(basisWkWrappers.get(0));
      }
    }

    getParentWindow().addWindow(new BewonerWindow(adres, dossierPersoon -> {
      getZaakDossier().getDossier().toevoegenPersoon(dossierPersoon);
      successMessage("Personen zijn toegevoegd");
      table.init();
    }));
  }

  public class Table extends BetrokkenenTable {

    @Override
    public void setColumns() {
      setSelectable(true);
      setMultiSelect(true);

      addColumn("Nr.", 50);
      addColumn("Naam", 300);
      addColumn("Adres", 400);
      addColumn("Status").setUseHTML(true);

      super.setColumns();
    }

    @Override
    public void setRecords() {
      int nr = 0;
      List<BasePLExt> persoonslijsten = getAanduidingen(getZaakDossier());
      List<DossierPersoon> personen = getZaakDossier().getDossier().getPersonen(DossierPersoonType.BETROKKENE);
      for (DossierPersoon dossierPersoon : personen) {
        Record r = addRecord(dossierPersoon);
        r.addValue(++nr);
        String adres = dossierPersoon.getAdres().getAdres_pc_wpl();
        r.addValue(BsPersoonUtils.getNaam(dossierPersoon));
        r.addValue(fil(adres) ? adres : "Geen adres ingevoerd");
        r.addValue(getPersoonslijstStatus(dossierPersoon, persoonslijsten));
      }

      super.setRecords();
    }
  }
}
