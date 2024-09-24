/*
 * Copyright 2022 - 2023 Procura B.V.
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

package nl.procura.gba.web.modules.zaken.personmutations.page3;

import static nl.procura.burgerzaken.gba.core.enums.GBATable.PLAATS;
import static nl.procura.gba.web.common.misc.Landelijk.LAND_NEDERLAND;
import static nl.procura.gba.web.common.misc.Landelijk.NATIONALITEIT_NEDERLANDS;

import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;

import nl.procura.burgerzaken.gba.core.enums.GBATable;
import nl.procura.burgerzaken.gba.core.validators.DatVal;
import nl.procura.gba.web.common.tables.GbaTables;
import nl.procura.gba.web.modules.zaken.personmutations.page2.PersonListMutElem;
import nl.procura.standard.ProcuraDate;
import nl.procura.vaadin.component.field.fieldvalues.DateFieldValue;

public class TablePersonListMutContainer {

  /**
   * Creates a component wrapper for some elements like country or place
   */
  public static Component get(PersonListMutElem mutElem) {
    Component layout = new CustomValueLayout(mutElem);
    if (!mutElem.isReadonly()) {
      if (mutElem.getField() instanceof ComboBox) {
        if (mutElem.getElemType().getTable() == GBATable.LAND) {
          layout = new CustomValueLayout(mutElem, services -> GbaTables.LAND.get(LAND_NEDERLAND));
        } else if (mutElem.getElemType().getTable() == GBATable.NATIO) {
          layout = new CustomValueLayout(mutElem,
              services -> GbaTables.NATIO.get(NATIONALITEIT_NEDERLANDS));
        } else if (mutElem.getElemType().getTable() == PLAATS) {
          layout = new CustomValueLayout(mutElem, services -> {
            String gemeenteCode = services.getGebruiker().getGemeenteCode();
            return GbaTables.PLAATS.get(gemeenteCode);
          });
        }
      } else if (mutElem.getTypes().getElem().getVal() instanceof DatVal) {
        layout = new CustomValueLayout(mutElem, services -> new DateFieldValue(new ProcuraDate().getSystemDate()));
      }
    }
    return layout;
  }
}
