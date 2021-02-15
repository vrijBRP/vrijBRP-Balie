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

package nl.procura.gbaws.web.vaadin.module.auth.profile.page3;

import com.vaadin.data.Item;

import nl.procura.burgerzaken.gba.core.enums.GBARecStatus;
import nl.procura.vaadin.component.container.ArrayListContainer;

public class GbaCategorieStatusContainer extends ArrayListContainer {

  public static final String DESCR = "descr";

  public GbaCategorieStatusContainer() {

    addContainerProperty(DESCR, String.class, "");

    Item item = addItem(GBARecStatus.CURRENT);
    item.getItemProperty(DESCR).setValue("Actueel");

    item = addItem(GBARecStatus.HIST);
    item.getItemProperty(DESCR).setValue("Historie");
  }
}
