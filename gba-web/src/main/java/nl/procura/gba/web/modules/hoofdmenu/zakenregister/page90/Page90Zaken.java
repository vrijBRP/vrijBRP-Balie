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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.page90;

import static nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakPersoonType.AANGEVER;
import static nl.procura.standard.Globalfunctions.along;

import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakPersoonType;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakTabsheet;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZakenregisterOptiePage;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page91.Page91Zaken;
import nl.procura.gba.web.modules.zaken.rijbewijs.errorpage.RijbewijsErrorWindow;
import nl.procura.gba.web.modules.zaken.rijbewijs.overzicht.RijbewijsOverzichtBuilder;
import nl.procura.gba.web.services.zaken.rijbewijs.NrdServices;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsAanvraag;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsAanvraagAntwoord;
import nl.procura.gba.web.services.zaken.rijbewijs.converters.P1659ToDocumentAanvraag;
import nl.procura.rdw.messages.P1659;
import nl.procura.rdw.processen.p1659.f02.AANVRRYBKRT;

/**
 * Tonen rijbewijsaanvraag
 */

public class Page90Zaken extends ZakenregisterOptiePage<RijbewijsAanvraag> {

  public Page90Zaken(RijbewijsAanvraag aanvraag) {
    super(aanvraag, "Zakenregister - rijbewijs");
    addButton(buttonPrev);
  }

  @Override
  protected void addOptieButtons() {

    addOptieButton(buttonDoc);
    addOptieButton(buttonPersonen);
  }

  @Override
  protected void addTabs(ZaakTabsheet<RijbewijsAanvraag> tabsheet) {
    RijbewijsOverzichtBuilder.addTab(tabsheet, getZaak());
  }

  @Override
  protected ZaakPersoonType[] getZoekpersoonTypes() {
    return new ZaakPersoonType[]{ AANGEVER };
  }

  @Override
  protected void goToDocument() {

    P1659 p1659 = new P1659();
    p1659.newF1(along(getZaak().getAanvraagNummer()));

    if (NrdServices.sendMessage(getServices().getRijbewijsService(), p1659)) {
      RijbewijsAanvraagAntwoord antwoord = P1659ToDocumentAanvraag.get(
          (AANVRRYBKRT) p1659.getResponse().getObject(), getServices());

      getNavigation().goToPage(new Page91Zaken(antwoord, getZaak()));
    } else {

      getParentWindow().addWindow(new RijbewijsErrorWindow(p1659.getResponse().getMelding()));
    }
  }

  @Override
  protected void goToPersoon(ZaakPersoonType type) {
    goToPersoon("zaken.rijbewijs", getZaak().getAnummer(), getZaak().getBurgerServiceNummer());
  }
}
