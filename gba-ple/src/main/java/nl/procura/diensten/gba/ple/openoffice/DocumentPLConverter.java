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

package nl.procura.diensten.gba.ple.openoffice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;

public class DocumentPLConverter {

  /**
   * Convert BasisPlWrapper to List of DocumentPersoonslijst
   */
  public static List<DocumentPL> convert(List<BasePLExt> pls, HashMap<String, BasePLExt> relatedPls) {
    List<DocumentPL> oopls = new ArrayList<>();
    if (pls != null && pls.size() > 0) {
      oopls.addAll(pls.stream()
          .map(pl -> new DocumentPL(pl, relatedPls))
          .collect(Collectors.toList()));
    }
    return oopls;
  }

  /**
   * Remove the stillborns
   */
  public static void removeStillborns(DocumentPL documentPL) {
    for (DocumentPL.OOKind kind : new ArrayList<>(documentPL.getKinderen())) {
      if (kind.isStillborn()) {
        documentPL.getKinderen().remove(kind);
      }
    }
  }
}
