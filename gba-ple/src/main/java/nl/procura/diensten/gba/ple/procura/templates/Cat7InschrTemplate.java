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
import nl.procura.gba.jpa.probev.db.AbstractInschr;

public class Cat7InschrTemplate extends PLETemplateProcura<AbstractInschr> {

  @Override
  public void parse(SortableObject<AbstractInschr> so) {

    addCat(GBACat.INSCHR, so);

    AbstractInschr inschr = so.getObject();
    addElem(GEM_BLOKK_PL, inschr.getGBlok());
    addElem(DATUM_INGANG_BLOK_PL, inschr.getDBlok());

    addElem(DATUM_OPSCH_BIJHOUD, inschr.getDOpschort());
    addElem(OMSCHR_REDEN_OPSCH_BIJHOUD, inschr.getRdnOpschort());
    addElem(DATUM_EERSTE_INSCHR_GBA, inschr.getDGba());
    addElem(GEMEENTE_VAN_PK, inschr.getGPk());
    addElem(IND_GEHEIM, inschr.getIndGeheim());
    addElem(VERSIENR, inschr.getVersie());
    addElem(DATUMTIJDSTEMPEL, inschr.getDT());

    addElem(INGANGSDAT_GELDIG, inschr.getId().getDGeld());
    addElem(VOLGCODE_GELDIG, inschr.getId().getVGeld());
    addElem(DATUM_VAN_OPNEMING, inschr.getDGba());

    addElem(PK_GEGEVENS_VOL_CONVERT, inschr.getPkConv());
  }
}
