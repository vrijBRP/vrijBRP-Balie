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

package nl.procura.gba.web.components.layouts.form.document.email.page1;

import static nl.procura.standard.Globalfunctions.emp;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;

import nl.procura.gba.web.common.misc.email.EmailAddress;

public class EmailContainer extends IndexedContainer {

  public static final String OMSCHRIJVING = "Omschrijving";

  public EmailContainer(List<EmailAddress> gebruikerAdressen, EmailPreviewContainer container) {

    List<EmailAddress> adressen = new ArrayList<>();
    adressen.addAll(container.getZaakAdressen());
    adressen.addAll(gebruikerAdressen);

    addContainerProperty(OMSCHRIJVING, String.class, "");

    for (EmailAddress ontvanger : adressen) {
      Item item = addItem(ontvanger);
      if (item != null) {
        String omschrijving = ontvanger.toString();
        if (emp(ontvanger.getNameAndEmail())) {
          omschrijving = ontvanger.getFunction() + ": niet gevuld";
        }
        item.getItemProperty(OMSCHRIJVING).setValue(omschrijving);
      }
    }
  }
}
