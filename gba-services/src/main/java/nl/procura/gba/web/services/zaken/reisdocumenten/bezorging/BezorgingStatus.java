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

import static nl.procura.gba.web.services.zaken.reisdocumenten.bezorging.BezorgingStatusType.AANGEMELD_AMP;
import static nl.procura.gba.web.services.zaken.reisdocumenten.bezorging.BezorgingStatusType.GEANNULEERD_AMP;
import static nl.procura.gba.web.services.zaken.reisdocumenten.bezorging.BezorgingStatusType.GEBLOKKEERD;
import static nl.procura.gba.web.services.zaken.reisdocumenten.bezorging.BezorgingStatusType.GEKOPPELD;
import static nl.procura.gba.web.services.zaken.reisdocumenten.bezorging.BezorgingStatusType.INGEKLAARD;
import static nl.procura.gba.web.services.zaken.reisdocumenten.bezorging.BezorgingStatusType.INGEVOERD;
import static nl.procura.gba.web.services.zaken.reisdocumenten.bezorging.BezorgingStatusType.ONBEKEND;
import static nl.procura.gba.web.services.zaken.reisdocumenten.bezorging.BezorgingStatusType.UITGEREIKT_AMP;
import static nl.procura.gba.web.services.zaken.reisdocumenten.bezorging.BezorgingStatusType.VERWERKT;

import nl.procura.gba.jpa.personen.db.RdmAmp;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BezorgingStatus {

  private BezorgingStatusType type;

  public String toString() {
    return type.getOms();
  }

  public static BezorgingStatus getStatus(RdmAmp melding) {
    if (melding != null) {
      if (melding.getDEnd().intValue() > 0) {
        return getStatus(VERWERKT);

      } else if (melding.isIndUitreiking()) {
        return getStatus(UITGEREIKT_AMP);

      } else if (melding.isIndAnnulering()) {
        return getStatus(GEANNULEERD_AMP);

      } else if (melding.isIndBlokkering()) {
        return getStatus(GEBLOKKEERD);

      } else if (melding.isIndInklaring()) {
        return getStatus(INGEKLAARD);

      } else if (melding.isIndKoppeling()) {
        return getStatus(GEKOPPELD);

      } else if (melding.isIndVoormelding()) {
        return getStatus(AANGEMELD_AMP);

      } else if (melding.getDIn().intValue() > 0) {
        return getStatus(INGEVOERD);
      }
    }

    return getStatus(ONBEKEND);
  }

  private static BezorgingStatus getStatus(BezorgingStatusType type) {
    return new BezorgingStatus(type);
  }
}
