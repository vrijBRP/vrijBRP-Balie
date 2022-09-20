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
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DATUM_ONTBINDING;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DATUM_VAN_OPNEMING;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DATUM_VERBINTENIS;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DATUM_VERTREK_UIT_NL;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DATUM_VESTIGING_IN_NL;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.FUNCTIE_ADRES;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.GEBOORTEDATUM;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.GEBOORTELAND;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.GEBOORTEPLAATS;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.GEM_DEEL;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.GEM_INSCHR;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.GESLACHTSAAND;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.GESLACHTSNAAM;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.HNR;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.HNR_A;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.HNR_L;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.HNR_T;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.IND_DOC;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.IND_GEHEIM;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.IND_ONJUIST;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.INGANGSDAT_GELDIG;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.LAND_ONTBINDING;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.LAND_VERBINTENIS;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.LAND_VERTREK;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.LAND_VESTIGING;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.LOCATIEBESCHR;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.OMSCHR_REDEN_OPSCH_BIJHOUD;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.OMSCHR_VAN_DE_AANGIFTE_ADRESH;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.PLAATS_ONTBINDING;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.PLAATS_VERBINTENIS;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.POSTCODE;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.REDEN_ONTBINDING;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.SOORT_VERBINTENIS;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.STRAATNAAM;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.TITEL_PREDIKAAT;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.VOORNAMEN;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.VOORV_GESLACHTSNAAM;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.diensten.gba.ple.procura.utils.SortableObject;
import nl.procura.gba.jpa.probev.db.AnrWijz;
import nl.procura.gba.jpa.probev.db.AnrWijzPK;
import nl.procura.gba.jpa.probev.db.Huw;
import nl.procura.gba.jpa.probev.db.Inschr;
import nl.procura.gba.jpa.probev.db.Inw;
import nl.procura.gba.jpa.probev.db.Vb;
import nl.procura.gba.jpa.probev.db.Verw;
import nl.procura.gba.jpa.probev.db.Xhuw;
import nl.procura.gba.jpa.probev.db.Xinschr;
import nl.procura.gba.jpa.probev.db.Xinw;
import nl.procura.gba.jpa.probev.db.Xvb;

public class Person extends CustomTemplate {

  @Override
  public void parse(int nr, SortableObject so) {
    addCurrent(so);
    addArchive(so);
  }

  private void addCurrent(SortableObject so) {

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
        addElem(ANR, anr(a1, a2, a3));
      }

