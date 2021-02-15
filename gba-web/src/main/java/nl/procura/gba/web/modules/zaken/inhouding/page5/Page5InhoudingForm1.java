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

package nl.procura.gba.web.modules.zaken.inhouding.page5;

import static nl.procura.gba.web.modules.zaken.inhouding.page5.Page5InhoudingBean1.*;

import nl.procura.gba.web.components.layouts.form.ReadOnlyForm;
import nl.procura.gba.web.services.zaken.reisdocumenten.Reisdocument;

public class Page5InhoudingForm1 extends ReadOnlyForm {

  public Page5InhoudingForm1(Reisdocument reisdocument) {

    setCaption("Reisdocument");

    setColumnWidths("100px", "");

    Page5InhoudingBean1 b = new Page5InhoudingBean1();

    setOrder(NUMMER, SOORT, VANTM, AUTORITEIT);

    b.setNummer(reisdocument.getNummerDocument().getVal());
    b.setSoort(reisdocument.getDocumentOmschrijvingFormat());

    String dUit = reisdocument.getDatumUitgifte().getDescr();
    String dEnd = reisdocument.getDatumEindeGeldigheid().getDescr();

    b.setVanTm(dUit + " t/m " + dEnd);
    b.setAutoriteit(reisdocument.getAutoriteitVanAfgifte().getDescr());

    setBean(b);
  }
}
