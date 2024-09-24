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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.page80;

import static nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakPersoonType.AANGEVER;

import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZaakPersoonType;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.ZakenregisterOptiePage;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page81.Page81Zaken;
import nl.procura.gba.web.modules.zaken.tmv.layouts.reacties.TmvReactieLayout;
import nl.procura.gba.web.modules.zaken.tmv.page4.Page4Tmv;
import nl.procura.gba.web.modules.zaken.tmv.page5.Page5Tmv;
import nl.procura.gba.web.modules.zaken.tmv.page6.Page6Tmv;
import nl.procura.gba.web.services.zaken.tmv.TerugmeldingAanvraag;

/**
 * Tonen terugmelding
 */
public class Page80Zaken extends ZakenregisterOptiePage<TerugmeldingAanvraag> {

  public Page80Zaken(TerugmeldingAanvraag aanvraag) {
    super(aanvraag, "Zakenregister - terugmelding");
    addButton(buttonPrev);
  }

  @Override
  public void goToDocument() {
    getNavigation().goToPage(new Page81Zaken(getZaak()));
  }

  @Override
  protected void addOptieButtons() {
    addOptieButton(buttonDoc);
    addOptieButton(buttonPersonen);
  }

  @Override
  protected void addTabs() {

    addTab(new Page4Tmv(getZaak()), "Terugmelding");
    addTab(new Page5Tmv(getZaak()), "Afhandeling");
    addTab(new Page6Tmv(getZaak()), "Landelijke registratie");

    TmvReactieLayout tmvReactieLayout = new TmvReactieLayout(getZaak());
    tmvReactieLayout.setTab(addTab(tmvReactieLayout, tmvReactieLayout.getHeader()));
  }

  @Override
  protected ZaakPersoonType[] getZoekpersoonTypes() {
    return new ZaakPersoonType[]{ AANGEVER };
  }

  @Override
  protected void goToPersoon(ZaakPersoonType type) {
    goToPersoon("zaken.tmv", getZaak().getAnummer(), getZaak().getBurgerServiceNummer());
  }
}
