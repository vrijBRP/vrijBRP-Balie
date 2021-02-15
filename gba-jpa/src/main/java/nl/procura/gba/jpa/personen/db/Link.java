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
@Table(name = "link")
public class Link extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @Id
  @TableGenerator(name = "table_gen_link",
      table = "serial",
      pkColumnName = "id",
      valueColumnName = "val",
      pkColumnValue = "link",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE,
      generator = "table_gen_link")
  @Column(name = "c_link",
      unique = true,
      nullable = false,
      precision = 131089)
  private Long cLink;

  @Column(length = 2147483647)
  private String id;

  @Column(name = "properties")
  private byte[] props;

  @Column(name = "link_type")
  private String type;

  @Column(name = "d_in",
      precision = 131089)
  private BigDecimal dIn;

  @Column(name = "d_end",
      precision = 131089)
  private BigDecimal dEnd;

  public Link() {
  }

  public Long getCLink() {
    return this.cLink;
  }

  public void setCLink(Long cLink) {
    this.cLink = cLink;
  }

  @Override
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public byte[] getProps() {
    return props;
  }

  public void setProps(byte[] props) {
    this.props = props;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public BigDecimal getDEnd() {
    return dEnd;
  }

  public void setDEnd(BigDecimal dEnd) {
    this.dEnd = dEnd;
  }

  public BigDecimal getDIn() {
    return dIn;
  }

  public void setDIn(BigDecimal dIn) {
    this.dIn = dIn;
  }
}
