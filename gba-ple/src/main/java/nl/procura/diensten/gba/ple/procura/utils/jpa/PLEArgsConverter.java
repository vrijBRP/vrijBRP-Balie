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

package nl.procura.diensten.gba.ple.procura.utils.jpa;

import static nl.procura.standard.Globalfunctions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import javax.persistence.TypedQuery;

import nl.procura.diensten.gba.ple.procura.arguments.PLEArgs;
import nl.procura.diensten.gba.ple.procura.arguments.PLECatArgs;
import nl.procura.diensten.gba.ple.procura.arguments.PLESearchArgs;
import nl.procura.diensten.gba.ple.procura.arguments.PLNumber;
import nl.procura.standard.diacrieten.ProcuraDiacrieten;

public class PLEArgsConverter {

  public static void convert(PLEJpaManager em, PLEArgs args) {

    if (args.isNawGevuld()) {
      List<PLNumber> numbers = new ArrayList<>(args.getNumbers());
      args.getNumbers().clear();
      zoekInw(em, args, numbers, "Inw", PLNumber.TABEL_BRON_INW);
      if (args.isShowSuspended()) {
        zoekInw(em, args, numbers, "Verw", PLNumber.TABEL_BRON_VERW);
      }
    }
  }

  public static PLEArgs convert(PLESearchArgs z, PLECatArgs c) {

    PLEArgs args = new PLEArgs();

    args.addNummer(z.getNummers().toArray(new String[z.getNummers().size()]));
    args.setGeslachtsnaam(z.getGeslachtsnaam());
    args.setVoornaam(z.getVoornaam());
    args.setGeslacht(z.getGeslacht());
    args.setGeboortedatum(z.getGeboortedatum());
    args.setTitel(z.getTitel());
    args.setStraat(z.getStraat());
    args.setHuisnummer(z.getHnr());
    args.setHuisletter(z.getHnrL());
    args.setHuisnummertoevoeging(z.getHnrT());
    args.setAanduiding(z.getHnrA());
    args.setPostcode(z.getPostcode());
    args.setGemeentedeel(z.getGemDeel());
    args.setGemeente(z.getGemeente());
    args.setShowSuspended(c.isShowSuspended());

    args.setShowArchives(c.isShowArchives());
    args.setShowHistory(c.isShowHistory());
    args.setSearchOnAddress(c.isSearchOnAddress());
    args.setDatasource(c.getDatasource());
    args.setMaxFindCount(c.getMaxFindCount());
    args.setCategories(c.getCategories());
    args.setCustomTemplate(c.getTemplate());

    return args;
  }

