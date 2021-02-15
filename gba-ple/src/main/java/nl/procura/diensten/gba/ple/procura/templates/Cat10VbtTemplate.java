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

package nl.procura.diensten.gba.ple.procura.templates;

import static nl.procura.burgerzaken.gba.core.enums.GBAElem.*;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.diensten.gba.ple.procura.utils.SortableObject;
import nl.procura.gba.jpa.probev.db.AbstractVbt;

public class Cat10VbtTemplate extends PLETemplateProcura<AbstractVbt> {

  @Override
  public void parse(SortableObject<AbstractVbt> so) {

    AbstractVbt vbt = so.getObject();
    addCat(GBACat.VBTITEL, so);

    addElem(AAND_VBT, vbt.getVbtit());
    addElem(VBT_OMS, vbt.getVbtit().getOms());
    addElem(DATUM_EINDE_VBT, vbt.getDVbtEnd());
    addElem(INGANGSDATUM_VBT, vbt.getDVbtIn());

    addElem(AAND_GEG_IN_ONDERZ, vbt.getIndBezw());
    addElem(DATUM_INGANG_ONDERZ, vbt.getDBezwIn());
    addElem(DATUM_EINDE_ONDERZ, vbt.getDBezwEnd());

    addElem(IND_ONJUIST, vbt.getIndO());
    addElem(INGANGSDAT_GELDIG, vbt.getId().getDGeld());
    addElem(VOLGCODE_GELDIG, vbt.getId().getVGeld());
    addElem(DATUM_VAN_OPNEMING, vbt.getDGba());
  }
}
