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

import static nl.procura.gba.web.modules.beheer.documenten.page1.tab1.page2.DocumentTypeBean.AANTALDOC;
import static nl.procura.gba.web.modules.beheer.documenten.page1.tab1.page2.DocumentTypeBean.DOCTYPE;

import java.util.List;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.zaken.documenten.DocumentRecord;
import nl.procura.gba.web.services.zaken.documenten.DocumentType;

public class DocumentTypeForm extends GbaForm<DocumentTypeBean> {

  public DocumentTypeForm(List<DocumentRecord> docList) {

    setOrder(AANTALDOC, DOCTYPE);
    initFields(docList);
  }

  private DocumentType giveCommonDocType(List<DocumentRecord> doclList) {
    DocumentType docType = doclList.get(0).getDocumentType(); // we nemen het eerste pad als referentie
    for (DocumentRecord doc : doclList) {
      if (doc.getDocumentType().equals(docType)) {
        continue;
      }
      docType = null;
      break;
    }

    return docType;
  }

  private void initFields(List<DocumentRecord> docList) {

    DocumentTypeBean bean = new DocumentTypeBean();
    bean.setAantalDocumenten(docList.size());
    DocumentType docType = giveCommonDocType(docList);
    bean.setDocType(docType);

    setBean(bean);
  }
}
