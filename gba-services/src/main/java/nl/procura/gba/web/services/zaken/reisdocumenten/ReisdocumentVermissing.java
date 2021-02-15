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

package nl.procura.gba.web.services.zaken.reisdocumenten;

import nl.procura.diensten.gba.ple.openoffice.DocumentPL;
import nl.procura.gba.web.services.zaken.inhoudingen.DocumentInhouding;

public class ReisdocumentVermissing {

  private DocumentInhouding inhouding    = null;
  private Reisdocument      reisdocument = null;

  public DocumentInhouding getInhouding() {
    return inhouding;
  }

  public void setInhouding(DocumentInhouding inhouding) {
    this.inhouding = inhouding;
  }

  public DocumentPL getPersoon() {
    return inhouding.getPersoon();
  }

  public Reisdocument getReisdocument() {
    return reisdocument;
  }

  public void setReisdocument(Reisdocument reisdocument) {
    this.reisdocument = reisdocument;
  }
}
