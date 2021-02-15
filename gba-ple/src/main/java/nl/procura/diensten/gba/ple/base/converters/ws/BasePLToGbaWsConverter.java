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

package nl.procura.diensten.gba.ple.base.converters.ws;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.BooleanUtils;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.burgerzaken.gba.core.enums.GBARecStatus;
import nl.procura.diensten.gba.ple.base.*;
import nl.procura.gbaws.web.rest.v2.personlists.*;

public class BasePLToGbaWsConverter {

  public static GbaWsPersonListResponse toGbaWsPersonListResponse(PLEResult result) {
    GbaWsPersonListResponse response = new GbaWsPersonListResponse();
    response.setPersonlists(collect(result.getBasePLs(), BasePLToGbaWsConverter::toPl));
    response.setErrors(collect(result.getMessages(), BasePLToGbaWsConverter::toMessage));
    return response;
  }

  private static String toMessage(PLEMessage message) {
    return message.getCode() + ": " + message.getDescr();
  }

  private static GbaWsPersonList toPl(BasePL basePL) {
    return new GbaWsPersonList()
        .setCats(collect(basePL.getCats(), BasePLToGbaWsConverter::toCat));
  }

  private static GbaWsPersonListCat toCat(BasePLCat basePLCat) {
    return new GbaWsPersonListCat()
        .setCode(basePLCat.getCatType().getCode())
        .setDescr(basePLCat.getCatType().getDescr())
        .setSets(collect(basePLCat.getSets(), BasePLToGbaWsConverter::addSet));
  }

  private static GbaWsPersonListSet addSet(BasePLSet basePLSet) {
    return new GbaWsPersonListSet()
        .setIndex(basePLSet.getExtIndex())
        .setInternalIndex(basePLSet.getIntIndex())
        .setMostRecentMarriage(basePLSet.getCatType() == GBACat.HUW_GPS ? basePLSet.isMostRecentSet() : null)
        .setRecords(collect(basePLSet.getRecs(), BasePLToGbaWsConverter::toRec));
  }

  private static GbaWsPersonListRec toRec(BasePLRec basePLRec) {
    return new GbaWsPersonListRec()
        .setIndex(basePLRec.getIndex())
        .setStatus(new GbaWsPersonListRecStatus()
            .setCode(basePLRec.getStatus().getCode())
            .setDescr(basePLRec.getStatus().getDescr()))
        .setIncorrect(basePLRec.isIncorrect())
        .setBagChange(basePLRec.isBagChange())
        .setAdminHist(basePLRec.isAdmHistory())
        .setConflicting(basePLRec.isConflicting())
        .setElems(collect(sort(basePLRec.getElems()), BasePLToGbaWsConverter::toElem));
  }

  private static List<BasePLElem> sort(BasePLList<BasePLElem> elems) {
    return elems.stream()
        .sorted(Comparator.comparingInt(BasePLElem::getElemCode))
        .collect(Collectors.toList());
  }

  private static GbaWsPersonListElem toElem(BasePLElem basePLElem) {
    GBAElem gbaElem = GBAElem.getByCode(basePLElem.getElemCode());
    GbaWsPersonListElem elem = new GbaWsPersonListElem()
        .setDescr(gbaElem.getDescr())
        .setNational(gbaElem.isNational())
        .setAdmin(gbaElem.isAdmin())
        .setTech(gbaElem.isTech())
        .setAllowed(basePLElem.isAllowed())
        .setCode(basePLElem.getElemCode())
        .setValue(new GbaWsPersonListVal()
            .setCode(basePLElem.getValue().getCode())
            .setVal(basePLElem.getValue().getVal())
            .setDescr(basePLElem.getValue().getDescr()));

    if (gbaElem.getTable() != null && gbaElem.getTable().getTableCode() > 0) {
      elem.setTable(new GbaWsPersonListRecTable()
          .setCode(gbaElem.getTable().getTableCode())
          .setDescr(gbaElem.getTable().getDescr()));
    }
    return elem;

  }

  private static <T, R> List<R> collect(List<T> basePLs, Function<T, R> mapper) {
    return basePLs.stream().map(mapper).collect(Collectors.toList());
  }

  public static PLEResult toPLEResult(GbaWsPersonListResponse dtoResult) {
    PLEResult result = new PLEResult();
    for (String msg : dtoResult.getErrors()) {
      String[] splits = msg.split(":");
      if (splits.length == 2) {
        result.getMessages().add(new PLEMessage(Integer.parseInt(splits[0].trim()), splits[1].trim()));
      }
    }

    for (GbaWsPersonList wsPl : dtoResult.getPersonlists()) {
      BasePL basePL = new BasePL();
      result.getBasePLs().add(basePL);

      for (GbaWsPersonListCat wsCat : wsPl.getCats()) {
        GBACat catType = GBACat.getByCode(wsCat.getCode());
        BasePLCat cat = new BasePLCat(catType);
        basePL.getCats().add(cat);

        for (GbaWsPersonListSet wsSet : wsCat.getSets()) {
          BasePLSet set = new BasePLSet(catType, wsSet.getIndex());
          set.setMostRecentSet(BooleanUtils.isTrue(wsSet.getMostRecentMarriage()));
          set.setIntIndex(wsSet.getInternalIndex());
          cat.getSets().add(set);

          for (GbaWsPersonListRec wsRec : wsSet.getRecords()) {
            BasePLRec rec = new BasePLRec(catType, getStatus(wsRec.getStatus()));
            rec.setIndex(wsRec.getIndex());
            set.getRecs().add(rec);

            for (GbaWsPersonListElem wsElem : wsRec.getElems()) {
              BasePLElem elem = new BasePLElem();
              elem.setCatCode(catType.getCode());
              elem.setElemCode(wsElem.getCode());
              elem.setAllowed(wsElem.isAllowed());
              rec.getElems().add(elem);

              BasePLValue value = new BasePLValue();
              GbaWsPersonListVal wsVal = wsElem.getValue();
              value.setCode(wsVal.getCode());
              value.setVal(wsVal.getVal());
              value.setDescr(wsVal.getDescr());
              elem.setValue(value);
            }
          }
        }
      }
    }

    return result;
  }

  private static GBARecStatus getStatus(GbaWsPersonListRecStatus status) {
    return Arrays.stream(GBARecStatus.values())
        .filter(s -> s.getCode() == status.getCode())
        .findFirst().orElseThrow(
            () -> new IllegalArgumentException("Unknown status: "
                + status.getCode() + " - " + status.getDescr()));
  }
}
