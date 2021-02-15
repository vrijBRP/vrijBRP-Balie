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

package nl.procura.diensten.gba.wk.baseWK;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.procura.diensten.gba.ple.procura.utils.PLESerializer;
import nl.procura.diensten.gba.wk.baseWK.converters.BaseWK2Adres;
import nl.procura.diensten.woningkaart.objecten.Adres;

public class BaseWKBuilder {

  private final static Logger LOGGER        = LoggerFactory.getLogger(BaseWKBuilder.class.getName());
  private WkSearchResult      zoekResultaat = new WkSearchResult();

  public BaseWK addNewWk() {
    BaseWK wk = new BaseWK();
    getZoekResultaat().getBaseWKs().add(wk);
    return wk;
  }

  public BaseWK getCurrentWK() {
    return getZoekResultaat().getBaseWKs().getLast().orElseThrow(() -> new RuntimeException("No record found"));
  }

  public void serialize(OutputStream os) {
    PLESerializer.serialize(os, getZoekResultaat());
  }

  public WkSearchResult deserialize(InputStream is) {
    setZoekResultaat(PLESerializer.deSerialize(is, WkSearchResult.class));
    return getZoekResultaat();
  }

  public List<Adres> toAdressen() {
    return new BaseWK2Adres().convertToAdressen(getZoekResultaat().getBaseWKs());
  }

  public WkSearchResult getZoekResultaat() {
    return zoekResultaat;
  }

  public void setZoekResultaat(WkSearchResult zoekResultaat) {
    this.zoekResultaat = zoekResultaat;
  }
}
