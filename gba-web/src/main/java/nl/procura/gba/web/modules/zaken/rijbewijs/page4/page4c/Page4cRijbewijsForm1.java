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

import static nl.procura.gba.web.modules.zaken.rijbewijs.page4.Page4RijbewijsBean1.*;
import static nl.procura.standard.Globalfunctions.*;

import com.vaadin.ui.Field;

import nl.procura.gba.web.modules.zaken.rijbewijs.RdwReadOnlyForm;
import nl.procura.gba.web.modules.zaken.rijbewijs.page4.Page4RijbewijsBean1;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsUitgever;
import nl.procura.rdw.processen.p0252.f08.RYBGEG;
import nl.procura.rdw.processen.p0252.f08.UITGRYBGEG;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class Page4cRijbewijsForm1 extends RdwReadOnlyForm {

  Page4cRijbewijsForm1(UITGRYBGEG c) {

    initForm();
    setColumnWidths(WIDTH_130, "", "130px", "");

    Page4RijbewijsBean1 b = new Page4RijbewijsBean1();
    RYBGEG r = c.getRybgeg();

    String afgifte = date2str(along(r.getAfgiftedatryb())) + " ";
    afgifte += RijbewijsUitgever.getCodeOms(r.getSrtautafg());

    b.setAfgifte(afgifte);
    b.setNummer(trimInteger(r.getRybnr()));
    b.setAfgifte2(trimInteger(r.getAutorcodeafg()) + " " + r.getNaamautafg());
    b.setVervangt(r.getVervangrybnr());

    String verlies = date2str(r.getVerldiefstdat().toString()) + " ";
    if (pos(r.getVerldiefstdat())) {
      verlies += RijbewijsUitgever.getCodeOms(r.getSrtautverld());
    }

    b.setVerlies(verlies);
    b.setVerlies2(trimInteger(r.getAutorcverld()));
    b.setErkend(date2str(r.getErkrybdat().toString()) + " " + r.getErkrybnr() + " " + trimInteger(r.getLandcodeerk()));

    setBean(b);
  }

  @Override
  public void setColumn(Column column, Field field, Property property) {

    if (property.is(PARTNER, VERLIES, VERLIES2, ERKEND)) {
      column.setColspan(3);
    }

    super.setColumn(column, field, property);
  }

  protected void initForm() {
  }
}
