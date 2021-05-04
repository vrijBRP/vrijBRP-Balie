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

import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.values;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.apache.commons.lang3.StringUtils;

import nl.procura.gba.web.common.database.DBCheckTemplateLb;
import nl.procura.gba.web.modules.beheer.parameters.bean.ParameterBean;
import nl.procura.gba.web.services.beheer.parameter.annotations.ParameterAnnotation;

import liquibase.database.Database;

public class DBCheckPost8 extends DBCheckTemplateLb {

  public DBCheckPost8(EntityManager entityManager, Database database, String type) {
    super(entityManager, database, type, "Parametertabel opschonen");
  }

  private static String deleteNotUserParameters() {
    return String.format("delete from parm where c_usr > 0 and parm in (%s)", join(getNotUserParameters()));
  }

  private static String deleteNotProfileParameters() {
    return String.format("delete from parm where c_profile > 0 and parm in (%s)", join(getNotProfileParameters()));
  }

  private static String deleteUnusedParameters() {
    return String.format("delete from parm where parm not in (%s)", join(getCurrentParameters()));
  }

  private static List<String> getNotUserParameters() {
    List<String> out = new ArrayList<>();
    for (Field pf : ParameterBean.class.getDeclaredFields()) {
      ParameterAnnotation ann = pf.getAnnotation(ParameterAnnotation.class);
      if (ann != null && !ann.value().isShowUser()) {
        out.add(String.format("'%s'", ann.value().getKey()));
      }
    }
    return out;
  }

  private static List<String> getNotProfileParameters() {
    List<String> out = new ArrayList<>();
    for (Field pf : ParameterBean.class.getDeclaredFields()) {
      ParameterAnnotation ann = pf.getAnnotation(ParameterAnnotation.class);
      if (ann != null && !ann.value().isShowProfile()) {
        out.add(String.format("'%s'", ann.value().getKey()));
      }
    }
    return out;
  }

  private static List<String> getCurrentParameters() {
    return Arrays.stream(values())
        .map(value -> String.format("'%s'", value.getKey()))
        .collect(Collectors.toList());
  }

  private static String join(List<String> parameters) {
    return StringUtils.join(parameters, ',');
  }

  @Override
  public void init() {
    count(nativeUpdate(deleteUnusedParameters()));
    count(nativeUpdate(deleteNotUserParameters()));
    count(nativeUpdate(deleteNotProfileParameters()));
  }
}
