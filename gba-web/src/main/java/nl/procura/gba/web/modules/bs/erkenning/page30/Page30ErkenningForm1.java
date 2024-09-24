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

package nl.procura.gba.web.modules.bs.erkenning.page30;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.buttons.KennisbankButton;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.modules.bs.erkenning.page30.Page30ErkenningForm1.BeanForm1;
import nl.procura.gba.web.services.bs.algemeen.nationaliteit.DossierNationaliteit;
import nl.procura.gba.web.services.bs.erkenning.DossierErkenning;
import nl.procura.gba.web.services.zaken.kennisbank.KennisBankBron;
import nl.procura.gba.web.services.zaken.kennisbank.KennisBankDoel;
import nl.procura.commons.core.exceptions.NotSupportedException;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class Page30ErkenningForm1 extends GbaForm<BeanForm1> {

  private static final String NAT_MOEDER   = "natMoeder";
  private static final String NAT_ERKENNER = "natErkenner";
  private static final String VBT_MOEDER   = "vbtMoeder";
  private static final String VBT_ERKENNER = "vbtErkenner";

  public Page30ErkenningForm1(DossierErkenning erkenning) {

    setCaption("Nationaliteit moeder / erkenner");

    setColumnWidths("160px", "350px", "100px", "");

    setOrder(NAT_MOEDER, VBT_MOEDER, NAT_ERKENNER, VBT_ERKENNER);

    BeanForm1 bean = new BeanForm1();

    bean.setNatMoeder(erkenning.getMoeder().getNationaliteitenOmschrijving());
    bean.setVbtMoeder(erkenning.getMoeder().getVerblijfstitelOmschrijving());

    bean.setNatErkenner(erkenning.getErkenner().getNationaliteitenOmschrijving());
    bean.setVbtErkenner(erkenning.getErkenner().getVerblijfstitelOmschrijving());

    setBean(bean);
  }

  @Override
  public void afterSetColumn(Column column, com.vaadin.ui.Field field, Property property) {

    if (property.is(NAT_MOEDER)) {
      column.addComponent(getKennisBankButton(getNationaliteitMoeder()));
    }

    if (property.is(NAT_ERKENNER)) {
      column.addComponent(getKennisBankButton(getNationaliteitErkenner()));
    }
  }

  @Override
  public BeanForm1 getNewBean() {
    return new BeanForm1();
  }

  protected DossierNationaliteit getNationaliteitErkenner() {
    throw new NotSupportedException();
  }

  protected DossierNationaliteit getNationaliteitMoeder() {
    throw new NotSupportedException();
  }

  private KennisbankButton getKennisBankButton(DossierNationaliteit natio) {
    return new KennisbankButton(KennisBankBron.NATIONALITEIT, KennisBankDoel.NATIONALITEIT,
        natio.getNationaliteit());
  }

  @FormFieldFactoryBean(accessType = ElementType.FIELD)
  public class BeanForm1 implements Serializable {

    @Field(type = FieldType.LABEL,
        caption = "Nationaliteit moeder")
    private String natMoeder = "";

    @Field(type = FieldType.LABEL,
        caption = "Nationaliteit erkenner")
    private String natErkenner = "";

    @Field(type = FieldType.LABEL,
        caption = "Verblijfstitelcode")
    private String vbtMoeder = "";

    @Field(type = FieldType.LABEL,
        caption = "Verblijfstitelcode")
    private String vbtErkenner = "";

    public String getNatErkenner() {
      return natErkenner;
    }

    public void setNatErkenner(String nat2) {
      this.natErkenner = nat2;
    }

    public String getNatMoeder() {
      return natMoeder;
    }

    public void setNatMoeder(String nat1) {
      this.natMoeder = nat1;
    }

    public String getVbtErkenner() {
      return vbtErkenner;
    }

    public void setVbtErkenner(String vbt2) {
      this.vbtErkenner = vbt2;
    }

    public String getVbtMoeder() {
      return vbtMoeder;
    }

    public void setVbtMoeder(String vbt1) {
      this.vbtMoeder = vbt1;
    }
  }
}
