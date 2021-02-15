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

package nl.procura.gba.web.services.bs.naamskeuze.argumenten;

import nl.procura.gba.common.ZaakType;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;

public class NaamskeuzeArgumenten extends ZaakArgumenten {

  private DossierPersoon moeder = null;

  public NaamskeuzeArgumenten() {
    setTypen(ZaakType.NAAMSKEUZE);
  }

  public DossierPersoon getMoeder() {
    return moeder;
  }

  public void setMoeder(DossierPersoon moeder) {
    this.moeder = moeder;
    setBsn(moeder.getBurgerServiceNummer().getLongValue());
  }

  @Override
  public boolean isCorrect() {
    return super.isCorrect() || moeder != null;
  }
}
