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

package nl.procura.gba.web.modules.zaken.vog.page10;

import static nl.procura.gba.web.modules.zaken.vog.page10.Page10VogBean1.*;
import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.pos;

import com.vaadin.ui.Field;

import nl.procura.diensten.gba.ple.openoffice.formats.Geboorteformats;
import nl.procura.diensten.gba.ple.openoffice.formats.Naamformats;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.zaken.vog.VogAanvraag;
import nl.procura.gba.web.services.zaken.vog.VogAanvrager;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class Page10VogForm1 extends GbaForm<Page10VogBean1> {

  public Page10VogForm1(VogAanvraag aanvraag) {

    setReadonlyAsText(true);
    setCaption("Aanvrager");
    setOrder(NAAMAANVRAGER, GEBORENAANVRAGER, GESLACHTAANVRAGER, AANSCHRNAAMAANVRAGER, AFWADRES, STRAAT, HNR, HNRL,
        HNRT, PCAANVRAGER, PLAATSAANVRAGER, LANDAANVRAGER);

    setColumnWidths(WIDTH_130, "300px", "100px", "");

    VogAanvrager a = aanvraag.getAanvrager();
    Naamformats nf = new Naamformats(a.getVoornamen(), a.getGeslachtsnaam(), a.getVoorvoegsel(), "", "", null);

    Geboorteformats gf = new Geboorteformats();
    gf.setValues(a.getDatumGeboorte().getFormatDate(), a.getPlaatsGeboren().getDescription(),
        a.getLandGeboren().getDescription());

    Page10VogBean1 b = new Page10VogBean1();
    b.setNaamAanvrager(nf.getNaam_naamgebruik_eerste_voornaam());
    b.setGeborenAanvrager(gf.getDatum_te_plaats_land());
    b.setGeslachtAanvrager(a.getGeslacht().getNormaal());
    b.setAanschrNaamAanvrager(a.getAanschrijf());
    b.setAfwAdres(a.isAfwijkendAdres());

    b.setStraat(a.getStraat());
    b.setHnr(pos(a.getHnr()) ? astr(a.getHnr()) : "");
    b.setHnrL(a.getHnrL());
    b.setHnrT(a.getHnrT());
    b.setPcAanvrager(a.getPc());
    b.setPlaatsAanvrager(a.getPlaats());
    b.setLandAanvrager(a.getLand());

    setBean(b);

    initFields();
  }

  @Override
  public Field newField(Field field, Property property) {

    super.newField(field, property);

    if (property.is(AFWADRES)) {
      getLayout().addBreak();
    }

    return field;
  }

  @Override
  public void setColumn(Column column, Field field, Property property) {

    if (property.is(AFWADRES, STRAAT)) {
      column.setColspan(3);
    }

    if (property.is(HNR, HNRL, HNRT, PLAATSAANVRAGER)) {
      column.setAppend(true);
    }

    super.setColumn(column, field, property);
  }

  private void checkAfwijkendAdres() {

    boolean afw = !(Boolean) getField(AFWADRES).getValue();

    getField(STRAAT).setReadOnly(afw);
    getField(HNR).setReadOnly(afw);
    getField(HNRL).setReadOnly(afw);
    getField(HNRT).setReadOnly(afw);
    getField(PCAANVRAGER).setReadOnly(afw);
    getField(PLAATSAANVRAGER).setReadOnly(afw);
    getField(LANDAANVRAGER).setReadOnly(afw);

    repaint();
  }

  private void initFields() {

    getField(AFWADRES).addListener((ValueChangeListener) event -> checkAfwijkendAdres());

    checkAfwijkendAdres();
  }
}
