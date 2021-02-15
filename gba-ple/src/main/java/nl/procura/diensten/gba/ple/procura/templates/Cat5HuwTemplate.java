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
import nl.procura.gba.jpa.probev.db.AbstractHuw;

public class Cat5HuwTemplate extends PLETemplateProcura<AbstractHuw> {

  @Override
  public void parse(SortableObject<AbstractHuw> so) {

    addCat(GBACat.HUW_GPS, so);

    AbstractHuw huw = so.getObject();
    addElem(ANR, anr(huw.getA1Ref(), huw.getA2Ref(), huw.getA3Ref()));
    addElem(BSN, huw.getSnr());

    addElem(VOORNAMEN, huw.getVoorn());
    addElem(TITEL_PREDIKAAT, huw.getTp());
    addElem(VOORV_GESLACHTSNAAM, huw.getVoorv());
    addElem(GESLACHTSNAAM, huw.getNaam());

    addElem(GEBOORTEDATUM, huw.getDGeb());
    addElem(GEBOORTEPLAATS, huw.getPGeb());
    addElem(GEBOORTELAND, huw.getLGeb());

    addElem(GESLACHTSAAND, huw.getGeslacht());

    if (huw.getDOntb().intValue() != -1) {
      addElem(DATUM_ONTBINDING, huw.getDOntb());
      addElem(PLAATS_ONTBINDING, huw.getPOntb());
      addElem(LAND_ONTBINDING, huw.getLOntb());
      addElem(REDEN_ONTBINDING, huw.getHuwOntb());
    }

    if (huw.getDHuw().intValue() != -1) {
      addElem(DATUM_VERBINTENIS, huw.getDHuw());
      addElem(PLAATS_VERBINTENIS, huw.getPHuw());
      addElem(LAND_VERBINTENIS, huw.getLHuw());
    }

    addElem(SOORT_VERBINTENIS, huw.getSoort());

    addElem(REGIST_GEM_AKTE, huw.getGReg());
    addElem(AKTENR, huw.getAktenr());
    addElem(GEMEENTE_DOC, huw.getGOntl());
    addElem(DATUM_DOC, huw.getDOntl());
    addElem(BESCHRIJVING_DOC, huw.getDocOntl().getDoc());

    addElem(AAND_GEG_IN_ONDERZ, huw.getIndBezw());
    addElem(DATUM_INGANG_ONDERZ, huw.getDBezwIn());
    addElem(DATUM_EINDE_ONDERZ, huw.getDBezwEnd());
    addElem(IND_ONJUIST, huw.getIndO());

    addElem(INGANGSDAT_GELDIG, huw.getId().getDGeld());
    addElem(VOLGCODE_GELDIG, huw.getId().getVGeld());
    addElem(DATUM_VAN_OPNEMING, huw.getDGba());
  }
}
