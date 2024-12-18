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

//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.06.26 at 02:32:30 PM CEST 
//

package nl.procura.rdw.processen.p1659.f02;

import java.io.Serializable;
import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for NAT-PERSOON-GEG complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="NAT-PERSOON-GEG">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="NAT-PERS-SL" type="{}NAT-PERS-SL"/>
 *         &lt;element name="GBA-NR-NAT-P" type="{}GBA-NR"/>
 *         &lt;element name="NATIONAL-PERS" type="{}NATIONAL-PERS" minOccurs="0"/>
 *         &lt;element name="FISC-NR-NAT-P" type="{}FISC-NR-NAT-P" minOccurs="0"/>
 *         &lt;element name="GESL-NAAM-NAT-P" type="{}GESL-NAAM"/>
 *         &lt;element name="VOOR-VOEG-NAT-P" type="{}VOOR-VOEG-DIA" minOccurs="0"/>
 *         &lt;element name="VOOR-LET-NAT-P" type="{}VOOR-LET" minOccurs="0"/>
 *         &lt;element name="ADEL-PRED-NAT-P" type="{}ADEL-PRED" minOccurs="0"/>
 *         &lt;element name="VOOR-NAAM-NAT-P" type="{}VOOR-NAAM" minOccurs="0"/>
 *         &lt;element name="VOORNAMEN-NAT-P" type="{}VOORNAMEN-NAT-P" minOccurs="0"/>
 *         &lt;element name="GESL-AAND-NAT-P" type="{}GESL-AAND" minOccurs="0"/>
 *         &lt;element name="BURG-ST-NAT-P" type="{}BURG-STAAT" minOccurs="0"/>
 *         &lt;element name="NAAM-GEBR-NAT-P" type="{}AAND-NAAM-GEBR" minOccurs="0"/>
 *         &lt;element name="GESL-NAAM-ECHTG" type="{}GESL-NAAM" minOccurs="0"/>
 *         &lt;element name="VOOR-VOEG-ECHTG" type="{}VOOR-VOEG-DIA" minOccurs="0"/>
 *         &lt;element name="ADEL-PRED-ECHTG" type="{}ADEL-PRED" minOccurs="0"/>
 *         &lt;element name="AUTOR-C-GEB-PL" type="{}AUTOR-C-GEB-PL" minOccurs="0"/>
 *         &lt;element name="GEB-PL-BUITENL" type="{}GEB-PL-BUITENL" minOccurs="0"/>
 *         &lt;element name="GEB-DAT-NAT-P" type="{}DATUM-EJMD"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NAT-PERSOON-GEG",
    propOrder = {

    })
public class NATPERSOONGEG implements Serializable {

  @XmlElement(name = "NAT-PERS-SL",
      required = true)
  private String     natperssl;
  @XmlElement(name = "GBA-NR-NAT-P",
      required = true)
  private BigInteger gbanrnatp;
  @XmlElement(name = "NATIONAL-PERS")
  private BigInteger nationalpers;
  @XmlElement(name = "FISC-NR-NAT-P")
  private BigInteger fiscnrnatp;
  @XmlElement(name = "GESL-NAAM-NAT-P",
      required = true)
  private String     geslnaamnatp;
  @XmlElement(name = "VOOR-VOEG-NAT-P")
  private String     voorvoegnatp;
  @XmlElement(name = "VOOR-LET-NAT-P")
  private String     voorletnatp;
  @XmlElement(name = "ADEL-PRED-NAT-P")
  private String     adelprednatp;
  @XmlElement(name = "VOOR-NAAM-NAT-P")
  private String     voornaamnatp;
  @XmlElement(name = "VOORNAMEN-NAT-P")
  private String     voornamennatp;
  @XmlElement(name = "GESL-AAND-NAT-P")
  private BigInteger geslaandnatp;
  @XmlElement(name = "BURG-ST-NAT-P")
  private BigInteger burgstnatp;
  @XmlElement(name = "NAAM-GEBR-NAT-P")
  private BigInteger naamgebrnatp;
  @XmlElement(name = "GESL-NAAM-ECHTG")
  private String     geslnaamechtg;
  @XmlElement(name = "VOOR-VOEG-ECHTG")
  private String     voorvoegechtg;
  @XmlElement(name = "ADEL-PRED-ECHTG")
  private String     adelpredechtg;
  @XmlElement(name = "AUTOR-C-GEB-PL")
  private BigInteger autorcgebpl;
  @XmlElement(name = "GEB-PL-BUITENL")
  private String     gebplbuitenl;
  @XmlElement(name = "GEB-DAT-NAT-P",
      required = true)
  private BigInteger gebdatnatp;

