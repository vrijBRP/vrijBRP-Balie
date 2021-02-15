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

package nl.procura.gba.web.modules.beheer.documenten.page1.tab1.page2;

import static nl.procura.gba.web.modules.beheer.documenten.page1.tab1.page2.StillbornBean.DOCUMENT_COUNT;
import static nl.procura.gba.web.modules.beheer.documenten.page1.tab1.page2.StillbornBean.STILLBORN;

import java.util.List;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.zaken.documenten.DocumentRecord;

public class StillbornForm extends GbaForm<StillbornBean> {

  public StillbornForm(List<DocumentRecord> docList) {
    setOrder(DOCUMENT_COUNT, STILLBORN);
    setColumnWidths("150px", "");

    StillbornBean bean = new StillbornBean();
    bean.setDocumentCount(docList.size());
    bean.setStillborn(isCommonValue(docList));
    setBean(bean);
  }

  /**
   * Return the common value of all records
   * otherwise returns false
   */
  private boolean isCommonValue(List<DocumentRecord> doclList) {
    boolean type = doclList.get(0).isStillbornAllowed();
    for (DocumentRecord doc : doclList) {
      if (doc.isStillbornAllowed() == type) {
        continue;
      }
      type = false;
      break;
    }

    return type;
  }
}
