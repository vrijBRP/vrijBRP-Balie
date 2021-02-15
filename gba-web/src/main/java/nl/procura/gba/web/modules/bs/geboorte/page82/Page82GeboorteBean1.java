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

package nl.procura.gba.web.modules.bs.geboorte.page82;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.services.bs.erkenning.ErkenningsType;
import nl.procura.gba.web.services.bs.geboorte.DossierGeboorte;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;

@FormFieldFactoryBean(accessType = ElementType.FIELD)
public class Page82GeboorteBean1 implements Serializable {

  public static final String ERKENNINGS_TYPE = "erkenningsType";

  @Field(type = FieldType.LABEL,
      caption = "Soort erkenning")
  private ErkenningsType erkenningsType = ErkenningsType.ONBEKEND;

  public Page82GeboorteBean1() {
  }

  public Page82GeboorteBean1(DossierGeboorte zaakDossier) {
    setErkenningsType(zaakDossier.getErkenningsType());
  }

  public ErkenningsType getErkenningsType() {
    return erkenningsType;
  }

  public void setErkenningsType(ErkenningsType erkenningsType) {
    this.erkenningsType = erkenningsType;
  }
}
