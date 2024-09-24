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

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.BETROKKENE;
import static nl.procura.standard.Globalfunctions.fil;
import static nl.procura.standard.exceptions.ProExceptionSeverity.WARNING;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.Button;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.web.components.layouts.OptieLayout;
import nl.procura.gba.web.modules.bs.common.pages.persoonpage.BsStatusForm;
import nl.procura.gba.web.modules.bs.onderzoek.BetrokkenenTable;
import nl.procura.gba.web.modules.bs.onderzoek.BsPageOnderzoek;
import nl.procura.gba.web.modules.bs.onderzoek.page11.Page11Onderzoek;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.quicksearch.person.QuickSearchPersonConfig;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.quicksearch.person.SelectListener;
import nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType;
import nl.procura.gba.web.services.bs.algemeen.functies.BsPersoonUtils;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.onderzoek.OnderzoekService;
import nl.procura.gba.web.services.zaken.algemeen.status.ZaakStatusService;
import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.page.pageEvents.AfterReturn;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.validation.Bsn;

public class Page10Onderzoek extends BsPageOnderzoek {

  private Table        table;
  private final Button buttonToevoegen       = new Button("Toevoegen");
  private final Button buttonMedeBetrokkenen = new Button("Gerelateerden");

  public Page10Onderzoek() {
    super("Onderzoek - betreft");
    addButton(buttonPrev);
    addButton(buttonNext);
  }

  @Override
  protected void initPage() {
    super.initPage();

    addComponent(new BsStatusForm(getDossier(), BETROKKENE));

    setInfo("Voeg alleen personen toe die nu op hetzelfde adres staan ingeschreven");

    OptieLayout ol = new OptieLayout();
    ol.getRight().setCaption("Personen");
    ol.getRight().setWidth("160px");
    ol.getRight().addButton(buttonToevoegen, this);
    ol.getRight().addButton(buttonMedeBetrokkenen, this);
    ol.getRight().addButton(buttonDel, this);

    table = new Table();
    ol.getLeft().addComponent(new Fieldset("Betreffende personen", table));
    addComponent(ol);

    if (getZaakDossier().getBetrokkenen().isEmpty()) {
      if (getNavigation().getPage(Page11Onderzoek.class) == null) {
        persoonToevoegen();
        return;
      }
    }
    getNavigation().removePage(Page11Onderzoek.class);
  }

  @Override
  public void event(PageEvent event) {
    if (event.isEvent(AfterReturn.class)) {
      table.init();
    }
    super.event(event);
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

    if (areDifferent(personen)) {
      throw new ProException(WARNING, "De personen hebben verschillende adressen.");
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

  private boolean areDifferent(List<DossierPersoon> personen) {
    return personen.stream()
        .collect(groupingBy(p -> p.getAdres().getAdres_pc_wpl_gem(), counting()))
        .size() > 1;
  }

  @Override
  public void onDelete() {
    List<DossierPersoon> personen = table.getSelectedValues(DossierPersoon.class);
    personen.forEach(persoon -> getServices().getOnderzoekService().deleteBetrokkene(getZaakDossier(), persoon));

    table.init();
    super.onDelete();
  }

  @Override
  public void handleEvent(Button button, int keyCode) {
    if (button == buttonToevoegen) {
      persoonToevoegen();

    } else if (button == buttonMedeBetrokkenen) {
      openMedeBetrokkeneWindow();
    }

    super.handleEvent(button, keyCode);
  }

  private void persoonToevoegen() {
    DossierPersoon dossierPersoon = new DossierPersoon(DossierPersoonType.BETROKKENE);
    getNavigation().goToPage(new Page11Onderzoek(dossierPersoon));
  }

  private void openMedeBetrokkeneWindow() {
    ArrayList<DossierPersoon> personen = table.getSelectedValues(DossierPersoon.class);
    if (personen.isEmpty()) {
      throw new ProException("Selecteer eerst één betrokkene");
    } else {
      DossierPersoon persoon = personen.get(0);
      Bsn bsn = new Bsn(persoon.getBurgerServiceNummer().getStringValue());
      if (bsn.isCorrect()) {
        getParentWindow().addWindow(new Page10OnderzoekRelatieWindow(QuickSearchPersonConfig.builder()
            .bsn(bsn)
            .sameAddress(true)
            .selectListener(getSelectListener())
            .build()));
      } else {
        throw new ProException(WARNING, "Geselecteerde betrokkene is niet ingeschreven in de BRP");
      }
    }
  }

  private SelectListener getSelectListener() {
    return pl -> {
      DossierPersoon dossierPersoon = new DossierPersoon(DossierPersoonType.BETROKKENE);
      BsPersoonUtils.kopieDossierPersoon(pl, dossierPersoon);
      dossierPersoon.setDefinitief(true);
      getZaakDossier().getDossier().toevoegenPersoon(dossierPersoon);
      successMessage("Persoon is toegevoegd");
      table.init();
    };
  }

  public class Table extends BetrokkenenTable {

    @Override
    public void setColumns() {
      setSelectable(true);
      setMultiSelect(false);

      addColumn("Nr.", 50);
      addColumn("Naam", 300);
      addColumn("Adres", 400);
      addColumn("Status").setUseHTML(true);

      super.setColumns();
    }

    @Override
    public void onDoubleClick(Record record) {
      getNavigation().goToPage(new Page11Onderzoek(record.getObject(DossierPersoon.class)));
      super.onDoubleClick(record);
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
