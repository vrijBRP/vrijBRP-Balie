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

package nl.procura.gba.web.modules.bs.overlijden.gemeente.processen;

import static nl.procura.gba.web.modules.bs.common.pages.BsProcesStatus.*;
import static nl.procura.standard.Globalfunctions.fil;

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.modules.bs.common.BsProcessen;
import nl.procura.gba.web.modules.bs.common.pages.BsProcesStatussen;
import nl.procura.gba.web.modules.bs.common.pages.BsProcesStatussen.BspProcesStatusEntry;
import nl.procura.gba.web.modules.bs.overlijden.gemeente.page1.Page1Overlijden;
import nl.procura.gba.web.modules.bs.overlijden.gemeente.page20.Page20Overlijden;
import nl.procura.gba.web.modules.bs.overlijden.gemeente.page30.Page30Overlijden;
import nl.procura.gba.web.modules.bs.overlijden.gemeente.page40.Page40Overlijden;
import nl.procura.gba.web.modules.bs.overlijden.gemeente.page50.Page50Overlijden;
import nl.procura.gba.web.modules.bs.overlijden.gemeente.page60.Page60Overlijden;
import nl.procura.gba.web.modules.bs.overlijden.gemeente.page70.Page70Overlijden;
import nl.procura.gba.web.modules.bs.overlijden.gemeente.page80.Page80Overlijden;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.overlijden.gemeente.DossierOverlijdenGemeente;
import nl.procura.gba.web.services.zaken.algemeen.commentaar.ZaakCommentaren;

public class OverlijdenGemeenteProcessen extends BsProcessen {

  public OverlijdenGemeenteProcessen(Dossier dossier, GbaApplication gbaApplication) {
    this(gbaApplication);
    setDossier(dossier);
  }

  private OverlijdenGemeenteProcessen(GbaApplication gbaApplication) {
    super(gbaApplication);

    addProces("Aangever", Page1Overlijden.class);
    addProces("Aangifte", Page20Overlijden.class);
    addProces("Correspondentie", Page80Overlijden.class);
    addProces("Overledene", Page30Overlijden.class);
    addProces("Gerelateerden", Page40Overlijden.class);
    addProces("Overzicht", Page50Overlijden.class);
    addProces("Aktenummer", Page60Overlijden.class);
    addProces("Afdrukken", Page70Overlijden.class);
  }

  public static BsProcesStatussen getProcesStatussen(Dossier dossier) {

    BsProcesStatussen statussen = new BsProcesStatussen();

    if (fil(dossier.getZaakId())) {
      DossierOverlijdenGemeente od = (DossierOverlijdenGemeente) dossier.getZaakDossier();
      boolean isAangever = fil(od.getAangever().getGeslachtsnaam());
      boolean isOverledene = fil(od.getOverledene().getGeslachtsnaam());
      boolean verzoekInd = od.getVerzoek().isVerzoekInd();
      boolean aangifteVolledig = od.isAangifteVolledig();

      statussen.add(Page1Overlijden.class, isAangever ? COMPLETE : EMPTY);
      statussen.add(Page20Overlijden.class, isAangever ? COMPLETE : EMPTY);
      statussen.add(Page80Overlijden.class, verzoekInd ? (aangifteVolledig ? COMPLETE : EMPTY) : DISABLED);
      statussen.add(Page30Overlijden.class, aangifteVolledig ? COMPLETE : EMPTY);
      statussen.add(Page40Overlijden.class, isOverledene ? COMPLETE : EMPTY);
      statussen.add(Page50Overlijden.class, isOverledene ? COMPLETE : EMPTY);
      statussen.add(Page60Overlijden.class, dossier.isAktesCorrect() ? COMPLETE : EMPTY);
    }

    return statussen;
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
