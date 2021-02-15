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

import javax.persistence.EntityManager;
import javax.persistence.Query;

import nl.procura.gba.jpa.personenws.db.Profile;
import nl.procura.gba.jpa.personenws.utils.GbaWsJpa;
import nl.procura.gbaws.db.wrappers.ProfileWrapper;
import nl.procura.gbaws.web.vaadin.module.auth.profile.page1.ElementsProfile;

public class ProfileDao extends GenericDao {

  @SuppressWarnings("unchecked")
  public static List<ProfileWrapper> getProfiles() {

    final List<ProfileWrapper> l = new ArrayList<>();
    final EntityManager m = GbaWsJpa.getManager();
    final Query q = m.createQuery("select p from Profile p order by p.profile");

    final List<Profile> list = q.getResultList();

    for (final Profile profile : list) {
      l.add(new ProfileWrapper(profile));
    }

    return l;
  }

  public static void switchKoppeling(ElementsProfile ep, List<ElementWrapper> elems) {

    final EntityManager em = GbaWsJpa.getManager();

    em.getTransaction().begin();

    for (ElementWrapper elem : elems) {

      if (elem.isGekoppeld()) {
        ep.getProfile().removeElement(elem.getCatCode(), elem.getElemCode(), ep.getDatabaseType(),
            ep.getRefDatabase());
      } else {
        ep.getProfile().addElement(elem.getCatCode(), elem.getElemCode(), ep.getDatabaseType(),
            ep.getRefDatabase());
      }

      elem.setGekoppeld(!elem.isGekoppeld());
    }

    em.getTransaction().commit();
  }

  public static void koppeling(ElementsProfile ep, List<ElementWrapper> elems) {

    final EntityManager em = GbaWsJpa.getManager();
    em.getTransaction().begin();

    for (ElementWrapper elem : elems) {
      if (!elem.isGekoppeld()) {
        ep.getProfile().addElement(elem.getCatCode(), elem.getElemCode(), ep.getDatabaseType(),
            ep.getRefDatabase());
        elem.setGekoppeld(true);
      }
    }

    em.getTransaction().commit();
  }

  public static void ontKoppeling(ElementsProfile ep, List<ElementWrapper> elems) {

    final EntityManager em = GbaWsJpa.getManager();
    em.getTransaction().begin();

    for (ElementWrapper elem : elems) {

      if (elem.isGekoppeld()) {
        ep.getProfile().removeElement(elem.getCatCode(), elem.getElemCode(), ep.getDatabaseType(),
            ep.getRefDatabase());
        elem.setGekoppeld(false);
      }
    }

    em.getTransaction().commit();
  }

  public static class ElementWrapper {

    private final int elemCode;
    private final int catCode;
    private boolean   gekoppeld;

    public ElementWrapper(boolean gekoppeld, int catCode, int elemCode) {
      this.gekoppeld = gekoppeld;
      this.catCode = catCode;
      this.elemCode = elemCode;
    }

    public boolean isGekoppeld() {
      return gekoppeld;
    }

    public void setGekoppeld(boolean gekoppeld) {
      this.gekoppeld = gekoppeld;
    }

    public int getElemCode() {
      return elemCode;
    }

    public int getCatCode() {
      return catCode;
    }
  }
}
