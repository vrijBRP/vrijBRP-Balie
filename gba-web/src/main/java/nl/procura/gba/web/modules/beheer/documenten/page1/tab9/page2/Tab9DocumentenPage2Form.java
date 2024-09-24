/*
 * Copyright 2024 - 2025 Procura B.V.
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

package nl.procura.gba.web.modules.beheer.documenten.page1.tab9.page2;

import static nl.procura.gba.web.modules.beheer.documenten.page1.tab9.page2.Tab9DocumentenPage2Bean.OMSCHRIJVING;

import nl.procura.gba.jpa.personen.db.Translation;
import nl.procura.gba.web.components.layouts.form.GbaForm;

public class Tab9DocumentenPage2Form extends GbaForm<Tab9DocumentenPage2Bean> {

  public Tab9DocumentenPage2Form(Translation translation) {
    setOrder(OMSCHRIJVING);
    setColumnWidths(WIDTH_130, "");
    initFields(translation);
  }

  private void initFields(Translation translation) {
    Tab9DocumentenPage2Bean bean = new Tab9DocumentenPage2Bean();
    bean.setOmschrijving(translation.getName());
    setBean(bean);
  }
}
