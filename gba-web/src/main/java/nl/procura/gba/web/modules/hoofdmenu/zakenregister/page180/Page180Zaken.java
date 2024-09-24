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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.page180;

import static nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakPersoonType.AANGEVER;

import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakPersoonType;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZakenregisterOptiePage;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page181.Page181Zaken;
import nl.procura.gba.web.modules.zaken.correspondentie.overzicht.CorrespondentieOverzichtLayout;
import nl.procura.gba.web.services.zaken.correspondentie.CorrespondentieZaak;

/**
 * Tonen correspondentie
 */
public class Page180Zaken extends ZakenregisterOptiePage<CorrespondentieZaak> {

  public Page180Zaken(CorrespondentieZaak zaak) {
    super(zaak, "Zakenregister - correspondentie");
    addButton(buttonPrev);
  }

  @Override
  protected void addOptieButtons() {
    addOptieButton(buttonDoc);
    addOptieButton(buttonPersonen);
  }

  @Override
  protected void addTabs() {
    addTab(new CorrespondentieOverzichtLayout(getZaak()), "Correspondentie");
  }

  @Override
  protected ZaakPersoonType[] getZoekpersoonTypes() {
    return new ZaakPersoonType[]{ AANGEVER };
  }

  @Override
  protected void goToDocument() {
    getNavigation().goToPage(new Page181Zaken(getZaak()));
  }

  @Override
  protected void goToPersoon(ZaakPersoonType type) {
    goToPersoon("zaken.correspondentie", getZaak().getAnummer(), getZaak().getBurgerServiceNummer());
  }
}