  public String getNatperssl() {
    return this.natperssl;
  }

  public void setNatperssl(String natperssl) {
    this.natperssl = natperssl;
  }

  public BigInteger getGbanrnatp() {
    return this.gbanrnatp;
  }

  public void setGbanrnatp(BigInteger gbanrnatp) {
    this.gbanrnatp = gbanrnatp;
  }

  public BigInteger getNationalpers() {
    return this.nationalpers;
  }

  public void setNationalpers(BigInteger nationalpers) {
    this.nationalpers = nationalpers;
  }

  public BigInteger getFiscnrnatp() {
    return this.fiscnrnatp;
  }

  public void setFiscnrnatp(BigInteger fiscnrnatp) {
    this.fiscnrnatp = fiscnrnatp;
  }

  public String getGeslnaamnatp() {
    return this.geslnaamnatp;
  }

  public void setGeslnaamnatp(String geslnaamnatp) {
    this.geslnaamnatp = geslnaamnatp;
  }

  public String getVoorvoegnatp() {
    return this.voorvoegnatp;
  }

  public void setVoorvoegnatp(String voorvoegnatp) {
    this.voorvoegnatp = voorvoegnatp;
  }

  public String getVoorletnatp() {
    return this.voorletnatp;
  }

  public void setVoorletnatp(String voorletnatp) {
    this.voorletnatp = voorletnatp;
  }

  public String getAdelprednatp() {
    return this.adelprednatp;
  }

  public void setAdelprednatp(String adelprednatp) {
    this.adelprednatp = adelprednatp;
  }

  public String getVoornaamnatp() {
    return this.voornaamnatp;
  }

  public void setVoornaamnatp(String voornaamnatp) {
    this.voornaamnatp = voornaamnatp;
  }

  public String getVoornamennatp() {
    return this.voornamennatp;
  }

  public void setVoornamennatp(String voornamennatp) {
    this.voornamennatp = voornamennatp;
  }

  public BigInteger getGeslaandnatp() {
    return this.geslaandnatp;
  }

  public void setGeslaandnatp(BigInteger geslaandnatp) {
    this.geslaandnatp = geslaandnatp;
  }

  public BigInteger getBurgstnatp() {
    return this.burgstnatp;
  }

  public void setBurgstnatp(BigInteger burgstnatp) {
    this.burgstnatp = burgstnatp;
  }

  public BigInteger getNaamgebrnatp() {
    return this.naamgebrnatp;
  }

  public void setNaamgebrnatp(BigInteger naamgebrnatp) {
    this.naamgebrnatp = naamgebrnatp;
  }

  public String getGeslnaamechtg() {
    return this.geslnaamechtg;
  }

  public void setGeslnaamechtg(String geslnaamechtg) {
    this.geslnaamechtg = geslnaamechtg;
  }

  public String getVoorvoegechtg() {
    return this.voorvoegechtg;
  }

  public void setVoorvoegechtg(String voorvoegechtg) {
    this.voorvoegechtg = voorvoegechtg;
  }

  public String getAdelpredechtg() {
    return this.adelpredechtg;
  }

  public void setAdelpredechtg(String adelpredechtg) {
    this.adelpredechtg = adelpredechtg;
  }

  public BigInteger getAutorcgebpl() {
    return this.autorcgebpl;
  }

  public void setAutorcgebpl(BigInteger autorcgebpl) {
    this.autorcgebpl = autorcgebpl;
  }

  public String getGebplbuitenl() {
    return this.gebplbuitenl;
  }

  public void setGebplbuitenl(String gebplbuitenl) {
    this.gebplbuitenl = gebplbuitenl;
  }

  public BigInteger getGebdatnatp() {
    return this.gebdatnatp;
  }

  public void setGebdatnatp(BigInteger gebdatnatp) {
    this.gebdatnatp = gebdatnatp;
  }

}
