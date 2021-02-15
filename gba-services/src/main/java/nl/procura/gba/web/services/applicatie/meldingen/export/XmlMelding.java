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

package nl.procura.gba.web.services.applicatie.meldingen.export;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "melding")
public class XmlMelding implements Serializable {

  private static final long serialVersionUID = -2450599889922476071L;
  private String            type             = "";
  private String            tijdstip         = "";
  private String            gebruiker        = "";
  private String            melding          = "";
  private String            oorzaken         = "";
  private String            exceptie         = "";

  public String getExceptie() {
    return exceptie;
  }

  public void setExceptie(String exceptie) {
    this.exceptie = exceptie;
  }

  public String getGebruiker() {
    return gebruiker;
  }

  public void setGebruiker(String gebruiker) {
    this.gebruiker = gebruiker;
  }

  public String getMelding() {
    return melding;
  }

  public void setMelding(String melding) {
    this.melding = melding;
  }

  public String getOorzaken() {
    return oorzaken;
  }

  public void setOorzaken(String oorzaken) {
    this.oorzaken = oorzaken;
  }

  public String getTijdstip() {
    return tijdstip;
  }

  public void setTijdstip(String tijdstip) {
    this.tijdstip = tijdstip;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }
}
