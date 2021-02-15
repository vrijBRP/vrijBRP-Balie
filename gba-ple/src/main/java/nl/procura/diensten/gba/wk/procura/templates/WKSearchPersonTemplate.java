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
import static nl.procura.standard.Globalfunctions.pos;

import java.util.Iterator;

import javax.persistence.Query;

public class WKSearchPersonTemplate extends WKTemplateProcura {

  private int    a1  = 0;
  private int    a2  = 0;
  private int    a3  = 0;
  private String bsn = "";

  @Override
  public void parse() {

    zoek("Inw", "Verw", "Xinw");
  }

  private void zoek(String... tables) {

    boolean found = false;

    for (String table : tables) {
      if (found) {
        break;
      }

      setSql("select i.snr from -TABLE i where i.id.a1 = :a1");
      addSQL("i.id.a2 = :a2");
      addSQL("i.id.a3 = :a3");
      addSQL("i.hist in ('A','B','E','M','O')");

      addProp("a1", getA1());
      addProp("a2", getA2());
      addProp("a3", getA3());

      setSql(getSql().replaceAll("-TABLE", table));
      setSql(getSql().replaceAll("_TABLE", table.toLowerCase()));

      Query q = getEntityManager().createQuery(getSql());
      Iterator<?> it = getProps().keySet().iterator();

      while (it.hasNext()) {
        String key = (String) it.next();
        q.setParameter(key, getProps().get(key));
      }

      Object object;
      try {
        object = q.getSingleResult();

        if (pos(object)) {
          setBsn(astr(object));
          found = true;
        }
      } catch (Exception e) {
        // Hoeft niet te worden afgevangen
      }
    }
  }

  public String getBsn() {
    return bsn;
  }

  public void setBsn(String bsn) {
    this.bsn = bsn;
  }

  public int getA1() {
    return a1;
  }

  public void setA1(int a1) {
    this.a1 = a1;
  }

  public int getA2() {
    return a2;
  }

  public void setA2(int a2) {
    this.a2 = a2;
  }

  public int getA3() {
    return a3;
  }

  public void setA3(int a3) {
    this.a3 = a3;
  }
}
