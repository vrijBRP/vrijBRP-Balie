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

package nl.procura.gba.web.modules.bs.ontbinding.page30;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.util.Date;

import nl.procura.gba.web.components.containers.RechtbankContainer;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.services.bs.algemeen.enums.RechtbankLocatie;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Select;
import nl.procura.vaadin.component.container.NLBooleanContainer;
import nl.procura.vaadin.component.field.ProDateField;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page30OntbindingBean1 implements Serializable {

  public static final String UITSPRAAK                 = "uitspraak";
  public static final String DATUM_UITSPRAAK           = "datumUitspraak";
  public static final String DATUM_GEWIJSDE            = "datumGewijsde";
  public static final String VERZOEK_INSCHRIJVING_DOOR = "verzoekInschrijvingDoor";
  public static final String DATUM_VERZOEK             = "datumVerzoek";
  public static final String BINNEN_TERMIJN            = "binnenTermijn";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Uitspraak door",
      required = true)
  @Select(containerDataSource = RechtbankContainer.class)
  private RechtbankLocatie uitspraak = null;

  @Field(customTypeClass = ProDateField.class,
      caption = "Datum uitspraak",
      required = true,
      width = "97px")
  private Date datumUitspraak = null;

  @Field(customTypeClass = ProDateField.class,
      caption = "Datum kracht van gewijsde",
      required = true,
      width = "97px")
  private Date datumGewijsde = null;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Verzoek tot inschrijving door",
      required = true)
  private FieldValue verzoekInschrijvingDoor = null;

  @Field(customTypeClass = ProDateField.class,
      caption = "Verzoek ontvangen op",
      required = true,
      width = "97px")
  private Date datumVerzoek = null;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Binnen termijn",
      required = true)
  @Select(nullSelectionAllowed = false,
      containerDataSource = NLBooleanContainer.class,
      itemCaptionPropertyId = NLBooleanContainer.JA_NEE)
  private Boolean binnenTermijn = null;

  public Boolean getBinnenTermijn() {
    return binnenTermijn;
  }

  public void setBinnenTermijn(Boolean binnenTermijn) {
    this.binnenTermijn = binnenTermijn;
  }

  public Date getDatumGewijsde() {
    return datumGewijsde;
  }

  public void setDatumGewijsde(Date datumGewijsde) {
    this.datumGewijsde = datumGewijsde;
  }

  public Date getDatumUitspraak() {
    return datumUitspraak;
  }

  public void setDatumUitspraak(Date datumUitspraak) {
    this.datumUitspraak = datumUitspraak;
  }

  public Date getDatumVerzoek() {
    return datumVerzoek;
  }

  public void setDatumVerzoek(Date datumVerzoek) {
    this.datumVerzoek = datumVerzoek;
  }

  public RechtbankLocatie getUitspraak() {
    return uitspraak;
  }

  public void setUitspraak(RechtbankLocatie uitspraak) {
    this.uitspraak = uitspraak;
  }

  public FieldValue getVerzoekInschrijvingDoor() {
    return verzoekInschrijvingDoor;
  }

  public void setVerzoekInschrijvingDoor(FieldValue verzoekInschrijvingDoor) {
    this.verzoekInschrijvingDoor = verzoekInschrijvingDoor;
  }
}
