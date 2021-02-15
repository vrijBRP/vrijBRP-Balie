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

package nl.procura.gba.web.modules.zaken.indicatie.page2;

import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.services.zaken.algemeen.aantekening.PlAantekeningIndicatie;
import nl.procura.gba.web.services.zaken.indicaties.IndicatieActie;
import nl.procura.vaadin.component.container.ArrayListContainer;

public class IndicatieContainer extends ArrayListContainer {

  public IndicatieContainer(GbaApplication application, IndicatieActie actie) {

    List<PlAantekeningIndicatie> indicaties = new ArrayList<>();

    if (actie != null) {
      switch (actie) {
        case TOEVOEGEN:
          indicaties.addAll(application.getServices().getIndicatiesService().getToegestaneIndicaties());
          break;

        case VERWIJDEREN:
          indicaties.addAll(application.getServices().getIndicatiesService().getPersoonslijstIndicaties());
          break;

        default:
          break;
      }
    }

    if (indicaties.size() > 0) {
      for (PlAantekeningIndicatie indicatie : indicaties) {
        addItem(indicatie);
      }
    }
  }
}
