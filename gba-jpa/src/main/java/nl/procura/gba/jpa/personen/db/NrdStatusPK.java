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
@EqualsAndHashCode
public class NrdStatusPK implements Serializable {

  private static final long serialVersionUID = 1L;

  @Column(name = "c_aanvr",
      unique = true,
      nullable = false,
      precision = 131089)
  private long cAanvr;

  @Column(name = "c_stat",
      unique = true,
      nullable = false,
      precision = 131089)
  private long cStat;

  @Column(name = "d_stat",
      unique = true,
      nullable = false,
      precision = 131089)
  private long dStat;

  @Column(name = "t_stat",
      unique = true,
      nullable = false,
      precision = 131089)
  private long tStat;

  public NrdStatusPK() {
  }

  public long getCAanvr() {
    return this.cAanvr;
  }

  public void setCAanvr(long cAanvr) {
    this.cAanvr = cAanvr;
  }

  public long getCStat() {
    return this.cStat;
  }

  public void setCStat(long cStat) {
    this.cStat = cStat;
  }

  public long getDStat() {
    return this.dStat;
  }

  public void setDStat(long dStat) {
    this.dStat = dStat;
  }

  public long getTStat() {
    return this.tStat;
  }

  public void setTStat(long tStat) {
    this.tStat = tStat;
  }
}
