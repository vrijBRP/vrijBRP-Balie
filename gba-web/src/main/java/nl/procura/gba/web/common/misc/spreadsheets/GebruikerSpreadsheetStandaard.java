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

package nl.procura.gba.web.common.misc.spreadsheets;

import static ch.lambdaj.Lambda.join;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.WARNING;

import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.gba.web.services.beheer.locatie.LocatieType;
import nl.procura.gba.web.services.zaken.documenten.UitvoerformaatType;
import nl.procura.commons.core.exceptions.ProException;

public class GebruikerSpreadsheetStandaard extends SpreadsheetTemplate {

  private Services        services   = null;
  private List<Gebruiker> gebruikers = new ArrayList<>();

  public GebruikerSpreadsheetStandaard(UitvoerformaatType type) {
    super("Gebruikers spreadsheet", type);
  }

  @Override
  public void compose() {

    if (getGebruikers().isEmpty()) {
      throw new ProException(WARNING, "Geen gebruikers geselecteerd");
    }

    clear();

    add("Code gebruiker");
    add("Gebruikersnaam");
    add("Volledige naam");
    add("Applicatiebeheerder");
    add("Datum ingang account");
    add("Datum einde account");
    add("Wachtwoord verlopen");
    add("Geblokkeerd");
    add("E-mail");
    add("Telefoonnummer");
    add("Afdeling");
    add("Map");
    add("Extra info");
    add("Profielen");
    add("Locaties");

    store();

    for (Gebruiker gebruiker : gebruikers) {

      add(gebruiker.getCUsr());
      add(gebruiker.getGebruikersnaam());
      add(gebruiker.getNaam());
      add(gebruiker.isAdministrator() ? "Ja" : "Nee");
      add(gebruiker.getDatumIngang());
      add(gebruiker.getDatumEinde());
      add(gebruiker.isWachtwoordVerlopen() ? "Ja" : "Nee");
      add(gebruiker.isGeblokkeerd() ? "Ja" : "Nee");
      add(gebruiker.getEmail());
      add(gebruiker.getTelefoonnummer());
      add(gebruiker.getAfdeling());
      add(gebruiker.getPad());
      add(gebruiker.getOmschrijving());
      add(getProfielen(gebruiker));
      add(getLocaties(gebruiker));

      store();
    }
  }

  public List<Gebruiker> getGebruikers() {
    return gebruikers;
  }

  public void setGebruikers(List<Gebruiker> gebruikers) {
    this.gebruikers = gebruikers;
  }

  public void setServices(Services services) {
    this.services = services;
  }

  private String getLocaties(Gebruiker gebruiker) {
    return join(services.getLocatieService().getGekoppeldeLocaties(gebruiker, LocatieType.NORMALE_LOCATIE));
  }

  private String getProfielen(Gebruiker gebruiker) {
    return gebruiker.getProfielen().getAlle().size() > 0 ? join(gebruiker.getProfielen().getAlle()) : "";
  }
}
