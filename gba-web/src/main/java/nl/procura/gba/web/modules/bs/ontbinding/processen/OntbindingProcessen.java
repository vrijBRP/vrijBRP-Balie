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

package nl.procura.gba.web.modules.bs.ontbinding.processen;

import static nl.procura.gba.web.modules.bs.common.pages.BsProcesStatus.COMPLETE;
import static nl.procura.gba.web.modules.bs.common.pages.BsProcesStatus.EMPTY;
import static nl.procura.gba.web.services.bs.algemeen.enums.SoortVerbintenis.GPS;
import static nl.procura.gba.web.services.bs.algemeen.enums.SoortVerbintenis.HUWELIJK;
import static nl.procura.standard.Globalfunctions.fil;

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.modules.bs.common.BsProcessen;
import nl.procura.gba.web.modules.bs.ontbinding.page1.Page1Ontbinding;
import nl.procura.gba.web.modules.bs.ontbinding.page10.Page10Ontbinding;
import nl.procura.gba.web.modules.bs.ontbinding.page20.Page20Ontbinding;
import nl.procura.gba.web.modules.bs.ontbinding.page30.Page30Ontbinding;
import nl.procura.gba.web.modules.bs.ontbinding.page40.Page40Ontbinding;
import nl.procura.gba.web.modules.bs.ontbinding.page50.Page50Ontbinding;
import nl.procura.gba.web.modules.bs.ontbinding.page60.Page60Ontbinding;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.ontbinding.DossierOntbinding;

public class OntbindingProcessen extends BsProcessen {

  public OntbindingProcessen(Dossier dossier, GbaApplication gbaApplication) {
    this(gbaApplication);
    setDossier(dossier);
  }

  private OntbindingProcessen(GbaApplication gbaApplication) {

    super(gbaApplication);

    addProces("Partner 1", Page1Ontbinding.class);
    addProces("Partner 2", Page10Ontbinding.class);
    addProces("Huidige situatie", Page20Ontbinding.class);
    addProces("Brondocument", Page30Ontbinding.class);
    addProces("Naam(gebruik)", Page40Ontbinding.class);
    addProces("Overzicht", Page50Ontbinding.class);
    addProces("Afdrukken", Page60Ontbinding.class);
  }

  @Override
  public void initStatusses(GbaApplication app) {
    updateStatus();
  }

  @Override
  public void updateStatus() {

    DossierOntbinding d = getOntbinding();

    if (fil(getDossier().getZaakId())) {

      boolean isPartner1 = d.getPartner1().isVolledig();
      boolean isPartner2 = d.getPartner2().isVolledig();
      boolean isHuidigeSituatie = d.getSoortVerbintenis() != null;
      boolean isBronDocument1 = d.getSoortVerbintenis() == HUWELIJK && d.getDatumGewijsde().getLongDate() > 0;
      boolean isBronDocument2 = d.getSoortVerbintenis() == GPS && d.getDatumVerklaring().getLongDate() > 0;
      boolean isBronDocument = (isBronDocument1 || isBronDocument2);
      boolean isNaam = fil(d.getNaamPartner1()) && fil(d.getNaamPartner2());

      getProces(Page1Ontbinding.class).setStatus(isPartner1 ? COMPLETE : EMPTY);
      getProces(Page10Ontbinding.class).setStatus(isPartner2 ? COMPLETE : EMPTY);
      getProces(Page20Ontbinding.class).setStatus(isHuidigeSituatie ? COMPLETE : EMPTY);
      getProces(Page30Ontbinding.class).setStatus(isBronDocument ? COMPLETE : EMPTY);
      getProces(Page40Ontbinding.class).setStatus(isNaam ? COMPLETE : EMPTY);
      getProces(Page50Ontbinding.class).setStatus(isNaam ? COMPLETE : EMPTY);
    }
  }

  private DossierOntbinding getOntbinding() {
    return (DossierOntbinding) getDossier().getZaakDossier();
  }
}
