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

public class ZaakDmsVariantContainer extends IndexedContainer implements ProcuraContainer {

  public static final String ALLEEN_ZAAKDETAILS = "1";
  public static final String ALLE_BERICHTEN     = "2";

  public ZaakDmsVariantContainer() {
    Item item;

    addContainerProperty(OMSCHRIJVING, String.class, "");
    removeAllItems();

    item = addItem(ALLEEN_ZAAKDETAILS);
    item.getItemProperty(OMSCHRIJVING).setValue("Alleen zaakdetails");

    item = addItem(ALLE_BERICHTEN);
    item.getItemProperty(OMSCHRIJVING).setValue("Alle berichten apart");
  }
}
