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
@NamedQueries({ @NamedQuery(name = "UsrInfo.findByEmail",
    query = "select i from UsrInfo i where i.key = 'email' and lower(i.value) = lower(:email) and i.usr.cUsr > 0") })
@Table(name = "usr_info")
public class UsrInfo extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @Id
  @TableGenerator(name = "table_gen_usr_info",
      table = "serial",
      pkColumnName = "id",
      valueColumnName = "val",
      pkColumnValue = "usr_info",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE,
      generator = "table_gen_usr_info")
  @Column(name = "c_usr_info",
      unique = true,
      nullable = false,
      precision = 131089)
  private Long cUsrInfo;

  @Column()
  private String descr;

  @Column(name = "info_key")
  private String key;

  @Column(name = "info_value",
      length = 2147483647)
  private String value;

  @ManyToOne
  @BatchFetch(BatchFetchType.IN)
  @JoinColumn(name = "c_usr")
  private Usr usr;

  public UsrInfo() {
  }

  public Long getCUsrInfo() {
    return this.cUsrInfo;
  }

  public void setCUsrInfo(Long cUsrInfo) {
    this.cUsrInfo = cUsrInfo;
  }

  public String getDescr() {
    return this.descr;
  }

  public void setDescr(String descr) {
    this.descr = descr;
  }

  public String getKey() {
    return this.key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getValue() {
    return this.value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public Usr getUsr() {
    return this.usr;
  }

  public void setUsr(Usr usr) {
    this.usr = usr;
  }

}
