/*
 * Copyright 2024 - 2025 Procura B.V.
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

package nl.procura.gba.web.modules.zaken.reisdocument.basisregister.page3;

import static nl.procura.vaadin.annotation.field.Field.FieldType.LABEL;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import lombok.Data;
import nl.procura.burgerzaken.vrsclient.api.VrsMeldingRedenType;
import nl.procura.burgerzaken.vrsclient.api.VrsMeldingType;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.validators.DatumNietToekomstValidator;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Immediate;
import nl.procura.vaadin.annotation.field.Select;
import nl.procura.vaadin.component.container.NLBooleanContainer;
import nl.procura.vaadin.component.field.DatumVeld;
import nl.procura.vaadin.component.field.ProNativeSelect;
import nl.procura.vaadin.component.field.ProTextField;
import nl.procura.vaadin.component.field.fieldvalues.DateFieldValue;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page3VrsBasisRegisterBean1 implements Serializable {

  public static final String DOC_NR           = "documentnummer";
  public static final String DOC_SOORT        = "documentSoort";
  public static final String MELDING_TYPE        = "meldingType";
  public static final String REDEN_TYPE          = "redenType";
  public static final String DATUM_REDEN_MELDING = "datumRedenMelding";
  public static final String INGELEVERD          = "ingeleverd";
  public static final String DATUM_INLEVERING = "datumInlevering";
  public static final String BRP_ACTIE           = "brpActie";

  @Field(type = LABEL,
      width = "300px",
      caption = "Documentnummer")
  private String documentnummer;

  @Field(type = LABEL,
      width = "300px",
      caption = "Soort document")
  private String documentSoort;

  @Field(customTypeClass = ProNativeSelect.class,
      caption = "Type melding",
      width = "400px",
      required = true)
  @Select(nullSelectionAllowed = false)
  private VrsMeldingType meldingType = null;

  @Field(customTypeClass = ProNativeSelect.class,
      caption = "Reden",
      width = "400px",
      required = true)
  @Select(nullSelectionAllowed = false)
  private VrsMeldingRedenType redenType = null;

  @Field(customTypeClass = DatumVeld.class,
      caption = "Datum reden",
      width = "80px",
      required = true,
      validators = DatumNietToekomstValidator.class)
  private DateFieldValue datumRedenMelding = new DateFieldValue();

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Is document ook ingeleverd?",
      required = true,
      width = "80px")
  @Select(nullSelectionAllowed = false,
      containerDataSource = NLBooleanContainer.class,
      itemCaptionPropertyId = NLBooleanContainer.JA_NEE)
  @Immediate
  private Boolean ingeleverd;

  @Field(customTypeClass = DatumVeld.class,
      caption = "Inleverdatum",
      width = "80px",
      required = true,
      validators = DatumNietToekomstValidator.class)
  private DateFieldValue datumInlevering = new DateFieldValue();

  @Field(customTypeClass = ProTextField.class,
      readOnly = true,
      caption = "Actie",
      width = "700px")
  private String brpActie = null;
}
