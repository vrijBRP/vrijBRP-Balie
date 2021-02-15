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

import org.eclipse.persistence.annotations.BatchFetch;
import org.eclipse.persistence.annotations.BatchFetchType;

@Entity
@Table(name = "kassa")
@NamedQueries({ @NamedQuery(name = "Kassa.findByTypeAndDocument",
    query = "select k from Kassa k where k.type = :type and k.document != null and k.document.cDocument = :cDocument"),
    @NamedQuery(name = "Kassa.findByTypeAndReisdocument",
        query = "select k from Kassa k where k.type = :type and k.reisdoc != null and k.reisdoc.cReisdoc = :cReisdoc"),
    @NamedQuery(name = "Kassa.findByTypeAndRijbewijs",
        query = "select k from Kassa k where k.type = :type and k.cRijb = :cRijb"),
    @NamedQuery(name = "Kassa.findByTypeAndAnders",
        query = "select k from Kassa k where k.type = :type and k.anders = :anders and k.productgroep = :productgroep"),
    @NamedQuery(name = "Kassa.findByType",
        query = "select k from Kassa k where k.type = :type") })
public class Kassa extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @Id
  @TableGenerator(name = "table_gen_kassa",
      table = "serial",
      pkColumnName = "id",
      valueColumnName = "val",
      pkColumnValue = "kassa",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE,
      generator = "table_gen_kassa")
  @Column(name = "c_kassa",
      unique = true,
      nullable = false,
      precision = 131089)
  private Long cKassa;

  @Column()
  private String kassa;

  @Column()
  private String anders;

  @Column()
  private String productgroep;

  @Column(name = "kassa_type",
      precision = 131089)
  private BigDecimal type;

  @Column(precision = 131089)
  private BigDecimal bundel;

  @Column(precision = 131089,
      name = "c_rijb")
  private BigDecimal cRijb;

  @ManyToOne
  @BatchFetch(BatchFetchType.IN)
  @JoinColumn(name = "c_document")
  private Document document;

  @ManyToOne
  @BatchFetch(BatchFetchType.IN)
  @JoinColumn(name = "c_reisdoc")
  private Reisdoc reisdoc;

  @ManyToMany
  @JoinTable(name = "kassa_bundel",
      joinColumns = { @JoinColumn(name = "c_sub_kassa",
          nullable = false) },
      inverseJoinColumns = { @JoinColumn(name = "c_parent_kassa",
          nullable = false) })
  private List<Kassa> parentKassas;

  @ManyToMany(mappedBy = "parentKassas")
  private List<Kassa> subKassas;

  public Kassa() {
  }

  public Long getCKassa() {
    return this.cKassa;
  }

  public void setCKassa(Long cKassa) {
    this.cKassa = cKassa;
  }

  public String getKassa() {
    return this.kassa;
  }

  public void setKassa(String kassa) {
    this.kassa = kassa;
  }

  public BigDecimal getType() {
    return this.type;
  }

  public void setType(BigDecimal type) {
    this.type = type;
  }

  public Document getDocument() {
    return this.document;
  }

  public void setDocument(Document document) {
    this.document = document;
  }

  public Reisdoc getReisdoc() {
    return this.reisdoc;
  }

  public void setReisdoc(Reisdoc reisdoc) {
    this.reisdoc = reisdoc;
  }

  public String getAnders() {
    return anders;
  }

  public void setAnders(String anders) {
    this.anders = anders;
  }

  public String getProductgroep() {
    return productgroep;
  }

  public void setProductgroep(String productgroep) {
    this.productgroep = productgroep;
  }

  public BigDecimal getcRijb() {
    return cRijb;
  }

  public void setcRijb(BigDecimal cRijb) {
    this.cRijb = cRijb;
  }

  public BigDecimal getBundel() {
    return bundel;
  }

  public void setBundel(BigDecimal bundel) {
    this.bundel = bundel;
  }

  public List<Kassa> getSubKassas() {
    return subKassas;
  }

  public void setSubKassas(List<Kassa> subKassas) {
    this.subKassas = subKassas;
  }

  public List<Kassa> getParentKassas() {
    return parentKassas;
  }

  public void setParentKassas(List<Kassa> parentKassas) {
    this.parentKassas = parentKassas;
  }

}
