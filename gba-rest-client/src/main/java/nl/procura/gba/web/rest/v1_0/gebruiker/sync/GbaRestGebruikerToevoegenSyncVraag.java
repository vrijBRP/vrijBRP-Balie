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

package nl.procura.gba.web.rest.v1_0.gebruiker.sync;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "vraag")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "gebruikersnaam", "naam", "admin", "geblokkeerd", "datumIngang", "datumEinde" })
public class GbaRestGebruikerToevoegenSyncVraag {

  private String  gebruikersnaam = "";
  private String  naam           = "";
  private boolean admin          = false;
  private boolean geblokkeerd    = false;
  private long    datumIngang    = -1;
  private long    datumEinde     = -1;

  public String getGebruikersnaam() {
    return gebruikersnaam;
  }

  public void setGebruikersnaam(String gebruikersnaam) {
    this.gebruikersnaam = gebruikersnaam;
  }

  public String getNaam() {
    return naam;
  }

  public void setNaam(String naam) {
    this.naam = naam;
  }

  public boolean isAdmin() {
    return admin;
  }

  public void setAdmin(boolean admin) {
    this.admin = admin;
  }

  public long getDatumIngang() {
    return datumIngang;
  }

  public void setDatumIngang(long datumIngang) {
    this.datumIngang = datumIngang;
  }

  public long getDatumEinde() {
    return datumEinde;
  }

  public void setDatumEinde(long datumEinde) {
    this.datumEinde = datumEinde;
  }

  public boolean isGeblokkeerd() {
    return geblokkeerd;
  }

  public void setGeblokkeerd(boolean geblokkeerd) {
    this.geblokkeerd = geblokkeerd;
  }
}
