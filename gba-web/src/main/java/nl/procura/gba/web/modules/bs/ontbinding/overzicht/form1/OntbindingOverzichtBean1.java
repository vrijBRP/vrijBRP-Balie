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

package nl.procura.gba.web.modules.bs.ontbinding.overzicht.form1;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;

import lombok.Data;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
@Data
public class OntbindingOverzichtBean1 implements Serializable {

  // Huidige situatie
  public static final String SLUITING = "sluiting";
  public static final String AKTE     = "akte";

  // Brondocument
  public static final String UITSPRAAK                 = "uitspraak";
  public static final String DATUM_GEWIJSDE            = "datumGewijsde";
  public static final String VERZOEK_INSCHRIJVING_DOOR = "verzoekInschrijvingDoor";
  public static final String DATUM_VERZOEK             = "datumVerzoek";
  public static final String BINNEN_TERMIJN            = "binnenTermijn";

  // Wijze van beindiging
  public static final String DOOR = "door";

  public static final String DATUM_VERKLARING    = "datumVerklaring";
  public static final String DATUM_ONDERTEKENING = "datumOndertekening";
  public static final String ONDERTEKEND_DOOR    = "ondertekendDoor";

  @Field(type = FieldType.LABEL,
      caption = "Huwelijk/GPS sluitingsgegevens")
  private Object sluiting = null;

  @Field(type = FieldType.LABEL,
      caption = "Huwelijk/GPS aktegegevens")
  private Object akte = null;

  @Field(type = FieldType.LABEL,
      caption = "Uitspraak door")
  private Object uitspraak = null;

  @Field(type = FieldType.LABEL,
      caption = "Datum kracht van gewijsde")
  private Object datumGewijsde = null;

  @Field(type = FieldType.LABEL,
      caption = "Verzoek tot inschrijving door")
  private Object verzoekInschrijvingDoor = null;

  @Field(type = FieldType.LABEL,
      caption = "Verzoek ontvangen op")
  private Object datumVerzoek = null;

  @Field(type = FieldType.LABEL,
      caption = "Binnen termijn")
  private Object binnenTermijn = null;

  @Field(type = FieldType.LABEL,
      caption = "door")
  private Object door = null;

  @Field(type = FieldType.LABEL,
      caption = "Verklaring ontvangen op")
  private Object datumVerklaring = null;

  @Field(type = FieldType.LABEL,
      caption = "Ondertekend op")
  private Object datumOndertekening = null;
  @Field(type = FieldType.LABEL,
      caption = "Ondertekend door partijen en")
  private Object ondertekendDoor    = null;

  public OntbindingOverzichtBean1() {
  }
}
