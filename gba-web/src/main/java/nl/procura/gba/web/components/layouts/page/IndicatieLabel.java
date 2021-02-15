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

package nl.procura.gba.web.components.layouts.page;

import java.util.Set;

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.services.zaken.algemeen.aantekening.PlAantekeningIndicatie;

public class IndicatieLabel extends MainLabel {

  public IndicatieLabel(GbaApplication application) {
    super(application, "Indicatie");
  }

  @Override
  public void doCheck() {

    Set<PlAantekeningIndicatie> indicaties = getIndicaties();
    int count = indicaties.size();
    setSizeUndefined();
    setAdd(count > 0);

    if (count > 0) {
      setValue(getLabel(indicaties));
      setDescription("Deze persoon heeft " + count + " indicatie(s).");
    }
  }

  private Set<PlAantekeningIndicatie> getIndicaties() {
    return getServices().getIndicatiesService().getAllIndicaties();
  }

  private String getLabel(Set<PlAantekeningIndicatie> indicaties) {
    if (indicaties.size() == 1) {
      return indicaties.iterator().next().getButton();
    } else if (indicaties.size() > 1) {
      return "Indicaties (" + indicaties.size() + ")";
    }

    return "";
  }
}
