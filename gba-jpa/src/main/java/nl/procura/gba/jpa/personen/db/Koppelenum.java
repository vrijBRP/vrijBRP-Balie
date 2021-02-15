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

import lombok.EqualsAndHashCode;

@Entity
@Table(name = "koppelenum")
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
public class Koppelenum extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @Id
  @TableGenerator(name = "table_gen_koppelenum",
      table = "serial",
      pkColumnName = "id",
      valueColumnName = "val",
      pkColumnValue = "koppelenum",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE,
      generator = "table_gen_koppelenum")
  @Column(name = "c_koppelenum",
      unique = true,
      nullable = false,
      precision = 131089)
  private Long cKoppelenum;

  @Column(name = "koppelenum",
      precision = 131089)
  @EqualsAndHashCode.Include
  private long koppelenum;

  @ManyToMany
  @JoinTable(name = "koppelenum_document",
      joinColumns = { @JoinColumn(name = "c_koppelenum",
          nullable = false) },
      inverseJoinColumns = { @JoinColumn(name = "c_document",
          nullable = false) })
  private List<Document> documents;

  public Koppelenum() {
  }

  public Long getCKoppelenum() {
    return cKoppelenum;
  }

  public void setCKoppelenum(Long cKoppelenum) {
    this.cKoppelenum = cKoppelenum;
  }

  public List<Document> getDocuments() {
    return documents;
  }

  public void setDocuments(List<Document> documents) {
    this.documents = documents;
  }

  public long getKoppelenum() {
    return koppelenum;
  }

  public void setKoppelenum(long koppelenum) {
    this.koppelenum = koppelenum;
  }
}
