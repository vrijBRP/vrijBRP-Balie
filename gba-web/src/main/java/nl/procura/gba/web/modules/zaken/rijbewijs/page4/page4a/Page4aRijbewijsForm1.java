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

package nl.procura.gba.web.modules.zaken.rijbewijs.page4.page4a;

import static nl.procura.gba.web.modules.zaken.rijbewijs.page4.page4a.Page4aRijbewijsBean1.*;
import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.date2str;

import com.vaadin.ui.Field;

import nl.procura.gba.web.modules.zaken.rijbewijs.RdwReadOnlyForm;
import nl.procura.rdw.processen.p0252.f08.CATPERSGEG;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class Page4aRijbewijsForm1 extends RdwReadOnlyForm {

  Page4aRijbewijsForm1(CATPERSGEG c) {

    setColumnWidths("160px", "", "130px", "", "130px", "");

    setOrder(CATEGORIE, AUTOMAAT, EERSTEAFGIFTE, MEDVERKLARING, OMGEWISSELD, LAND, VOERTUIG, BEPERKING, GESCHIKTHEID);

    Page4aRijbewijsBean1 b = new Page4aRijbewijsBean1();

    String eersteAfgifte = date2str(c.getEerstafgdatc().toString());

    if ("E".equals(c.getSrteafgdatc())) {
      eersteAfgifte += " E (Exact)";
    }

    if ("S".equals(c.getSrteafgdatc())) {
      eersteAfgifte += " S (Schatting)";
    }

    String geschiktheid = "N";
    if ("J".equals(c.getGverklind())) {
      geschiktheid = String.format("Ja, gezet op %s door %s (%s)",
          date2str(astr(c.getRegdatgind())), c.getNaamgemgind(), c.getAutorcgind());
    }

    b.setCategorie(c.getRybcatp());
    b.setAutomaat(c.getAutomcatind());
    b.setEersteAfgifte(eersteAfgifte);
    b.setMedVerklaring(c.getMedverklindc());
    b.setOmgewisseld(c.getOmgewrybnr());
    b.setLand(trimInteger(c.getLandcodeomwc()));
    b.setVoertuig(c.getLandcvtomwc());
    b.setBeperking(c.getBepvermcat());
    b.setGeschiktheid(geschiktheid);

    setBean(b);
  }

  @Override
  public void setColumn(Column column, Field field, Property property) {

    if (property.is(BEPERKING)) {
      column.setColspan(3);
    }

    if (property.is(GESCHIKTHEID)) {
      column.setColspan(5);
    }

    super.setColumn(column, field, property);
  }
}
