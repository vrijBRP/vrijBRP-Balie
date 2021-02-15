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

package nl.procura.gba.web.modules.zaken.vog.page12;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.services.zaken.vog.VogDoel;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Immediate;
import nl.procura.vaadin.annotation.field.TextArea;
import nl.procura.vaadin.component.field.ProTextArea;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page12VogBean1 implements Serializable {

  public static final String DOEL         = "doel";
  public static final String OMSCHRIJVING = "omschrijving";
  public static final String FUNCTIE      = "functie";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Doel",
      required = true,
      width = "400px")
  @Immediate()
  private VogDoel doel         = null;
  @Field(customTypeClass = ProTextArea.class,
      caption = "Omschrijving",
      required = true,
      width = "400px",
      visible = false)
  @TextArea(maxLength = 95,
      rows = 5)
  private String  omschrijving = "";
  @Field(customTypeClass = ProTextArea.class,
      caption = "Functie",
      required = true,
      width = "400px",
      visible = false)
  @TextArea(maxLength = 95,
      rows = 5)
  private String  functie      = "";

  public VogDoel getDoel() {
    return doel;
  }

  public void setDoel(VogDoel doel) {
    this.doel = doel;
  }

  public String getFunctie() {
    return functie;
  }

  public void setFunctie(String functie) {
    this.functie = functie;
  }

  public String getOmschrijving() {
    return omschrijving;
  }

  public void setOmschrijving(String omschrijving) {
    this.omschrijving = omschrijving;
  }
}
