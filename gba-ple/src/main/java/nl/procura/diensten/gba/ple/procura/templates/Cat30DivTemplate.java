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
import nl.procura.gba.jpa.probev.db.AbstractDiv;

public class Cat30DivTemplate extends PLETemplateProcura<AbstractDiv> {

  @Override
  public void parse(SortableObject<AbstractDiv> so) {

    addCat(GBACat.DIV, so);

    AbstractDiv div = so.getObject();
    addElem(DIV_GEZINSVERH, div.getGvhBean().getGvh());
    addElem(DIV_GEZINSVERH_OMSCHR, div.getGvhBean().getOms());

    addElem(DIV_IND_VUURW, div.getVuurw());
    addElem(DIV_KENMERK, div.getKenmerk().getCKenmerk());
    addElem(DIV_KENMERK_OMSCHR, div.getKenmerk().getKenmerk());

    addElem(DIV_DATUM_PL_VERSTREKT, div.getDPlVerstr());
    addElem(DIV_OTS, div.getOts());
    addElem(DIV_SIGNAAL, div.getSignaal());
    addElem(DIV_CORR_ADRES_1, div.getCorrAdres1().getAdr());
    addElem(DIV_CORR_ADRES_2, div.getCorrAdres2().getAdr());

    addElem(ANR, anr(div.getA1Ref(), div.getA2Ref(), div.getA3Ref()));

    addElem(INGANGSDAT_GELDIG, div.getId().getDGeld());
    addElem(VOLGCODE_GELDIG, div.getId().getVGeld());
  }
}
