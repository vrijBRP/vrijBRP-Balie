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

package nl.procura.gba.web.modules.beheer.verkiezing.page2.ongeldigverklaren;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;

import nl.procura.gba.jpa.personen.db.KiesrVerk;
import nl.procura.vaadin.component.container.ProcuraContainer;

public class RedenOngeldigVerklarenContainer extends IndexedContainer implements ProcuraContainer {

  public static final String OMSCHRIJVING = "Omschrijving";

  public RedenOngeldigVerklarenContainer(KiesrVerk verkiezing) {
    addContainerProperty(OMSCHRIJVING, String.class, "");
    removeAllItems();

    for (RedenOngeldigVerklarenType reden : RedenOngeldigVerklarenType.values()) {
      Item item = addItem(reden);
      item.getItemProperty(OMSCHRIJVING).setValue(reden.getLabel(verkiezing));
    }
  }
}
