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

import static nl.procura.gba.web.modules.zaken.inhouding.page2.Page2InhoudingBean1.*;

import nl.procura.gba.web.components.layouts.form.ReadOnlyForm;
import nl.procura.gba.web.services.zaken.inhoudingen.DocumentInhouding;
import nl.procura.gba.web.services.zaken.reisdocumenten.Reisdocument;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentType;

public class Page2InhoudingForm1 extends ReadOnlyForm {

  public Page2InhoudingForm1(DocumentInhouding inhouding, Reisdocument reisdocument) {

    setCaption("Reisdocument");
    setColumnWidths("160px", "");

    Page2InhoudingBean1 b = new Page2InhoudingBean1();

    if (inhouding != null && reisdocument == null) {
      if (ReisdocumentType.ONBEKEND.equals(inhouding.getDocumentType())) {
        setOrder(NUMMER);
      } else {
        setOrder(NUMMER, SOORT);
        b.setSoort(inhouding.getDocumentType().getOms());
      }
      b.setNummer(inhouding.getNummerDocument());
    } else {
      setOrder(NUMMER, SOORT, VANTM, AUTORITEIT);

      b.setNummer(reisdocument.getNummerDocument().getVal());
      b.setSoort(reisdocument.getDocumentOmschrijvingFormat());

      String dUit = reisdocument.getDatumUitgifte().getDescr();
      String dEnd = reisdocument.getDatumEindeGeldigheid().getDescr();

      b.setVanTm(dUit + " t/m " + dEnd);
      b.setAutoriteit(reisdocument.getAutoriteitVanAfgifte().getDescr());
    }

    setBean(b);
  }
}
