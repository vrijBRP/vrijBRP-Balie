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

package nl.procura.gba.web.modules.bs.erkenning.page30;

import static java.util.Arrays.asList;
import static nl.procura.gba.web.services.bs.algemeen.enums.DossierNationaliteitDatumVanafType.ERKENNING_AANGIFTE_DATUM;
import static nl.procura.gba.web.services.bs.algemeen.enums.DossierNationaliteitDatumVanafType.GEBOORTE_DATUM;
import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.ERKENNER;
import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.MOEDER;
import static nl.procura.gba.web.services.bs.algemeen.functies.BsNatioUtils.heeftNederlandseNationaliteit;
import static nl.procura.standard.Globalfunctions.fil;

import nl.procura.gba.web.common.misc.Landelijk;
import nl.procura.gba.web.modules.bs.common.layouts.namenrecht.BsNamenrechtLayout;
import nl.procura.gba.web.modules.bs.common.pages.nationaliteitpage.BsNatioLayout;
import nl.procura.gba.web.modules.bs.common.pages.nationaliteitpage.BsNatioType;
import nl.procura.gba.web.modules.bs.common.pages.persoonpage.BsStatusForm;
import nl.procura.gba.web.modules.bs.erkenning.BsPageErkenning;
import nl.procura.gba.web.services.bs.algemeen.functies.BsNatioUtils;
import nl.procura.gba.web.services.bs.algemeen.nationaliteit.DossierNationaliteit;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.erkenning.DossierErkenning;
import nl.procura.gba.web.services.bs.erkenning.KindLeeftijdsType;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

/**
 * Erkenning
 */

public class Page30Erkenning extends BsPageErkenning {

  private Page30ErkenningForm1 form1 = null;
  private Page30ErkenningForm2 form2 = null;

  private BsNatioLayout      natioLayout      = null;
  private BsNamenrechtLayout namenrechtLayout = null;

  public Page30Erkenning() {

    super("Erkenning - namenrecht");

    addButton(buttonPrev);
    addButton(buttonNext);
  }

  @Override
  public boolean checkPage() {

    namenrechtLayout.getForm().toDossier();

    getServices().getErkenningService().save(getDossier());

    return true;
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      addComponent(new BsStatusForm(getZaakDossier().getDossier()));

      setInfo(
          "Bepaal de nationaliteit(en), het toe te passen namenrecht en de geslachtsnaam en druk op Volgende (F2) om verder te gaan.");

      initFormulieren();
      initWaarden();

      addComponent(form1);

      if (getZaakDossier().isBestaandKind()) {
        addComponent(form2);
      }

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
  }

  private void checkRecht() {

    DossierErkenning impl = getZaakDossier();

    if (heeftNederlandseNationaliteit(impl.getDossier())) {
      setNederlandsRecht("Het kind krijgt de Nederlandse nationaliteit");
    }

    namenrechtLayout.checkRecht();
  }

