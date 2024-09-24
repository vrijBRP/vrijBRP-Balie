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

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "rdm_amp")
public class RdmAmp extends BaseEntity<Long> {

  private static final long serialVersionUID = 1L;

  @Id
  @TableGenerator(name = "table_gen_rdm_amp",
      table = "serial",
      pkColumnName = "id",
      valueColumnName = "val",
      pkColumnValue = "rdm_amp",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE,
      generator = "table_gen_rdm_amp")
  @Column(name = "c_rdm_amp",
      unique = true,
      nullable = false,
      precision = 131089)
  private Long id;

  @Column(name = "bezorging_gewenst")
  private boolean bezorgingGewenst;

  @Column(name = "aanvr_nr")
  private String aanvrNr;

  @Column(name = "bundel_ref_nr")
  private String bundelRefNr;

  @Column(name = "order_ref_nr")
  private String orderRefNr;

  @Column(name = "hoofdorder")
  private Boolean hoofdorder;

  @Column(name = "doc_type")
  private String docType;

  @Column(name = "gemeentecode")
  private Long gemeentecode;

  @Column(name = "gemeentenaam")
  private String gemeentenaam;

  @Column(name = "locatiecode")
  private Long locatiecode;

  @Column(name = "opmerkingen")
  private String opmerkingen;

  @Column(name = "status")
  private Integer status;

  @Column(name = "d_in")
  private Long dIn;

  @Column(name = "d_end")
  private Long dEnd;

  @Column(name = "ind_voormelding")
  private boolean indVoormelding;

  @Column(name = "ind_koppeling")
  private boolean indKoppeling;

  @Column(name = "ind_inklaring")
  private boolean indInklaring;

  @Column(name = "ind_blokkering")
  private boolean indBlokkering;

  @Column(name = "code_blokkering")
  private String codeBlokkering;

  @Column(name = "oms_blokkering")
  private String omsBlokkering;

  @Column(name = "ind_annulering")
  private boolean indAnnulering;

  @Column(name = "code_annulering")
  private String codeAnnulering;

  @Column(name = "oms_annulering")
  private String omsAnnulering;

  @Column(name = "ind_uitreiking")
  private boolean indUitreiking;

  @Column(name = "bsn")
  private Long bsn;

  @Column(name = "voorl")
  private String voorl;

  @Column(name = "voornamen")
  private String voornamen;

  @Column(name = "voorv")
  private String voorv;

  @Column(name = "geslachtsnaam")
  private String geslachtsnaam;

  @Column(name = "d_geb")
  private Long dGeb;

  @Column(name = "gesl")
  private String gesl;

  @Column(name = "hnr")
  private Long hnr;

  @Column(name = "hnr_l")
  private String hnrL;

  @Column(name = "hnr_t")
  private String hnrT;

  @Column(name = "pc")
  private String pc;

  @Column(name = "straat")
  private String straat;

  @Column(name = "wpl")
  private String wpl;

  @Column(name = "email")
  private String email;

  @Column(name = "tel1")
  private String tel1;

  @Column(name = "tel2")
  private String tel2;

  @Column(name = "rdm01_id")
  private Long rdm01Id;

  @OneToMany(mappedBy = "rdmAmp")
  private List<RdmAmpDoc> rdmAmpDocs;

  public RdmAmp() {
    hoofdorder = true;
    orderRefNr = "";
    indVoormelding = false;
    indKoppeling = false;
    indInklaring = false;
    indBlokkering = false;
    codeBlokkering = "";
    omsBlokkering = "";
    indAnnulering = false;
    codeAnnulering = "";
    omsAnnulering = "";
    indUitreiking = false;
    dEnd = -1L;
    email = "";
    tel1 = "";
    tel2 = "";
    opmerkingen = "";
  }
}
