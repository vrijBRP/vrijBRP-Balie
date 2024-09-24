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

package nl.procura.gba.web.components.layouts.form.document;

import static ch.lambdaj.Lambda.join;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.sum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.services.zaken.documenten.DocumentSoort;
import nl.procura.vaadin.component.container.ArrayListContainer;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class DocumentSoortContainer extends ArrayListContainer {

  private static final int MAX_DOCUMENTEN = 15;

  public DocumentSoortContainer(GbaApplication application, List<DocumentSoort> soorten) {

    try {
      for (FieldValue fieldValue : getSoortCaption(soorten)) {
        addItem(fieldValue);
      }
    } catch (Exception e) {
      application.handleException(application.getCurrentWindow(), e);
    }
  }

  /**
   * Vul de FieldValues met een lijst met documentsoorten en een omschrijving.
   */
  private List<FieldValue> getSoortCaption(List<DocumentSoort> soorten) {

    List<FieldValue> values = new ArrayList<>();
    int aantalDocumenten = sum(soorten, on(DocumentSoort.class).getDocumenten().size());

    if (soorten.size() > 0) {
      if (aantalDocumenten <= MAX_DOCUMENTEN) {
        values.add(new FieldValue(soorten, join(soorten, " + ")));
      } else {
        for (DocumentSoort soort : soorten) {
          values.add(new FieldValue(Collections.singletonList(soort), soort.getType().getOms()));
        }
      }
    }

    return values;
  }
}
