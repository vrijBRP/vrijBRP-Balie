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

package nl.procura.gba.web.modules.zaken.vog.page2;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page2VogBean4 implements Serializable {

  public static final String SCREENING      = "screening";
  public static final String PROFIEL        = "profiel";
  public static final String FUNCTIEGEBIED  = "functiegebied";
  public static final String OMSTANDIGHEDEN = "omstandigheden";

  @Field(type = FieldType.TEXT_FIELD,
      caption = "Screening")
  private String screening      = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Profiel")
  private String profiel        = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Functiegebieden")
  private String functiegebied  = "";
  @Field(type = FieldType.TEXT_FIELD,
      caption = "Omstandigheden")
  private String omstandigheden = "";

  public String getFunctiegebied() {
    return functiegebied;
  }

  public void setFunctiegebied(String functiegebied) {
    this.functiegebied = functiegebied;
  }

  public String getOmstandigheden() {
    return omstandigheden;
  }

  public void setOmstandigheden(String omstandigheden) {
    this.omstandigheden = omstandigheden;
  }

  public String getProfiel() {
    return profiel;
  }

  public void setProfiel(String profiel) {
    this.profiel = profiel;
  }

  public String getScreening() {
    return screening;
  }

  public void setScreening(String screening) {
    this.screening = screening;
  }
}
