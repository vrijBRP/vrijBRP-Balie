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
@Table(name = "doss_natio")
public class DossNatio extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @Id
  @TableGenerator(name = "table_gen_doss_natio",
      table = "serial",
      pkColumnName = "id",
      valueColumnName = "val",
      pkColumnValue = "doss_natio",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE, generator = "table_gen_doss_natio")
  @Column(name = "c_doss_natio", unique = true, nullable = false, precision = 131_089)
  private Long cDossNatio;

  @Column(name = "c_natio", precision = 131_089)
  private BigDecimal cNatio;

  @Column(name = "d_ontlening", precision = 131_089)
  private BigDecimal dOntlening;

  @Column(name = "d_verkrijging", precision = 131_089)
  private BigDecimal dVerkrijging;

  @Column(name = "d_verlies", precision = 131_089)
  private BigDecimal dVerlies;

  @Column()
  private String natio;

  @Column()
  private String redenverkrijging;

  @Column(name = "type_verkrijging", length = 1)
  private String typeVerkrijging;

  @Column(name = "t_ontlening", precision = 131_089)
  private BigDecimal tOntlening;

  @Column(name = "afgeleid", precision = 131_089)
  private BigDecimal afgeleid;

  @Column(name = "source_description", length = 255)
  private String sourceDescription = "";

  @Column(name = "source_foreign_id", length = 50)
  private String sourceForeignId = "";

  @ManyToOne
  @JoinColumn(name = "c_doss")
  private Doss doss;

  @ManyToOne
  @JoinColumn(name = "c_doss_pers")
  private DossPer dossPer;

  public DossNatio() {
  }

  public Long getCDossNatio() {
    return cDossNatio;
  }

  public void setCDossNatio(Long cDossNatio) {
    this.cDossNatio = cDossNatio;
  }

  public BigDecimal getCNatio() {
    return cNatio;
  }

  public void setCNatio(BigDecimal cNatio) {
    this.cNatio = cNatio;
  }

  public BigDecimal getDOntlening() {
    return dOntlening;
  }

  public void setDOntlening(BigDecimal dOntlening) {
    this.dOntlening = dOntlening;
  }

  public BigDecimal getDVerkrijging() {
    return dVerkrijging;
  }

  public void setDVerkrijging(BigDecimal dVerkrijging) {
    this.dVerkrijging = dVerkrijging;
  }

  public BigDecimal getDVerlies() {
    return dVerlies;
  }

  public void setDVerlies(BigDecimal dVerlies) {
    this.dVerlies = dVerlies;
  }

  public String getNatio() {
    return natio;
  }

  public void setNatio(String natio) {
    this.natio = natio;
  }

  public String getRedenverkrijging() {
    return redenverkrijging;
  }

  public void setRedenverkrijging(String redenverkrijging) {
    this.redenverkrijging = redenverkrijging;
  }

  public BigDecimal getTOntlening() {
    return tOntlening;
  }

  public void setTOntlening(BigDecimal tOntlening) {
    this.tOntlening = tOntlening;
  }

  public String getTypeVerkrijging() {
    return typeVerkrijging;
  }

  public void setTypeVerkrijging(String typeVerkrijging) {
    this.typeVerkrijging = typeVerkrijging;
  }

  @Override
  public Object getUniqueKey() {
    return getCNatio();
  }

  public BigDecimal getAfgeleid() {
    return afgeleid;
  }

  public void setAfgeleid(BigDecimal afgeleid) {
    this.afgeleid = afgeleid;
  }

  public DossPer getDossPer() {
    return dossPer;
  }

  public void setDossPer(DossPer dossPer) {
    this.dossPer = dossPer;
  }

  public Doss getDoss() {
    return doss;
  }

  public void setDoss(Doss doss) {
    this.doss = doss;
  }

  public String getSourceDescription() {
    return sourceDescription;
  }

  public void setSourceDescription(String sourceDescription) {
    this.sourceDescription = sourceDescription;
  }

  public String getSourceForeignId() {
    return sourceForeignId;
  }

  public void setSourceForeignId(String sourceForeignId) {
    this.sourceForeignId = sourceForeignId;
  }

}
