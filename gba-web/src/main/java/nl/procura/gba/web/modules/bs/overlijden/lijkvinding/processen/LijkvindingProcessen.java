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

package nl.procura.gba.web.modules.bs.overlijden.lijkvinding.processen;

import static nl.procura.gba.web.modules.bs.common.pages.BsProcesStatus.*;
import static nl.procura.standard.Globalfunctions.fil;

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.modules.bs.common.BsProcessen;
import nl.procura.gba.web.modules.bs.common.pages.BsProcesStatussen;
import nl.procura.gba.web.modules.bs.common.pages.BsProcesStatussen.BspProcesStatusEntry;
import nl.procura.gba.web.modules.bs.overlijden.lijkvinding.page20.Page20Lijkvinding;
import nl.procura.gba.web.modules.bs.overlijden.lijkvinding.page30.Page30Lijkvinding;
import nl.procura.gba.web.modules.bs.overlijden.lijkvinding.page40.Page40Lijkvinding;
import nl.procura.gba.web.modules.bs.overlijden.lijkvinding.page50.Page50Lijkvinding;
import nl.procura.gba.web.modules.bs.overlijden.lijkvinding.page60.Page60Lijkvinding;
import nl.procura.gba.web.modules.bs.overlijden.lijkvinding.page70.Page70Lijkvinding;
import nl.procura.gba.web.modules.bs.overlijden.lijkvinding.page80.Page80Lijkvinding;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.overlijden.lijkvinding.DossierLijkvinding;
import nl.procura.gba.web.services.zaken.algemeen.commentaar.ZaakCommentaren;

public class LijkvindingProcessen extends BsProcessen {

  public LijkvindingProcessen(Dossier dossier, GbaApplication gbaApplication) {

    this(gbaApplication);

    setDossier(dossier);
  }

  private LijkvindingProcessen(GbaApplication gbaApplication) {

    super(gbaApplication);

    addProces("Aangifte", Page20Lijkvinding.class);
    addProces("Overledene", Page30Lijkvinding.class);
    addProces("Gerelateerden", Page40Lijkvinding.class);
    addProces("Overzicht", Page50Lijkvinding.class);
    addProces("Aktenummer", Page60Lijkvinding.class);
    addProces("Correspondentie", Page80Lijkvinding.class);
    addProces("Afdrukken", Page70Lijkvinding.class);
  }

  public static BsProcesStatussen getProcesStatussen(Dossier dossier) {

    BsProcesStatussen statussen = new BsProcesStatussen();

    if (fil(dossier.getZaakId())) {
      DossierLijkvinding od = (DossierLijkvinding) dossier.getZaakDossier();
      boolean isOverledene = fil(od.getOverledene().getGeslachtsnaam());
      boolean verzoekInd = od.getVerzoek().isVerzoekInd();
      boolean aangifteVolledig = od.isAangifteVolledig();

      statussen.add(Page80Lijkvinding.class, verzoekInd ? (aangifteVolledig ? COMPLETE : EMPTY) : DISABLED);
      statussen.add(Page30Lijkvinding.class, aangifteVolledig ? COMPLETE : EMPTY);
      statussen.add(Page40Lijkvinding.class, isOverledene ? COMPLETE : EMPTY);
      statussen.add(Page50Lijkvinding.class, isOverledene ? COMPLETE : EMPTY);
      statussen.add(Page60Lijkvinding.class, dossier.isAktesCorrect() ? COMPLETE : EMPTY);
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
