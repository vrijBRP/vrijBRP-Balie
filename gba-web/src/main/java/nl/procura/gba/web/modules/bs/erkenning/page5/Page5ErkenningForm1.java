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

package nl.procura.gba.web.modules.bs.erkenning.page5;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.modules.bs.erkenning.page5.Page5ErkenningForm1.Page5ErkenningBean1;
import nl.procura.gba.web.services.bs.erkenning.DossierErkenning;
import nl.procura.gba.web.services.bs.erkenning.ErkenningsType;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Immediate;
import nl.procura.vaadin.annotation.field.Select;

public class Page5ErkenningForm1 extends GbaForm<Page5ErkenningBean1> {

  public static final String  ERKENNINGS_TYPE = "erkenningsType";
  private static final String MOEDER          = "moeder";

  public Page5ErkenningForm1(DossierErkenning erkenning) {

    setCaption("Erkenning");

    setColumnWidths("120px", "");

    setOrder(MOEDER, ERKENNINGS_TYPE);

    Page5ErkenningBean1 bean = new Page5ErkenningBean1();

    bean.setMoeder(erkenning.getMoeder().getNaam().getNaam_naamgebruik_eerste_voornaam());

    if (erkenning.getErkenningsType() != ErkenningsType.ONBEKEND) {
      bean.setErkenningsType(erkenning.getErkenningsType());
    }

    setBean(bean);

    getField(ERKENNINGS_TYPE).setReadOnly(erkenning.isGelijkMetGeboorte());
  }

  @Override
  public Page5ErkenningBean1 getNewBean() {
    return new Page5ErkenningBean1();
  }

  @FormFieldFactoryBean(accessType = ElementType.FIELD)
  public class Page5ErkenningBean1 implements Serializable {

    @Field(type = FieldType.LABEL,
        caption = "Moeder")
    private String moeder = "";

    @Field(customTypeClass = GbaNativeSelect.class,
        caption = "Type erkenning",
        required = true)
    @Select(containerDataSource = ErkenningTypeContainer.class)
    @Immediate()
    private ErkenningsType erkenningsType = null;

    public ErkenningsType getErkenningsType() {
      return erkenningsType;
    }

    public void setErkenningsType(ErkenningsType erkenningType) {
      this.erkenningsType = erkenningType;
    }

    public String getMoeder() {
      return moeder;
    }

    public void setMoeder(String moeder) {
      this.moeder = moeder;
    }
  }
}
