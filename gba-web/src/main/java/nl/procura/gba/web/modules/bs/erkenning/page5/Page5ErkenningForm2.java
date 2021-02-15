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

import nl.procura.gba.web.components.layouts.form.ReadOnlyForm;
import nl.procura.gba.web.modules.bs.erkenning.page5.Page5ErkenningForm2.Page5ErkenningBean2;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;

public class Page5ErkenningForm2 extends ReadOnlyForm<Page5ErkenningBean2> {

  private static final String LAND     = "land";
  private static final String NAT      = "nat";
  private static final String LEEFTIJD = "leeftijd";

  public Page5ErkenningForm2() {

    setCaption("Gedeelde gegevens tussen de kinderen");

    setColumnWidths("160px", "");

    setOrder(LAND, NAT, LEEFTIJD);

    setBean(new Page5ErkenningBean2());
  }

  @Override
  public Page5ErkenningBean2 getNewBean() {
    return new Page5ErkenningBean2();
  }

  @FormFieldFactoryBean(accessType = ElementType.FIELD)
  public class Page5ErkenningBean2 implements Serializable {

    @Field(type = FieldType.LABEL,
        caption = "Woonland")
    private String land = "";

    @Field(type = FieldType.LABEL,
        caption = "Nationaliteiten")
    private String nat = "";

    @Field(type = FieldType.LABEL,
        caption = "Leeftijdscategorieën")
    private String leeftijd = "";

    public String getLand() {
      return land;
    }

    public void setLand(String land) {
      this.land = land;
    }

    public String getLeeftijd() {
      return leeftijd;
    }

    public void setLeeftijd(String leeftijd) {
      this.leeftijd = leeftijd;
    }

    public String getNat() {
      return nat;
    }

    public void setNat(String nat) {
      this.nat = nat;
    }
  }
}
