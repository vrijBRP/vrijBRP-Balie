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

package nl.procura.gba.web.modules.bs.naamskeuze.page5;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.modules.bs.naamskeuze.page5.Page5NaamskeuzeForm1.Page5NaamskeuzeBean1;
import nl.procura.gba.web.services.bs.naamskeuze.DossierNaamskeuze;
import nl.procura.gba.web.services.bs.naamskeuze.NaamskeuzeType;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Immediate;
import nl.procura.vaadin.annotation.field.Select;

public class Page5NaamskeuzeForm1 extends GbaForm<Page5NaamskeuzeBean1> {

  public static final String  NAAMSKEUZE_TYPE = "naamskeuzesType";
  private static final String MOEDER          = "moeder";

  public Page5NaamskeuzeForm1(DossierNaamskeuze naamskeuze) {
    setCaption("Naamskeuze");
    setColumnWidths("120px", "");
    setOrder(MOEDER, NAAMSKEUZE_TYPE);

    Page5NaamskeuzeBean1 bean = new Page5NaamskeuzeBean1();
    bean.setMoeder(naamskeuze.getMoeder().getNaam().getNaam_naamgebruik_eerste_voornaam());

    if (naamskeuze.getDossierNaamskeuzeType() != NaamskeuzeType.ONBEKEND) {
      bean.setNaamskeuzesType(naamskeuze.getDossierNaamskeuzeType());
    }

    setBean(bean);
  }

  @Override
  public Page5NaamskeuzeBean1 getNewBean() {
    return new Page5NaamskeuzeBean1();
  }

  @FormFieldFactoryBean(accessType = ElementType.FIELD)
  public class Page5NaamskeuzeBean1 implements Serializable {

    @Field(type = FieldType.LABEL,
        caption = "Moeder")
    private String moeder = "";

    @Field(customTypeClass = GbaNativeSelect.class,
        caption = "Type naamskeuze",
        required = true)
    @Select(containerDataSource = NaamskeuzeTypeContainer.class)
    @Immediate()
    private NaamskeuzeType naamskeuzesType = null;

    public NaamskeuzeType getNaamskeuzesType() {
      return naamskeuzesType;
    }

    public void setNaamskeuzesType(NaamskeuzeType naamskeuzeType) {
      this.naamskeuzesType = naamskeuzeType;
    }

    public String getMoeder() {
      return moeder;
    }

    public void setMoeder(String moeder) {
      this.moeder = moeder;
    }
  }
}
