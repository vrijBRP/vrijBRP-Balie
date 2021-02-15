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

package nl.procura.diensten.gba.ple.base;

import static nl.procura.standard.Globalfunctions.emp;
import static nl.procura.standard.Globalfunctions.fil;

import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.burgerzaken.gba.core.tables.GBATableValues;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BasePLFormatter {

  public BasePLFormatter(PLEResult result) {
    for (BasePL pl : result.getBasePLs()) {
      for (BasePLCat cat : pl.getCats()) {
        for (BasePLSet set : cat.getSets()) {
          for (BasePLRec record : set.getRecs()) {
            for (BasePLElem element : record.getElems()) {
              setDescr(element, GBAElem.getByCode(element.getElemCode()));
            }
          }
        }
      }
    }
  }

  public abstract String getNationalValue(int elementCode, BasePLValue value);

  private void setDescr(BasePLElem element, GBAElem elementType) {
    String val = element.getValue().getVal();
    String descr = element.getValue().getDescr();
    if (fil(val) && emp(descr)) {
      descr = val;
      GBATableValues tableValues = elementType.getTable().getValues();
      if (tableValues.getValues().size() > 0) {
        descr = tableValues.get(val).getValue(); // Standaard waarden zetten

      } else if (elementType.getTable().isNational()) {
        descr = getNationalValue(element.getElemCode(), element.getValue()); // Zoeken in externe bron
      }
      element.getValue().setDescr(elementType.getVal().getFormat(descr)); // Formatteren
    }
  }
}
