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

package nl.procura.gba.web.services.beheer.profiel;

import static java.util.Arrays.asList;
import static nl.procura.gba.common.MiscUtils.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import nl.procura.gba.jpa.personen.dao.ProfileDao;
import nl.procura.gba.jpa.personen.db.Usr;
import nl.procura.gba.jpa.personen.utils.GbaDaoUtils;
import nl.procura.gba.web.services.AbstractService;
import nl.procura.gba.web.services.aop.ThrowException;
import nl.procura.gba.web.services.aop.Transactional;
import nl.procura.gba.web.services.beheer.KoppelActie;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;

public class ProfielService extends AbstractService {

  public ProfielService() {
    super("Profielen");
  }

  /**
   * Laad de profielen van een specifieke gebruiker
   */
  @ThrowException("Fout bij ophalen profielen")
  public Profielen getGebruikerProfielen(Gebruiker gebruiker) {

    Profielen profielen = new Profielen();
    profielen.getAlle().addAll(
        copyList(findEntity(Usr.class, gebruiker.getCUsr()).getProfiles(), Profiel.class));

    for (Profiel profiel : profielen.getAlle()) {
      herlaadProfiel(profiel);
    }

    return profielen;
  }

  @ThrowException("Fout bij het tonen van profielen")
  public List<Profiel> getProfielen() {
    return copyList(ProfileDao.find(), Profiel.class);
  }

  public Set<Gebruiker> getProfielGebruikers(Profiel profiel) {
    return copySet(to(profiel, Profiel.class).getUsrs(), Gebruiker.class);
  }

  /**
   * De setfuncties zoeken in de database naar gekoppelde acties, velden en elementen
   * en voegen deze toe aan het profielobject.
   */
  public void herlaadProfiel(Profiel profiel) {

    Profiel p = profiel;

    p.setActies(getServices().getProfielExtrasService().getActies(p));
    p.setVelden(getServices().getProfielExtrasService().getVelden(p));
    p.setElementen(getServices().getProfielExtrasService().getElementen(p));
    p.setCategorieen(getServices().getProfielExtrasService().getCategorieen(p));
    p.setIndicaties(getServices().getProfielExtrasService().getIndicaties(p));
    p.setParameters(getServices().getProfielExtrasService().getParameters(p));
    p.setZaakConfiguraties(getServices().getProfielExtrasService().getZaakConfiguraties(p));
  }

  @ThrowException("Fout bij het koppelen/ontkoppelen van record")
  @Transactional
  public void koppelActie(List<? extends KoppelbaarAanProfiel> koppelList,
      List<Profiel> profList,
      KoppelActie koppelActie) {

    for (Profiel profiel : profList) {
      for (KoppelbaarAanProfiel koppelObject : koppelList) {
        KoppelbaarAanProfiel dbKoppelObject = checkDatabaseRepresentation(koppelObject);
        if (koppelActie.isPossible(profiel.isGekoppeld(asList(dbKoppelObject)))) {
          dbKoppelObject.koppelActie(profiel, koppelActie);
          koppelObject.koppelActie(profiel, koppelActie);
          profiel.koppelActie(dbKoppelObject, koppelActie);
          saveEntity(dbKoppelObject);
        }
      }

      saveEntity(profiel);
    }
  }

  @Transactional
  public void save(Profiel profiel) {
    saveEntity(profiel);
  }

  @ThrowException("Fout bij het verwijderen van profielen")
  @Transactional
  public void delete(Profiel profiel) {
    removeEntity(profiel);
  }

  /**
   * Deze functie controleert of koppelObject een key naar een bijbehorend jpa-object heeft.
   * Is koppelObject nog niet opgeslagen in de database dan wordt dit uitgevoerd.
   */
  private KoppelbaarAanProfiel checkDatabaseRepresentation(KoppelbaarAanProfiel koppelObject) {
    Optional<Object> entity = findEntity(GbaDaoUtils.getEntity(koppelObject)).stream().findFirst();
    if (entity.isPresent()) {
      return copy(entity.get(), koppelObject.getClass());
    }

    saveEntity(koppelObject);
    return koppelObject;
  }
}
