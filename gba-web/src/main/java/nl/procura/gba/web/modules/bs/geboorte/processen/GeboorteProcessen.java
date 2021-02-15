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

package nl.procura.gba.web.modules.bs.geboorte.processen;

import static nl.procura.gba.web.modules.bs.common.pages.BsProcesStatus.*;
import static nl.procura.standard.Globalfunctions.fil;
import static nl.procura.standard.Globalfunctions.pos;

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.modules.bs.common.BsProcessen;
import nl.procura.gba.web.modules.bs.common.pages.BsProcesStatus;
import nl.procura.gba.web.modules.bs.common.pages.BsProcesStatussen;
import nl.procura.gba.web.modules.bs.common.pages.BsProcesStatussen.BspProcesStatusEntry;
import nl.procura.gba.web.modules.bs.geboorte.page1.Page1Geboorte;
import nl.procura.gba.web.modules.bs.geboorte.page20.Page20Geboorte;
import nl.procura.gba.web.modules.bs.geboorte.page30.Page30Geboorte;
import nl.procura.gba.web.modules.bs.geboorte.page35.Page35Geboorte;
import nl.procura.gba.web.modules.bs.geboorte.page40.Page40Geboorte;
import nl.procura.gba.web.modules.bs.geboorte.page60.Page60Geboorte;
import nl.procura.gba.web.modules.bs.geboorte.page70.Page70Geboorte;
import nl.procura.gba.web.modules.bs.geboorte.page80.Page80Geboorte;
import nl.procura.gba.web.modules.bs.geboorte.page82.Page82Geboorte;
import nl.procura.gba.web.modules.bs.geboorte.page84.Page84Geboorte;
import nl.procura.gba.web.modules.bs.geboorte.page90.Page90Geboorte;
import nl.procura.gba.web.modules.bs.geboorte.page95.Page95Geboorte;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.erkenning.ErkenningsType;
import nl.procura.gba.web.services.bs.geboorte.DossierGeboorte;
import nl.procura.gba.web.services.zaken.algemeen.commentaar.ZaakCommentaren;

public class GeboorteProcessen extends BsProcessen {

  public GeboorteProcessen(Dossier dossier, GbaApplication gbaApplication) {
    this(gbaApplication);
    setDossier(dossier);
  }

  private GeboorteProcessen(GbaApplication gbaApplication) {

    super(gbaApplication);

    addProces("Aangever", Page1Geboorte.class);
    addProces("Aangifte", Page20Geboorte.class);
    addProces("Moeder", Page30Geboorte.class);
    addProces("Gezinssituatie", Page35Geboorte.class);
    addProces("Vader / Duo-moeder", Page40Geboorte.class);
    addProces("Afstamming", Page60Geboorte.class);
    addProces("Namenrecht", Page70Geboorte.class);
    addProces("Kind", Page80Geboorte.class);
    addProces("Erkenning bij geboorte", Page82Geboorte.class);
    addProces("Overzicht", Page84Geboorte.class);
    addProces("Aktenummer", Page90Geboorte.class);
    addProces("Afdrukken", Page95Geboorte.class);
  }

  public static BsProcesStatussen getProcesStatussen(Dossier dossier) {

    BsProcesStatussen statussen = new BsProcesStatussen();

    if (fil(dossier.getZaakId())) {

      DossierGeboorte geboorte = (DossierGeboorte) dossier.getZaakDossier();

      boolean isAangever = fil(geboorte.getAangever().getGeslachtsnaam());
      boolean isGeboorten = pos(geboorte.getKinderen().size());
      boolean isMoeder = fil(geboorte.getMoeder().getGeslachtsnaam());
      boolean isVader = fil(geboorte.getVader().getGeslachtsnaam());
      boolean isAfstamming = pos(geboorte.getLandAfstammingsRecht().getValue());
      boolean isNamenrecht = pos(geboorte.getLandNaamRecht().getValue());
      boolean isKinderen = !geboorte.getKinderen().isEmpty() && geboorte.getKinderen().iterator().next().isVolledig();
      boolean isAktenummers = dossier.isAktesCorrect();

      statussen.add(Page1Geboorte.class, isAangever, "");
      statussen.add(Page20Geboorte.class, isGeboorten, "");
      statussen.add(Page30Geboorte.class, isGeboorten, "");
      statussen.add(Page30Geboorte.class, isMoeder, "");

      boolean isErkenningBijGeboorte = geboorte.getErkenningsType() == ErkenningsType.ERKENNING_BIJ_AANGIFTE;
      statussen.add(Page40Geboorte.class, isMoeder, "")
          .setStatus(isErkenningBijGeboorte ? DISABLED : (isVader ? COMPLETE : EMPTY));

      statussen.add(Page60Geboorte.class, isAfstamming, "");
      statussen.add(Page70Geboorte.class, isNamenrecht, "");
      statussen.add(Page80Geboorte.class, isKinderen, "");

      statussen.add(Page82Geboorte.class, isErkenningBijGeboorte, "")
          .setStatus(isErkenningBijGeboorte ? EMPTY : DISABLED);
      statussen.add(Page84Geboorte.class, isAktenummers, "");
      statussen.add(Page90Geboorte.class, isAktenummers, "");
      statussen.add(Page95Geboorte.class, isAktenummers, "");

      if (!geboorte.isVaderVanToepassing()) {
        statussen.add(Page40Geboorte.class, true, "").setStatus(BsProcesStatus.DISABLED);
      }
    }

    return statussen;
  }

  @Override
  public void addProces(String id, Class pageClass) {
    super.addProces(id, pageClass);
  }

  @Override
  public void initStatusses(GbaApplication app) {
    updateStatus();
  }

  @Override
  public void updateStatus() {

    BsProcesStatussen procesStatussen = getProcesStatussen(getDossier());
    for (BspProcesStatusEntry entry : procesStatussen.getList()) {
      getProces(entry.getCl()).setStatus(entry.getStatus(), entry.getMessage());
    }

    if (isPaginaReset(getDossier())) {
      goToFirst();
    }

    ZaakCommentaren commentaar = getDossier().getCommentaren();
    commentaar.verwijderen().toevoegenWarn(procesStatussen.getMessages());
  }
}
