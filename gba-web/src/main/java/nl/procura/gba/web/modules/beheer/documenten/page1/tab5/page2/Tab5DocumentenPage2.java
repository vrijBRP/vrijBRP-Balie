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

package nl.procura.gba.web.modules.beheer.documenten.page1.tab5.page2;

import static nl.procura.standard.Globalfunctions.astr;

import com.vaadin.ui.Button;

import nl.procura.gba.web.components.layouts.OptieLayout;
import nl.procura.gba.web.modules.beheer.documenten.DocumentenTabPage;
import nl.procura.gba.web.modules.beheer.documenten.page1.tab5.PrinterContainer;
import nl.procura.gba.web.modules.beheer.documenten.page1.tab5.page3.Tab5DocumentenPage3;
import nl.procura.gba.web.modules.beheer.documenten.page1.tab5.page4.CoupleLocsToPrintOptions;
import nl.procura.gba.web.services.zaken.documenten.printopties.PrintOptie;
import nl.procura.vaadin.component.layout.info.InfoLayout;
import nl.procura.vaadin.theme.twee.ProcuraTheme;

public class Tab5DocumentenPage2 extends DocumentenTabPage {

  private final Button buttonDoc = new Button("Documenten koppelen");
  private final Button buttonLoc = new Button("Locaties koppelen");

  private PrintOptie printOptie;
  private boolean    isPrinterAanwezig;

  private Tab5DocumentenPage2Form form        = null;
  private PrinterContainer        printers    = null;
  private InfoLayout              infoMelding = null;

  public Tab5DocumentenPage2(PrintOptie printOptie) {
    this(printOptie, false);
  }

  public Tab5DocumentenPage2(PrintOptie printOptie, boolean isPrinterAanwezig) {
    super("Toevoegen / muteren printoptie");
    this.printOptie = printOptie;
    this.isPrinterAanwezig = isPrinterAanwezig;
    setMargin(true);
  }

  @Override
  protected void initPage() {
    printers = new PrinterContainer(getServices());

    addButton(buttonPrev, buttonNew, buttonSave);
    setForm(new Tab5DocumentenPage2Form(printers, printOptie));
    checkPrinter(printOptie, isPrinterAanwezig);

    OptieLayout ol = new OptieLayout();
    ol.getLeft().addComponent(getForm());

    ol.getRight().setWidth("200px");
    ol.getRight().setCaption("Opties");

    ol.getRight().addButton(buttonDoc, this);
    ol.getRight().addButton(buttonLoc, this);
    addComponent(ol);

    super.initPage();
  }

  public Tab5DocumentenPage2Form getForm() {
    return form;
  }

  public void setForm(Tab5DocumentenPage2Form form) {
    this.form = form;
  }

  public PrintOptie getPrintOptie() {
    return printOptie;
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if (button == buttonDoc) {
      getNavigation().goToPage(new Tab5DocumentenPage3(printOptie));
    }

    if (button == buttonLoc) {
      getNavigation().goToPage(new CoupleLocsToPrintOptions(printOptie));
    }

    super.handleEvent(button, keyCode);
  }

  @Override
  public void onNew() {
    getForm().reset();
    this.printOptie = new PrintOptie();
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }

  @Override
  public void onSave() {

    getForm().commit();

    Tab5DocumentenPage2Bean b = getForm().getBean();
    String printerDriver = astr(b.getPrinter().getValue());

    getPrintOptie().setOms(b.getName());
    getPrintOptie().setPrintoptie(printerDriver);
    getPrintOptie().setLocatie(b.getLocation());
    getPrintOptie().setType(b.getPrinterType().getCode());
    getPrintOptie().setCmd("");
    getPrintOptie().setMedia("");
    getPrintOptie().setKleur("");
    getPrintOptie().setOrientatie("");
    getPrintOptie().setZijde("");
    getPrintOptie().setBsmId("");
    getPrintOptie().setMoBerichttype("");

    switch (b.getPrinterType()) {
      case COMMAND:
        getPrintOptie().setCmd(b.getCommand());
        break;
      case LOCAL_PRINTER:
      case NETWORK_PRINTER:
        getPrintOptie().setMedia(astr(b.getMedia()));
        getPrintOptie().setKleur(astr(b.getColor()));
        break;
      case MIJN_OVERHEID:
        getPrintOptie().setBsmId(b.getBsmHashcode());
        getPrintOptie().setMoBerichttype(b.getMoBerichtType());
        break;
      case POPUP:
        break;
    }

    getServices().getPrintOptieService().save(getPrintOptie());
    successMessage("Printoptie is opgeslagen.");
  }

  private void checkPrinter(PrintOptie printOptie, boolean isPrinterAanwezig) {
    if (printOptie.isStored()) {
      if (!isPrinterAanwezig) {
        infoMelding = new InfoLayout("Ter informatie", ProcuraTheme.ICOON_24.WARNING,
            "Foutieve printoptie: de opgeslagen printer kan niet gevonden worden."
                + " Zie het veld 'Printer driver' voor mogelijke opties.");
        addComponent(infoMelding);
      }
    }
  }
}
