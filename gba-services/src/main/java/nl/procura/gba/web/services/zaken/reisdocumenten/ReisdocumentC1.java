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

import nl.procura.diensten.gba.ple.openoffice.DocumentPL;

public class ReisdocumentC1 {

  private String               datumEindeGeldig           = "";
  private String               datumVerstrekt             = "";
  private String               documentNummer             = "";
  private String               documentSoort              = "";
  private boolean              documentSoortVreemdelingen = false;
  private boolean              documentSoortVluchtelingen = false;
  private String               gemeenteInschrijving       = "";
  private String               landImmigratie             = "";
  private String               lengte                     = "";
  private String               vbt                        = "";
  private String               verstrektDoor              = "";   // Autoriteit
  private String               datumVerklaring            = "";
  private ReisdocumentAanvraag aanvraag;

  private DocumentPL persoon = null;

  public String getDatumEindeGeldig() {
    return datumEindeGeldig;
  }

  public void setDatumEindeGeldig(String datumEindeGeldig) {
    this.datumEindeGeldig = datumEindeGeldig;
  }

  public String getDatumVerklaring() {
    return datumVerklaring;
  }

  public void setDatumVerklaring(String datumVerklaring) {
    this.datumVerklaring = datumVerklaring;
  }

  public String getDatumVerstrekt() {
    return datumVerstrekt;
  }

  public void setDatumVerstrekt(String datumVerstrekt) {
    this.datumVerstrekt = datumVerstrekt;
  }

  public String getDocumentNummer() {
    return documentNummer;
  }

  public void setDocumentNummer(String documentNummer) {
    this.documentNummer = documentNummer;
  }

  public String getDocumentSoort() {
    return documentSoort;
  }

  public void setDocumentSoort(String documentSoort) {
    this.documentSoort = documentSoort;
  }

  public String getGemeenteInschrijving() {
    return gemeenteInschrijving;
  }

  public void setGemeenteInschrijving(String gemeenteInschrijving) {
    this.gemeenteInschrijving = gemeenteInschrijving;
  }

  public String getLandImmigratie() {
    return landImmigratie;
  }

  public void setLandImmigratie(String landImmigratie) {
    this.landImmigratie = landImmigratie;
  }

  public String getLengte() {
    return lengte;
  }

  public void setLengte(String lengte) {
    this.lengte = lengte;
  }

  public DocumentPL getPersoon() {
    return persoon;
  }

  public void setPersoon(DocumentPL persoon) {
    this.persoon = persoon;
  }

  public String getVbt() {
    return vbt;
  }

  public void setVbt(String vbt) {
    this.vbt = vbt;
  }

  public String getVerstrektDoor() {
    return verstrektDoor;
  }

  public void setVerstrektDoor(String verstrektDoor) {
    this.verstrektDoor = verstrektDoor;
  }

  public boolean isDocumentSoortVluchtelingen() {
    return documentSoortVluchtelingen;
  }

  public void setDocumentSoortVluchtelingen(boolean documentSoortVluchtelingen) {
    this.documentSoortVluchtelingen = documentSoortVluchtelingen;
  }

  public boolean isDocumentSoortVreemdelingen() {
    return documentSoortVreemdelingen;
  }

  public void setDocumentSoortVreemdelingen(boolean documentSoortVreemdelingen) {
    this.documentSoortVreemdelingen = documentSoortVreemdelingen;
  }

  public ReisdocumentAanvraag getAanvraag() {
    return aanvraag;
  }

  public void setAanvraag(ReisdocumentAanvraag aanvraag) {
    this.aanvraag = aanvraag;
  }
}
