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

import java.util.List;

import com.vaadin.data.Item;

import nl.procura.gba.web.services.beheer.email.EmailTemplate;
import nl.procura.vaadin.component.container.ArrayListContainer;

public class EmailTemplateContainer extends ArrayListContainer {

  public static final String OMSCHRIJVING = "Omschrijving";

  public EmailTemplateContainer(List<EmailTemplate> templates) {

    addContainerProperty(OMSCHRIJVING, String.class, "");

    for (EmailTemplate template : templates) {
      Item item = addItem(template);
      item.getItemProperty(OMSCHRIJVING).setValue(template.getOnderwerp());
    }
  }
}
