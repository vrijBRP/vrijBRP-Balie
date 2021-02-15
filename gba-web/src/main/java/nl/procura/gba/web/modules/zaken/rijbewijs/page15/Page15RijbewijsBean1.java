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

package nl.procura.gba.web.modules.zaken.rijbewijs.page15;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.math.BigInteger;

import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Select;
import nl.procura.vaadin.component.field.DatumVeld;
import nl.procura.vaadin.component.field.ProNativeSelect;
import nl.procura.vaadin.component.field.fieldvalues.DateFieldValue;

import lombok.Data;

@FormFieldFactoryBean(accessType = ElementType.FIELD,
    defaultWidth = "80px")
@Data
public class Page15RijbewijsBean1 implements Serializable {

  public static final String RIJBEWIJS_NR   = "rijbewijsNr";
  public static final String DATUM_INLEV    = "datumInlevering";
  public static final String REDEN_ONGELDIG = "redenOngeldig";
  public static final String CAT_AM         = "catAM";
  public static final String CAT_A1         = "catA1";
  public static final String CAT_A2         = "catA2";
  public static final String CAT_A          = "catA";
  public static final String CAT_AL         = "catAL";
  public static final String CAT_AZ         = "catAZ";

  public static final String CAT_B  = "catB";
  public static final String CAT_C  = "catC";
  public static final String CAT_C1 = "catC1";
  public static final String CAT_D  = "catD";
  public static final String CAT_D1 = "catD1";

  public static final String CAT_BE  = "catBE";
  public static final String CAT_CE  = "catCE";
  public static final String CAT_C1E = "catC1E";
  public static final String CAT_DE  = "catDE";
  public static final String CAT_D1E = "catD1E";
  public static final String CAT_T   = "catT";

  @Field(type = Field.FieldType.LABEL,
      caption = "Rijbewijsnummer")
  private BigInteger rijbewijsNr;

  @Field(customTypeClass = DatumVeld.class,
      caption = "Datum inlevering rijbewijs",
      width = "80px")
  private DateFieldValue datumInlevering = null;

  @Field(customTypeClass = ProNativeSelect.class,
      caption = "Reden ongeldigverklaring",
      required = true,
      width = "400px")

  @Select(containerDataSource = RedenOngeldigVerklaringContainer.class)
  private RedenOngeldigVerklaring redenOngeldig = null;

  @Field(customTypeClass = DatumVeld.class,
      caption = "AM")
  private DateFieldValue catAM = null;

  @Field(customTypeClass = DatumVeld.class,
      caption = "A1")
  private DateFieldValue catA1 = null;

  @Field(customTypeClass = DatumVeld.class,
      caption = "A2")
  private DateFieldValue catA2 = null;

  @Field(customTypeClass = DatumVeld.class,
      caption = "A")
  private DateFieldValue catA = null;

  @Field(customTypeClass = DatumVeld.class,
      caption = "AL")
  private DateFieldValue catAL = null;

  @Field(customTypeClass = DatumVeld.class,
      caption = "AZ")
  private DateFieldValue catAZ = null;

  @Field(customTypeClass = DatumVeld.class,
      caption = "B")
  private DateFieldValue catB = null;

  @Field(customTypeClass = DatumVeld.class,
      caption = "C")
  private DateFieldValue catC = null;

  @Field(customTypeClass = DatumVeld.class,
      caption = "C1")
  private DateFieldValue catC1 = null;

  @Field(customTypeClass = DatumVeld.class,
      caption = "D")
  private DateFieldValue catD = null;

  @Field(customTypeClass = DatumVeld.class,
      caption = "D1")
  private DateFieldValue catD1 = null;

  @Field(customTypeClass = DatumVeld.class,
      caption = "BE")
  private DateFieldValue catBE = null;

  @Field(customTypeClass = DatumVeld.class,
      caption = "CE")
  private DateFieldValue catCE = null;

  @Field(customTypeClass = DatumVeld.class,
      caption = "C1E")
  private DateFieldValue catC1E = null;

  @Field(customTypeClass = DatumVeld.class,
      caption = "DE")
  private DateFieldValue catDE = null;

  @Field(customTypeClass = DatumVeld.class,
      caption = "D1E")
  private DateFieldValue catD1E = null;

  @Field(customTypeClass = DatumVeld.class,
      caption = "T")
  private DateFieldValue catT = null;
}
