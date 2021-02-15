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

import static nl.procura.standard.Globalfunctions.pos;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.diensten.gba.ple.base.BasePLElem;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.base.BasePLValue;
import nl.procura.diensten.gba.ple.extensions.formats.Adres;
import nl.procura.diensten.gba.ple.extensions.formats.Geboorte;
import nl.procura.diensten.gba.ple.extensions.formats.Naam;

public class Cat21VerwExt extends BasePLCatExt {

  private BasePLValue anr = null;
  private BasePLValue bsn = null;

  public Cat21VerwExt(BasePLExt ext) {
    super(ext);
  }

  public Naam getNaam() {
    return new Naam(getExt().getLatestRec(GBACat.VERW));
  }

  public Geboorte getGeboorte() {
    GBACat cat = getExt().heeftVerwijzing() ? GBACat.VERW : GBACat.PERSOON;
    return new Geboorte(getExt().getLatestRec(cat),
        getExt().getLatestRec(GBACat.OVERL));
  }

  /**
   * Geeft het Bsn en bij ontbreken daarvan het A-nummer terug.
   */
  public BasePLValue getNummer() {
    if (pos(getBsn().getCode())) {
      return getBsn();
    }
    return getAnr();
  }

  public BasePLValue getAnr() {
    if (anr == null) {
      GBACat cat = getExt().heeftVerwijzing() ? GBACat.VERW : GBACat.PERSOON;
      anr = getElem(cat, GBAElem.ANR);
    }
    return anr;
  }

  public BasePLValue getBsn() {
    if (bsn == null) {
      GBACat cat = getExt().heeftVerwijzing() ? GBACat.VERW : GBACat.PERSOON;
      bsn = getElem(cat, GBAElem.BSN);
    }
    return bsn;
  }

  public Adres getAdres() {
    BasePLRec r = getExt().getLatestRec(GBACat.VERW);
    BasePLElem straat = r.getElem(GBAElem.STRAATNAAM);
    BasePLElem huisnummer = r.getElem(GBAElem.HNR);
    BasePLElem huisletter = r.getElem(GBAElem.HNR_L);
    BasePLElem huisnummertoev = r.getElem(GBAElem.HNR_T);
    BasePLElem huisnummeraand = r.getElem(GBAElem.HNR_A);
    BasePLElem locatie = r.getElem(GBAElem.LOCATIEBESCHR);
    BasePLElem postcode = r.getElem(GBAElem.POSTCODE);
    BasePLElem gemeente = r.getElem(GBAElem.GEM_INSCHR);
    BasePLElem datum_aanvang = r.getElem(GBAElem.DATUM_INSCHR);

    BasePLElem gemeentedeel = new BasePLElem();
    BasePLElem woonplaats = new BasePLElem();
    BasePLElem emigratieland = new BasePLElem();
    BasePLElem emigratiedatum = new BasePLElem();
    BasePLElem buitenland1 = new BasePLElem();
    BasePLElem buitenland2 = new BasePLElem();
    BasePLElem buitenland3 = new BasePLElem();

    return new Adres(straat, huisnummer, huisletter, huisnummertoev, huisnummeraand, locatie, postcode,
        gemeentedeel, woonplaats, gemeente, datum_aanvang, emigratieland, emigratiedatum, buitenland1,
        buitenland2, buitenland3);
  }
}
