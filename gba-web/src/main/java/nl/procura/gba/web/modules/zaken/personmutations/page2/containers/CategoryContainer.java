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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.vaadin.data.Item;

import nl.procura.burgerzaken.gba.core.enums.GBACat;

public class CategoryContainer extends MutationFieldContainer {

  public CategoryContainer() {
    Arrays.stream(GBACat.values())
        .filter(cat -> cat.getCode() > 0)
        .filter(cat -> !isIgnoreCategory(cat))
        .forEach(this::addItem);
  }

  @Override
  public Item addItem(Object itemId) {
    GBACat category = (GBACat) itemId;
    String label = category.getCode() + ": " + category.getDescr();
    return super.addItem(new ContainerItem<>(category, label));
  }

  private boolean isIgnoreCategory(GBACat category) {
    List<GBACat> list = new ArrayList<>();
    list.add(GBACat.AFNEMERS);
    list.add(GBACat.DIV);
    list.add(GBACat.WK);
    list.add(GBACat.KLADBLOK);
    list.add(GBACat.LOK_AF_IND);
    list.add(GBACat.VERW);
    return list.contains(category);
  }
}
