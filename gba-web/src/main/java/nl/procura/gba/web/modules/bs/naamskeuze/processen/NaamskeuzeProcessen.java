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

package nl.procura.gba.web.modules.bs.naamskeuze.processen;

import static nl.procura.standard.Globalfunctions.fil;
import static nl.procura.standard.Globalfunctions.pos;

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.modules.bs.common.BsProcessen;
import nl.procura.gba.web.modules.bs.common.pages.BsProcesStatus;
import nl.procura.gba.web.modules.bs.naamskeuze.page1.Page1Naamskeuze;
import nl.procura.gba.web.modules.bs.naamskeuze.page15.Page15Naamskeuze;
import nl.procura.gba.web.modules.bs.naamskeuze.page30.Page30Naamskeuze;
import nl.procura.gba.web.modules.bs.naamskeuze.page32.Page32Naamskeuze;
import nl.procura.gba.web.modules.bs.naamskeuze.page35.Page35Naamskeuze;
import nl.procura.gba.web.modules.bs.naamskeuze.page40.Page40Naamskeuze;
import nl.procura.gba.web.modules.bs.naamskeuze.page5.Page5Naamskeuze;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.naamskeuze.DossierNaamskeuze;

public class NaamskeuzeProcessen extends BsProcessen {

  public NaamskeuzeProcessen(Dossier dossier, GbaApplication gbaApplication) {
    this(gbaApplication);
    setDossier(dossier);
  }

  private NaamskeuzeProcessen(GbaApplication gbaApplication) {

    super(gbaApplication);

    addProces("Moeder", Page1Naamskeuze.class);
    addProces("Kind(eren)", Page5Naamskeuze.class);
    addProces("Partner / andere ouder", Page15Naamskeuze.class);
    addProces("Namenrecht en keuze", Page30Naamskeuze.class);
    addProces("Aktenummers", Page32Naamskeuze.class);
    addProces("Overzicht", Page35Naamskeuze.class);
    addProces("Afdrukken", Page40Naamskeuze.class);
  }

  @Override
  public void initStatusses(GbaApplication app) {

    Dossier d = getDossier();
    DossierNaamskeuze zd = (DossierNaamskeuze) d.getZaakDossier();

    if (fil(zd.getDossier().getZaakId())) {
      boolean isMoeder = fil(zd.getMoeder().getGeslachtsnaam());
      boolean isKinderen = pos(zd.getKinderen().size());
      boolean isErkenner = fil(zd.getPartner().getGeslachtsnaam());
      boolean isAktenummers = d.isAktesCorrect();

      getProces(Page1Naamskeuze.class).setStatus(isMoeder ? BsProcesStatus.COMPLETE : BsProcesStatus.EMPTY);
      getProces(Page5Naamskeuze.class).setStatus(isKinderen ? BsProcesStatus.COMPLETE : BsProcesStatus.EMPTY);
      getProces(Page15Naamskeuze.class).setStatus(isErkenner ? BsProcesStatus.COMPLETE : BsProcesStatus.EMPTY);
      getProces(Page32Naamskeuze.class).setStatus(isAktenummers ? BsProcesStatus.COMPLETE : BsProcesStatus.EMPTY);
      getProces(Page35Naamskeuze.class).setStatus(isAktenummers ? BsProcesStatus.COMPLETE : BsProcesStatus.EMPTY);
    }
  }

  @Override
  public void updateStatus() {
  }
}
