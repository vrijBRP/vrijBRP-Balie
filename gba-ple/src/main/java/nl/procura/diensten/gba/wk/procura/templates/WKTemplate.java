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

package nl.procura.diensten.gba.wk.procura.templates;

import static nl.procura.standard.Globalfunctions.astr;

import nl.procura.diensten.gba.ple.procura.utils.diacrits.Diacs;
import nl.procura.diensten.gba.wk.baseWK.BaseWK;
import nl.procura.diensten.gba.wk.baseWK.BaseWKPerson;
import nl.procura.diensten.gba.wk.baseWK.BaseWKValue;
import nl.procura.diensten.gba.wk.procura.argumenten.ZoekArgumenten;
import nl.procura.gba.jpa.probev.db.GemDeel;
import nl.procura.gba.jpa.probev.db.Locatie;
import nl.procura.gba.jpa.probev.db.Obj1;
import nl.procura.gba.jpa.probev.db.Obj2;
import nl.procura.gba.jpa.probev.db.Obj3;
import nl.procura.gba.jpa.probev.db.Obr;
import nl.procura.gba.jpa.probev.db.Ppd;
import nl.procura.gba.jpa.probev.db.Stem;
import nl.procura.gba.jpa.probev.db.Straat;
import nl.procura.gba.jpa.probev.db.Vbo;
import nl.procura.gba.jpa.probev.db.VboKrt;
import nl.procura.gba.jpa.probev.db.VboKrtPK;
import nl.procura.gba.jpa.probev.db.VboPK;
import nl.procura.gba.jpa.probev.db.WonInd;
import nl.procura.gba.jpa.probev.db.Woning;
import nl.procura.gba.jpa.probev.db.Wpl;

public class WKTemplate extends WKTemplateProcura {

  private ZoekArgumenten zoekArgumenten = new ZoekArgumenten();

  @Override
  public void parse() {

    WKSearchObjectTemplate zoekTemplate = new WKSearchObjectTemplate();
    zoekTemplate.setEntityManager(getEntityManager());
    zoekTemplate.setSearchArguments(zoekArgumenten);
    zoekTemplate.setBuilder(getBuilder());
    zoekTemplate.parse();

    // Objecten worden gezocht obv de zoekargumenten

    for (Vbo object : zoekTemplate.getVbos()) {

      getBuilder().addNewWk();
      fillAdres(object, zoekTemplate.getVbos().size());
    }
  }

  public void fillAdres(Vbo object, int count_objecten) {

    BaseWK basiswk = getBuilder().getCurrentWK();

    Straat straat = object.getStraat();
    Locatie locatie = object.getLocatie();
    GemDeel gemdeel = object.getGemDeel();
    Wpl wpl = object.getWpl();
    Ppd ppd = object.getPpd();
    Stem stem = object.getStem1();
    Obj1 wijk = object.getObj1();
    Obj2 buurt = object.getObj2();
    Obj3 sbuurt = object.getObj3();
    Woning woning = object.getWoning();
    WonInd wInd = object.getWonInd();
    VboPK vko = object.getId();
    Obr obr = object.getObr();

    basiswk.getStraat().setCode(astr(straat.getCStraat())).setValue(getDiacriet(straat, Diacs.STRAAT));
    basiswk.getHuisnummer().setCode(astr(object.getHnr()));
    basiswk.getHuisletter().setCode(object.getHnrL());
    basiswk.getToevoeging().setCode(object.getHnrT());
    basiswk.getAanduiding().setCode(object.getHnrA());
    basiswk.getPnd().setCode(astr(object.getPnd()));
    basiswk.getPostcode().setCode(object.getPc()).setDescr(getPostcodeFormat(object.getPc()));
    basiswk.getLocatie().setCode(astr(locatie.getCLocatie())).setValue(getDiacriet(locatie, Diacs.LOCATIE));
    basiswk.getGemeentedeel().setCode(astr(gemdeel.getCGemDeel())).setValue(getDiacriet(gemdeel, Diacs.GEM_DEEL));
    basiswk.getAdres_indicatie().setCode(astr(object.getAdresind().getAdrInd())).setDescr(
        object.getAdresind().getOms());
    basiswk.getPpd().setCode(astr(ppd.getCPpd())).setValue(ppd.getPpd());
    basiswk.getStemdistrict().setCode(astr(stem.getCStem())).setValue(getDiacriet(stem, Diacs.STEM));
    basiswk.getWijk().setCode(astr(wijk.getCObj())).setValue(getDiacriet(wijk, Diacs.OBJ_1, wijk.getObj()));
    basiswk.getBuurt().setCode(astr(buurt.getCObj())).setValue(getDiacriet(buurt, Diacs.OBJ_2, buurt.getObj()));
    basiswk.getSub_buurt().setCode(astr(sbuurt.getCObj())).setValue(
        getDiacriet(sbuurt, Diacs.OBJ_3, sbuurt.getObj()));
    basiswk.getWoning().setCode(astr(woning.getCWoning())).setValue(getDiacriet(woning, Diacs.WONING));
    basiswk.getWoning_indicatie().setCode(astr(wInd.getWonInd())).setValue(
        getDiacriet(wInd, Diacs.WON_IND, wInd.getOms()));
    basiswk.setDatum_ingang(getDate(object.getDIn().intValue()));
    basiswk.setDatum_einde(getDate(vko.getDEnd().intValue()));
    basiswk.getVolgcode_einde().setCode(astr(vko.getVEnd()));
    basiswk.getCode_object().setCode(astr(vko.getCVbo()));
    basiswk.getOpmerking().setValue(object.getOpm());

    // Bag elementen
    basiswk.getWoonplaats().setCode(astr(wpl.getCWplBag())).setValue(getDiacriet(wpl, Diacs.WPL));
    basiswk.getOpenbareRuimte().setCode(astr(obr.getCObr())).setValue(obr.getObr());
    basiswk.getAon().setCode(astr(object.getAon()));
    basiswk.getIna().setCode(astr(object.getIna()));

    if ((count_objecten == 1) && !getZoekArgumenten().isGeen_bewoners()) {
      fillPersonen(object);
    }
  }

