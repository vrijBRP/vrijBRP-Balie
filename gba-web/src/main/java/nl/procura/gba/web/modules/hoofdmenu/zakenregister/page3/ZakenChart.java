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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.page3;

import static ch.lambdaj.Lambda.*;
import static nl.procura.standard.Globalfunctions.*;

import java.util.List;

import com.google.common.collect.Multiset;
import com.google.common.collect.TreeMultiset;

import nl.procura.gba.jpa.personen.dao.ZaakKey;
import nl.procura.gba.web.common.misc.charts.Chart;
import nl.procura.gba.web.services.zaken.algemeen.ZaakSorterBuilder;
import nl.procura.gba.web.services.zaken.algemeen.ZaakSortering;
import nl.procura.standard.ProcuraDate;

import ch.lambdaj.group.Group;

public class ZakenChart extends Chart {

  public ZakenChart(List<ZaakKey> zaakKeys) {
    super("bar");
    setWidth("100%");
    setHeight("600px");

    add("var chart = new Highcharts.Chart({");
    add("chart: {renderTo: 'container', type: 'spline', zoomType: 'x'},");
    add("title: {text: 'Zaken'},");
    add("xAxis: {type: 'datetime', dateTimeLabelFormats: {month: '%e. %b',year: '%b'}},");
    add("yAxis: {min: 0, title: {text: 'Totaal aantal zaken'}},");
    add("legend: {backgroundColor: '#FFFFFF',reversed: true, layout: 'vertical', align: 'right', y: 0, x: 0},");
    add("tooltip: {");
    add("formatter: function() {");
    add("return ''+");
    add("this.series.name +': '+ this.y +'';");
    add("}");
    add("},");
    add("plotOptions: {spline: {linewidth: 3, marker: {enabled: false}}},");
    add("series: [");

    zaakKeys.sort(ZaakSorterBuilder.getZaakKeySorter(ZaakSortering.DATUM_INVOER_NIEUW_OUD));

    long first = zaakKeys.get(0).getdInv().longValue();
    long last = zaakKeys.get(zaakKeys.size() - 1).getdInv().longValue();

    long min = first;
    long max = last;

    // Afhankelijk van de sortering omdraaien
    if (min > max) {
      min = last;
      max = first;
    }

    Group<ZaakKey> group = getGroups(zaakKeys);

    for (String key : group.keySet()) {

      add("{name: '" + key + "',");
      add("data: [");

      int dayCount = new ProcuraDate(astr(min)).diffInDays(astr(max));
      for (int i = 0; i <= dayCount; i++) {
        long curDate = along(new ProcuraDate(astr(min)).addDays(i).getSystemDate());
        addData(curDate, getCountByDate(group, key, curDate));
      }

      add("]},");
    }

    add("]");
    add("});");
  }

  private void addData(long dIn, long count) {

    // 1 maand eraf vanwege javascript bug
    // https://github.com/highslide-software/highcharts.com/issues/565

    ProcuraDate dd = new ProcuraDate(astr(dIn));
    String d = dd.getYear() + "," + (aval(dd.getMonth()) - 1) + "," + dd.getDay();
    add("[Date.UTC(" + d + "), " + count + "],");
  }

  private Multiset<Long> getCount(List<ZaakKey> zaakKeys) {

    Multiset<Long> multiset = TreeMultiset.create();

    for (ZaakKey zaak : zaakKeys) {
      multiset.add(zaak.getdInv().longValue());
    }

    return multiset;
  }

  private int getCountByDate(Group<ZaakKey> group, String key, long date) {
    for (com.google.common.collect.Multiset.Entry<Long> dateEntry : getCount(
        group.findGroup(key).findAll()).entrySet()) {
      if (dateEntry.getElement() == date) {
        addData(dateEntry.getElement(), dateEntry.getCount());
        return dateEntry.getCount();
      }
    }
    return 0;
  }

  private Group<ZaakKey> getGroups(List<ZaakKey> zaken) {
    return group(zaken, by(on(ZaakKey.class).getZaakType()));
  }
}
