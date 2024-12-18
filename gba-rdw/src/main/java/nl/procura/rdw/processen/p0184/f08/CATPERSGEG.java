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
// Generated on: 2018.06.26 at 02:32:23 PM CEST 
//

package nl.procura.rdw.processen.p0184.f08;

import java.io.Serializable;
import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for CAT-PERS-GEG complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="CAT-PERS-GEG">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="RYB-CAT-P" type="{}RYB-CATEGORIE" minOccurs="0"/>
 *         &lt;element name="EERST-AFG-DAT-C" type="{}DATUM-EJMD" minOccurs="0"/>
 *         &lt;element name="SRT-E-AFG-DAT-C" type="{}SRT-E-AFG-DAT-C" minOccurs="0"/>
 *         &lt;element name="OMGEW-RYB-NR" type="{}OMGEW-RYB-NR" minOccurs="0"/>
 *         &lt;element name="LAND-CODE-OMW-C" type="{}LAND-CODE" minOccurs="0"/>
 *         &lt;element name="LAND-C-VT-OMW-C" type="{}LAND-CODE-VRTG" minOccurs="0"/>
 *         &lt;element name="AUTOM-CAT-IND" type="{}INDIC-JN" minOccurs="0"/>
 *         &lt;element name="MED-VERKL-IND-C" type="{}INDIC-JN" minOccurs="0"/>
 *         &lt;element name="BEP-VERM-CAT" type="{}BEP-VERM-CAT" minOccurs="0"/>
 *         &lt;element name="G-VERKL-IND" type="{}INDIC-JN" minOccurs="0"/>
 *         &lt;element name="REG-DAT-G-IND" type="{}DATUM-EJMD" minOccurs="0"/>
 *         &lt;element name="AUTOR-C-G-IND" type="{}AUTOR-CODE" minOccurs="0"/>
 *         &lt;element name="NAAM-GEM-G-IND" type="{}NAAM-AUTOR" minOccurs="0"/>
 *         &lt;element name="NM-GEM-G-IND-D" type="{}NAAM-AUTOR-DIA" minOccurs="0"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CAT-PERS-GEG",
    propOrder = {

    })
public class CATPERSGEG implements Serializable {

  @XmlElement(name = "RYB-CAT-P")
  private String     rybcatp;
  @XmlElement(name = "EERST-AFG-DAT-C")
  private BigInteger eerstafgdatc;
  @XmlElement(name = "SRT-E-AFG-DAT-C")
  private String     srteafgdatc;
  @XmlElement(name = "OMGEW-RYB-NR")
  private String     omgewrybnr;
  @XmlElement(name = "LAND-CODE-OMW-C")
  private BigInteger landcodeomwc;
  @XmlElement(name = "LAND-C-VT-OMW-C")
  private String     landcvtomwc;
  @XmlElement(name = "AUTOM-CAT-IND")
  private String     automcatind;
  @XmlElement(name = "MED-VERKL-IND-C")
  private String     medverklindc;
  @XmlElement(name = "BEP-VERM-CAT")
  private String     bepvermcat;
  @XmlElement(name = "G-VERKL-IND")
  private String     gverklind;
  @XmlElement(name = "REG-DAT-G-IND")
  private BigInteger regdatgind;
  @XmlElement(name = "AUTOR-C-G-IND")
  private BigInteger autorcgind;
  @XmlElement(name = "NAAM-GEM-G-IND")
  private String     naamgemgind;
  @XmlElement(name = "NM-GEM-G-IND-D")
  private String     nmgemgindd;

  public String getRybcatp() {
    return this.rybcatp;
  }

  public void setRybcatp(String rybcatp) {
    this.rybcatp = rybcatp;
  }

  public BigInteger getEerstafgdatc() {
    return this.eerstafgdatc;
  }

  public void setEerstafgdatc(BigInteger eerstafgdatc) {
    this.eerstafgdatc = eerstafgdatc;
  }

  public String getSrteafgdatc() {
    return this.srteafgdatc;
  }

  public void setSrteafgdatc(String srteafgdatc) {
    this.srteafgdatc = srteafgdatc;
  }

  public String getOmgewrybnr() {
    return this.omgewrybnr;
  }

  public void setOmgewrybnr(String omgewrybnr) {
    this.omgewrybnr = omgewrybnr;
  }

  public BigInteger getLandcodeomwc() {
    return this.landcodeomwc;
  }

  public void setLandcodeomwc(BigInteger landcodeomwc) {
    this.landcodeomwc = landcodeomwc;
  }

  public String getLandcvtomwc() {
    return this.landcvtomwc;
  }

  public void setLandcvtomwc(String landcvtomwc) {
    this.landcvtomwc = landcvtomwc;
  }

  public String getAutomcatind() {
    return this.automcatind;
  }

  public void setAutomcatind(String automcatind) {
    this.automcatind = automcatind;
  }

  public String getMedverklindc() {
    return this.medverklindc;
  }

  public void setMedverklindc(String medverklindc) {
    this.medverklindc = medverklindc;
  }

  public String getBepvermcat() {
    return this.bepvermcat;
  }

  public void setBepvermcat(String bepvermcat) {
    this.bepvermcat = bepvermcat;
  }

  public String getGverklind() {
    return this.gverklind;
  }

  public void setGverklind(String gverklind) {
    this.gverklind = gverklind;
  }

  public BigInteger getRegdatgind() {
    return this.regdatgind;
  }

  public void setRegdatgind(BigInteger regdatgind) {
    this.regdatgind = regdatgind;
  }

  public BigInteger getAutorcgind() {
    return this.autorcgind;
  }

  public void setAutorcgind(BigInteger autorcgind) {
    this.autorcgind = autorcgind;
  }

  public String getNaamgemgind() {
    return this.naamgemgind;
  }

  public void setNaamgemgind(String naamgemgind) {
    this.naamgemgind = naamgemgind;
  }

  public String getNmgemgindd() {
    return this.nmgemgindd;
  }

  public void setNmgemgindd(String nmgemgindd) {
    this.nmgemgindd = nmgemgindd;
  }

}
