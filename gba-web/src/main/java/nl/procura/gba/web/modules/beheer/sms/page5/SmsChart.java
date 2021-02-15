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

package nl.procura.gba.web.modules.beheer.sms.page5;

import static java.lang.String.format;
import static nl.procura.standard.Globalfunctions.aval;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

import nl.procura.gba.web.common.misc.charts.Chart;
import nl.procura.sms.rest.domain.AccountInformationResponse;
import nl.procura.sms.rest.domain.AccountUsage;

public class SmsChart extends Chart {

  public SmsChart(AccountInformationResponse account) {
    super("bar");
    setWidth("100%");
    setHeight("100%");

    add("var chart = new Highcharts.Chart({");
    add("chart: {renderTo: 'container', type: 'line', zoomType: 'x'},");
    add("title: {text: 'SMS berichten'},");
    add("xAxis: {categories: [");

    for (AccountUsage usage : account.getAccountUsage()) {
      add(format("'%s. %s',", getMonthName(usage), usage.getYear()));
    }
    add("]},");
    add("yAxis: {min: 0, title: {text: 'Aantal SMS berichten'}},");
    add("plotOptions: {line: {dataLabels: {enabled: true},enableMouseTracking: false}},");
    add("legend: {backgroundColor: '#FFFFFF',reversed: true, layout: 'vertical', align: 'right', y: 0, x: 0},");
    add("tooltip: {");
    add("formatter: function() {");
    add("return ''+");
    add("this.series.name +': '+ this.y +'';");
    add("}");
    add("},");
    add("series: [");

    add("{name: 'SMS berichten',");
    add("data: [");
    for (AccountUsage usage : account.getAccountUsage()) {
      add(usage.getSent());
      add(",");
    }
    add("]},");
    add("]");
    add("});");
  }

  private String getMonthName(AccountUsage usage) {
    LocalDate ld = LocalDate.of(aval(usage.getYear()), Month.of(aval(usage.getMonth())), 1);
    return StringUtils.capitalize(ld.getMonth().getDisplayName(TextStyle.SHORT, Locale.getDefault()));
  }
}
