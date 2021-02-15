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

package nl.procura.gba.jpa.personen.db;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.EqualsAndHashCode;

@Embeddable
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Aant3PK implements Serializable {

  private static final long serialVersionUID = 1L;

  @EqualsAndHashCode.Include
  @Column(unique = true,
      nullable = false,
      precision = 131089)
  private long anr;

  @Column(unique = true,
      nullable = false,
      precision = 131089)
  private long bsn;

  @EqualsAndHashCode.Include
  @Column(name = "c_aantek3",
      unique = true,
      nullable = false,
      precision = 131089)
  private long cAantek3;

  public Aant3PK() {
  }

  public long getAnr() {
    return this.anr;
  }

  public void setAnr(long anr) {
    this.anr = anr;
  }

  public long getCAantek3() {
    return this.cAantek3;
  }

  public void setCAantek3(long cAantek3) {
    this.cAantek3 = cAantek3;
  }

  public long getBsn() {
    return bsn;
  }

  public void setBsn(long bsn) {
    this.bsn = bsn;
  }
}
