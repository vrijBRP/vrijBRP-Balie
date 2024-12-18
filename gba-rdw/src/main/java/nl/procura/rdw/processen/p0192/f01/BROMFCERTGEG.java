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
// Generated on: 2018.06.26 at 02:32:27 PM CEST 
//

package nl.procura.rdw.processen.p0192.f01;

import java.io.Serializable;
import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for BROMF-CERT-GEG complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="BROMF-CERT-GEG">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="BROMF-CERT-NR" type="{}BROMF-CERT-NR"/>
 *         &lt;element name="ONGELD-DAT-BC" type="{}DATUM-EJMD" minOccurs="0"/>
 *         &lt;element name="ONGELD-CODE-BC" type="{}ONGELD-CODE-BC" minOccurs="0"/>
 *         &lt;element name="INLEV-DAT-BC" type="{}DATUM-EJMD"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BROMF-CERT-GEG",
    propOrder = {

    })
public class BROMFCERTGEG implements Serializable {

  @XmlElement(name = "BROMF-CERT-NR",
      required = true)
  private BigInteger bromfcertnr;
  @XmlElement(name = "ONGELD-DAT-BC")
  private BigInteger ongelddatbc;
  @XmlElement(name = "ONGELD-CODE-BC")
  private BigInteger ongeldcodebc;
  @XmlElement(name = "INLEV-DAT-BC",
      required = true)
  private BigInteger inlevdatbc;

  public BigInteger getBromfcertnr() {
    return this.bromfcertnr;
  }

  public void setBromfcertnr(BigInteger bromfcertnr) {
    this.bromfcertnr = bromfcertnr;
  }

  public BigInteger getOngelddatbc() {
    return this.ongelddatbc;
  }

  public void setOngelddatbc(BigInteger ongelddatbc) {
    this.ongelddatbc = ongelddatbc;
  }

  public BigInteger getOngeldcodebc() {
    return this.ongeldcodebc;
  }

  public void setOngeldcodebc(BigInteger ongeldcodebc) {
    this.ongeldcodebc = ongeldcodebc;
  }

  public BigInteger getInlevdatbc() {
    return this.inlevdatbc;
  }

  public void setInlevdatbc(BigInteger inlevdatbc) {
    this.inlevdatbc = inlevdatbc;
  }

}
