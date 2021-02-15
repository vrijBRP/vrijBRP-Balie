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

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "terugm_tmv")
public class TerugmTmv extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private TerugmTmvPK id;

  @Column()
  private String berichtcode;

  @Column(precision = 131089)
  private BigDecimal berichtnummer;

  @Column()
  private String berichtreferentie;

  @Column(name = "c_gem_beh",
      precision = 131089)
  private BigDecimal cGemBeh;

  @Column(name = "c_terugmelding",
      precision = 131089)
  private BigDecimal cTerugmelding;

  @Column(name = "c_usr",
      precision = 131089)
  private BigDecimal cUsr;

  @Column(name = "d_aanleg",
      precision = 131089)
  private BigDecimal dAanleg;

  @Column(name = "d_verw_afh",
      precision = 131089)
  private BigDecimal dVerwAfh;

  @Column(name = "d_wijz",
      precision = 131089)
  private BigDecimal dWijz;

  @Column(precision = 131089)
  private BigDecimal dossierstatus;

  @Column(length = 2147483647)
  private String resultaat;

  @Column(precision = 131089)
  private BigDecimal resultaatonderzoek;

  @Column(length = 2147483647)
  private String toelichting;

  @Column(precision = 131089)
  private BigDecimal verwerkingcode;

  @Column()
  private String verwerkingomschrijving;

  public TerugmTmv() {
  }

  @Override
  public TerugmTmvPK getId() {
    return this.id;
  }

  public void setId(TerugmTmvPK id) {
    this.id = id;
  }

  public String getBerichtcode() {
    return this.berichtcode;
  }

  public void setBerichtcode(String berichtcode) {
    this.berichtcode = berichtcode;
  }

  public BigDecimal getBerichtnummer() {
    return this.berichtnummer;
  }

  public void setBerichtnummer(BigDecimal berichtnummer) {
    this.berichtnummer = berichtnummer;
  }

  public String getBerichtreferentie() {
    return this.berichtreferentie;
  }

  public void setBerichtreferentie(String berichtreferentie) {
    this.berichtreferentie = berichtreferentie;
  }

  public BigDecimal getCGemBeh() {
    return this.cGemBeh;
  }

  public void setCGemBeh(BigDecimal cGemBeh) {
    this.cGemBeh = cGemBeh;
  }

  public BigDecimal getCTerugmelding() {
    return this.cTerugmelding;
  }

  public void setCTerugmelding(BigDecimal cTerugmelding) {
    this.cTerugmelding = cTerugmelding;
  }

  public BigDecimal getCUsr() {
    return this.cUsr;
  }

  public void setCUsr(BigDecimal cUsr) {
    this.cUsr = cUsr;
  }

  public BigDecimal getDAanleg() {
    return this.dAanleg;
  }

  public void setDAanleg(BigDecimal dAanleg) {
    this.dAanleg = dAanleg;
  }

  public BigDecimal getDVerwAfh() {
    return this.dVerwAfh;
  }

  public void setDVerwAfh(BigDecimal dVerwAfh) {
    this.dVerwAfh = dVerwAfh;
  }

  public BigDecimal getDWijz() {
    return this.dWijz;
  }

  public void setDWijz(BigDecimal dWijz) {
    this.dWijz = dWijz;
  }

  public BigDecimal getDossierstatus() {
    return this.dossierstatus;
  }

  public void setDossierstatus(BigDecimal dossierstatus) {
    this.dossierstatus = dossierstatus;
  }

  public String getResultaat() {
    return this.resultaat;
  }

  public void setResultaat(String resultaat) {
    this.resultaat = resultaat;
  }

  public BigDecimal getResultaatonderzoek() {
    return this.resultaatonderzoek;
  }

  public void setResultaatonderzoek(BigDecimal resultaatonderzoek) {
    this.resultaatonderzoek = resultaatonderzoek;
  }

  public String getToelichting() {
    return this.toelichting;
  }

  public void setToelichting(String toelichting) {
    this.toelichting = toelichting;
  }

  public BigDecimal getVerwerkingcode() {
    return this.verwerkingcode;
  }

  public void setVerwerkingcode(BigDecimal verwerkingcode) {
    this.verwerkingcode = verwerkingcode;
  }

  public String getVerwerkingomschrijving() {
    return this.verwerkingomschrijving;
  }

  public void setVerwerkingomschrijving(String verwerkingomschrijving) {
    this.verwerkingomschrijving = verwerkingomschrijving;
  }

}
