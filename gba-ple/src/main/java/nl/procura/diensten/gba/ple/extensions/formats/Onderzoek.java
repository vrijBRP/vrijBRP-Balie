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

package nl.procura.diensten.gba.ple.extensions.formats;

import static nl.procura.burgerzaken.gba.core.enums.GBACat.GEZAG;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.HUW_GPS;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.KINDEREN;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.NATIO;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.OUDER_1;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.OUDER_2;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.OVERL;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.PERSOON;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.REISDOC;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.VB;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.VBTITEL;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.AAND_GEG_IN_ONDERZ;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DATUM_EINDE_ONDERZ;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DATUM_INGANG_ONDERZ;
import static nl.procura.standard.Globalfunctions.aval;
import static nl.procura.standard.Globalfunctions.emp;
import static nl.procura.standard.Globalfunctions.fil;
import static nl.procura.standard.Globalfunctions.pos;

import java.util.ArrayList;
import java.util.List;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.burgerzaken.gba.core.enums.GBAGroup;
import nl.procura.burgerzaken.gba.core.enums.GBAGroupElements;
import nl.procura.diensten.gba.ple.base.BasePLCat;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.base.BasePLSet;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;

public class Onderzoek {

  private BasePLExt ext = new BasePLExt();

  public Onderzoek(BasePLExt ext) {
    this.ext = ext;
  }

  public List<OnderzoeksGeval> getHuidigeInOnderzoek() {
    List<OnderzoeksGeval> list = new ArrayList<>();
    for (OnderzoeksGeval geval : getCategoriesInOnderzoek()) {
      if (emp(geval.getD_end())) {
        list.add(geval);
      }
    }

    return list;
  }

  public List<OnderzoeksGeval> getCategoriesInOnderzoek() {
    List<OnderzoeksGeval> list = new ArrayList<>();

    for (BasePLCat soort : ext.getCats(PERSOON, OUDER_1, OUDER_2, NATIO, HUW_GPS,
        OVERL, VB, KINDEREN, VBTITEL, GEZAG, REISDOC)) {

      for (BasePLSet set : soort.getSets()) {
        for (BasePLRec rec : set.getRecs()) {
          String aand = rec.getElemVal(AAND_GEG_IN_ONDERZ).getDescr();
          String dIn = rec.getElemVal(DATUM_INGANG_ONDERZ).getDescr();
          String dEnd = rec.getElemVal(DATUM_EINDE_ONDERZ).getDescr();

          if (rec.isCorrect() && fil(aand) && emp(dEnd)) {
            list.add(new OnderzoeksGeval(dIn, dEnd, aand));
          }
        }
      }
    }

    return list;
  }

  public static class OnderzoeksGeval {

    private String d_in       = "";
    private String d_end      = "";
    private String aanduiding = "";

    public OnderzoeksGeval(String d_in, String d_end, String aanduiding) {
      setD_in(d_in);
      setD_end(d_end);
      setAanduiding(aanduiding);
    }

    public int getCatCode() {
      try {
        return aval(getAanduiding().replaceAll("\\D", "").substring(0, 2));
      } catch (Exception e) {
        return 0;
      }
    }

    public int getGroupCode() {
      try {
        return aval(getAanduiding().replaceAll("\\D", "").substring(2, 4));
      } catch (Exception e) {
        return 0;
      }
    }

    public int getElemCode() {
      try {
        return aval(getAanduiding().replaceAll("\\D", "").substring(4, 6));
      } catch (Exception e) {
        return 0;
      }
    }

    public String getCat() {
      try {
        return pos(getCatCode()) ? GBACat.getByCode(getCatCode()).getDescr() : "";
      } catch (Exception e) {
        return "";
      }
    }

    public String getGroup() {
      try {
        return pos(getGroupCode()) ? GBAGroup.getByCode(getGroupCode()).getDescr() : "";
      } catch (Exception e) {
        return "";
      }
    }

    public String getElem() {
      try {
        return GBAGroupElements.getByCat(getCatCode(), getElemCode()).getElem().getDescr();
      } catch (Exception e) {
        return "";
      }
    }

    public String getAanduiding() {
      return aanduiding;
    }

    public void setAanduiding(String aanduiding) {
      this.aanduiding = aanduiding;
    }

    public String getD_in() {
      return d_in;
    }

    public void setD_in(String d_in) {
      this.d_in = d_in;
    }

    public String getD_end() {
      return d_end;
    }

    public void setD_end(String d_end) {
      this.d_end = d_end;
    }
  }
}
