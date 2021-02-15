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

package nl.procura.diensten.gba.wk.extensions;

import java.util.ArrayList;
import java.util.List;

import nl.procura.diensten.gba.wk.baseWK.BaseWK;
import nl.procura.diensten.gba.wk.baseWK.WkSearchResult;

public class WKResultWrapper {

  private List<BaseWKExt> extensions = new ArrayList<>();
  private WkSearchResult  resultaat;

  public WKResultWrapper(WkSearchResult resultaat) {

    for (BaseWK wk : resultaat.getBaseWKs()) {
      BaseWKExt ext = new BaseWKExt(wk);
      getBasisWkWrappers().add(ext);
    }

    this.setResultaat(resultaat);
  }

  public WkSearchResult getResultaat() {
    return resultaat;
  }

  public void setResultaat(WkSearchResult resultaat) {
    this.resultaat = resultaat;
  }

  public List<BaseWKExt> getBasisWkWrappers() {
    return extensions;
  }

  public void setBasisWkWrappers(ArrayList<BaseWKExt> basisWkWrappers) {
    this.extensions = basisWkWrappers;
  }
}
