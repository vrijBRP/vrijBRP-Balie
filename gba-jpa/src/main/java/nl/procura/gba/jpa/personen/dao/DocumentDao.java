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

import java.util.List;

import javax.persistence.EntityManager;

import nl.procura.gba.jpa.personen.db.*;
import nl.procura.gba.jpa.personen.utils.GbaJpa;

public class DocumentDao extends GenericDao {

  public static List<Document> findUsrDocuments(long cUsr) {
    String query = "select d from Document d " +
        "left join d.usrs u where (u.cUsr = " + cUsr + ") " +
        "and d.cDocument > 0";

    EntityManager em = GbaJpa.getManager();
    return em.createQuery(query, Document.class).getResultList();
  }

  public static List<Document> findGeneralDocuments() {
    String query = "select d from Document d " +
        "where d.allAllowed > 0 " +
        "and d.cDocument > 0";

    EntityManager em = GbaJpa.getManager();
    return em.createQuery(query, Document.class).getResultList();
  }

  public static List<Document> findAllDocuments() {
    StringBuilder query = new StringBuilder();
    query.append("select d from Document d ");
    query.append("where d.cDocument > 0");
    return GbaJpa.getManager().createQuery(query.toString(), Document.class).getResultList();
  }

  public static List<DocumentAfn> findAfnemers() {
    return GbaJpa.getManager().createQuery("select d from DocumentAfn d where d.cDocumentAfn > 0",
        DocumentAfn.class).getResultList();
  }

  public static List<DocumentDmsType> findDmsTypes() {
    return GbaJpa.getManager().createQuery("select d from DocumentDmsType d where d.cDocumentDmsType > 0",
        DocumentDmsType.class).getResultList();
  }

  public static List<Stempel> findStempels() {
    return GbaJpa.getManager()
        .createQuery("select d from Stempel d where d.cStempel > 0 order by d.zIndex asc, d.cStempel asc",
            Stempel.class)
        .getResultList();
  }

  public static List<Kenmerk> findKenmerken() {
    return GbaJpa.getManager().createQuery("select d from Kenmerk d where d.cKenmerk > 0",
        Kenmerk.class).getResultList();
  }

  public static List<DocumentDoel> findDoelen() {
    return GbaJpa.getManager().createQuery("select d from DocumentDoel d where d.cDocumentDoel > 0",
        DocumentDoel.class).getResultList();
  }

  public static List<Document> findDocuments(String sql) {
    return GbaJpa.getManager().createQuery(sql, Document.class).getResultList();
  }
}
