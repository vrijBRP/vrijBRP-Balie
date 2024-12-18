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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for UITG-MAATR-GEG complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="UITG-MAATR-GEG">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="MAATREGEL-GEG" type="{}MAATREGEL-GEG" minOccurs="0"/>
 *         &lt;element name="VORD-PROC-GEG" type="{}VORD-PROC-GEG" minOccurs="0"/>
 *         &lt;element name="ONG-VERKL-GEG" type="{}ONG-VERKL-GEG" minOccurs="0"/>
 *         &lt;element name="ONGELD-CAT-TAB" type="{}ONGELD-CAT-TAB" minOccurs="0"/>
 *         &lt;element name="STRAF-MAATR-GEG" type="{}STRAF-MAATR-GEG" minOccurs="0"/>
 *         &lt;element name="INHOUD-PER-TAB" type="{}INHOUD-PER-TAB" minOccurs="0"/>
 *         &lt;element name="ONTZEG-PER-TAB" type="{}ONTZEG-PER-TAB" minOccurs="0"/>
 *         &lt;element name="INNEM-RYB-GEG" type="{}INNEM-RYB-GEG" minOccurs="0"/>
 *         &lt;element name="INNEM-PER-TAB" type="{}INNEM-PER-TAB" minOccurs="0"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UITG-MAATR-GEG",
    propOrder = {

    })
public class UITGMAATRGEG implements Serializable {

  @XmlElement(name = "MAATREGEL-GEG")
  private MAATREGELGEG  maatregelgeg;
  @XmlElement(name = "VORD-PROC-GEG")
  private VORDPROCGEG   vordprocgeg;
  @XmlElement(name = "ONG-VERKL-GEG")
  private ONGVERKLGEG   ongverklgeg;
  @XmlElement(name = "ONGELD-CAT-TAB")
  private ONGELDCATTAB  ongeldcattab;
  @XmlElement(name = "STRAF-MAATR-GEG")
  private STRAFMAATRGEG strafmaatrgeg;
  @XmlElement(name = "INHOUD-PER-TAB")
  private INHOUDPERTAB  inhoudpertab;
  @XmlElement(name = "ONTZEG-PER-TAB")
  private ONTZEGPERTAB  ontzegpertab;
  @XmlElement(name = "INNEM-RYB-GEG")
  private INNEMRYBGEG   innemrybgeg;
  @XmlElement(name = "INNEM-PER-TAB")
  private INNEMPERTAB   innempertab;

  public MAATREGELGEG getMaatregelgeg() {
    return this.maatregelgeg;
  }

  public void setMaatregelgeg(MAATREGELGEG maatregelgeg) {
    this.maatregelgeg = maatregelgeg;
  }

  public VORDPROCGEG getVordprocgeg() {
    return this.vordprocgeg;
  }

  public void setVordprocgeg(VORDPROCGEG vordprocgeg) {
    this.vordprocgeg = vordprocgeg;
  }

  public ONGVERKLGEG getOngverklgeg() {
    return this.ongverklgeg;
  }

  public void setOngverklgeg(ONGVERKLGEG ongverklgeg) {
    this.ongverklgeg = ongverklgeg;
  }

  public ONGELDCATTAB getOngeldcattab() {
    return this.ongeldcattab;
  }

  public void setOngeldcattab(ONGELDCATTAB ongeldcattab) {
    this.ongeldcattab = ongeldcattab;
  }

  public STRAFMAATRGEG getStrafmaatrgeg() {
    return this.strafmaatrgeg;
  }

  public void setStrafmaatrgeg(STRAFMAATRGEG strafmaatrgeg) {
    this.strafmaatrgeg = strafmaatrgeg;
  }

  public INHOUDPERTAB getInhoudpertab() {
    return this.inhoudpertab;
  }

  public void setInhoudpertab(INHOUDPERTAB inhoudpertab) {
    this.inhoudpertab = inhoudpertab;
  }

  public ONTZEGPERTAB getOntzegpertab() {
    return this.ontzegpertab;
  }

  public void setOntzegpertab(ONTZEGPERTAB ontzegpertab) {
    this.ontzegpertab = ontzegpertab;
  }

  public INNEMRYBGEG getInnemrybgeg() {
    return this.innemrybgeg;
  }

  public void setInnemrybgeg(INNEMRYBGEG innemrybgeg) {
    this.innemrybgeg = innemrybgeg;
  }

  public INNEMPERTAB getInnempertab() {
    return this.innempertab;
  }

  public void setInnempertab(INNEMPERTAB innempertab) {
    this.innempertab = innempertab;
  }

}
