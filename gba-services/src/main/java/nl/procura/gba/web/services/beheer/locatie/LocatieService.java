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

package nl.procura.gba.web.services.beheer.locatie;

import static ch.lambdaj.Lambda.*;
import static nl.procura.gba.common.MiscUtils.copy;
import static nl.procura.gba.common.MiscUtils.copyList;
import static nl.procura.gba.web.services.beheer.locatie.LocatieType.NORMALE_LOCATIE;
import static nl.procura.standard.Globalfunctions.astr;

import java.net.InetAddress;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.procura.gba.jpa.personen.dao.LocationDao;
import nl.procura.gba.jpa.personen.db.Location;
import nl.procura.gba.jpa.personen.db.Printoptie;
import nl.procura.gba.jpa.personen.db.Usr;
import nl.procura.gba.web.services.AbstractService;
import nl.procura.gba.web.services.ServiceEvent;
import nl.procura.gba.web.services.aop.ThrowException;
import nl.procura.gba.web.services.aop.Transactional;
import nl.procura.gba.web.services.beheer.KoppelActie;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.gba.web.services.zaken.documenten.printopties.PrintOptie;
import nl.procura.standard.exceptions.ProException;

public class LocatieService extends AbstractService {

  private static final String DECOUPLE_ERROR_MESSAGE = "Fout bij het onkoppelen van record.";
  private static final Logger LOGGER                 = LoggerFactory.getLogger(LocatieService.class.getName());

  public LocatieService() {
    super("Locaties");
  }

  @ThrowException("Fout bij het verwijderen van locatie")
  @Transactional
  public void delete(Locatie locatie) {
    removeEntity(locatie);
  }

  @ThrowException("Fout bij het ophalen van de locaties")
  public List<Locatie> getAlleLocaties(LocatieType... types) {
    List<Long> codes = new ArrayList<>();
    for (LocatieType type : types) {
      codes.add(type.getCode());
    }
    return copyList(LocationDao.find(codes), Locatie.class);
  }

  public Locatie getGebruikerLocatie(long cLocatie) {
    for (Locatie l : getGebruikerLocaties(getServices().getGebruiker(), NORMALE_LOCATIE).getLocaties()) {
      if (l.getCLocation().equals(cLocatie)) {
        return l;
      }
    }

    return null;
  }

  /**
   * Geeft de gebruikerlocaties terug op basis van prioriteit
   */
  @ThrowException("Fout bij ophalen van locaties bij de gebruiker")
  public LocatieSelectie getGebruikerLocaties(Gebruiker gebruiker, LocatieType... types) {

    LocatieSelectie selectie = new LocatieSelectie();
    List<Locatie> alleLocaties = getAlleLocaties(types);
    List<Locatie> usrLocaties = getGekoppeldeLocaties(gebruiker, types);
    List<Locatie> matchLocaties = usrLocaties.isEmpty() ? alleLocaties : usrLocaties;
    List<Locatie> alleIpLocaties = select(updateMatch(gebruiker, matchLocaties),
        having(on(Locatie.class).isMatchesIp()));

    if (!alleIpLocaties.isEmpty()) {
      selectie.setMelding(String.format("Deze %s gekoppeld aan uw werkplek.", getAantal(alleIpLocaties)));
      selectie.setLocaties(alleIpLocaties);
    } else if (!usrLocaties.isEmpty()) {
      selectie.setMelding(String.format("Deze %s gekoppeld aan uw account.", getAantal(usrLocaties)));
      selectie.setLocaties(usrLocaties);
    } else {
      selectie.setMelding("Er zijn geen locaties gekoppeld aan uw account. Bepaal zelf de locatie.");
      selectie.setLocaties(alleLocaties);
    }

    Collections.sort(selectie.getLocaties());

    return selectie;
  }

