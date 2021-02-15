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

package nl.procura.gba.web.rest.v1_0.zaak.verwerken.riskanalysis.rules;

import static nl.procura.gba.web.services.zaken.verhuizing.FunctieAdres.ONBEKEND;
import static nl.procura.gba.web.services.zaken.verhuizing.FunctieAdres.WOONADRES;

import nl.procura.diensten.gba.ple.openoffice.formats.Adresformats;
import nl.procura.gba.web.services.zaken.verhuizing.FunctieAdres;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RuleAddress {

  private boolean      isCaseAddress = false; // true == address from the new relocation case
  private long         countryCode   = -1;
  private Adresformats address;
  private long         dIn;
  private FunctieAdres addressUsage;          // Type of usage for address (living (W) or letter (B))

  /**
   * Workaround. Sometimes addressusage is not stored in the table.
   * It should default to WOONADRES then.
   */
  public RuleAddress setAddressUsage(FunctieAdres addressUsage) {
    this.addressUsage = ONBEKEND.equals(addressUsage) ? WOONADRES : addressUsage;
    return this;
  }
}
