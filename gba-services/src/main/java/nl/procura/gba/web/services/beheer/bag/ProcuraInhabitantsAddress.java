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

import static nl.procura.standard.Globalfunctions.aval;
import static org.apache.commons.lang3.StringUtils.normalizeSpace;

import java.util.ArrayList;
import java.util.List;

import nl.procura.diensten.gba.wk.baseWK.BaseWKPerson;
import nl.procura.diensten.gba.wk.extensions.BaseWKExt;
import nl.procura.gba.web.services.interfaces.address.AddressSourceType;
import nl.procura.validation.Postcode;

import lombok.Getter;

/**
 * Address implementation for adress information from the 'Woningkaart'
 */
public class ProcuraInhabitantsAddress extends AbstractAddress {

  @Getter
  private final List<BaseWKPerson> persons = new ArrayList<>();

  @Getter
  private int internalId = -1;

  @Getter
  private int endDateNumber = -1;

  private ProcuraInhabitantsAddress() {
  }

  public ProcuraInhabitantsAddress(BaseWKExt wk) {
    sourceType = AddressSourceType.BRP;
    aonId = wk.getBasisWk().getAon().getValue();
    inaId = wk.getBasisWk().getIna().getValue();
    hnr = wk.getBasisWk().getHuisnummer().getValue();
    hnrL = wk.getBasisWk().getHuisletter().getValue();
    hnrT = wk.getBasisWk().getToevoeging().getValue();
    hnrA = wk.getBasisWk().getAanduiding().getValue();
    pc = wk.getBasisWk().getPostcode().getValue();
    street = wk.getBasisWk().getStraat().getValue();
    recidenceCode = wk.getBasisWk().getWoonplaats().getCode();
    recidenceName = wk.getBasisWk().getWoonplaats().getDescr();
    suitableForLiving = aval(wk.getBasisWk().getWoning_indicatie().getCode()) == 0;
    String hnrString = normalizeSpace(hnr + hnrL + " " + hnrT);
    label = normalizeSpace(street + " " + hnrString + ", " + Postcode.getFormat(pc) + " " + recidenceName);
    persons.addAll(wk.getBasisWk().getPersonen());

    // Specific fields
    internalId = aval(wk.getBasisWk().getCode_object().getCode());
    startDate = aval(wk.getBasisWk().getDatum_ingang().getCode());
    endDate = aval(wk.getBasisWk().getDatum_einde().getCode());
    endDateNumber = aval(wk.getBasisWk().getVolgcode_einde().getCode());
    ppd = aval(wk.getBasisWk().getPpd().getCode());
    addressIndicationCode = aval(wk.getBasisWk().getAdres_indicatie().getCode());
    addressIndicationName = wk.getBasisWk().getAdres_indicatie().getDescr();
    recidenceType = wk.getBasisWk().getWoning().getDescr();
    votingdistrict = wk.getBasisWk().getStemdistrict().getDescr();
    district = wk.getBasisWk().getWijk().getDescr();
    neighborhood = wk.getBasisWk().getBuurt().getDescr();
    subNeighborhood = wk.getBasisWk().getSub_buurt().getDescr();
  }
}
