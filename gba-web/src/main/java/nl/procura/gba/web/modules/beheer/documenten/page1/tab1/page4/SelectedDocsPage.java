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

package nl.procura.gba.web.modules.beheer.documenten.page1.tab1.page4;

import static nl.procura.gba.common.MiscUtils.setClass;

import java.io.File;
import java.util.List;
import java.util.Set;

import nl.procura.gba.common.MiscUtils;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.beheer.KoppelbaarAanDocumentType;
import nl.procura.gba.web.services.zaken.documenten.DocumentRecord;
import nl.procura.gba.web.services.zaken.documenten.DocumentService;
import nl.procura.gba.web.services.zaken.documenten.KoppelbaarAanDocument;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.commons.core.exceptions.ProExceptionSeverity;
import nl.procura.commons.core.exceptions.ProExceptionType;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.LoadPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;

public class SelectedDocsPage extends NormalPageTemplate {

  private final int                       MAX_TO_SHOW = 3;
  private final KoppelbaarAanDocumentType type;
  private final String                    PAGE_ERROR  = "Er is iets fout gegaan bij het tonen van de gekoppelde objecten.";
  private GbaTable                        table       = null;
  private final List<DocumentRecord>      docList;

  public SelectedDocsPage(List<DocumentRecord> docList, KoppelbaarAanDocumentType type) {
    this.docList = docList;
    this.type = type;
    setMargin(false);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      table = new GbaTable() {

        @Override
        public void setColumns() {

          setSelectable(true);
          setMultiSelect(true);

          addColumn("Naam");
          addColumn("Sjabloon").setUseHTML(true);
          addColumn("Type");
          addColumn("Aantal");
          addColumn(type);
        }

        private void addColumn(KoppelbaarAanDocumentType type) {

          if (KoppelbaarAanDocumentType.GEBRUIKER.equals(type)) {
            addColumn("Gekoppelde gebruikers");
          } else if (KoppelbaarAanDocumentType.PRINTOPTIE.equals(type)) {
            addColumn("Gekoppelde printopties");
          }
        }
      };

      addComponent(table);
    } else if (event.isEvent(LoadPage.class)) {

      table.getRecords().clear();

      for (DocumentRecord doc : docList) {

        Set<? extends KoppelbaarAanDocument> coupledObjects = getCoupledObjects(doc);
        String sjabloonMetPad = doc.getBestand();
        Record r = table.addRecord(doc);

        r.addValue(doc.getDocument());
        r.addValue(sjabloonBestaat(sjabloonMetPad) ? sjabloonMetPad
            : setClass("red",
                sjabloonMetPad + " (Dit sjabloon kan niet gevonden worden)"));

        r.addValue(doc.getDocumentType());
        r.addValue(getNumberOfCoupledObjects(doc, coupledObjects));
        r.addValue(showCoupledObjects(doc, coupledObjects));
      }

      table.reloadRecords();
    }

    super.event(event);
  }

  private Set<? extends KoppelbaarAanDocument> getCoupledObjects(DocumentRecord doc) {

    DocumentService dD = getServices().getDocumentService();

    if (KoppelbaarAanDocumentType.GEBRUIKER.equals(type)) {
      return dD.getDocumentGebruikers(doc);
    } else if (KoppelbaarAanDocumentType.PRINTOPTIE.equals(type)) {
      return dD.getDocumentPrintopties(doc);
    } else if (KoppelbaarAanDocumentType.STEMPEL.equals(type)) {
      return dD.getDocumentStempels(doc);
    } else if (KoppelbaarAanDocumentType.KENMERK.equals(type)) {
      return dD.getDocumentKenmerken(doc);
    }

    throw new ProException(ProExceptionType.PROGRAMMING, ProExceptionSeverity.ERROR, PAGE_ERROR);
  }

  private int getNumberOfCoupledObjects(DocumentRecord doc, Set<? extends KoppelbaarAanDocument> coupledObjects) {

    if (KoppelbaarAanDocumentType.GEBRUIKER.equals(type)) {
      return getNumberOfCoupledUsers(doc, coupledObjects);
    } else if (KoppelbaarAanDocumentType.PRINTOPTIE.equals(type)) {
      return coupledObjects.size();
    } else if (KoppelbaarAanDocumentType.STEMPEL.equals(type)) {
      return coupledObjects.size();
    } else if (KoppelbaarAanDocumentType.KENMERK.equals(type)) {
      return coupledObjects.size();
    }

    throw new ProException(ProExceptionType.PROGRAMMING, ProExceptionSeverity.ERROR, PAGE_ERROR);
  }

  private int getNumberOfCoupledUsers(DocumentRecord doc, Set<? extends KoppelbaarAanDocument> coupledUsers) {

    if (doc.isIedereenToegang()) { // alle gebruikers gekoppeld
      return getServices().getGebruikerService().getGebruikers(false).size();
    }

    return coupledUsers.size();
  }

  private String showCoupledObjects(DocumentRecord doc, Set<? extends KoppelbaarAanDocument> coupledObjects) {

    if (KoppelbaarAanDocumentType.GEBRUIKER.equals(type)) {
      return showCoupledUsers(doc, coupledObjects);
    } else if (KoppelbaarAanDocumentType.PRINTOPTIE.equals(type)) {
      return MiscUtils.abbreviate(coupledObjects, MAX_TO_SHOW);
    } else if (KoppelbaarAanDocumentType.STEMPEL.equals(type)) {
      return MiscUtils.abbreviate(coupledObjects, MAX_TO_SHOW);
    } else if (KoppelbaarAanDocumentType.KENMERK.equals(type)) {
      return MiscUtils.abbreviate(coupledObjects, MAX_TO_SHOW);
    }

    throw new ProException(ProExceptionType.PROGRAMMING, ProExceptionSeverity.ERROR, PAGE_ERROR);
  }

  private String showCoupledUsers(DocumentRecord doc, Set<? extends KoppelbaarAanDocument> coupledObjects) {

    if (doc.isIedereenToegang()) {
      return "iedereen";
    }

    return MiscUtils.abbreviate(coupledObjects, MAX_TO_SHOW);
  }

  private boolean sjabloonBestaat(String sjabloonMetPad) {
    return new File(getServices().getDocumentService().getSjablonenMap(), sjabloonMetPad).exists();
  }
}
