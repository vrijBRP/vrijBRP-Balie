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

import static nl.procura.gba.common.MiscUtils.isNrEquals;
import static nl.procura.standard.Globalfunctions.*;

import java.util.Arrays;
import java.util.List;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.diensten.gba.ple.base.*;
import nl.procura.diensten.gba.ple.extensions.formats.*;
import nl.procura.diensten.gba.ple.extensions.formats.Onderzoek.OnderzoeksGeval;

public class Cat1PersoonExt extends BasePLCatExt {

  private BasePLValue anr = null;
  private BasePLValue bsn = null;

  public Cat1PersoonExt(BasePLExt ext) {
    super(ext);
  }

  public BasePLElem getTitel() {
    return getExt().getLatestRec(GBACat.PERSOON).getElem(
        GBAElem.TITEL_PREDIKAAT);
  }

  public Naam getNaam() {

    BasePLRec r;
    BasePLElem ng = new BasePLElem();
    Naam partner = null;

    if (getExt().heeftVerwijzing()) {
      r = getExt().getLatestRec(GBACat.VERW);
    } else {
      r = getExt().getLatestRec(GBACat.PERSOON);
      ng = r.getElem(GBAElem.AANDUIDING_NAAMGEBRUIK);
    }

    BasePLSet partnerSet = getExt().getHuwelijk().getHuwelijkSet();
    if (partnerSet.getRecs().isNotEmpty()) {
      BasePLRec partnerRecord = partnerSet.getLatestRec();
      if (partnerRecord.hasElems()) {
        partner = new Naam(partnerRecord);
      }
    }

    return new Naam(r, ng, partner);
  }

  public BasePLValue getGeslacht() {
    return getExt().getLatestRec(GBACat.PERSOON).getElem(
        GBAElem.GESLACHTSAAND).getValue();
  }

  public BasePLValue getAkteNummer() {
    return getAkteWaarde(GBAElem.AKTENR);
  }

  public BasePLValue getAkteGemeente() {
    return getAkteWaarde(GBAElem.REGIST_GEM_AKTE);
  }

  public BurgerlijkeStaat getBurgerlijkeStand() {
    return new BurgerlijkeStaat(getExt());
  }

  public Geboorte getGeboorte() {
    GBACat cat = getExt().heeftVerwijzing() ? GBACat.VERW : GBACat.PERSOON;
    return new Geboorte(getExt().getLatestRec(cat),
        getExt().getLatestRec(GBACat.OVERL));
  }

  public Status getStatus() {
    Status status = new Status();
    int countMutaties = 0;
    for (BasePLCat st : getExt().getCats()) {
      for (BasePLSet set : st.getSets()) {
        if (set.isMutation()) {
          countMutaties++;
          status.getCatMutaties().add(st.getCatType());
        }
      }
    }

    status.setMutaties(countMutaties);
    status.setOnverwerktDocument(pos(getExt().getLatestRec(GBACat.VB).getElem(
        GBAElem.IND_DOC).getValue().getCode()));

    status.setHeeftBriefAdres(getExt().getLatestRec(GBACat.VB).getElem(
        GBAElem.FUNCTIE_ADRES).getValue().getCode().equalsIgnoreCase("B"));

    status.setStaatOnderCuratele(isStaatOnderCuratele());
    status.setGeheim(isGeheim());
    status.setGeheimCode(getGeheim());

    String rdn_opsch = getRedenOpgeschorting().getCode();

    if (fil(rdn_opsch)) {
      status.setOpgeschort(true);
      status.setOverleden(rdn_opsch.equalsIgnoreCase("O"));
      status.setEmigratie(rdn_opsch.equalsIgnoreCase("E"));
      status.setRni(rdn_opsch.equalsIgnoreCase("R"));
      status.setMinisterieelBesluit(rdn_opsch.equalsIgnoreCase("M"));
    }

    status.setBlokkering(pos(getElem(GBACat.INSCHR, GBAElem.DATUM_INGANG_BLOK_PL).getCode()));
    status.setVerwijzing(getExt().heeftVerwijzing());

    List<OnderzoeksGeval> onderzoekCats = getOnderzoek().getHuidigeInOnderzoek();

    if ((onderzoekCats.size() == 1) && (onderzoekCats.get(
        0).getCatCode() == GBACat.VB.getCode())) {
      status.setAdresInOnderzoek(true);
    } else if (pos(onderzoekCats.size())) {
      status.setInOnderzoek(true);
    }

    return status;
  }

  public int getGeheim() {
    return aval(defaultNul(getElem(GBACat.INSCHR, GBAElem.IND_GEHEIM).getCode()));
  }

  public boolean isStaatOnderCuratele() {
    return getExt().getGezag().staatOnderCuratele();
  }

  public boolean isGeheim() {
    return pos(getGeheim());
  }

  public BasePLValue getRedenOpgeschorting() {
    return getElem(GBACat.INSCHR, GBAElem.OMSCHR_REDEN_OPSCH_BIJHOUD);
  }

  public boolean isNr(String... nrs) {
    return Arrays.stream(nrs)
        .anyMatch(nr -> isNrEquals(nr, getAnr().getCode()) ||
            isNrEquals(nr, getBsn().getCode()));
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

  public Onderzoek getOnderzoek() {
    return new Onderzoek(getExt());
  }

  private BasePLValue getAkteWaarde(GBAElem element) {
    for (BasePLSet set : getExt().getCat(GBACat.PERSOON).getSets()) {
      for (BasePLRec record : set.getRecs()) {
        if (!record.isIncorrect() && !record.getElem(element).isEmpty()) {
          return record.getElemVal(element);
        }
      }
    }
    return getExt().getLatestRec(GBACat.PERSOON).getElem(element).getValue();
  }
}
