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
import java.math.BigDecimal;

import javax.persistence.*;

import org.eclipse.persistence.annotations.BatchFetch;
import org.eclipse.persistence.annotations.BatchFetchType;

@Entity
@Table(name = "ind_verwerkt")
@NamedQueries({ @NamedQuery(name = "IndVerwerkt.find",
    query = "select i from IndVerwerkt i where i.zaakId = :zaakid and i.indVerwerkt = :indverwerkt order by i.cIndVerwerkt desc"),
    @NamedQuery(name = "IndVerwerkt.findByZaakId",
        query = "select i from IndVerwerkt i where i.zaakId = :zaakid order by i.cIndVerwerkt desc") })
public class IndVerwerkt implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @TableGenerator(name = "table_gen_ind_verwerkt",
      table = "serial",
      pkColumnName = "id",
      valueColumnName = "val",
      pkColumnValue = "ind_verwerkt",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE,
      generator = "table_gen_ind_verwerkt")
  @Column(name = "c_ind_verwerkt",
      unique = true,
      nullable = false,
      precision = 131089)
  private Long cIndVerwerkt;

  @Column(name = "zaak_id",
      unique = true,
      nullable = false)
  private String zaakId;

  @Column(name = "ind_verwerkt",
      unique = true,
      nullable = false,
      precision = 131089)
  private long indVerwerkt;

  @ManyToOne
  @BatchFetch(BatchFetchType.IN)
  @JoinColumn(name = "c_usr")
  private Usr usr;

  @Column(name = "d_in",
      precision = 131089)
  private BigDecimal dIn;

  @Column(length = 2147483647)
  private String opmerking;

  @Column(name = "t_in",
      precision = 131089)
  private BigDecimal tIn;

  public IndVerwerkt() {
  }

  public IndVerwerkt(String zaakId) {
    this.zaakId = zaakId;
  }

  public BigDecimal getDIn() {
    return this.dIn;
  }

  public void setDIn(BigDecimal dIn) {
    this.dIn = dIn;
  }

  public String getOpmerking() {
    return this.opmerking;
  }

  public void setOpmerking(String opmerking) {
    this.opmerking = opmerking;
  }

  public BigDecimal getTIn() {
    return this.tIn;
  }

  public void setTIn(BigDecimal tIn) {
    this.tIn = tIn;
  }

  public Usr getUsr() {
    return usr;
  }

  public void setUsr(Usr usr) {
    this.usr = usr;
  }

  public long getIndVerwerkt() {
    return indVerwerkt;
  }

  public void setIndVerwerkt(long indVerwerkt) {
    this.indVerwerkt = indVerwerkt;
  }

  public String getZaakId() {
    return zaakId;
  }

  public void setZaakId(String zaakId) {
    this.zaakId = zaakId;
  }

  public Long getcIndVerwerkt() {
    return cIndVerwerkt;
  }

  public void setcIndVerwerkt(Long cIndVerwerkt) {
    this.cIndVerwerkt = cIndVerwerkt;
  }
}
