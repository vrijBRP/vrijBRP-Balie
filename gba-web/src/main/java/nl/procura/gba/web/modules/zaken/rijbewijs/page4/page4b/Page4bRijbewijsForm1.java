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

import static nl.procura.gba.web.modules.zaken.rijbewijs.page4.page4b.Page4bRijbewijsBean1.*;
import static nl.procura.standard.Globalfunctions.date2str;
import static nl.procura.standard.Globalfunctions.pos;

import com.vaadin.ui.Field;

import nl.procura.gba.web.modules.zaken.rijbewijs.RdwReadOnlyForm;
import nl.procura.rdw.processen.p0252.f07.STRAFMAATRGEG;
import nl.procura.rdw.processen.p0252.f07.UITGMAATRGEG;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

/**
 * Strafmaatregelgegevens
 */
public class Page4bRijbewijsForm1 extends RdwReadOnlyForm {

  Page4bRijbewijsForm1(UITGMAATRGEG c) {

    setCaption("Strafmaatregelgegevens");

    setColumnWidths(WIDTH_130, "300px", "130px", "");

    setOrder(NUMMER, INVORDERING, EINDE, RKKCODE, KORPSCODE, VERBALISANT, INHTERMIJN);

    Page4bRijbewijsBean1 b = new Page4bRijbewijsBean1();

    STRAFMAATRGEG st = c.getStrafmaatrgeg();

    if (st != null && pos(st.getStrmaatrnr())) {

      b.setNummer(trimInteger(st.getStrmaatrnr()));
      b.setInvordering(date2str(st.getInvorddatstr().toString()));
      b.setEinde(date2str(st.getEindeinvdat().toString()));
      b.setRkkcode(st.getRkkcodestr());
      b.setKorpscode(st.getKorpscodestr());
      b.setVerbalisant(st.getVerbalisnrstr());
      b.setInhtermijn(st.getInhtermyn());
    }

    setBean(b);
  }

  @Override
  public void setColumn(Column column, Field field, Property property) {

    if (property.is(INHTERMIJN)) {
      column.setColspan(3);
    }

    super.setColumn(column, field, property);
  }
}
