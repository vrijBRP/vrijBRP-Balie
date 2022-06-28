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
import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.diensten.gba.ple.procura.utils.SortableObject;
import nl.procura.gba.jpa.probev.db.AbstractNat;

public class Cat4NatioTemplate extends PLETemplateProcura<AbstractNat> {

  @Override
  public void parse(SortableObject<AbstractNat> so) {

    addCat(GBACat.NATIO, so);

    AbstractNat nat = so.getObject();
    addElem(NATIONALITEIT, nat.getNatio());

    addElem(REDEN_OPN_NATIO, nat.getRdnVk());
    addElem(REDEN_EINDE_NATIO, nat.getRdnVl());
    addElem(AAND_BIJZ_NL_SCHAP, nat.getAandByzNl());

    addElem(GEMEENTE_DOC, nat.getGOntl());
    addElem(DATUM_DOC, nat.getDOntl());
    addElem(BESCHRIJVING_DOC, nat.getDocOntl());

    addElem(AAND_GEG_IN_ONDERZ, nat.getIndBezw());
    addElem(DATUM_INGANG_ONDERZ, nat.getDBezwIn());
    addElem(DATUM_EINDE_ONDERZ, nat.getDBezwEnd());

    addElem(IND_ONJUIST, nat.getIndO());
    addElem(INGANGSDAT_GELDIG, nat.getId().getDGeld());
    addElem(VOLGCODE_GELDIG, nat.getId().getVGeld());
    addElem(DATUM_VAN_OPNEMING, nat.getDGba());
    addElem(GBAElem.EU_PERS_NR, nat.getEuDoc());
  }
}
