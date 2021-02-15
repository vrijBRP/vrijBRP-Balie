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

package nl.procura.gba.web.rest.v1_0.zaak.dashboard;

import static nl.procura.gba.web.rest.v1_0.zaak.GbaRestElementHandler.add;
import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.aval;

import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.web.rest.v1_0.GbaRestHandler;
import nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElement;
import nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.beheer.parameter.ParameterConstant;
import nl.procura.gba.web.services.zaken.dashboard.DashboardOverzicht1;
import nl.procura.gba.web.services.zaken.dashboard.DashboardTelling;
import nl.procura.standard.ProcuraDate;

/**
 * Haal de dashboardgegevens op
 */
public class GbaRestDashboardHandler extends GbaRestHandler {

  public GbaRestDashboardHandler(Services services) {
    super(services);
  }

  public List<GbaRestElement> getDashboard(GbaRestDashboardVraag vraag) {

    String periode = vraag.getPeriode();
    int datumVanaf = aval(new ProcuraDate(vraag.getDatumVanaf()).getSystemDate());
    int datumTm = aval(new ProcuraDate(vraag.getDatumTm()).getSystemDate());

    List<GbaRestElement> antwoord = new ArrayList<>();

    GbaRestElement elementen = new GbaRestElement(GbaRestElementType.TELLINGEN);

    antwoord.add(elementen);

    String bron = getServices().getParameterService().getParm(ParameterConstant.MIDOFFICE_DASHBOARD_BRONNEN);
    String leverancier = getServices().getParameterService().getParm(
        ParameterConstant.MIDOFFICE_DASHBOARD_LEVERANCIERS);

    DashboardOverzicht1 overzicht = new DashboardOverzicht1(periode, datumVanaf, datumTm, bron, leverancier);

    for (DashboardTelling telling : overzicht.getTellingen()) {

      GbaRestElement element = elementen.add(GbaRestElementType.TELLING);

      add(element, GbaRestElementType.CODE, telling.getKey());
      add(element, GbaRestElementType.OMSCHRIJVING, telling.getOms());

      String aantal = astr(telling.getAantal());

      if (telling.getAantal() < 0) {
        aantal = "N.v.t.";
      }

      add(element, GbaRestElementType.AANTAL, aantal);
    }

    return antwoord;
  }
}
