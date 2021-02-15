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

import static java.text.MessageFormat.format;
import static nl.procura.gba.web.services.beheer.KoppelActie.KOPPEL;
import static nl.procura.gba.web.services.beheer.KoppelActie.ONTKOPPEL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.vaadin.data.Container;

import nl.procura.gba.web.components.dialogs.KoppelProcedure;
import nl.procura.gba.web.modules.beheer.overig.KoppelForm;
import nl.procura.gba.web.modules.beheer.overig.KoppelPage;
import nl.procura.gba.web.modules.beheer.overig.NavigationSorter.Dir;
import nl.procura.gba.web.services.beheer.KoppelActie;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.gba.web.services.zaken.documenten.DocumentRecord;
import nl.procura.gba.web.services.zaken.documenten.KoppelbaarAanDocument;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;

public class KoppelDocumentPage<K extends KoppelbaarAanDocument> extends KoppelPage {

  private KoppelDocumentTabel<K> table           = null;
  private List<K>                koppelList;
  private final KoppelForm       form;
  private final String           type;                                // dit is het type waar aan gekoppeld wordt
  private List<Record>           selectedRecords = new ArrayList<>(); // wordt verkregen via CoupleProcedure; bij

  // 'alles koppelen' is dit bv. de hele tabel.

  @SuppressWarnings("unchecked")
  public KoppelDocumentPage(K koppelObject, String type) {
    this(Arrays.asList(koppelObject), type);
  }

  public KoppelDocumentPage(List<K> koppelList, String type) { // type is gebruikers of printopties

    super("");
    setMargin(false);

    form = new KoppelForm();
    this.type = type;
    this.koppelList = koppelList;

    setTable();
    this.koppelList = koppelList;

    setInfo("De status van een map is 'Gekoppeld' precies dan als <b>elk</b> document in de map gekoppeld is aan de "
        + "geselecteerde " + type + "." + "<br>" + "Klik één keer op de regel om deze te selecteren."
        + " Wijzig de status door te dubbelklikken op een document of door de knoppen te gebruiken in geval van een map.");

    addComponent(form);
    addComponent(table.getDirectoryLayout());
    addExpandComponent(table);
  }

  /**
   * Nodig als deze pagina ergens ingevoegd wordt waar al een 'vorige' knop aanwezig is.
   */

  public void disablePreviousButton() {
    buttonPrev.setVisible(false);
  }

  @Override
  protected void allesKoppelActie(KoppelActie koppelActie) {

    new KoppelProcedure(table, koppelActie, type, true) {

      @Override
      public void koppel(KoppelActie koppelActie, boolean wholeTable) {

        KoppelDocumentPage.this.couple(koppelActie, wholeTable);
      }
    };
  }

  @Override
  protected void koppelActie(KoppelActie koppelActie) {

    new KoppelProcedure(table, koppelActie, type, false) {

      @Override
      public void koppel(KoppelActie koppelActie, boolean wholeTable) {

        KoppelDocumentPage.this.couple(koppelActie, wholeTable);
      }
    };
  }

  private void addDocToList(Record r, List<DocumentRecord> docList) {

    if (r.getObject() instanceof DocumentRecord) {
      DocumentRecord doc = (DocumentRecord) r.getObject();
      docList.add(doc);
    }
  }

  private void checkForAllAllowedDocuments(DocumentRecord doc) {
    List<DocumentRecord> docAsList = Arrays.asList(doc);
    checkForAllAllowedDocuments(docAsList);
  }

  private void checkForAllAllowedDocuments(List<DocumentRecord> documenten) {

    List<Gebruiker> gebruikers = getGebruikersLijst();
    VerdeelEnTelDocumenten info = new VerdeelEnTelDocumenten(documenten);

    if (info.getAantalAllesToegestaan() > 0) {
      geefBevestigingDialog(info, gebruikers);
    } else {
      koppelEnUpdateTabel(ONTKOPPEL, documenten);
    }
  }

  private void couple(KoppelActie koppelActie, boolean wholeTable) {

    setSelectedRecords(wholeTable);
    List<DocumentRecord> documenten = getGeselecteerdeDocumenten(selectedRecords);

    if (koppelList.get(0) instanceof Gebruiker) {
      coupleDocsUsersProcedure(documenten, koppelActie);
    } else {
      koppelEnUpdateTabel(koppelActie, documenten);
    }
  }

