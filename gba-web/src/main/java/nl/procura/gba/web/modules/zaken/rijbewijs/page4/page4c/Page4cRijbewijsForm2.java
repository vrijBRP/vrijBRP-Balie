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

package nl.procura.gba.web.modules.zaken.rijbewijs.page4.page4c;

import static nl.procura.gba.web.modules.zaken.rijbewijs.page4.Page4RijbewijsBean3.*;
import static nl.procura.standard.Globalfunctions.date2str;
import static nl.procura.standard.Globalfunctions.pos;

import com.vaadin.ui.Field;

import nl.procura.gba.web.modules.zaken.rijbewijs.RdwReadOnlyForm;
import nl.procura.gba.web.modules.zaken.rijbewijs.page4.Page4RijbewijsBean3;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsUitgever;
import nl.procura.rdw.processen.p0252.f08.UITGRYBGEG;
import nl.procura.rdw.processen.p0252.f08.VERBLYFRYBGEG;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class Page4cRijbewijsForm2 extends RdwReadOnlyForm {

  Page4cRijbewijsForm2(UITGRYBGEG u) {

    setColumnWidths(WIDTH_130, "", "130px", "");
    setCaption("Verblijf rijbewijsgegevens");
    setOrder(ONTVANGST, KORPSCODEO, ONTVANGST2, VERZENDING, KORPSCODEV, VERZENDING2);

    Page4RijbewijsBean3 b = new Page4RijbewijsBean3();

    VERBLYFRYBGEG v = u.getVerblyfrybgeg();

    String ontvangst = "";

    if (pos(v.getOntvrybdat())) {
      ontvangst += date2str(v.getOntvrybdat().toString()) + " " + v.getOntvrybtyd() + " ";
      ontvangst += RijbewijsUitgever.getCodeOms(v.getSrtautontv());
    }

    String verzending = "";

    if (pos(v.getVerzdatryb())) {
      verzending += date2str(v.getVerzdatryb().toString()) + " " + v.getVerzdatryb() + " ";
      verzending += RijbewijsUitgever.getCodeOms(v.getSrtautverz());
    }

    b.setOntvangst(ontvangst);
    b.setKorpscodeO(v.getKorpscodeontv());
    b.setOntvangst2(trimInteger(v.getAutorcodeontv()) + " " + v.getNaamautontv());

    b.setVerzending(verzending);
    b.setKorpscodeV(v.getKorpscodeverz());
    b.setVerzending2(trimInteger(v.getAutorcodeverz()) + " " + v.getNaamautverz());

    setBean(b);
  }

  @Override
  public void setColumn(Column column, Field field, Property property) {

    if (property.is(VERZENDING2, ONTVANGST2)) {
      column.setColspan(3);
    }

    super.setColumn(column, field, property);
  }
}
