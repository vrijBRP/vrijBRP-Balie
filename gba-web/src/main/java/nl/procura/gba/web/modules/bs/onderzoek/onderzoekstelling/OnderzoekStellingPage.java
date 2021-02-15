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

package nl.procura.gba.web.modules.bs.onderzoek.onderzoekstelling;

import static nl.procura.gba.common.MiscUtils.setClass;

import java.util.List;

import com.vaadin.ui.Button;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.modules.bs.onderzoek.BetrokkenenTable;
import nl.procura.gba.web.services.bs.algemeen.functies.BsPersoonUtils;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.onderzoek.DossierOnderzoek;
import nl.procura.gba.web.services.bs.onderzoek.OnderzoekService;
import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.info.InfoLayout;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.theme.twee.ProcuraTheme;

public class OnderzoekStellingPage extends NormalPageTemplate {

  private final Table table;

  private final Button           buttonReload       = new Button("Herladen");
  private final Button           buttonInOnderzoek  = new Button("Zet in onderzoek");
  private final Button           buttonUitOnderzoek = new Button("Haal uit onderzoek");
  private final DossierOnderzoek zaakDossier;

  public OnderzoekStellingPage(DossierOnderzoek zaakDossier) {
    this.zaakDossier = zaakDossier;
    table = new Table();

    buttonInOnderzoek.setEnabled(zaakDossier.isOnderzoekGestart());
    addButton(buttonInOnderzoek);
    addButton(buttonUitOnderzoek);
    addButton(buttonReload, 1f);
    addButton(buttonClose);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {
      if (!zaakDossier.isOnderzoekGestart()) {
        String message = setClass(false, "Verwerking op de persoonslijst is niet van " +
            "toepassing aangezien er geen onderzoek wordt gestart.");
        addComponent(new InfoLayout("", ProcuraTheme.ICOON_24.WARNING, message));
      }

      addComponent(new Fieldset("Betreffende personen", table));
    }

    super.event(event);
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if (buttonReload.equals(button)) {
      reload();
    } else if (buttonInOnderzoek.equals(button)) {
      verwerk(true);
    } else if (buttonUitOnderzoek.equals(button)) {
      verwerk(false);
    }

    super.handleEvent(button, keyCode);
  }

  private void verwerk(boolean inOnderzoek) {
    List<DossierPersoon> personen = table.getSelectedValues(DossierPersoon.class);
    if (personen.isEmpty()) {
      throw new ProException("Geen personen geselecteerd.");
    }
    OnderzoekService onderzoekService = getServices().getOnderzoekService();
    personen.forEach(dossierPersoon -> {
      if (inOnderzoek) {
        onderzoekService.setInOnderzoek(zaakDossier, dossierPersoon.getBurgerServiceNummer());
      } else {
        onderzoekService.haalUitOnderzoek(zaakDossier, dossierPersoon.getBurgerServiceNummer());
      }
    });

    reload();
  }

  @Override
  public void onClose() {
    getWindow().closeWindow();
  }

  @Override
  public void onEnter() {
    onSearch();
  }

  public boolean checkPage() {
    List<DossierPersoon> personen = table.getAllValues(DossierPersoon.class);
    for (DossierPersoon dossierPersoon : personen) {
      if (dossierPersoon.isVolledig()) {
        zaakDossier.getDossier().toevoegenPersoon(dossierPersoon);
      }
    }

    getApplication().getServices().getOnderzoekService().save(zaakDossier.getDossier());
    return true;
  }

  private void reload() {
    // Clear alle cached PL
    getServices().getPersonenWsService().getOpslag().clear();

    // Load the table
    table.init();
  }

  public class Table extends BetrokkenenTable {

    @Override
    public void setColumns() {

      setSelectable(true);
      setMultiSelect(true);

      addColumn("Nr.", 50);
      addColumn("Naam");
      addColumn("Aanduiding op persoonslijst", 450).setUseHTML(true);

      super.setColumns();
    }

    @Override
    public void setRecords() {

      List<BasePLExt> persoonslijsten = getAanduidingen(zaakDossier);

      int nr = 0;
      for (DossierPersoon d : zaakDossier.getBetrokkenen()) {
        Record r = addRecord(d);
        r.addValue(++nr);
        r.addValue(BsPersoonUtils.getNaam(d));
        r.addValue(getAanduidingStatus(d, persoonslijsten));
      }

      super.setRecords();
    }

  }
}
