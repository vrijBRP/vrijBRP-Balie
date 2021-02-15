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

package nl.procura.gba.web.modules.hoofdmenu.klapper.page1;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.services.bs.algemeen.akte.DossierAkteDeel;
import nl.procura.gba.web.services.bs.algemeen.akte.DossierAkteInvoerType;
import nl.procura.gba.web.services.bs.algemeen.akte.DossierAkteRegistersoort;
import nl.procura.gba.web.services.bs.algemeen.akte.KlapperVolgordeType;
import nl.procura.vaadin.annotation.field.*;
import nl.procura.vaadin.component.field.BsnField;
import nl.procura.vaadin.component.field.DatumVeld;
import nl.procura.vaadin.component.field.NumberField;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.DateFieldValue;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page1KlapperBean implements Serializable {

  public static final String INVOERTYPE    = "invoerType";
  public static final String DATUM         = "datum";
  public static final String JAAR_VAN      = "jaarVan";
  public static final String JAAR_TM       = "jaarTm";
  public static final String SOORT         = "soort";
  public static final String DEEL          = "deel";
  public static final String NUMMER        = "nummer";
  public static final String BSN           = "bsn";
  public static final String GESLACHTSNAAM = "geslachtsnaam";
  public static final String VOLGORDE      = "volgorde";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Type",
      width = "150px")
  @Immediate
  private DossierAkteInvoerType invoerType = null;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Periode t/m",
      width = "87px")
  @Select(nullSelectionAllowed = false)
  @Immediate
  private long jaarVan = 0;

  @Field(customTypeClass = GbaNativeSelect.class,
      width = "87px")
  @Select(nullSelectionAllowed = false)
  @Immediate
  private long jaarTm = 0;

  @Field(customTypeClass = DatumVeld.class,
      caption = "Datum",
      width = "180px")
  @Immediate
  private DateFieldValue datum = null;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Registersoort",
      width = "280px")
  @Immediate
  private DossierAkteRegistersoort soort = DossierAkteRegistersoort.AKTE_ONBEKEND;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Registerdeel",
      width = "280px")
  @Immediate
  private DossierAkteDeel deel = null;

  @Field(customTypeClass = NumberField.class,
      caption = "Volgnummer",
      width = "80px")
  @TextField(maxLength = 4)
  @Immediate
  private String nummer = "";

  @Field(customTypeClass = BsnField.class,
      caption = "Burgerservicenummer",
      required = true,
      width = "150px")
  @Immediate
  private BsnFieldValue bsn = null;

  @Field(customTypeClass = BsnField.class,
      caption = "Geslachtsnaam",
      required = true,
      width = "150px")
  @Immediate
  private BsnFieldValue geslachtsnaam = null;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Volgorde",
      width = "180px")
  @Immediate
  private KlapperVolgordeType volgorde = KlapperVolgordeType.DATUM_OPLOPEND;
}
