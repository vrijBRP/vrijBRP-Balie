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

package nl.procura.gba.web.modules.hoofdmenu.gv.page2;

import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.BETROKKENE;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.components.layouts.tabsheet.GbaTabsheet;
import nl.procura.gba.web.modules.hoofdmenu.gv.PageGvTemplate;
import nl.procura.gba.web.modules.hoofdmenu.gv.overzicht.GvOverzichtTable;
import nl.procura.gba.web.services.bs.algemeen.functies.BsPersoonUtils;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.zaken.algemeen.koppelenumeratie.KoppelEnumeratieType;
import nl.procura.gba.web.services.zaken.gv.GegevensVerstrekkingService;
import nl.procura.gba.web.services.zaken.gv.GvAanvraag;
import nl.procura.gba.web.services.zaken.gv.GvAanvraagProces;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page2Gv extends PageGvTemplate {

  private final GbaTabsheet      tabSheet        = new GbaTabsheet();
  private final GvAanvraag       zaak;
  private final GvAanvraagProces zaakProces      = new GvAanvraagProces();
  private Page2GvForm1           persoonForm     = null;
  private Page2GvForm2           verzoekForm     = null;
  private Page2GvForm3           adresseringForm = null;
  private Page2GvForm4           procesForm      = null;
  private GvOverzichtTable       table;

  public Page2Gv(GvAanvraag zaak) {
    super("Gegevensverstrekking - aanvraag");
    this.zaak = zaak;
    setMargin(true);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      addButton(buttonPrev);
      addButton(buttonPreview);
      addButton(buttonPrint);
      addButton(buttonReset);

      setInfo("Deze zaak heeft een belangenafweging");

      setBetrokkene(new DossierPersoon(BETROKKENE));
      BsPersoonUtils.kopieDossierPersoon(zaak.getBasisPersoon(), getBetrokkene());

      persoonForm = new Page2GvForm1(getBetrokkene());
      verzoekForm = new Page2GvForm2(zaak);
      adresseringForm = new Page2GvForm3(zaak);
      table = new GvOverzichtTable(zaak);

      tabSheet.addTab(persoonForm, "Betrokkene");
      tabSheet.addTab(verzoekForm, "Verzoek");
      tabSheet.addTab(adresseringForm, "Adressering");

      addComponent(tabSheet);

      addComponent(new Fieldset("Huidige procesinformatie", table));

      procesForm = new Page2GvForm4("Nieuwe procesinformatie") {

        @Override
        public void wijzig(KoppelEnumeratieType procesActie, KoppelEnumeratieType reactie) {

          if (verzoekForm != null) {

            KoppelEnumeratieType grondslag = zaak.getGrondslagType();
            KoppelEnumeratieType toekenning = zaak.getToekenningType();

            checkPrintTable(grondslag, toekenning, procesActie, reactie, isEigenGemeente(),
                isVerstrekkingsBeperking());
          }
        }
      };

      addComponent(procesForm);

      setPrintTable(new PrintTable(null));

      addComponent(getPrintTable());

      buttonPrev.setEnabled(getNavigation().getPages().size() > 1);
    }

    super.event(event);
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }

  @Override
  protected void validatePrint(boolean isPreview) {

    GegevensVerstrekkingService gv = getServices().getGegevensverstrekkingService();

    if (procesForm.getParent() != null) {
      procesForm.commit();
    }

    Page2GvBean4 procesBean = procesForm.getBean();
    zaakProces.setProcesActieType(procesBean.getActiesoort());
    zaakProces.setDatumEindeTermijn(new DateTime(procesBean.getDatumEindeTermijn()));
    zaakProces.setReactieType(procesBean.getReactie());
    zaakProces.setMotivering(procesBean.getMotivering());

    zaak.getProcessen().setProces(zaakProces);

    if (isPreview) {
      doPrint(zaak, true);
    } else {
      doPrint(zaak, false);

      gv.saveProces(zaak, zaakProces);

      successMessage("De gegevens zijn opgeslagen.");

      table.init();
    }
  }
}
