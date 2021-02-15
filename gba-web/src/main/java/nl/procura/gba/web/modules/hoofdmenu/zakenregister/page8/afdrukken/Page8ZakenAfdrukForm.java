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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.page8.afdrukken;

import static nl.procura.gba.web.modules.hoofdmenu.zakenregister.page8.afdrukken.Page8ZakenAfdrukBean.*;

import com.vaadin.ui.TextField;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.components.layouts.form.document.DocumentUitvoerContainer;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page8.tab3.Page8ZakenTab3Bean;
import nl.procura.gba.web.services.zaken.documenten.DocumentRecord;

public class Page8ZakenAfdrukForm extends GbaForm<Page8ZakenTab3Bean> {

  public Page8ZakenAfdrukForm() {

    setOrder(DOCUMENT, UITVOER, UITVOER_LABEL, STATUS);
    setColumnWidths("160px", "");
    setBean(new Page8ZakenTab3Bean());

    getDocumentVeld().addListener((ValueChangeListener) this);
  }

  public DocumentRecord getDocument() {
    return getBean().getDocument();
  }

  public GbaNativeSelect getDocumentVeld() {
    return ((GbaNativeSelect) getField(DOCUMENT));
  }

  public Boolean getStatus() {
    return getBean().getStatus();
  }

  public TextField getUitvoerLabel() {
    return ((TextField) getField(UITVOER_LABEL));
  }

  public GbaNativeSelect getUitvoerVeld() {
    return ((GbaNativeSelect) getField(UITVOER));
  }

  @Override
  public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {

    if (event.getProperty() == getDocumentVeld()) {

      DocumentRecord document = (DocumentRecord) getDocumentVeld().getValue();

      getUitvoerVeld().setContainerDataSource(new DocumentUitvoerContainer(document));

      boolean isDocument = (document != null);
      boolean isUitvoer = (getUitvoerVeld().getContainerDataSource().size() > 0);

      getUitvoerVeld().setVisible(isDocument && isUitvoer);
      getUitvoerLabel().setVisible(isDocument && !isUitvoer);

      repaint();
    }
  }
}
