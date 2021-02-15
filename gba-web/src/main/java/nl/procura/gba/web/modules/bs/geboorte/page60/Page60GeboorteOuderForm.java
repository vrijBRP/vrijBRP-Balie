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
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.geboorte.DossierGeboorte;
import nl.procura.gba.web.services.zaken.kennisbank.KennisBankBron;
import nl.procura.gba.web.services.zaken.kennisbank.KennisBankDoel;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

import lombok.Data;

public class Page60GeboorteOuderForm extends GbaForm<Page60GeboorteOuderForm.Page60GeboorteOuderBean> {

  private static final String NAT_MOEDER = "natMoeder";
  private static final String VBT_MOEDER = "vbtMoeder";
  private static final String NAT_VADER  = "natVader";
  private static final String VBT_VADER  = "vbtVader";
  private final boolean       kennisbank;
  private DossierPersoon      moeder;
  private DossierPersoon      vader;

  public Page60GeboorteOuderForm(boolean kennisbank) {

    this.kennisbank = kennisbank;

    setColumnWidths("200px", "371px", "100px", "");
    setOrder(NAT_MOEDER, VBT_MOEDER, NAT_VADER, VBT_VADER);
  }

  @Override
  public void afterSetColumn(Column column, com.vaadin.ui.Field field, Property property) {

    if (kennisbank) {
      if (property.is(NAT_MOEDER) && moeder != null) {
        column.addComponent(new KennisbankButton(KennisBankBron.NATIONALITEIT, KennisBankDoel.NATIONALITEIT,
            moeder.getNationaliteiten()));
      }

      if (property.is(NAT_VADER) && vader != null) {
        column.addComponent(new KennisbankButton(KennisBankBron.NATIONALITEIT, KennisBankDoel.NATIONALITEIT,
            vader.getNationaliteiten()));
      }
    }

    super.afterSetColumn(column, field, property);
  }

  @Override
  public Page60GeboorteOuderBean getNewBean() {
    return new Page60GeboorteOuderBean();
  }

  public void setGeboorte(DossierGeboorte geboorte) {

    Page60GeboorteOuderBean bean = new Page60GeboorteOuderBean();

    bean.setNatMoeder(geboorte.getMoeder().getNationaliteitenOmschrijving());
    bean.setVbtMoeder(geboorte.getMoeder().getVerblijfstitelOmschrijving());

    if (geboorte.getVader().isVolledig()) {

      bean.setNatVader(geboorte.getVader().getNationaliteitenOmschrijving());
      bean.setVbtVader(geboorte.getVader().getVerblijfstitelOmschrijving());
    }

    setMoeder(geboorte.getMoeder());
    setVader(geboorte.getVader());

    BeanHandler.trim(bean);

    setBean(bean);
  }

  public void setMoeder(DossierPersoon moeder) {
    this.moeder = moeder;
  }

  public void setVader(DossierPersoon vader) {
    this.vader = vader;
  }

  @Data
  @FormFieldFactoryBean(accessType = ElementType.FIELD)
  public static class Page60GeboorteOuderBean implements Serializable {

    @Field(type = FieldType.LABEL,
        caption = "Nationaliteit moeder")
    private String natMoeder = "";

    @Field(type = FieldType.LABEL,
        caption = "Nationaliteit vader / duo-moeder")
    private String natVader = "Niet van toepassing";

    @Field(type = FieldType.LABEL,
        caption = "Verblijfstitelcode")
    private String vbtMoeder = "";

    @Field(type = FieldType.LABEL,
        caption = "Verblijfstitelcode")
    private String vbtVader = "Niet van toepassing";

    public Page60GeboorteOuderBean() {
    }
  }
}
