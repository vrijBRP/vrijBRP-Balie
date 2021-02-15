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

package nl.procura.gba.web.modules.bs.common.layouts.namenrecht;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.containers.AdelijkeTitelContainer;
import nl.procura.gba.web.components.containers.LandContainer;
import nl.procura.gba.web.components.containers.VoorvoegselContainer;
import nl.procura.gba.web.components.fields.GbaComboBox;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.fields.GbaTextField;
import nl.procura.gba.web.modules.bs.erkenning.page30.EersteTypeContainer;
import nl.procura.gba.web.modules.bs.erkenning.page30.NaamskeuzePersoonContainer;
import nl.procura.gba.web.modules.bs.erkenning.page30.NaamskeuzeTypeContainer;
import nl.procura.gba.web.services.bs.erkenning.EersteKindType;
import nl.procura.gba.web.services.bs.erkenning.NaamsPersoonType;
import nl.procura.gba.web.services.bs.erkenning.NaamskeuzeVanToepassingType;
import nl.procura.vaadin.annotation.field.*;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FilteringMode.Mode;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class BsNamenrechtBean implements Serializable {

  public static final String KIND_OUDER_DAN_16  = "kindOuderDan16";
  public static final String RECHT              = "recht";
  public static final String GESLACHTSNAAM      = "geslachtsnaam";
  public static final String VOORV              = "voorv";
  public static final String TITEL              = "titel";
  public static final String NAAMSKEUZE_PERSOON = "naamskeuzePersoon";
  public static final String NAAMSKEUZE_TYPE    = "naamskeuzeType";
  public static final String EERSTE_KIND_TYPE   = "eersteKindType";

  @Field(type = FieldType.LABEL,
      caption = "Kind(eren) ouder dan 16")
  private String kindOuderDan16 = "";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Toegepast recht van",
      required = true,
      width = "200px")
  @Select(containerDataSource = LandContainer.class)
  @Immediate
  private FieldValue recht = new FieldValue();

  @Field(customTypeClass = GbaTextField.class,
      caption = "Geslachtsnaam kind",
      required = true,
      width = "200px")
  @Immediate
  @TextField(nullRepresentation = "")
  private String geslachtsnaam = "";

  @Field(customTypeClass = GbaComboBox.class,
      caption = "Voorvoegsel kind",
      width = "200px")
  @Select(containerDataSource = VoorvoegselContainer.class)
  @Immediate
  @TextField(nullRepresentation = "")
  @FilteringMode(mode = Mode.FILTERINGMODE_STARTSWITH)
  private FieldValue voorv = new FieldValue();

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Titel kind",
      width = "200px")
  @Select(containerDataSource = AdelijkeTitelContainer.class)
  @Immediate
  @TextField(nullRepresentation = "")
  @FilteringMode(mode = Mode.FILTERINGMODE_STARTSWITH)
  private FieldValue titel = new FieldValue();

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Naam gekregen van",
      width = "200px",
      readOnly = true)
  @Select(containerDataSource = NaamskeuzePersoonContainer.class)
  @Immediate
  private NaamsPersoonType naamskeuzePersoon = NaamsPersoonType.ONBEKEND;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Naamskeuze",
      width = "200px",
      required = true)
  @Select(containerDataSource = NaamskeuzeTypeContainer.class)
  @Immediate
  private NaamskeuzeVanToepassingType naamskeuzeType = NaamskeuzeVanToepassingType.NVT;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Eerste kind",
      width = "200px",
      required = true)
  @Select(containerDataSource = EersteTypeContainer.class)
  @Immediate
  private EersteKindType eersteKindType = EersteKindType.ONBEKEND;
}
