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

package nl.procura.gba.web.modules.beheer.documenten.page1.tab1.page1;

import java.util.List;

import nl.procura.gba.web.components.layouts.navigation.GbaPopupButton;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.beheer.documenten.components.DocumentImportExportHandler;
import nl.procura.gba.web.modules.beheer.documenten.page1.tab1.page2.AandVertrouwPage;
import nl.procura.gba.web.modules.beheer.documenten.page1.tab1.page2.DocumentMapPage;
import nl.procura.gba.web.modules.beheer.documenten.page1.tab1.page2.DocumentTypePage;
import nl.procura.gba.web.modules.beheer.documenten.page1.tab1.page2.StillbornPage;
import nl.procura.gba.web.modules.beheer.documenten.page1.tab1.page2.importing.ImportDocPage;
import nl.procura.gba.web.modules.beheer.documenten.page1.tab1.page3.Tab1DocumentenPage3;
import nl.procura.gba.web.modules.beheer.documenten.page1.tab1.page4.Tab1DocumentenPage4;
import nl.procura.gba.web.modules.beheer.documenten.page1.tab6.page3.Tab6DocumentenPage3;
import nl.procura.gba.web.modules.beheer.documenten.page1.tab7.page3.Tab7DocumentenPage3;
import nl.procura.gba.web.modules.beheer.overig.TabelToonType;
import nl.procura.gba.web.modules.beheer.overig.TableSelectionCheck;
import nl.procura.gba.web.services.zaken.documenten.DocumentRecord;
import nl.procura.gba.web.windows.GbaWindow;
import nl.procura.vaadin.component.layout.page.PageNavigation;

public class Tab1DocumentenPopup extends GbaPopupButton {

  public Tab1DocumentenPopup(final GbaTable table, final TabelToonType toonType) {

    super("Opties", "150px", "145px");

    addChoice(new Choice("Map") {

      @Override
      public void onClick() {

        TableSelectionCheck.checkSelection(table);
        List<DocumentRecord> allSelectedDocs = getAllSelectedDocs();

        if (!isParentMapOnlySelectedRec(allSelectedDocs)) {
          getNavigation().goToPage(new DocumentMapPage(allSelectedDocs, toonType));
        }
      }
    });

    addChoice(new Choice("Type") {

      @Override
      public void onClick() {

        TableSelectionCheck.checkSelection(table);
        List<DocumentRecord> allSelectedDocs = getAllSelectedDocs();

        if (!isParentMapOnlySelectedRec(allSelectedDocs)) {
          getNavigation().goToPage(new DocumentTypePage(allSelectedDocs));
        }
      }
    });

    addChoice(new Choice("Vertrouwelijkheid") {

      @Override
      public void onClick() {

        TableSelectionCheck.checkSelection(table);
        List<DocumentRecord> allSelectedDocs = getAllSelectedDocs();

        if (!isParentMapOnlySelectedRec(allSelectedDocs)) {
          getNavigation().goToPage(new AandVertrouwPage(allSelectedDocs));
        }
      }
    });

    addChoice(new Choice("Levenloos geboren") {

      @Override
      public void onClick() {

        TableSelectionCheck.checkSelection(table);
        List<DocumentRecord> allSelectedDocs = getAllSelectedDocs();

        if (!isParentMapOnlySelectedRec(allSelectedDocs)) {
          getNavigation().goToPage(new StillbornPage(allSelectedDocs));
        }
      }
    });

    addChoice(new Choice("Importeren") {

      @Override
      public void onClick() {
        getNavigation().goToPage(new ImportDocPage());
      }
    });

    addChoice(new Choice("Exporteren") {

      @Override
      public void onClick() {

        TableSelectionCheck.checkSelection(table);
        List<DocumentRecord> allSelectedDocs = getAllSelectedDocs();

        if (!isParentMapOnlySelectedRec(allSelectedDocs)) {
          new DocumentImportExportHandler().exportDocumenten((GbaWindow) getWindow(), allSelectedDocs);
        }
      }
    });

    addChoice(new Choice("Printers koppelen") {

      @Override
      public void onClick() {

        TableSelectionCheck.checkSelection(table);
        List<DocumentRecord> allSelectedDocs = getAllSelectedDocs();

        if (!isParentMapOnlySelectedRec(allSelectedDocs)) {
          getNavigation().goToPage(new Tab1DocumentenPage3(allSelectedDocs));
        }
      }
    });

    addChoice(new Choice("Stempels koppelen") {

      @Override
      public void onClick() {

        TableSelectionCheck.checkSelection(table);
        List<DocumentRecord> allSelectedDocs = getAllSelectedDocs();

        if (!isParentMapOnlySelectedRec(allSelectedDocs)) {
          getNavigation().goToPage(new Tab6DocumentenPage3(allSelectedDocs));
        }
      }
    });

    addChoice(new Choice("Kenmerken koppelen") {

      @Override
      public void onClick() {

        TableSelectionCheck.checkSelection(table);
        List<DocumentRecord> allSelectedDocs = getAllSelectedDocs();

        if (!isParentMapOnlySelectedRec(allSelectedDocs)) {
          getNavigation().goToPage(new Tab7DocumentenPage3(allSelectedDocs));
        }
      }
    });

    addChoice(new Choice("Gebruikers koppelen") {

      @Override
      public void onClick() {

        TableSelectionCheck.checkSelection(table);
        List<DocumentRecord> allSelectedDocs = getAllSelectedDocs();

        if (!isParentMapOnlySelectedRec(allSelectedDocs)) {
          getNavigation().goToPage(new Tab1DocumentenPage4(allSelectedDocs));
        }
      }
    });
  }

  protected List<DocumentRecord> getAllSelectedDocs() {
    return null;
  }

  protected PageNavigation getNavigation() {
    return null;
  }

  @SuppressWarnings("unused")
  protected boolean isParentMapOnlySelectedRec(List<DocumentRecord> allSelectedDocs) { // Override
    return false;
  }
}
