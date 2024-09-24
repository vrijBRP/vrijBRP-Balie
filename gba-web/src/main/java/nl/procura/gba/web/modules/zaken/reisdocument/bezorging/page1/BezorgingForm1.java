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

import static nl.procura.gba.web.modules.zaken.reisdocument.bezorging.page1.BezorgingBean1.AANVR_NR;
import static nl.procura.gba.web.modules.zaken.reisdocument.bezorging.page1.BezorgingBean1.DOC_TYPE;
import static nl.procura.gba.web.modules.zaken.reisdocument.bezorging.page1.BezorgingBean1.HOOFDORDER;
import static nl.procura.gba.web.modules.zaken.reisdocument.bezorging.page1.BezorgingBean1.OPMERKINGEN;
import static nl.procura.gba.web.modules.zaken.reisdocument.bezorging.page1.BezorgingBean1.ORDER_REF_NR;
import static nl.procura.gba.web.modules.zaken.reisdocument.bezorging.page1.BezorgingBean1.REF_NR;

import nl.procura.gba.jpa.personen.db.RdmAmp;
import nl.procura.gba.web.components.layouts.form.ReadOnlyForm;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentType;
import nl.procura.gba.web.services.zaken.reisdocumenten.bezorging.Bezorging;

public class BezorgingForm1 extends ReadOnlyForm<BezorgingBean1> {

  private final Bezorging bezorging;

  public BezorgingForm1(Bezorging bezorging) {
    this.bezorging = bezorging;
    setReadonlyAsText(false);
    setOrder(DOC_TYPE, AANVR_NR, REF_NR, ORDER_REF_NR, HOOFDORDER, OPMERKINGEN);
    setColumnWidths(WIDTH_130, "");
    update();
  }

  public void update() {
    RdmAmp melding = bezorging.getMelding();
    BezorgingBean1 bean = new BezorgingBean1();
    if (melding != null) {
      bean.setDocType(ReisdocumentType.get(melding.getDocType()).getOms());
      bean.setAanvrNr(melding.getAanvrNr());
      bean.setRefNr(melding.getBundelRefNr());
      bean.setOrderRefNr(melding.getOrderRefNr());
      bean.setNaam(bezorging.getNaam());
      bean.setAdres(melding.getPc());
      bean.setHoofdorder(melding.getHoofdorder() ? "Ja" : "Nee");
      bean.setOpmerkingen(melding.getOpmerkingen());
    }
    setBean(bean);
  }
}
