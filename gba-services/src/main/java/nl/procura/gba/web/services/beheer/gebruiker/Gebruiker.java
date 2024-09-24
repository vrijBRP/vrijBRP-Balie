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

package nl.procura.gba.web.services.beheer.gebruiker;

import static ch.lambdaj.Lambda.*;
import static nl.procura.gba.web.common.tables.GbaTables.PLAATS;
import static nl.procura.standard.Globalfunctions.*;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.WARNING;
import static nl.procura.commons.core.exceptions.ProExceptionType.ENTRY;
import static org.hamcrest.Matchers.equalTo;

import java.util.List;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.common.MiscUtils;
import nl.procura.gba.jpa.personen.db.*;
import nl.procura.gba.web.services.beheer.KoppelActie;
import nl.procura.gba.web.services.beheer.gebruiker.info.GebruikerInformatie;
import nl.procura.gba.web.services.beheer.locatie.KoppelbaarAanLocatie;
import nl.procura.gba.web.services.beheer.locatie.Locatie;
import nl.procura.gba.web.services.beheer.parameter.Parameter;
import nl.procura.gba.web.services.beheer.parameter.ParameterConstant;
import nl.procura.gba.web.services.beheer.parameter.Parameters;
import nl.procura.gba.web.services.beheer.profiel.KoppelbaarAanProfiel;
import nl.procura.gba.web.services.beheer.profiel.Profiel;
import nl.procura.gba.web.services.beheer.profiel.Profielen;
import nl.procura.gba.web.services.interfaces.DatabaseTable;
import nl.procura.gba.web.services.interfaces.Geldigheid;
import nl.procura.gba.web.services.interfaces.GeldigheidStatus;
import nl.procura.gba.web.services.zaken.documenten.DocumentRecord;
import nl.procura.gba.web.services.zaken.documenten.KoppelbaarAanDocument;
import nl.procura.java.reflection.ReflectionUtil;
import nl.procura.commons.core.exceptions.ProException;

