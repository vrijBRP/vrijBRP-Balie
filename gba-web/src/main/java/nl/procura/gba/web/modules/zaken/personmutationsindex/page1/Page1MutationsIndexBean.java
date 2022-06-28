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

package nl.procura.gba.web.modules.zaken.personmutationsindex.page1;

import static nl.procura.vaadin.component.container.ProcuraContainer.OMSCHRIJVING;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import com.vaadin.ui.CheckBox;

import nl.procura.bsm.rest.v1_0.objecten.gba.probev.mutations.MutationType;
import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.gba.web.common.misc.ZaakPeriode;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page4.telling.Vandaag;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page4.zoeken.ZaakHistorischePeriodesContainer;
import nl.procura.vaadin.annotation.field.*;
import nl.procura.vaadin.component.field.AnrField;
import nl.procura.vaadin.component.field.DatumVeld;
import nl.procura.vaadin.component.field.fieldvalues.AnrFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.DateFieldValue;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page1MutationsIndexBean implements Serializable {

  public static final String PERSOON           = "persoon";
  public static final String RELATIES          = "relaties";
  public static final String ANR               = "anr";
  public static final String DATUM_MUTATIE     = "datumMutatie";
  public static final String DATUM_MUTATIE_VAN = "datumMutatieVan";
  public static final String DATUM_MUTATIE_TM  = "datumMutatieTm";
  public static final String CAT               = "cat";
  public static final String STATUS            = "status";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Persoon",
      width = "250px")
  @Immediate
  @Select(itemCaptionPropertyId = OMSCHRIJVING)
  private AnrFieldValue persoon;

  @Field(customTypeClass = CheckBox.class,
      caption = "Gerelateerden")
  @Immediate
  private Boolean relaties = true;

  @Field(customTypeClass = AnrField.class,
      caption = "A-nummer",
      width = "100px",
      visible = false)
  @InputPrompt(text = "A-nummer")
  private AnrFieldValue anr;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Datum mutatie",
      width = "250px")
  @Select(containerDataSource = ZaakHistorischePeriodesContainer.class)
  @Immediate
  private ZaakPeriode datumMutatie = new Vandaag();

  @Field(customTypeClass = DatumVeld.class,
      caption = "Van",
      width = "80px",
      required = true,
      visible = false)
  private DateFieldValue datumMutatieVan = null;

  @Field(customTypeClass = DatumVeld.class,
      caption = "t/m",
      width = "80px",
      required = true,
      visible = false)
  private DateFieldValue datumMutatieTm = null;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Status",
      width = "250px")
  @Immediate
  @Select(itemCaptionPropertyId = OMSCHRIJVING)
  private MutationType status;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Categorie",
      width = "200px")
  @Immediate
  @Select(itemCaptionPropertyId = OMSCHRIJVING)
  private GBACat cat;
}
