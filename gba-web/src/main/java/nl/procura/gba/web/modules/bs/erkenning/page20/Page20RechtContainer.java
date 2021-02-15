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

package nl.procura.gba.web.modules.bs.erkenning.page20;

import nl.procura.gba.web.common.misc.Landelijk;
import nl.procura.gba.web.modules.bs.common.utils.BsRechtContainer;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.erkenning.DossierErkenning;

public class Page20RechtContainer extends BsRechtContainer {

  public Page20RechtContainer(Services services, DossierErkenning erkenning) {

    super(services);

    add(Landelijk.getNederland());

    addPersoonNationaliteiten(erkenning.getErkenner());
    addPersoonLand(erkenning.getErkenner());

    if (erkenning.isBestaandKind()) {
      for (DossierPersoon kind : erkenning.getKinderen()) {
        addPersoonNationaliteiten(kind);
        addPersoonLand(kind);
      }
    } else {
      addDossierNationaliteiten(erkenning);
    }
  }
}
