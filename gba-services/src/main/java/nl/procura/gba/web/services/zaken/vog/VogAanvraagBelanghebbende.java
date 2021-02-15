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

package nl.procura.gba.web.services.zaken.vog;

import static nl.procura.standard.Globalfunctions.toBigDecimal;
import static nl.procura.standard.Globalfunctions.trim;

import java.io.Serializable;
import java.text.MessageFormat;

import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class VogAanvraagBelanghebbende implements Serializable {

  private static final long serialVersionUID = 6477994246105224704L;

  private String     naam              = "";
  private String     vertegenwoordiger = "";
  private String     straat            = "";
  private long       hnr               = -1;
  private String     hnrT              = "";
  private String     hnrL              = "";
  private FieldValue pc                = new FieldValue();
  private String     plaats            = "";
  private FieldValue land              = new FieldValue();
  private String     tel               = "";

  private VogAanvraag aanvraag;

  public VogAanvraagBelanghebbende(VogAanvraag aanvraag) {
    setAanvraag(aanvraag);
  }

  public VogAanvraag getAanvraag() {
    return aanvraag;
  }

  public void setAanvraag(VogAanvraag aanvraag) {
    this.aanvraag = aanvraag;
  }

  public String getAdres() {
    return trim(MessageFormat.format("{0} {1} {2} {3}", getStraat(), getHnr(), getHnrL(), getHnrT()));
  }

  public long getHnr() {
    return hnr;
  }

  public void setHnr(long hnr) {

    this.hnr = hnr;
    getAanvraag().setHnrB(toBigDecimal(hnr));
  }

  public String getHnrL() {
    return hnrL;
  }

  public void setHnrL(String hnrL) {

    this.hnrL = hnrL;
    getAanvraag().setHnrLB(hnrL);
  }

  public String getHnrT() {
    return hnrT;
  }

  public void setHnrT(String hnrT) {

    this.hnrT = hnrT;
    getAanvraag().setHnrTB(hnrT);
  }

  public FieldValue getLand() {
    return land;
  }

  public void setLand(FieldValue land) {
    this.land = FieldValue.from(land);
    getAanvraag().setCLandB(this.land.getBigDecimalValue());
  }

  public String getNaam() {
    return naam;
  }

  public void setNaam(String naam) {

    this.naam = naam;
    getAanvraag().setInstellingB(naam);
  }

  public FieldValue getPc() {
    return pc;
  }

  public void setPc(FieldValue pc) {
    this.pc = FieldValue.from(pc);
    getAanvraag().setPcB(this.pc.getStringValue());
  }

  public String getPlaats() {
    return plaats;
  }

  public void setPlaats(String plaats) {

    this.plaats = plaats;
    getAanvraag().setPlaatsB(plaats);
  }

  public String getStraat() {
    return straat;
  }

  public void setStraat(String straat) {

    this.straat = straat;
    getAanvraag().setStraatB(straat);
  }

  public String getTel() {
    return tel;
  }

  public void setTel(String tel) {

    this.tel = tel;
    getAanvraag().setTelB(tel);
  }

  public String getVertegenwoordiger() {
    return vertegenwoordiger;
  }

  public void setVertegenwoordiger(String vertegenwoordiger) {

    this.vertegenwoordiger = vertegenwoordiger;
    getAanvraag().setNaamB(vertegenwoordiger);
  }
}
