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

package nl.procura.diensten.gba.ple.procura.templates;

import static nl.procura.standard.Globalfunctions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;

import nl.procura.diensten.gba.ple.base.BasePLUtils;
import nl.procura.diensten.gba.ple.base.PLEMessage;
import nl.procura.diensten.gba.ple.procura.arguments.PLNumber;
import nl.procura.gba.jpa.probev.db.Inw;
import nl.procura.gba.jpa.probev.db.Verw;
import nl.procura.gba.jpa.probev.db.Xinw;

public class PersonsTemplate extends PLETemplateProcura<Object> {

  public void parse() {
    search(new ArrayList<>(getArguments().getNumbers()));
    if (getArguments().isSearchRelations()) {
      Set<PLNumber> relatieNummers = BasePLUtils.getRelations(getBuilder());
      getArguments().getNumbers().addAll(relatieNummers);
      search(new ArrayList<>(relatieNummers));
    }
    BasePLUtils.removeDuplicates(getBuilder());
  }

  private void search(List<PLNumber> totalList) {
    if (totalList.size() > 0) {
      for (List<PLNumber> subList : Lists.partition(totalList, 500)) {
        int currentCount = getBuilder().getResult().getBasePLs().size();
        int part_count = subList.size();
        int maxCount = getArguments().getMaxFindCount();
        int missing_count = (maxCount - currentCount);

        searchTable(subList, Inw.class, BRON_INW);
        searchTable(subList, Verw.class, BRON_VERW);
        searchTable(subList, Xinw.class, BRON_XINW);

        //addAnummerWijziging(subList);

        boolean max = (missing_count <= part_count);
        boolean moreThanMax = (totalList.size() > maxCount);

        List<PLNumber> tl = new ArrayList<>(max ? subList.subList(0, missing_count) : subList);

        PersonTemplate tPersoon = new PersonTemplate();
        tPersoon.init(this);
        tPersoon.setNumbers(tl);
        tPersoon.parse();

        if (moreThanMax) {
          getBuilder().getResult()
              .getMessages()
              .add(new PLEMessage(201, String.format(
                  "Maximaal aantal zoekresultaten bereikt (gevonden = %s, max. = %s).",
                  (totalList.size() > 300) ? "Meer dan 300" : totalList.size(), maxCount)));

          return;
        }
      }
    }
  }

  private void searchTable(List<PLNumber> nummers, Class<?> tableClass, int type) {
    int i = 0;
    String tableName = tableClass.getSimpleName().toLowerCase();
    String selectf = "a1,a2,a3,snr";

    StringBuilder where = new StringBuilder();
    where.append(" where ind_o not in ('O','S','X','Y') ");
    where.append(" and hist in ('<','A','B','E','M','O') ");
    where.append(!getArguments().isShowSuspended() ? " and hist not in ('E','M','O')" : "");
    where.append(!getArguments().isShowMutations() ? " and hist not in ('<')" : "");
    where.append(" and (");

    for (PLNumber nummer : nummers) {
      if (nummer.getSource() != BRON_ONBEKEND) {
        continue;
      }

      if (pos(nummer.getBsn())) {
        if (i > 0) {
          where.append(" or ");
        }
        where.append("(snr = ")
            .append(nummer.getBsn())
            .append(")");
      } else if (pos(nummer.getA1())) {
        if (i > 0) {
          where.append(" or ");
        }
        where.append("(a1 = ")
            .append(nummer.getA1())
            .append(" and a2 = ")
            .append(nummer.getA2())
            .append(" and a3 = ")
            .append(nummer.getA3())
            .append(")");
      }
      i++;
    }
    where.append(")");

    if (i > 0) {
      String sql = String.format("select %s from %s%s order by hist asc", selectf, tableName, where);
      List<?> resultList = getEntityManager().createNativeQuery(sql).getResultList();
      appendPLNumber(nummers, type, resultList);
    }
  }

  private void appendPLNumber(List<PLNumber> numbers, int type, List<?> resultList) {
    for (Object row : resultList) {
      Object[] rowArray = (Object[]) row;
      int a1 = aval(rowArray[0]);
      int a2 = aval(rowArray[1]);
      int a3 = aval(rowArray[2]);
      long bsn = along(rowArray[3]);

      PLNumber mnr = new PLNumber(a1, a2, a3, bsn, type);
      int index = numbers.indexOf(mnr);

      if (index >= 0) {
        PLNumber nr = numbers.get(index);

        if (nr != null) {
          if (nr.getA1() <= 0) {
            nr.setA1(a1);
            nr.setA2(a2);
            nr.setA3(a3);
          }

          if (nr.getBsn() <= 0) {
            nr.setBsn(bsn);
          }

          if (nr.getSource() == BRON_ONBEKEND) {
            nr.setSource(type);
          }
        }
      }
    }
  }

  //  DISABLED FOR NOW. MAY BE USED IN THE FUTURE
  //
  //  private void addAnummerWijziging(List<PLNumber> numbers) {
  //    for (PLNumber number : numbers) {
  //      if (pos(number.getA1()) && number.getSource() == BRON_ONBEKEND) {
  //        TypedQuery<AnrWijz> query = getEntityManager()
  //            .createQuery("select a from AnrWijz a where a.anrOud = :anr_oud", AnrWijz.class)
  //            .setParameter("anr_oud", astr(number.getNummer()));
  //
  //        for (AnrWijz anrWijz : query.getResultList()) {
  //          Anr anrN = new Anr(anrWijz.getId().getAnrN());
  //          number.setA1(aval(anrN.getA1()));
  //          number.setA2(aval(anrN.getA2()));
  //          number.setA3(aval(anrN.getA3()));
  //        }
  //      }
  //    }
  //    searchTable(numbers, Inw.class, BRON_INW);
  //    searchTable(numbers, Verw.class, BRON_VERW);
  //    searchTable(numbers, Xinw.class, BRON_XINW);
  //  }
}
