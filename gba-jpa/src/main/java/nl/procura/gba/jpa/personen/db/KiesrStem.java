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

import javax.persistence.*;

import org.eclipse.persistence.annotations.BatchFetch;
import org.eclipse.persistence.annotations.BatchFetchType;

@Entity
@Table(name = "kiesr_stem")
public class KiesrStem extends BaseEntity<Long> {

  private static final long serialVersionUID = 1L;

  @Id
  @TableGenerator(name = "table_gen_kiesr_stem",
      table = "serial",
      pkColumnName = "id",
      valueColumnName = "val",
      pkColumnValue = "kiesr_stem",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.TABLE,
      generator = "table_gen_kiesr_stem")
  @Column(name = "c_kiesr_stem",
      unique = true,
      nullable = false,
      precision = 131089)
  private Long cKiesrStem;

  @Column(name = "vnr")
  private Long vnr;

  @Column(name = "anr")
  private Long anr;

  @Column(name = "c_kiesr_verk")
  private Long cKiesrVerk;

  @Column(name = "pas_nr")
  private String pasNr;

  @Column(name = "d_geb")
  private Long dGeb;

  @Column(name = "geslacht")
  private String geslacht;

  @Column(name = "voorn")
  private String voorn;

  @Column(name = "naam")
  private String naam;

  @Column(name = "straat")
  private String straat;

  @Column(name = "hnr")
  private Long hnr;

  @Column(name = "hnr_l")
  private String hnrL;

  @Column(name = "hnr_t")
  private String hnrT;

  @Column(name = "pc")
  private String pc;

  @Column(name = "wpl")
  private String wpl;

  @Column(name = "ind_toev")
  private boolean indToegevoegd;

  @Column(name = "d_aand")
  private Long dAand;

  @Column(name = "t_aand")
  private Long tAand;

  @Column(name = "aand")
  private Long aand;

  @Column(name = "anr_volmacht")
  private Long anrVolmacht;

  @Column(name = "vnr_vervanging")
  private Long vnrVervanging;

  @ManyToOne
  @BatchFetch(BatchFetchType.IN)
  @JoinColumn(name = "c_kiesr_verk",
      nullable = false,
      insertable = false,
      updatable = false)
  private KiesrVerk kiesrVerk;

  public KiesrStem() {
    setAand(-1L);
    setdAand(-1L);
    settAand(-1L);
    setIndToegevoegd(false);
    setAnrVolmacht(-1L);
    setVnrVervanging(-1L);
  }

  public KiesrStem(KiesrVerk kiesrVerk) {
    this();
    this.cKiesrVerk = kiesrVerk.getcKiesrVerk();
  }

  @Override
  public Long getUniqueKey() {
    return getcCKiesrStem();
  }

  public Long getcCKiesrStem() {
    return cKiesrStem;
  }

  public void setcCKiesrStem(Long cKiesrStem) {
    this.cKiesrStem = cKiesrStem;
  }

  public Long getVnr() {
    return vnr;
  }

  public void setVnr(Long vnr) {
    this.vnr = vnr;
  }

  public Long getAnr() {
    return anr;
  }

  public void setAnr(Long anr) {
    this.anr = anr;
  }

  public Long getcKiesrVerk() {
    return cKiesrVerk;
  }

  public void setcKiesrVerk(Long cKiesrVerk) {
    this.cKiesrVerk = cKiesrVerk;
  }

  public String getPasNr() {
    return pasNr;
  }

  public void setPasNr(String pasNr) {
    this.pasNr = pasNr;
  }

  public KiesrVerk getKiesrVerk() {
    return kiesrVerk;
  }

  public void setKiesrVerk(KiesrVerk kiesrVerk) {
    this.kiesrVerk = kiesrVerk;
  }

  public boolean isIndToegevoegd() {
    return indToegevoegd;
  }

  public void setIndToegevoegd(boolean indToegevoegd) {
    this.indToegevoegd = indToegevoegd;
  }

  public Long getdGeb() {
    return dGeb;
  }

  public void setdGeb(Long dGeb) {
    this.dGeb = dGeb;
  }

  public String getVoorn() {
    return voorn;
  }

  public void setVoorn(String voorn) {
    this.voorn = voorn;
  }

  public String getNaam() {
    return naam;
  }

  public void setNaam(String naam) {
    this.naam = naam;
  }

  public String getGeslacht() {
    return geslacht;
  }

  public void setGeslacht(String geslacht) {
    this.geslacht = geslacht;
  }

  public String getStraat() {
    return straat;
  }

  public void setStraat(String straat) {
    this.straat = straat;
  }

  public Long getHnr() {
    return hnr;
  }

  public void setHnr(Long hnr) {
    this.hnr = hnr;
  }

  public String getHnrL() {
    return hnrL;
  }

  public void setHnrL(String hnrL) {
    this.hnrL = hnrL;
  }

  public String getHnrT() {
    return hnrT;
  }

  public void setHnrT(String hnrT) {
    this.hnrT = hnrT;
  }

  public String getPc() {
    return pc;
  }

  public void setPc(String pc) {
    this.pc = pc;
  }

  public String getWpl() {
    return wpl;
  }

  public void setWpl(String wpl) {
    this.wpl = wpl;
  }

  public Long getAand() {
    return aand;
  }

  public void setAand(Long codeInd) {
    this.aand = codeInd;
  }

  public Long getdAand() {
    return dAand;
  }

  public void setdAand(Long dAand) {
    this.dAand = dAand;
  }

  public Long gettAand() {
    return tAand;
  }

  public void settAand(Long tAand) {
    this.tAand = tAand;
  }

  public Long getAnrVolmacht() {
    return anrVolmacht;
  }

  public void setAnrVolmacht(Long anrVolmacht) {
    this.anrVolmacht = anrVolmacht;
  }

  public Long getVnrVervanging() {
    return vnrVervanging;
  }

  public void setVnrVervanging(Long vnrVervanging) {
    this.vnrVervanging = vnrVervanging;
  }
}
