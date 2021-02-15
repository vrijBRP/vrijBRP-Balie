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

package nl.procura.gba.web.services.zaken.reisdocumenten.clausule;

import static nl.procura.standard.Globalfunctions.*;

import java.io.Serializable;

import nl.procura.diensten.gba.ple.openoffice.DocumentPL;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentAanvraag;

public class Clausules implements Serializable {

  private static final long serialVersionUID = -7142230649339666707L;

  private DocumentPL           partner = null;
  private ReisdocumentAanvraag aanvraag;

  public Clausules(ReisdocumentAanvraag aanvraag) {
    this.setAanvraag(aanvraag);
  }

  public ReisdocumentAanvraag getAanvraag() {
    return aanvraag;
  }

  public void setAanvraag(ReisdocumentAanvraag aanvraag) {
    this.aanvraag = aanvraag;
  }

  public String getGeldigVoorReizen() {
    return getAanvraag().getSClXb();
  }

  public void setGeldigVoorReizen(String geldigVoorReizen) {
    getAanvraag().setSClXb(geldigVoorReizen);
  }

  public TekenenType getOndertekening() {
    return TekenenType.get(aval(getAanvraag().getClV()));
  }

  public void setOndertekening(TekenenType t) {
    getAanvraag().setClV(toBigDecimal(t.getCode()));
  }

  public DocumentPL getPartner() {
    return partner;
  }

  public void setPartner(DocumentPL partner) {
    this.partner = partner;
  }

  public String getPseudoniem() {
    return getAanvraag().getSClIv();
  }

  public void setPseudoniem(String pseudoniem) {
    getAanvraag().setSClIv(pseudoniem);
  }

  public StaatloosType getStaatloos() {
    return StaatloosType.get(aval(getAanvraag().getClXib()));
  }

  public void setStaatloos(StaatloosType t) {
    getAanvraag().setClXib(toBigDecimal(t.getCode()));
  }

  public String getTvv() {
    return getAanvraag().getClXii();
  }

  public void setTvv(String tvv) {
    getAanvraag().setClXii(tvv);
  }

  public String getUitzonderingLanden() {
    return getAanvraag().getSClXa();
  }

  public void setUitzonderingLanden(String uitzonderingLanden) {
    getAanvraag().setSClXa(uitzonderingLanden);
  }

  public VermeldPartnerType getVermeldingPartner() {
    return VermeldPartnerType.get(getAanvraag().getClI());
  }

  public void setVermeldingPartner(VermeldPartnerType vermeldingPartner) {
    getAanvraag().setClI(vermeldingPartner.getCode());
  }

  public boolean isSprakeVanPartner() {
    return getPartner() != null;
  }

  public boolean isInBezitBuitenlandsDocument() {
    return pos(getAanvraag().getIndBezitBuitenlDoc());
  }

  public void setInBezigBuitenlandsDocument(boolean inBezitBuitenlandDoc) {
    getAanvraag().setIndBezitBuitenlDoc(toBigDecimal(inBezitBuitenlandDoc ? 1 : 0));
  }
}
