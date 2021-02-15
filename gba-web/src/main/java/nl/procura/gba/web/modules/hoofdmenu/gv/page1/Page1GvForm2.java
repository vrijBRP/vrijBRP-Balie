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

package nl.procura.gba.web.modules.hoofdmenu.gv.page1;

import static nl.procura.gba.web.modules.hoofdmenu.gv.page1.Page1GvBean2.*;

import com.vaadin.ui.Field;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.zaken.documenten.afnemers.DocumentAfnemer;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class Page1GvForm2 extends GbaForm<Page1GvBean2> {

  public Page1GvForm2() {

    setCaption("Adressering");
    setColumnWidths(WIDTH_130, "330px", "100px", "");
    setReadThrough(true);
    setOrder(INFORMATIEVRAGER, TAV_AANHEF, TAV_VOORL, TAV_NAAM, ADRES, PC, PLAATS, EMAIL, KENMERK);

    setBean(new Page1GvBean2());
  }

  @Override
  public Page1GvBean2 getNewBean() {
    return new Page1GvBean2();
  }

  public void setAfnemer(DocumentAfnemer da) {

    if (da == null) {
      getBean().setInformatievrager("");
      getBean().setTavAanhef(null);
      getBean().setTavVoorl("");
      getBean().setTavNaam("");
      getBean().setEmail("");
      getBean().setAdres("");
      getBean().setPc(new FieldValue());
      getBean().setPlaats("");
    } else {
      getBean().setInformatievrager(da.getDocumentAfn());
      getBean().setTavAanhef(da.getTerAttentieVanAanhef());
      getBean().setTavVoorl(da.getTavVoorl());
      getBean().setTavNaam(da.getTavNaam());
      getBean().setEmail(da.getEmail());
      getBean().setAdres(da.getAdres());
      getBean().setPc(da.getPostcode());
      getBean().setPlaats(da.getPlaats());
    }

    setBean(getBean());
  }

  @Override
  public void setColumn(Column column, Field field, Property property) {

    if (property.is(TAV_VOORL, TAV_NAAM)) {
      column.setAppend(true);
    }

    if (property.is(KENMERK)) {
      column.setColspan(3);
    }

    super.setColumn(column, field, property);
  }
}
