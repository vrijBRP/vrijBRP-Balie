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

import static nl.procura.gba.web.modules.beheer.documenten.page1.tab9.page2.Tab9DocumentenPage3Bean.BUITENLANDS;
import static nl.procura.gba.web.modules.beheer.documenten.page1.tab9.page2.Tab9DocumentenPage3Bean.NL;

import nl.procura.gba.jpa.personen.db.TranslationRec;
import nl.procura.gba.web.components.layouts.form.GbaForm;

public class Tab9DocumentenPage3Form extends GbaForm<Tab9DocumentenPage3Bean> {

  public Tab9DocumentenPage3Form(TranslationRec record) {
    setOrder(NL, BUITENLANDS);
    setColumnWidths("100px", "");
    initFields(record);
  }

  private void initFields(TranslationRec record) {
    Tab9DocumentenPage3Bean bean = new Tab9DocumentenPage3Bean();
    bean.setNl(record.getNl());
    bean.setBuitenlands(record.getFl());
    setBean(bean);
  }
}
