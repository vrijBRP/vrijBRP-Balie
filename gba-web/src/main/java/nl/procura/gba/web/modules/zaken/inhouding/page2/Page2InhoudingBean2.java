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

package nl.procura.gba.web.modules.zaken.inhouding.page2;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.fields.GbaTextField;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.TextArea;
import nl.procura.vaadin.component.field.ProTextArea;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page2InhoudingBean2 implements Serializable {

  public static final String NUMMERPV           = "nummerPv";
  public static final String VERZOEKORGAANVRAAG = "verzoekOrgAanvraag";

  @Field(customTypeClass = GbaTextField.class,
      caption = "Nummer proces-verbaal",
      required = true)
  private String nummerPv           = "";
  @Field(customTypeClass = ProTextArea.class,
      caption = "Verzoek originele aanvraag",
      width = "400px")
  @TextArea(rows = 5)
  private String verzoekOrgAanvraag = "";

  public String getNummerPv() {
    return nummerPv;
  }

  public void setNummerPv(String nummerPv) {
    this.nummerPv = nummerPv;
  }

  public String getVerzoekOrgAanvraag() {
    return verzoekOrgAanvraag;
  }

  public void setVerzoekOrgAanvraag(String verzoekOrgAanvraag) {
    this.verzoekOrgAanvraag = verzoekOrgAanvraag;
  }
}
