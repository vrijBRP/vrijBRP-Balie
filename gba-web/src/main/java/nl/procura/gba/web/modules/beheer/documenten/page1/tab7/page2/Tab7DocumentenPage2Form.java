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

package nl.procura.gba.web.modules.beheer.documenten.page1.tab7.page2;

import static nl.procura.gba.web.modules.beheer.documenten.page1.tab7.page2.Tab7DocumentenPage2Bean.KENMERK;
import static nl.procura.gba.web.modules.beheer.documenten.page1.tab7.page2.Tab7DocumentenPage2Bean.LABEL;
import static nl.procura.gba.web.modules.beheer.documenten.page1.tab7.page2.Tab7DocumentenPage2Bean.TYPE;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.zaken.documenten.kenmerk.DocumentKenmerk;
import nl.procura.gba.web.services.zaken.documenten.kenmerk.DocumentKenmerkType;

public abstract class Tab7DocumentenPage2Form extends GbaForm<Tab7DocumentenPage2Bean> {

  public Tab7DocumentenPage2Form(DocumentKenmerk documentKenmerk) {
    setOrder(KENMERK, TYPE, LABEL);
    setColumnWidths(WIDTH_130, "");
    initFields(documentKenmerk);
    setReadonlyAsText(false);
  }

  @Override
  public void afterSetBean() {

    getField(Tab7DocumentenPage2Bean.TYPE).addListener((ValueChangeListener) event -> {
      DocumentKenmerkType type = (DocumentKenmerkType) event.getProperty().getValue();
      getField(LABEL).setValue(type.getToel());
      setOnChangeType(type);
    });

    super.afterSetBean();
  }

  protected abstract void setOnChangeType(DocumentKenmerkType type);

  private void initFields(DocumentKenmerk documentKenmerk) {

    Tab7DocumentenPage2Bean bean = new Tab7DocumentenPage2Bean();

    if (documentKenmerk.isStored()) {
      bean.setKenmerk(documentKenmerk.getDocumentKenmerk());
      bean.setType(documentKenmerk.getKenmerkType());
      bean.setLabel(documentKenmerk.getKenmerkType().getToel());
    }

    setBean(bean);

    repaint();
  }
}
