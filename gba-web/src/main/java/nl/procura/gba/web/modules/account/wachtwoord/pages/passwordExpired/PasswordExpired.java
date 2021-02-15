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

package nl.procura.gba.web.modules.account.wachtwoord.pages.passwordExpired;

import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.gba.web.services.beheer.gebruiker.GebruikerService;
import nl.procura.gba.web.services.beheer.link.LinkService;
import nl.procura.gba.web.services.beheer.link.PersonenLink;

public class PasswordExpired {

  private Gebruiker        gebruiker;
  private PersonenLink     personenLink;
  private GebruikerService gebruikers;
  private LinkService      links;

  public PasswordExpired(Gebruiker gebruiker, PersonenLink personenLink, GebruikerService gebruikers,
      LinkService links) {
    this.gebruiker = gebruiker;
    this.personenLink = personenLink;
    this.gebruikers = gebruikers;
    this.links = links;
  }

  public Gebruiker getGebruiker() {
    return gebruiker;
  }

  public void setGebruiker(Gebruiker gebruiker) {
    this.gebruiker = gebruiker;
  }

  public GebruikerService getGebruikers() {
    return gebruikers;
  }

  public void setGebruikers(GebruikerService gebruikers) {
    this.gebruikers = gebruikers;
  }

  public LinkService getLinks() {
    return links;
  }

  public void setLinks(LinkService links) {
    this.links = links;
  }

  public PersonenLink getPersonenLink() {
    return personenLink;
  }

  public void setPersonenLink(PersonenLink personenLink) {
    this.personenLink = personenLink;
  }
}
