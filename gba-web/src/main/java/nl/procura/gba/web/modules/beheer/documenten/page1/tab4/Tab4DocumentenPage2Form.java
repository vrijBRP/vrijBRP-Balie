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

package nl.procura.gba.web.modules.beheer.documenten.page1.tab4;

import static nl.procura.gba.web.modules.beheer.documenten.page1.tab4.Tab4DocumentenPage2Bean.DOEL;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.zaken.documenten.doelen.DocumentDoel;

public class Tab4DocumentenPage2Form extends GbaForm<Tab4DocumentenPage2Bean> {

  public Tab4DocumentenPage2Form(DocumentDoel documentDoel) {

    setOrder(DOEL);

    setColumnWidths("200px", "");

    initFields(documentDoel);
  }

  private void initFields(DocumentDoel documentDoel) {

    Tab4DocumentenPage2Bean bean = new Tab4DocumentenPage2Bean();

    if (documentDoel.isStored()) {
      bean.setDoel(documentDoel.getDocumentDoel());
    }

    setBean(bean);
    repaint();
  }
}
