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

package nl.procura.gba.web.services.zaken.verhuizing;

import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.toBigDecimal;

import java.io.Serializable;

import nl.procura.diensten.gba.ple.openoffice.formats.Adresformats;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class VerhuisAanvraagAdres implements Serializable {

  private static final long serialVersionUID = -8574356803611707331L;

  private boolean      nieuwAdres     = false;
  private long         hnr            = 0;
  private String       hnrL           = "";
  private String       hnrT           = "";
  private String       hnrA           = "";
  private FieldValue   straat         = new FieldValue();
  private FieldValue   pc             = new FieldValue();
  private FieldValue   locatie        = new FieldValue();
  private FieldValue   gemeenteDeel   = new FieldValue();
  private FieldValue   gemeente       = new FieldValue();
  private FieldValue   woonplaats     = new FieldValue();
  private FunctieAdres functieAdres   = FunctieAdres.ONBEKEND;
  private int          aantalPersonen = 0;

  private VerhuisAanvraag aanvraag;

  public VerhuisAanvraagAdres(VerhuisAanvraag aanvraag, boolean nieuwAdres) {

    setAanvraag(aanvraag);
    setNieuwAdres(nieuwAdres);
  }

  public int getAantalPersonen() {
    return aantalPersonen;
  }

  public void setAantalPersonen(int aantalPersonen) {

    if (isNieuwAdres()) {
      getAanvraag().setAantPers(toBigDecimal(aantalPersonen));
    }

    this.aantalPersonen = aantalPersonen;
  }

  public VerhuisAanvraag getAanvraag() {
    return aanvraag;
  }

  public void setAanvraag(VerhuisAanvraag aanvraag) {
    this.aanvraag = aanvraag;
  }

  public Adresformats getAdres() {

    Adresformats f = new Adresformats();
    f.setValues(getStraat().getDescription(), astr(getHnr()), getHnrL(), getHnrT(), getHnrA(),
        getLocatie().getDescription(), getPc().getDescription(), getGemeenteDeel().getDescription(),
        getWoonplaats().getDescription(), getGemeente().getDescription(), "", "", "", "", "", "");

    return f;
  }

  public FunctieAdres getFunctieAdres() {
    return functieAdres;
  }

  public void setFunctieAdres(FunctieAdres functieAdres) {

    if (isNieuwAdres()) {
      getAanvraag().setFuncAdr(functieAdres.getCode());
    }

    this.functieAdres = functieAdres;
  }

  public FieldValue getGemeente() {
    return gemeente;
  }

  public void setGemeente(FieldValue gemeente) {

    this.gemeente = FieldValue.from(gemeente);

    if (!isNieuwAdres()) {
      getAanvraag().setCGemHerkomst(this.gemeente.getBigDecimalValue());
    }
  }

  public FieldValue getGemeenteDeel() {
    return gemeenteDeel;
  }

  public void setGemeenteDeel(FieldValue gemeenteDeel) {

    this.gemeenteDeel = FieldValue.from(gemeenteDeel);

    if (isNieuwAdres()) {
      getAanvraag().setCGemDeel(this.gemeenteDeel.getBigDecimalValue());
      getAanvraag().setGemeentedeel(this.gemeenteDeel.getDescription());
    }
  }

  public long getHnr() {
    return hnr;
  }

  public void setHnr(long hnr) {

    if (isNieuwAdres()) {
      getAanvraag().setHnr(astr(hnr));
    }

    this.hnr = hnr;
  }

  public String getHnrA() {
    return hnrA;
  }

  public void setHnrA(String hnrA) {

    if (isNieuwAdres()) {
      getAanvraag().setHnrA(hnrA);
    }

    this.hnrA = hnrA;
  }

  public String getHnrL() {
    return hnrL;
  }

  public void setHnrL(String hnrL) {

    if (isNieuwAdres()) {
      getAanvraag().setHnrL(hnrL);
    }

    this.hnrL = hnrL;
  }

  public String getHnrT() {
    return hnrT;
  }

  public void setHnrT(String hnrT) {

    if (isNieuwAdres()) {
      getAanvraag().setHnrT(hnrT);
    }

    this.hnrT = hnrT;
  }

  public FieldValue getLocatie() {
    return locatie;
  }

  public void setLocatie(FieldValue locatie) {

    this.locatie = FieldValue.from(locatie);

    if (isNieuwAdres()) {
      getAanvraag().setCLocatie(this.locatie.getBigDecimalValue());
      getAanvraag().setLocatie(this.locatie.getDescription());
    }
  }

  public FieldValue getPc() {
    return pc;
  }

  public void setPc(FieldValue pc) {

    this.pc = FieldValue.from(pc);

    if (isNieuwAdres()) {
      getAanvraag().setPc(this.pc.getStringValue());
    }

  }

  public FieldValue getStraat() {
    return straat;
  }

  public void setStraat(FieldValue straat) {

    this.straat = FieldValue.from(straat);

    if (isNieuwAdres()) {
      getAanvraag().setCStraat(this.straat.getBigDecimalValue());
      getAanvraag().setStraat(this.straat.getDescription());
    }
  }

  public FieldValue getWoonplaats() {
    return woonplaats;
  }

  public void setWoonplaats(FieldValue woonplaats) {
    this.woonplaats = FieldValue.from(woonplaats);
    if (isNieuwAdres()) {
      getAanvraag().setWoonplaats(astr(this.woonplaats.getDescription()));
    }
  }

  public boolean isNieuwAdres() {
    return nieuwAdres;
  }

  public void setNieuwAdres(boolean nieuwAdres) {
    this.nieuwAdres = nieuwAdres;
  }

  public String toString() {

    return "VerhuisAanvraagAdresImpl [straat=" + straat + ", hnr=" + hnr + ", hnrL=" + hnrL + ", hnrT=" + hnrT
        + ", hnrA=" + hnrA + ", pc=" + pc + ", locatie=" + locatie + ", gemeenteDeel=" + gemeenteDeel + ", gemeente="
        + gemeente + ", functieAdres=" + functieAdres + "]";
  }
}
