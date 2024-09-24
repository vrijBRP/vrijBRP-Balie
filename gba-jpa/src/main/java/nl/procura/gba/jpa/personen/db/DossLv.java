/*
 * Copyright 2024 - 2025 Procura B.V.
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

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import nl.procura.gba.jpa.personen.converters.BigDecimalBooleanConverter;
import nl.procura.gba.jpa.personen.converters.BigDecimalDateConverter;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
@Entity
@Table(name = "doss_lv")
public class DossLv extends BaseEntity {

  @Id // ID is set with value of doss.c_doss
  @Column(name = "c_doss_lv", unique = true, nullable = false, precision = 131_089)
  private Long cDossLv;

  // bi-directional one-to-one association to Doss
  @OneToOne(cascade = {CascadeType.REMOVE})
  @JoinColumn(name = "c_doss_lv", nullable = false, insertable = false, updatable = false)
  private Doss doss;

  @Column(name = "soort_lv")
  private BigDecimal soortLv;

  @Column(name = "d_lv")
  @Convert(converter = BigDecimalDateConverter.class)
  private Date datumLv;

  @Column(name = "akte")
  private String akte;

  @Column(name = "akte_huidig")
  private String huidigBrpAkte;

  @Column(name = "akte_nieuw")
  private String nieuweBrpAkte;

  @Column(name = "akte_gem")
  private BigDecimal akteGem;

  @Column(name = "akte_jaar")
  private BigDecimal akteJaar;

  @Column(name = "uitspraak")
  private String uitspraak;

  @Column(name = "d_uitspraak")
  @Convert(converter = BigDecimalDateConverter.class)
  private Date datumUitspraak;

  @Column(name = "d_gewijsde")
  @Convert(converter = BigDecimalDateConverter.class)
  private Date datumGewijsde;

  @Column(name = "d_verzoekschrift")
  @Convert(converter = BigDecimalDateConverter.class)
  private Date datumVerzoekschrift;

  @Column(name = "soort_verbintenis")
  private String soortVerbintenis;

  @Column(name = "doc")
  private String doc;

  @Column(name = "doc_nr")
  private String docNr;

  @Column(name = "doc_datum")
  @Convert(converter = BigDecimalDateConverter.class)
  private Date docDatum;

  @Column(name = "doc_plaats")
  private String docPlaats;

  @Column(name = "doc_door")
  private BigDecimal docDoor;

  @Column(name = "tweede_doc")
  @Convert(converter = BigDecimalBooleanConverter.class)
  private Boolean tweedeDoc;

  @Column(name = "tweede_doc_oms")
  private String tweedeDocOms;

  @Column(name = "tweede_doc_plaats")
  private String tweedeDocPlaats;

  @Column(name = "tweede_doc_datum")
  @Convert(converter = BigDecimalDateConverter.class)
  private Date tweedeDocDatum;

  @Column(name = "betreft_ouder")
  private String betreftOuder;

  @Column(name = "toestemming")
  private String toestemming;

  @Column(name = "toeg_recht")
  private BigDecimal toegepastRecht;

  @Column(name = "gezag")
  private String gezag;

  @Column(name = "betreft_ouder_persoon")
  private BigDecimal betreftOuderP;

  @Column(name = "gesl_ouder")
  private String geslOuder;

  @Column(name = "keuze_gesl")
  private String keuzeGesl;

  @Column(name = "gesl")
  private String gesl;

  @Column(name = "voorn_ouder")
  private String voornOuder;

  @Column(name = "voorn")
  private String voorn;

  @Column(name = "gesl_aand")
  private String geslAand;

  @Column(name = "gekozen_recht")
  private String gekozenRecht;

  @Column(name = "d_wijz")
  @Convert(converter = BigDecimalDateConverter.class)
  private Date datumWijziging;

  @Column(name = "verbeterd")
  private String verbeterd;
}
