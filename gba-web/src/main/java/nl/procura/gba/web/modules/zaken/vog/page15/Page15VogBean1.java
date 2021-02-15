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

package nl.procura.gba.web.modules.zaken.vog.page15;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.modules.zaken.vog.BurgemeesterAdviesContainer;
import nl.procura.gba.web.services.zaken.vog.BurgemeesterAdvies;
import nl.procura.vaadin.annotation.field.*;
import nl.procura.vaadin.component.container.NLBooleanContainer;
import nl.procura.vaadin.component.field.ProTextArea;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page15VogBean1 implements Serializable {

  public static final String BIJZONDER          = "bijzonder";
  public static final String BIJZONDERTOEL      = "bijzonderToel";
  public static final String PERSIST            = "persist";
  public static final String PERSISTTOEL        = "persistToel";
  public static final String COVOGADVIES        = "covogAdvies";
  public static final String COVOGADVIESTOEL    = "covogAdviesToel";
  public static final String ALGTOELICHTING     = "algToelichting";
  public static final String ALGTOELICHTINGTOEL = "algToelichtingToel";
  public static final String ADVIES             = "advies";

  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Bijzonderheden geconstateerd",
      width = "400px")
  @Select(containerDataSource = NLBooleanContainer.class,
      itemCaptionPropertyId = NLBooleanContainer.JA_NEE,
      nullSelectionAllowed = false)
  @Immediate()
  private boolean            bijzonder          = false;
  @Field(customTypeClass = ProTextArea.class,
      width = "400px",
      required = true,
      requiredError = "Toelichting bij \"bijzonderheden geconstateerd\" verplicht.")
  @TextArea(rows = 3)
  private String             bijzonderToel      = "";
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Persisteren",
      width = "400px")
  @Select(containerDataSource = NLBooleanContainer.class,
      itemCaptionPropertyId = NLBooleanContainer.JA_NEE,
      nullSelectionAllowed = false)
  @Immediate()
  private boolean            persist            = false;
  @Field(customTypeClass = ProTextArea.class,
      width = "400px",
      required = true,
      requiredError = "Toelichting bij \"Persisteren\" verplicht.")
  @TextArea(rows = 3)
  private String             persistToel        = "";
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "COVOG-advies gevraagd",
      width = "400px")
  @Select(containerDataSource = NLBooleanContainer.class,
      itemCaptionPropertyId = NLBooleanContainer.JA_NEE,
      nullSelectionAllowed = false)
  @Immediate()
  private boolean            covogAdvies        = false;
  @Field(customTypeClass = ProTextArea.class,
      width = "400px",
      required = true,
      requiredError = "Toelichting bij \"COVOG-advies gevraagd\" verplicht.")
  @TextArea(rows = 3)
  private String             covogAdviesToel    = "";
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Algemene toelichting",
      width = "400px")
  @Select(containerDataSource = NLBooleanContainer.class,
      itemCaptionPropertyId = NLBooleanContainer.JA_NEE,
      nullSelectionAllowed = false)
  @Immediate()
  private boolean            algToelichting     = false;
  @Field(customTypeClass = ProTextArea.class,
      width = "400px",
      required = true,
      requiredError = "Toelichting bij \"Algemene toelichting\" verplicht.")
  @TextArea(rows = 3)
  private String             algToelichtingToel = "";
  @Field(customTypeClass = GbaNativeSelect.class,
      caption = "Burgemeestersadvies",
      width = "400px")
  @Select(containerDataSource = BurgemeesterAdviesContainer.class,
      nullSelectionAllowed = false)
  private BurgemeesterAdvies advies             = BurgemeesterAdvies.ONBEKEND;

  public BurgemeesterAdvies getAdvies() {
    return advies;
  }

  public void setAdvies(BurgemeesterAdvies advies) {
    this.advies = advies;
  }

  public String getAlgToelichtingToel() {
    return algToelichtingToel;
  }

  public void setAlgToelichtingToel(String algToelichtingToel) {
    this.algToelichtingToel = algToelichtingToel;
  }

  public String getBijzonderToel() {
    return bijzonderToel;
  }

  public void setBijzonderToel(String bijzonderToel) {
    this.bijzonderToel = bijzonderToel;
  }

  public String getCovogAdviesToel() {
    return covogAdviesToel;
  }

  public void setCovogAdviesToel(String covogAdviesToel) {
    this.covogAdviesToel = covogAdviesToel;
  }

  public String getPersistToel() {
    return persistToel;
  }

  public void setPersistToel(String persistToel) {
    this.persistToel = persistToel;
  }

  public boolean isAlgToelichting() {
    return algToelichting;
  }

  public void setAlgToelichting(boolean algToelichting) {
    this.algToelichting = algToelichting;
  }

  public boolean isBijzonder() {
    return bijzonder;
  }

  public void setBijzonder(boolean bijzonder) {
    this.bijzonder = bijzonder;
  }

  public boolean isCovogAdvies() {
    return covogAdvies;
  }

  public void setCovogAdvies(boolean covogAdvies) {
    this.covogAdvies = covogAdvies;
  }

  public boolean isPersist() {
    return persist;
  }

  public void setPersist(boolean persist) {
    this.persist = persist;
  }
}
