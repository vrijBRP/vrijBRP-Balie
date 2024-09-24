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

package nl.procura.gba.web.modules.bs.naamskeuze.page30;

import java.io.Serializable;
import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.buttons.KennisbankButton;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.modules.bs.naamskeuze.page30.Page30NaamskeuzeForm1.BeanForm1;
import nl.procura.gba.web.services.bs.algemeen.nationaliteit.DossierNationaliteit;
import nl.procura.gba.web.services.bs.naamskeuze.DossierNaamskeuze;
import nl.procura.gba.web.services.zaken.kennisbank.KennisBankBron;
import nl.procura.gba.web.services.zaken.kennisbank.KennisBankDoel;
import nl.procura.commons.core.exceptions.NotSupportedException;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class Page30NaamskeuzeForm1 extends GbaForm<BeanForm1> {

  private static final String NAT_MOEDER  = "natMoeder";
  private static final String NAT_PARTNER = "natPartner";
  private static final String VBT_MOEDER  = "vbtMoeder";
  private static final String VBT_PARTNER = "vbtPartner";

  public Page30NaamskeuzeForm1(DossierNaamskeuze naamskeuze) {

    setCaption("Nationaliteit moeder / partner");
    setColumnWidths("160px", "350px", "100px", "");
    setOrder(NAT_MOEDER, VBT_MOEDER, NAT_PARTNER, VBT_PARTNER);

    BeanForm1 bean = new BeanForm1();
    bean.setNatMoeder(naamskeuze.getMoeder().getNationaliteitenOmschrijving());
    bean.setVbtMoeder(naamskeuze.getMoeder().getVerblijfstitelOmschrijving());
    bean.setNatPartner(naamskeuze.getPartner().getNationaliteitenOmschrijving());
    bean.setVbtPartner(naamskeuze.getPartner().getVerblijfstitelOmschrijving());

    setBean(bean);
  }

  @Override
  public void afterSetColumn(Column column, com.vaadin.ui.Field field, Property property) {

    if (property.is(NAT_MOEDER)) {
      column.addComponent(getKennisBankButton(getNationaliteitMoeder()));
    }

    if (property.is(NAT_PARTNER)) {
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
        caption = "Nationaliteit partner")
    private String natPartner = "";

    @Field(type = FieldType.LABEL,
        caption = "Verblijfstitelcode")
    private String vbtMoeder = "";

    @Field(type = FieldType.LABEL,
        caption = "Verblijfstitelcode")
    private String vbtPartner = "";

    public String getNatMoeder() {
      return natMoeder;
    }

    public void setNatMoeder(String nat1) {
      this.natMoeder = nat1;
    }

    public String getVbtMoeder() {
      return vbtMoeder;
    }

    public void setVbtMoeder(String vbt1) {
      this.vbtMoeder = vbt1;
    }

    public String getNatPartner() {
      return natPartner;
    }

    public void setNatPartner(String natPartner) {
      this.natPartner = natPartner;
    }

    public String getVbtPartner() {
      return vbtPartner;
    }

    public void setVbtPartner(String vbtPartner) {
      this.vbtPartner = vbtPartner;
    }
  }
}
