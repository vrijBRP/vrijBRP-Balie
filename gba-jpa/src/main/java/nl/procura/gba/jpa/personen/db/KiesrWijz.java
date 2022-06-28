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

import javax.persistence.*;

import org.eclipse.persistence.annotations.BatchFetch;
import org.eclipse.persistence.annotations.BatchFetchType;

@Entity
@Table(name = "kiesr_wijz")
public class KiesrWijz extends BaseEntity<Long> {

  private static final long serialVersionUID = 1L;

  @Id
  @TableGenerator(name = "table_gen_kiesr_wijz",
      table = "serial",
      pkColumnName = "id",
      valueColumnName = "val",
      pkColumnValue = "kiesr_wijz",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE,
      generator = "table_gen_kiesr_wijz")
  @Column(name = "c_kiesr_wijz",
      unique = true,
      nullable = false,
      precision = 131089)
  private Long cKiesrWijz;

  @Column(name = "c_kiesr_stem")
  private Long cKiesrStem;

  @Column(name = "c_usr")
  private Long cUsr;

  @Column(name = "actietype")
  private Long actietype;

  @Column(name = "d_in")
  private Long dIn;

  @Column(name = "t_in")
  private Long tIn;

  @Column(name = "opm")
  private String opm;

  @ManyToOne
  @BatchFetch(BatchFetchType.IN)
  @JoinColumn(name = "c_kiesr_stem",
      nullable = false,
      insertable = false,
      updatable = false)
  private KiesrStem kiesrStem;

  @ManyToOne
  @BatchFetch(BatchFetchType.IN)
  @JoinColumn(name = "c_usr",
      nullable = false,
      insertable = false,
      updatable = false)
  private Usr usr;

  public KiesrWijz() {
  }

  public KiesrWijz(KiesrStem kiesrStem) {
    this.cKiesrStem = kiesrStem.getcCKiesrStem();
    this.kiesrStem = kiesrStem;
  }

  @Override
  public Long getUniqueKey() {
    return getcCKiesrStem();
  }

  public Long getcKiesrWijz() {
    return cKiesrWijz;
  }

  public void setcKiesrWijz(Long cKiesrWijz) {
    this.cKiesrWijz = cKiesrWijz;
  }

  public Long getcCKiesrStem() {
    return cKiesrStem;
  }

  public void setcCKiesrStem(Long cKiesrStem) {
    this.cKiesrStem = cKiesrStem;
  }

  public Long getcKiesrStem() {
    return cKiesrStem;
  }

  public void setcKiesrStem(Long cKiesrStem) {
    this.cKiesrStem = cKiesrStem;
  }

  public KiesrStem getKiesrStem() {
    return kiesrStem;
  }

  public void setKiesrStem(KiesrStem kiesrStem) {
    this.kiesrStem = kiesrStem;
  }

  public Long getcUsr() {
    return cUsr;
  }

  public void setcUsr(Long cUsr) {
    this.cUsr = cUsr;
  }

  public void setUsr(Usr usr) {
    this.usr = usr;
  }

  public Usr getUsr() {
    return usr;
  }

  public Long getActietype() {
    return actietype;
  }

  public void setActietype(Long actietype) {
    this.actietype = actietype;
  }

  public Long getdIn() {
    return dIn;
  }

  public void setdIn(Long dIn) {
    this.dIn = dIn;
  }

  public Long gettIn() {
    return tIn;
  }

  public void settIn(Long tIn) {
    this.tIn = tIn;
  }

  public String getOpm() {
    return opm;
  }

  public void setOpm(String opm) {
    this.opm = opm;
  }
}
