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

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "aantekening")
public class Aantekening extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @Id
  @TableGenerator(name = "table_gen_aantekening",
      table = "serial",
      pkColumnName = "id",
      valueColumnName = "val",
      pkColumnValue = "aantekening",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE,
      generator = "table_gen_aantekening")
  @Column(name = "c_aantekening",
      unique = true,
      nullable = false,
      precision = 131089)
  private Long cAantekening;

  @Column(precision = 131089)
  private BigDecimal bsn;

  @Column(name = "zaak_id")
  private String zaakId;

  @OneToMany(mappedBy = "aantekening")
  private List<AantekeningHist> aantekeningHists;

  public Aantekening() {
  }

  public Long getCAantekening() {
    return this.cAantekening;
  }

  public void setCAantekening(Long cAantekening) {
    this.cAantekening = cAantekening;
  }

  public BigDecimal getBsn() {
    return this.bsn;
  }

  public void setBsn(BigDecimal bsn) {
    this.bsn = bsn;
  }

  public String getZaakId() {
    return this.zaakId;
  }

  public void setZaakId(String zaakId) {
    this.zaakId = zaakId;
  }

  public List<AantekeningHist> getAantekeningHists() {
    return this.aantekeningHists;
  }

  public void setAantekeningHists(List<AantekeningHist> aantekeningHists) {
    this.aantekeningHists = aantekeningHists;
  }

}
