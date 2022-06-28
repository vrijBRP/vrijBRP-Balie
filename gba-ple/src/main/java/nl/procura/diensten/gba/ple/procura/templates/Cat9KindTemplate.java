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
import nl.procura.gba.jpa.probev.db.AbstractAfst;

public class Cat9KindTemplate extends PLETemplateProcura<AbstractAfst> {

  @Override
  public void parse(SortableObject<AbstractAfst> so) {

    AbstractAfst kind = so.getObject();

    if (isFilterStillborns(kind.getRegBetrek())) {
      return;
    }

    addCat(GBACat.KINDEREN, so);

    addElem(ANR, anr(kind.getA1Ref(), kind.getA2Ref(), kind.getA3Ref()));
    addElem(BSN, kind.getSnr());

    addElem(VOORNAMEN, kind.getVoorn());
    addElem(TITEL_PREDIKAAT, kind.getTp());
    addElem(VOORV_GESLACHTSNAAM, kind.getVoorv());
    addElem(GESLACHTSNAAM, kind.getNaam());

    addElem(GEBOORTEDATUM, kind.getDGeb());
    addElem(GEBOORTEPLAATS, kind.getPGeb());
    addElem(GEBOORTELAND, kind.getLGeb());

    addElem(REGIST_GEM_AKTE, kind.getGReg());
    addElem(AKTENR, kind.getAktenr());

    addElem(GEMEENTE_DOC, kind.getGOntl());
    addElem(DATUM_DOC, kind.getDOntl());
    addElem(BESCHRIJVING_DOC, kind.getDocOntl());

    addElem(AAND_GEG_IN_ONDERZ, kind.getIndBezw());
    addElem(DATUM_INGANG_ONDERZ, kind.getDBezwIn());
    addElem(DATUM_EINDE_ONDERZ, kind.getDBezwEnd());

    addElem(IND_ONJUIST, kind.getIndO());
    addElem(INGANGSDAT_GELDIG, kind.getId().getDGeld());
    addElem(VOLGCODE_GELDIG, kind.getId().getVGeld());
    addElem(DATUM_VAN_OPNEMING, kind.getDGba());
    addElem(REG_BETREKK, kind.getRegBetrek());
  }

  /**
   * Should stillborns be removed from the results
   * based on the authorization in personen-ws?
   */
  private boolean isFilterStillborns(String regBetrekking) {
    return "L".equals(regBetrekking) && !getBuilder().getFilter()
        .isAllowed(GBACat.KINDEREN.getCode(), REG_BETREKK.getCode());
  }
}
