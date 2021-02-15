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

package nl.procura.gba.jpa.personen.dao;

import static nl.procura.standard.Globalfunctions.emp;

import java.util.List;

import javax.persistence.TypedQuery;

import nl.procura.gba.common.UniqueList;
import nl.procura.gba.jpa.personen.db.Usr;
import nl.procura.gba.jpa.personen.db.UsrInfo;
import nl.procura.gba.jpa.personen.utils.GbaJpa;
import nl.procura.standard.exceptions.ProException;

public class UsrDao extends GenericDao {

  public static final List<Usr> find() {
    return GbaJpa.getManager().createNamedQuery("Usr.findAll").getResultList();
  }

  public static final List<Usr> findByName(String username) {
    if (emp(username)) {
      throw new ProException("Gebruikersnaam is leeg");
    }

    Usr usr = new Usr();
    usr.setUsr(username);
    return refreshEntities(findByExample(usr));
  }

  public static final List<Usr> findByEmail(String email) {
    List<Usr> usrs = new UniqueList();
    TypedQuery<UsrInfo> query = GbaJpa.getManager().createNamedQuery("UsrInfo.findByEmail", UsrInfo.class);
    query.setParameter("email", email);

    for (UsrInfo info : query.getResultList()) {
      usrs.add(info.getUsr());
    }

    return usrs;
  }

  public static final List<Object[]> findUsrProfiles() {
    String sql = "select usr.c_usr, usrfullname, profile.c_profile, profile from usr, usr_profile, profile "
        + "where usr.c_usr = usr_profile.c_usr and profile.c_profile = usr_profile.c_profile";

    return GbaJpa.getManager().createNativeQuery(sql).getResultList();
  }
}
