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

package nl.procura.gba.web.modules.bs.ontbinding.page20;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.containers.PlaatsContainer;
import nl.procura.gba.web.components.fields.GbaComboBox;
import nl.procura.gba.web.components.fields.UpperCaseField;
import nl.procura.gba.web.components.fields.YearField;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Select;
import nl.procura.vaadin.annotation.field.TextField;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page20OntbindingBean2 implements Serializable {

  public static final String BS_AKTE_NUMMER  = "bsAkteNummer";
  public static final String BRP_AKTE_NUMMER = "brpAkteNummer";
  public static final String AKTE_JAAR       = "akteJaar";
  public static final String AKTE_PLAATS     = "aktePlaats";

  @Field(customTypeClass = UpperCaseField.class,
      caption = "Aktenummer",
      required = true,
      width = "70px")
  @TextField(maxLength = 7)
  private String bsAkteNummer = "";

  @Field(customTypeClass = UpperCaseField.class,
      caption = "BRP-aktenummer",
      required = true,
      width = "70px")
  @TextField(maxLength = 7)
  private String brpAkteNummer = "";

  @Field(customTypeClass = YearField.class,
      caption = "Jaar",
      width = "70px",
      required = true)
  @TextField(maxLength = 4)
  private String akteJaar = "";

  @Field(customTypeClass = GbaComboBox.class,
      caption = "Aktegemeente",
      width = "200px",
      required = true)
  @Select(containerDataSource = PlaatsContainer.class)
  private FieldValue aktePlaats = null;

  public String getAkteJaar() {
    return akteJaar;
  }

  public void setAkteJaar(String akteJaar) {
    this.akteJaar = akteJaar;
  }

  public FieldValue getAktePlaats() {
    return aktePlaats;
  }

  public void setAktePlaats(FieldValue aktePlaats) {
    this.aktePlaats = aktePlaats;
  }

  public String getBrpAkteNummer() {
    return brpAkteNummer;
  }

  public void setBrpAkteNummer(String brpAkteNummer) {
    this.brpAkteNummer = brpAkteNummer;
  }

  public String getBsAkteNummer() {
    return bsAkteNummer;
  }

  public void setBsAkteNummer(String bsAkteNummer) {
    this.bsAkteNummer = bsAkteNummer;
  }
}
