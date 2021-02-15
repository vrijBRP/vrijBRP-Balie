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

package nl.procura.gba.web.services.beheer.profiel.zaak_conf;

import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.web.services.zaken.algemeen.zaakconfiguraties.ZaakConfiguratie;

public class ZaakConfiguraties {

  private List<ZaakConfiguratie> all = new ArrayList<>();

  public ZaakConfiguraties() {
  }

  public ZaakConfiguraties(List<ZaakConfiguratie> all) {
    setAll(all);
  }

  public List<ZaakConfiguratie> getAlle() {
    return all;
  }

  public ZaakConfiguratie getZaakConfiguratie(ZaakConfiguratie zaakConfiguratie) {
    return getAlle().stream()
        .filter(configuratie -> configuratie.equals(zaakConfiguratie))
        .findFirst()
        .orElse(null);

  }

  public void setAll(List<ZaakConfiguratie> all) {
    this.all = all;
  }
}
