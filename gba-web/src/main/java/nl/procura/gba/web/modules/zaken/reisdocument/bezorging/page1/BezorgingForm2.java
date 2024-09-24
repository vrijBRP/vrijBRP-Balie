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

import static nl.procura.gba.web.modules.zaken.reisdocument.bezorging.page1.BezorgingBean1.ADRES;
import static nl.procura.gba.web.modules.zaken.reisdocument.bezorging.page1.BezorgingBean1.NAAM;
import static nl.procura.standard.Globalfunctions.trim;

import nl.procura.gba.jpa.personen.db.RdmAmp;
import nl.procura.gba.web.components.layouts.form.ReadOnlyForm;
import nl.procura.gba.web.services.zaken.reisdocumenten.bezorging.Bezorging;

public class BezorgingForm2 extends ReadOnlyForm<BezorgingBean1> {

  public BezorgingForm2(Bezorging bezorging) {
    setOrder(NAAM, ADRES);
    setColumnWidths(WIDTH_130, "");

    BezorgingBean1 bean = new BezorgingBean1();
    RdmAmp melding = bezorging.getMelding();

    if (melding != null) {
      bean.setAanvrNr(melding.getAanvrNr());
      bean.setRefNr(melding.getBundelRefNr());
      bean.setNaam(trim(bezorging.getNaam()));
      bean.setAdres(trim(bezorging.getAdres()));
      bean.setHoofdorder(melding.getHoofdorder() ? "Ja" : "Nee");
      bean.setOpmerkingen(melding.getOpmerkingen());
    }

    setBean(bean);
  }
}
