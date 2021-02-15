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

package nl.procura.gba.web.modules.zaken.bs.page1;

import nl.procura.gba.common.ZaakType;
import nl.procura.gba.web.modules.zaken.bs.page2.Page2BsTemplate;
import nl.procura.gba.web.modules.zaken.common.ZakenListPage;
import nl.procura.gba.web.modules.zaken.common.ZakenListTable;
import nl.procura.gba.web.modules.zaken.common.ZakenListTable.ZaakRecord;
import nl.procura.gba.web.services.beheer.profiel.actie.ProfielActie;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.gba.web.services.zaken.algemeen.ZaakUtils;
import nl.procura.gba.web.services.zaken.documenten.DocumentType;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;

/**
 * Overzicht
 */
public abstract class Page1BsTemplate extends ZakenListPage<Dossier> {

  public Page1BsTemplate(String caption) {
    super(caption);
    setMargin(true);
    addButton(buttonPrev);
    addButton(buttonNew);
    addButton(buttonStatus);
    addButton(buttonDel);
  }

  @Override
  protected ZaakArgumenten getZaakArgumenten() {
    return new ZaakArgumenten(getPl(), getZaakTypes());
  }

  @Override
  protected void setTableColumns(ZakenListTable table) {
    table.addColumn("Nr", 30);
    table.addColumn("Ingevoerd op", 130);
    table.addColumn("Status", 120).setUseHTML(true);
    table.addColumn("Ingevoerd door", 200);
    table.addColumn("Zaaktype");
  }

  @Override
  protected void selectTableRecord(ZaakRecord<Dossier> zaakRecord) {
    Dossier dossier = zaakRecord.getZaak();
    DocumentType documentType = getDocumentType(dossier.getType());
    String fragment = getFragment(dossier.getType());
    String caption = getTitle() + " - overzicht";
    Page2BsTemplate page = new Page2BsTemplate(dossier, documentType, fragment, caption, getProfielActie());
    getNavigation().goToPage(page);
  }

  @Override
  protected void loadTableRecord(Record record, ZaakRecord<Dossier> zaakRecord) {
    Dossier zaak = zaakRecord.getZaak();
    record.getValues().get(0).setValue(zaakRecord.getNr());
    record.getValues().get(1).setValue(zaak.getDatumTijdInvoer());
    record.getValues().get(2).setValue(ZaakUtils.getStatus(zaak.getStatus()));
    record.getValues().get(3).setValue(zaak.getIngevoerdDoor().getDescription());
    record.getValues().get(4).setValue(zaak.getType());
  }

  protected abstract DocumentType getDocumentType(ZaakType zaakType);

  protected abstract String getFragment(ZaakType zaakType);

  protected abstract ProfielActie getProfielActie();

  protected abstract ZaakType[] getZaakTypes();
}
