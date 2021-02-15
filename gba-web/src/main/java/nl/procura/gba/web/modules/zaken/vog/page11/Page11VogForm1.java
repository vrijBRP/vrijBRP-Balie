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

package nl.procura.gba.web.modules.zaken.vog.page11;

import static nl.procura.gba.web.modules.zaken.vog.page11.Page11VogBean1.*;
import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.pos;

import com.vaadin.ui.Field;

import nl.procura.gba.web.common.misc.Landelijk;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.zaken.vog.VogAanvraag;
import nl.procura.gba.web.services.zaken.vog.VogAanvraagBelanghebbende;
import nl.procura.gba.web.services.zaken.vog.VogBelanghebbende;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class Page11VogForm1 extends GbaForm<Page11VogBean1> {

  public Page11VogForm1(VogAanvraag aanvraag) {

    setReadonlyAsText(true);
    setReadThrough(true);
    setCaption("Belanghebbende");
    setOrder(SNELKEUZE, NAAM, VERTEGENWOORDIGER, STRAAT, HNR, HNRL, HNRT, POSTCODE, PLAATS, LAND, TELEFOON);
    setColumnWidths(WIDTH_130, "", "130px", "");

    Page11VogBean1 b = new Page11VogBean1();
    VogAanvraagBelanghebbende bel = aanvraag.getBelanghebbende();

    b.setNaam(bel.getNaam());
    b.setVertegenwoordiger(bel.getVertegenwoordiger());
    b.setStraat(bel.getStraat());
    b.setHnr(astr(bel.getHnr()));
    b.setHnrL(bel.getHnrL());
    b.setHnrT(astr(bel.getHnrT()));
    b.setPostcode(bel.getPc());
    b.setPlaats(bel.getPlaats());
    b.setLand(bel.getLand());
    b.setTelefoon(bel.getTel());

    // Standaard Nederland
    if (!pos(b.getLand().getLongValue())) {
      b.setLand(Landelijk.getNederland());
    }

    setBean(b);
  }

  @Override
  public Field newField(Field field, Property property) {

    super.newField(field, property);
    if (property.is(NAAM, STRAAT)) {
      getLayout().addBreak();
    }

    return field;
  }

  public void setBelangHebbende(VogBelanghebbende bel) {

    getBean().setNaam(bel.getNaam());
    getBean().setVertegenwoordiger(bel.getVertegenwoordiger());
    getBean().setStraat(bel.getStraat());
    getBean().setHnr(astr(bel.getHnr()));
    getBean().setHnrT(astr(bel.getHnrT()));
    getBean().setHnrL("");
    getBean().setPostcode(bel.getPc());
    getBean().setPlaats(bel.getPlaats());
    getBean().setLand(bel.getLand());
    getBean().setTelefoon(bel.getTel());

    repaint();
  }

  @Override
  public void setColumn(Column column, Field field, Property property) {

    if (property.is(SNELKEUZE, NAAM, VERTEGENWOORDIGER, STRAAT, POSTCODE)) {
      column.setColspan(3);
    }

    if (property.is(HNR, HNRL, HNRT, PLAATS)) {
      column.setAppend(true);
    }

    super.setColumn(column, field, property);
  }
}
