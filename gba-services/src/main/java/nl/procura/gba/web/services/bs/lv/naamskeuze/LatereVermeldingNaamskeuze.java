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

package nl.procura.gba.web.services.bs.lv.naamskeuze;

import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.KIND;
import static nl.procura.standard.Globalfunctions.astr;

import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.web.common.misc.Landelijk;
import nl.procura.gba.web.services.bs.algemeen.akte.DossierAkte;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.lv.LatereVermelding;
import nl.procura.gba.web.services.bs.naamskeuze.DossierNaamskeuze;

public class LatereVermeldingNaamskeuze implements LatereVermelding<LvNaamskeuze> {

  private List<LvNaamskeuze> aktes = new ArrayList<>();

  /**
   * Naamskeuze van bestaand kind
   */
  public LatereVermeldingNaamskeuze(DossierNaamskeuze naamskeuze) {
    for (DossierAkte naamskeuzeAkte : naamskeuze.getDossier().getAktes()) {
      LvNaamskeuze akte = addNaamskeuze(naamskeuzeAkte, naamskeuze);
      for (DossierPersoon p : naamskeuzeAkte.getPersonen()) {
        if (p.getDossierPersoonType().is(KIND)) {
          akte.getGeboorteAkte().setNummer(p.getGeboorteAkteNummer());
          akte.getGeboorteAkte().setBrpNummer(p.getGeboorteAkteBrpNummer());
          akte.getGeboorteAkte().setJaar(astr(p.getGeboorteAkteJaar()));
          akte.getGeboorteAkte().setPlaats(p.getGeboorteAktePlaats());
        }
      }

      aktes.add(akte);
    }
  }

  @Override
  public List<LvNaamskeuze> getAktes() {
    return aktes;
  }

  public void setAktes(List<LvNaamskeuze> aktes) {
    this.aktes = aktes;
  }

  /**
   * Naamskeuze binnen proweb
   */
  private LvNaamskeuze addNaamskeuze(DossierAkte dossierAkte, DossierNaamskeuze naamskeuze) {
    LvNaamskeuze akte = new LvNaamskeuze();
    akte.setKind(dossierAkte.getAktePersoon());
    akte.setLand(Landelijk.getNederland());
    akte.setGemeente(naamskeuze.getGemeente());
    akte.setBuitenlandsePlaats("");
    akte.setDatum(dossierAkte.getDatumIngang());
    akte.setGemeente(naamskeuze.getGemeente());
    akte.setMoeder(naamskeuze.getMoeder());
    akte.setPartner(naamskeuze.getPartner());
    akte.setNaamskeuzeType(naamskeuze.getDossierNaamskeuzeType());
    akte.setTitel(naamskeuze.getKeuzeTitel());
    akte.setVoorvoegsel(naamskeuze.getKeuzeVoorvoegsel());
    akte.setGeslachtsnaam(naamskeuze.getKeuzeGeslachtsnaam());
    akte.setAkte(dossierAkte);
    return akte;
  }
}
