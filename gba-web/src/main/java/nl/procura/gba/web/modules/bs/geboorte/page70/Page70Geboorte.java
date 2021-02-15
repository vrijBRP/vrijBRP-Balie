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

package nl.procura.gba.web.modules.bs.geboorte.page70;

import static java.util.Arrays.asList;
import static nl.procura.gba.web.services.bs.algemeen.enums.DossierNationaliteitDatumVanafType.GEBOORTE_DATUM;
import static nl.procura.gba.web.services.bs.algemeen.functies.BsNatioUtils.heeftNationaliteiten;
import static nl.procura.gba.web.services.bs.algemeen.functies.BsNatioUtils.heeftNederlandseNationaliteit;
import static nl.procura.standard.Globalfunctions.fil;

import nl.procura.gba.web.common.misc.Landelijk;
import nl.procura.gba.web.modules.bs.common.layouts.namenrecht.BsNamenrechtLayout;
import nl.procura.gba.web.modules.bs.common.pages.BsPage;
import nl.procura.gba.web.modules.bs.common.pages.nationaliteitpage.BsNatioLayout;
import nl.procura.gba.web.modules.bs.common.pages.nationaliteitpage.BsNatioType;
import nl.procura.gba.web.modules.bs.common.pages.persoonpage.BsStatusForm;
import nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType;
import nl.procura.gba.web.services.bs.algemeen.functies.BsNatioUtils;
import nl.procura.gba.web.services.bs.algemeen.functies.BsPersoonUtils;
import nl.procura.gba.web.services.bs.algemeen.nationaliteit.DossierNationaliteit;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.geboorte.DossierGeboorte;
import nl.procura.vaadin.component.dialog.ConfirmDialog;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

/**
 * Geboorte
 */
public class Page70Geboorte<T extends DossierGeboorte> extends BsPage<T> {

  private Page70GeboorteForm1 form1                          = null;
  private BsNatioLayout       natioLayout                    = null;
  private BsNamenrechtLayout  namenrechtLayout               = null;
  private boolean             geenNationaliteitenBevestiging = false;

  public Page70Geboorte() {
    this("Geboorte - namenrecht");
  }

  public Page70Geboorte(String title) {

    super(title);

    addButton(buttonPrev);
    addButton(buttonNext);
  }

  @Override
  public boolean checkPage() {

    DossierGeboorte zaakDossier = getZaakDossier();

    if (!heeftNationaliteiten(zaakDossier.getDossier()) && !geenNationaliteitenBevestiging) {

      getWindow().addWindow(new ConfirmDialog(
          "<b>Er is geen nationaliteit toegekend aan het kind.</b><br/>Hierdoor waren mogelijk ook niet alle opties voor het namenrecht beschikbaar.<hr/>Is dit correct?") {

        @Override
        public void buttonYes() {

          geenNationaliteitenBevestiging = true;

          namenrechtLayout.getForm().toDossier();

          Page70Geboorte.this.getApplication().getServices().getGeboorteService().save(getDossier());

          onNextPage();

          super.buttonYes();
        }
      });

      return false;
    }

    namenrechtLayout.getForm().toDossier();

    getApplication().getServices().getGeboorteService().save(getDossier());

    return true;
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      addComponent(new BsStatusForm(getDossier()));

      setInfo(
          "Bepaal de nationaliteit(en) en van welk land het namenrecht moet worden toegepast en druk op Volgende (F2) om verder te gaan.");

      if (isDerdeGeneratie()) {
        addInfo(
            "<hr/><b>Het is waarschijnlijk dat dit kind de Nederlandse nationaliteit heeft op basis van artikel 3 lid 3 RWM (derde generatie Nederlander)</b>");
      }

      form1 = new Page70GeboorteForm1(getZaakDossier());

      initFormulieren();
      initWaarden();

      addComponent(form1);
      addComponent(natioLayout);
      addComponent(namenrechtLayout);

      namenrechtLayout.resetNaamsPersoonType();
    }

