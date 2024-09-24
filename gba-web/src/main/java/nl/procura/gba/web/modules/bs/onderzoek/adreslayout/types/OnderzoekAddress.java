/*
 * Copyright 2023 - 2024 Procura B.V.
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

package nl.procura.gba.web.modules.bs.onderzoek.adreslayout.types;

import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.normalizeSpace;

import nl.procura.gba.web.modules.bs.onderzoek.adreslayout.OnderzoekAdres;
import nl.procura.gba.web.services.beheer.bag.AbstractAddress;

public class OnderzoekAddress extends AbstractAddress {

  public OnderzoekAddress(OnderzoekAdres adres) {
    street = defaultString(adres.getAdres().getStringValue());
    hnr = defaultString(adres.getHnr());
    hnrL = defaultString(adres.getHnrL());
    hnrT = defaultString(adres.getHnrT());
    pc = defaultString(adres.getPc().getStringValue());
    String hnrString = normalizeSpace(hnr + hnrL + " " + hnrT);
    label = normalizeSpace(street + " " + hnrString + ", " + pc + " " + recidenceName);
  }
}
