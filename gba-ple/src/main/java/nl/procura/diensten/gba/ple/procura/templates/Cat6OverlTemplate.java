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
import nl.procura.gba.jpa.probev.db.Overl;

public class Cat6OverlTemplate extends PLETemplateProcura {

  @Override
  public void parse(SortableObject so) {

    addCat(GBACat.OVERL, so);

    Overl overl = (Overl) so.getObject();
    addElem(DATUM_OVERL, overl.getDOverl());
    addElem(PLAATS_OVERL, overl.getPOverl());
    addElem(LAND_OVERL, overl.getLOverl());

    addElem(REGIST_GEM_AKTE, overl.getGReg());
    addElem(AKTENR, overl.getAktenr());

    addElem(GEMEENTE_DOC, overl.getGOntl());
    addElem(DATUM_DOC, overl.getDOntl());
    addElem(BESCHRIJVING_DOC, overl.getDocOntl().getDoc());

    addElem(AAND_GEG_IN_ONDERZ, overl.getIndBezw());
    addElem(DATUM_INGANG_ONDERZ, overl.getDBezwIn());
    addElem(DATUM_EINDE_ONDERZ, overl.getDBezwEnd());

    addElem(IND_ONJUIST, overl.getIndO());
    addElem(INGANGSDAT_GELDIG, overl.getId().getDGeld());
    addElem(VOLGCODE_GELDIG, overl.getId().getVGeld());
    addElem(DATUM_VAN_OPNEMING, overl.getDGba());
  }
}
