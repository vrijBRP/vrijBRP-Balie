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

package nl.procura.gba.web.modules.beheer.documenten.page1.tab6.page2;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.containers.DocumentFontContainer;
import nl.procura.gba.web.components.containers.DocumentStempelTypeContainer;
import nl.procura.gba.web.components.containers.PositieTypeContainer;
import nl.procura.gba.web.components.fields.GbaTextField;
import nl.procura.gba.web.services.zaken.documenten.stempel.DocumentFontType;
import nl.procura.gba.web.services.zaken.documenten.stempel.DocumentStempelType;
import nl.procura.gba.web.services.zaken.documenten.stempel.PositieType;
import nl.procura.vaadin.annotation.field.*;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.component.field.NumberField;
import nl.procura.vaadin.component.field.ProNativeSelect;
import nl.procura.vaadin.component.field.ValidNumberField;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Tab6DocumentenPage2Bean implements Serializable {

  public static final String STEMPEL     = "stempel";
  public static final String TYPE        = "type";
  public static final String FONT        = "font";
  public static final String FONT_SIZE   = "fontsize";
  public static final String XCOORDINAAT = "x";
  public static final String YCOORDINAAT = "y";
  public static final String HOOGTE      = "hoogte";
  public static final String BREEDTE     = "breedte";
  public static final String POSITIE     = "positie";
  public static final String WOORD       = "woord";
  public static final String VOLGORDE    = "volgorde";
  public static final String PAGINAS     = "paginas";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Stempel",
      required = true,
      width = "200px")
  private String stempel = "";

  @Field(customTypeClass = ProNativeSelect.class,
      caption = "Type",
      required = true,
      width = "200px")
  @Select(containerDataSource = DocumentStempelTypeContainer.class)
  @Immediate
  private DocumentStempelType type = null;

  @Field(customTypeClass = ProNativeSelect.class,
      caption = "Lettertype",
      required = true,
      width = "200px")
  @Select(containerDataSource = DocumentFontContainer.class)
  @Immediate
  private DocumentFontType font = null;

  @Field(customTypeClass = ValidNumberField.class,
      caption = "Lettergrootte",
      required = true,
      width = "50px")
  @TextField(nullRepresentation = "",
      maxLength = 2)
  private String fontsize = "9";

  @Field(customTypeClass = ProNativeSelect.class,
      caption = "Uitgangspositie",
      required = true,
      width = "200px")
  @Select(containerDataSource = PositieTypeContainer.class)
  @Immediate
  private PositieType positie = null;

  @Field(customTypeClass = GbaTextField.class,
      caption = "Woord",
      required = true,
      width = "200px")
  @TextField(nullRepresentation = "",
      maxLength = 100)
  private String woord = "";

  @Field(customTypeClass = ValidNumberField.class,
      caption = "X-coördinaat",
      required = true,
      width = "50px")
  @TextField(nullRepresentation = "",
      maxLength = 4)
  private String x = "0";

  @Field(customTypeClass = ValidNumberField.class,
      caption = "Y-coördinaat",
      required = true,
      width = "50px")
  @TextField(nullRepresentation = "",
      maxLength = 4)
  private String y = "0";

  @Field(customTypeClass = NumberField.class,
      caption = "Hoogte",
      required = true,
      width = "50px")
  @TextField(nullRepresentation = "",
      maxLength = 5)
  private String hoogte = "100";

  @Field(customTypeClass = NumberField.class,
      caption = "Breedte",
      required = true,
      width = "50px")
  @TextField(nullRepresentation = "",
      maxLength = 5)
  private String breedte = "100";

  @Field(customTypeClass = GbaTextField.class,
      caption = "Pagina's",
      width = "200px")
  @TextField(nullRepresentation = "",
      maxLength = 150)
  @InputPrompt(text = "bijv. 1-5, 8, 11-13")
  private String paginas = "";

  @Field(customTypeClass = ValidNumberField.class,
      caption = "Volgorde",
      required = true,
      width = "50px")
  @TextField(nullRepresentation = "",
      maxLength = 5)
  private String volgorde = "0";
}