  public void fillPersonen(Vbo object) {
    BaseWK basiswk = getBuilder().getCurrentWK();
    WKSearchWKPersonsTemplate zoekTemplate = new WKSearchWKPersonsTemplate();
    zoekTemplate.setEntityManager(getEntityManager());
    zoekTemplate.setBuilder(getBuilder());
    zoekTemplate.setObject(object);
    zoekTemplate.parse();

    for (VboKrt vboKrt : zoekTemplate.getPersons()) {
      BaseWKPerson persoon = new BaseWKPerson();
      String anr = anr(vboKrt.getId().getA1(), vboKrt.getId().getA2(), vboKrt.getId().getA3());

      persoon.getAnummer()
          .setCode(anr)
          .setDescr(getAnrFormat(anr));

      if (getZoekArgumenten().isExtra_pl_gegevens()) {
        WKSearchPersonTemplate inwZoekTemplate = new WKSearchPersonTemplate();
        inwZoekTemplate.setEntityManager(getEntityManager());
        inwZoekTemplate.setBuilder(getBuilder());
        inwZoekTemplate.setA1(vboKrt.getId().getA1().intValue());
        inwZoekTemplate.setA2(vboKrt.getId().getA2().intValue());
        inwZoekTemplate.setA3(vboKrt.getId().getA3().intValue());
        inwZoekTemplate.parse();

        persoon.getBsn()
            .setCode(inwZoekTemplate.getBsn())
            .setValue(padl(persoon.getBsn().getCode(), 9))
            .setDescr(getFormatBsn(inwZoekTemplate.getBsn()));
      }

      VboKrtPK krt = vboKrt.getId();

      persoon.setDatum_ingang(getDate(vboKrt.getDIn().intValue()));
      persoon.setDatum_vertrek(getDate(krt.getDEnd().intValue()));
      persoon.setDatum_geboren(getDate(vboKrt.getDGeb().intValue()));
      persoon.getGezin_code().setCode(astr(vboKrt.getVGezin()));
      persoon.getVolg_code().setCode(astr(vboKrt.getVInw()));

      basiswk.getPersonen().add(persoon);
    }
  }

  private BaseWKValue getDate(Integer code) {
    BaseWKValue waarde = new BaseWKValue();
    waarde.setCode(astr(code));

    if (code == 0) {
      waarde.setDescr("00-00-0000");
    } else if (code == -1) {
      waarde.setDescr("");
    } else {
      waarde.setDescr(getDateFormat(astr(code)));
    }

    return waarde;
  }

  public ZoekArgumenten getZoekArgumenten() {
    return zoekArgumenten;
  }

  public void setZoekArgumenten(ZoekArgumenten zoekArgumenten) {
    this.zoekArgumenten = zoekArgumenten;
  }
}
