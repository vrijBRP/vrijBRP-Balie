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

package nl.procura.gba.web.modules.bs.overlijden.buitenland.processen;

import static nl.procura.standard.Globalfunctions.fil;

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.modules.bs.common.BsProcessen;
import nl.procura.gba.web.modules.bs.common.pages.BsProcesStatus;
import nl.procura.gba.web.modules.bs.overlijden.buitenland.page1.Page1Overlijden;
import nl.procura.gba.web.modules.bs.overlijden.buitenland.page10.Page10Overlijden;
import nl.procura.gba.web.modules.bs.overlijden.buitenland.page20.Page20Overlijden;
import nl.procura.gba.web.modules.bs.overlijden.buitenland.page50.Page50Overlijden;
import nl.procura.gba.web.modules.bs.overlijden.buitenland.page70.Page70Overlijden;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.overlijden.buitenland.DossierOverlijdenBuitenland;

public class OverlijdenProcessen extends BsProcessen {

  public OverlijdenProcessen(Dossier dossier, GbaApplication gbaApplication) {
    this(gbaApplication);
    setDossier(dossier);
  }

  private OverlijdenProcessen(GbaApplication gbaApplication) {

    super(gbaApplication);

    addProces("Overledene", Page1Overlijden.class);
    addProces("Gerelateerden", Page10Overlijden.class);
    addProces("Brondocument", Page20Overlijden.class);
    addProces("Overzicht", Page50Overlijden.class);
    addProces("Afdrukken", Page70Overlijden.class);
  }

  @Override
  public void initStatusses(GbaApplication app) {

    Dossier d = getDossier();
    DossierOverlijdenBuitenland od = (DossierOverlijdenBuitenland) d.getZaakDossier();

    if (fil(d.getZaakId())) {

      boolean isOverledene = fil(od.getOverledene().getGeslachtsnaam());

      getProces(Page1Overlijden.class).setStatus(
          od.isAangifteVolledig() ? BsProcesStatus.COMPLETE : BsProcesStatus.EMPTY);
      getProces(Page10Overlijden.class).setStatus(isOverledene ? BsProcesStatus.COMPLETE : BsProcesStatus.EMPTY);
      getProces(Page50Overlijden.class).setStatus(isOverledene ? BsProcesStatus.COMPLETE : BsProcesStatus.EMPTY);
      getProces(Page70Overlijden.class).setStatus(
          d.isAktesCorrect() ? BsProcesStatus.COMPLETE : BsProcesStatus.EMPTY);
    }
  }

  @Override
  public void updateStatus() {
  }
}