  @ThrowException("Fout bij ophalen van locaties bij de gebruiker")
  public List<Locatie> getGekoppeldeLocaties(Gebruiker gebruiker, LocatieType... types) {

    List<Locatie> usrLocaties = new ArrayList<>();
    Usr usr = findEntity(Usr.class, gebruiker.getCUsr());
    List<Location> usrLocations = usr.getLocations();

    if (!usrLocations.isEmpty()) { // geef nu alle locaties terug
      for (Locatie locatie : copyList(usr.getLocations(), Locatie.class)) {
        if (locatie.getCLocation() != null) {
          for (LocatieType type : types) {
            if (locatie.getCLocation() > 0 && type.equals(locatie.getLocatieType())) {
              usrLocaties.add(locatie);
            }
          }
        }
      }
    }

    return usrLocaties;
  }

  public Set<String> getLookupAdress(String address) {

    Set<String> adresSet = new LinkedHashSet();
    long startTime = System.currentTimeMillis();
    try {
      InetAddress[] elems = InetAddress.getAllByName(address);
      for (InetAddress elem : elems) {
        adresSet.add(astr(elem.getHostAddress()).trim().split("%")[0]);
      }
    } catch (Exception e) {
      long endTime = System.currentTimeMillis();
      long time = endTime - startTime;
      throw new ProException(
          "Fout bij opvragen adres: " + address + ". Afgebroken na " + time
              + " ms. Pas dit aan onder menu -> locaties!!",
          e);
    }

    return adresSet;
  }

  /**
   * Geeft de verzameling van gekoppelde printopties aan de locatie.
   */

  public Set<PrintOptie> getPrintOptions(Locatie locatie) {

    Set<PrintOptie> printopties = new HashSet();
    Locatie locImpl = locatie;

    for (Printoptie po : locImpl.getPrintopties()) {
      PrintOptie printoptie = copy(po, PrintOptie.class);
      printopties.add(printoptie);
    }

    return printopties;
  }

  public boolean isCorrect(Locatie locatie) {

    for (String address : locatie.getIpAddressen()) {
      try {
        getServices().getLocatieService().getLookupAdress(address);
      } catch (Exception e) {
        return false;
      }
    }

    return true;
  }

  @ThrowException(DECOUPLE_ERROR_MESSAGE)
  @Transactional
  public void koppelActie(List<? extends KoppelbaarAanLocatie> koppelList,
      List<Locatie> locList,
      KoppelActie koppelActie) {

    for (KoppelbaarAanLocatie koppelObject : koppelList) {
      for (Locatie loc : locList) {
        if (koppelActie.isPossible(koppelObject.isGekoppeld(loc))) {
          loc.koppelActie(koppelObject, koppelActie);
          koppelObject.koppelActie(loc, koppelActie);
          saveEntity(loc);
        }
      }

      saveEntity(koppelObject);
    }
  }

  @ThrowException("Fout bij het opslaan van locatie")
  @Transactional
  public void save(Locatie locatie) {
    saveEntity(locatie);
  }

  /**
   * Voeg locatie toe aan gebruiker en roept listeners aan
   */
  public void setLocatie(Locatie locatie) {
    getServices().getGebruiker().setLocatie(locatie);
    callListeners(ServiceEvent.CHANGE);
  }

  private String getAantal(List<Locatie> locaties) {
    return (locaties.size() == 1) ? "locatie is" : "locaties zijn";
  }

  private boolean isMatch(Locatie l, Set<String> userAddresses) {

    for (String address : l.getIpAddressen()) {
      try {
        for (String locationAdress : getLookupAdress(address)) {
          for (String userAddress : userAddresses) {
            if (locationAdress.matches(userAddress)) {
              return true;
            }
          }
        }
      } catch (Exception e) {
        LOGGER.error("Fout: " + e.getMessage());
      }
    }

    return false;
  }

  private List<Locatie> updateMatch(Gebruiker gebruiker, List<Locatie> locaties) {
    Set<String> userAdresses = getLookupAdress(gebruiker.getIpAdres());
    for (Locatie locatie : locaties) {
      if (isMatch(locatie, userAdresses)) {
        locatie.setMatchesIp(true);
      }
    }

    return locaties;
  }
}
