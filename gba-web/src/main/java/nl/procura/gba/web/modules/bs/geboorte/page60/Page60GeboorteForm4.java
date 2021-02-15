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

import nl.procura.gba.web.components.buttons.KennisbankButton;
import nl.procura.gba.web.components.layouts.BeanHandler;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.bs.geboorte.DossierGeboorte;
import nl.procura.gba.web.services.zaken.kennisbank.KennisBankBron;
import nl.procura.gba.web.services.zaken.kennisbank.KennisBankDoel;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

import lombok.Data;

public class Page60GeboorteForm4 extends GbaForm<Page60GeboorteForm4.Page60GeboorteBean4> {

  public static final String NAT = "nat";
  public static final String VBT = "vbt";
  public static final String VB  = "vb";

  private final DossierGeboorte geboorte;

  public Page60GeboorteForm4(DossierGeboorte geboorte) {

    this.geboorte = geboorte;

    setColumnWidths("200px", "", "100px", "300px");

    setOrder(NAT, VBT, VB);

    Page60GeboorteBean4 bean = new Page60GeboorteBean4();
    bean.setNat(geboorte.getMoeder().getNationaliteitenOmschrijving());
    bean.setVbt(geboorte.getMoeder().getVerblijfstitelOmschrijving());
    bean.setVb(geboorte.getMoeder().getLand().getDescription());

    BeanHandler.trim(bean);

    setBean(bean);
  }

  @Override
  public void afterSetColumn(Column column, com.vaadin.ui.Field field, Property property) {

    if (property.is(VB)) {
      column.setColspan(3);
      column.addComponent(new KennisbankButton(KennisBankBron.LAND, KennisBankDoel.AFSTAMMING,
          geboorte.getMoeder().getLand()));
    }

    if (property.is(NAT)) {
      column.addComponent(new KennisbankButton(KennisBankBron.NATIONALITEIT, KennisBankDoel.AFSTAMMING,
          geboorte.getMoeder().getNationaliteiten()));
    }

    super.afterSetColumn(column, field, property);
  }

  @Override
  public Page60GeboorteBean4 getNewBean() {
    return new Page60GeboorteBean4();
  }

  @Data
  @FormFieldFactoryBean(accessType = ElementType.FIELD)
  public static class Page60GeboorteBean4 implements Serializable {

    @Field(type = FieldType.LABEL,
        caption = "Nationaliteit moeder")
    private String nat = "";

    @Field(type = FieldType.LABEL,
        caption = "Verblijfstitelcode")
    private String vbt = "";

    @Field(type = FieldType.LABEL,
        caption = "Verblijfplaats moeder in")
    private String vb = "";
  }
}
