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

package nl.procura.gba.web.modules.beheer.gebruikers;

import static nl.procura.gba.web.services.beheer.KoppelActie.KOPPEL;
import static nl.procura.gba.web.services.beheer.KoppelActie.ONTKOPPEL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.vaadin.data.Container;

import nl.procura.gba.web.components.dialogs.KoppelProcedure;
import nl.procura.gba.web.modules.beheer.documenten.AllesKoppelenAanDocumentenDialog;
import nl.procura.gba.web.modules.beheer.documenten.VerdeelEnTelDocumenten;
import nl.procura.gba.web.modules.beheer.overig.KoppelForm;
import nl.procura.gba.web.modules.beheer.overig.KoppelPage;
import nl.procura.gba.web.modules.beheer.overig.NavigationSorter.Dir;
import nl.procura.gba.web.services.beheer.KoppelActie;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.gba.web.services.beheer.gebruiker.KoppelbaarAanGebruiker;
import nl.procura.gba.web.services.zaken.documenten.DocumentRecord;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;

/**
 * Deze klasse wordt gebruikt om een selectie van 'koppelbaar aan gebruiker objecten'
 * te koppelen aan een selectie van gebruikers.
 *

 * <p>
 * 2012
 */
public class KoppelGebruikersPage<K extends KoppelbaarAanGebruiker> extends KoppelPage {

  private KoppelGebruikerTabel<K> table           = null;
  private final List<K>           koppelList;
  private final KoppelForm        form;
  private final String            type;                               // dit is het type waar aan gekoppeld wordt
  private List<Record>            selectedRecords = new ArrayList<>();

  @SuppressWarnings("unchecked")
  public KoppelGebruikersPage(K koppelObject, String type) {
    this(Arrays.asList(koppelObject), type);
  }

  public KoppelGebruikersPage(List<K> koppelList, String type) {
    super("");
    setMargin(false);
    this.koppelList = koppelList;
    form = new KoppelForm();
    this.type = type;
  }

  /**
   * Nodig als deze pagina ergens ingevoegd wordt waar al een 'vorige' knop aanwezig is.
   */
  public void disablePreviousButton() {
    buttonPrev.setVisible(false);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      setTable(); // nu worden alle gebruikers voor de tabel alleen opgevraagd bij de eerste keer aanmaken van de pagina!
      setInfo("De status van een map is 'Gekoppeld' als <b>elke</b> gebruiker in de map gekoppeld is aan de "
          + "geselecteerde " + type + "." + "<br>" + "Klik één keer op de regel om deze te selecteren."
          + " Wijzig de status door te dubbelklikken op een document of door de knoppen te gebruiken in geval van een map.");

      addComponent(form);
      addComponent(table.getDirectoryLayout());
      addExpandComponent(table);
    }

