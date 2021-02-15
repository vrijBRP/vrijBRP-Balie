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

package nl.procura.gba.web.services.gba.presentievraag;

import static nl.procura.standard.Globalfunctions.pos;

public class PresentievraagZoekargumenten {

  private long   datumInvoerVanaf = -1;
  private long   datumInvoerTm    = -1;
  private String inhoudBericht    = "";

  public long getDatumInvoerTm() {
    return datumInvoerTm;
  }

  public void setDatumInvoerTm(long dInvoerTm) {
    this.datumInvoerTm = dInvoerTm;
  }

  public long getDatumInvoerVanaf() {
    return datumInvoerVanaf;
  }

  public void setDatumInvoerVanaf(long dInvoerVanaf) {
    this.datumInvoerVanaf = dInvoerVanaf;
  }

  public String getInhoudBericht() {
    return inhoudBericht;
  }

  public void setInhoudBericht(String inhoudBericht) {
    this.inhoudBericht = inhoudBericht;
  }

  public boolean isGevuld() {
    return pos(datumInvoerVanaf) && pos(datumInvoerTm);
  }
}
