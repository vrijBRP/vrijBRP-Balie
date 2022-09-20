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

import static nl.procura.burgerzaken.gba.core.enums.GBAElem.AAND_GEG_IN_ONDERZ;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.ANR;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.BSN;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DATUM_EINDE_ONDERZ;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DATUM_INGANG_ONDERZ;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DATUM_INSCHR;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DATUM_VAN_OPNEMING;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.GEBOORTEDATUM;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.GEBOORTELAND;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.GEBOORTEPLAATS;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.GEM_INSCHR;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.GESLACHTSNAAM;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.HNR;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.HNR_A;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.HNR_L;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.HNR_T;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.IDCODE_NUMMERAAND;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.ID_VERBLIJFPLAATS;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.IND_GEHEIM;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.IND_ONJUIST;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.INGANGSDAT_GELDIG;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.LOCATIEBESCHR;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.OPENB_RUIMTE;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.POSTCODE;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.STRAATNAAM;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.TITEL_PREDIKAAT;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.VOLGCODE_GELDIG;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.VOORNAMEN;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.VOORV_GESLACHTSNAAM;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.WPL_NAAM;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.diensten.gba.ple.procura.utils.SortableObject;
import nl.procura.gba.jpa.probev.db.AnrWijz;
import nl.procura.gba.jpa.probev.db.AnrWijzPK;
import nl.procura.gba.jpa.probev.db.Verw;

public class Cat21VerwTemplate extends PLETemplateProcura<Verw> {

  @Override
  public void parse(SortableObject<Verw> so) {

    Verw verw = so.getObject();
    addCat(GBACat.VERW, so);

    long a1, a2, a3, bsn = 0;
    a1 = verw.getId().getA1();
    a2 = verw.getId().getA2();
    a3 = verw.getId().getA3();

    if (verw.getSnr() != null) {
      bsn = verw.getSnr().intValue();
    }

    AnrWijz anrWijz = getEntityManager().find(AnrWijz.class,
        new AnrWijzPK(anr(a1, a2, a3), verw.getIndAnr().longValue()));

    if (anrWijz != null) {
      addElem(ANR, anrWijz.getAnrOud());
    } else {
      addElem(ANR, anr(a1, a2, a3));
    }

    addElem(BSN, bsn);

    addElem(VOORNAMEN, verw.getVoorn());
    addElem(TITEL_PREDIKAAT, verw.getTp());
    addElem(VOORV_GESLACHTSNAAM, verw.getVoorv());
    addElem(GESLACHTSNAAM, verw.getNaam());

    addElem(GEBOORTEDATUM, verw.getDGeb());
    addElem(GEBOORTEPLAATS, verw.getPGeb());
    addElem(GEBOORTELAND, verw.getLGeb());

    addElem(GEM_INSCHR, verw.getGUitschr());
    addElem(DATUM_INSCHR, verw.getDUitschr());

    if (verw.getLocatie().getCLocatie() != -1L) {
      addElem(LOCATIEBESCHR, verw.getLocatie().getLocatie());

    } else {
      addElem(STRAATNAAM, verw.getStraat());
      addElem(OPENB_RUIMTE, verw.getObr());
      addElem(HNR, verw.getHnr());
      addElem(HNR_L, verw.getHnrL());
      addElem(HNR_T, verw.getHnrT());
      addElem(HNR_A, verw.getHnrA());
      addElem(POSTCODE, verw.getPc());
      addElem(WPL_NAAM, verw.getWpl());
      addElem(ID_VERBLIJFPLAATS, verw.getAon());
      addElem(IDCODE_NUMMERAAND, verw.getIna());
    }

    addElem(IND_GEHEIM, verw.getIndGeheim());

    addElem(AAND_GEG_IN_ONDERZ, verw.getIndBezw());
    addElem(DATUM_INGANG_ONDERZ, verw.getDBezwIn());
    addElem(DATUM_EINDE_ONDERZ, verw.getDBezwEnd());

    addElem(IND_ONJUIST, verw.getIndO());
    addElem(INGANGSDAT_GELDIG, verw.getId().getDGeld());
    addElem(VOLGCODE_GELDIG, verw.getId().getVGeld());
    addElem(DATUM_VAN_OPNEMING, verw.getDGba());
  }
}
