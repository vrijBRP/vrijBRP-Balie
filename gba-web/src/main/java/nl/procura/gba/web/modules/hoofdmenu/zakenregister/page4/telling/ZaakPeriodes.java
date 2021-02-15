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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.page4.telling;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.web.common.misc.ZaakPeriode;

public class ZaakPeriodes {

  private final List<ZaakPeriode> historischePeriodes = new ArrayList<>();
  private final List<ZaakPeriode> allePeriodes        = new ArrayList<>();

  public ZaakPeriodes() {

    historischePeriodes.addAll(
        asList(new Gisteren(), new Vandaag(), new VorigeWeek(), new DezeWeek(), new VorigeMaand(),
            new DezeMaand(), new VorigJaar(), new DitJaar(), new Anders()));

    allePeriodes.addAll(asList(new Vandaag(), new Gisteren(), new VorigeWeek(), new DezeWeek(), new VolgendeWeek(),
        new VorigeMaand(), new DezeMaand(), new VolgendeMaand(), new VorigJaar(),
        new DitJaar(), new VolgendJaar(), new Anders()));
  }

  public List<ZaakPeriode> getHistorischePeriodes() {
    return historischePeriodes;
  }

  public List<ZaakPeriode> getAllePeriodes() {
    return allePeriodes;
  }
}
