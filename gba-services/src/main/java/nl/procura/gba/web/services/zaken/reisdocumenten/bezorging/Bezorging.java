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

package nl.procura.gba.web.services.zaken.reisdocumenten.bezorging;

import static org.apache.commons.lang3.StringUtils.normalizeSpace;

import java.util.Arrays;

import nl.procura.gba.jpa.personen.db.RdmAmp;

import lombok.Data;

@Data
public class Bezorging {

  private RdmAmp melding;

  public Bezorging() {
  }

  public Bezorging(RdmAmp melding) {
    this.melding = melding;
  }

  public boolean isMelding() {
    return melding != null;
  }

  public boolean isStatus(BezorgingStatusType... types) {
    BezorgingStatus status = getStatus();
    return Arrays.stream(types).anyMatch(type -> status.getType() == type);
  }

  public BezorgingStatus getStatus() {
    return BezorgingStatus.getStatus(melding);
  }

  public boolean isAangemeld() {
    return melding != null && melding.isIndVoormelding();
  }

  public String getAdres() {
    return melding != null
        ? normalizeSpace(String.format("%s %d %s %s, %s %s",
            melding.getStraat(),
            melding.getHnr(),
            melding.getHnrL(),
            melding.getHnrT(),
            melding.getPc(),
            melding.getWpl()))
        : "";
  }

  public String getNaam() {
    return melding != null
        ? normalizeSpace(String.format("%s %s %s",
            melding.getVoornamen(),
            melding.getVoorv(),
            melding.getGeslachtsnaam()))
        : "";
  }
}
