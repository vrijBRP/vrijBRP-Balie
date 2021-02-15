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
import nl.procura.gba.jpa.probev.db.AbstractParent;
import nl.procura.gba.jpa.probev.db.Mdr;
import nl.procura.gba.jpa.probev.db.Xmdr;

public class Cat2and3ParentTemplate extends PLETemplateProcura<AbstractParent> {

  @Override
  public void parse(SortableObject<AbstractParent> so) {

    AbstractParent ouder = so.getObject();

    if (ouder instanceof Mdr || ouder instanceof Xmdr) {
      addCat(GBACat.OUDER_1, so);

    } else {
      addCat(GBACat.OUDER_2, so);
    }

    addElem(ANR, anr(ouder.getA1Ref(), ouder.getA2Ref(), ouder.getA3Ref()));
    addElem(BSN, ouder.getSnr());

    addElem(VOORNAMEN, ouder.getVoorn());
    addElem(TITEL_PREDIKAAT, ouder.getTp());
    addElem(VOORV_GESLACHTSNAAM, ouder.getVoorv());
    addElem(GESLACHTSNAAM, ouder.getNaam());

    addElem(GEBOORTEDATUM, ouder.getDGeb());
    addElem(GEBOORTEPLAATS, ouder.getPGeb());
    addElem(GEBOORTELAND, ouder.getLGeb());

    addElem(GESLACHTSAAND, ouder.getGeslacht());

    addElem(DATUM_INGANG_FAM_RECHT_BETREK, ouder.getDAfst());

    addElem(REGIST_GEM_AKTE, ouder.getGReg());
    addElem(AKTENR, ouder.getAktenr());

    addElem(GEMEENTE_DOC, ouder.getGOntl());
    addElem(DATUM_DOC, ouder.getDOntl());
    addElem(BESCHRIJVING_DOC, ouder.getDocOntl().getDoc());

    addElem(AAND_GEG_IN_ONDERZ, ouder.getIndBezw());
    addElem(DATUM_INGANG_ONDERZ, ouder.getDBezwIn());
    addElem(DATUM_EINDE_ONDERZ, ouder.getDBezwEnd());

    addElem(IND_ONJUIST, ouder.getIndO());
    addElem(INGANGSDAT_GELDIG, ouder.getId().getDGeld());
    addElem(VOLGCODE_GELDIG, ouder.getId().getVGeld());
    addElem(DATUM_VAN_OPNEMING, ouder.getDGba());
  }
}
