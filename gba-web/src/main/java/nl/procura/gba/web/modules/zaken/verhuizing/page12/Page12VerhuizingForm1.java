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

package nl.procura.gba.web.modules.zaken.verhuizing.page12;

import static nl.procura.gba.web.modules.zaken.verhuizing.page12.Page12VerhuizingBean1.*;

import com.vaadin.ui.Field;

import nl.procura.diensten.gba.ple.procura.arguments.PLEDatasource;
import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.components.listeners.FieldChangeListener;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class Page12VerhuizingForm1 extends GbaForm<Page12VerhuizingBean1> {

  public Page12VerhuizingForm1(GbaApplication application) {

    setReadonlyAsText(true);
    setReadThrough(true);
    setCaption("Nieuw adres ingeven");
    setOrder(GEGEVENSBRON, STRAAT_GEM, STRAAT_LAND, HNR, PC, ADRESIND);
    setColumnWidths(WIDTH_130, "");

    setBean(new Page12VerhuizingBean1());

    getField(GEGEVENSBRON, GbaNativeSelect.class).setContainerDataSource(
        new DataSourceContainer(application.getServices()));
    getField(GEGEVENSBRON).focus();
  }

  @Override
  public void setColumn(Column column, Field field, Property property) {

    if (property.is(HNR)) {
      column.setAppend(true);
    }

    if (property.is(GEGEVENSBRON)) {

      field.addListener(new FieldChangeListener<PLEDatasource>() {

        @Override
        public void onChange(PLEDatasource bron) {
          getField(STRAAT_GEM).setVisible(bron == PLEDatasource.PROCURA);
          getField(STRAAT_LAND).setVisible(bron == PLEDatasource.GBAV);
          getField(ADRESIND).setVisible(bron == PLEDatasource.GBAV);
          repaint();
        }
      });
    }

    super.setColumn(column, field, property);
  }
}
