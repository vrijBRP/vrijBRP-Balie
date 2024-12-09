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

package nl.procura.gba.web.modules.zaken.inhouding.page2;

import static nl.procura.gba.web.modules.zaken.inhouding.page2.Page2InhoudingBean4.DATUM_REDEN_MELDING;
import static nl.procura.gba.web.modules.zaken.inhouding.page2.Page2InhoudingBean4.DATUM_TIJD;
import static nl.procura.gba.web.modules.zaken.inhouding.page2.Page2InhoudingBean4.INGELEVERD;
import static nl.procura.gba.web.modules.zaken.inhouding.page2.Page2InhoudingBean4.MELDING_TYPE;
import static nl.procura.gba.web.modules.zaken.inhouding.page2.Page2InhoudingBean4.REDEN_TYPE;

import nl.procura.gba.web.components.layouts.form.ReadOnlyForm;
import nl.procura.gba.web.services.zaken.inhoudingen.DocumentInhouding;

public class Page2InhoudingForm4 extends ReadOnlyForm<Page2InhoudingBean4> {

  private final DocumentInhouding inhouding;

  public Page2InhoudingForm4(DocumentInhouding zaak) {
    this.inhouding = zaak;
    setCaption("BR registratie melding");
    setColumnWidths("160px", "");
    setOrder(DATUM_TIJD, MELDING_TYPE, REDEN_TYPE, DATUM_REDEN_MELDING, INGELEVERD);
    update();
  }

  public void update() {
    Page2InhoudingBean4 bean = new Page2InhoudingBean4();
    bean.setDatumTijd(get(inhouding.getVrsDatumTijd().getFormatDate() + " " + inhouding.getVrsDatumTijd().getFormatTime()));
    bean.setMeldingType(get(inhouding.getVrsMelding()));
    bean.setRedenType(get(inhouding.getVrsReden()));
    bean.setDatumRedenMelding(get(inhouding.getVrsDatumRedenMelding()));
    bean.setIngeleverd(inhouding.isIngeleverd() ? "Ja, op " + inhouding.getVrsDatumInlevering() : "Nee");
    setBean(bean);
  }

  public String get(Object value) {
    if (inhouding.isVrsRegistratieGedaan()) {
      return value.toString();
    } else {
      return "Niet van toepassing";
    }
  }
}
