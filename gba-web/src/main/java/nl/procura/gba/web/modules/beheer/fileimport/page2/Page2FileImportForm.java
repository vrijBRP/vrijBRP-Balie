/*
 * Copyright 2022 - 2023 Procura B.V.
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

package nl.procura.gba.web.modules.beheer.fileimport.page2;

import static nl.procura.gba.web.modules.beheer.fileimport.page2.Page2FileImportBean.F_CLOSED;
import static nl.procura.gba.web.modules.beheer.fileimport.page2.Page2FileImportBean.F_FILE_IMPORTTYPE;
import static nl.procura.gba.web.modules.beheer.fileimport.page2.Page2FileImportBean.F_NAME;

import nl.procura.gba.jpa.personen.db.FileImport;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.modules.beheer.fileimport.FileImportType;

public class Page2FileImportForm extends GbaForm<Page2FileImportBean> {

  private final FileImport fileImport;

  public Page2FileImportForm(FileImport fileImport) {
    this.fileImport = fileImport;
    setCaption("Gegevens bestand");
    setOrder(F_NAME, F_FILE_IMPORTTYPE, F_CLOSED);
    setColumnWidths(WIDTH_130, "");
    update();
  }

  public void update() {
    Page2FileImportBean bean = new Page2FileImportBean();
    bean.setName(fileImport.getName());
    bean.setFileImportType(FileImportType.getById(fileImport.getTemplate()).orElse(null));
    bean.setClosed(fileImport.isClosed());
    setBean(bean);
  }

  @Override
  public void afterSetBean() {
    getField(F_FILE_IMPORTTYPE).setReadOnly(fileImport.isStored());
    super.afterSetBean();
  }
}
