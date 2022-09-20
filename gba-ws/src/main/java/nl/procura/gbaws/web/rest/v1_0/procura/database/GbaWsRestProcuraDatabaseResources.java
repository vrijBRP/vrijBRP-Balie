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

package nl.procura.gbaws.web.rest.v1_0.procura.database;

import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.fil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.io.IOUtils;

import com.google.inject.servlet.RequestScoped;

import nl.procura.diensten.gba.ple.procura.utils.jpa.PLEJpaManager;
import nl.procura.gbaws.db.jpa.PleJpaUtils;
import nl.procura.gbaws.web.rest.GbaWsRestDienstenbusResource;
import nl.procura.gbaws.web.rest.v1_0.database.procura.query.GbaWsRestProcuraSelecterenAntwoord;
import nl.procura.gbaws.web.rest.v1_0.database.procura.query.GbaWsRestProcuraSelecterenVraag;
import nl.procura.gbaws.web.rest.v1_0.database.procura.query.GbaWsRestQueryRecord;
import nl.procura.gbaws.web.rest.v1_0.database.procura.query.GbaWsRestQueryVeld;
import nl.procura.proweb.rest.guice.annotations.AuthenticatieVereist;
import nl.procura.proweb.rest.v1_0.Rol;

@RequestScoped
@Path("v1.0/procura/database")
@AuthenticatieVereist(rollen = { Rol.BEHEERDER })
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public class GbaWsRestProcuraDatabaseResources extends GbaWsRestDienstenbusResource {

  @POST
  @Path("/selecteren")
  public GbaWsRestProcuraSelecterenAntwoord getTabel(GbaWsRestProcuraSelecterenVraag vraag) {

    GbaWsRestProcuraSelecterenAntwoord antwoord = new GbaWsRestProcuraSelecterenAntwoord();
    final PLEJpaManager em = PleJpaUtils.createManager();

    try {
      List<?> list = em.createQuery(trimSql(vraag.getQuery())).getResultList();
      for (Object obj : list) {
        GbaWsRestQueryRecord record = new GbaWsRestQueryRecord();
        List<GbaWsRestQueryVeld> velden = new ArrayList<>();

        if (obj instanceof Object[]) {
          Object[] oArray = (Object[]) obj;
          int index = 0;
          for (Object object : oArray) {
            velden.add(new GbaWsRestQueryVeld(astr(index), astr(object)));
            index++;
          }
        } else if ((obj instanceof String) || (obj instanceof Number)) {
          velden.add(new GbaWsRestQueryVeld("0", astr(obj)));

        } else {
          getMethods(obj, velden);
        }

        record.setVelden(velden);
        antwoord.getRecords().add(record);
      }
    } finally {
      IOUtils.closeQuietly(em);
    }

    return antwoord;
  }

  private String trimSql(String sql) {
    while (true) {
      int start = sql.indexOf("_");
      if (start < 0) {
        return sql;
      }

      int next = (start + 2) < sql.length() ? 2 : 1;
      sql = (sql.substring(0, start)
          + sql.substring(start + 1, start + next).toUpperCase()
          + sql.substring(start + next));
    }
  }

  private void getMethods(Object o, List<GbaWsRestQueryVeld> velden) {
    for (Field f : o.getClass().getDeclaredFields()) {
      if (f.getType() == Collection.class) {
        continue;
      }

      String methodName = normalize1(f, normalize2(f.getName()));
      if (f.getAnnotations().length > 0) {
        String name = getDatabaseFieldName(f);

        for (Method m : o.getClass().getMethods()) {
          if (m.getName().equals(methodName)) {
            Object returnvalue = invoke(methodName, o);
            if (returnvalue.getClass().getName().startsWith("java")) {
              velden.add(new GbaWsRestQueryVeld(name, astr(returnvalue)));
            } else {
              getMethods(returnvalue, velden);
            }

            break;
          }
        }
      }
    }
  }

  /**
   * Geef veld naam zoals deze in de dabase staat
   */
  private String getDatabaseFieldName(Field field) {
    String name = field.getName();
    for (Annotation a : field.getAnnotations()) {
      if (a instanceof Column) {
        Column c = ((Column) a);
        if (fil(c.name())) {
          name = c.name();
        }
      }
    }

    return name;
  }

  private String normalize1(Field field, String name) {
    return (field.getType() == boolean.class) ? "is" : "get" + name;
  }

  private String normalize2(String name) {
    return name.substring(0, 1).toUpperCase() + name.substring(1);
  }

  private Object invoke(String methodName, Object o) {
    try {
      Method subMethod = o.getClass().getMethod(methodName);
      return subMethod.invoke(o);
    } catch (Exception e) {
      System.err.println("ERROR: " + e.getMessage());
    }

    return "";
  }
}
