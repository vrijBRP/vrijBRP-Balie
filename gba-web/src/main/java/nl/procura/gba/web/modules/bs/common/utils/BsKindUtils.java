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

package nl.procura.gba.web.modules.bs.common.utils;

import java.util.ArrayList;
import java.util.List;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.extensions.Cat9KindExt;
import nl.procura.diensten.gba.ple.procura.arguments.PLEArgs;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.bs.algemeen.functies.BsPersoonUtils;
import nl.procura.gba.web.services.bs.algemeen.functies.BsUtils;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.validation.Anummer;
import nl.procura.validation.Bsn;

public class BsKindUtils extends BsUtils {

  /**
   * Haal de kinderen van de moeder op die wel id-nummers hebben
   */
  public static List<BasePLExt> getKinderenMetPersoonslijst(Services services, DossierPersoon moeder) {

    List<BasePLExt> personen = new ArrayList<>();

    String bsnMoeder = moeder.getBurgerServiceNummer().getStringValue();
    String anrMoeder = moeder.getAnummer().getStringValue();

    if (moeder.getAnummer().isCorrect() || moeder.getBurgerServiceNummer().isCorrect()) {

      BasePLExt moederPl = services.getPersonenWsService().getPersoonslijst(anrMoeder, bsnMoeder);
      PLEArgs args = new PLEArgs();

      for (Cat9KindExt k : moederPl.getKinderen().getKinderen()) {

        String bsnKind = k.getBsn().getVal();
        String anrKind = k.getAnr().getVal();

        if (Bsn.isCorrect(bsnKind)) {
          args.addNummer(bsnKind);
        } else if (Anummer.isCorrect(anrKind)) {
          args.addNummer(anrKind);
        }
      }

      if (args.getNumbers().size() > 0) {
        personen.addAll(services.getPersonenWsService().getPersoonslijsten(args, false).getBasisPLWrappers());
      }
    }

    return personen;
  }

  /**
   * Haal de kinderen van de moeder op die geen id-nummers hebben
   */
  public static List<DossierPersoon> getKinderenZonderPersoonslijst(Services services, DossierPersoon moeder) {

    List<DossierPersoon> personen = new ArrayList<>();

    String bsnMoeder = moeder.getBurgerServiceNummer().getStringValue();
    String anrMoeder = moeder.getAnummer().getStringValue();

    if (moeder.getAnummer().isCorrect() || moeder.getBurgerServiceNummer().isCorrect()) {

      BasePLExt moederPl = services.getPersonenWsService().getPersoonslijst(anrMoeder, bsnMoeder);

      for (Cat9KindExt k : moederPl.getKinderen().getKinderen()) {

        String bsnKind = k.getBsn().getVal();
        String anrKind = k.getAnr().getVal();

        if (!Bsn.isCorrect(bsnKind) && !Anummer.isCorrect(anrKind)) {
          personen.add(BsPersoonUtils.kopieDossierPersoon(k.getRecord(), new DossierPersoon()));
        }
      }
    }

    return personen;
  }
}
