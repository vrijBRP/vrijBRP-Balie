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

package nl.procura.diensten.gba.wk.procura.templates;

import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.aval;
import static nl.procura.standard.Globalfunctions.pad_left;

import java.util.Properties;

import nl.procura.diensten.gba.ple.procura.utils.diacrits.Diacrieten;
import nl.procura.diensten.gba.ple.procura.utils.jpa.PLEJpaManager;
import nl.procura.diensten.gba.wk.baseWK.BaseWKBuilder;
import nl.procura.standard.diacrieten.ProcuraDiacrieten;
import nl.procura.validation.Anr;
import nl.procura.validation.Bsn;

public class WKTemplateProcura {

  private PLEJpaManager entityManager = null;
  private Diacrieten    diacrieten    = null;
  private BaseWKBuilder builder       = new BaseWKBuilder();
  private String        sql           = "";
  private Properties    props         = new Properties();

  public void parse() {
  }

  public void addSQL(String s) {
    sql += " and " + s;
  }

  public void addProp(String k, Object s) {
    props.put(k, s);
  }

  public String anr(Object a1, Object a2, Object a3) {

    if (aval(astr(a1)) > 100) {
      return padl(a1, 3) + padl(a2, 4) + padl(a3, 3);
    }

    return null;
  }

  public String padl(Object waarde, int i) {

    if (aval(astr(waarde)) >= 0) {
      return pad_left(astr(waarde), "0", i);
    }

    return null;
  }

  public String getDateFormat(String val) {

    if (val.length() >= 8) {
      return val.substring(6, 8) + "-" + val.substring(4, 6) + "-" + val.substring(0, 4);
    }

    return val;
  }

  public String getPostcodeFormat(String val) {

    if (val.length() >= 6) {
      return val.substring(0, 4) + " " + val.substring(4, 6);
    }

    return val;
  }

  public String getAnrFormat(String val) {
    return new Anr(val).getFormatAnummer();
  }

  public String getFormatBsn(String val) {
    return new Bsn(val).getFormatBsn();
  }

  public String getDiacsTrimmed(String s) {
    return ProcuraDiacrieten.parseUT8String(s).getNormalString();
  }

  public BaseWKBuilder getBuilder() {
    return builder;
  }

  public void setBuilder(BaseWKBuilder builder) {
    this.builder = builder;
  }

  public String getSql() {
    return sql;
  }

  public void setSql(String sql) {
    this.sql = sql;
  }

  public Properties getProps() {
    return props;
  }

  public void setProps(Properties props) {
    this.props = props;
  }

  public PLEJpaManager getEntityManager() {
    return entityManager;
  }

  public void setEntityManager(PLEJpaManager entityManager) {
    this.entityManager = entityManager;
    this.diacrieten = new Diacrieten(entityManager);
  }

  protected String getDiacriet(Object object, String diacType) {
    return diacrieten.merge(object, diacType);
  }

  protected String getDiacriet(Object object, String diacType, String value) {
    return diacrieten.merge(object, diacType, value);
  }
}