  private void coupleDocsUsersProcedure(List<DocumentRecord> allSelectedDocs, KoppelActie koppelActie) {

    if (koppelActie == KOPPEL) {
      koppelEnUpdateTabel(KOPPEL, allSelectedDocs);
    } else {
      checkForAllAllowedDocuments(allSelectedDocs);
    }
  }

  private void geefBevestigingDialog(VerdeelEnTelDocumenten info, final List<Gebruiker> gebruikers) {

    String message;
    int aantal = info.getAantalAllesToegestaan();

    if (aantal == 1) {
      message = "Er is 1 document geselecteerd met de optie 'iedereen toegang'. Weet u zeker <br/> dat u dit ongedaan wilt maken?";
    } else {
      message = format(
          "Er zijn {0} documenten geselecteerd met de <br/> optie ''iedereen toegang''. "
              + "Weet u zeker dat u dit <br/> ongedaan wilt maken?",
          aantal);
    }

    table.getWindow().addWindow(
        new AllesKoppelenAanDocumentenDialog(table, info, gebruikers, selectedRecords, message) {

          @Override
          public void unlink(List<DocumentRecord> nonAllAllowedDocs) {
            KoppelDocumentPage.this.koppelActieDocumenten(ONTKOPPEL, gebruikers, nonAllAllowedDocs);
          }
        });
  }

  private List<Gebruiker> getGebruikersLijst() {
    List<Gebruiker> userList = new ArrayList<>();

    for (K koppelObject : koppelList) {
      if (koppelObject instanceof Gebruiker) {
        Gebruiker user = (Gebruiker) koppelObject;
        userList.add(user);
      }
    }

    return userList;
  }

  private List<DocumentRecord> getGeselecteerdeDocumenten(List<Record> selectedRecords) {
    List<DocumentRecord> docList = new ArrayList<>();

    for (Record r : selectedRecords) {
      if (r.getObject() instanceof Dir) {
        voegDocumentToe(r, docList);
      } else {
        addDocToList(r, docList);
      }
    }

    return docList;
  }

  private void koppelEnUpdateTabel(KoppelActie koppelActie, List<DocumentRecord> allSelectedDocuments) {

    koppelActieDocumenten(koppelActie, koppelList, allSelectedDocuments);
    table.setTableStatus(koppelActie, selectedRecords);
  }

  private void setSelectedRecords(boolean wholeTable) {

    if (wholeTable) {
      selectedRecords = table.getRecords();
    } else {
      selectedRecords = table.getSelectedRecords();
    }
  }

  private void setTable() {

    table = new KoppelDocumentenTabel<>(koppelList);
  }

  private void voegDocumentToe(Record record, List<DocumentRecord> docList) {
    Dir dir = record.getObject(Dir.class);
    List<DocumentRecord> docsInDir = table.getDocumentenInMap(dir);
    docList.addAll(docsInDir);
  }

  /**
   * Bevat een check voor het koppelen van gebruikers.
   * Dit is van belang bij 'all-allowed documenten'.
   *
  
   * <p>
   * 2012
   */
  private final class KoppelDocumentenTabel<D extends KoppelbaarAanDocument> extends KoppelDocumentTabel<D> {

    private KoppelDocumentenTabel(List<D> koppelList) {
      super(koppelList);
    }

    @Override
    public void onDoubleClick(Record record) { // check voor docs-gebruikers koppelen

      if (record.getObject() instanceof DocumentRecord) {
        DocumentRecord doc = (DocumentRecord) record.getObject();
        checkForUsers(doc, record);
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

    private void checkForUsers(DocumentRecord doc, Record record) {

      if (koppelList.get(0) instanceof Gebruiker) { // gebruikers-docs koppelen
        if (doc.isGekoppeld(koppelList)) {
          selectedRecords = Arrays.asList(record);
          checkForAllAllowedDocuments(doc);
        } else {
          super.onDoubleClick(record);
        }
      } else {
        super.onDoubleClick(record);
      }
    }
  }
}
