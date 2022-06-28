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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.page4.zoeken;

import static nl.procura.standard.Globalfunctions.fil;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.common.misc.ZaakPeriode;
import nl.procura.gba.web.components.fields.BsnAnrVeld;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.fields.GbaTextField;
import nl.procura.gba.web.components.fields.SimpleMultiField;
import nl.procura.gba.web.components.fields.values.MultiFieldValue;
import nl.procura.gba.web.components.fields.values.UsrFieldValue;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Immediate;
import nl.procura.vaadin.annotation.field.Select;
import nl.procura.vaadin.component.field.DatumVeld;
import nl.procura.vaadin.component.field.fieldvalues.DateFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page4ZakenBean implements Serializable {

  public static final String INVOER_VAN = "invoerVan";
  public static final String INVOER_TM  = "invoerTm";

  public static final String INGANG_VAN = "ingangVan";
  public static final String INGANG_TM  = "ingangTm";

  public static final String AANVRAAGNR         = "aanvraagNr";
  public static final String NR                 = "nr";
  public static final String INCOMPLEET         = "incompleet";
  public static final String WACHTKAMER         = "wachtkamer";
  public static final String OPGENOMEN          = "opgenomen";
  public static final String GEPREVALIDEERD     = "geprevalideerd";
  public static final String INBEHANDELING      = "inbehandeling";
  public static final String DOCUMENT_ONTVANGEN = "documentOntvangen";
  public static final String NIET_VERWERKT      = "nietVerwerkt";
  public static final String VERWERKT           = "verwerkt";
  public static final String GEANNULEERD        = "geannuleerd";
  public static final String GEBRUIKER          = "gebruiker";
  public static final String PROFIEL            = "profiel";
  public static final String ZAAKTYPES          = "zaakTypes";
  public static final String ZAAKSTATUSSEN      = "zaakStatussen";
  static final String        DATUM_INVOER       = "datumInvoer";
  static final String        DATUM_INGANG       = "datumIngang";
  static final String        INDICATIE          = "indicatie";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Datum invoer",
      width = "230px")
  @Select(containerDataSource = ZaakHistorischePeriodesContainer.class)
  @Immediate
  private ZaakPeriode datumInvoer = null;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Datum ingang",
      width = "230px")
  @Select(containerDataSource = ZaakAllePeriodesContainer.class)
  @Immediate
  private ZaakPeriode datumIngang = null;

  @Field(customTypeClass = DatumVeld.class,
      caption = "Van",
      width = "80px",
      required = true,
      visible = false)
  private DateFieldValue invoerVan = null;

  @Field(customTypeClass = DatumVeld.class,
      caption = "t/m",
      width = "80px",
      required = true,
      visible = false)
  private DateFieldValue invoerTm = null;

  @Field(customTypeClass = DatumVeld.class,
      caption = "Van",
      width = "80px",
      required = true,
      visible = false)
  private DateFieldValue ingangVan = null;

  @Field(customTypeClass = DatumVeld.class,
      caption = "t/m",
      width = "80px",
      required = true,
      visible = false)
  private DateFieldValue ingangTm = null;

  @Field(customTypeClass = GbaTextField.class,
      caption = "Zaak-id",
      width = "230px")
  private String aanvraagNr = "";

  @Field(customTypeClass = BsnAnrVeld.class,
      caption = "A-nummer / BSN",
      width = "230px")
  private String nr = "";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Gebruiker",
      width = "230px")
  @Immediate
  private UsrFieldValue gebruiker = new UsrFieldValue();

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Profiel",
      width = "230px")
  @Immediate
  private FieldValue profiel = new FieldValue();

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Indicatie",
      width = "230px")
  @Immediate
  private FieldValue indicatie = new FieldValue();

  @Field(customTypeClass = SimpleMultiField.class,
      caption = "Zaaktypes",
      width = "230px")
  private MultiFieldValue<FieldValue> zaakTypes = new MultiFieldValue<>();

  @Field(customTypeClass = SimpleMultiField.class,
      caption = "Zaakstatussen",
      width = "230px")
  private MultiFieldValue<FieldValue> zaakStatussen = new MultiFieldValue<>();

  public boolean isFilled() {
    return getDatumInvoer() != null || getDatumIngang() != null || fil(getAanvraagNr()) || fil(getNr());
  }
}
