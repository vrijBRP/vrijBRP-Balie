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

package nl.procura.gba.web.services.zaken.algemeen.samenvatting;

import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.VADER_DUO_MOEDER;

import nl.procura.gba.web.services.bs.algemeen.ZaakDossier;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.geboorte.DossierGeboorte;

public class ToonAktePersoonUtils {

  /**
   * Toon geen personen die toch niet ingevuld hoeven te worden.
   */
  public static boolean toon(ZaakDossier zaakDossier, DossierPersoon persoon) {

    // Bij geboorte geen vader / duo-moeder tonen als deze niet van toepassing is.
    if (zaakDossier instanceof DossierGeboorte) {
      DossierGeboorte geboorte = (DossierGeboorte) zaakDossier;
      return persoon.isVolledig() || geboorte.isVaderMogelijk() ||
          !persoon.getDossierPersoonType().is(VADER_DUO_MOEDER);
    }
    return true;
  }
}
