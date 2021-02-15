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

import javax.persistence.*;

@Entity
@Table(name = "gem")
@NamedQuery(name = "Gem.findAll",
    query = "select g from Gem g where g.cGem > 0 order by g.gemeente")
public class Gem extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @Id
  @TableGenerator(name = "table_gen_gem",
      table = "serial",
      pkColumnName = "id",
      valueColumnName = "val",
      pkColumnValue = "gem",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE,
      generator = "table_gen_gem")
  @Column(name = "c_gem",
      unique = true,
      nullable = false,
      precision = 131089)
  private Long cGem;

  @Column(precision = 131089)
  private BigDecimal cbscode;

  @Column()
  private String gemeente;

  @Column(length = 10)
  private String adres;

  @Column(length = 6)
  private String pc;

  @Column()
  private String plaats;

  public Gem() {
  }

  public Long getCGem() {
    return this.cGem;
  }

  public void setCGem(Long cGem) {
    this.cGem = cGem;
  }

  public BigDecimal getCbscode() {
    return this.cbscode;
  }

  public void setCbscode(BigDecimal cbscode) {
    this.cbscode = cbscode;
  }

  public String getGemeente() {
    return this.gemeente;
  }

  public void setGemeente(String gemeente) {
    this.gemeente = gemeente;
  }

  public String getAdres() {
    return adres;
  }

  public void setAdres(String adres) {
    this.adres = adres;
  }

  public String getPc() {
    return this.pc;
  }

  public void setPc(String pc) {
    this.pc = pc;
  }

  public String getPlaats() {
    return this.plaats;
  }

  public void setPlaats(String plaats) {
    this.plaats = plaats;
  }
}
