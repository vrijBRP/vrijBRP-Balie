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
public class TerugmTmvRelPK implements Serializable {

  private static final long serialVersionUID = 1L;

  @Column(name = "c_terugmelding",
      unique = true,
      nullable = false,
      precision = 131089)
  private long cTerugmelding;

  @Column(unique = true,
      nullable = false,
      precision = 131089)
  private long dossiernummer;

  public TerugmTmvRelPK() {
  }

  public long getCTerugmelding() {
    return this.cTerugmelding;
  }

  public void setCTerugmelding(long cTerugmelding) {
    this.cTerugmelding = cTerugmelding;
  }

  public long getDossiernummer() {
    return this.dossiernummer;
  }

  public void setDossiernummer(long dossiernummer) {
    this.dossiernummer = dossiernummer;
  }
}
