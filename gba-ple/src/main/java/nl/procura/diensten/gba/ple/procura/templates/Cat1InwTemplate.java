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

import static nl.procura.burgerzaken.gba.core.enums.GBAElem.AANDUIDING_NAAMGEBRUIK;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.AAND_GEG_IN_ONDERZ;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.AKTENR;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.ANR;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.BESCHRIJVING_DOC;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.BSN;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DATUM_DOC;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DATUM_EINDE_ONDERZ;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DATUM_INGANG_ONDERZ;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DATUM_VAN_OPNEMING;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.GEBOORTEDATUM;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.GEBOORTELAND;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.GEBOORTEPLAATS;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.GEMEENTE_DOC;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.GESLACHTSAAND;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.GESLACHTSNAAM;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.IND_ONJUIST;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.INGANGSDAT_GELDIG;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.REGIST_GEM_AKTE;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.TITEL_PREDIKAAT;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.VOLGCODE_GELDIG;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.VOLGEND_A_NUMMER;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.VOORNAMEN;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.VOORV_GESLACHTSNAAM;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.VORIG_A_NUMMER;

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
    long a1, a2, a3, bsn = 0;
    a1 = inw.getId().getA1();
    a2 = inw.getId().getA2();
    a3 = inw.getId().getA3();
    String anr = anr(a1, a2, a3);

    if (inw.getSnr() != null) {
      bsn = inw.getSnr().intValue();
    }

    AnrWijz anrWijz = getEntityManager().find(AnrWijz.class,
        new AnrWijzPK(anr, inw.getIndAnr().longValue()));

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
