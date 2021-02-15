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

package nl.procura.gba.web.services.beheer.bsm;

import nl.procura.gba.web.services.zaken.algemeen.Zaak;

public class BsmMijnOverheidBestand extends BsmBestand {

  private final Zaak   zaak;
  private final String berichttype;

  public BsmMijnOverheidBestand(String bsmId, String berichttype, Zaak zaak, String bestandsnaam, String extension,
      byte[] bytes) {
    super(bsmId, bestandsnaam, extension, bytes);
    this.berichttype = berichttype;
    this.zaak = zaak;
  }

  public String getBerichttype() {
    return berichttype;
  }

  public Zaak getZaak() {
    return zaak;
  }
}
