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
@Table(name = "prot_new")
@NamedQueries({ @NamedQuery(name = "Prot.find",
    query = "select p from ProtNew p where p.anr = :anr and p.dIn = :dIn and p.usr.cUsr = :cUsr") })
public class ProtNew extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @Id
  @TableGenerator(name = "table_gen_prot_new",
      table = "serial",
      pkColumnName = "id",
      valueColumnName = "val",
      pkColumnValue = "prot_new",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE,
      generator = "table_gen_prot_new")
  @Column(name = "c_prot_new",
      unique = true,
      nullable = false,
      precision = 131089)
  private Long cProtNew;

  @Column(precision = 131089)
  private BigDecimal anr;

  @Column(name = "d_in",
      nullable = false,
      precision = 131089)
  private BigDecimal dIn;

  @ManyToOne
  @JoinColumn(name = "c_usr")
  private Usr usr;

  @OneToMany(mappedBy = "protNew")
  private List<ProtNewSearch> protNewSearches;

  public ProtNew() {
  }

  public Long getCProtNew() {
    return this.cProtNew;
  }

  public void setCProtNew(Long cProtNew) {
    this.cProtNew = cProtNew;
  }

  public BigDecimal getAnr() {
    return this.anr;
  }

  public void setAnr(BigDecimal anr) {
    this.anr = anr;
  }

  public Usr getUsr() {
    return this.usr;
  }

  public void setUsr(Usr usr) {
    this.usr = usr;
  }

  public List<ProtNewSearch> getProtNewSearches() {
    return this.protNewSearches;
  }

  public void setProtNewSearches(List<ProtNewSearch> protNewSearches) {
    this.protNewSearches = protNewSearches;
  }

  public BigDecimal getdIn() {
    return dIn;
  }

  public void setdIn(BigDecimal dIn) {
    this.dIn = dIn;
  }

}
