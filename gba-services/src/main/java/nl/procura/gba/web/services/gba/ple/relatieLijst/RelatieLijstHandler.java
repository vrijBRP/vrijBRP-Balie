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

package nl.procura.gba.web.services.gba.ple.relatieLijst;

import static nl.procura.burgerzaken.gba.core.enums.GBACat.*;
import static nl.procura.gba.common.MiscUtils.isNrEquals;
import static nl.procura.standard.Globalfunctions.*;

import java.util.ArrayList;
import java.util.List;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.diensten.gba.ple.base.BasePLCat;
import nl.procura.diensten.gba.ple.base.BasePLSet;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.procura.arguments.PLEArgs;
import nl.procura.diensten.gba.ple.procura.arguments.PLEDatasource;
import nl.procura.gba.web.services.gba.ple.PersonenWsService;

public class RelatieLijstHandler {

  public static RelatieType getRelatieType(BasePLExt pl, BasePLExt bpl) {

    boolean isMeerderJarig = aval(bpl.getPersoon().getGeboorte().getLeeftijd()) >= 18;

    for (BasePLCat cat : pl.getCats(PERSOON, OUDER_1, OUDER_2, KINDEREN, HUW_GPS)) {

      for (BasePLSet set : cat.getSets()) {

        String anr = set.getLatestRec().getElem(GBAElem.ANR).getValue().getVal();
        String bsn = set.getLatestRec().getElem(
            GBAElem.BSN).getValue().getVal();

        boolean eqAnr = isNrEquals(anr, bpl.getPersoon().getAnr().getVal());
        boolean eqBsn = isNrEquals(bsn, bpl.getPersoon().getBsn().getVal());

        if (eqAnr || eqBsn) {

          if (cat.isCategoryType(GBACat.PERSOON)) {

            return RelatieType.AANGEVER;
          } else if (cat.isCategoryType(GBACat.OUDER_1, GBACat.OUDER_2)) {

            return RelatieType.OUDER;
          } else if (cat.isCategoryType(GBACat.HUW_GPS)) {

            return RelatieType.PARTNER;
          } else if (cat.isCategoryType(GBACat.KINDEREN)) {

            if (isMeerderJarig) {
              return RelatieType.MEERDERJARIG_KIND;
            }

            return RelatieType.KIND;
          }
        }
      }
    }

    if (isMeerderJarig) {
      return RelatieType.NIET_GERELATEERD;
    }

    return RelatieType.NIET_GERELATEERD_MINDERJARG;
  }

  public static RelatieLijst zoek(BasePLExt pl, boolean toonOverleden, PersonenWsService gbaWs) {

    RelatieLijst lijst = new RelatieLijst();
    PLEArgs args = new PLEArgs();
    List<Relatie> relaties = new ArrayList<>();

    for (BasePLCat cat : pl.getCats(PERSOON, OUDER_1, OUDER_2, HUW_GPS, KINDEREN)) {

      for (BasePLSet set : cat.getSets()) {

        String anr = set.getLatestRec().getElem(GBAElem.ANR).getValue().getVal();
        String bsn = set.getLatestRec().getElem(
            GBAElem.BSN).getValue().getVal();

        if (cat.isCategoryType(HUW_GPS) && !isActueelHuwelijk(set)) {
          continue;
        }

        if (fil(bsn)) {
          args.addNummer(bsn);
        } else if (fil(anr)) {
          args.addNummer(anr);
        }
      }
    }

    args.setDatasource(PLEDatasource.STANDAARD);

    for (BasePLExt bpl : gbaWs.getPersoonslijsten(args, false).getBasisPLWrappers()) {

      Relatie relatie = new Relatie();
      relatie.setRelatieType(getRelatieType(pl, bpl));
      relatie.setPl(bpl);
      relatie.setHuisgenoot(
          bpl.getVerblijfplaats().getAdres().getAdres().equals(pl.getVerblijfplaats().getAdres().getAdres()));

      // Als er sprake is van overleden gerelateerde dan niet tonen.
      if (!toonOverleden && relatie.getRelatieType() != RelatieType.AANGEVER
          && bpl.getPersoon().getStatus().isOverleden()) {
        continue;
      }

      relaties.add(relatie);
    }

    lijst.setRelaties(relaties);

    return lijst;
  }

  private static boolean isActueelHuwelijk(BasePLSet set) {

    return !pos(set.getLatestRec().getElem(
        GBAElem.DATUM_ONTBINDING).getValue().getVal());
  }
}
