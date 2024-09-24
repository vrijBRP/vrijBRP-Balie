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

package nl.procura.gba.web.modules.zaken.common;

import org.apache.commons.lang3.ArrayUtils;

import com.vaadin.ui.Button;

import nl.procura.gba.web.components.layouts.form.document.PrintMultiLayout;
import nl.procura.gba.web.components.layouts.form.document.PrintSelectRecordFilter;
import nl.procura.gba.web.services.bs.algemeen.ZaakDossier;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.documenten.DocumentRecord;
import nl.procura.gba.web.services.zaken.documenten.DocumentType;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class ZakenPrintPage<M, Z extends Zaak> extends ZakenPage<Z> {

  private final PrintMultiLayout printLayout;

  public ZakenPrintPage(String title, Object model, Zaak zaak, DocumentType... types) {

    super(zaak, title);

    // Voeg Document.ZAAK toe
    DocumentType[] newTypes = types;
    if ((model instanceof Zaak) || (model instanceof ZaakDossier)) {
      newTypes = ArrayUtils.add(types, DocumentType.ZAAK);
    }

    printLayout = new PrintMultiLayout(model, zaak, new DocumentSelectListener(), newTypes);
    setButtons();
    addExpandComponent(printLayout);
  }

  @Override
  public void event(PageEvent event) {
    if (event.isEvent(InitPage.class)) {
      if (getZaak() != null) {
        getServices().getZakenService().getService(getZaak()).setVolledigeZaakExtra(getZaak());
      }
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

  @SuppressWarnings("unused")
  public boolean onSelectDocument(DocumentRecord document, boolean isPreSelect) {
    return isPreSelect;
  }

  protected M getModel() {
    return (M) printLayout.getModel();
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
