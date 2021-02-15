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

package nl.procura.gba.web.modules.zaken.rijbewijs.page4.page4b;

import static nl.procura.gba.web.modules.zaken.rijbewijs.page4.page4b.Page4bRijbewijsBean4.*;
import static nl.procura.standard.Globalfunctions.date2str;

import com.vaadin.ui.Field;

import nl.procura.gba.web.modules.zaken.rijbewijs.RdwReadOnlyForm;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsOngeldigCodeOV;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsUitgever;
import nl.procura.rdw.processen.p0252.f07.ONGVERKLGEG;
import nl.procura.rdw.processen.p0252.f07.UITGMAATRGEG;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

/**
 * Ongeldigverklaring gegevens
 */
public class Page4bRijbewijsForm4 extends RdwReadOnlyForm {

  Page4bRijbewijsForm4(UITGMAATRGEG c) {

    setCaption("Ongeldigverklaring gegevens");
    setOrder(INLEVERDATUM, CODEONGELDIG, AUTORITEIT1, AUTORITEIT2, AUTORITEIT3);
    setColumnWidths(WIDTH_130, "300px", "130px", "");

    Page4bRijbewijsBean4 b = new Page4bRijbewijsBean4();
    ONGVERKLGEG o = c.getOngverklgeg();

    b.setInleverdatum(date2str(o.getInlevdatov().toString()));
    b.setAutoriteit1(RijbewijsUitgever.getCodeOms(o.getSrtautinlov()));
    b.setAutoriteit2(o.getAutorcinlov().toString() + " " + o.getNaamautinlov());
    b.setAutoriteit3(o.getNaaminlovdia());
    b.setCodeOngeldig(RijbewijsOngeldigCodeOV.getCodeOms(o.getOngeldcodeov()));

    setBean(b);
  }

  @Override
  public void setColumn(Column column, Field field, Property property) {

    if (property.is(AUTORITEIT1, AUTORITEIT2, AUTORITEIT3)) {
      column.setColspan(3);
    }

    super.setColumn(column, field, property);
  }
}
