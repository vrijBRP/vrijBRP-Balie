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

package nl.procura.gba.web.modules.bs.omzetting.page50;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.containers.actueel.LandActueelContainer;
import nl.procura.gba.web.components.fields.GbaComboBox;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Select;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page50OmzettingBean1 implements Serializable {

  public static final String NATIOP1 = "natioP1";
  public static final String NATIOP2 = "natioP2";
  public static final String RECHTP1 = "rechtP1";
  public static final String RECHTP2 = "rechtP2";

  private DossierPersoon p1;
  private DossierPersoon p2;

  @Field(type = FieldType.LABEL,
      caption = "Partner 1: nationaliteit")
  private String     natioP1 = "";
  @Field(type = FieldType.LABEL,
      caption = "Partner 2: nationaliteit")
  private String     natioP2 = "";
  @Field(customTypeClass = GbaComboBox.class,
      caption = "Partner 1: toegepast recht van ",
      required = true)
  @Select(containerDataSource = LandActueelContainer.class)
  private FieldValue rechtP1 = new FieldValue("6030");
  @Field(customTypeClass = GbaComboBox.class,
      caption = "Partner 2: toegepast recht van",
      required = true)
  @Select(containerDataSource = LandActueelContainer.class)
  private FieldValue rechtP2 = null;

  public Page50OmzettingBean1() {
  }

  public Page50OmzettingBean1(DossierPersoon p1, DossierPersoon p2) {

    this.p1 = p1;
    this.p2 = p2;

    setNatioP1(p1.getNationaliteitenOmschrijving() + " (" + p1.getNaam().getPred_adel_voorv_gesl_voorn() + ")");
    setNatioP2(p2.getNationaliteitenOmschrijving() + " (" + p2.getNaam().getPred_adel_voorv_gesl_voorn() + ")");
  }

  public String getNatioP1() {
    return natioP1;
  }

  public void setNatioP1(String natioP1) {
    this.natioP1 = natioP1;
  }

  public String getNatioP2() {
    return natioP2;
  }

  public void setNatioP2(String natioP2) {
    this.natioP2 = natioP2;
  }

  public DossierPersoon getP1() {
    return p1;
  }

  public void setP1(DossierPersoon p1) {
    this.p1 = p1;
  }

  public DossierPersoon getP2() {
    return p2;
  }

  public void setP2(DossierPersoon p2) {
    this.p2 = p2;
  }

  public FieldValue getRechtP1() {
    return rechtP1;
  }

  public void setRechtP1(FieldValue rechtP1) {
    this.rechtP1 = rechtP1;
  }

  public FieldValue getRechtP2() {
    return rechtP2;
  }

  public void setRechtP2(FieldValue rechtP2) {
    this.rechtP2 = rechtP2;
  }
}
