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
import nl.procura.gba.jpa.probev.db.AbstractInw;
import nl.procura.gba.jpa.probev.db.AnrWijz;
import nl.procura.gba.jpa.probev.db.AnrWijzPK;

public class Cat1InwTemplate extends PLETemplateProcura<AbstractInw> {

  @Override
  public void parse(SortableObject<AbstractInw> so) {

    addCat(GBACat.PERSOON, so);

    AbstractInw inw = so.getObject();
    int a1, a2, a3, bsn = 0;
    a1 = (int) inw.getId().getA1();
    a2 = (int) inw.getId().getA2();
    a3 = (int) inw.getId().getA3();
    String anr = anr(a1, a2, a3);

    if (inw.getSnr() != null) {
      bsn = inw.getSnr().intValue();
    }

    AnrWijz anrWijz = getEntityManager().find(AnrWijz.class,
        new AnrWijzPK(anr, inw.getIndAnr().intValue()));

    if (anrWijz != null) {
      addElem(ANR, anrWijz.getAnrOud());
    } else {
      addElem(ANR, anr);
    }

    addElem(BSN, bsn);

    addElem(VOORNAMEN, inw.getVoorn());
    addElem(TITEL_PREDIKAAT, inw.getTp());
    addElem(VOORV_GESLACHTSNAAM, inw.getVoorv());
    addElem(GESLACHTSNAAM, inw.getNaam());

    addElem(GEBOORTEDATUM, inw.getDGeb());
    addElem(GEBOORTEPLAATS, inw.getPGeb());
    addElem(GEBOORTELAND, inw.getLGeb());

    addElem(GESLACHTSAAND, inw.getGeslacht());

    addElem(VORIG_A_NUMMER, inw.getAnrPrev());
    addElem(VOLGEND_A_NUMMER, inw.getAnrNext());

    addElem(AANDUIDING_NAAMGEBRUIK, inw.getNaamgebruik());

    addElem(REGIST_GEM_AKTE, inw.getGReg());
    addElem(AKTENR, inw.getAktenr());

    addElem(GEMEENTE_DOC, inw.getGOntl());
    addElem(DATUM_DOC, inw.getDOntl());
    addElem(BESCHRIJVING_DOC, inw.getDocOntl());

    addElem(AAND_GEG_IN_ONDERZ, inw.getIndBezw());
    addElem(DATUM_INGANG_ONDERZ, inw.getDBezwIn());
    addElem(DATUM_EINDE_ONDERZ, inw.getDBezwEnd());

    addElem(IND_ONJUIST, inw.getIndO());
    addElem(INGANGSDAT_GELDIG, inw.getId().getDGeld());
    addElem(VOLGCODE_GELDIG, inw.getId().getVGeld());
    addElem(DATUM_VAN_OPNEMING, inw.getDGba());
  }
}
