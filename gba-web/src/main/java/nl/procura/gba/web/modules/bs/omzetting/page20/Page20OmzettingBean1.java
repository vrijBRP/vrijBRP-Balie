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

package nl.procura.gba.web.modules.bs.omzetting.page20;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.util.Date;

import nl.procura.gba.web.components.containers.SoortVerbintenisContainer;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.fields.TimeField;
import nl.procura.gba.web.services.bs.algemeen.enums.SoortVerbintenis;
import nl.procura.gba.web.services.bs.omzetting.StatusVerbintenis;
import nl.procura.vaadin.annotation.field.*;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.component.field.ProDateField;
import nl.procura.vaadin.component.field.fieldvalues.TimeFieldValue;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page20OmzettingBean1 implements Serializable {

  public static final String SOORT                = "soort";
  public static final String DATUMVERBINTENIS     = "datumVerbintenis";
  public static final String LOCATIEVERBINTENIS   = "locatieVerbintenis";
  public static final String STATUSVERBINTENIS    = "statusVerbintenis";
  public static final String DATUMSTATUSTOT       = "datumStatusTot";
  public static final String TIJDVERBINTENIS      = "tijdVerbintenis";
  public static final String TOELICHTINGCEREMONIE = "toelichtingCeremonie";

  @Field(type = FieldType.TEXT_AREA,
      caption = "Toelichting")
  @TextArea(rows = 5,
      columns = 50)
  private String toelichtingCeremonie = "";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Soort",
      required = true)
  @Select(containerDataSource = SoortVerbintenisContainer.class)
  private SoortVerbintenis soort = SoortVerbintenis.HUWELIJK;

  @Field(customTypeClass = ProDateField.class,
      caption = "Datum / tijd verbintenis",
      width = "97px",
      description = "Datum verbintenis")
  private Date datumVerbintenis = null;

  @Field(type = FieldType.LABEL,
      caption = "Locatie")
  private String locatieVerbintenis = "(Geen locatie geselecteerd)";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Status")
  @Select(containerDataSource = StatusVerbintenisContainer.class)
  @Immediate
  private StatusVerbintenis statusVerbintenis = null;

  @Field(customTypeClass = TimeField.class,
      caption = "Tijdstip",
      width = "45px")
  @TextField(maxLength = 5)
  @Immediate
  private TimeFieldValue tijdVerbintenis = new TimeFieldValue();

  @Field(customTypeClass = ProDateField.class,
      caption = "Einddatum status",
      width = "97px",
      description = "Einddatum status",
      required = true)
  private Date datumStatusTot = null;

  @Field(type = FieldType.TEXT_FIELD,
      caption = "1e keuze",
      width = "300px")
  private String ambtenaar1 = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "2e keuze",
      width = "300px")
  private String ambtenaar2 = "";

  public String getAmbtenaar1() {
    return ambtenaar1;
  }

  public void setAmbtenaar1(String ambtenaar1) {
    this.ambtenaar1 = ambtenaar1;
  }

  public String getAmbtenaar2() {
    return ambtenaar2;
  }

  public void setAmbtenaar2(String ambtenaar2) {
    this.ambtenaar2 = ambtenaar2;
  }

  public Date getDatumStatusTot() {
    return datumStatusTot;
  }

  public void setDatumStatusTot(Date datumStatusTot) {
    this.datumStatusTot = datumStatusTot;
  }

  public Date getDatumVerbintenis() {
    return datumVerbintenis;
  }

  public void setDatumVerbintenis(Date datumVerbintenis) {
    this.datumVerbintenis = datumVerbintenis;
  }

  public String getLocatieVerbintenis() {
    return locatieVerbintenis;
  }

  public void setLocatieVerbintenis(String locatieVerbintenis) {
    this.locatieVerbintenis = locatieVerbintenis;
  }

  public SoortVerbintenis getSoort() {
    return soort;
  }

  public void setSoort(SoortVerbintenis soort) {
    this.soort = soort;
  }

  public StatusVerbintenis getStatusVerbintenis() {
    return statusVerbintenis;
  }

  public void setStatusVerbintenis(StatusVerbintenis statusVerbintenis) {
    this.statusVerbintenis = statusVerbintenis;
  }

  public TimeFieldValue getTijdVerbintenis() {
    return tijdVerbintenis;
  }

  public void setTijdVerbintenis(TimeFieldValue tijdVerbintenis) {
    this.tijdVerbintenis = tijdVerbintenis;
  }

  public String getToelichtingCeremonie() {
    return toelichtingCeremonie;
  }

  public void setToelichtingCeremonie(String toelichtingCeremonie) {
    this.toelichtingCeremonie = toelichtingCeremonie;
  }
}
