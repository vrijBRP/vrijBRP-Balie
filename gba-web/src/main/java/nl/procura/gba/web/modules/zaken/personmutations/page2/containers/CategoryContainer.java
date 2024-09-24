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

package nl.procura.gba.web.modules.zaken.personmutations.page2.containers;

import static nl.procura.burgerzaken.gba.core.enums.GBACat.AFNEMERS;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.DIV;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.KLADBLOK;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.LOK_AF_IND;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.VERW;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.WK;

import java.util.Arrays;

import com.vaadin.data.Item;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;

public class CategoryContainer extends MutationFieldContainer {

  public CategoryContainer(BasePLExt pl) {
    Arrays.stream(GBACat.values())
        .filter(cat -> cat.getCode() > 0)
        .filter(cat -> isCategorie(pl, cat))
        .forEach(this::addItem);
  }

  @Override
  public Item addItem(Object itemId) {
    GBACat category = (GBACat) itemId;
    String label = category.getCode() + ": " + category.getDescr();
    return super.addItem(new ContainerItem<>(category, label));
  }

  private boolean isCategorie(BasePLExt pl, GBACat cat) {
    if (pl.heeftVerwijzing()) {
      return cat.is(VERW);
    } else {
      return !cat.is(AFNEMERS, DIV, WK, KLADBLOK, LOK_AF_IND, VERW);
    }
  }
}
