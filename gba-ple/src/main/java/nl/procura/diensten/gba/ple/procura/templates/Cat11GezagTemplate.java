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
import nl.procura.gba.jpa.probev.db.AbstractGezag;

public class Cat11GezagTemplate extends PLETemplateProcura<AbstractGezag> {

  @Override
  public void parse(SortableObject<AbstractGezag> so) {

    addCat(GBACat.GEZAG, so);

    AbstractGezag gezag = so.getObject();
    addElem(IND_GEZAG_MINDERJ, gezag.getVoogdij());
    addElem(IND_CURATELE_REG, gezag.getIndCuratele());

    addElem(GEMEENTE_DOC, gezag.getGOntl());
    addElem(DATUM_DOC, gezag.getDOntl());
    addElem(BESCHRIJVING_DOC, gezag.getDocOntl());

    addElem(AAND_GEG_IN_ONDERZ, gezag.getIndBezw());
    addElem(DATUM_INGANG_ONDERZ, gezag.getDBezwIn());
    addElem(DATUM_EINDE_ONDERZ, gezag.getDBezwEnd());

    addElem(IND_ONJUIST, gezag.getIndO());
    addElem(INGANGSDAT_GELDIG, gezag.getId().getDGeld());
    addElem(VOLGCODE_GELDIG, gezag.getId().getVGeld());
    addElem(DATUM_VAN_OPNEMING, gezag.getDGba());
  }
}
