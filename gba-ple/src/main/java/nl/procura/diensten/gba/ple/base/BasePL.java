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

import static nl.procura.burgerzaken.gba.core.enums.GBACat.UNKNOWN;
import static nl.procura.standard.Globalfunctions.aval;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.diensten.gba.ple.procura.arguments.PLEDatasource;

public class BasePL implements Serializable {

  private static final long serialVersionUID = -5022581846086417210L;

  private BasePLList<BasePLCat> cats     = new BasePLList<>();
  private Map<String, Object>   metaInfo = new HashMap<>();

  public BasePL() {
  }

  public BasePLRec getCurrentRec(GBACat categoryType) {
    return getCat(categoryType).getCurrentRec();
  }

  public BasePLRec getLatestRec(GBACat categoryType) {
    return getCat(categoryType).getLatestRec();
  }

  public BasePLCat getCat(GBACat categoryType) {
    return cats.stream().filter(type -> type.getCatType() == categoryType)
        .findFirst().orElse(new BasePLCat(UNKNOWN));
  }

  public List<BasePLCat> getCats(GBACat... types) {
    List<BasePLCat> typesList = new ArrayList<>();
    for (BasePLCat cat : getCats()) {
      for (GBACat catType : types) {
        if (cat.getCatType() == catType) {
          typesList.add(cat);
        }
      }
    }

    return typesList;
  }

  public Map<String, Object> getMetaInfo() {
    return metaInfo;
  }

  public void setMetaInfo(Map<String, Object> metaInfo) {
    this.metaInfo = metaInfo;
  }

  public BasePLList<BasePLCat> getCats() {
    return cats;
  }

  public void setCats(BasePLList<BasePLCat> cats) {
    this.cats = cats;
  }

  public PLEDatasource getDatasource() {
    return PLEDatasource.get(aval(getMetaInfo().get("databron")));
  }
}
