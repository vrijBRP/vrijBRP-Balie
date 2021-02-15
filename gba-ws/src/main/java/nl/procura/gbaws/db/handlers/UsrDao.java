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

package nl.procura.gbaws.db.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.procura.gba.common.password.DelegatingPasswordEncoder;
import nl.procura.gba.common.password.PasswordEncoder;
import nl.procura.gba.jpa.personenws.db.Usr;
import nl.procura.gba.jpa.personenws.utils.GbaWsJpa;
import nl.procura.gbaws.db.wrappers.UsrWrapper;

public class UsrDao extends GenericDao {

  private static final Logger          LOGGER           = LoggerFactory.getLogger(UsrDao.class);
  private static final PasswordEncoder PASSWORD_ENCODER = new DelegatingPasswordEncoder(1);

  public static UsrWrapper getGebruiker(String username) {
    for (final UsrWrapper g : getUsers()) {
      if (g.getGebruikersNaam().equals(username)) {
        return g;
      }
    }
    return null;
  }

  public static UsrWrapper getUser(String username, String password) {
    return getUserByName(username)
        .filter(u -> PASSWORD_ENCODER.matches(password, u.getPw()))
        .map(UsrWrapper::new)
        .orElse(null);
  }

  private static Optional<Usr> getUserByName(String username) {
    return GbaWsJpa.getManager().createQuery("select g from Usr g" +
        " where g.usr = :usr" +
        " order by g.usr", Usr.class)
        .setParameter("usr", username)
        .getResultList()
        .stream()
        .findFirst();
  }

  @SuppressWarnings("unchecked")
  public static List<UsrWrapper> getUsers() {

    final List<UsrWrapper> l = new ArrayList<>();

    try {

      final Query q = GbaWsJpa.getManager().createQuery("select g from Usr g order by g.usr");
      final List<Usr> list = q.getResultList();

      for (final Usr usr : list) {
        l.add(new UsrWrapper(usr));
      }
    } catch (final RuntimeException e) {
      LOGGER.debug(e.toString());
    }

    return l;
  }

  public static int updatePasswords(EntityManager entityManager) {
    List<Usr> users = entityManager.createQuery("select u from Usr u" +
        " where u.pw like '{0}%'", Usr.class)
        .getResultList();
    users.forEach(user -> user.setPw(PASSWORD_ENCODER.encode(user.getPw().substring(3))));
    return users.size();
  }

}
