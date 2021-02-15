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

import static nl.procura.standard.Globalfunctions.pos;

import nl.procura.gba.web.common.misc.Landelijk;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.bs.algemeen.functies.BsUtils;
import nl.procura.gba.web.services.bs.algemeen.interfaces.DossierNamenrecht;
import nl.procura.gba.web.services.bs.algemeen.nationaliteit.DossierNationaliteit;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class BsNamenRechtUtils extends BsUtils {

  public static FieldValue getNamenRecht(Services services, FieldValue huidigeWaarde, DossierNamenrecht dossier) {

    if (pos(huidigeWaarde.getValue())) {
      return huidigeWaarde;
    }

    for (DossierNationaliteit nationaliteit : dossier.getDossier().getNationaliteiten()) {
      if (Landelijk.isNederland(nationaliteit)) {
        return services.getKennisbankService().getLand(nationaliteit.getNationaliteit());
      }
    }

    return new FieldValue();
  }
}
