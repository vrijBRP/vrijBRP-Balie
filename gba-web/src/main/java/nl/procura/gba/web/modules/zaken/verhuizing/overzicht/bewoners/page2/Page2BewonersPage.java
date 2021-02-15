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

package nl.procura.gba.web.modules.zaken.verhuizing.overzicht.bewoners.page2;

import static nl.procura.diensten.gba.ple.openoffice.DocumentPLConverter.convert;
import static nl.procura.diensten.gba.ple.openoffice.DocumentPLConverter.removeStillborns;
import static nl.procura.gba.web.services.zaken.documenten.DocumentType.PL_BEWONER_BRIEF;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.ui.Button;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.openoffice.DocumentPL;
import nl.procura.gba.web.components.layouts.form.document.PrintMultiLayout;
import nl.procura.gba.web.components.layouts.form.document.PrintRecord;
import nl.procura.gba.web.components.layouts.page.ButtonPageTemplate;
import nl.procura.gba.web.services.bs.algemeen.ZaakDossier;
import nl.procura.gba.web.services.zaken.documenten.DocumentRecord;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page2BewonersPage extends ButtonPageTemplate {

  private final PrintMultiLayout printLayout;
  private final List<BasePLExt>  persoonslijsten;

  public Page2BewonersPage(final List<BasePLExt> persoonslijsten) {

    this.persoonslijsten = persoonslijsten;

    printLayout = new PrintMultiLayout(null, null, null, PL_BEWONER_BRIEF) {

      @Override
      protected List<PrintRecord> getPrintRecords(boolean isPreview) {

        // Vertaal de persoonslijsten naar een PrintRecord

        List<PrintRecord> printRecords = new ArrayList<>();

        for (PrintRecord printRecord : super.getPrintRecords(isPreview)) {

          DocumentRecord document = printRecord.getDocument();
          List<DocumentPL> dps = convert(persoonslijsten, null);
          dps.stream().filter(dp -> !document.isStillbornAllowed()).forEach(dpl -> removeStillborns(dpl));

          PrintRecord newPrintRecord = new PrintRecord();
          newPrintRecord.setSoort(printRecord.getSoort());
          newPrintRecord.setDocument(document);
          newPrintRecord.setUitvoer(printRecord.getUitvoer());
          newPrintRecord.setModel(dps);
          printRecords.add(newPrintRecord);
        }

        return printRecords;
      }
    };

    addButton(buttonPrev);

    addButton(getPrintButtons());

    addComponent(printLayout);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {
      printLayout.setInfo(getInfo());
    }

    super.event(event);
  }

  public Button[] getPrintButtons() {
    return printLayout.getButtons();
  }

  public PrintMultiLayout getPrintLayout() {
    return printLayout;
  }

  @Override
  public void handleEvent(Button button, int keyCode) {
    printLayout.handleActions(button, keyCode);
    super.handleEvent(button, keyCode);
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }

  protected Object getModel() {
    return printLayout.getModel();
  }

  protected void setModel(ZaakDossier zaakDossier) {
    getPrintLayout().setModel(zaakDossier);
    getPrintLayout().setZaak(zaakDossier.getDossier());
  }

  protected void setButtons() {
  }

  private String getInfo() {

    StringBuilder info = new StringBuilder();

    info.append("<b>");

    switch (persoonslijsten.size()) {
      case 1:
        info.append("1 persoon geselecteerd.");
        break;

      default:
        info.append(persoonslijsten.size());
        info.append(" personen geselecteerd.");
        break;
    }

    info.append("</b><br/>Controleer de gegevens op het document nauwkeurig voordat u het document uitreikt.");

    return info.toString();
  }
}
