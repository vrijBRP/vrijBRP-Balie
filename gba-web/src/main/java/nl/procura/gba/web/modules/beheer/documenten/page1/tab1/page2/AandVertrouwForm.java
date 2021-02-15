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

import static nl.procura.gba.web.modules.beheer.documenten.page1.tab1.page2.AandVertrouwBean.AANTALDOC;
import static nl.procura.gba.web.modules.beheer.documenten.page1.tab1.page2.AandVertrouwBean.VERTROUWELIJKHEID;

import java.util.List;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.zaken.documenten.DocumentRecord;
import nl.procura.gba.web.services.zaken.documenten.DocumentVertrouwelijkheid;

public class AandVertrouwForm extends GbaForm<AandVertrouwBean> {

  public AandVertrouwForm(List<DocumentRecord> docList) {
    setOrder(AANTALDOC, VERTROUWELIJKHEID);
    initFields(docList);
  }

  private DocumentVertrouwelijkheid giveCommonType(List<DocumentRecord> doclList) {
    DocumentVertrouwelijkheid type = doclList.get(0).getVertrouwelijkheid();
    for (DocumentRecord doc : doclList) {
      if (doc.getVertrouwelijkheid().equals(type)) {
        continue;
      }
      type = null;
      break;
    }

    return type;
  }

  private void initFields(List<DocumentRecord> docList) {

    AandVertrouwBean bean = new AandVertrouwBean();
    bean.setAantalDocumenten(docList.size());
    bean.setVertrouwelijkheid(giveCommonType(docList));

    setBean(bean);
  }
}
