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

package nl.procura.gba.jpa.personen.db.views;

public class ZakenAantalView {

  private long zaakType;

  private long indVerwerkt;

  private long aantal;

  public ZakenAantalView(long zaakType, long indVerwerkt, long aantal) {
    super();
    this.zaakType = zaakType;
    this.indVerwerkt = indVerwerkt;
    this.aantal = aantal;
  }

  public long getAantal() {
    return aantal;
  }

  public void setAantal(long aantal) {
    this.aantal = aantal;
  }

  public long getZaakType() {
    return zaakType;
  }

  public void setZaakType(long zaakType) {
    this.zaakType = zaakType;
  }

  public long getIndVerwerkt() {
    return indVerwerkt;
  }

  public void setIndVerwerkt(long indVerwerkt) {
    this.indVerwerkt = indVerwerkt;
  }
}
