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

import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "aantek3")
public class Aantek3 extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @Id
  @TableGenerator(name = "table_gen_aantek3",
      table = "serial",
      pkColumnName = "id",
      valueColumnName = "val",
      pkColumnValue = "aantek3",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE,
      generator = "table_gen_aantek3")
  @Column(name = "c_aantek3",
      unique = true,
      nullable = false,
      precision = 131089)
  private Long cAantek3;

  @Column()
  private String aantek3;

  @Column(length = 2147483647)
  private String oms;

  @OneToMany(mappedBy = "aantek3")
  private List<Aant3> aant3s;

  public Aantek3() {
  }

  public Long getCAantek3() {
    return this.cAantek3;
  }

  public void setCAantek3(Long cAantek3) {
    this.cAantek3 = cAantek3;
  }

  public String getAantek3() {
    return this.aantek3;
  }

  public void setAantek3(String aantek3) {
    this.aantek3 = aantek3;
  }

  public String getOms() {
    return this.oms;
  }

  public void setOms(String oms) {
    this.oms = oms;
  }

  public List<Aant3> getAant3s() {
    return this.aant3s;
  }

  public void setAant3s(List<Aant3> aant3s) {
    this.aant3s = aant3s;
  }
}
