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

package nl.procura.gba.web.modules.bs.onderzoek.processen;

import static nl.procura.gba.web.modules.bs.common.pages.BsProcesStatus.COMPLETE;
import static nl.procura.gba.web.modules.bs.common.pages.BsProcesStatus.DISABLED;
import static nl.procura.gba.web.modules.bs.common.pages.BsProcesStatus.EMPTY;

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.modules.bs.common.BsProcessen;
import nl.procura.gba.web.modules.bs.onderzoek.page1.Page1Onderzoek;
import nl.procura.gba.web.modules.bs.onderzoek.page10.Page10Onderzoek;
import nl.procura.gba.web.modules.bs.onderzoek.page20.Page20Onderzoek;
import nl.procura.gba.web.modules.bs.onderzoek.page30.Page30Onderzoek;
import nl.procura.gba.web.modules.bs.onderzoek.page40.Page40Onderzoek;
import nl.procura.gba.web.modules.bs.onderzoek.page60.Page60Onderzoek;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType;
import nl.procura.gba.web.services.bs.onderzoek.DossierOnderzoek;
import nl.procura.gba.web.services.bs.onderzoek.enums.BetrokkeneType;
import nl.procura.gba.web.services.bs.onderzoek.enums.VermoedAdresType;

public class OnderzoekProcessen extends BsProcessen {

  public OnderzoekProcessen(Dossier dossier, GbaApplication gbaApplication) {
    this(gbaApplication);
    setDossier(dossier);
  }

  private OnderzoekProcessen(GbaApplication gbaApplication) {

    super(gbaApplication);

    addProces("Aanleiding", Page1Onderzoek.class);
    addProces("Betreft", Page10Onderzoek.class);
    addProces("Beoordeling", Page20Onderzoek.class);
    addProces("Uitbreiding", Page30Onderzoek.class);
    addProces("Resultaat", Page40Onderzoek.class);
    addProces("Overzicht", Page60Onderzoek.class);
  }

  @Override
  public void initStatusses(GbaApplication app) {

    Dossier d = getDossier();
    DossierOnderzoek zd = (DossierOnderzoek) d.getZaakDossier();

    boolean isAanleiding = !VermoedAdresType.ONBEKEND.equals(zd.getVermoedelijkAdres());
    boolean isBetreft = !zd.getDossier().getPersonen(DossierPersoonType.BETROKKENE).isEmpty();
    boolean isBeoordeling = zd.getBinnenTermijn() != null;
    boolean isOnderzoek = zd.isOnderzoekGestart();
    boolean isUitbreiding = zd.getFase1Reactie() != null;
    boolean isResultaat = !BetrokkeneType.ONBEKEND.equals(zd.getResultaatOnderzoekBetrokkene());

    getProces(Page1Onderzoek.class).setStatus(isAanleiding ? COMPLETE : EMPTY);
    getProces(Page10Onderzoek.class).setStatus(isAanleiding && isBetreft ? COMPLETE : EMPTY);
    getProces(Page20Onderzoek.class).setStatus(isBeoordeling ? COMPLETE : EMPTY);
    getProces(Page30Onderzoek.class).setStatus(isOnderzoek ? (isUitbreiding ? COMPLETE : EMPTY) : DISABLED);
    getProces(Page40Onderzoek.class).setStatus(isResultaat ? COMPLETE : EMPTY);
    getProces(Page60Onderzoek.class).setStatus(isResultaat ? COMPLETE : EMPTY);
  }

  @Override
  public void updateStatus() {
  }
}
