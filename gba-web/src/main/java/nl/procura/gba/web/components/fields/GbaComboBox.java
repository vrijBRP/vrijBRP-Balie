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

package nl.procura.gba.web.components.fields;

import com.vaadin.data.Container;

import nl.procura.gba.web.components.containers.TabelContainer;
import nl.procura.vaadin.component.field.ProComboBox;

public class GbaComboBox extends ProComboBox {

  private static final long serialVersionUID = 944768620074623960L;

  public GbaComboBox() {
  }

  @Override
  public void setContainerDataSource(Container container) {
    setItemCaptionPropertyId(null);
    if (container instanceof TabelContainer) {
      setItemCaptionPropertyId(TabelContainer.OMSCHRIJVING);
      setItemCaptionMode(ITEM_CAPTION_MODE_PROPERTY); // Alleen met deze mode werkt de diacrietenfilter
    }
    super.setContainerDataSource(container);
  }
}
