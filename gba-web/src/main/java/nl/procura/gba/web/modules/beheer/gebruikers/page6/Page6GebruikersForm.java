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

package nl.procura.gba.web.modules.beheer.gebruikers.page6;

import static nl.procura.gba.web.modules.beheer.gebruikers.page6.Page6GebruikersBean.*;

import com.vaadin.ui.Field;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;

public class Page6GebruikersForm extends GbaForm<Page6GebruikersBean> {

  private final Gebruiker gebruiker;

  public Page6GebruikersForm(Gebruiker gebruiker) {

    this.gebruiker = gebruiker;

    setOrder(STANDAARDOPTIE, TERM, OMSCHRIJVING, WAARDE, AANGEPASTEWAARDE);
    setColumnWidths("200px", "");
  }

  @Override
  public Field newField(Field field, Property property) {

    if (property.is(AANGEPASTEWAARDE)) {
      field.setCaption("Waarde voor " + gebruiker.getNaam());
    }

    return super.newField(field, property);
  }
}
