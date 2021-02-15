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

package nl.procura.gba.web.modules.tabellen.overlijdenaangever.page2;

import static nl.procura.gba.web.modules.bs.common.pages.persoonpage.BsPersoonBean1.*;
import static nl.procura.gba.web.modules.tabellen.overlijdenaangever.page2.Page2OverlijdenAangeverBean.BSN;
import static nl.procura.gba.web.modules.tabellen.overlijdenaangever.page2.Page2OverlijdenAangeverBean.INGANG_GELD;
import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.pos;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Field;

import nl.procura.gba.web.common.misc.Landelijk;
import nl.procura.gba.web.components.FormFieldset;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.gba.basistabellen.overlijdenaangever.OverlijdenAangever;
import nl.procura.vaadin.component.field.fieldvalues.DateFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class Page2OverlijdenAangeverForm extends GbaForm<Page2OverlijdenAangeverBean> {

  @Override
  public void afterSetColumn(Column column, Field field, Property property) {

    if (property.is(BSN)) {
      Button ophaalButton = new Button("Ophalen");
      ophaalButton.addListener((ClickListener) event -> {
        setValidationVisible(true);
        commit();
        onOphalen(astr(getField(BSN).getValue()));
      });
      column.addComponent(ophaalButton);
    }

    super.afterSetColumn(column, field, property);
  }

  @Override
  public void setBean(Object bean) {
    super.setBean(bean);

    if (getField(GEBOORTELAND) != null) {
      getField(GEBOORTELAND).addListener((ValueChangeListener) event -> changeGeboorteland());

      changeGeboorteland();
    }
  }

  @Override
  public Field newField(Field field, Property property) {

    if (property.is(INGANG_GELD)) {
      getLayout().addBreak(new FormFieldset("Geldigheid"));
    }

    return super.newField(field, property);
  }

  @SuppressWarnings("unused")
  public void onOphalen(String bsn) {
  }

  protected void init(OverlijdenAangever aangever) {

    setColumnWidths(WIDTH_130, "");

    Page2OverlijdenAangeverBean bean = new Page2OverlijdenAangeverBean();

    bean.setBsn(aangever.getBurgerServiceNummer());
    bean.setVoorn(aangever.getVoornamen());
    bean.setVoorv(new FieldValue(aangever.getVoorvoegsel()));
    bean.setTitel(aangever.getTitel());
    bean.setNaam(aangever.getGeslachtsnaam());
    bean.setGeslacht(aangever.getGeslacht());
    bean.setGeboortedatum(aangever.getDatumGeboorte());
    bean.setGeboorteland(aangever.getGeboorteland());
    bean.setTelefoon(aangever.getTelefoon());
    bean.setEmail(aangever.getEmail());
    bean.setIngangGeld(new DateFieldValue(aangever.getDatumIngang().getLongDate()));
    bean.setEindeGeld(new DateFieldValue(aangever.getDatumEinde().getLongDate()));

    if (aangever.getGeboorteplaats() != null) {
      if (pos(aangever.getGeboorteplaats().getValue())) {
        bean.setGeboorteplaatsNL(aangever.getGeboorteplaats());
      } else {
        bean.setGeboorteplaatsBuitenland(aangever.getGeboorteplaats().getDescription());
      }
    }

    setBean(bean);
  }

  private void changeGeboorteland() {

    FieldValue fv = (FieldValue) getField(GEBOORTELAND).getValue();

    if ((fv == null) || Landelijk.isNederland(fv)) {
      getField(GEBOORTEPLAATS_NL).setVisible(true);
      getField(GEBOORTEPLAATS_BL).setVisible(false);
    } else {
      getField(GEBOORTEPLAATS_NL).setVisible(false);
      getField(GEBOORTEPLAATS_BL).setVisible(true);
    }

    repaint();
  }
}