  private void initFormulieren() {

    form1 = new Page30ErkenningForm1(getZaakDossier()) {

      @Override
      protected DossierNationaliteit getNationaliteitErkenner() {
        return getZaakDossier().getErkenner().getNationaliteit();
      }

      @Override
      protected DossierNationaliteit getNationaliteitMoeder() {
        return getZaakDossier().getMoeder().getNationaliteit();
      }
    };

    form2 = new Page30ErkenningForm2(getZaakDossier());

    String bsNatioCaption = "Nationaliteiten(en) als gevolg van de erkenning";

    if (getZaakDossier().isOngeborenVrucht()) {
      bsNatioCaption = "Nationaliteiten(en) ten behoeve van het namenrecht";
    }

    natioLayout = new BsNatioLayout(BsNatioType.STANDAARD, getDossier(), bsNatioCaption) {

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

          checkRecht();
        }
      }
    };

    namenrechtLayout = new BsNamenrechtLayout(getServices(), getZaakDossier());

    checkRecht();
  }

  private void initNederlandseNationaliteit(DossierErkenning impl) {

    final boolean moederNL = heeftNederlandseNationaliteit(impl.getMoeder());
    final boolean erkennerNL = heeftNederlandseNationaliteit(impl.getErkenner());

    // Als kind al Nederlandse Nationaliteit heeft dan deze niet nogmaals toevoegen
    // als gevolg van de erkenning
    if (impl.isGelijkMetGeboorte()) {
      if (BsNatioUtils.heeftNederlandseNationaliteit(impl.getDossierGeboorte().getDossier())) {
        return;
      }
    }

    // Geldt alleen voor ongeboren kinderen
    if (!isBestaandKindNL(impl)) {

      if (impl.getKindLeeftijdsType() == KindLeeftijdsType.JONGER_DAN_7) {

        final DossierNationaliteit nationaliteit = new DossierNationaliteit();
        nationaliteit.setNationaliteit(Landelijk.getNederlandse());
        nationaliteit.setVerkrijgingType(impl.isOngeborenVrucht() ? GEBOORTE_DATUM : ERKENNING_AANGIFTE_DATUM);

        if (moederNL) {

          // Als de moeder de NL nationaliteit heeft dan krijgt het kind die ook.

          nationaliteit.setAfgeleidVan(MOEDER);
          natioLayout.toevoegenNationaliteit(nationaliteit);

          // Alleen bij ongeboren vrucht deze tekst tonen

          if (!impl.isBestaandKind()) {
            natioLayout.setToelichting(
                "Omdat de moeder de Nederlandse nationaliteit heeft "
                    + "krijgt het kind ook de Nederlandse nationaliteit.");
          }
        } else if (erkennerNL) {

          // Als de erkenner de NL nationaliteit heeft dan krijgt het kind die ook.

          nationaliteit.setAfgeleidVan(ERKENNER);
          natioLayout.toevoegenNationaliteit(nationaliteit);

          if (impl.isBestaandKind()) {
            natioLayout.setToelichting(
                "Omdat de erkenner de Nederlandse nationaliteit heeft "
                    + "én het kind jonger is dan 7 jaar krijgt het kind ook de Nederlandse nationaliteit.");
          } else {
            natioLayout.setToelichting(
                "Omdat de erkenner de Nederlandse nationaliteit heeft "
                    + "krijgt het kind ook de Nederlandse nationaliteit.");
          }
        }
      } else {

        // Ouder dan 7

        if ((moederNL || erkennerNL) && !isBestaandKindNL(impl)) {
          natioLayout.setToelichting(
              "Het kind is ouder dan 6 jaar en krijgt dus terecht niet de Nederlandse nationaliteit. "
                  + "Een minderjarige ouder dan 6 jaar die wordt erkend door een Nederlander verkrijgt alleen de Nederlandse "
                  + "nationaliteit als na DNA-onderzoek het biologische vaderschap is vastgesteld.");
        }
      }
    }
  }

  private void initWaarden() {

    DossierErkenning impl = getZaakDossier();

    initNederlandseNationaliteit(impl);

    if (isOngeborenKindNL(impl)) {
      setNederlandsRecht("Het kind krijgt de Nederlandse nationaliteit");
    } else if (isBestaandKindNL(impl)) {
      setNederlandsRecht("Het kind heeft de Nederlandse nationaliteit");
    }
  }

  private boolean isBestaandKindNL(DossierErkenning impl) {
    return (impl.isBestaandKind() && heeftNederlandseNationaliteit(impl.getKinderen()));
  }

  private boolean isOngeborenKindNL(DossierErkenning impl) {
    return (impl.isOngeborenVrucht() && heeftNederlandseNationaliteit(impl.getDossier()));
  }

  private void setNederlandsRecht(String reden) {

    DossierErkenning impl = getZaakDossier();

    if (!impl.isNaamRechtBepaald()) {
      namenrechtLayout.getForm().setToegepastRechtVeld(Landelijk.getNederland());
    }

    namenrechtLayout.setToelichting(
        fil(reden) ? (reden + ". Hierdoor is het recht van Nederland van toepassing.") : "Leidt zelf het recht af");
  }
}
