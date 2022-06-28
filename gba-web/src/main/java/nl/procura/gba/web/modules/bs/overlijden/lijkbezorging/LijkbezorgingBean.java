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

package nl.procura.gba.web.modules.bs.overlijden.lijkbezorging;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.util.Date;

import nl.procura.gba.web.components.containers.LandContainer;
import nl.procura.gba.web.components.fields.GbaComboBox;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.fields.GbaTextField;
import nl.procura.gba.web.components.fields.TimeField;
import nl.procura.gba.web.modules.bs.overlijden.DoodsoorzaakContainer;
import nl.procura.gba.web.modules.bs.overlijden.TermijnLijkbezorgingContainer;
import nl.procura.gba.web.modules.bs.overlijden.WijzeLijkbezorgingContainer;
import nl.procura.gba.web.services.bs.algemeen.enums.Doodsoorzaak;
import nl.procura.gba.web.services.bs.algemeen.enums.OntvangenDocument;
import nl.procura.gba.web.services.bs.algemeen.enums.TermijnLijkbezorging;
import nl.procura.gba.web.services.bs.algemeen.enums.WijzeLijkbezorging;
import nl.procura.vaadin.annotation.field.*;
import nl.procura.vaadin.component.container.NLBooleanContainer;
import nl.procura.vaadin.component.field.ProDateField;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.field.fieldvalues.TimeFieldValue;

import lombok.Data;

@Data
@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class LijkbezorgingBean implements Serializable {

  public static final String WIJZE_LIJKBEZORGING = "wijzeLijkBezorging";
  public static final String DATUM_LIJKBEZORGING = "datumLijkbezorging";
  public static final String TIJD_LIJKBEZORGING  = "tijdLijkbezorging";

  public static final String BUITEN_BENELUX    = "buitenBenelux";
  public static final String LAND_BESTEMMING   = "landBestemming";
  public static final String PLAATS_BESTEMMING = "plaatsBestemming";
  public static final String PLAATS_ONTLEDING  = "plaatsOntleding";
  public static final String VIA               = "via";
  public static final String DOODSOORZAAK      = "doodsoorzaak";
  public static final String VERVOERMIDDEL     = "vervoermiddel";

  public static final String TERMIJN_LIJKBEZORGING            = "termijnLijkbezorging";
  public static final String ONTVANGEN_DOCUMENT_LIJKBEZORGING = "ontvangenDocumentLijkbezorging";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Wijze van lijkbezorging",
      required = true,
      width = "150px")
  @Select(containerDataSource = WijzeLijkbezorgingContainer.class)
  private WijzeLijkbezorging wijzeLijkBezorging = null;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Doodsoorzaak",
      width = "250px")
  @Select(nullSelectionAllowed = false,
      containerDataSource = DoodsoorzaakContainer.class)
  private Doodsoorzaak doodsoorzaak = null;

  @Field(customTypeClass = ProDateField.class,
      caption = "Op (datum)",
      description = "Op (datum)",
      required = true,
      width = "100px")
  private Date datumLijkbezorging = null;

  @Field(customTypeClass = TimeField.class,
      caption = "Tijdstip",
      width = "76px")
  @TextField(maxLength = 5)
  @Immediate
  private TimeFieldValue tijdLijkbezorging = new TimeFieldValue();

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Buiten Benelux",
      width = "76px",
      required = true)
  @Select(containerDataSource = NLBooleanContainer.class,
      itemCaptionPropertyId = NLBooleanContainer.JA_NEE,
      nullSelectionAllowed = false)
  private boolean buitenBenelux = false;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Termijn van lijkbezorging",
      width = "350px",
      required = true)
  @Select(containerDataSource = TermijnLijkbezorgingContainer.class,
      nullSelectionAllowed = false)
  private TermijnLijkbezorging termijnLijkbezorging = null;

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Ontvangen document",
      width = "350px",
      required = true)
  @Select(nullSelectionAllowed = false)
  private OntvangenDocument ontvangenDocumentLijkbezorging = OntvangenDocument.ONBEKEND;

  @Field(customTypeClass = GbaComboBox.class,
      caption = "Land van bestemming",
      width = "250px")
  @Select(containerDataSource = LandContainer.class)
  @Immediate
  private FieldValue landBestemming = null;

  @Field(customTypeClass = GbaTextField.class,
      caption = "Plaats van bestemming",
      width = "250px")
  @TextField(nullRepresentation = "")
  private String plaatsBestemming = null;

  @Field(customTypeClass = GbaTextField.class,
      caption = "Plaats van ontleding",
      width = "250px")
  @TextField(nullRepresentation = "")
  private String plaatsOntleding = null;

  @Field(customTypeClass = GbaTextField.class,
      caption = "Via",
      width = "250px")
  @TextField(nullRepresentation = "")
  private String via = null;

  @Field(customTypeClass = GbaTextField.class,
      caption = "Vervoermiddel",
      width = "250px")
  @TextField(nullRepresentation = "")
  private String vervoermiddel = "";
}