  private static void zoekInw(PLEJpaManager entityManager, PLEArgs args, List<PLNumber> numbers, String tabel,
      int bron) {

    Properties props = new Properties();
    StringBuilder sql = new StringBuilder();

    sql.append("select x.id.a1,x.id.a2,x.id.a3, x.snr " + getFrom(args) + getWhere(args));

    List<Long> bsns = new ArrayList<>();
    for (PLNumber nummer : numbers) {
      if (nummer.getBsn() > 0) {
        bsns.add(nummer.getBsn());
      }
    }

    if (bsns.size() > 0) {
      addSQL(sql, "(x.snr in :snrs)");
      addProp(props, "snrs", bsns);
    }

    if (fil(args.getGeslachtsnaam())) {
      addSQL(sql, "(x.naam.naam like :naam or x.naam.naam like :naam2)");
      addProp(props, "naam", getDt(fc(args.getGeslachtsnaam())));
      addProp(props, "naam2", getDt(args.getGeslachtsnaam()));
    }

    if (fil(args.getVoornaam())) {
      addSQL(sql, "(x.voorn.voorn like :voorn or x.voorn.voorn like :voorn2)");
      String v = args.getVoornaam().contains("%") ? args.getVoornaam() : args.getVoornaam() + "%";
      addProp(props, "voorn", getDt(fc(v)));
      addProp(props, "voorn2", getDt(v));
    }

    if (fil(args.getVoorvoegsel())) {
      addSQL(sql, "x.voorv.voorv like :voorv");
      addProp(props, "voorv", getDt(args.getVoorvoegsel()));
    }

    if (fil(args.getTitel())) {
      addSQL(sql, "x.tp.tp = :tp");
      addProp(props, "tp", args.getTitel());
    }

    if (fil(args.getGeboortedatum())) {
      addSQL(sql, "x.dGeb = :d_geb");
      addProp(props, "d_geb", aval(args.getGeboortedatum()));
    }

    if (fil(args.getGeslacht()) && (bron == PLNumber.TABEL_BRON_INW)) {
      addSQL(sql, "x.geslacht = :gesl");
      addProp(props, "gesl", args.getGeslacht());
    }

    if (fil(args.getStraat())) {
      addSQL(sql, "vb1.straat.straat like :str");
      addProp(props, "str", getDt(args.getStraat()));
    }

    if (fil(args.getHuisnummer())) {
      addSQL(sql, "vb1.hnr = :hnr");
      addProp(props, "hnr", aval(args.getHuisnummer()));
    }

    String hnrL = args.getHuisletter();
    String hnrT = args.getHuisnummertoevoeging();
    String hnrA = args.getAanduiding();
    String pc = args.getPostcode().replaceAll("\\s+", "");

    if (fil(hnrL)) {
      addSQL(sql, "(vb1.hnrL = :hnrl1 or vb1.hnrL = :hnrl2)");
      addProp(props, "hnrl1", hnrL.toLowerCase());
      addProp(props, "hnrl2", hnrL.toUpperCase());
    }

    if (fil(hnrT)) {
      addSQL(sql, "(vb1.hnrT = :hnrt1 or vb1.hnrT = :hnrt2)");
      addProp(props, "hnrt1", hnrT.toLowerCase());
      addProp(props, "hnrt2", hnrT.toUpperCase());
    }

    if (fil(hnrA)) {
      addSQL(sql, "(vb1.hnrA = :hnra1 or vb1.hnrA = :hnra2)");
      addProp(props, "hnra1", hnrA.toLowerCase());
      addProp(props, "hnra2", hnrA.toUpperCase());
    }

    if (fil(pc)) {
      addSQL(sql, "(vb1.pc = :pc1 or vb1.pc = :pc2)");
      addProp(props, "pc1", pc.toUpperCase());
      addProp(props, "pc2", pc.toLowerCase());
    }

    if (fil(args.getGemeentedeel())) {
      addSQL(sql, "vb1.gemDeel.gemDeel like :gemdeel");
      addProp(props, "gemdeel", args.getGemeentedeel());
    }

    if (fil(args.getGemeente())) {
      addSQL(sql, "vb1.plaat.plaats like :gemeente");
      addProp(props, "gemeente", args.getGemeente());
    }

    if (props.isEmpty()) {
      return;
    }

    sql = new StringBuilder(sql.toString().replaceAll("--TAB", tabel));
    TypedQuery<Object> q = entityManager.createQuery(sql.toString(), Object.class);
    q.setMaxResults(305);

    for (Entry<Object, Object> entry : props.entrySet()) {
      q.setParameter((String) entry.getKey(), entry.getValue());
    }

    List<Object> list = q.getResultList();

    for (Object obj : list) {
      if (obj instanceof Object[]) {
        Object[] objs = (Object[]) obj;
        if (objs.length == 4) {
          int a1 = aval(objs[0]);
          int a2 = aval(objs[1]);
          int a3 = aval(objs[2]);
          long bsn = along(objs[3]);
          add(args.getNumbers(), new PLNumber(a1, a2, a3, bsn, bron));
        }
      }
    }
  }

  private static void add(Set<PLNumber> set, PLNumber... newNummers) {
    for (PLNumber newNummer : newNummers) {
      for (PLNumber nr : set) {
        if (nr.equals(newNummer)) {
          return;
        }
      }

      set.add(newNummer);
    }
  }

  private static String getDt(String s) {
    return ProcuraDiacrieten.parseUT8String(s).getNormalString();
  }

  private static String getFrom(PLEArgs args) {
    StringBuilder sb = new StringBuilder();
    sb.append("from --TAB x");
    if (fil(args.getStraat())
        || fil(args.getHuisnummer())
        || fil(args.getHuisletter())
        || fil(args.getHuisnummertoevoeging())
        || fil(args.getAanduiding())
        || fil(args.getPostcode())
        || fil(args.getGemeentedeel())
        || fil(args.getGemeente())) {
      sb.append(", Vb vb1");
    }

    return sb.toString();
  }

  private static String getWhere(PLEArgs args) {
    StringBuilder sb = new StringBuilder();

    if (args.isShowSuspended()) {
      sb.append(" where x.hist in ('<','A','B','E','M','O')");
    } else {
      sb.append(" where x.hist in ('<','A')");
    }

    if (fil(args.getStraat()) || fil(args.getHuisnummer()) || fil(args.getPostcode()) || fil(
        args.getGemeentedeel()) || fil(args.getGemeente())) {
      sb.append(" and vb1.id.a1 = x.id.a1 and " +
          "vb1.id.a2 = x.id.a2 and " +
          "vb1.id.a3 = x.id.a3 and " +
          "vb1.hist in ('<','A','B','E','M','O')");
    }

    return sb.toString();
  }

  private static String fc(String s) {
    if (fil(s)) {
      return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
    return s;
  }

  private static void addSQL(StringBuilder sql, String s) {
    sql.append(" and ");
    sql.append(s);
  }

  private static void addProp(Properties props, String k, Object s) {
    props.put(k, s);
  }

}
