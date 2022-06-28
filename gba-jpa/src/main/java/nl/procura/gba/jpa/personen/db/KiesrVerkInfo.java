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
@Table(name = "kiesr_verk_info")
public class KiesrVerkInfo extends BaseEntity<Long> {

  private static final long serialVersionUID = 1L;

  @Id
  @TableGenerator(name = "table_gen_kiesr_verk_info",
      table = "serial",
      pkColumnName = "id",
      valueColumnName = "val",
      pkColumnValue = "kiesr_verk_info",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE,
      generator = "table_gen_kiesr_verk_info")
  @Column(name = "c_kiesr_verk_info",
      unique = true,
      nullable = false,
      precision = 131089)
  private Long cKiesrVerkInfo;

  @Column(name = "c_kiesr_verk")
  private Long cKiesrVerk;

  @Column(name = "naam")
  private String naam;

  @Column(name = "inhoud")
  private String inhoud;

  @ManyToOne
  @BatchFetch(BatchFetchType.IN)
  @JoinColumn(name = "c_kiesr_verk",
      nullable = false,
      insertable = false,
      updatable = false)
  private KiesrVerk kiesrVerk;

  public KiesrVerkInfo() {
  }

  public KiesrVerkInfo(KiesrVerk KiesrVerk) {
    this.cKiesrVerk = KiesrVerk.getcKiesrVerk();
    this.kiesrVerk = KiesrVerk;
  }

  @Override
  public Long getUniqueKey() {
    return getcKiesrVerkInfo();
  }

  public Long getcKiesrVerkInfo() {
    return cKiesrVerkInfo;
  }

  public void setcKiesrVerkInfo(Long cKiesrVerkInfo) {
    this.cKiesrVerkInfo = cKiesrVerkInfo;
  }

  public Long getcKiesrVerk() {
    return cKiesrVerk;
  }

  public void setcKiesrVerk(Long cKiesrVerk) {
    this.cKiesrVerk = cKiesrVerk;
  }

  public nl.procura.gba.jpa.personen.db.KiesrVerk getKiesrVerk() {
    return kiesrVerk;
  }

  public void setKiesrVerk(nl.procura.gba.jpa.personen.db.KiesrVerk kiesrVerk) {
    this.kiesrVerk = kiesrVerk;
  }

  public String getNaam() {
    return naam;
  }

  public void setNaam(String alias) {
    this.naam = alias;
  }

  public String getInhoud() {
    return inhoud;
  }

  public void setInhoud(String inhoud) {
    this.inhoud = inhoud;
  }
}
