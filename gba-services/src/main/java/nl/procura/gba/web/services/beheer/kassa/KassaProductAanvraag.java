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

package nl.procura.gba.web.services.beheer.kassa;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.components.fields.values.UsrFieldValue;

import lombok.Data;

@Data
public class KassaProductAanvraag {

  private String        zaakId;
  private KassaProduct  kassaProduct;
  private DateTime      tijdstip;
  private UsrFieldValue gebruiker;
  private BasePLExt     pl;

  public KassaProductAanvraag(KassaProduct kassaProduct, String zaakId, BasePLExt pl, UsrFieldValue gebruiker) {
    this.kassaProduct = kassaProduct;
    this.zaakId = zaakId;
    this.pl = pl;
    this.gebruiker = gebruiker;
    this.tijdstip = new DateTime();
  }

  public boolean isTheSame(KassaProductAanvraag aanvraag) {
    boolean isKassaProduct = kassaProduct != null && kassaProduct.getId().equals(aanvraag.getKassaProduct().getId());
    boolean isPL = pl != null && pl.is(aanvraag.getPl());
    boolean isZaakId = zaakId != null && zaakId.equals(aanvraag.getZaakId());
    return isNotBlank(aanvraag.zaakId) && isKassaProduct && isPL && isZaakId;
  }
}
