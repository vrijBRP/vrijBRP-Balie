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

package nl.procura.gba.web.services.bs.lv.erkenning;

import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.KIND;
import static nl.procura.standard.Globalfunctions.astr;

import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.web.common.misc.Landelijk;
import nl.procura.gba.web.services.bs.algemeen.akte.DossierAkte;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.erkenning.DossierErkenning;
import nl.procura.gba.web.services.bs.erkenning.NaamskeuzeVanToepassingType;
import nl.procura.gba.web.services.bs.lv.LatereVermelding;

public class LatereVermeldingErkenning implements LatereVermelding<LvErkenning> {

  private List<LvErkenning> aktes = new ArrayList<>();

  /**
   * Erkenning van bestaande kinderen
   */
  public LatereVermeldingErkenning(DossierErkenning erkenning) {

    int akteVolgcode = 0;
    for (DossierAkte erkenningAkte : erkenning.getDossier().getAktes()) {

      akteVolgcode++;
      LvErkenning akte = addErkenning(akteVolgcode, erkenningAkte, erkenning);

      for (DossierPersoon p : erkenningAkte.getPersonen()) {
        if (p.getDossierPersoonType().is(KIND)) {
          akte.getGeboorteAkte().setNummer(p.getGeboorteAkteNummer());
          akte.getGeboorteAkte().setBrpNummer(p.getGeboorteAkteBrpNummer());
          akte.getGeboorteAkte().setJaar(astr(p.getGeboorteAkteJaar()));
          akte.getGeboorteAkte().setPlaats(p.getGeboorteAktePlaats());
        }

        setNaamskeuze(akteVolgcode, erkenning.getNaamskeuzeType(), erkenningAkte, akte);
      }

      aktes.add(akte);
    }
  }

  @Override
  public List<LvErkenning> getAktes() {
    return aktes;
  }

  public void setAktes(List<LvErkenning> aktes) {
    this.aktes = aktes;
  }

  /**
   * Erkenning binnen proweb
   */
  private LvErkenning addErkenning(int akteVolgcode, DossierAkte dossierAkte, DossierErkenning erkenning) {

    LvErkenning akte = new LvErkenning();

    setNaamskeuze(akteVolgcode, erkenning.getNaamskeuzeType(), dossierAkte, akte);

    akte.setKind(dossierAkte.getAktePersoon());
    akte.setLandErkenning(Landelijk.getNederland());
    akte.setGemeente(erkenning.getGemeente());
    akte.setBuitenlandsePlaats("");
    akte.setDatum(dossierAkte.getDatumIngang());
    akte.setGemeente(erkenning.getGemeente());
    akte.setMoeder(erkenning.getMoeder());
    akte.setErkenner(erkenning.getErkenner());
    akte.setRechtbank(erkenning.getRechtbank());
    akte.setErkenningsType(erkenning.getErkenningsType());
    akte.setTitel(erkenning.getKeuzeTitel());
    akte.setVoorvoegsel(erkenning.getKeuzeVoorvoegsel());
    akte.setGeslachtsnaam(erkenning.getKeuzeGeslachtsnaam());
    akte.setToestemminggeverType(erkenning.getToestemminggeverType());
    akte.setVerklaringGezag(erkenning.isVerklaringGezag());
    akte.setAfstammingsrecht(erkenning.getLandAfstammingsRecht());
    akte.setAkte(dossierAkte);

    return akte;
  }

  /**
   * Als 1e kind is dan naamskeuze overnemen. Bij 2e kind altijd geen naamskeuze.
   */
  private void setNaamskeuze(int akteVolgcode, NaamskeuzeVanToepassingType type, DossierAkte dossierAkte,
      LvErkenning akte) {

    akte.setNaamskeuzeType(NaamskeuzeVanToepassingType.NVT);
    if (akteVolgcode < 2) { // Alleen bij de eerste de waarde overnemen. 2e kind heeft geen naamskeuze
      akte.setNaamskeuzeType(type);
    }

    // Overrulen als er kinderen zijn
    for (DossierPersoon p : dossierAkte.getPersonen()) {
      if (p.getDossierPersoonType().is(KIND)) {
        akte.setNaamskeuzeType(p.getNaamskeuzeType());
      }
    }
  }
}
