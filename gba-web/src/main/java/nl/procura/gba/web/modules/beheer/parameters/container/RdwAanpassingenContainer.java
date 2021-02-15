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

package nl.procura.gba.web.modules.beheer.parameters.container;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;

import nl.procura.vaadin.component.container.ProcuraContainer;

public class RdwAanpassingenContainer extends IndexedContainer implements ProcuraContainer {

  public static final String OMSCHRIJVING = "Omschrijving";
  public static final String OUD          = "";
  public static final String DEEL1        = "1";
  //  public static final String DEEL2        = "2"; // Nog niet van toepassing

  public RdwAanpassingenContainer() {

    addContainerProperty(OMSCHRIJVING, String.class, "");
    removeAllItems();

    Item item = addItem(OUD);
    item.getItemProperty(OMSCHRIJVING).setValue("Geen aanpassingen");

    item = addItem(DEEL1);
    item.getItemProperty(OMSCHRIJVING).setValue("Deel 1 van de aanpassingen");

    //      item = addItem(DEEL2);
    //      item.getItemProperty(OMSCHRIJVING).setValue("Deel 2 van de aanpassingen");
  }
}
