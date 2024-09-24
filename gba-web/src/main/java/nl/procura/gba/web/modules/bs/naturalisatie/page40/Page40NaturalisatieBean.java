/*
 * Copyright 2022 - 2023 Procura B.V.
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

package nl.procura.gba.web.modules.bs.naturalisatie.page40;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.services.bs.naturalisatie.enums.BereidAfstandType;
import nl.procura.gba.web.services.bs.naturalisatie.enums.BewijsNationaliteitType;
import nl.procura.gba.web.services.bs.naturalisatie.enums.GeldigeVerblijfsvergunningType;
import nl.procura.gba.web.services.bs.naturalisatie.enums.NaamvaststellingType;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Select;
import nl.procura.vaadin.annotation.field.TextArea;
import nl.procura.vaadin.annotation.field.TextField;
import nl.procura.vaadin.component.container.NLBooleanContainer;
import nl.procura.vaadin.component.field.ProTextArea;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page40NaturalisatieBean implements Serializable {

  public static final String F_VERKLARING_VERBLIJF            = "verklaringVerblijf";
  public static final String F_BEREID_TOT_AFLEGGEN_VERKLARING = "bereidTotAfleggenVerklaring";
  public static final String F_BETROKKENE_BEKEND_MET_BETALING = "betrokkeneBekendMetBetaling";
  public static final String F_BEREID_TOT_DOEN_VAN_AFSTAND    = "bereidTotDoenVanAfstand";
  public static final String F_BEWIJS_VAN_IDENTITEIT          = "bewijsVanIdentiteit";
  public static final String F_BEWIJS_VAN_NATIONALITEIT       = "bewijsVanNationaliteit";
  public static final String F_BEWIJSNOOD_AANGETOOND          = "bewijsnoodAangetoond";
  public static final String F_TOELICHTING1                   = "toelichting1";
  public static final String F_GELDIGE_VERBLIJFSVERGUNNING    = "geldigeVerblijfsvergunning";
  public static final String F_INBURGERING                    = "inburgering";
  public static final String F_NAAMSVASTSTELLINGOFWIJZIGING   = "naamsvaststellingOfWijziging";
  public static final String F_NAAM_VASTGESTELD               = "naamVastgesteld";
  public static final String F_NAAM_GEWIJZIGD                 = "naamGewijzigd";
  public static final String F_TOELICHTING2                   = "toelichting2";
  public static final String F_VNR                            = "vnr";

  @Field(type = FieldType.LABEL,
      caption = "V-nummer",
      readOnly = true)
  private String vnr;

  @Field(customTypeClass = GbaNativeSelect.class,
      required = true,
      caption = "Verklaring verblijf en gedrag ondertekend")
  @Select(containerDataSource = NLBooleanContainer.class,
      itemCaptionPropertyId = NLBooleanContainer.JA_NEE)
  private Boolean verklaringVerblijf;

  @Field(customTypeClass = GbaNativeSelect.class,
      required = true,
      caption = "Bereid tot afleggen verklaring van verbondenheid")
  @Select(containerDataSource = NLBooleanContainer.class,
      itemCaptionPropertyId = NLBooleanContainer.JA_NEE)
  private Boolean bereidTotAfleggenVerklaring;

  @Field(customTypeClass = GbaNativeSelect.class,
      required = true,
      caption = "Betrokkene bekend met betaling/ontheffing/vrijstelling")
  @Select(containerDataSource = NLBooleanContainer.class,
      itemCaptionPropertyId = NLBooleanContainer.JA_NEE)
  private Boolean betrokkeneBekendMetBetaling;

  @Field(customTypeClass = GbaNativeSelect.class,
      required = true,
      caption = "Bereid tot doen van afstand huidige nationaliteit")
  @Select(containerDataSource = BereidAfstandContainer.class)
  private BereidAfstandType bereidTotDoenVanAfstand;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Bewijs van identiteit aanwezig")
  @Select(containerDataSource = NLBooleanContainer.class,
      itemCaptionPropertyId = NLBooleanContainer.JA_NEE)
  private Boolean bewijsVanIdentiteit;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Bewijs van nationaliteit aanwezig")
  @Select(containerDataSource = BewijsNationaliteitContainer.class)
  private BewijsNationaliteitType bewijsVanNationaliteit;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Bewijsnood aangetoond")
  @Select(containerDataSource = NLBooleanContainer.class,
      itemCaptionPropertyId = NLBooleanContainer.JA_NEE)
  private Boolean bewijsnoodAangetoond;

  @Field(customTypeClass = ProTextArea.class,
      width = "500px",
      caption = "Toelichting")
  @TextArea(rows = 5, nullRepresentation = "")
  private String toelichting1;

  @Field(customTypeClass = GbaNativeSelect.class,
      required = true,
      caption = "Geldige verblijfsvergunning aanwezig")
  @Select(containerDataSource = GeldigeVerblijfsvergunningContainer.class)
  private GeldigeVerblijfsvergunningType geldigeVerblijfsvergunning;

  // Alleen bij naturalisatie
  @Field(type = FieldType.LABEL,
      caption = "Inburgering",
      readOnly = true)
  private String inburgering;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Naamsvaststelling of wijziging nodig")
  @Select(containerDataSource = NaamstellingContainer.class)
  private NaamvaststellingType naamsvaststellingOfWijziging;

  @Field(type = FieldType.LABEL,
      caption = "Naam vastgesteld als",
      readOnly = true)
  @TextField(nullRepresentation = "")
  private String naamVastgesteld;

  @Field(type = FieldType.LABEL,
      caption = "Naam gewijzigd in",
      readOnly = true)
  private String naamGewijzigd;

  @Field(customTypeClass = ProTextArea.class,
      width = "500px",
      caption = "Toelichting")
  @TextArea(rows = 5, nullRepresentation = "")
  private String toelichting2;

}
