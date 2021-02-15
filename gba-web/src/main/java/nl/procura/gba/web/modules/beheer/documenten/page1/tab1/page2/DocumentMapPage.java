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

package nl.procura.gba.web.modules.beheer.documenten.page1.tab1.page2;

import static nl.procura.gba.common.MiscUtils.cleanPath;
import static nl.procura.gba.web.modules.beheer.documenten.page1.tab1.page2.DocumentMapBean.MAP;

import java.util.List;

import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.modules.beheer.documenten.page1.tab1.page1.Tab1DocumentenPage1;
import nl.procura.gba.web.modules.beheer.overig.CheckPadEnOpslaanDocument;
import nl.procura.gba.web.modules.beheer.overig.TabelToonType;
import nl.procura.gba.web.services.zaken.documenten.DocumentRecord;

public class DocumentMapPage extends NormalPageTemplate {

  private final List<DocumentRecord> docList;
  private final DocumentMapForm      form;
  private final String               commonDocPath;
  private final TabelToonType        toonType;

  public DocumentMapPage(List<DocumentRecord> docList, TabelToonType toonType) {

    super("Documenten: indelen in mappen");
    setMargin(true);
    addButton(buttonPrev, buttonSave);

    this.docList = docList;
    form = new DocumentMapForm(docList);
    addComponent(form);
    commonDocPath = (String) form.getField(MAP).getValue();

    this.toonType = toonType;
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }

  @Override
  public void onSave() {
    form.commit();

    String cleanedPath = cleanPath(form.getBean().getMap());

    new CheckPadEnOpslaanDocument(cleanedPath, null, form) { // bean wordt niet gebruikt in dit geval

      @Override
      protected void nietOpslaanDocumentActions() {
        form.setDocumentmapContainer();
        form.getField(MAP).setValue(commonDocPath);
      }

      @Override
      protected void opslaanDocument(String docPath, Tab1DocumentenPage2Bean b) {
        saveDocuments(docPath);
        showRelevantPage(docPath);
      }

      @Override
      protected void welOpslaanDocumentActies(String docPath, Tab1DocumentenPage2Bean b) {
        saveDocuments(docPath);
        showRelevantPage(docPath);
      }
    };
  }

  private void saveDocuments(String docPath) {

    for (DocumentRecord doc : docList) {
      doc.setPad(docPath);
    }

    getServices().getDocumentService().save(docList);
  }

  private void showDirPage(String legalPath) {
    getNavigation().goToPage(new Tab1DocumentenPage1(legalPath));
    getNavigation().removeOtherPages();
  }

  private void showRelevantPage(String docPath) {

    if (toonType == TabelToonType.MAPPEN) {
      showDirPage(docPath);
    } else {
      // lijstvorm: we gaan terug naar de vorige pagina.
      onPreviousPage();
    }
  }
}
