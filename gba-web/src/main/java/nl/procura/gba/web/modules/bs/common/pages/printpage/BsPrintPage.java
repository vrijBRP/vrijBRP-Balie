/*
 * Copyright 2024 - 2025 Procura B.V.
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

package nl.procura.gba.web.modules.bs.common.pages.printpage;

import com.vaadin.ui.Button;

import nl.procura.gba.web.components.layouts.form.document.PrintMultiLayout;
import nl.procura.gba.web.components.layouts.form.document.PrintSelectRecordFilter;
import nl.procura.gba.web.modules.bs.common.pages.BsPage;
import nl.procura.gba.web.modules.bs.common.utils.DossierKennisgevingSituaties;
import nl.procura.gba.web.services.bs.algemeen.ZaakDossier;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.documenten.DocumentRecord;
import nl.procura.gba.web.services.zaken.documenten.DocumentType;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public abstract class BsPrintPage<T extends ZaakDossier> extends BsPage<T> {

  private PrintMultiLayout printLayout;
  private Object           model;
  private Zaak             zaak = null;

  public BsPrintPage(String title, Object model) {

    super(title);

    this.model = model;
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      printLayout = new PrintMultiLayout(model, zaak, new DocumentSelectListener(), getDocumentTypes());
      printLayout.setInfo(getInfo());

      setButtons();

      addExpandComponent(printLayout);
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

  public boolean onSelectDocument(DocumentRecord document, boolean isPreSelect) {

    if (getModel() instanceof ZaakDossier) {

      ZaakDossier zaakDossier = (ZaakDossier) getModel();
      String doc = document.getDocument().toLowerCase();

      if (doc.contains("kennisgeving") && doc.contains("andere")) {
        return DossierKennisgevingSituaties.isKennisgeving(getApplication(), zaakDossier.getDossier());
      }
    }

    return isPreSelect;
  }

  protected abstract DocumentType[] getDocumentTypes();

  protected String getInfo() {
    return "Controleer de akte, druk deze en eventuele andere documenten af en draag zorg voor de ondertekening. " +
        "Daarna kunt u het proces voltooien (F2)";
  }

  protected Object getModel() {
    return printLayout.getModel();
  }

  protected void setModel(ZaakDossier zaakDossier) {
    this.model = zaakDossier;
    this.zaak = zaakDossier.getDossier();
  }

  protected void setModel(Object model, ZaakDossier zaakDossier) {
    this.model = model;
    this.zaak = zaakDossier.getDossier();
  }

  protected boolean isDoc(String doc, String... regexes) {
    for (String regex : regexes) {
      if (!doc.contains(regex)) {
        return false;
      }
    }
    return true;
  }

  protected void setButtons() {
  }

  private class DocumentSelectListener implements PrintSelectRecordFilter {

    @Override
    public boolean select(DocumentRecord document, boolean isPreSelect) {
      return onSelectDocument(document, isPreSelect);
    }
  }
}
