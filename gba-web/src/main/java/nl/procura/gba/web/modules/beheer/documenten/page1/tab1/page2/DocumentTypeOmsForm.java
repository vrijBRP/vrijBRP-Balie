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

import static nl.procura.gba.web.modules.beheer.documenten.page1.tab1.page2.DocumentTypeOmsBean.AANTALDOC;
import static nl.procura.gba.web.modules.beheer.documenten.page1.tab1.page2.DocumentTypeOmsBean.DOCUMENTTYPE;

import java.util.List;

import nl.procura.gba.web.components.fields.GbaComboBox;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.zaken.documenten.DocumentRecord;
import nl.procura.gba.web.services.zaken.documenten.dmstypes.DmsDocumentType;
import nl.procura.vaadin.component.container.ArrayListContainer;

public class DocumentTypeOmsForm extends GbaForm<DocumentTypeOmsBean> {

  public DocumentTypeOmsForm(List<DocumentRecord> docList) {
    setOrder(AANTALDOC, DOCUMENTTYPE);
    initFields(docList);
  }

  private void initFields(List<DocumentRecord> docList) {
    DocumentTypeOmsBean bean = new DocumentTypeOmsBean();
    bean.setAantalDocumenten(docList.size());
    setBean(bean);
  }

  @Override
  public void attach() {
    super.attach();
    setDocumenttypeOmschrijvingContainer();
  }

  protected void setDocumenttypeOmschrijvingContainer() {
    GbaComboBox field = getField(DOCUMENTTYPE, GbaComboBox.class);
    field.setContainerDataSource(new DocumentTypeOmschrijvingContainer());
    field.setNewItemsAllowed(true);
    field.setValue(getBean().getDocumenttype());
  }

  protected class DocumentTypeOmschrijvingContainer extends ArrayListContainer {

    public DocumentTypeOmschrijvingContainer() {
      getApplication().getServices().getDocumentService().getDmsDocumentTypes().stream()
          .map(DmsDocumentType::toString)
          .forEach(this::addItem);
    }
  }
}
