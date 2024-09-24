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
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.eclipse.persistence.annotations.BatchFetch;
import org.eclipse.persistence.annotations.BatchFetchType;

import nl.procura.gba.jpa.personen.converters.BigDecimalBooleanConverter;
import nl.procura.gba.jpa.personen.converters.BigDecimalDateConverter;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "doss_natur_verz")
public class DossNaturVerz extends BaseEntity<Long> {

  private static final long serialVersionUID = 1L;

  @Id
  @TableGenerator(name = "table_gen_doss_natur_verz",
      table = "serial",
      pkColumnName = "id",
      valueColumnName = "val",
      pkColumnValue = "doss_natur_verz",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE,
      generator = "table_gen_doss_natur_verz")
  @Column(name = "c_doss_natur_verz",
      unique = true,
      nullable = false,
      precision = 131089)
  private Long cDossNaturVerz;

  @Column(name = "bsn")
  private BigDecimal bsn;

  @Column(name = "vnr")
  private String vnr;

  @Column(name = "toets_inburgering")
  private BigDecimal toetsInburgering;

  @Column(name = "naamst_gesl")
  private String naamstGesl;

  @Column(name = "naamst_voorn")
  private String naamstVoorn;

  @Column(name = "naamst_gesl_gew")
  private String naamstGeslGew;

  @Column(name = "naamst_voorn_gew")
  private String naamstVoornGew;

  @Column(name = "beh_besliss_optie")
  private BigDecimal behBeslissOptie;

  @Column(name = "beh_advies_natur")
  private BigDecimal behAdviesNatur;

  @Column(name = "beh_d_bevest")
  @Convert(converter = BigDecimalDateConverter.class)
  private Date behDBevest;

  @Column(name = "beh_andere_vert_bsn")
  private BigDecimal behAndereVertBsn;

  @Column(name = "beh_andere_vert_akk")
  private BigDecimal behAndereVertAkk;

  @Column(name = "beh_d_koning_besluit")
  @Convert(converter = BigDecimalDateConverter.class)
  private Date behDKoningBesluit;

  @Column(name = "beh_nr_koning_besluit")
  private String behNrKoningBesluit;

  @Column(name = "ceremonie1_d_in")
  @Convert(converter = BigDecimalDateConverter.class)
  private Date ceremonie1DIn;

  @Column(name = "ceremonie1_t_in")
  private BigDecimal ceremonie1TIn;

  @Column(name = "ceremonie1_bijgewoond")
  @Convert(converter = BigDecimalBooleanConverter.class)
  private Boolean ceremonie1Bijgewoond;

  @Column(name = "ceremonie2_d_in")
  @Convert(converter = BigDecimalDateConverter.class)
  private Date ceremonie2DIn;

  @Column(name = "ceremonie2_t_in")
  private BigDecimal ceremonie2TIn;

  @Column(name = "ceremonie2_bijgewoond")
  @Convert(converter = BigDecimalBooleanConverter.class)
  private Boolean ceremonie2Bijgewoond;

  @Column(name = "ceremonie3_d_in")
  @Convert(converter = BigDecimalDateConverter.class)
  private Date ceremonie3DIn;

  @Column(name = "ceremonie3_t_in")
  private BigDecimal ceremonie3TIn;

  @Column(name = "ceremonie3_bijgewoond")
  @Convert(converter = BigDecimalBooleanConverter.class)
  private Boolean ceremonie3Bijgewoond;

  @Column(name = "ceremonie_d_uitreik")
  @Convert(converter = BigDecimalDateConverter.class)
  private Date ceremonieDUitreik;

  @Column(name = "ceremonie_d_verval")
  @Convert(converter = BigDecimalDateConverter.class)
  private Date ceremonieDVerval;

  @ManyToOne
  @BatchFetch(BatchFetchType.IN)
  @JoinColumn(name = "c_doss_natur")
  private DossNatur dossNatur;

  public DossNaturVerz() {
  }
}
