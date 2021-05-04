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

package nl.procura.gba.web.modules.beheer.documenten.page1.tab1.page2.importing;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Select;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class DocumentExportBean implements Serializable {

  public static final String ALIAS             = "alias";
  public static final String DMS_DOCUMENT_TYPE = "documentTypeOms";
  public static final String VERTROUWELIJKHEID = "vertrouwelijkheid";
  public static final String OMSCHRIJVING      = "omschrijving";
  public static final String FORMATS           = "formats";

  public static final String IEDEREEN_TOEGANG       = "iedereenToegang";
  public static final String STANDAARD_GESELECTEERD = "standaardGeselecteerd";
  public static final String PROTOCOLLERING         = "protocollering";
  public static final String KOPIE_OPSLAAN          = "kopieOpslaan";
  public static final String STILLBORN_ALLOWED      = "stillbornAllowed";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Alias")
  @Select(containerDataSource = AllAllowedStringContainer.class,
      nullSelectionAllowed = false)
  private DocumentImportOptieType alias = DocumentImportOptieType.INITIEEL_OVERNEMEN;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "DMS documenttype")
  @Select(containerDataSource = AllAllowedStringContainer.class,
      nullSelectionAllowed = false)
  private DocumentImportOptieType documentTypeOms = DocumentImportOptieType.INITIEEL_OVERNEMEN;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Vertrouwelijkheid")
  @Select(containerDataSource = AllAllowedStringContainer.class,
      nullSelectionAllowed = false)
  private DocumentImportOptieType vertrouwelijkheid = DocumentImportOptieType.INITIEEL_OVERNEMEN;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Omschrijving")
  @Select(containerDataSource = AllAllowedStringContainer.class,
      nullSelectionAllowed = false)
  private DocumentImportOptieType omschrijving = DocumentImportOptieType.INITIEEL_OVERNEMEN;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Uitvoerformaten")
  @Select(containerDataSource = AllAllowedStringContainer.class,
      nullSelectionAllowed = false)
  private DocumentImportOptieType formats = DocumentImportOptieType.INITIEEL_OVERNEMEN;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Iedereen toegang")
  @Select(containerDataSource = AllAllowedBooleanContainer.class,
      nullSelectionAllowed = false)
  private DocumentImportOptieType iedereenToegang = DocumentImportOptieType.INITIEEL_OVERNEMEN;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Levenloos geboren")
  @Select(containerDataSource = AllAllowedBooleanContainer.class,
      nullSelectionAllowed = false)
  private DocumentImportOptieType stillbornAllowed = DocumentImportOptieType.INITIEEL_OVERNEMEN;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Standaard geselecteerd")
  @Select(containerDataSource = AllAllowedBooleanContainer.class,
      nullSelectionAllowed = false)
  private DocumentImportOptieType standaardGeselecteerd = DocumentImportOptieType.INITIEEL_OVERNEMEN;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Kopie opslaan")
  @Select(containerDataSource = AllAllowedBooleanContainer.class,
      nullSelectionAllowed = false)
  private DocumentImportOptieType kopieOpslaan = DocumentImportOptieType.INITIEEL_OVERNEMEN;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Protocollering")
  @Select(containerDataSource = AllAllowedBooleanContainer.class,
      nullSelectionAllowed = false)
  private DocumentImportOptieType protocollering = DocumentImportOptieType.INITIEEL_OVERNEMEN;
}
