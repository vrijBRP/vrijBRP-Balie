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

package nl.procura.gba.jpa.personen.dao.views;

import static nl.procura.standard.Globalfunctions.*;

import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.common.ZaakType;
import nl.procura.gba.jpa.personen.dao.ZaakKey;

public abstract class DashboardDao {

  protected static String select(String table) {
    return "select zaak_id from " + table;
  }

  protected static String typeDoss(long... types) {
    return add("where type_doss in %s", in(types));
  }

  protected static String in(long... types) {

    StringBuilder s = new StringBuilder();
    for (long type : types) {
      s.append(astr(type));
      s.append(", ");
    }

    return add(" (%s)", trim(s.toString()));
  }

  protected static String dIn(String date, DashboardPeriode periode) {
    StringBuilder rsql = new StringBuilder();
    rsql.append(" and ");
    rsql.append(date);
    rsql.append(" >= ");
    rsql.append(periode.getdFrom());
    rsql.append(" and ");
    rsql.append(date);
    rsql.append(" <= ");
    rsql.append(periode.getdTo());
    return rsql.toString();
  }

  protected static String addBronnen(boolean digitaal, String bronnen) {
    return add(digitaal, "bron", bronnen);
  }

  protected static String addLeveranciers(boolean digitaal, String leveranciers) {
    return add(digitaal, "leverancier", leveranciers);
  }

  protected static String add(boolean digitaal, String veld, String waarden) {
    StringBuilder sb = new StringBuilder();
    if (digitaal && fil(waarden)) {
      int nr = 0;
      sb.append(add("and ("));
      for (String l : waarden.split(",")) {
        nr++;
        if (nr > 1) {
          sb.append(add("or"));
        }
        sb.append(add(veld + " like '%s'", l.trim()));
      }
      sb.append(add(")"));
    }

    return sb.toString();
  }

  protected static String add(String sql, Object... args) {
    return " " + (args.length > 0 ? String.format(sql, args) : sql);
  }

  protected static List<ZaakKey> toZaakKeys(List<String> zaakIds, ZaakType zaakType) {
    List<ZaakKey> zaakKeys = new ArrayList<>();
    for (String zaakId : zaakIds) {
      zaakKeys.add(new ZaakKey(zaakId, zaakType));
    }

    return zaakKeys;
  }

  public static class DashboardPeriode {

    private final String bronnen;
    private final String leveranciers;
    private long         dFrom = -1;
    private long         dTo   = -1;

    public DashboardPeriode(long dFrom, long dTo, String bronnen, String leveranciers) {
      super();
      this.dFrom = dFrom;
      this.dTo = dTo;
      this.bronnen = bronnen;
      this.leveranciers = leveranciers;
    }

    public long getdFrom() {
      return dFrom;
    }

    public long getdTo() {
      return dTo;
    }

    public String getBronnen() {
      return bronnen;
    }

    public String getLeveranciers() {
      return leveranciers;
    }
  }
}
