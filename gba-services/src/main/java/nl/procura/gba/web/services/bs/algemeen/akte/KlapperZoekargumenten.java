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

package nl.procura.gba.web.services.bs.algemeen.akte;

import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.pos;

import java.util.HashSet;
import java.util.Set;

public class KlapperZoekargumenten {

  private long                          datumVanaf    = 0;
  private long                          datumTm       = 0;
  private long                          jaarVan       = -1;
  private long                          jaarTm        = -1;
  private long                          datum         = -1;
  private long                          nummer        = -1;
  private DossierAkteInvoerType         invoerType    = null;
  private Set<DossierAkteRegistersoort> soorten       = new HashSet<>();
  private DossierAkteDeel               deel          = null;
  private long                          bsn           = -1;
  private String                        geslachtsnaam = "";
  private KlapperVolgordeType           volgorde      = KlapperVolgordeType.DATUM_OPLOPEND;

  public long getBsn() {
    return bsn;
  }

  public void setBsn(long bsn) {
    this.bsn = bsn;
  }

  public long getDatum() {
    return datum;
  }

  public void setDatum(long datum) {
    this.datum = datum;
  }

  public long getDatumTm() {
    return datumTm;
  }

  public void setDatumTm(long datumTm) {
    this.datumTm = datumTm;
  }

  public long getDatumVanaf() {
    return datumVanaf;
  }

  public void setDatumVanaf(long datumVanaf) {
    this.datumVanaf = datumVanaf;
  }

  public DossierAkteDeel getDeel() {
    return deel;
  }

  public void setDeel(DossierAkteDeel deel) {
    this.deel = deel;
  }

  public String getGeslachtsnaam() {
    return geslachtsnaam;
  }

  public void setGeslachtsnaam(String geslachtsnaam) {
    this.geslachtsnaam = geslachtsnaam;
  }

  public DossierAkteInvoerType getInvoerType() {
    return invoerType;
  }

  public void setInvoerType(DossierAkteInvoerType invoerType) {
    this.invoerType = invoerType;
  }

  public long getJaarTm() {
    return jaarTm;
  }

  public void setJaarTm(long jaarTm) {
    this.jaarTm = jaarTm;
  }

  public long getJaarVan() {
    return jaarVan;
  }

  public void setJaarVan(long jaarVan) {
    this.jaarVan = jaarVan;
  }

  public long getNummer() {
    return nummer;
  }

  public void setNummer(long nummer) {
    this.nummer = nummer;
  }

  public String getPeriodes() {

    long va = getJaarVan();
    long tm = getJaarTm();

    return va == tm ? astr(va) : (va + " - " + tm);
  }

  public Set<DossierAkteRegistersoort> getSoorten() {
    return soorten;
  }

  public void setSoorten(Set<DossierAkteRegistersoort> soorten) {
    this.soorten = soorten;
  }

  public KlapperVolgordeType getVolgorde() {
    return volgorde;
  }

  public void setVolgorde(KlapperVolgordeType volgorde) {
    this.volgorde = volgorde;
  }

  public boolean heeftPeriodes() {
    return pos(getJaarVan()) || pos(getJaarTm());
  }
}
