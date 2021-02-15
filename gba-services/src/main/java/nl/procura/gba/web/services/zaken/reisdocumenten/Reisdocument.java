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

package nl.procura.gba.web.services.zaken.reisdocumenten;

import static nl.procura.standard.Globalfunctions.fil;
import static nl.procura.standard.Globalfunctions.pos;

import nl.procura.diensten.gba.ple.base.BasePLValue;

public class Reisdocument {

  private BasePLValue nummerDocument                = null;
  private BasePLValue nederlandsReisdocument        = null;
  private BasePLValue buitenlandsReisdocument       = null;
  private BasePLValue autoriteitVanAfgifte          = null;
  private BasePLValue datumEindeGeldigheid          = null;
  private BasePLValue signalering                   = null;
  private BasePLValue aanduidingInhoudingVermissing = null;
  private BasePLValue datumUitgifte                 = null;
  private BasePLValue datumInhoudingVermissing      = null;

  public BasePLValue getAanduidingInhoudingVermissing() {
    return aanduidingInhoudingVermissing;
  }

  public void setAanduidingInhoudingVermissing(BasePLValue aanduidingInhoudingVermissing) {
    this.aanduidingInhoudingVermissing = aanduidingInhoudingVermissing;
  }

  public BasePLValue getAutoriteitVanAfgifte() {
    return autoriteitVanAfgifte;
  }

  public void setAutoriteitVanAfgifte(BasePLValue autoriteitVanAfgifte) {
    this.autoriteitVanAfgifte = autoriteitVanAfgifte;
  }

  public BasePLValue getBuitenlandsReisdocument() {
    return buitenlandsReisdocument;
  }

  public void setBuitenlandsReisdocument(BasePLValue buitenlandsReisdocument) {
    this.buitenlandsReisdocument = buitenlandsReisdocument;
  }

  public BasePLValue getDatumEindeGeldigheid() {
    return datumEindeGeldigheid;
  }

  public void setDatumEindeGeldigheid(BasePLValue datumEindeGeldigheid) {
    this.datumEindeGeldigheid = datumEindeGeldigheid;
  }

  public BasePLValue getDatumInhoudingVermissing() {
    return datumInhoudingVermissing;
  }

  public void setDatumInhoudingVermissing(BasePLValue datumInhoudingVermissing) {
    this.datumInhoudingVermissing = datumInhoudingVermissing;
  }

  public BasePLValue getDatumUitgifte() {
    return datumUitgifte;
  }

  public void setDatumUitgifte(BasePLValue datumUitgifte) {
    this.datumUitgifte = datumUitgifte;
  }

  public String getDocumentNummerFormat() {

    boolean signalering = pos(getSignalering().getCode());
    boolean buitenlReisd = pos(getBuitenlandsReisdocument().getCode());

    if (signalering || buitenlReisd) {
      return "Onbekend nr.";
    }

    return getNummerDocument().getDescr();
  }

  public String getDocumentOmschrijvingFormat() {

    boolean signalering = pos(getSignalering().getCode());
    boolean buitenlReisd = pos(getBuitenlandsReisdocument().getCode());

    if (signalering) {
      return "Signalering";
    } else if (buitenlReisd) {
      return "Buitenlands document";
    }

    return getNederlandsReisdocument().getDescr();
  }

  public BasePLValue getNederlandsReisdocument() {
    return nederlandsReisdocument;
  }

  public void setNederlandsReisdocument(BasePLValue nederlandsReisdocument) {
    this.nederlandsReisdocument = nederlandsReisdocument;
  }

  public BasePLValue getNummerDocument() {
    return nummerDocument;
  }

  public void setNummerDocument(BasePLValue nummerDocument) {
    this.nummerDocument = nummerDocument;
  }

  public BasePLValue getSignalering() {
    return signalering;
  }

  public void setSignalering(BasePLValue signalering) {
    this.signalering = signalering;
  }

  public boolean isHeeftBuitenlandsReisdocument() {
    return fil(getBuitenlandsReisdocument().getVal());
  }

  public boolean isHeeftSignalering() {
    return fil(getSignalering().getVal());
  }

  public boolean isInBezit() {

    String dUit = getDatumUitgifte().getVal();
    String dInh = getDatumInhoudingVermissing().getVal();

    return (pos(dUit) && !pos(dInh));
  }

  /**
   * Alleen de documenten tonen die nog steeds relevant zijn
   */

  public boolean isTonen() {
    String type = getNederlandsReisdocument().getVal();
    return (fil(type) && !type.toUpperCase().matches("R1|R2|RD|RV|RM"));
  }
}
