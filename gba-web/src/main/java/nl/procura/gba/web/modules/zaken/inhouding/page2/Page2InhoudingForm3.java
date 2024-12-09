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

package nl.procura.gba.web.modules.zaken.inhouding.page2;

import static nl.procura.gba.web.modules.zaken.inhouding.page2.Page2InhoudingBean1.DATUM;
import static nl.procura.gba.web.modules.zaken.inhouding.page2.Page2InhoudingBean1.DATUM_READONLY;
import static nl.procura.gba.web.modules.zaken.inhouding.page2.Page2InhoudingBean1.INHOUDING;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.zaken.inhoudingen.DocumentInhouding;

public class Page2InhoudingForm3 extends GbaForm<Page2InhoudingBean1> {

  public Page2InhoudingForm3(DocumentInhouding inhouding, boolean readOnlyDatumGeldigheid) {
    setColumnWidths("160px", "");
    Page2InhoudingBean1 bean = new Page2InhoudingBean1();

    if (inhouding != null) {
      if (readOnlyDatumGeldigheid) {
        setOrder(INHOUDING, DATUM_READONLY);
      } else {
        setOrder(INHOUDING, DATUM);
      }
      bean.setInhouding(inhouding.getInhoudingType().getOms());
      bean.setDatum(new DateTime(inhouding.getDInneming()).getDate());
      bean.setDatumReadOnly(new DateTime(inhouding.getDInneming()).getFormatDate());
    }

    setBean(bean);
  }
}
