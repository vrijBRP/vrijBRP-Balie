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

package nl.procura.gba.web.services.zaken.voorraad;

import static nl.procura.standard.Globalfunctions.along;
import static nl.procura.standard.Globalfunctions.astr;

import java.math.BigDecimal;
import java.util.List;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import nl.procura.diensten.gba.ple.procura.utils.jpa.PLEJpa;
import nl.procura.diensten.gba.ple.procura.utils.jpa.PLEJpaEclipseLink;
import nl.procura.diensten.gba.ple.procura.utils.jpa.PLEJpaManager;
import nl.procura.gba.jpa.personen.db.Voorraad;
import nl.procura.gba.jpa.personen.utils.GbaEclipseLinkUtil;
import nl.procura.validation.Anummer;
import nl.procura.validation.Bsn;

/**
 * Haalt BSN / A-nummer uit PROBEV DB
 */
public class VulVoorraadTest {

  public VulVoorraadTest() {

    EntityManager jpaEm = getGbaWebEntityManager();

    try {

      jpaEm.getTransaction().begin();

      try (PLEJpaManager pleEm = getPleEntityManager()) {

        Query q = pleEm.createQuery("select i.snr, i.id.a1, i.id.a2, i.id.a3 from Inw i");

        List list = q.getResultList();

        int i = 0;

        int currentInt = 0;

        for (Object o : list) {

          i++;

          double perc = (100.00 / list.size()) * i;

          if (currentInt != ((int) perc)) {

            System.out.println("PERC: " + (int) perc);

            currentInt = (int) perc;

            if (((int) perc) % 5 == 0) {

              System.out.println("committing");

              jpaEm.getTransaction().commit();

              jpaEm.getTransaction().begin();
            }
          }

          Object[] s = (Object[]) o;

          Bsn bsn = new Bsn(astr(s[0]));

          Anummer anr = new Anummer(along(s[1]), along(s[2]), along(s[3]));

          if (bsn.isCorrect()) {

            addNummer(jpaEm, bsn.getDefaultBsn(), 1);
          }

          if (anr.isCorrect()) {

            addNummer(jpaEm, astr(anr.getLongAnummer()), 2);
          }
        }
      }

      jpaEm.getTransaction().commit();
    } finally {
      jpaEm.close();
    }
  }

  public static void main(String[] args) {

    new VulVoorraadTest();
  }

  public PLEJpaManager getPleEntityManager() {

    PLEJpa jpa = new PLEJpa();

    PLEJpaEclipseLink jpaImpl = new PLEJpaEclipseLink();

    jpaImpl.setDatabase("postgres");
    jpaImpl.setServer("dbp");
    jpaImpl.setPort(5432);
    jpaImpl.setSid("bev0392");
    jpaImpl.setUsername("postgres");
    jpaImpl.setPassword("postgres");

    jpa.setJpaImplementation(jpaImpl);
    jpa.connect("probev-jpa");

    return jpa.createManager();
  }

  private void addNummer(EntityManager jpaEm, String defaultBsn, long type) {

    Voorraad v = new Voorraad();
    v.setNummer(along(defaultBsn));
    v.setStatus(BigDecimal.valueOf(0));
    v.setType(BigDecimal.valueOf(type));

    jpaEm.merge(v);
  }

  private EntityManager getGbaWebEntityManager() {

    Properties p = new Properties();

    p.put("app_db_name", "postgres");
    p.put("app_db_server", "dbp");
    p.put("app_db_port", "5432");
    p.put("app_db_sid", "zoekpersoon_new_frits");
    p.put("app_db_username", "postgres");
    p.put("app_db_password", "postgres");

    EntityManager em = GbaEclipseLinkUtil.createEntityManager("personen-jpa", p);

    return em;
  }
}
