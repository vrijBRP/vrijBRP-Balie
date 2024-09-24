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

package nl.procura.gba.web.modules.bs.onderzoek.page40;

import static nl.procura.gba.web.modules.bs.onderzoek.page40.Page40OnderzoekBean1.DATUM_EINDE_ONDERZOEK;
import static nl.procura.gba.web.modules.bs.onderzoek.page40.Page40OnderzoekBean1.NOGMAALS_AANSCHRIJVEN;
import static nl.procura.gba.web.modules.bs.onderzoek.page40.Page40OnderzoekBean1.TOELICHTING;
import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.BETROKKENE;
import static nl.procura.gba.web.services.bs.onderzoek.enums.BetrokkeneType.BINNEN_INTER_BRIEF;
import static nl.procura.gba.web.services.bs.onderzoek.enums.BetrokkeneType.BINNEN_INTER_WOON;
import static nl.procura.gba.web.services.bs.onderzoek.enums.BetrokkeneType.EMIGRATIE;
import static nl.procura.gba.web.services.bs.onderzoek.enums.BetrokkeneType.IMMIGRATIE;
import static nl.procura.gba.web.services.bs.onderzoek.enums.BetrokkeneType.NAAR_ANDERE;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.modules.bs.common.pages.persoonpage.BsStatusForm;
import nl.procura.gba.web.modules.bs.onderzoek.BsPageOnderzoek;
import nl.procura.gba.web.modules.bs.onderzoek.adreslayout.AdresLayout;
import nl.procura.gba.web.modules.bs.onderzoek.adreslayout.types.AanleidingToResultaatAdres;
import nl.procura.gba.web.modules.bs.onderzoek.adreslayout.types.ResultaatAdres;
import nl.procura.gba.web.services.bs.onderzoek.enums.BetrokkeneType;
import nl.procura.gba.web.services.gba.basistabellen.gemeente.Gemeente;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

/**
 * Onderzoek - resultaat
 */
public class Page40Onderzoek extends BsPageOnderzoek {

  private Page40OnderzoekForm1 form1;
  private AdresLayout          adresLayout;
  private PlWarningLayout      warningLayout;

  public Page40Onderzoek() {
    super("Onderzoek - resultaat");
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      addButton(buttonPrev);
      addButton(buttonNext);

      adresLayout = new AdresLayout(new ResultaatAdres(getZaakDossier()));

      warningLayout = new PlWarningLayout(getZaakDossier(), true) {

        @Override
        protected void onGeenWijzigingen() {
          infoMessage("Er zijn geen wijzigingen");
        }
      };

      form1 = new Page40OnderzoekForm1(getZaakDossier()) {

        @Override
        protected void onChangeAdresOvernemen() {
          adresLayout.setAdres(new AanleidingToResultaatAdres(getZaakDossier()));
        }

        @Override
        protected void onChangeBetrokkene(BetrokkeneType betrokkeneType) {
          setBetrokkene(betrokkeneType);
        }

        @Override
        protected void onShowWarning(boolean showWarning) {
          if (showWarning) {
            warningLayout.reload(false);
          }
          warningLayout.setVisible(showWarning);
        }
      };

      addComponent(new BsStatusForm(getDossier(), BETROKKENE));
      setInfo("Bepaal aan de hand van de beschikbare informatie wat het resultaat is van het onderzoek. <br/>" +
          "Geef - indien van toepassing - aan of nogmaals aanschrijven van toepassing is. <br/>" +
          "Druk op Volgende (F2) om verder te gaan.");

      addComponent(form1);
      addComponent(adresLayout);
      addComponent(warningLayout);
      setBetrokkene(form1.getBean().getBetrokkene());
    }

    super.event(event);
  }

  private void setBetrokkene(BetrokkeneType betrokkeneType) {
    if (BINNEN_INTER_WOON.equals(betrokkeneType)
        || BINNEN_INTER_BRIEF.equals(betrokkeneType)
        || IMMIGRATIE.equals(betrokkeneType)) {
      adresLayout.setForm(AdresLayout.FormType.BINNEN_GEM);

    } else if (NAAR_ANDERE.equals(betrokkeneType)) {
      adresLayout.setForm(AdresLayout.FormType.BUITEN_GEM);

    } else if (EMIGRATIE.equals(betrokkeneType)) {
      adresLayout.setForm(AdresLayout.FormType.LAND);

    } else {
      adresLayout.setForm(null);
    }
  }

  @Override
  public boolean checkPage() {

    if (super.checkPage()) {

      form1.commit();

      getZaakDossier().setResultaatAdresGelijk(form1.getBean().getHetzelfde());
      getZaakDossier().setResultaatOnderzoekBetrokkene(form1.getBean().getBetrokkene());
      getZaakDossier().setDatumEindeOnderzoek(null);
      getZaakDossier().setNogmaalsAanschrijving(null);
      getZaakDossier().setResultaatToelichting(null);

      getZaakDossier().setResultaatAdres(null);
      getZaakDossier().setResultaatHnr("");
      getZaakDossier().setResultaatHnrL("");
      getZaakDossier().setResultaatHnrT("");
      getZaakDossier().setResultaatHnrA(null);
      getZaakDossier().setResultaatPc(null);
      getZaakDossier().setResultaatPlaats(new FieldValue());
      getZaakDossier().setResultaatGemeente(new FieldValue());
      getZaakDossier().setResultaatGemeentePostbus(new Gemeente());
      getZaakDossier().setResultaatBuitenl1("");
      getZaakDossier().setResultaatBuitenl2("");
      getZaakDossier().setResultaatBuitenl3("");
      getZaakDossier().setResultaatLand(new FieldValue());

      if (form1.getField(DATUM_EINDE_ONDERZOEK) != null) {
        getZaakDossier().setDatumEindeOnderzoek(new DateTime(form1.getBean().getDatumEindeOnderzoek()));
      }

      if (form1.getField(NOGMAALS_AANSCHRIJVEN) != null) {
        getZaakDossier().setNogmaalsAanschrijving(form1.getBean().getNogmaalsAanschrijven());
      }

      if (form1.getField(TOELICHTING) != null) {
        getZaakDossier().setResultaatToelichting(form1.getBean().getToelichting());
      }

      if (adresLayout.isSaved()) {
        getServices().getOnderzoekService().save(getDossier());
        return true;
      }
    }

    return false;
  }

  @Override
  public void onPreviousPage() {
    goToPreviousProces();
  }

  @Override
  public void onNextPage() {
    goToNextProces();
  }
}
