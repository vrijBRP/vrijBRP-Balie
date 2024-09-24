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
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import nl.procura.gba.jpa.personen.converters.BigDecimalBooleanConverter;
import nl.procura.gba.jpa.personen.converters.BigDecimalDateConverter;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "doss_natur")
public class DossNatur extends BaseEntity<Long> {

  private static final long serialVersionUID = 1L;

  @Id
  @Column(name = "c_doss_natur",
      unique = true,
      nullable = false,
      precision = 131089)
  private Long cDossNatur;

  @OneToOne(cascade = { CascadeType.REMOVE })
  @JoinColumn(name = "c_doss_natur",
      nullable = false,
      insertable = false,
      updatable = false)
  private Doss doss;

  @Column(name = "bevoegd_indienen")
  private BigDecimal bevoegdIndienen;

  @Column(name = "bevoegd_indienen_toel")
  private String bevoegdIndienenToel;

  @Column(name = "optie")
  @Convert(converter = BigDecimalBooleanConverter.class)
  private Boolean optie;

  @Column(name = "optie_toel")
  private String optieToel;

  @Column(name = "basis_verzoek")
  private BigDecimal basisVerzoek;

  @Column(name = "dossiernr")
  private String dossiernr;

  @Column(name = "toets_verkl_ondertekend")
  @Convert(converter = BigDecimalBooleanConverter.class)
  private Boolean toetsverklOndertekend;

  @Column(name = "toets_bereid_verkl")
  @Convert(converter = BigDecimalBooleanConverter.class)
  private Boolean toetsBereidVerkl;

  @Column(name = "toets_betrokk_bekend")
  @Convert(converter = BigDecimalBooleanConverter.class)
  private Boolean toetsBetrokkBekend;

  @Column(name = "toets_bereid_afstand")
  private BigDecimal toetsBereidAfstand;

  @Column(name = "toets_bewijs_id_aanw")
  @Convert(converter = BigDecimalBooleanConverter.class)
  private Boolean toetsBewijsIdAanw;

  @Column(name = "toets_bewijs_nat_aanw")
  private BigDecimal toetsBewijsNatAanw;

  @Column(name = "toets_bewijsnood")
  @Convert(converter = BigDecimalBooleanConverter.class)
  private Boolean toetsBewijsnood;

  @Column(name = "toets_bewijsnood_toel")
  private String toetsBewijsnoodToel;

  @Column(name = "toets_geld_verblijfs_aanw")
  private BigDecimal toetsGeldVerblijfsAanw;

  @Column(name = "naamst_nodig")
  private BigDecimal naamstNodig;

  @Column(name = "naamst_gesl_gew_toel")
  private String naamstGeslGewToel;

  @Column(name = "beh_bot_opgevraagd")
  @Convert(converter = BigDecimalBooleanConverter.class)
  private Boolean behBotOpgevraagd;

  @Column(name = "beh_minderj_kind1")
  private BigDecimal behMinderjKind1;

  @Column(name = "beh_minderj_kind2")
  @Convert(converter = BigDecimalBooleanConverter.class)
  private Boolean behMinderjKind2;

  @Column(name = "beh_andere_vert_toel")
  private String behAndereVertToel;

  @Column(name = "beh_opgevr_justis")
  @Convert(converter = BigDecimalBooleanConverter.class)
  private Boolean behOpgevrJustis;

  @Column(name = "beh_d_aanvr")
  @Convert(converter = BigDecimalDateConverter.class)
  private Date behDAanvr;

  @Column(name = "beh_term_d_end")
  @Convert(converter = BigDecimalDateConverter.class)
  private Date behTermDEnd;

  @Column(name = "beh_term_toel")
  private String behTermToel;

  @OneToMany(cascade = { CascadeType.REMOVE },
      mappedBy = "dossNatur")
  private List<DossNaturVerz> dossNaturVerz;

  public DossNatur() {
  }
}
