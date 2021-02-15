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

package nl.procura.gba.web.modules.zaken.document.page2;

import static nl.procura.gba.web.services.zaken.documenten.DocumentType.*;
import static nl.procura.standard.Globalfunctions.fil;

import nl.procura.gba.web.modules.zaken.common.ZakenListPage;
import nl.procura.gba.web.modules.zaken.common.ZakenListTable;
import nl.procura.gba.web.modules.zaken.common.ZakenListTable.ZaakRecord;
import nl.procura.gba.web.modules.zaken.document.DocumentenTab;
import nl.procura.gba.web.modules.zaken.document.page6.Page6Document;
import nl.procura.gba.web.services.beheer.profiel.actie.ProfielActie;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.gba.web.services.zaken.algemeen.ZaakUtils;
import nl.procura.gba.web.services.zaken.documenten.DocumentType;
import nl.procura.gba.web.services.zaken.documenten.aanvragen.DocumentZaak;
import nl.procura.gba.web.services.zaken.documenten.aanvragen.DocumentZaakArgumenten;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;
import nl.procura.vaadin.functies.VaadinUtils;

public class Page2Document extends ZakenListPage<DocumentZaak> {

  public Page2Document() {
    super("Documenten: afgedrukte documenten");
    setMargin(true);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      // Als je geen document mag verwijderen dan button uitzetten.

      if (getApplication().isProfielActie(ProfielActie.UPDATE_ZAAK_DOCUMENT)) {
        addButton(buttonStatus);
      }

      if (getApplication().isProfielActie(ProfielActie.DELETE_ZAAK_DOCUMENT)) {
        addButton(buttonDel);
      } else {
        buttonDel.setVisible(false);
      }
    }

    super.event(event);
  }

  @Override
  protected ZaakArgumenten getZaakArgumenten() {
    DocumentZaakArgumenten zaakArgumenten = new DocumentZaakArgumenten(getPl());
    zaakArgumenten.setDocumentTypes(PL_FORMULIER, PL_NATURALISATIE, PL_OPTIE, PL_ADRESONDERZOEK);
    return zaakArgumenten;
  }

  @Override
  protected void setTableColumns(ZakenListTable table) {
    table.addColumn("Nr", 30);
    table.addColumn("Ingevoerd op", 130);
    table.addColumn("Status", 120).setUseHTML(true);
    table.addColumn("Ingevoerd door", 200);
    table.addColumn("Soort", 150);
    table.addColumn("Document");
  }

  @Override
  protected void selectTableRecord(ZaakRecord<DocumentZaak> zaakRecord) {
    getNavigation().goToPage(new Page6Document(zaakRecord.getZaak()));
  }

  @Override
  protected void loadTableRecord(Record record, ZaakRecord<DocumentZaak> zaakRecord) {
    DocumentZaak zaak = zaakRecord.getZaak();
    String subLabel = "";

    if (fil(zaak.getDocumentDoel())) {
      subLabel += " doel: " + zaak.getDocumentDoel();
    }

    if (fil(zaak.getDocumentAfn())) {
      subLabel += fil(subLabel) ? " en " : "";
      subLabel += " afnemer: " + zaak.getDocumentAfn();
    }

    record.getValues().get(0).setValue(zaakRecord.getNr());
    record.getValues().get(1).setValue(zaak.getDatumTijdInvoer());
    record.getValues().get(2).setValue(ZaakUtils.getStatus(zaak.getStatus()));
    record.getValues().get(3).setValue(zaak.getIngevoerdDoor().getDescription());
    record.getValues().get(4).setValue(DocumentType.getType(zaak.getDoc().getType()).getOms());
    record.getValues().get(5).setValue(zaak.getDoc().getDocument() + subLabel);
  }

  @Override
  public void onAfterDelete() {
    reloadCaptions();
  }

  protected void reloadCaptions() {
    VaadinUtils.getParent(this, DocumentenTab.class).reload();
  }
}
