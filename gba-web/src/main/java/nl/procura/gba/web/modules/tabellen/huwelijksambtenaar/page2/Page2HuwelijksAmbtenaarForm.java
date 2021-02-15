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

package nl.procura.gba.web.modules.tabellen.huwelijksambtenaar.page2;

import static nl.procura.gba.web.modules.tabellen.belanghebbende.page2.Page2BelanghebbendeBean.INGANG_GELD;
import static nl.procura.gba.web.modules.tabellen.huwelijksambtenaar.page2.Page2HuwelijksAmbtenaarBean.BSN;
import static nl.procura.standard.Globalfunctions.astr;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Field;

import nl.procura.gba.web.components.FormFieldset;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.gba.basistabellen.huwelijksambtenaar.HuwelijksAmbtenaar;
import nl.procura.vaadin.component.field.fieldvalues.DateFieldValue;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class Page2HuwelijksAmbtenaarForm extends GbaForm<Page2HuwelijksAmbtenaarBean> {

  @Override
  public void afterSetColumn(Column column, Field field, Property property) {

    if (property.is(BSN)) {
      Button ophaalButton = new Button("Ophalen");
      ophaalButton.addListener(new ClickListenerImplementation());
      column.addComponent(ophaalButton);
    }

    super.afterSetColumn(column, field, property);
  }

  public void init(HuwelijksAmbtenaar ambtenaar) {

    setColumnWidths(WIDTH_130, "");
    Page2HuwelijksAmbtenaarBean bean = new Page2HuwelijksAmbtenaarBean();

    bean.setNaam(ambtenaar.getHuwelijksAmbtenaar());
    bean.setToelichting(ambtenaar.getToelichting());
    bean.setTelefoon(ambtenaar.getTelefoon());
    bean.setEmail(ambtenaar.getEmail());
    bean.setBsn(ambtenaar.getBurgerServiceNummer());
    bean.setAlias(StringUtils.join(ambtenaar.getAliassen(), ", "));
    bean.setIngangGeld(new DateFieldValue(ambtenaar.getDatumIngang().getLongDate()));
    bean.setEindeGeld(new DateFieldValue(ambtenaar.getDatumEinde().getLongDate()));

    setBean(bean);
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

  private final class ClickListenerImplementation implements ClickListener {

    @Override
    public void buttonClick(ClickEvent event) {
      setValidationVisible(true);
      commit();
      onOphalen(astr(getField(BSN).getValue()));
    }
  }
}