    super.event(event);
  }

  public KoppelGebruikerTabel<K> getTable() {
    return table;
  }

  public void setTable(KoppelGebruikerTabel<K> table) {
    this.table = table;
  }

  @Override
  protected void allesKoppelActie(KoppelActie koppelActie) {

    new KoppelProcedure(table, koppelActie, type, true) {

      @Override
      public void koppel(KoppelActie koppelActie, boolean wholeTable) {

        KoppelGebruikersPage.this.couple(koppelActie, wholeTable);
      }
    };
  }

  protected void couple(KoppelActie koppelActie, boolean wholeTable) {

    setSelectedRecords(wholeTable);
    List<Gebruiker> allSelectedUsers = giveAllSelectedUsers(selectedRecords);

    if (koppelList.get(0) instanceof DocumentRecord) { // gebruikers-documenten koppelen/ontkoppelen

      if (koppelActie == ONTKOPPEL) {
        checkForAllAllowedDocuments(allSelectedUsers);
      } else {
        doCoupleAndUpdateTable(KOPPEL, allSelectedUsers);
      }
    } else {
      doCoupleAndUpdateTable(koppelActie, allSelectedUsers);
    }
  }

  @Override
  protected void koppelActie(KoppelActie koppelActie) {

    new KoppelProcedure(table, koppelActie, type, false) {

      @Override
      public void koppel(KoppelActie koppelActie, boolean wholeTable) {

        KoppelGebruikersPage.this.couple(koppelActie, wholeTable);
      }
    };
  }

  private void addUsersInDir(Record r, List<Gebruiker> userList) {
    Dir dir = (Dir) r.getObject();
    List<Gebruiker> usersInDir = table.getUsersInDir(dir);
    userList.addAll(usersInDir);
  }

  private void addUserToList(Record r, List<Gebruiker> userList) {

    if (r.getObject() instanceof Gebruiker) {

      Gebruiker user = (Gebruiker) r.getObject();
      userList.add(user);
    }
  }

  private void checkForAllAllowedDocuments(List<Gebruiker> allSelectedUsers) {

    List<DocumentRecord> docList = makeDocListFromKoppelList(); // gaan ervan uit dat koppelList alleen docs bevat.
    VerdeelEnTelDocumenten info = new VerdeelEnTelDocumenten(docList);

    if (info.getAantalAllesToegestaan() > 0) {
      giveConfirmationDialog(info, allSelectedUsers);
    } else {
      doCoupleAndUpdateTable(ONTKOPPEL, allSelectedUsers);
    }
  }

  private void doCoupleAndUpdateTable(KoppelActie koppelActie, List<Gebruiker> allSelectedUsers) {

    koppelActieGebruikers(koppelActie, koppelList, allSelectedUsers);

    table.setTableStatus(koppelActie, selectedRecords);
  }

  private List<Gebruiker> giveAllSelectedUsers(List<Record> selectedRecords) {

    List<Gebruiker> userList = new ArrayList<>();

    for (Record r : selectedRecords) {
      if (r.getObject() instanceof Dir) {
        addUsersInDir(r, userList);
      } else {
        addUserToList(r, userList);
      }
    }

    return userList;
  }

  private void giveConfirmationDialog(VerdeelEnTelDocumenten info, List<Gebruiker> allSelectedUsers) {

    String message;
    int numberOfAllAllowedDocs = info.getAantalAllesToegestaan();
    final List<Gebruiker> selectedUsers = allSelectedUsers;

    if (numberOfAllAllowedDocs == 1) {
      message = "Er is 1 document geselecteerd met de optie <br>"
          + "'iedereen toegang'. Weet u zeker dat u dit <br> ongedaan wilt maken?";
    } else {
      message = "Er zijn " + numberOfAllAllowedDocs + " documenten geselecteerd met de "
          + "<br> optie 'iedereeen toegang'. Weet u zeker dat u dit ongedaan wilt maken?";
    }

    table.getWindow().addWindow(
        new AllesKoppelenAanDocumentenDialog(table, info, selectedUsers, selectedRecords, message) {

          @Override
          public void unlink(List<DocumentRecord> nonAllAllowedDocs) {

            KoppelGebruikersPage.this.koppelActieGebruikers(ONTKOPPEL, nonAllAllowedDocs, selectedUsers);
          }
        });
  }

  private List<DocumentRecord> makeDocListFromKoppelList() {

    List<DocumentRecord> docList = new ArrayList<>();

    for (K coupleObject : koppelList) {
      if (coupleObject instanceof DocumentRecord) {
        DocumentRecord doc = (DocumentRecord) coupleObject;
        docList.add(doc);
      }
    }

    return docList;
  }

  private void setSelectedRecords(boolean wholeTable) {

    if (wholeTable) {
      selectedRecords = table.getRecords();
    } else {
      selectedRecords = table.getSelectedRecords();
    }
  }

  private void setTable() {
    table = new KoppelGebruikersTable<>(koppelList);
  }

  /**
   * bevat een check voor het koppelen/ontkoppelen van all-allowed documenten
   */
  private class KoppelGebruikersTable<D extends KoppelbaarAanGebruiker> extends KoppelGebruikerTabel<D> {

    private KoppelGebruikersTable(D koppelObject) {
      super(koppelObject);
    }

    private KoppelGebruikersTable(List<D> koppelList) {
      super(koppelList);
    }

    @Override
    public void onDoubleClick(Record record) {

      if (record.getObject() instanceof Gebruiker) {
        Gebruiker user = (Gebruiker) record.getObject();
        checkForCouplingDocs(record, user);
      } else {
        super.onDoubleClick(record);
      }
    }

    @Override
    public void setContainerDataSource(Container newDataSource) {

      super.setContainerDataSource(newDataSource);
      form.check(this);
    }

    @Override
    public void setRecordValue(Record record, Object propertyId, Object value) {

      super.setRecordValue(record, propertyId, value);
      form.check(this);
    }

    protected void checkForCouplingDocs(Record record, Gebruiker user) {

      if (koppelList.get(0) instanceof DocumentRecord) {
        coupleUserDocProcedure(record, user);
      } else {
        super.onDoubleClick(record);
      }
    }

    protected void coupleUserDocProcedure(Record record, Gebruiker user) {

      if (user.isGekoppeld(koppelList)) {
        selectedRecords = Arrays.asList(record);
        checkForAllAllowedDocuments(Arrays.asList(user));
      } else {
        super.onDoubleClick(record);
      }
    }
  }
}
