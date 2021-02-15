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

package nl.procura.gba.web.modules.bs.erkenning.processen;

import static nl.procura.standard.Globalfunctions.fil;
import static nl.procura.standard.Globalfunctions.pos;

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.modules.bs.common.BsProcessen;
import nl.procura.gba.web.modules.bs.common.pages.BsProcesStatus;
import nl.procura.gba.web.modules.bs.erkenning.page1.Page1Erkenning;
import nl.procura.gba.web.modules.bs.erkenning.page15.Page15Erkenning;
import nl.procura.gba.web.modules.bs.erkenning.page20.Page20Erkenning;
import nl.procura.gba.web.modules.bs.erkenning.page25.Page25Erkenning;
import nl.procura.gba.web.modules.bs.erkenning.page30.Page30Erkenning;
import nl.procura.gba.web.modules.bs.erkenning.page32.Page32Erkenning;
import nl.procura.gba.web.modules.bs.erkenning.page35.Page35Erkenning;
import nl.procura.gba.web.modules.bs.erkenning.page40.Page40Erkenning;
import nl.procura.gba.web.modules.bs.erkenning.page45.Page45Erkenning;
import nl.procura.gba.web.modules.bs.erkenning.page5.Page5Erkenning;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.erkenning.DossierErkenning;

public class ErkenningProcessen extends BsProcessen {

  public ErkenningProcessen(Dossier dossier, GbaApplication gbaApplication) {
    this(gbaApplication);
    setDossier(dossier);
  }

  private ErkenningProcessen(GbaApplication gbaApplication) {

    super(gbaApplication);

    addProces("Moeder", Page1Erkenning.class);
    addProces("Kind(eren)", Page5Erkenning.class);
    addProces("Erkenner", Page15Erkenning.class);
    addProces("Vereisten", Page45Erkenning.class);
    addProces("Afstamming", Page20Erkenning.class);
    addProces("Toestemming", Page25Erkenning.class);
    addProces("Namenrecht", Page30Erkenning.class);
    addProces("Aktenummers", Page32Erkenning.class);
    addProces("Overzicht", Page35Erkenning.class);
    addProces("Afdrukken", Page40Erkenning.class);
  }

  @Override
  public void initStatusses(GbaApplication app) {

    Dossier d = getDossier();
    DossierErkenning zd = (DossierErkenning) d.getZaakDossier();

    if (fil(zd.getDossier().getZaakId())) {

      boolean isMoeder = fil(zd.getMoeder().getGeslachtsnaam());
      boolean isKinderen = pos(zd.getKinderen().size());
      boolean isErkenner = fil(zd.getErkenner().getGeslachtsnaam());

      //          boolean isAfstamming  = fil (zd.getAfstammingsrecht ());
      //          boolean isToestemming = fil (zd.getToestemmingsrecht ());
      //          boolean isNamenrecht  = fil (zd.getNamenrecht ());

      boolean isAktenummers = d.isAktesCorrect();

      getProces(Page1Erkenning.class).setStatus(isMoeder ? BsProcesStatus.COMPLETE : BsProcesStatus.EMPTY);
      getProces(Page5Erkenning.class).setStatus(isKinderen ? BsProcesStatus.COMPLETE : BsProcesStatus.EMPTY);
      getProces(Page15Erkenning.class).setStatus(isErkenner ? BsProcesStatus.COMPLETE : BsProcesStatus.EMPTY);

      //          getProces (Page20Erkenning.class).setStatus (isAfstamming ? BsProcesStatus.COMPLETE : BsProcesStatus.EMPTY);
      //          getProces (Page25Erkenning.class).setStatus (isToestemming ? BsProcesStatus.COMPLETE : BsProcesStatus.EMPTY);
      //          getProces (Page30Erkenning.class).setStatus (isNamenrecht ? BsProcesStatus.COMPLETE : BsProcesStatus.EMPTY);

      getProces(Page32Erkenning.class).setStatus(isAktenummers ? BsProcesStatus.COMPLETE : BsProcesStatus.EMPTY);
      getProces(Page35Erkenning.class).setStatus(isAktenummers ? BsProcesStatus.COMPLETE : BsProcesStatus.EMPTY);
    }
  }

  @Override
  public void updateStatus() {
  }
}
