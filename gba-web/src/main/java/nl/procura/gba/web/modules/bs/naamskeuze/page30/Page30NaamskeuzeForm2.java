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

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.modules.bs.naamskeuze.page30.Page30NaamskeuzeForm2.BeanForm2;
import nl.procura.gba.web.services.bs.algemeen.functies.BsNatioUtils;
import nl.procura.gba.web.services.bs.naamskeuze.DossierNaamskeuze;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;

public class Page30NaamskeuzeForm2 extends GbaForm<BeanForm2> {

  private static final String NAT_KIND = "natKind";

  public Page30NaamskeuzeForm2(DossierNaamskeuze naamskeuze) {
    setCaption("Bestaande nationaliteit(en) van het kind");
    setColumnWidths("160px", "");
    setOrder(NAT_KIND);

    BeanForm2 bean = new BeanForm2();
    bean.setNatKind(BsNatioUtils.getNationaliteitOmschrijving(naamskeuze.getKinderen()));
    setBean(bean);
  }

  @Override
  public BeanForm2 getNewBean() {
    return new BeanForm2();
  }

  @FormFieldFactoryBean(accessType = ElementType.FIELD)
  public class BeanForm2 implements Serializable {

    @Field(type = FieldType.LABEL,
        caption = "Nationaliteit(en) kinderen")
    private String natKind = "";

    public String getNatKind() {
      return natKind;
    }

    public void setNatKind(String natKind) {
      this.natKind = natKind;
    }
  }
}
