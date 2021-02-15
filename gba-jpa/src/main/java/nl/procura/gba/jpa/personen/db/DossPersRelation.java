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

import nl.procura.java.reflection.ReflectionUtil;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

/* Entity for holding relation data about two people who register for the first time.
 * <p>
 * It's for first registration only now, as the relation_type is first registration only.
 * </p>
 */
@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@Entity
@Table(name = "doss_pers_rel")
public class DossPersRelation extends BaseEntity {

  private static final long       serialVersionUID = 6674367626696963143L;
  private static final BigDecimal DATE_TYPE_CUSTOM = BigDecimal.valueOf(2);

  @Id
  @TableGenerator(name = "table_gen_doss_relative",
      table = "serial",
      pkColumnName = "id",
      valueColumnName = "val",
      pkColumnValue = "doss_pers_rel",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE, generator = "table_gen_doss_relative")
  @Column(name = "c_doss_pers_rel", unique = true, nullable = false, precision = 131_089)
  private BigDecimal cDossRelative;

  @ManyToOne
  @JoinColumn(name = "c_doss_pers_1")
  private DossPer person;

  @ManyToOne
  @JoinColumn(name = "c_doss_pers_2")
  private DossPer relatedPerson;

  @Column(name = "relation_type", precision = 131_089)
  private BigDecimal relationShipType;

  @Column(name = "d_in_type", precision = 131_089)
  private BigDecimal startDateType;

  @Column(name = "d_in")
  private BigDecimal customStartDate;

  @Column(name = "d_in_land", length = 10)
  private String startDateCountry;

  @Column(name = "d_in_gem")
  private String startDateMunicipality;

  @Column(name = "d_end")
  private BigDecimal customEndDate;

  @Column(name = "d_end_land", length = 10)
  private String endDateCountry;

  @Column(name = "d_end_gem")
  private String endDateMunicipality;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "c_doss_source_doc")
  private DossSourceDoc dossSourceDoc;

  @Column(name = "srt_huw", length = 1)
  private String commitmentType;

  @Column(name = "rdn_huw_ontb", length = 1)
  private String endReason;

  @Column(name = "match_type", length = 1)
  private BigDecimal matchType;

  @Column(name = "voorn")
  private String voorn;

  @Column(name = "voorv")
  private String voorv;

  @Column(name = "tp")
  private String tp;

  @Column(name = "geslachtsnaam")
  private String geslachtsnaam;

  // empty public constructor required for persistence with GenericDao.saveEntity
  public DossPersRelation() {
  }

  /**
   * Create new object based on a source object but with different people.
   */
  public DossPersRelation(DossPersRelation source,
      DossPer person,
      BigDecimal relationShipType,
      DossPer relatedPerson,
      BigDecimal matchType) {

    this.person = ReflectionUtil.deepCopyBean(DossPer.class, person);
    this.relatedPerson = ReflectionUtil.deepCopyBean(DossPer.class, relatedPerson);
    this.matchType = matchType;
    this.relationShipType = relationShipType;

    if (source != null) {
      this.startDateType = source.startDateType;
      this.customStartDate = source.customStartDate;
      this.startDateCountry = source.startDateCountry;
      this.startDateMunicipality = source.startDateMunicipality;
      this.customEndDate = source.customEndDate;
      this.endDateCountry = source.endDateCountry;
      this.endDateMunicipality = source.endDateMunicipality;
      this.dossSourceDoc = source.dossSourceDoc;
      this.commitmentType = source.commitmentType;
      this.endReason = source.endReason;
      this.voorn = source.getVoorn();
      this.voorv = source.getVoorv();
      this.geslachtsnaam = source.getGeslachtsnaam();
      this.tp = source.getTp();
    }
  }

  public static DossPersRelation reverse(DossPersRelation source, BigDecimal relationShipType) {
    return new DossPersRelation(source,
        source.getRelatedPerson(),
        relationShipType,
        source.getPerson(),
        source.getMatchType());
  }

  public void setCustomStartDate(BigDecimal customStartDate) {
    this.startDateType = DATE_TYPE_CUSTOM;
    this.customStartDate = customStartDate;
  }
}
