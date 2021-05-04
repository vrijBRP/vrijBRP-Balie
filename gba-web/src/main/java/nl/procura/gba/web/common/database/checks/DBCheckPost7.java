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

package nl.procura.gba.web.common.database.checks;

import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.toBigDecimal;

import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.*;

import org.apache.commons.lang3.math.NumberUtils;
import org.reflections.Reflections;

import nl.procura.gba.jpa.personen.db.Serial;
import nl.procura.gba.web.common.database.DBCheckTemplateLb;

import liquibase.database.Database;

public class DBCheckPost7 extends DBCheckTemplateLb {

  public DBCheckPost7(EntityManager entityManager, Database database, String type) {
    super(entityManager, database, type, "Serialtabel bijwerken");
  }

  @Override
  public void init() {
    for (Entry<String, String> entry : getAllTableGeneratorClasses().entrySet()) {
      long newMax = 0;
      for (Object max : getEntityManager().createNativeQuery(
          "select max (" + entry.getValue() + ") from " + entry.getKey()).getResultList()) {
        newMax = NumberUtils.toLong(astr(max), 0);
      }

      Serial serial = new Serial();
      serial.setId(entry.getKey());
      serial.setVal(toBigDecimal(newMax));
      getEntityManager().merge(serial);
      count(1);
    }
  }

  /**
   * Returns all classes with @Id and @TableGenerator
   */
  private Map<String, String> getAllTableGeneratorClasses() {
    Reflections reflections = new Reflections("nl.procura.gba.jpa.personen.db");
    Set<Class<?>> allClasses = reflections.getTypesAnnotatedWith(Entity.class);

    Map<String, String> map = new LinkedHashMap<>();
    for (Class<?> cl : allClasses.stream().sorted(Comparator.comparing(Class::getSimpleName))
        .collect(Collectors.toList())) {
      String tableName = cl.getAnnotation(Table.class).name();
      for (Field field : cl.getDeclaredFields()) {
        if (field.isAnnotationPresent(Id.class) && field.isAnnotationPresent(TableGenerator.class)) {
          map.put(tableName, field.getAnnotation(Column.class).name());
          break;
        }
      }
    }

    return map;
  }
}
