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

package nl.procura.gba.web.services.beheer.kassa;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.components.fields.values.UsrFieldValue;

public class KassaProductAanvraag {

  private KassaProduct  kassaProduct = new KassaProduct();
  private DateTime      tijdstip     = new DateTime();
  private UsrFieldValue gebruiker    = new UsrFieldValue();
  private BasePLExt     pl           = new BasePLExt();

  public KassaProductAanvraag(KassaProduct kassaProduct, BasePLExt pl, UsrFieldValue gebruiker) {

    setKassaProduct(kassaProduct);
    setPl(pl);
    setGebruiker(gebruiker);
  }

  public UsrFieldValue getGebruiker() {
    return gebruiker;
  }

  public void setGebruiker(UsrFieldValue gebruiker) {
    this.gebruiker = gebruiker;
  }

  public KassaProduct getKassaProduct() {
    return kassaProduct;
  }

  public void setKassaProduct(KassaProduct kassaProduct) {
    this.kassaProduct = kassaProduct;
  }

  public BasePLExt getPl() {
    return pl;
  }

  public void setPl(BasePLExt pl) {
    this.pl = pl;
  }

  public DateTime getTijdstip() {
    return tijdstip;
  }

  public void setTijdstip(DateTime tijdstip) {
    this.tijdstip = tijdstip;
  }
}
