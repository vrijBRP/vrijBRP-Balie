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

package nl.procura.gba.web.modules.bs.overlijden.levenloos.processen;

import static nl.procura.gba.web.modules.bs.common.pages.BsProcesStatus.*;
import static nl.procura.standard.Globalfunctions.fil;
import static nl.procura.standard.Globalfunctions.pos;

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.modules.bs.common.BsProcessen;
import nl.procura.gba.web.modules.bs.common.pages.BsProcesStatus;
import nl.procura.gba.web.modules.bs.overlijden.levenloos.page1.Page1Levenloos;
import nl.procura.gba.web.modules.bs.overlijden.levenloos.page20.Page20Levenloos;
import nl.procura.gba.web.modules.bs.overlijden.levenloos.page30.Page30Levenloos;
import nl.procura.gba.web.modules.bs.overlijden.levenloos.page35.Page35Levenloos;
import nl.procura.gba.web.modules.bs.overlijden.levenloos.page40.Page40Levenloos;
import nl.procura.gba.web.modules.bs.overlijden.levenloos.page60.Page60Levenloos;
import nl.procura.gba.web.modules.bs.overlijden.levenloos.page70.Page70Levenloos;
import nl.procura.gba.web.modules.bs.overlijden.levenloos.page80.Page80Levenloos;
import nl.procura.gba.web.modules.bs.overlijden.levenloos.page82.Page82Levenloos;
import nl.procura.gba.web.modules.bs.overlijden.levenloos.page84.Page84Levenloos;
import nl.procura.gba.web.modules.bs.overlijden.levenloos.page90.Page90Levenloos;
import nl.procura.gba.web.modules.bs.overlijden.levenloos.page95.Page95Levenloos;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.erkenning.ErkenningsType;
import nl.procura.gba.web.services.bs.geboorte.DossierGeboorte;

public class LevenloosProcessen extends BsProcessen {

  private static final String VADER = "Vader / Duo-moeder";

  public LevenloosProcessen(Dossier dossier, GbaApplication gbaApplication) {
    this(gbaApplication);
    setDossier(dossier);
  }

  private LevenloosProcessen(GbaApplication gbaApplication) {

    super(gbaApplication);

    addProces("Aangever", Page1Levenloos.class);
    addProces("Aangifte", Page20Levenloos.class);
    addProces("Moeder", Page30Levenloos.class);
    addProces("Gezinssituatie", Page35Levenloos.class);
    addProces(VADER, Page40Levenloos.class);
    addProces("Afstamming", Page60Levenloos.class);
    addProces("Namenrecht", Page70Levenloos.class);
    addProces("Kind", Page80Levenloos.class);
    addProces("Erkenning bij geboorte", Page82Levenloos.class);
    addProces("Overzicht", Page84Levenloos.class);
    addProces("Aktenummer", Page90Levenloos.class);
    addProces("Afdrukken", Page95Levenloos.class);
  }

  @Override
  public void initStatusses(GbaApplication app) {

    Dossier d = getDossier();
    DossierGeboorte zd = (DossierGeboorte) d.getZaakDossier();

    if (fil(d.getZaakId())) {

      boolean isAangever = fil(zd.getAangever().getGeslachtsnaam());
      boolean isGeboorten = pos(zd.getKinderen().size());
      boolean isMoeder = fil(zd.getMoeder().getGeslachtsnaam());
      boolean isVader = fil(zd.getVader().getGeslachtsnaam());
      boolean isAfstamming = pos(zd.getLandAfstammingsRecht().getValue());
      boolean isNamenrecht = pos(zd.getLandNaamRecht().getValue());
      boolean isKinderen = (zd.getKinderen().size() > 0) && zd.getKinderen().iterator().next().isVolledig();
      boolean isAktenummers = d.isAktesCorrect();

      getProces(Page1Levenloos.class).setStatus(isAangever ? COMPLETE : EMPTY);
      getProces(Page20Levenloos.class).setStatus(isGeboorten ? COMPLETE : EMPTY);
      getProces(Page30Levenloos.class).setStatus(isGeboorten ? COMPLETE : EMPTY);
      getProces(Page30Levenloos.class).setStatus(isMoeder ? COMPLETE : EMPTY);

      boolean isErkenningBijGeboorte = zd.getErkenningsType() == ErkenningsType.ERKENNING_BIJ_AANGIFTE;

      getProces(Page40Levenloos.class).setStatus(
          isErkenningBijGeboorte ? DISABLED : (isVader ? COMPLETE : EMPTY));
      getProces(Page60Levenloos.class).setStatus(isAfstamming ? COMPLETE : EMPTY);
      getProces(Page70Levenloos.class).setStatus(isNamenrecht ? COMPLETE : EMPTY);
      getProces(Page80Levenloos.class).setStatus(isKinderen ? COMPLETE : EMPTY);
      getProces(Page82Levenloos.class).setStatus(isErkenningBijGeboorte ? EMPTY : DISABLED);
      getProces(Page84Levenloos.class).setStatus(isAktenummers ? COMPLETE : EMPTY);
      getProces(Page90Levenloos.class).setStatus(isAktenummers ? COMPLETE : EMPTY);
      getProces(Page95Levenloos.class).setStatus(isAktenummers ? COMPLETE : EMPTY);

      if (!zd.isVaderVanToepassing()) {
        getProces(Page40Levenloos.class).forceStatus(BsProcesStatus.DISABLED);
      }
    }
  }

  @Override
  public void updateStatus() {
  }
}
