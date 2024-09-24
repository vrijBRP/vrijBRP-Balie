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

package nl.procura.gba.web.services.zaken.correspondentie;

import java.util.Map;
import java.util.Map.Entry;

import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.gba.web.services.zaken.algemeen.controle.Controles;
import nl.procura.gba.web.services.zaken.algemeen.controle.ControlesListener;
import nl.procura.gba.web.services.zaken.algemeen.controle.ControlesTemplate;
import nl.procura.gba.web.services.zaken.algemeen.controle.ZaakControle;
import nl.procura.gba.web.services.zaken.algemeen.zaakrelaties.ZaakRelatie;
import nl.procura.gba.web.services.zaken.algemeen.zaakrelaties.ZaakRelatieService;
import nl.procura.gba.web.services.zaken.algemeen.zaakrelaties.ZaakRelaties;

/**
 * Alle controles van correspondentie
 */
public class CorrespondentieZaakControles extends ControlesTemplate<CorrespondentieService> {

  public CorrespondentieZaakControles(CorrespondentieService service) {
    super(service);
  }

  /**
   * Als alle gerelateerde zaken zijn beeindigd dan ook de correspondentie beeindigen.
   */
  @Override
  public Controles getControles(ControlesListener listener) {

    Controles controles = new Controles();
    CorrespondentieService service = getService();
    ZaakRelatieService dbZaakRelaties = service.getServices().getZaakRelatieService();
    ZaakArgumenten corArgs = new ZaakArgumenten(ZaakStatusType.OPGENOMEN);

    for (Zaak zaak : service.getMinimalZaken(corArgs)) {
      CorrespondentieZaak corrZaak = (CorrespondentieZaak) zaak;
      ZaakRelaties relaties = dbZaakRelaties.getGerelateerdeZaakRelaties(zaak);
      Map<ZaakRelatie, Zaak> map = dbZaakRelaties.getGerelateerdeZaken(relaties);

      // Alleen controles als automatisch afsluiten van toepassing is.
      if (CorrespondentieAfsluitingType.AUTOMATISCH != corrZaak.getAfsluitType()) {
        continue;
      }

      boolean alleenEindStatus = true;
      for (Entry<ZaakRelatie, Zaak> entry : map.entrySet()) {
        if (!entry.getValue().getStatus().isEindStatus()) {
          alleenEindStatus = false;
        }
      }

      ZaakControle controle = controles.addControle(new ZaakControle("Correspondentie", zaak));

      if (alleenEindStatus) {
        controle.addOpmerking("Gerelateerde zaken zijn allemaal verwerkt");
        service.updateStatus(corrZaak, zaak.getStatus(), ZaakStatusType.VERWERKT,
            "Automatisch afgesloten omdat er geen openstaande gerelateerde zaken meer zijn.");
      } else {
        controle.addOpmerking("Heeft nog openstaande zaken");
      }
    }

    return controles;
  }
}
