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

package nl.procura.gba.web.common.misc.email.templates;

import static nl.procura.gba.web.services.beheer.link.PersonenLinkType.WACHTWOORD_RESET;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.common.misc.email.EmailAddressType;
import nl.procura.gba.web.common.misc.email.Verzending;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.beheer.email.EmailTemplate;
import nl.procura.gba.web.services.beheer.email.EmailVariable;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.gba.web.services.beheer.gebruiker.GebruikerService;
import nl.procura.gba.web.services.beheer.link.LinkService;
import nl.procura.gba.web.services.beheer.link.PersonenLink;
import nl.procura.gba.web.services.beheer.link.PersonenLinkProperty;
import nl.procura.standard.ProcuraDate;

public class WachtwoordVergetenEmail extends TemplateEmail {

  public WachtwoordVergetenEmail(EmailTemplate template, Services services, String applicationUrl,
      Verzending verzending) {

    super(template, services, applicationUrl);

    Gebruiker gebruiker = verzending.getGebruiker();
    String emailAdres = verzending.getEmailAdres();

    String url = applicationUrl.replaceAll("/$", "");
    GebruikerService gebruikers = services.getGebruikerService();
    LinkService links = services.getLinkService();

    // Email opzoeken in de database
    PersonenLink link = new PersonenLink(links.getRandomId(), WACHTWOORD_RESET);

    // Properties toevoegen aan de link
    link.getProperties().put(PersonenLinkProperty.GEBRUIKER.getCode(), gebruiker.getGebruikersnaam());
    link.getProperties().put(PersonenLinkProperty.EMAIL.getCode(), emailAdres);

    // Verloopdatum toevoegen
    link.setDatumEinde(new DateTime(new ProcuraDate().addDays(getDuration()).getDateFormat()));
    links.addPersonenLink(link);

    // Maak wachtwoord
    getAdresses().add(EmailAddressType.TO, "", gebruiker.getEmail());

    replaceValue(EmailVariable.NAAM.getCode(), gebruiker.getNaam());
    replaceValue(EmailVariable.LINK.getCode(), url + "?link=" + link.getId());
    replaceValue(EmailVariable.LINK_GELDIG.getCode(), getDurationInDays());

    // Wachtwoord resetten
    gebruikers.setWachtwoord(gebruiker, "", true);
  }
}
