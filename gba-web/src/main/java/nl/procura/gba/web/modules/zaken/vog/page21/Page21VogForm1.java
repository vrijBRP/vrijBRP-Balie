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

package nl.procura.gba.web.modules.zaken.vog.page21;

import static nl.procura.gba.web.modules.zaken.vog.page21.Page21VogBean1.*;
import static nl.procura.standard.Globalfunctions.astr;

import com.vaadin.ui.Field;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.zaken.vog.VogBelanghebbende;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class Page21VogForm1 extends GbaForm<Page21VogBean1> {

  public Page21VogForm1(VogBelanghebbende bel) {

    setReadonlyAsText(true);
    setCaption("Belanghebbende");
    setOrder(NAAM, VERTEGENWOORDIGER, STRAAT, HNR, HNRT, POSTCODE, PLAATS, LAND, TELEFOON);
    setColumnWidths(WIDTH_130, "");

    Page21VogBean1 b = new Page21VogBean1();

    if (bel.isStored()) {
      b.setNaam(bel.getNaam());
      b.setVertegenwoordiger(bel.getVertegenwoordiger());
      b.setStraat(bel.getStraat());
      b.setHnr(astr(bel.getHnr()));
      b.setHnrT(astr(bel.getHnrT()));
      b.setPostcode(bel.getPc());
      b.setPlaats(bel.getPlaats());
      b.setLand(bel.getLand());
      b.setTelefoon(bel.getTel());
    }

    setBean(b);
  }

  @Override
  public Field newField(Field field, Property property) {
    super.newField(field, property);

    if (property.is(STRAAT)) {
      getLayout().addBreak();
    }

    return field;
  }

  @Override
  public void setColumn(Column column, Field field, Property property) {

    if (property.is(HNR, HNRT, PLAATS)) {
      column.setAppend(true);
    }

    super.setColumn(column, field, property);
  }
}
