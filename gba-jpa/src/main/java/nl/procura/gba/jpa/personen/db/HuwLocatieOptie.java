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
@Table(name = "huw_locatie_optie")
public class HuwLocatieOptie extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @Id
  @TableGenerator(name = "table_gen_huw_locatie_optie",
      table = "serial",
      pkColumnName = "id",
      valueColumnName = "val",
      pkColumnValue = "huw_locatie_optie",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE,
      generator = "table_gen_huw_locatie_optie")
  @Column(name = "c_huw_locatie_optie",
      unique = true,
      nullable = false,
      precision = 131089)
  private Long cHuwLocatieOptie;

  @Column(name = "huw_locatie_optie")
  private String huwLocatieOptie;

  @Column()
  private String type;

  @Column(precision = 1)
  private BigDecimal verplicht;

  @Column
  private int vnr;

  @Column
  private int min;

  @Column
  private int max;

  @Column(name = "alias",
      length = 1000)
  private String alias;

  @OneToMany(mappedBy = "huwLocatieOptie")
  private List<DossHuwOptie> dossHuwOpties;

  @ManyToMany
  @JoinTable(name = "huw_loc_opt",
      joinColumns = { @JoinColumn(name = "c_huw_locatie_optie",
          nullable = false) },
      inverseJoinColumns = { @JoinColumn(name = "c_huw_locatie",
          nullable = false) })
  private List<HuwLocatie> huwLocaties;

  public HuwLocatieOptie() {
  }

  public Long getCHuwLocatieOptie() {
    return this.cHuwLocatieOptie;
  }

  public void setCHuwLocatieOptie(Long cHuwLocatieOptie) {
    this.cHuwLocatieOptie = cHuwLocatieOptie;
  }

  public String getHuwLocatieOptie() {
    return this.huwLocatieOptie;
  }

  public void setHuwLocatieOptie(String huwLocatieOptie) {
    this.huwLocatieOptie = huwLocatieOptie;
  }

  public String getType() {
    return this.type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public BigDecimal getVerplicht() {
    return this.verplicht;
  }

  public void setVerplicht(BigDecimal verplicht) {
    this.verplicht = verplicht;
  }

  public List<DossHuwOptie> getDossHuwOpties() {
    return this.dossHuwOpties;
  }

  public void setDossHuwOpties(List<DossHuwOptie> dossHuwOpties) {
    this.dossHuwOpties = dossHuwOpties;
  }

  public List<HuwLocatie> getHuwLocaties() {
    return this.huwLocaties;
  }

  public void setHuwLocaties(List<HuwLocatie> huwLocaties) {
    this.huwLocaties = huwLocaties;
  }

  public int getVnr() {
    return vnr;
  }

  public void setVnr(int vnr) {
    this.vnr = vnr;
  }

  public int getMin() {
    return min;
  }

  public void setMin(int min) {
    this.min = min;
  }

  public int getMax() {
    return max;
  }

  public void setMax(int max) {
    this.max = max;
  }

  public String getAlias() {
    return alias;
  }

  public void setAlias(String alias) {
    this.alias = alias;
  }
}
