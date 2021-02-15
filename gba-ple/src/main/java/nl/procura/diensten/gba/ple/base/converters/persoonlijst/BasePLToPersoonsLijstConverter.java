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

package nl.procura.diensten.gba.ple.base.converters.persoonlijst;

import static java.util.Collections.singletonList;
import static nl.procura.burgerzaken.gba.core.enums.GBACat.KLADBLOK;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.*;
import static nl.procura.burgerzaken.gba.core.enums.GBARecStatus.HIST;
import static nl.procura.standard.Globalfunctions.*;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.burgerzaken.gba.core.enums.GBARecStatus;
import nl.procura.burgerzaken.gba.core.validators.DatVal;
import nl.procura.diensten.gba.ple.base.*;
import nl.procura.diensten.zoekpersoon.objecten.*;
import nl.procura.standard.ProcuraDate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BasePLToPersoonsLijstConverter {

  private Object targetObject = new Object();

  public Persoonslijst convert(BasePL basisPersoonslijst, boolean expanderen, boolean history, boolean webservice) {
    return convert(singletonList(basisPersoonslijst), expanderen, history, webservice).get(0);
  }

  public List<Persoonslijst> convert(List<BasePL> basisPersoonslijsten, boolean expand, boolean history,
      boolean webservice) {

    targetObject = new Object();

    List<Persoonslijst> persoonslijsten = new ArrayList<>();

    for (BasePL basisPl : basisPersoonslijsten) {

      Persoonslijst expandPl = new Persoonslijst();
      persoonslijsten.add(expandPl);

      addMetaData(basisPl, expandPl);

      for (BasePLCat cat : basisPl.getCats()) {
        for (BasePLSet set : cat.getSets()) {
          List<BasePLRec> recs = new ArrayList<>();
          recs.add(set.getLatestRec());
          recs.addAll(set.getHistRecs());

          for (BasePLRec record : recs) {
            boolean isIncorrect = record.isIncorrect() && record.isStatus(GBARecStatus.HIST);
            if (isIncorrect || record.isBagChange() || record.isStatus(GBARecStatus.UNKNOWN)) {
              continue;
            }

            try {
              addRecord(expandPl, record, expand, history, webservice);
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        }
      }
    }

    return persoonslijsten;
  }

  private void addRecord(Persoonslijst expandPl, BasePLRec rec, boolean expand, boolean history,
      boolean webservice) {

    switch (rec.getCatType()) {
      case PERSOON: // Persoon
        if (expandPl.getPersoon() == null) {
          expandPl.setPersoon(new Persoon());
        }

        targetObject = new Persoonsgegevens();
        if (rec.isStatus(HIST)) {
          if (history) {
            expandPl.getPersoon().setPersoon_historie(addToList(expandPl.getPersoon().getPersoon_historie(),
                targetObject));
          }
        } else {
          expandPl.getPersoon().setPersoon_actueel((Persoonsgegevens) targetObject);
        }

        break;

      case OUDER_1: // Ouder1
        if (expandPl.getOuder_1() == null) {
          expandPl.setOuder_1(new Ouder());
        }

        targetObject = new Oudergegevens();
        if (rec.isStatus(HIST)) {
          if (history) {
            expandPl.getOuder_1().setOuder_historie(addToList(expandPl.getOuder_1().getOuder_historie(), targetObject));
          }
        } else {
          expandPl.getOuder_1().setOuder_actueel((Oudergegevens) targetObject);
        }

        break;

      case OUDER_2: // Ouder2
        if (expandPl.getOuder_2() == null) {
          expandPl.setOuder_2(new Ouder());
        }

        targetObject = new Oudergegevens();
        if (rec.isStatus(HIST)) {
          if (history) {
            expandPl.getOuder_2().setOuder_historie(addToList(expandPl.getOuder_2().getOuder_historie(), targetObject));
          }
        } else {
          expandPl.getOuder_2().setOuder_actueel((Oudergegevens) targetObject);
        }

        break;

      case NATIO: // Nationaliteit
        targetObject = new Nationaliteitgegevens();

        if (rec.isStatus(HIST)) {
          if (history) {
            Nationaliteit n = getLastObject(expandPl.getNationaliteiten());
            n.setNationaliteit_historie(addToList(n.getNationaliteit_historie(), targetObject));
          }
        } else {
          Nationaliteit nat = new Nationaliteit();
          expandPl.setNationaliteiten(addToList(expandPl.getNationaliteiten(), nat));
          nat.setNationaliteit_actueel((Nationaliteitgegevens) targetObject);
        }

        break;

      case HUW_GPS: // Huwelijk
        targetObject = new Huwelijkgegevens();

        // Set indication for most recent commitment
        ((Huwelijkgegevens) targetObject)
            ._setAanduiding_recentste_verbintenis(rec.getSet().isMostRecentSet() ? "1" : "");

        if (rec.isStatus(HIST)) {
          if (history) {
            Huwelijk h = getLastObject(expandPl.getHuwelijken());
            h.setHuwelijk_historie(addToList(h.getHuwelijk_historie(), targetObject));
          }
        } else {
          Huwelijk huw = new Huwelijk();
          expandPl.setHuwelijken(addToList(expandPl.getHuwelijken(), huw));
          huw.setHuwelijk_actueel((Huwelijkgegevens) targetObject);
        }

        break;

      case OVERL: // Overlijden
        if (expandPl.getOverlijden() == null) {
          expandPl.setOverlijden(new Overlijden());
        }

        targetObject = new Overlijdengegevens();
        if (rec.isStatus(HIST)) {
          if (history) {
            expandPl.getOverlijden().setOverlijden_historie(addToList(expandPl.getOverlijden().getOverlijden_historie(),
                targetObject));
          }
        } else {
          expandPl.getOverlijden().setOverlijden_actueel((Overlijdengegevens) targetObject);
        }

        break;

      case INSCHR: // Inschrijving
        if (expandPl.getInschrijving() == null) {
          expandPl.setInschrijving(new Inschrijving());
        }

        targetObject = new Inschrijvinggegevens();
        if (rec.isStatus(HIST)) {
          if (history) {
            expandPl.getInschrijving().setInschrijving_historie(addToList(
                expandPl.getInschrijving().getInschrijving_historie(), targetObject));
          }
        } else {
          expandPl.getInschrijving().setInschrijving_actueel((Inschrijvinggegevens) targetObject);
        }

        break;

      case VB: // Verblijfplaats
        if (expandPl.getVerblijfplaats() == null) {
          expandPl.setVerblijfplaats(new Verblijfplaats());
        }

        targetObject = new Verblijfplaatsgegevens();
        if (rec.isStatus(HIST)) {
          if (history) {
            Verblijfplaatsgegevens[] gegevens = addToList(
                expandPl.getVerblijfplaats().getVerblijfplaats_historie(), targetObject);

            expandPl.getVerblijfplaats().setVerblijfplaats_historie(gegevens);
          }
        } else {
          expandPl.getVerblijfplaats().setVerblijfplaats_actueel((Verblijfplaatsgegevens) targetObject);
        }

        break;

      case KINDEREN: // Kind
        targetObject = new Kindgegevens();

        Kindgegevens kindgegevens = (Kindgegevens) this.targetObject;
        kindgegevens._setRegistratie_betrekking(rec.getElemVal(REG_BETREKK).getVal());

        if (rec.isStatus(HIST)) {
          if (history) {
            Kind k = getLastObject(expandPl.getKinderen());
            k.setKind_historie(addToList(k.getKind_historie(), this.targetObject));
          }
        } else {
          Kind k = new Kind();
          expandPl.setKinderen(addToList(expandPl.getKinderen(), k));
          k.setKind_actueel(kindgegevens);
        }

        break;

      case VBTITEL: // Verblijfstitel
        if (expandPl.getVerblijfstitel() == null) {
          expandPl.setVerblijfstitel(new Verblijfstitel());
        }

        this.targetObject = new Verblijfstitelgegevens();
        if (rec.isStatus(HIST)) {
          if (history) {
            Verblijfstitelgegevens[] gegevens = addToList(
                expandPl.getVerblijfstitel().getVerblijfstitel_historie(), this.targetObject);
            expandPl.getVerblijfstitel().setVerblijfstitel_historie(gegevens);
          }
        } else {
          expandPl.getVerblijfstitel().setVerblijfstitel_actueel((Verblijfstitelgegevens) this.targetObject);
        }

        break;

      case GEZAG: // Gezag
        if (expandPl.getGezag() == null) {
          expandPl.setGezag(new Gezag());
        }

        this.targetObject = new Gezaggegevens();
        if (rec.isStatus(HIST)) {
          if (history) {
            expandPl.getGezag()
                .setGezag_historie(addToList(expandPl.getGezag().getGezag_historie(), this.targetObject));
          }
        } else {
          expandPl.getGezag().setGezag_actueel((Gezaggegevens) this.targetObject);
        }

        break;

      case REISDOC: // Reisdocument

        this.targetObject = new Reisdocumentgegevens();
        if (rec.isStatus(HIST)) {
          if (history) {
            Reisdocument r = getLastObject(expandPl.getReisdocumenten());
            r.setReisdocument_historie(addToList(r.getReisdocument_historie(), this.targetObject));
          }
        } else {
          Reisdocument r = new Reisdocument();
          expandPl.setReisdocumenten(addToList(expandPl.getReisdocumenten(), r));
          r.setReisdocument_actueel((Reisdocumentgegevens) this.targetObject);
        }

        break;

      case KIESR: // Kiesrecht
        if (expandPl.getKiesrecht() == null) {
          expandPl.setKiesrecht(new Kiesrecht());
        }

        this.targetObject = new Kiesrechtgegevens();
        if (rec.isStatus(HIST)) {
          if (history) {
            Kiesrechtgegevens[] gegevens = addToList(
                expandPl.getKiesrecht().getKiesrecht_historie(), this.targetObject);
            expandPl.getKiesrecht().setKiesrecht_historie(gegevens);
          }
        } else {
          expandPl.getKiesrecht().setKiesrecht_actueel((Kiesrechtgegevens) this.targetObject);
        }

        break;

      case AFNEMERS: // Afnemer
        this.targetObject = new Afnemergegevens();
        if (rec.isStatus(HIST)) {
          if (history) {
            Afnemer a = getLastObject(expandPl.getAfnemers());
            a.setAfnemer_historie(addToList(a.getAfnemer_historie(), this.targetObject));
          }
        } else {
          Afnemer a = new Afnemer();
          expandPl.setAfnemers(addToList(expandPl.getAfnemers(), a));
          a.setAfnemer_actueel((Afnemergegevens) this.targetObject);
        }

        break;

      case VERW: // Verwijzing
        this.targetObject = new Verwijzinggegevens();
        if (expandPl.getVerwijzing() == null) {
          expandPl.setVerwijzing(new Verwijzing());
        }

        if (rec.isStatus(HIST)) {
          if (history) {
            expandPl.getVerwijzing().setVerwijzing_historie(addToList(expandPl.getVerwijzing().getVerwijzing_historie(),
                this.targetObject));
          }
        } else {
          expandPl.getVerwijzing().setVerwijzing_actueel((Verwijzinggegevens) this.targetObject);
        }

        break;

      case DIV: // Diversen
        if (expandPl.getDiversen() == null) {
          expandPl.setDiversen(new Diversen());
        }
        this.targetObject = new Diversengegevens();
        if (rec.isStatus(HIST)) {
          if (history) {
            expandPl.getDiversen().setDiversen_historie(addToList(expandPl.getDiversen().getDiversen_historie(),
                this.targetObject));
          }
        } else {
          expandPl.getDiversen().setDiversen_actueel((Diversengegevens) this.targetObject);
        }

        break;

      case WK: // Woningkaart
        this.targetObject = new Woningkaartgegevens();
        if (expandPl.getWoningkaart() == null) {
          expandPl.setWoningkaart(new Woningkaart());
        }

        if (rec.isStatus(HIST)) {
          if (history) {
            expandPl.getWoningkaart()
                .setWoningkaart_historie(addToList(expandPl.getWoningkaart().getWoningkaart_historie(),
                    this.targetObject));
          }
        } else {
          expandPl.getWoningkaart().setWoningkaart_actueel((Woningkaartgegevens) this.targetObject);
        }

        break;

      case KLADBLOK: // Vrijeaantekening
        Kladblokaantekening aantek = expandPl.getKladblokaantekening();
        if (aantek == null) {
          aantek = new Kladblokaantekening();
          expandPl.setKladblokaantekening(aantek);
        }

        this.targetObject = aantek;

        break;

      case LOK_AF_IND: // Lokale Afnemer indicaties
        Lokaleafnemersindicatie llg = new Lokaleafnemersindicatie();
        expandPl.setLokaleafnemersindicaties(addToList(expandPl.getLokaleafnemersindicaties(), llg));
        this.targetObject = llg;
        break;

      default:
        break;
    }

    for (BasePLElem elem : rec.getElems()) {
      int catCode = rec.getCatType().getCode();
      if (catCode == KLADBLOK.getCode()) { // Kladblok
        Kladblokaantekening aantek = (Kladblokaantekening) targetObject;
        aantek.setRegels(addToList(aantek.getRegels(), elem.getValue().getDescr()));

      } else {
        String elementName = PersoonsLijstTypes.getName(catCode, elem.getElemCode());
        if (fil(elementName)) {
          String value = getValue(elem, expand, webservice);
          inVoke(catCode, elem.getElemCode(), targetObject, elementName, value);
        }
      }
    }
  }

  private String getValue(BasePLElem element, boolean expand, boolean webservice) {
    if (!element.isAllowed()) {
      return "";
    }

    GBAElem gbaElement = getByCode(element.getElemCode());
    if (element.isElement(AAND_VBT)) {
      return element.getValue().getVal();
    }

    if (expand) {
      if (gbaElement.getTable().isNational()) {
        return element.getValue().getDescr();
      } else if (element.isElement(ANR, BSN)) {
        if (webservice) {
          return element.getValue().getVal();
        }
        return element.getValue().getDescr();
      }

      if (!webservice) {
        if (gbaElement.getVal() instanceof DatVal) {
          ProcuraDate date = new ProcuraDate();
          date.setForceFormatType(ProcuraDate.SYSTEMDATE_ONLY);
          date.setAllowedFormatExceptions(true);
          date.setStringDate(element.getValue().getVal());

          if (date.isCorrect()) {
            return date.getFormatDate();
          }

          return element.getValue().getDescr();
        }

        if (element.isElement(GESLACHTSAAND, SOORT_VERBINTENIS, REDEN_ONTBINDING, IND_GEZAG_MINDERJ)) {
          return element.getValue().getDescr();
        }
      }
    }

    return element.getValue().getVal();
  }

  private void addMetaData(BasePL basePL, Persoonslijst expandPl) {
    List<MetaInfoGegevens> metaList = new ArrayList<>();
    for (Object key : basePL.getMetaInfo().keySet().toArray()) {
      metaList.add(new MetaInfoGegevens(astr(key), astr(basePL.getMetaInfo().get(key))));
    }
    expandPl.setMetaInfoGegevens(metaList.toArray(new MetaInfoGegevens[metaList.size()]));
  }

  @SuppressWarnings("unchecked")
  private <T> T addToList(Object[] T, Object obj) {
    int length = (T != null) ? T.length : 0;
    Object[] newArray = (Object[]) Array.newInstance(obj.getClass(), length + BigDecimal.ONE.intValue());
    if (T != null) {
      System.arraycopy(T, 0, newArray, 0, T.length);
    }
    newArray[newArray.length - 1] = obj;
    return (T) newArray;
  }

  private <T> T getLastObject(T[] array) {
    return array[array.length - 1];
  }

  private String toSet(String name) {
    return "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
  }

  private void inVoke(int catNr, int elemNr, Object obj, String name, String content) {
    try {
      Method subMethod = obj.getClass().getMethod(toSet(name), String.class);
      subMethod.invoke(obj, content);
    } catch (Exception e) {
      // Dont catch
      if ((elemNr != 5) && (name != null)) {
        String catNrString = pad_left(astr(catNr), " ", 2);
        String elemNrString = pad_left(astr(elemNr), " ", 4);
        log.debug("addReplacement (" + catNrString + ", " + elemNrString + ", \"" + name + "\");");
      }
    }
  }
}
