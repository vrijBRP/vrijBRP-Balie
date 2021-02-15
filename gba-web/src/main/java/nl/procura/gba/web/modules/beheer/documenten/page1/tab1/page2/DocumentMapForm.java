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

import static nl.procura.gba.common.MiscUtils.cleanPath;
import static nl.procura.gba.web.modules.beheer.documenten.page1.tab1.page2.DocumentMapBean.AANTALDOC;
import static nl.procura.gba.web.modules.beheer.documenten.page1.tab1.page2.DocumentMapBean.MAP;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.vaadin.ui.ComboBox;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.zaken.documenten.DocumentRecord;
import nl.procura.vaadin.component.container.ArrayListContainer;

public class DocumentMapForm extends GbaForm<DocumentMapBean> {

  public DocumentMapForm(List<DocumentRecord> docList) {

    setOrder(AANTALDOC, MAP);
    initFields(docList);
  }

  @Override
  public void attach() {
    setDocumentmapContainer();
    super.attach();
  }

  void setDocumentmapContainer() {

    ComboBox map = (ComboBox) getField(DocumentMapBean.MAP);
    map.setContainerDataSource(new DocumentmapContainer());
    map.setNewItemsAllowed(true);
    map.setValue(cleanPath(getBean().getMap()));
  }

  /**
   * Kijk of er een gemeenschappelijke map voor de documenten te vinden is.
   */

  private String getCommonDocPath(List<DocumentRecord> doclList) {

    String path = doclList.get(0).getPad(); // we nemen het eerste pad als referentie

    for (DocumentRecord doc : doclList) {

      String docPath = doc.getPad();

      if (path.startsWith(docPath)) {
        path = docPath;
      } else if (!docPath.startsWith(path)) { // geen gemeenschappelijk pad!
        return "";
      }
    }

    return path;
  }

  private void initFields(List<DocumentRecord> docList) {

    DocumentMapBean bean = new DocumentMapBean();
    bean.setAantalDocumenten(docList.size());
    String path = getCommonDocPath(docList);
    bean.setMap(path);

    setBean(bean);
  }

  private class DocumentmapContainer extends ArrayListContainer {

    private DocumentmapContainer() {
      List<DocumentRecord> documentList = getApplication().getServices().getDocumentService().getDocumenten(
          false);
      List<String> pathList = new ArrayList<>();

      for (DocumentRecord doc : documentList) {

        String cleanedPath = cleanPath(doc.getPad());

        if (!pathList.contains(cleanedPath)) {
          pathList.add(cleanedPath);
        }
      }

      Collections.sort(pathList);

      for (String map : pathList) {
        addItem(map);
      }
    }
  }
}
