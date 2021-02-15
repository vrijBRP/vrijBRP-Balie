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

package nl.procura.gba.web.modules.zaken.tmv.page4;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.services.zaken.tmv.TmvResultaat;
import nl.procura.gba.web.services.zaken.tmv.TmvStatus;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page4TmvBean4 implements Serializable {

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Datum aanleg dossier")
  private String datumAanlegDossier = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Datum wijziging dossier")
  private String datumWijzigingDossier = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Behandelende gemeente")
  private String behandelendeGemeente = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Datum verwachte afhandeling")
  private String datumVerwachteAfhandeling = "";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Status dossier")
  private String statusDossier = TmvStatus.ONBEKEND.toString();

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Resultaatcode onderzoek")
  private String resultaatcodeOnderzoek = TmvResultaat.ONBEKEND.toString();

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Toelichting resultaat")

  private String toelichtingResultaat = "";

  public String getBehandelendeGemeente() {
    return behandelendeGemeente;
  }

  public void setBehandelendeGemeente(String behandelendeGemeente) {
    this.behandelendeGemeente = behandelendeGemeente;
  }

  public String getDatumAanlegDossier() {
    return datumAanlegDossier;
  }

  public void setDatumAanlegDossier(String datumAanlegDossier) {
    this.datumAanlegDossier = datumAanlegDossier;
  }

  public String getDatumVerwachteAfhandeling() {
    return datumVerwachteAfhandeling;
  }

  public void setDatumVerwachteAfhandeling(String datumVerwachteAfhandeling) {
    this.datumVerwachteAfhandeling = datumVerwachteAfhandeling;
  }

  public String getDatumWijzigingDossier() {
    return datumWijzigingDossier;
  }

  public void setDatumWijzigingDossier(String datumWijzigingDossier) {
    this.datumWijzigingDossier = datumWijzigingDossier;
  }

  public String getResultaatcodeOnderzoek() {
    return resultaatcodeOnderzoek;
  }

  public void setResultaatcodeOnderzoek(String resultaatcodeOnderzoek) {
    this.resultaatcodeOnderzoek = resultaatcodeOnderzoek;
  }

  public String getStatusDossier() {
    return statusDossier;
  }

  public void setStatusDossier(String statusDossier) {
    this.statusDossier = statusDossier;
  }

  public String getToelichtingResultaat() {
    return toelichtingResultaat;
  }

  public void setToelichtingResultaat(String toelichtingResultaat) {
    this.toelichtingResultaat = toelichtingResultaat;
  }
}
