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
import nl.procura.gba.jpa.probev.db.AnrWijz;
import nl.procura.gba.jpa.probev.db.AnrWijzPK;
import nl.procura.gba.jpa.probev.db.Verw;

public class Cat21VerwTemplate extends PLETemplateProcura<Verw> {

  @Override
  public void parse(SortableObject<Verw> so) {

    Verw verw = so.getObject();
    addCat(GBACat.VERW, so);

    int a1, a2, a3, bsn = 0;

    a1 = (int) verw.getId().getA1();
    a2 = (int) verw.getId().getA2();
    a3 = (int) verw.getId().getA3();

    if (verw.getSnr() != null) {
      bsn = verw.getSnr().intValue();
    }

    AnrWijz anrWijz = getEntityManager().find(AnrWijz.class,
        new AnrWijzPK(anr(a1, a2, a3), verw.getIndAnr().intValue()));

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
