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

import static org.apache.commons.lang3.StringUtils.normalizeSpace;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.extensions.formats.Adres;
import nl.procura.gba.web.services.interfaces.address.AddressSourceType;
import nl.procura.validation.Postcode;

/**
 * Address implementation for adress information from the personslist
 */
public class ProcuraPersonListAddress extends AbstractAddress {

  private ProcuraPersonListAddress() {
  }

  public ProcuraPersonListAddress(BasePLExt pl) {

    sourceType = AddressSourceType.PERSONLIST;
    Adres a = pl.getVerblijfplaats().getAdres();
    boolean isEmigrated = pl.getPersoon().getStatus().isEmigratie();
    boolean isRni = pl.getPersoon().getStatus().isRni();
    boolean isMinister = pl.getPersoon().getStatus().isMinisterieelBesluit();

    if (isEmigrated || isRni || isMinister) {
      foreignAddres1 = a.getBuitenland1().getValue().getDescr();
      foreignAddres2 = a.getBuitenland2().getValue().getDescr();
      foreignAddres3 = a.getBuitenland3().getValue().getDescr();
      countryCode = a.getEmigratieland().getValue().getVal();
      countryName = a.getEmigratieland().getValue().getDescr();
      label = normalizeSpace(foreignAddres1 + " " + foreignAddres2 + " " + foreignAddres3 + " " + countryName);

    } else {
      street = a.getStraat().getValue().getDescr();
      hnr = a.getHuisnummer().getValue().getDescr();
      hnrL = a.getHuisletter().getValue().getDescr();
      hnrT = a.getHuisnummertoev().getValue().getDescr();
      hnrA = a.getHuisnummeraand().getValue().getDescr();
      pc = a.getPostcode().getValue().getDescr();
      recidenceCode = a.getWoonplaats().getValue().getCode();
      recidenceName = a.getWoonplaats().getValue().getDescr();
      municipalityCode = a.getGemeente().getValue().getCode();
      municipalityName = a.getGemeente().getValue().getDescr();

      String hnrString = normalizeSpace(hnr + hnrL + " " + hnrT);
      label = normalizeSpace(street + " " + hnrString + ", " + Postcode.getFormat(pc) + " " + recidenceName);
    }
  }
}