      addElem(BSN, (inw.getSnr() != null) ? inw.getSnr() : 0);
      addElem(VOORNAMEN, inw.getVoorn());
      addElem(TITEL_PREDIKAAT, inw.getTp());
      addElem(VOORV_GESLACHTSNAAM, inw.getVoorv());
      addElem(GESLACHTSNAAM, inw.getNaam());
      addElem(GEBOORTEDATUM, inw.getDGeb());
      addElem(GESLACHTSAAND, inw.getGeslacht());
      addElem(AANDUIDING_NAAMGEBRUIK, inw.getNaamgebruik());

    } else if (so.getObject() instanceof Huw) {

      addCat(GBACat.HUW_GPS, so);

      Huw huw = (Huw) so.getObject();
      addElem(VOORNAMEN, huw.getVoorn());
      addElem(TITEL_PREDIKAAT, huw.getTp());
      addElem(VOORV_GESLACHTSNAAM, huw.getVoorv());
      addElem(GESLACHTSNAAM, huw.getNaam());

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

    } else if (so.getObject() instanceof Inschr) {

      addCat(GBACat.INSCHR, so);

      Inschr inschr = (Inschr) so.getObject();
      addElem(OMSCHR_REDEN_OPSCH_BIJHOUD, inschr.getRdnOpschort());
      addElem(IND_GEHEIM, inschr.getIndGeheim());

    } else if (so.getObject() instanceof Vb) {

      addCat(GBACat.VB, so);

      Vb vb = (Vb) so.getObject();
      addElem(GEM_INSCHR, vb.getGInschr());
      addElem(DATUM_INSCHR, vb.getDInschr());

      if (vb.getDVertrek().intValue() != -1) { // groep 13 komt voor
        addElem(LAND_VERTREK, vb.getLVertrek());
        addElem(DATUM_VERTREK_UIT_NL, vb.getDVertrek());
        addElem(ADRES_BUITENL_1, vb.getBAdres1().getAdr());
        addElem(ADRES_BUITENL_2, vb.getBAdres2().getAdr());
        addElem(ADRES_BUITENL_3, vb.getBAdres3().getAdr());
      } else { // groep 13 komt niet voor

        if (vb.getDAanv().intValue() != -1) { // groep 10 komt voor
          addElem(FUNCTIE_ADRES, vb.getFuncAdr());
          addElem(GEM_DEEL, vb.getGemDeel());
          addElem(DATUM_AANVANG_ADRESH, vb.getDAanv());

          if (vb.getLocatie().getCLocatie() != -1) {
            addElem(LOCATIEBESCHR, vb.getLocatie());

          } else {
            addElem(STRAATNAAM, vb.getStraat());
            addElem(HNR, vb.getHnr());
            addElem(HNR_L, vb.getHnrL());
            addElem(HNR_T, vb.getHnrT());
            addElem(HNR_A, vb.getHnrA());
            addElem(POSTCODE, vb.getPc());
          }

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

      Verw verw = (Verw) so.getObject();
      addCat(GBACat.VERW, so);

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

      if (verw.getLocatie().getCLocatie() != -1) {
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

  private void addArchive(SortableObject so) {

    if (so.getObject() instanceof Xinw) {

      addCat(GBACat.PERSOON, so);

      Xinw inw = (Xinw) so.getObject();
      long a1 = inw.getId().getA1();
      long a2 = inw.getId().getA2();
      long a3 = inw.getId().getA3();

      AnrWijz anrWijz = getEntityManager().find(AnrWijz.class,
          new AnrWijzPK(anr(a1, a2, a3), inw.getIndAnr().longValue()));

      if (anrWijz != null) {
        addElem(ANR, anrWijz.getAnrOud());
      } else {
        addElem(ANR, anr(a1, a2, a3));
      }

      addElem(BSN, (inw.getSnr() != null) ? inw.getSnr() : 0);
      addElem(VOORNAMEN, inw.getVoorn());
      addElem(TITEL_PREDIKAAT, inw.getTp());
      addElem(VOORV_GESLACHTSNAAM, inw.getVoorv());
      addElem(GESLACHTSNAAM, inw.getNaam());
      addElem(GEBOORTEDATUM, inw.getDGeb());
      addElem(GESLACHTSAAND, inw.getGeslacht());
      addElem(AANDUIDING_NAAMGEBRUIK, inw.getNaamgebruik());

    } else if (so.getObject() instanceof Xhuw) {

      addCat(GBACat.HUW_GPS, so);

      Xhuw huw = (Xhuw) so.getObject();
      addElem(VOORNAMEN, huw.getVoorn());
      addElem(TITEL_PREDIKAAT, huw.getTp());
      addElem(VOORV_GESLACHTSNAAM, huw.getVoorv());
      addElem(GESLACHTSNAAM, huw.getNaam());

      if (huw.getDOntb().intValue() != -1) {
        addElem(DATUM_ONTBINDING, huw.getDOntb());
        addElem(PLAATS_ONTBINDING, huw.getPHuw());
        addElem(LAND_ONTBINDING, huw.getLOntb());
        addElem(REDEN_ONTBINDING, huw.getHuwOntb());
      }

      if (huw.getDHuw().intValue() != -1) {
        addElem(DATUM_VERBINTENIS, huw.getDHuw());
        addElem(PLAATS_VERBINTENIS, huw.getPHuw());
        addElem(LAND_VERBINTENIS, huw.getLHuw());
      }

      addElem(SOORT_VERBINTENIS, huw.getSoort());

    } else if (so.getObject() instanceof Xinschr) {

      addCat(GBACat.INSCHR, so);

      Xinschr inschr = (Xinschr) so.getObject();
      addElem(OMSCHR_REDEN_OPSCH_BIJHOUD, inschr.getRdnOpschort());
      addElem(IND_GEHEIM, inschr.getIndGeheim());

    } else if (so.getObject() instanceof Xvb) {

      addCat(GBACat.VB, so);

      Xvb vb = (Xvb) so.getObject();
      addElem(GEM_INSCHR, vb.getGInschr());
      addElem(DATUM_INSCHR, vb.getDInschr());

      if (vb.getDVertrek().intValue() != -1) { // groep 13 komt voor
        addElem(LAND_VERTREK, vb.getLVertrek());
        addElem(DATUM_VERTREK_UIT_NL, vb.getDVertrek());
        addElem(ADRES_BUITENL_1, vb.getBAdres1().getAdr());
        addElem(ADRES_BUITENL_2, vb.getBAdres2().getAdr());
        addElem(ADRES_BUITENL_3, vb.getBAdres3().getAdr());
      } else { // groep 13 komt niet voor

        if (vb.getDAanv().intValue() != -1) { // groep 10 komt voor
          addElem(FUNCTIE_ADRES, vb.getFuncAdr());
          addElem(GEM_DEEL, vb.getGemDeel());
          addElem(DATUM_AANVANG_ADRESH, vb.getDAanv());

          if (vb.getLocatie().getCLocatie() != -1) {
            addElem(LOCATIEBESCHR, vb.getLocatie());

          } else {
            addElem(STRAATNAAM, vb.getStraat());
            addElem(HNR, vb.getHnr());
            addElem(HNR_L, vb.getHnrL());
            addElem(HNR_T, vb.getHnrT());
            addElem(HNR_A, vb.getHnrA());
            addElem(POSTCODE, vb.getPc());
          }

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
    }
  }
}
