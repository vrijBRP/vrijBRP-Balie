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

import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.normalizeSpace;

import java.util.stream.Collectors;

import nl.procura.gba.web.services.interfaces.address.AddressSourceType;
import nl.procura.geo.rest.domain.ngr.wfs.WfsFeature;
import nl.procura.geo.rest.domain.ngr.wfs.WfsGenericProperties;
import nl.procura.geo.rest.domain.ngr.wfs.WfsVboProperties;
import nl.procura.geo.rest.domain.ngr.wfs.types.GebruiksdoelType;
import nl.procura.validation.Postcode;

/**
 * Address implementation for information from the PDOK WFS service
 */
public class PdokWfsServiceAddress extends AbstractAddress {

  private PdokWfsServiceAddress() {
  }

  public PdokWfsServiceAddress(WfsFeature a) {
    sourceType = AddressSourceType.BAG;
    WfsGenericProperties p = null;
    switch (a.getType()) {
      case VERBLIJFSOBJECT:
        p = a.getVerblijfsobject();
        WfsVboProperties vbo = a.getVerblijfsobject();
        buildingId = defaultString(vbo.getPandidentificatie());
        recidenceName = defaultString(vbo.getWoonplaats());
        purpose = defaultString(vbo.getGebruiksdoelen().stream().map(g -> g.getValue())
            .collect(Collectors.joining(", ")));
        status = defaultString(vbo.getStatus().getValue());
        buildingStatus = defaultString(vbo.getPandstatus().getValue());
        surfaceSize = vbo.getOppervlakte() != null ? vbo.getOppervlakte().intValue() : 0;
        suitableForLiving = vbo.getGebruiksdoelen().contains(GebruiksdoelType.WOONFUNCTIE);
        break;
      case LIGPLAATS:
        p = a.getLigplaats();
        break;
      case STANDPLAATS:
        p = a.getStandplaats();
        break;
    }
    aonId = defaultString(p.getIdentificatie());
    street = defaultString(p.getOpenbareRuimte());
    hnr = defaultString(p.getHuisnummer() != null ? p.getHuisnummer().toString() : "");
    hnrL = defaultString(p.getHuisletter());
    hnrT = defaultString(p.getToevoeging());
    pc = defaultString(p.getPostcode());

    String hnrString = normalizeSpace(hnr + hnrL + " " + hnrT);
    label = normalizeSpace(street + " " + hnrString + ", " + Postcode.getFormat(pc) + " " + recidenceName);
  }
}
