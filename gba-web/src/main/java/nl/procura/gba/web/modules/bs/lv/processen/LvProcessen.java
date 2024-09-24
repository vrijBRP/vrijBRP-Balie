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

package nl.procura.gba.web.modules.bs.lv.processen;

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.modules.bs.common.BsProcessen;
import nl.procura.gba.web.modules.bs.lv.page10.Page10Lv;
import nl.procura.gba.web.modules.bs.lv.page20.Page20Lv;
import nl.procura.gba.web.modules.bs.lv.page30.Page30Lv;
import nl.procura.gba.web.modules.bs.lv.page40.Page40Lv;
import nl.procura.gba.web.modules.bs.lv.page50.Page50Lv;
import nl.procura.gba.web.modules.bs.lv.page60.Page60Lv;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.lv.afstamming.DossierLv;

public class LvProcessen extends BsProcessen {

  public LvProcessen(Dossier dossier, GbaApplication gbaApplication) {
    this(gbaApplication);
    setDossier(dossier);
  }

  private LvProcessen(GbaApplication gbaApplication) {

    super(gbaApplication);

    addProces("Kind", Page10Lv.class);
    addProces("Ouder(s)", Page20Lv.class);
    addProces("Soort & huidige situatie", Page30Lv.class);
    addProces("Brondocument", Page40Lv.class);
    addProces("Overzicht", Page50Lv.class);
    addProces("Afdrukken", Page60Lv.class);
  }

  @Override
  public void initStatusses(GbaApplication app) {

    Dossier d = getDossier();
    DossierLv zd = (DossierLv) d.getZaakDossier();

    //    boolean isAanleiding = !VermoedAdresType.ONBEKEND.equals(zd.getVermoedelijkAdres());
    //    boolean isBetreft = !zd.getDossier().getPersonen(DossierPersoonType.BETROKKENE).isEmpty();
    //    boolean isBeoordeling = zd.getBinnenTermijn() != null;
    //    boolean isOnderzoek = zd.isOnderzoekGestart();
    //    boolean isUitbreiding = zd.getFase1Reactie() != null;
    //    boolean isResultaat = !BetrokkeneType.ONBEKEND.equals(zd.getResultaatOnderzoekBetrokkene());
    //
    //    getProces(Page1Onderzoek.class).setStatus(isAanleiding ? COMPLETE : EMPTY);
    //    getProces(Page10Onderzoek.class).setStatus(isAanleiding && isBetreft ? COMPLETE : EMPTY);
    //    getProces(Page20Onderzoek.class).setStatus(isBeoordeling ? COMPLETE : EMPTY);
    //    getProces(Page30Onderzoek.class).setStatus(isOnderzoek ? (isUitbreiding ? COMPLETE : EMPTY) : DISABLED);
    //    getProces(Page40Onderzoek.class).setStatus(isResultaat ? COMPLETE : EMPTY);
    //    getProces(Page60Onderzoek.class).setStatus(isResultaat ? COMPLETE : EMPTY);
  }

  @Override
  public void updateStatus() {
  }
}
