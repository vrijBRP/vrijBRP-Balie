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

package nl.procura.gba.web.services.beheer.bag;

import static org.apache.commons.lang3.StringUtils.*;

import nl.procura.gba.web.services.interfaces.address.AddressSourceType;
import nl.procura.geo.rest.domain.pdok.locationserver.LocationServerDoc;
import nl.procura.validation.Postcode;

/**
 * Address implementation for information from the PDOK location server
 */
public class PdokLocationServiceAddress extends AbstractAddress {

  private PdokLocationServiceAddress() {
  }

  public PdokLocationServiceAddress(LocationServerDoc a) {
    sourceType = AddressSourceType.BAG;
    aonId = defaultString(a.getAdresseerbaarobjectId());
    inaId = defaultString(a.getNummeraanduidingId());
    street = defaultString(a.getStraatnaam());
    hnr = defaultString(a.getHuisnummer() != null ? a.getHuisnummer().toString() : null);
    hnrL = defaultString(a.getHuisletter());
    hnrT = defaultString(a.getHuisnummerToevoeging());
    pc = defaultString(a.getPostcode());
    recidenceCode = defaultString(a.getWoonplaatscode());
    recidenceName = defaultString(a.getWoonplaatsnaam());
    municipalityCode = defaultString(a.getGemeentecode());
    municipalityName = defaultString(a.getGemeentenaam());
    district = defaultString(a.getWijknaam());
    neighborhood = defaultString(a.getBuurtnaam());
    sourceType = AddressSourceType.BAG;

    if (isBlank(a.getWeergavenaam())) {
      String hnrString = normalizeSpace(hnr + hnrL + " " + hnrT);
      label = normalizeSpace(street + " " + hnrString + ", " + Postcode.getFormat(pc) + " " + recidenceName);
    } else {
      label = a.getWeergavenaam();
    }
  }
}
