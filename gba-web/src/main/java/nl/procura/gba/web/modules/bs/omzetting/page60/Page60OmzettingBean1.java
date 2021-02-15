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

package nl.procura.gba.web.modules.bs.omzetting.page60;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.containers.AdelijkeTitelContainer;
import nl.procura.gba.web.components.containers.VoorvoegselContainer;
import nl.procura.gba.web.components.fields.GbaComboBox;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FilteringMode;
import nl.procura.vaadin.annotation.field.FilteringMode.Mode;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Select;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

import lombok.Data;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
@Data
public class Page60OmzettingBean1 implements Serializable {

  public static final String NATIOP1 = "natioP1";
  public static final String NATIOP2 = "natioP2";
  public static final String NAAMP1  = "naamP1";
  public static final String NAAMP2  = "naamP2";
  public static final String VOORVP1 = "voorvP1";
  public static final String VOORVP2 = "voorvP2";
  public static final String TITELP1 = "titelP1";
  public static final String TITELP2 = "titelP2";

  private DossierPersoon p1;
  private DossierPersoon p2;

  @Field(type = FieldType.LABEL,
      caption = "Nationaliteit partner 1")
  private String natioP1 = null;

  @Field(type = FieldType.LABEL,
      caption = "Nationaliteit partner 2")
  private String natioP2 = null;

  @Field(customTypeClass = GbaComboBox.class,
      caption = "Naam na omzetting partner 1",
      width = "100px")
  @Select(containerDataSource = VoorvoegselContainer.class)
  @FilteringMode(mode = Mode.FILTERINGMODE_STARTSWITH)
  private FieldValue voorvP1 = null;

  @Field(customTypeClass = GbaComboBox.class,
      caption = "Naam na omzetting partner 1",
      width = "100px")
  @Select(containerDataSource = AdelijkeTitelContainer.class)
  @FilteringMode(mode = Mode.FILTERINGMODE_STARTSWITH)
  private FieldValue titelP1 = null;

  @Field(customTypeClass = GbaComboBox.class,
      caption = "Naam na omzetting partner 2",
      width = "100px")
  @Select(containerDataSource = AdelijkeTitelContainer.class)
  @FilteringMode(mode = Mode.FILTERINGMODE_STARTSWITH)
  private FieldValue titelP2 = null;

  @Field(customTypeClass = GbaComboBox.class,
      caption = "Naam na omzetting partner 2",
      width = "100px")
  @Select(containerDataSource = VoorvoegselContainer.class)
  @FilteringMode(mode = Mode.FILTERINGMODE_STARTSWITH)
  private FieldValue voorvP2 = null;

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Naam na omzetting partner 1",
      width = "300px",
      required = true)
  private String naamP1 = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Naam na omzetting partner 2",
      width = "300px",
      required = true)
  private String naamP2 = "";

  public Page60OmzettingBean1() {
  }

  public Page60OmzettingBean1(DossierPersoon p1, DossierPersoon p2) {

    setNatioP1(p1.getNationaliteitenOmschrijving() + " (" + p1.getNaam().getPred_adel_voorv_gesl_voorn() + ")");
    setNatioP2(p2.getNationaliteitenOmschrijving() + " (" + p2.getNaam().getPred_adel_voorv_gesl_voorn() + ")");

    this.p1 = p1;
    this.p2 = p2;
  }
}
