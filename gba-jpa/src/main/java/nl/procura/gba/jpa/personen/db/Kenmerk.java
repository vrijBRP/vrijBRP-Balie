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
@Table(name = "kenmerk")
@NamedQuery(name = "Kenmerk.findAll",
    query = "SELECT s FROM Kenmerk s")
public class Kenmerk extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @Id
  @TableGenerator(name = "table_gen_kenmerk",
      table = "serial",
      pkColumnName = "id",
      valueColumnName = "val",
      pkColumnValue = "kenmerk",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE,
      generator = "table_gen_kenmerk")
  @Column(name = "c_kenmerk",
      unique = true,
      nullable = false,
      precision = 131089)
  private Long cKenmerk;

  @Column()
  private String kenmerk;

  @Column(name = "kenmerk_type")
  private String type;

  @ManyToMany(mappedBy = "kenmerks")
  private List<Document> documents;

  public Kenmerk() {
  }

  public Long getCKenmerk() {
    return this.cKenmerk;
  }

  public void setCKenmerk(Long cKenmerk) {
    this.cKenmerk = cKenmerk;
  }

  public String getKenmerk() {
    return this.kenmerk;
  }

  public void setKenmerk(String kenmerk) {
    this.kenmerk = kenmerk;
  }

  public List<Document> getDocuments() {
    return this.documents;
  }

  public void setDocuments(List<Document> documents) {
    this.documents = documents;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }
}
