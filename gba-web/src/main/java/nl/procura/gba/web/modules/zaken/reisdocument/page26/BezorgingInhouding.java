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

package nl.procura.gba.web.modules.zaken.reisdocument.page26;

import nl.procura.gba.web.modules.zaken.reisdocument.page26.Page26ReisdocumentTable1.BooleanField;
import nl.procura.gba.web.services.zaken.reisdocumenten.Reisdocument;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentType;

import lombok.Data;

@Data
public class BezorgingInhouding {

  private boolean          automatischInhouden;
  private Long             datumEindeGeldigheid;
  private ReisdocumentType documentType;
  private String           documentNr;
  private BooleanField     valueField;

  public BezorgingInhouding(Reisdocument reisdocument) {
    this.datumEindeGeldigheid = reisdocument.getDatumEindeGeldigheid().toLong();
    this.documentType = ReisdocumentType.get(reisdocument.getNederlandsReisdocument().getVal());
    this.documentNr = reisdocument.getNummerDocument().getVal();
  }

  public boolean isInhoudenKeuze() {
    return (boolean) valueField.getValue();
  }

  public boolean isDocumentNr(String documentNr) {
    return this.documentNr.equals(documentNr);
  }
}
