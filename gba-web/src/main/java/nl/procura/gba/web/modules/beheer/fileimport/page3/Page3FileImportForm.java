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

package nl.procura.gba.web.modules.beheer.fileimport.page3;

import static nl.procura.gba.web.modules.beheer.fileimport.page3.Page3FileImportBean.F_FILENAME;
import static nl.procura.gba.web.modules.beheer.fileimport.page3.Page3FileImportBean.F_SIZE;
import static nl.procura.gba.web.modules.beheer.fileimport.page3.Page3FileImportBean.F_VALIDATION;

import nl.procura.gba.web.components.layouts.form.GbaForm;

public class Page3FileImportForm extends GbaForm<Page3FileImportBean> {

  public Page3FileImportForm() {
    setOrder(F_FILENAME, F_SIZE, F_VALIDATION);
    setColumnWidths(WIDTH_130, "");
    setBean(new Page3FileImportBean());
  }
}
