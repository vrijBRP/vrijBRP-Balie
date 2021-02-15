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

package nl.procura.gba.web.services.bs.lv.ontbinding;

import static nl.procura.standard.Globalfunctions.astr;

import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.web.services.bs.lv.LatereVermelding;
import nl.procura.gba.web.services.bs.ontbinding.DossierOntbinding;

public class LatereVermeldingOntbinding implements LatereVermelding<LvOntbinding> {

  private final List<LvOntbinding> aktes = new ArrayList<>();

  public LatereVermeldingOntbinding(DossierOntbinding ontbinding) {
    LvOntbinding akte = addOntbinding(ontbinding);
    akte.getRechtsfeitAkte().setNummer(ontbinding.getBsAkteNummerVerbintenis());
    akte.getRechtsfeitAkte().setBrpNummer(ontbinding.getBrpAkteNummerVerbintenis());
    akte.getRechtsfeitAkte().setJaar(astr(ontbinding.getAkteJaarVerbintenis()));
    akte.getRechtsfeitAkte().setPlaats(ontbinding.getAktePlaatsVerbintenis());
    aktes.add(akte);
  }

  @Override
  public List<LvOntbinding> getAktes() {
    return aktes;
  }

  private LvOntbinding addOntbinding(DossierOntbinding ontbinding) {
    LvOntbinding akte = new LvOntbinding();
    akte.setDatum(ontbinding.getAkteDatum());
    akte.setPartner1(ontbinding.getPartner1());
    akte.setPartner2(ontbinding.getPartner2());
    akte.setZaak(ontbinding);

    return akte;
  }
}
