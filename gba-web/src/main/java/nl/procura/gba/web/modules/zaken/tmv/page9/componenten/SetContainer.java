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

package nl.procura.gba.web.modules.zaken.tmv.page9.componenten;

import java.util.List;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;

import nl.procura.diensten.gba.ple.base.BasePLCat;
import nl.procura.diensten.gba.ple.base.BasePLSet;

public class SetContainer extends IndexedContainer {

  public static final String OMSCHRIJVING = "omschrijving";

  public SetContainer(BasePLCat soort) {

    addContainerProperty(OMSCHRIJVING, String.class, "");
    removeAllItems();

    List<BasePLSet> sets = soort.getSets();
    int maxVolgCode = 0;

    if (sets.size() > 0) {
      maxVolgCode = sets.get(0).getExtIndex();

      for (BasePLSet s : sets) {
        Item item = super.addItem(s);

        if (item != null) {
          item.getItemProperty(OMSCHRIJVING).setValue("Gegevensset " + s.getExtIndex());
        }
      }
    }

    if ((sets.isEmpty()) || soort.getCatType().isMultiSet()) {
      Item item = super.addItem(new BasePLSet(soort.getCatType(), maxVolgCode + 1));
      if (item != null) {
        item.getItemProperty(OMSCHRIJVING).setValue("Nieuwe stapel (" + (maxVolgCode + 1) + ")");
      }
    }
  }
}
