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

package nl.procura.gba.web.modules.zaken.vog.page2;

import static nl.procura.gba.web.modules.zaken.vog.page2.Page2VogBean1.*;
import static nl.procura.standard.Globalfunctions.astr;

import com.vaadin.ui.Field;

import nl.procura.diensten.gba.ple.openoffice.formats.Adresformats;
import nl.procura.diensten.gba.ple.openoffice.formats.Geboorteformats;
import nl.procura.diensten.gba.ple.openoffice.formats.Naamformats;
import nl.procura.gba.web.services.zaken.vog.VogAanvraag;
import nl.procura.gba.web.services.zaken.vog.VogAanvrager;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class Page2VogForm1 extends Page2VogForm {

  public Page2VogForm1(VogAanvraag aanvraag) {

    setCaption("Aanvrager");
    setOrder(NAAMAANVRAGER, GEBORENAANVRAGER, GESLACHTAANVRAGER, AANSCHRNAAMAANVRAGER, ADRESAANVRAGER, PCAANVRAGER,
        PLAATSAANVRAGER, LANDAANVRAGER);
    setColumnWidths(WIDTH_130, "", "130px", "200px");

    Page2VogBean1 b = new Page2VogBean1();

    VogAanvrager a = aanvraag.getAanvrager();

    Naamformats nf = new Naamformats(a.getVoornamen(), a.getGeslachtsnaam(), a.getVoorvoegsel(), "", "", null);

    Geboorteformats g = new Geboorteformats();
    g.setValues(a.getDatumGeboorte().getFormatDate(), a.getPlaatsGeboren().getDescription(),
        a.getLandGeboren().getDescription());

    b.setNaamAanvrager(nf.getNaam_naamgebruik_eerste_voornaam());
    b.setGeborenAanvrager(g.getDatum_te_plaats_land());
    b.setGeslachtAanvrager(a.getGeslacht().getNormaal());

    Adresformats af = new Adresformats();
    af.setValues(a.getStraat(), astr(a.getHnr()), a.getHnrL(), a.getHnrT(), "", "", "", "", "", "", "", "", "", "",
        "", "");

    b.setAanschrNaamAanvrager(a.getAanschrijf());
    b.setAdresAanvrager(af.getAdres());
    b.setPcAanvrager(a.getPc().getDescription());
    b.setPlaatsAanvrager(a.getPlaats());
    b.setLandAanvrager(a.getLand().getDescription());

    setBean(b);
  }

  @Override
  public Field newField(Field field, Property property) {

    super.newField(field, property);

    if (property.is(AANSCHRNAAMAANVRAGER)) {
      getLayout().addBreak();
    }

    return field;
  }

  @Override
  public void setColumn(Column column, Field field, Property property) {

    if (property.is(NAAMAANVRAGER, AANSCHRNAAMAANVRAGER)) {
      column.setColspan(3);
    }

    super.setColumn(column, field, property);
  }

}
