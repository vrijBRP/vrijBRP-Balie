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

package nl.procura.gba.web.modules.bs.common.pages.aktepage.page2;

import static nl.procura.gba.common.MiscUtils.setClass;

import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.bs.common.pages.aktepage.BsAktePage;
import nl.procura.gba.web.services.bs.algemeen.ZaakDossier;
import nl.procura.gba.web.services.bs.algemeen.akte.AkteListener;
import nl.procura.gba.web.services.bs.algemeen.akte.DossierAkte;
import nl.procura.gba.web.services.bs.algemeen.akte.DossierAkteInvoerType;
import nl.procura.gba.web.services.bs.algemeen.akte.DossierAkteNummer;
import nl.procura.gba.web.services.bs.algemeen.functies.BsPersoonUtils;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.zaken.algemeen.samenvatting.ToonAktePersoonUtils;
import nl.procura.vaadin.component.dialog.ConfirmDialog;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

/**
 * Akte pagina
 */

public class BsAktePage2<T extends ZaakDossier> extends BsAktePage<T> {

  private final DossierAkte akte;
  private BsAktePageForm    form   = null;
  private Table1            table1 = null;

  public BsAktePage2(DossierAkte akte) {

    super("Aktegegevens");

    this.akte = akte;

    addButton(buttonPrev);
    addButton(buttonNext);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      setInfo("Controleer de aktegegevens.");

      form = new BsAktePageForm(getApplication(), getZaakDossier().getDossier(), akte);
      table1 = new Table1();

      addComponent(form);
      addComponent(table1);
    }

    super.event(event);
  }

  @Override
  public void onNextPage() {

    form.commit();

    final DossierAkteNummer akteNummer = form.getAkteNummer();

    if (akte.isCorrect()) { // Akte reeds ingevuld
      opslaan(akteNummer);
    } else {

      // Zet invoertype op personen voor verdere controles
      akte.setInvoerType(DossierAkteInvoerType.PROWEB_PERSONEN);

      if (getServices().getAkteService().isCheck(akte, akteNummer, new MyAkteListener())) {

        ConfirmDialog confirmDialog = new ConfirmDialog(
            "Wilt u aktenummer " + akteNummer.getAkte() + " toekennen?") {

          @Override
          public void buttonYes() {
            closeWindow();
            opslaan(akteNummer);
          }
        };

        getParentWindow().addWindow(confirmDialog);
      }
    }
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }

  private void opslaan(final DossierAkteNummer akteNummer) {

    // Invoertype altijd op personen zetten.
    akte.setInvoerType(DossierAkteInvoerType.PROWEB_PERSONEN);

    getServices().getAkteService().save(getZaakDossier().getDossier(), akte, akteNummer, false);

    if (isAktesCorrect()) {
      onPreviousPage();
    } else {
      naarVolgendeAkte();
    }
  }

  public class MyAkteListener implements AkteListener {

    @Override
    public void verkeerdeAkte(final DossierAkteNummer akteNummer, long verkeerdNr, long correctNr) {

      StringBuilder tekst = new StringBuilder();
      tekst.append("Eigenlijk is volgnummer <b>%d</b> het eerstvolgende vrije nummer voor deze akte.");
      tekst.append("<br/>Wilt u toch volgnummer <b>%d</b> toekennen?");
      String msg = String.format(tekst.toString(), correctNr, verkeerdNr);

      AkteConfirmDialog window = new AkteConfirmDialog(msg) {

        @Override
        public void buttonYes() {
          opslaan(akteNummer);
          super.buttonYes();
        }
      };

      getWindow().addWindow(window);
    }
  }

  class Table1 extends GbaTable {

    @Override
    public void setColumns() {

      addColumn("Type persoon", 120);
      addColumn("Persoon").setUseHTML(true);

      super.setColumns();
    }

    @Override
    public void setRecords() {

      for (DossierPersoon person : BsPersoonUtils.sort(akte.getPersonen())) {
        if (ToonAktePersoonUtils.toon(getZaakDossier(), person)) {
          String naam = person.getNaam().getPred_adel_voorv_gesl_voorn();
          Record record = addRecord(person);
          record.addValue(person.getDossierPersoonType());
          record.addValue(person.isVolledig() ? naam : setClass(false, "Nog niet aangegeven"));
        }
      }

      super.setRecords();
    }
  }
}
