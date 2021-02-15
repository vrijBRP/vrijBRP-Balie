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

package nl.procura.gba.web.services.bs.lv.omzetting;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.services.bs.algemeen.akte.DossierAkte;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.lv.Lv;
import nl.procura.gba.web.services.bs.lv.LvRechtsfeitAkte;
import nl.procura.gba.web.services.bs.omzetting.DossierOmzetting;

public class LvOmzetting implements Lv {

  private final LvRechtsfeitAkte rechtsfeitAkte = new LvRechtsfeitAkte();
  private DossierAkte            akte           = new DossierAkte();
  private DossierPersoon         partner1       = new DossierPersoon();
  private DossierPersoon         partner2       = new DossierPersoon();
  private DateTime               datum          = null;
  private DossierOmzetting       zaak;

  public DossierAkte getAkte() {
    return akte;
  }

  public void setAkte(DossierAkte akte) {
    this.akte = akte;
  }

  public DateTime getDatum() {
    return datum;
  }

  public void setDatum(DateTime datum) {
    this.datum = datum;
  }

  public LvRechtsfeitAkte getGpsAkte() {
    return rechtsfeitAkte;
  }

  public DossierPersoon getPartner1() {
    return partner1;
  }

  public void setPartner1(DossierPersoon partner1) {
    this.partner1 = partner1;
  }

  public DossierPersoon getPartner2() {
    return partner2;
  }

  public void setPartner2(DossierPersoon partner2) {
    this.partner2 = partner2;
  }

  public DossierOmzetting getZaak() {
    return zaak;
  }

  public void setZaak(DossierOmzetting zaak) {
    this.zaak = zaak;
  }
}
