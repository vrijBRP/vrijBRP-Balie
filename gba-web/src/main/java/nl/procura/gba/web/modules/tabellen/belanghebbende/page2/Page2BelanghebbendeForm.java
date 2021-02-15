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

package nl.procura.gba.web.modules.tabellen.belanghebbende.page2;

import static nl.procura.gba.web.modules.tabellen.belanghebbende.page2.Page2BelanghebbendeBean.*;

import com.vaadin.ui.Field;

import nl.procura.gba.web.components.FormFieldset;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.gba.basistabellen.belanghebbende.Belanghebbende;
import nl.procura.vaadin.component.field.fieldvalues.DateFieldValue;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class Page2BelanghebbendeForm extends GbaForm<Page2BelanghebbendeBean> {

  public Page2BelanghebbendeForm(Belanghebbende belanghebbende) {

    setColumnWidths(WIDTH_130, "");
    setCaption("Belanghebbende");
    setOrder(TYPE, NAAM, TAV_AANHEF, TAV_VOORL, TAV_NAAM, ADRES, POSTCODE, PLAATS, LAND, TELEFOON, EMAIL,
        INGANG_GELD, EINDE_GELD);
    updateBean(belanghebbende);
  }

  @Override
  public Field newField(Field field, Property property) {

    if (property.is(INGANG_GELD)) {
      getLayout().addBreak(new FormFieldset("Geldigheid"));
    }

    return super.newField(field, property);
  }

  @Override
  public void setColumn(Column column, Field field, Property property) {

    if (property.is(TAV_VOORL, TAV_NAAM)) {
      column.setAppend(true);
    }

    super.setColumn(column, field, property);
  }

  public void updateBean(Belanghebbende belanghebbende) {

    Page2BelanghebbendeBean bean = new Page2BelanghebbendeBean();
    bean.setType(belanghebbende.getBelanghebbendeType());
    bean.setNaam(belanghebbende.getNaam());
    bean.setTavAanhef(belanghebbende.getTerAttentieVanAanhef());
    bean.setTavVoorl(belanghebbende.getTavVoorl());
    bean.setTavNaam(belanghebbende.getTavNaam());
    bean.setAdres(belanghebbende.getAdres());
    bean.setPostcode(belanghebbende.getPostcode());
    bean.setPlaats(belanghebbende.getPlaats());
    bean.setLand(belanghebbende.getLand());
    bean.setTelefoon(belanghebbende.getTelefoon());
    bean.setEmail(belanghebbende.getEmail());
    bean.setIngangGeld(new DateFieldValue(belanghebbende.getDatumIngang().getLongDate()));
    bean.setEindeGeld(new DateFieldValue(belanghebbende.getDatumEinde().getLongDate()));

    setBean(bean);
  }
}