    super.event(event);
  }

  @Override
  public void onNextPage() {

    // Een ID opvragen in het Zaak-DMS
    getApplication().getServices().getZaakIdentificatieService().getDmsZaakId(getDossier());

    goToNextProces();
  }

  @Override
  public void onPreviousPage() {
    goToPreviousProces();
    super.onPreviousPage();
  }

  private void checkRecht() {

    DossierGeboorte zaakDossier = getZaakDossier();
    if (heeftNederlandseNationaliteit(zaakDossier.getDossier())) {
      setNederlandsRecht("Het kind heeft de Nederlandse nationaliteit");
    }

    namenrechtLayout.checkRecht();
  }

  private void initFormulieren() {

    form1 = new Page70GeboorteForm1(getZaakDossier());

    natioLayout = new BsNatioLayout(BsNatioType.STANDAARD, getDossier(),
        "Nationaliteiten(en) als gevolg van de geboorte") {

      @Override
      public void toevoegenNationaliteit(DossierNationaliteit nationaliteit) {
        getServices().getDossierService().addNationaliteit(getDossier(), nationaliteit);
        checkRecht();
      }

      @Override
      public void verwijderNationaliteit(DossierNationaliteit nationaliteit) {
        getServices().getDossierService().deleteNationaliteiten(getDossier(), asList(nationaliteit));
        for (DossierPersoon kind : getZaakDossier().getKinderen()) {
          getServices().getDossierService().deleteNationaliteiten(kind, asList(nationaliteit));
        }
        checkRecht();
      }
    };

    namenrechtLayout = new BsNamenrechtLayout(getServices(), getZaakDossier());
    checkRecht();
    namenrechtLayout.resetNaamsPersoonType();
  }

  private void initWaarden() {

    DossierGeboorte impl = getZaakDossier();

    boolean moederNL = heeftNederlandseNationaliteit(impl.getMoeder());
    boolean vaderNL = heeftNederlandseNationaliteit(impl.getVader());

    DossierNationaliteit nationaliteit = new DossierNationaliteit();
    nationaliteit.setNationaliteit(Landelijk.getNederlandse());
    nationaliteit.setVerkrijgingType(GEBOORTE_DATUM);

    if (moederNL) {

      // Als de moeder de NL nationaliteit heeft dan krijgt het kind die ook.

      nationaliteit.setAfgeleidVan(DossierPersoonType.MOEDER);
      natioLayout.toevoegenNationaliteit(nationaliteit);
      natioLayout.setToelichting(
          "Omdat de moeder de Nederlandse nationaliteit heeft " + "krijgt het kind ook de Nederlandse nationaliteit.");
    } else if (vaderNL) {

      // Als de vader de NL nationaliteit heeft dan krijgt het kind die ook.

      nationaliteit.setAfgeleidVan(DossierPersoonType.ERKENNER);
      natioLayout.toevoegenNationaliteit(nationaliteit);
      natioLayout.setToelichting(
          "Omdat de vader / duo-moeder de Nederlandse nationaliteit heeft krijgt het kind ook de Nederlandse nationaliteit.");
    }
  }

  /**
   * Is er sprake van een derde generatie
   */
  private boolean isDerdeGeneratie() {

    DossierPersoon moeder = getZaakDossier().getMoeder();
    DossierPersoon vader = getZaakDossier().getVaderErkenner();

    boolean checkMoeder = (BsPersoonUtils.isGeborenInNederland(
        moeder) && !BsNatioUtils.heeftNederlandseNationaliteit(moeder));
    boolean checkVader = (BsPersoonUtils.isGeborenInNederland(vader) && !BsNatioUtils.heeftNederlandseNationaliteit(
        vader));

    return checkMoeder && checkVader;
  }

  private void setNederlandsRecht(String reden) {

    DossierGeboorte impl = getZaakDossier();

    if (!impl.isNaamRechtBepaald()) {
      namenrechtLayout.getForm().setToegepastRechtVeld(Landelijk.getNederland());
    }

    namenrechtLayout.setToelichting(
        fil(reden) ? (reden + ". Hierdoor is het recht van Nederland van toepassing.") : "Leidt zelf het recht af");
  }
}
