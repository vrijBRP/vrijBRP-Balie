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

import static ch.lambdaj.Lambda.*;
import static org.hamcrest.Matchers.greaterThan;

import java.util.List;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;

import nl.procura.gba.jpa.personen.db.BaseEntity;
import nl.procura.gba.web.components.fields.values.UsrFieldValue;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;

public class GebruikerContainer extends IndexedContainer {

  private static final String OMSCHRIJVING = "Omschrijving";
  private static final String CODE         = "Code";

  public GebruikerContainer(List<Gebruiker> gebruikers) {
    this(gebruikers, true);
  }

  public GebruikerContainer(List<Gebruiker> gebruikers, boolean addGebruikerNul) {

    removeAllItems();

    addContainerProperty(CODE, Long.class, "");
    addContainerProperty(OMSCHRIJVING, String.class, null);

    if (addGebruikerNul) {
      addItem(Gebruiker.getDefault());
    }

    for (Gebruiker gebruiker : select(gebruikers,
        having(on(Gebruiker.class).getCUsr(), greaterThan(BaseEntity.DEFAULT)))) {
      addItem(gebruiker);
    }
  }

  @Override
  public Item addItem(Object itemId) {

    Item item = null;

    if (itemId instanceof Gebruiker) {

      Gebruiker g = (Gebruiker) itemId;
      UsrFieldValue waarde = new UsrFieldValue(g.getCUsr(), g.getNaam());
      item = super.addItem(waarde);

      if (item != null) {
        item.getItemProperty(CODE).setValue(waarde.getValue());
        item.getItemProperty(OMSCHRIJVING).setValue(waarde.getDescription());
      }
    }

    return item;
  }
}