public class Gebruiker extends Usr
    implements KoppelbaarAanDocument, KoppelbaarAanLocatie, KoppelbaarAanProfiel, Geldigheid, DatabaseTable {

  private static final int    MAX_INLOGPOGINGEN           = 3;
  private static final String GEEN_GEMEENTECODE           = "Gemeentecode niet ingevuld in de parameters";
  private static final String BEGIN_COUPLIN_ERROR_MESSAGE = "Object van type ";
  private static final String END_COUPLIN_ERROR_MESSAGE   = " kan niet gekoppeld worden aan een gebruiker.";

  private Locatie             locatie    = new Locatie();
  private Parameters          parameters = new Parameters();
  private Profielen           profielen  = new Profielen();
  private GebruikerInformatie informatie = new GebruikerInformatie();

  private String  ipAdres            = "";
  private String  email              = "";
  private String  telefoonnummer     = "";
  private String  afdeling           = "";
  private boolean wachtwoordVerlopen = false;

  public Gebruiker() {
    setLocatie(Locatie.getDefault());
  }

  public static Gebruiker getDefault() {
    Gebruiker g = new Gebruiker();
    g.setCUsr(BaseEntity.DEFAULT);
    return g;
  }

  public String getAfdeling() {
    return afdeling;
  }

  public void setAfdeling(String afdeling) {
    this.afdeling = afdeling;
  }

  @Override
  public DateTime getDatumEinde() {
    return new DateTime(getDEnd());
  }

  @Override
  public void setDatumEinde(DateTime dateTime) {
    setDEnd(toBigDecimal(dateTime.getLongDate()));
  }

  @Override
  public DateTime getDatumIngang() {
    return new DateTime(getDIn());
  }

  @Override
  public void setDatumIngang(DateTime dateTime) {
    setDIn(toBigDecimal(dateTime.getLongDate()));
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getGebruikersnaam() {
    return getUsr();
  }

  public void setGebruikersnaam(String gebruikersnaam) {
    setUsr(gebruikersnaam);
  }

  @Override
  public GeldigheidStatus getGeldigheidStatus() {
    return GeldigheidStatus.get(this);
  }

  public String getGemeente() {
    return PLAATS.get(getGemeenteCode()).getDescription();
  }

  public String getGemeenteCode() {

    Parameter p = getParameters().get(ParameterConstant.GEMEENTE_CODES);

    if (emp(p.getValue())) {
      throw new ProException(ENTRY, WARNING, GEEN_GEMEENTECODE);
    }

    for (String value : p.getValue().split(",")) {
      if (pos(value)) {
        return String.format("%04d", along(value));
      }
    }

    return String.format("%04d", 0);
  }

  public GebruikerInformatie getInformatie() {
    return informatie;
  }

  public void setInformatie(GebruikerInformatie informatie) {
    this.informatie = informatie;
  }

  public String getIpAdres() {
    return ipAdres;
  }

  public void setIpAdres(String ipAdres) {
    this.ipAdres = ipAdres;
  }

  public Locatie getLocatie() {
    return locatie;
  }

  public void setLocatie(Locatie locatie) {
    this.locatie = locatie;
  }

  public String getNaam() {
    return getUsrfullname();
  }

  public void setNaam(String naam) {
    setUsrfullname(naam);
  }

  public String getOmschrijving() {
    return getDescr();
  }

  public void setOmschrijving(String omschrijving) {
    setDescr(omschrijving);
  }

  public String getPad() {
    return getPath();
  }

  public void setPad(String pad) {
    setPath(pad);
  }

  public Parameters getParameters() {
    return parameters;
  }

  public void setParameters(Parameters parameters) {
    this.parameters = parameters;
  }

  public Profielen getProfielen() {
    return profielen;
  }

  public void setProfielen(Profielen profielen) {
    this.profielen = profielen;
  }

  public String getTelefoonnummer() {
    return telefoonnummer;
  }

  public void setTelefoonnummer(String telefoonnummer) {
    this.telefoonnummer = telefoonnummer;
  }

  public boolean heeftLocatie() {
    return getLocatie().getCLocation() > 0;
  }

  public boolean isAdministrator() {
    return pos(getAdmin());
  }

  public void setAdministrator(boolean b) {
    setAdmin(toBigDecimal(b ? 1 : 0));
  }

  public boolean isGeblokkeerd() {
    return getBlok() != null && (aval(getBlok()) >= MAX_INLOGPOGINGEN);
  }

  public void setGeblokkeerd(boolean b) {
    setBlok(toBigDecimal(b ? MAX_INLOGPOGINGEN : 0));
  }

  @Override
  public boolean isGekoppeld(DocumentRecord document) {
    if (document.isIedereenToegang()) {
      return true;
    }
    return exists(getDocuments(), having(on(Document.class), equalTo(document)));
  }

  public <K extends KoppelbaarAanGebruiker> boolean isGekoppeld(List<K> objectList) {
    for (K object : objectList) {
      boolean isGekoppeld;
      if (object instanceof Profiel) {
        isGekoppeld = isGekoppeld((Profiel) object);
      } else if (object instanceof DocumentRecord) {
        isGekoppeld = isGekoppeld((DocumentRecord) object);
      } else if (object instanceof Locatie) {
        isGekoppeld = isGekoppeld((Locatie) object);
      } else {
        throw new IllegalArgumentException(
            BEGIN_COUPLIN_ERROR_MESSAGE + object.getClass() + END_COUPLIN_ERROR_MESSAGE);
      }

      if (isGekoppeld) {
        return true;
      }
    }

    return false;
  }

  @Override
  public boolean isGekoppeld(Locatie locatie) {
    return MiscUtils.contains(locatie, getLocations());
  }

  @Override
  public boolean isGekoppeld(Profiel profiel) {
    return MiscUtils.contains(profiel, getProfiles());
  }

  public boolean isGemeente(long gemeenteCode) {

    Parameter p = getParameters().get(ParameterConstant.GEMEENTE_CODES);
    if (emp(p.getValue())) {
      throw new ProException(ENTRY, WARNING, GEEN_GEMEENTECODE);
    }

    for (String value : p.getValue().split(",")) {
      if (aval(value) == gemeenteCode) {
        return true;
      }
    }

    return false;
  }

  public boolean isWachtwoordVerlopen() {
    return wachtwoordVerlopen;
  }

  public void setWachtwoordVerlopen(boolean wachtwoordVerlopen) {
    this.wachtwoordVerlopen = wachtwoordVerlopen;
  }

  public <K extends KoppelbaarAanGebruiker> void koppelActie(K koppelObject, KoppelActie koppelActie) {
    if (koppelObject instanceof DocumentRecord) {
      koppelActie((DocumentRecord) koppelObject, koppelActie);
    } else if (koppelObject instanceof Locatie) {
      koppelActie((Locatie) koppelObject, koppelActie);
    } else if (koppelObject instanceof Profiel) {
      koppelActie((Profiel) koppelObject, koppelActie);
    } else {
      throw new IllegalArgumentException(
          BEGIN_COUPLIN_ERROR_MESSAGE + koppelObject.getClass() + END_COUPLIN_ERROR_MESSAGE);
    }
  }

  @Override
  public void koppelActie(DocumentRecord document, KoppelActie koppelActie) {
    if (KoppelActie.KOPPEL == koppelActie) {
      getDocuments().add(ReflectionUtil.deepCopyBean(Document.class, document));
    } else {
      getDocuments().remove(document);
    }
  }

  @Override
  public void koppelActie(Locatie locatie, KoppelActie koppelActie) {
    if (KoppelActie.KOPPEL == koppelActie) {
      getLocations().add(ReflectionUtil.deepCopyBean(Location.class, locatie));
    } else {
      getLocations().remove(locatie);
    }
  }

  @Override
  public void koppelActie(Profiel profiel, KoppelActie koppelActie) {
    if (KoppelActie.KOPPEL == koppelActie) {
      getProfiles().add(ReflectionUtil.deepCopyBean(Profile.class, profiel));
    } else {
      getProfiles().remove(profiel);
    }
  }

  public boolean isInlogbaar() {
    return emp(getAccountProbleem());
  }

  public String getAccountProbleem() {

    if (isGeblokkeerd()) {
      return "Account is geblokkeerd";
    }
    if (isWachtwoordVerlopen()) {
      return "Wachtwoord is verlopen";
    }
    if (!getGeldigheidStatus().is(GeldigheidStatus.ACTUEEL)) {
      return "Account is (nog) niet geldig";
    }
    return "";
  }

  @Override
  public String toString() {
    return astr(getNaam());
  }
}
