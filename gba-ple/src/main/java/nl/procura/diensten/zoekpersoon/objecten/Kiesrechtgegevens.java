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

package nl.procura.diensten.zoekpersoon.objecten;

public class Kiesrechtgegevens {

  private String aanduiding_uitsluiting_kiesrecht               = "";
  private String beschrijving_document                          = "";
  private String gemeente_document                              = "";
  private String datum_document                                 = "";
  private String einddatum_uitsluiting_europees_kiesrecht       = "";
  private String datum_verzoek_of_mededeling_europees_kiesrecht = "";
  private String aanduiding_europees_kiesrecht                  = "";
  private String einddatum_uitsluiting_kiesrecht                = "";
  private String datum_geldigheid                               = "";

  public Kiesrechtgegevens() {
  }

  public String getAanduiding_europees_kiesrecht() {
    return aanduiding_europees_kiesrecht;
  }

  public void setAanduiding_europees_kiesrecht(String aanduiding_europees_kiesrecht) {
    this.aanduiding_europees_kiesrecht = aanduiding_europees_kiesrecht;
  }

  public String getAanduiding_uitsluiting_kiesrecht() {
    return aanduiding_uitsluiting_kiesrecht;
  }

  public void setAanduiding_uitsluiting_kiesrecht(String aanduiding_uitsluiting_kiesrecht) {
    this.aanduiding_uitsluiting_kiesrecht = aanduiding_uitsluiting_kiesrecht;
  }

  public String getBeschrijving_document() {
    return beschrijving_document;
  }

  public void setBeschrijving_document(String beschrijving_document) {
    this.beschrijving_document = beschrijving_document;
  }

  public String getDatum_document() {
    return datum_document;
  }

  public void setDatum_document(String datum_document) {
    this.datum_document = datum_document;
  }

  public String getDatum_verzoek_of_mededeling_europees_kiesrecht() {
    return datum_verzoek_of_mededeling_europees_kiesrecht;
  }

  public void setDatum_verzoek_of_mededeling_europees_kiesrecht(
      String datum_verzoek_of_mededeling_europees_kiesrecht) {
    this.datum_verzoek_of_mededeling_europees_kiesrecht = datum_verzoek_of_mededeling_europees_kiesrecht;
  }

  public String getEinddatum_uitsluiting_europees_kiesrecht() {
    return einddatum_uitsluiting_europees_kiesrecht;
  }

  public void setEinddatum_uitsluiting_europees_kiesrecht(String einddatum_uitsluiting_europees_kiesrecht) {
    this.einddatum_uitsluiting_europees_kiesrecht = einddatum_uitsluiting_europees_kiesrecht;
  }

  public String getEinddatum_uitsluiting_kiesrecht() {
    return einddatum_uitsluiting_kiesrecht;
  }

  public void setEinddatum_uitsluiting_kiesrecht(String einddatum_uitsluiting_kiesrecht) {
    this.einddatum_uitsluiting_kiesrecht = einddatum_uitsluiting_kiesrecht;
  }

  public String getGemeente_document() {
    return gemeente_document;
  }

  public void setGemeente_document(String gemeente_documen) {
    this.gemeente_document = gemeente_documen;
  }

  public String getDatum_geldigheid() {
    return datum_geldigheid;
  }

  public void setDatum_geldigheid(String datum_geldigheid) {
    this.datum_geldigheid = datum_geldigheid;
  }
}
