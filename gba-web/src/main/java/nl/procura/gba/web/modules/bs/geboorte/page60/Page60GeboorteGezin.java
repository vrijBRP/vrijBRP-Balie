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

package nl.procura.gba.web.modules.bs.geboorte.page60;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.bs.geboorte.GezinssituatieType;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;

import lombok.Data;

public class Page60GeboorteGezin extends GbaForm<Page60GeboorteGezin.Page60GeboorteBean1> {

  public static final String GEZIN = "gezin";

  public Page60GeboorteGezin() {

    setCaption("Afstammingsrecht");
    setColumnWidths("200px", "");
    setOrder(GEZIN);

    setCaptionAndOrder();
  }

  @Override
  public Page60GeboorteBean1 getNewBean() {
    return new Page60GeboorteBean1();
  }

  public void setCaptionAndOrder() {
  }

  @Data
  @FormFieldFactoryBean(accessType = ElementType.FIELD)
  public static class Page60GeboorteBean1 implements Serializable {

    @Field(type = FieldType.LABEL,
        caption = "Gezinssituatie")
    private GezinssituatieType gezin = GezinssituatieType.ONBEKEND;

    public Page60GeboorteBean1() {
    }

    public Page60GeboorteBean1(GezinssituatieType gezinssituatie) {
      setGezin(gezinssituatie);
    }
  }
}
