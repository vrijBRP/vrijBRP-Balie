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
// Generated on: 2018.06.26 at 02:32:31 PM CEST 
//

package nl.procura.rdw.processen.p1914.f02;

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
 *         &lt;element name="GBA-NR-NAT-P" type="{}GBA-NR"/>
 *         &lt;element name="BURG-SERVICE-NR" type="{}FISC-NR-NAT-P"/>
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

  @XmlElement(name = "GBA-NR-NAT-P",
      required = true)
  private BigInteger gbanrnatp;
  @XmlElement(name = "BURG-SERVICE-NR",
      required = true)
  private BigInteger burgservicenr;

  public BigInteger getGbanrnatp() {
    return this.gbanrnatp;
  }

  public void setGbanrnatp(BigInteger gbanrnatp) {
    this.gbanrnatp = gbanrnatp;
  }

  public BigInteger getBurgservicenr() {
    return this.burgservicenr;
  }

  public void setBurgservicenr(BigInteger burgservicenr) {
    this.burgservicenr = burgservicenr;
  }

}
