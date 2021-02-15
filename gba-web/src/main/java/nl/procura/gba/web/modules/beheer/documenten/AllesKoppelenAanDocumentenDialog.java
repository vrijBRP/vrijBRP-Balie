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

package nl.procura.gba.web.modules.beheer.documenten;

import static java.util.Arrays.asList;
import static nl.procura.gba.web.services.beheer.KoppelActie.KOPPEL;
import static nl.procura.gba.web.services.beheer.KoppelActie.ONTKOPPEL;

import java.util.List;

import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.beheer.gebruikers.KoppelGebruikerTabel;
import nl.procura.gba.web.services.beheer.KoppelActie;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.gba.web.services.zaken.documenten.DocumentRecord;
import nl.procura.vaadin.component.dialog.ConfirmDialog;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;

/**
 * Dialog gebruikt voor het ontkoppelen van gebruikers aan documenten met
 * 'all_allowed = 1'. In dit geval wordt bevestiging gevraagd.
 *

 * <p>
 * 2012
 */

public abstract class AllesKoppelenAanDocumentenDialog extends ConfirmDialog {

  private final VerdeelEnTelDocumenten info;
  private final List<Gebruiker>        selectedUsers;
  private final List<Record>           selectedRecords;
  private final GbaTable               table;

  public AllesKoppelenAanDocumentenDialog(GbaTable table, VerdeelEnTelDocumenten info, List<Gebruiker> selectedUsers,
      List<Record> selectedRecords, String message) {
    super(message);
    this.info = info;
    this.selectedUsers = selectedUsers;
    this.selectedRecords = selectedRecords;
    this.table = table;
  }

  @Override
  public void buttonYes() {

    close();
    unlink();
    setTableStatus(ONTKOPPEL);
  }

  public abstract void unlink(List<DocumentRecord> nonAllAllowedDocs);

  private void disableAllAllowed(DocumentRecord document) {
    document.setIedereenToegang(false);
    table.getApplication().getServices().getDocumentService().save(document);
  }

  private void linkAllOtherUsers(List<DocumentRecord> allAllowedDocs) {

    List<Gebruiker> allUsers = table.getApplication().getServices().getGebruikerService().getGebruikers(false);

    for (Gebruiker user : selectedUsers) {
      allUsers.remove(user);
    }

    for (DocumentRecord doc : allAllowedDocs) {
      for (Gebruiker user : allUsers) {
        table.getApplication().getServices().getGebruikerService().koppelActie(asList(doc), asList(user),
            KOPPEL);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private void setTableStatus(KoppelActie koppelActie) {
    if (table instanceof KoppelGebruikerTabel) {
      ((KoppelGebruikerTabel) table).setTableStatus(koppelActie, selectedRecords);
    } else if (table instanceof KoppelDocumentTabel) {
      ((KoppelDocumentTabel) table).setTableStatus(koppelActie, selectedRecords);
    }
  }

  private void unlink() {
    unlink(info.getNietAlleToegestaan());
    unlinkFromAllAllowedDocs(info.getAlleToegestaan());
  }

  /**
   * Geselecteerde gebruiker ontkoppelen; de rest van de gebruikers kopppelen!
   * Dit verwacht
   */
  private void unlinkFromAllAllowedDocs(List<DocumentRecord> allAllowedDocs) {

    for (Gebruiker user : selectedUsers) {
      for (DocumentRecord doc : allAllowedDocs) {
        unlinkUser(user, doc);
      }
    }

    linkAllOtherUsers(allAllowedDocs);
  }

  private void unlinkUser(Gebruiker gebruiker, DocumentRecord document) {
    table.getApplication().getServices().getGebruikerService().koppelActie(asList(document), asList(gebruiker),
        ONTKOPPEL);
    disableAllAllowed(document);
  }
}
