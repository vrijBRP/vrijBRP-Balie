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

package nl.procura.diensten.gba.ple.procura.templates.custom;

import static nl.procura.burgerzaken.gba.core.enums.GBAElem.AANDUIDING_NAAMGEBRUIK;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.AAND_GEG_IN_ONDERZ;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.ADRES_BUITENL_1;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.ADRES_BUITENL_2;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.ADRES_BUITENL_3;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.ANR;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.BSN;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DATUM_AANVANG_ADRESH;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DATUM_EINDE_ONDERZ;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DATUM_INGANG_ONDERZ;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DATUM_INSCHR;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DATUM_VAN_OPNEMING;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DATUM_VERTREK_UIT_NL;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DATUM_VESTIGING_IN_NL;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.FUNCTIE_ADRES;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.GEBOORTEDATUM;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.GEBOORTELAND;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.GEBOORTEPLAATS;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.GEM_DEEL;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.GEM_INSCHR;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.GEM_INSCHR_CODE;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.GESLACHTSAAND;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.GESLACHTSNAAM;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.HNR;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.HNR_A;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.HNR_L;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.HNR_T;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.IDCODE_NUMMERAAND;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.ID_VERBLIJFPLAATS;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.IND_DOC;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.IND_GEHEIM;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.IND_ONJUIST;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.INGANGSDAT_GELDIG;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.LAND_VERTREK;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.LAND_VESTIGING;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.LOCATIEBESCHR;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.OMSCHR_REDEN_OPSCH_BIJHOUD;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.OMSCHR_VAN_DE_AANGIFTE_ADRESH;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.OPENB_RUIMTE;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.POSTCODE;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.STRAATNAAM;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.TITEL_PREDIKAAT;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.VOORNAMEN;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.VOORV_GESLACHTSNAAM;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.WPL_NAAM;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.diensten.gba.ple.procura.utils.PLEWoningObject;
import nl.procura.diensten.gba.ple.procura.utils.SortableObject;
import nl.procura.gba.jpa.probev.db.AnrWijz;
import nl.procura.gba.jpa.probev.db.AnrWijzPK;
import nl.procura.gba.jpa.probev.db.Inschr;
import nl.procura.gba.jpa.probev.db.Inw;
import nl.procura.gba.jpa.probev.db.Vb;
import nl.procura.gba.jpa.probev.db.Verw;

public class Wk extends CustomTemplate {

