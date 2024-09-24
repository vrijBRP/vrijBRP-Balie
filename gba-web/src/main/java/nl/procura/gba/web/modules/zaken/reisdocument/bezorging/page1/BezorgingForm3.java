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

package nl.procura.gba.web.modules.zaken.reisdocument.bezorging.page1;

import static nl.procura.gba.web.modules.zaken.reisdocument.bezorging.page1.BezorgingBean1.RDN_ANNULERING;
import static nl.procura.gba.web.modules.zaken.reisdocument.bezorging.page1.BezorgingBean1.RDN_BLOKKERING;
import static nl.procura.gba.web.services.zaken.reisdocumenten.bezorging.BezorgingAnnuleringReden.getOms;
import static org.apache.commons.lang3.StringUtils.isAllBlank;

import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.jpa.personen.db.RdmAmp;
import nl.procura.gba.web.components.layouts.form.ReadOnlyForm;
import nl.procura.gba.web.services.zaken.reisdocumenten.bezorging.Bezorging;

public class BezorgingForm3 extends ReadOnlyForm<BezorgingBean1> {

  private final Bezorging bezorging;

  public BezorgingForm3(Bezorging bezorging) {
    this.bezorging = bezorging;
    setColumnWidths(WIDTH_130, "");
    update();
  }

  public void update() {
    List<String> fields = new ArrayList<>();
    RdmAmp melding = bezorging.getMelding();
    if (!isAllBlank(melding.getCodeBlokkering(), melding.getOmsBlokkering())) {
      fields.add(RDN_BLOKKERING);
    }
    String codeAnnulering = melding.getCodeAnnulering();
    String omsAnnulering = melding.getOmsAnnulering();

    if (!isAllBlank(codeAnnulering, omsAnnulering)) {
      fields.add(RDN_ANNULERING);
    }

    setVisible(!fields.isEmpty());
    setOrder(fields.toArray(new String[0]));
    BezorgingBean1 bean = new BezorgingBean1();
    bean.setRedenBlokkering(melding.getCodeBlokkering() + " - " + melding.getOmsBlokkering());
    bean.setRedenAnnulering(codeAnnulering + " - " + getOms(codeAnnulering, omsAnnulering));
    setBean(bean);
  }
}
