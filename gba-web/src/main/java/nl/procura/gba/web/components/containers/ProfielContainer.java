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

package nl.procura.gba.web.components.containers;

import java.util.List;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;

import nl.procura.gba.web.services.beheer.profiel.Profiel;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class ProfielContainer extends IndexedContainer {

  private static final String OMSCHRIJVING = "Omschrijving";
  private static final String CODE         = "Code";

  public ProfielContainer(List<Profiel> profielen) {

    removeAllItems();

    addContainerProperty(CODE, Long.class, "");
    addContainerProperty(OMSCHRIJVING, String.class, null);

    for (Profiel profiel : profielen) {
      addItem(profiel);
    }
  }

  @Override
  public Item addItem(Object itemId) {

    Item item = null;

    if (itemId instanceof Profiel) {

      Profiel p = (Profiel) itemId;

      FieldValue waarde = new FieldValue(p.getCProfile(), p.getCProfile() + ": " + p.getProfiel());

      item = super.addItem(waarde);

      if (item != null) {

        item.getItemProperty(CODE).setValue(waarde.getValue());
        item.getItemProperty(OMSCHRIJVING).setValue(waarde.getValue() + ":" + waarde.getDescription());
      }
    }

    return item;
  }
}
