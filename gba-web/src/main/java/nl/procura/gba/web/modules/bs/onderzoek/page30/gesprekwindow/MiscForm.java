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

package nl.procura.gba.web.modules.bs.onderzoek.page30.gesprekwindow;

import static nl.procura.gba.web.modules.bs.onderzoek.page30.gesprekwindow.BronBean.*;

import com.vaadin.ui.Field;

import nl.procura.gba.web.services.bs.onderzoek.DossierOnderzoekBron;
import nl.procura.gba.web.services.bs.onderzoek.enums.VermoedAdresType;
import nl.procura.vaadin.component.layout.table.TableLayout;

public class MiscForm extends AbstractBronForm {

  private nl.procura.gba.web.components.listeners.ValueChangeListener<VermoedAdresType> changeListener;

  public MiscForm(DossierOnderzoekBron bron,
      nl.procura.gba.web.components.listeners.ValueChangeListener<VermoedAdresType> changeListener) {
    setCaption("Aanschrijving overige");
    setColumnWidths("200px", "");
    setOrder(F_INSTANTIE, F_AFDELING, F_TAV_AANHEF, F_TAV_VOORL, F_TAV_NAAM, F_ADRES_TYPE);
    this.changeListener = changeListener;
    setBron(bron);
  }

  @Override
  public void afterSetBean() {
    getField(F_ADRES_TYPE).addListener((ValueChangeListener) event -> {
      changeListener.onChange((VermoedAdresType) event.getProperty().getValue());
      repaint();
    });
  }

  @Override
  public void setColumn(TableLayout.Column column, Field field, Property property) {
    if (property.is(F_TAV_VOORL, F_TAV_NAAM)) {
      column.setAppend(true);
    }

    super.setColumn(column, field, property);
  }
}