  @Override
  public void parse(int nr, SortableObject so) {

    if (so.getObject() instanceof Inw) {

      addCat(GBACat.PERSOON, so);

      Inw inw = (Inw) so.getObject();
      long a1 = inw.getId().getA1();
      long a2 = inw.getId().getA2();
      long a3 = inw.getId().getA3();

      AnrWijz anrWijz = getEntityManager().find(AnrWijz.class,
          new AnrWijzPK(anr(a1, a2, a3), inw.getIndAnr().longValue()));

      if (anrWijz != null) {
        addElem(ANR, anrWijz.getAnrOud());
      } else {
        addElem(ANR, so.getAnr());
      }

      addElem(BSN, (inw.getSnr() != null) ? inw.getSnr() : 0);
      addElem(VOORNAMEN, inw.getVoorn());
      addElem(TITEL_PREDIKAAT, inw.getTp());
      addElem(VOORV_GESLACHTSNAAM, inw.getVoorv());
      addElem(GESLACHTSNAAM, inw.getNaam());
      addElem(GEBOORTEDATUM, inw.getDGeb());
      addElem(GESLACHTSAAND, inw.getGeslacht());
      addElem(AANDUIDING_NAAMGEBRUIK, inw.getNaamgebruik());

    } else if (so.getObject() instanceof Inschr) {

      addCat(GBACat.INSCHR, so);

      Inschr inschr = (Inschr) so.getObject();
      addElem(OMSCHR_REDEN_OPSCH_BIJHOUD, inschr.getRdnOpschort());
      addElem(IND_GEHEIM, inschr.getIndGeheim());

    } else if (so.getObject() instanceof Vb) {

      addCat(GBACat.VB, so);

      Vb vb = (Vb) so.getObject();
      addElem(GEM_INSCHR, vb.getGInschr());
      addElem(GEM_INSCHR_CODE, vb.getGInschr().getCPlaats());
      addElem(DATUM_INSCHR, vb.getDInschr());

      if (vb.getDVertrek().intValue() != -1) { // groep 13 komt voor
        addElem(LAND_VERTREK, vb.getLVertrek());
        addElem(DATUM_VERTREK_UIT_NL, vb.getDVertrek());
        addElem(ADRES_BUITENL_1, vb.getBAdres1());
        addElem(ADRES_BUITENL_2, vb.getBAdres2());
        addElem(ADRES_BUITENL_3, vb.getBAdres3());

      } else { // groep 13 komt niet voor

        if (vb.getDAanv().intValue() != -1) { // groep 10 komt voor
          addElem(FUNCTIE_ADRES, vb.getFuncAdr());
          addElem(GEM_DEEL, vb.getGemDeel());
          addElem(DATUM_AANVANG_ADRESH, vb.getDAanv());

          PLEWoningObject woningobject = new PLEWoningObject();
          woningobject.setRef_pl(getBuilder().getResult().getBasePLs().size() - 1);
          woningobject.setC_gem_deel(vb.getGemDeel().getCGemDeel().intValue());
          woningobject.setC_locatie(vb.getLocatie().getCLocatie().intValue());
          woningobject.setC_straat(vb.getStraat().getCStraat().intValue());
          woningobject.setHnr(vb.getHnr().intValue());
          woningobject.setHnr_a(vb.getHnrA());
          woningobject.setHnr_l(vb.getHnrL());
          woningobject.setHnr_t(vb.getHnrT());

          if (vb.getLocatie().getCLocatie() != -1L) {
            addElem(LOCATIEBESCHR, vb.getLocatie());

          } else {
            addElem(STRAATNAAM, vb.getStraat());
            addElem(HNR, vb.getHnr());
            addElem(HNR_L, vb.getHnrL());
            addElem(HNR_T, vb.getHnrT());
            addElem(HNR_A, vb.getHnrA());
            addElem(POSTCODE, vb.getPc());
          }

          addElem(OPENB_RUIMTE, vb.getObr());
          addElem(WPL_NAAM, vb.getWpl());
          addElem(ID_VERBLIJFPLAATS, vb.getAon());
          addElem(IDCODE_NUMMERAAND, vb.getIna());
          addElem(LAND_VESTIGING, vb.getLVestiging());
          addElem(DATUM_VESTIGING_IN_NL, vb.getDVestiging());
        }
      }

      addElem(OMSCHR_VAN_DE_AANGIFTE_ADRESH, vb.getAangifte());
      addElem(IND_DOC, vb.getIndDoc());
      addElem(AAND_GEG_IN_ONDERZ, vb.getIndBezw());
      addElem(DATUM_INGANG_ONDERZ, vb.getDBezwIn());
      addElem(DATUM_EINDE_ONDERZ, vb.getDBezwEnd());
      addElem(IND_ONJUIST, vb.getIndO());
      addElem(INGANGSDAT_GELDIG, vb.getId().getDGeld());
      addElem(DATUM_VAN_OPNEMING, vb.getDGba());

    } else if (so.getObject() instanceof Verw) {

      addCat(GBACat.VERW, so);

      Verw verw = (Verw) so.getObject();
      long a1 = verw.getId().getA1();
      long a2 = verw.getId().getA2();
      long a3 = verw.getId().getA3();

      AnrWijz anrWijz = getEntityManager().find(AnrWijz.class,
          new AnrWijzPK(anr(a1, a2, a3), verw.getIndAnr().longValue()));

      if (anrWijz != null) {
        addElem(ANR, anrWijz.getAnrOud());
      } else {
        addElem(ANR, anr(a1, a2, a3));
      }

      addElem(BSN, verw.getSnr());
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
        addElem(HNR, verw.getHnr());
        addElem(HNR_L, verw.getHnrL());
        addElem(HNR_T, verw.getHnrT());
        addElem(HNR_A, verw.getHnrA());
        addElem(POSTCODE, verw.getPc());
      }

      addElem(IND_GEHEIM, verw.getIndGeheim());
      addElem(AAND_GEG_IN_ONDERZ, verw.getIndBezw());
      addElem(DATUM_INGANG_ONDERZ, verw.getDBezwIn());
      addElem(DATUM_EINDE_ONDERZ, verw.getDBezwEnd());
      addElem(IND_ONJUIST, verw.getIndO());
      addElem(INGANGSDAT_GELDIG, verw.getId().getDGeld());
      addElem(DATUM_VAN_OPNEMING, verw.getDGba());
    }
  }
}
