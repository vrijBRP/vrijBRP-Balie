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

package nl.procura.gba.web.modules.beheer.documenten.page1.tab1.page2.importing;

import static nl.procura.gba.web.modules.beheer.documenten.page1.tab1.page2.importing.DocumentExportBean.*;

import com.vaadin.ui.Field;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.vaadin.component.layout.table.TableLayout;

public class DocumentImportForm extends GbaForm<DocumentExportBean> {

  public DocumentImportForm() {

    setOrder(KOPIE_OPSLAAN, PROTOCOLLERING, STANDAARD_GESELECTEERD, IEDEREEN_TOEGANG, STILLBORN_ALLOWED);
    setColumnWidths("140px", "160px", "120px", "");
    setBean(new DocumentExportBean());
  }

  @Override
  public void setColumn(TableLayout.Column column, Field field, Property property) {
    if (property.is(STILLBORN_ALLOWED)) {
      column.setColspan(3);
    }
    super.setColumn(column, field, property);
  }
}
