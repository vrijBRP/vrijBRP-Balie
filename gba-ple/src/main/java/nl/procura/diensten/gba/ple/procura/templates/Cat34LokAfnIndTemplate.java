/*
 * Copyright 2024 - 2025 Procura B.V.
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

import static nl.procura.burgerzaken.gba.core.enums.GBAElem.LOK_AFN_AANT_OMSCHR;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.LOK_AFN_AANT_OPMERK;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.LOK_AFN_AANT_SOORT;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.diensten.gba.ple.procura.utils.SortableObject;
import nl.procura.gba.jpa.probev.db.AbstractAant3;

public class Cat34LokAfnIndTemplate extends PLETemplateProcura<AbstractAant3> {

  @Override
  public void parse(SortableObject<AbstractAant3> so) {

    addCat(GBACat.LOK_AF_IND, so);

    AbstractAant3 aant = so.getObject();
    addElem(LOK_AFN_AANT_SOORT, aant.getAantek3().getAant());
    addElem(LOK_AFN_AANT_OMSCHR, aant.getAantek3().getOms());
    addElem(LOK_AFN_AANT_OPMERK, aant.getAant());
  }
}
