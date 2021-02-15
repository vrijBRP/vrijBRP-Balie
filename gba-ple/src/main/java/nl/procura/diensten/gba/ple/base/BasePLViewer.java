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

import java.util.Collections;
import java.util.List;

import nl.procura.burgerzaken.gba.core.enums.GBAGroupElements;

public class BasePLViewer {

  public static void view(List<BasePL> basisPersoonslijsten) {

    for (BasePL pl : basisPersoonslijsten) {
      System.out.println("Nieuwe persoonlijst");

      for (BasePLCat cat : pl.getCats()) {
        System.out.println(String.format("%2s%s", " ", "Categorie: " + cat.getCatType()));

        for (BasePLSet set : cat.getSets()) {
          if (set.getExtIndex() == set.getIntIndex()) {
            System.out.println(String.format("%4s%s", " ", "Set: vnr: " + set.getExtIndex() +
                ", records: " + set.getRecs().size()));
          } else {
            System.out.println(String.format("%4s%s", " ", "Set: vnr: " + set.getExtIndex() + " (internal: " +
                set.getIntIndex() + "), records: " + set.getRecs().size()));
          }

          for (BasePLRec rec : set.getRecs()) {
            System.out.println(String.format("%6s%s", " ",
                "Record: " + rec.getIndex() + " (" + rec.getStatus() + ")"));

            Collections.sort(rec.getElems());

            for (BasePLElem e : rec.getElems()) {
              BasePLValue value = e.getValue();

              String sVal = value.getVal();
              String sDescr = value.getDescr();

              if (sVal.isEmpty()) {
                sVal = "(geen waarde)";
              }

              if (sDescr.isEmpty()) {
                sDescr = "(geen omschrijving)";
              }

              GBAGroupElements.GBAGroupElem pleType = GBAGroupElements.getByCat(rec.getCatType().getCode(),
                  e.getElemCode());
              String descr = "code :" + rec.getCatType().getCode() + " Onbekend (" + e.getElemCode() + ")";

              if (pleType != null) {
                descr = pleType.getElem().getDescr();
              }
              if (descr.length() > 25) {
                descr = descr.substring(0, 22) + "...";
              }

              System.out.println(String.format("%8sElement: %4s %-25s%s", " ", e.getElemCode(), descr,
                  " = " + sVal + "(" + sDescr + ")"));
            }
          }
        }
      }
    }
  }
}
