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

package nl.procura.gba.web.modules.bs.naamskeuze.page30;

import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.MOEDER;
import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.PARTNER;
import static nl.procura.gba.web.services.bs.algemeen.functies.BsNatioUtils.heeftNederlandseNationaliteit;

import java.util.List;

import nl.procura.gba.web.common.misc.Landelijk;
import nl.procura.gba.web.modules.bs.common.layouts.namenrecht.BsNamenrechtLayout;
import nl.procura.gba.web.modules.bs.common.pages.persoonpage.BsStatusForm;
import nl.procura.gba.web.modules.bs.naamskeuze.BsPageNaamskeuze;
import nl.procura.gba.web.services.bs.algemeen.nationaliteit.DossierNationaliteit;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.naamskeuze.DossierNaamskeuze;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page30Naamskeuze extends BsPageNaamskeuze {

  private Page30NaamskeuzeForm1 form1            = null;
  private Page30NaamskeuzeForm2 form2            = null;
  private BsNamenrechtLayout    namenrechtLayout = null;

  public Page30Naamskeuze() {
    super("Naamskeuze - namenrecht en keuze");
    addButton(buttonPrev);
    addButton(buttonNext);
  }

  @Override
  public boolean checkPage() {
    namenrechtLayout.getForm().toDossier();
    getServices().getNaamskeuzeService().save(getDossier());
    return true;
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {
      addComponent(new BsStatusForm(getZaakDossier().getDossier()));
      setInfo("Bepaal het toe te passen namenrecht en de geslachtsnaam " +
          "en druk op Volgende (F2) om verder te gaan.");

      initFormulieren();
      initWaarden();

      addComponent(form1);

      if (getZaakDossier().isBestaandKind()) {
        addComponent(form2);
      }

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

  private void initFormulieren() {
    form1 = new Page30NaamskeuzeForm1(getZaakDossier()) {

      @Override
      protected DossierNationaliteit getNationaliteitErkenner() {
        return getZaakDossier().getPartner().getNationaliteit();
      }

      @Override
      protected DossierNationaliteit getNationaliteitMoeder() {
        return getZaakDossier().getMoeder().getNationaliteit();
      }
    };

    form2 = new Page30NaamskeuzeForm2(getZaakDossier());
    namenrechtLayout = new BsNamenrechtLayout(getServices(), getZaakDossier());
  }

  private void initWaarden() {

    DossierNaamskeuze impl = getZaakDossier();

    StringBuilder toelichting = new StringBuilder();
    toelichting.append("De moeder of de partner van de moeder of het kind zelf (bij naamskeuze bestaand kind) ")
        .append("moeder de Nederlandse nationaliteit bezitten om naamskeuze te kunnen doen.")
        .append("<br>Controleer of dit het geval is. Dan is het namenrecht van Nederland van toepassing");

    if (isMoederHeeftNL(impl)) {
      setNederlandsRecht();
      toelichting.append("<hr>De moeder heeft de Nederlandse nationaliteit. " +
          "Hierdoor is het recht van Nederland van toepassing.");

    } else if (isPartnerHeeftNL(impl)) {
      setNederlandsRecht();
      toelichting.append("<hr>De partner heeft de Nederlandse nationaliteit. " +
          "Hierdoor is het recht van Nederland van toepassing.");

    } else if (isBestaandKindNL(impl)) {
      setNederlandsRecht();
      toelichting.append("<hr>Het kind heeft de Nederlandse nationaliteit. " +
          "Hierdoor is het recht van Nederland van toepassing.");
    }

    namenrechtLayout.setToelichting(toelichting.toString());
  }

  private boolean isBestaandKindNL(DossierNaamskeuze impl) {
    return (impl.isBestaandKind() && heeftNederlandseNationaliteit(impl.getKinderen()));
  }

  private boolean isMoederHeeftNL(DossierNaamskeuze impl) {
    List<DossierPersoon> personen = impl.getDossier().getPersonen(MOEDER);
    return (impl.isVoorGeboorte() && heeftNederlandseNationaliteit(personen));
  }

  private boolean isPartnerHeeftNL(DossierNaamskeuze impl) {
    List<DossierPersoon> personen = impl.getDossier().getPersonen(PARTNER);
    return (impl.isVoorGeboorte() && heeftNederlandseNationaliteit(personen));
  }

  private void setNederlandsRecht() {
    DossierNaamskeuze impl = getZaakDossier();
    if (!impl.isNaamRechtBepaald()) {
      namenrechtLayout.getForm().setToegepastRechtVeld(Landelijk.getNederland());
    }
  }
}
