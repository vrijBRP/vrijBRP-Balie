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

package nl.procura.diensten.gba.ple.procura.utils.diacrits;

import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.aval;
import static nl.procura.standard.Globalfunctions.emp;
import static nl.procura.standard.Globalfunctions.fil;
import static nl.procura.standard.Globalfunctions.pos;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.Table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.procura.diensten.gba.ple.procura.utils.diacrits.DiacList.Diacref;
import nl.procura.diensten.gba.ple.procura.utils.jpa.PLEJpaManager;
import nl.procura.gba.jpa.probev.db.Diac;
import nl.procura.gba.jpa.probev.db.DiacPK;
import nl.procura.standard.diacrieten.ProcuraDiacrieten;

public class Diacrieten {

  private final static Logger           LOGGER         = LoggerFactory.getLogger(Diacrieten.class);
  private final HashMap<String, String> found_diac     = new HashMap<>();
  private final Properties              diacproperties = new Properties();
  private PLEJpaManager                 entityManager;

  public Diacrieten(PLEJpaManager entityManager) {
    setEntityManager(entityManager);
  }

  public Object invokeGetMethod(Object object, String method) {
    return invokeMethod(object,
        "get" + method.substring(0, 1).toUpperCase() + method.substring(1));
  }

  public Object invokeMethod(Object object, String method) {
    try {
      return object.getClass().getMethod(method, new Class[]{}).invoke(object);
    } catch (Exception e) {

      LOGGER.debug(e.toString());
      return null;
    }
  }

  public String merge(Object obj, String diacType) {
    return merge(obj, diacType, astr(invokeGetMethod(obj, obj.getClass().getSimpleName())));
  }

  public String merge(Object obj, String diacType, String value) {

    boolean ref_b = pos(invokeGetMethod(obj, "Diac"));
    Object ref_c = invokeGetMethod(obj, "C" + obj.getClass().getSimpleName());

    if (ref_c == null || !ref_b) {
      return value;
    }

    Diacref d = DiacList.getRef(diacType);

    if (d != null) {

      int i = fil(d.getDiac()) ? aval(invokeGetMethod(obj, d.getDiac()).toString()) : 1;
      int code = aval(invokeGetMethod(obj, d.getCodefield()).toString());

      return merge(d.getDescr(), i, code, value);
    }

    throw new DiacrietException("Onbekende diacriet veld: " + obj.getClass().getName());
  }

  private String merge(String veld, int has_diac, int code, String value) {

    if (pos(has_diac)) {

      String key = veld + "_" + code;

      try {

        if (found_diac.containsKey(key)) {
          return found_diac.get(key);
        }

        byte[] diacs = getDiacJPA(veld, code);
        String val = ProcuraDiacrieten.merge(value, diacs);
        found_diac.put(key, val);

        return val;
      } catch (Exception e) {
        LOGGER.debug(e.toString());
      }
    }

    return value;
  }

  public String getDiacField(Object o) {
    String diacveld = "";

    for (Annotation ann : o.getClass().getAnnotations()) {
      if (ann instanceof Table) {
        diacveld = (String) diacproperties.get((((Table) ann).name()));
      }
    }

    if (emp(diacveld)) {
      throw new DiacrietException("Geen diacrieten voor object: " + o.getClass().getName());
    }
    return diacveld;
  }

  private byte[] getDiacJPA(String veld, long c_veld) {
    return getEntityManager().getManager().find(Diac.class, new DiacPK(veld, c_veld)).getDiac().getBytes();
  }

  @SuppressWarnings("unused")
  private byte[] getDiac(String veld, int c_veld) throws SQLException, IOException {

    Connection con;
    Statement stmt = null;
    ResultSet rs = null;
    byte[] b = null;
    EntityManager em;

    try {

      em = getEntityManager().getManager();

      if (em.isOpen()) {

        try {

          em.getTransaction().begin();

          con = em.unwrap(java.sql.Connection.class);

          if (con == null) {
            LOGGER.error("Diacrieten kunnen niet worden opgehaald! Verbinding bestaat niet!");
          } else if (con.isClosed()) {
            LOGGER.error("Diacrieten kunnen niet worden opgehaald! Verbinding is afgesloten!");
          } else {

            stmt = con.createStatement();

            rs = stmt.executeQuery(
                "select diac from diac where veld = '" + veld + "' and c_veld = " + c_veld);

            while (rs.next()) {
              InputStream is = rs.getBinaryStream("diac");
              b = new byte[is.available()];
              is.read(b);

              break;
            }
          }

        } finally {

          try {
            em.getTransaction().commit();
          } catch (Exception e) {
            LOGGER.debug(astr(e));
          }
        }
      }
    } finally {

      try {

        if (rs != null) {
          rs.close();
        }
      } catch (Exception e) {
        LOGGER.debug(astr(e));
      }

      try {

        if (stmt != null) {
          stmt.close();
        }
      } catch (Exception e) {
        LOGGER.debug(astr(e));
      }
    }

    return b;
  }

  public PLEJpaManager getEntityManager() {
    return entityManager;
  }

  public void setEntityManager(PLEJpaManager entityManager) {
    this.entityManager = entityManager;
  }
}
