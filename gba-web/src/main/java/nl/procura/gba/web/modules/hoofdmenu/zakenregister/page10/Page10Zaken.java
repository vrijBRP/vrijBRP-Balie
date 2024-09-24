/*
 * Copyright 2024 - 2025 Procura B.V.
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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.page10;

import static nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakPersoonType.AANGEVER;

import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakPersoonType;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakTabsheet;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZakenregisterOptiePage;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page11.Page11Zaken;
import nl.procura.gba.web.modules.zaken.naamgebruik.overzicht.NaamgebruikOverzichtBuilder;
import nl.procura.gba.web.services.zaken.naamgebruik.NaamgebruikAanvraag;

/**
 * Tonen naamgebruikaanvraag
 */

public class Page10Zaken extends ZakenregisterOptiePage<NaamgebruikAanvraag> {

  public Page10Zaken(NaamgebruikAanvraag zaak) {
    super(zaak, "Zakenregister - naamgebruik");
    addButton(buttonPrev);
  }

  @Override
  protected void addOptieButtons() {
    addOptieButton(buttonDoc);
    addOptieButton(buttonPersonen);
    addOptieButton(buttonFiat);
    addOptieButton(buttonVerwerken);
  }

  @Override
  protected void addTabs(ZaakTabsheet<NaamgebruikAanvraag> tabsheet) {
    NaamgebruikOverzichtBuilder.addTab(tabsheet, getZaak());
  }

  @Override
  protected ZaakPersoonType[] getZoekpersoonTypes() {
    return new ZaakPersoonType[]{ AANGEVER };
  }

  @Override
  protected void goToDocument() {
    getNavigation().goToPage(new Page11Zaken(getZaak()));
  }

  @Override
  protected void goToPersoon(ZaakPersoonType type) {
    goToPersoon("zaken.naamgebruik", getZaak().getAnummer(), getZaak().getBurgerServiceNummer());
  }
}
