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

package nl.procura.gba.web.services.bs.lv.omzetting;

import static nl.procura.standard.Globalfunctions.astr;

import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.web.services.bs.algemeen.akte.DossierAkte;
import nl.procura.gba.web.services.bs.lv.LatereVermelding;
import nl.procura.gba.web.services.bs.lv.Lv;
import nl.procura.gba.web.services.bs.omzetting.DossierOmzetting;

public class LatereVermeldingOmzetting implements LatereVermelding {

  private final List<Lv> aktes = new ArrayList<>();

  /**
   * Omzetting van GPS in huwelijk
   */
  public LatereVermeldingOmzetting(DossierOmzetting omzetting) {

    for (DossierAkte omzettingAkte : omzetting.getDossier().getAktes()) {

      LvOmzetting akte = addOmzetting(omzettingAkte, omzetting);
      akte.getGpsAkte().setNummer(omzetting.getAkteNummerPartnerschap());
      akte.getGpsAkte().setBrpNummer(omzetting.getAkteBrpNummerPartnerschap());
      akte.getGpsAkte().setJaar(astr(omzetting.getAkteJaarPartnerschap()));
      akte.getGpsAkte().setPlaats(omzetting.getAktePlaatsPartnerschap());

      aktes.add(akte);
    }
  }

  @Override
  public List<Lv> getAktes() {
    return aktes;
  }

  private LvOmzetting addOmzetting(DossierAkte dossierAkte, DossierOmzetting omzetting) {

    LvOmzetting akte = new LvOmzetting();
    akte.setDatum(dossierAkte.getDatumIngang());
    akte.setPartner1(omzetting.getPartner1());
    akte.setPartner2(omzetting.getPartner2());
    akte.setAkte(dossierAkte);
    akte.setZaak(omzetting);

    return akte;
  }
}
