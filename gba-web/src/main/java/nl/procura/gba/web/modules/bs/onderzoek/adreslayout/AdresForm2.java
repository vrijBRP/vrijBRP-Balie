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

package nl.procura.gba.web.modules.bs.onderzoek.adreslayout;

import static nl.procura.gba.web.modules.bs.onderzoek.adreslayout.AdresBean2.*;
import static nl.procura.standard.Globalfunctions.fil;
import static nl.procura.standard.Globalfunctions.pos;

import com.vaadin.ui.Field;

import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.gba.basistabellen.gemeente.Gemeente;
import nl.procura.gba.web.services.gba.basistabellen.gemeente.GemeenteService;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.layout.table.TableLayout;

public class AdresForm2 extends AdresForm<AdresBean2> {

  public AdresForm2(Adres adres) {

    super();
    setOrder(STRAAT, HNR, HNR_L, HNR_T, HNR_A, PC, WPL, GEMEENTE, POSTBUS);

    AdresBean2 bean = new AdresBean2();
    bean.setStraat(adres.getAdres());
    bean.setHnr(adres.getHnr());
    bean.setHnrL(adres.getHnrL());
    bean.setHnrT(adres.getHnrT());
    bean.setHnrA(adres.getHnrA());
    bean.setPc(adres.getPc());
    bean.setWoonplaats(adres.getPlaats());

    if (pos(adres.getGemeente().getStringValue())) {
      bean.setGemeente(adres.getGemeente());
    }

    setBean(bean);
  }

  @Override
  public void afterSetBean() {
    super.afterSetBean();
    getField(GEMEENTE).addListener((ValueChangeListener) event -> {
      setPostAdres((FieldValue) event.getProperty().getValue());
    });

    setPostAdres(getBean().getGemeente());
  }

  private void setPostAdres(FieldValue value) {

    Field field = getField(POSTBUS);
    field.setVisible(false);

    if (value != null) {
      GemeenteService gemService = Services.getInstance().getGemeenteService();
      Gemeente gemeente = gemService.getGemeente(value);

      field.setReadOnly(false);
      if (fil(gemeente.getAdres())) {
        field.setVisible(true);
        field.setCaption("(Post)adres gemeente " + gemeente.getGemeente());
        field.setValue(gemeente.getAdres() + " " + gemeente.getPc() + " " + gemeente.getPlaats());
      }

      field.setReadOnly(true);
    }
    repaint();
  }

  @Override
  public void setColumn(TableLayout.Column column, Field field, Property property) {

    if (property.is(HNR, HNR_L, HNR_T, HNR_A)) {
      column.setAppend(true);
    }

    super.setColumn(column, field, property);
  }
}
