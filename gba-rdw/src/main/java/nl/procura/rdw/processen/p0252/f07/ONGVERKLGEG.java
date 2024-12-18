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
// Generated on: 2018.06.26 at 02:32:28 PM CEST 
//

package nl.procura.rdw.processen.p0252.f07;

import java.io.Serializable;
import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for ONG-VERKL-GEG complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="ONG-VERKL-GEG">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="INLEV-DAT-OV" type="{}DATUM-EJMD" minOccurs="0"/>
 *         &lt;element name="SRT-AUT-INL-OV" type="{}SOORT-AUTOR" minOccurs="0"/>
 *         &lt;element name="AUTOR-C-INL-OV" type="{}AUTOR-CODE-AFG" minOccurs="0"/>
 *         &lt;element name="NAAM-AUT-INL-OV" type="{}NAAM-AUTOR" minOccurs="0"/>
 *         &lt;element name="NAAM-INL-OV-DIA" type="{}NAAM-AUTOR-DIA" minOccurs="0"/>
 *         &lt;element name="ONGELD-CODE-OV" type="{}ONGELD-CODE-OV" minOccurs="0"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ONG-VERKL-GEG",
    propOrder = {

    })
public class ONGVERKLGEG implements Serializable {

  @XmlElement(name = "INLEV-DAT-OV")
  private BigInteger inlevdatov;
  @XmlElement(name = "SRT-AUT-INL-OV")
  private String     srtautinlov;
  @XmlElement(name = "AUTOR-C-INL-OV")
  private BigInteger autorcinlov;
  @XmlElement(name = "NAAM-AUT-INL-OV")
  private String     naamautinlov;
  @XmlElement(name = "NAAM-INL-OV-DIA")
  private String     naaminlovdia;
  @XmlElement(name = "ONGELD-CODE-OV")
  private String     ongeldcodeov;

  public BigInteger getInlevdatov() {
    return this.inlevdatov;
  }

  public void setInlevdatov(BigInteger inlevdatov) {
    this.inlevdatov = inlevdatov;
  }

  public String getSrtautinlov() {
    return this.srtautinlov;
  }

  public void setSrtautinlov(String srtautinlov) {
    this.srtautinlov = srtautinlov;
  }

  public BigInteger getAutorcinlov() {
    return this.autorcinlov;
  }

  public void setAutorcinlov(BigInteger autorcinlov) {
    this.autorcinlov = autorcinlov;
  }

  public String getNaamautinlov() {
    return this.naamautinlov;
  }

  public void setNaamautinlov(String naamautinlov) {
    this.naamautinlov = naamautinlov;
  }

  public String getNaaminlovdia() {
    return this.naaminlovdia;
  }

  public void setNaaminlovdia(String naaminlovdia) {
    this.naaminlovdia = naaminlovdia;
  }

  public String getOngeldcodeov() {
    return this.ongeldcodeov;
  }

  public void setOngeldcodeov(String ongeldcodeov) {
    this.ongeldcodeov = ongeldcodeov;
  }

}
