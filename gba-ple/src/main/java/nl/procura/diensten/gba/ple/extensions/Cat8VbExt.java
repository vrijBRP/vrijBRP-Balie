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

package nl.procura.diensten.gba.ple.extensions;

import static nl.procura.burgerzaken.gba.core.enums.GBACat.VB;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DATUM_VESTIGING_IN_NL;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.LAND_VESTIGING;
import static nl.procura.standard.Globalfunctions.pos;

import java.util.ArrayList;
import java.util.List;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.diensten.gba.ple.base.BasePLElem;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.base.BasePLSet;
import nl.procura.diensten.gba.ple.base.BasePLValue;
import nl.procura.diensten.gba.ple.extensions.formats.Adres;
import nl.procura.standard.ProcuraDate;

public class Cat8VbExt extends BasePLCatExt {

  public Cat8VbExt(BasePLExt ext) {
    super(ext);
  }

  public BasePLValue getLand() {
    if (pos(getAdres().getEmigratieland().getValue().getCode())) {
      return getAdres().getEmigratieland().getValue();
    }

    return new BasePLValue("6030", "Nederland");
  }

  public List<Adres> getAdressen() {
    List<Adres> list = new ArrayList<>();
    if (getExt().heeftVerwijzing()) {
      list.add(getAdres(getExt().getLatestRec(GBACat.VERW)));
    } else {
      for (BasePLSet set : getExt().getCat(GBACat.VB).getSets()) {
        for (BasePLRec r : set.getRecs()) {
          list.add(getAdres(r));
        }
      }
    }

    return list;
  }

  public Adres getAdres() {
    return getAdres(getExt().getLatestRec(
        getExt().heeftVerwijzing() ? GBACat.VERW : GBACat.VB));
  }

  public BasePLValue getImmigratieland() {
    return getExt().getLatestRec(VB).getElemVal(LAND_VESTIGING);
  }

  public BasePLValue getImmigratiedatum() {
    return getExt().getLatestRec(VB).getElemVal(DATUM_VESTIGING_IN_NL);
  }

  public int getDagenInNL() {
    return new ProcuraDate(getImmigratiedatum().getVal()).diffInDays(new ProcuraDate());
  }

  public boolean is185DagenInNL() {

    String dVestiging = getImmigratiedatum().getVal();
    return !pos(dVestiging) || new ProcuraDate(dVestiging).diffInDays(new ProcuraDate()) > 185;
  }

  private Adres getAdres(BasePLRec r) {
    BasePLElem straat;
    BasePLElem huisnummer;
    BasePLElem huisletter;
    BasePLElem huisnummertoev;
    BasePLElem huisnummeraand;
    BasePLElem locatie;
    BasePLElem postcode;
    BasePLElem gemeente;
    BasePLElem datum_aanvang;

    BasePLElem gemeentedeel = new BasePLElem();
    BasePLElem woonplaats = new BasePLElem();
    BasePLElem emigratieland = new BasePLElem();
    BasePLElem emigratiedatum = new BasePLElem();
    BasePLElem buitenland1 = new BasePLElem();
    BasePLElem buitenland2 = new BasePLElem();
    BasePLElem buitenland3 = new BasePLElem();

    if (r.getCatType() == GBACat.VERW) {

      straat = r.getElem(GBAElem.STRAATNAAM);
      huisnummer = r.getElem(GBAElem.HNR);
      huisletter = r.getElem(GBAElem.HNR_L);
      huisnummertoev = r.getElem(GBAElem.HNR_T);
      huisnummeraand = r.getElem(GBAElem.HNR_A);
      locatie = r.getElem(GBAElem.LOCATIEBESCHR);
      postcode = r.getElem(GBAElem.POSTCODE);
      gemeente = r.getElem(GBAElem.GEM_INSCHR);
      datum_aanvang = r.getElem(GBAElem.DATUM_INSCHR);
    } else {

      straat = r.getElem(GBAElem.STRAATNAAM);
      huisnummer = r.getElem(GBAElem.HNR);
      huisletter = r.getElem(GBAElem.HNR_L);
      huisnummertoev = r.getElem(GBAElem.HNR_T);
      huisnummeraand = r.getElem(GBAElem.HNR_A);
      locatie = r.getElem(GBAElem.LOCATIEBESCHR);
      postcode = r.getElem(GBAElem.POSTCODE);
      gemeentedeel = r.getElem(GBAElem.GEM_DEEL);
      woonplaats = r.getElem(GBAElem.WPL_NAAM);
      gemeente = r.getElem(GBAElem.GEM_INSCHR);
      datum_aanvang = r.getElem(GBAElem.DATUM_INSCHR);
      emigratieland = r.getElem(GBAElem.LAND_VERTREK);
      emigratiedatum = r.getElem(GBAElem.DATUM_VERTREK_UIT_NL);
      buitenland1 = r.getElem(GBAElem.ADRES_BUITENL_1);
      buitenland2 = r.getElem(GBAElem.ADRES_BUITENL_2);
      buitenland3 = r.getElem(GBAElem.ADRES_BUITENL_3);
    }

    return new Adres(straat, huisnummer, huisletter, huisnummertoev, huisnummeraand, locatie, postcode,
        gemeentedeel, woonplaats, gemeente, datum_aanvang, emigratieland, emigratiedatum, buitenland1,
        buitenland2, buitenland3);
  }
}
