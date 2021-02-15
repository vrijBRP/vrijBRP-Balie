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

package nl.procura.gba.web.modules.beheer.overig;

import static nl.procura.standard.Globalfunctions.emp;
import static nl.procura.standard.Globalfunctions.eq;
import static nl.procura.standard.exceptions.ProExceptionSeverity.WARNING;
import static nl.procura.standard.exceptions.ProExceptionType.ENTRY;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.modules.beheer.documenten.page1.tab1.page2.Tab1DocumentenPage2Bean;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.zaken.documenten.DocumentRecord;
import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.component.dialog.ConfirmDialog;

/**
 * Deze klasse bevat de logica voor het opslaan van een nieuwe mapnaam bij een document.
 * Er wordt gecontroleerd of de mapnaam nieuw is en lege mapnamen worden niet toegestaan.
 * Merk op dat er bij het invoeren van de mapnaam een check wordt gedaan door MapValidator.
 *

 * <p>
 * 2012
 */

public abstract class CheckPadEnOpslaanDocument {

  private final GbaForm form;

  public CheckPadEnOpslaanDocument(String cleanedPath, Tab1DocumentenPage2Bean bean, GbaForm form) {
    this.form = form;
    String legalPath = normaliseerPad(cleanedPath).toString();
    checkPadEnOpslaanDocument(legalPath, bean);
  }

  protected abstract void nietOpslaanDocumentActions();

  protected abstract void opslaanDocument(String pad, Tab1DocumentenPage2Bean bean);

  protected abstract void welOpslaanDocumentActies(String pad, Tab1DocumentenPage2Bean b);

  private boolean bestaatPad(String pad) {
    for (DocumentRecord doc : getAllDocuments()) {
      if (eq(doc.getPad(), pad)) {
        return true;
      }
    }
    return false;
  }

  private void checkPadEnOpslaanDocument(final String pad, Tab1DocumentenPage2Bean bean) {

    final Tab1DocumentenPage2Bean b = bean;

    if (!emp(pad)) {

      if (bestaatPad(pad)) {
        opslaanDocument(pad, b);
      } else {

        String msg = MessageFormat.format("Ingevoerde mapnaam ''{0}'' <br>  is nieuw. Aan lijst toevoegen?",
            pad);
        form.getWindow().addWindow(new ConfirmDialog(msg) {

          @Override
          public void buttonNo() {
            close();
            nietOpslaanDocumentActions();
          }

          @Override
          public void buttonYes() {
            close();
            welOpslaanDocumentActies(pad, b);
          }
        });
      }
    } else {
      // docPath is gelijk aan de lege string
      // (omdat er al een trim() overheen is gegaan in onSave()).
      opslaanDocument(pad, bean);
    }
  }

  private List<DocumentRecord> getAllDocuments() {
    Services dbC = form.getApplication().getServices();
    return dbC.getDocumentService().getDocumenten(false);
  }

  /**
   * Deze functie controleert of er geen lege mapnamen voorkomen.
   */
  private StringBuilder normaliseerPad(String cleanedPath) {

    List<String> mappenString = new ArrayList<>();
    StringBuilder legaalPad = new StringBuilder();
    String[] mappen = cleanedPath.split("/");
    int index = 0;

    if (!emp(cleanedPath)) {
      for (String map : mappen) {
        if (emp(map)) {
          throw new ProException(ENTRY, WARNING, "Gebruik geen lege mapnamen a.u.b.");
        }
        // niet-lege mapnaam
        mappenString.add(map);
      }

      for (String dir : mappenString) {
        if (index == mappenString.size() - 1) {
          legaalPad = legaalPad.append(dir);
        } else {
          legaalPad = legaalPad.append(dir).append("/");
        }
        index++;
      }
    }
    return legaalPad;
  }
}
