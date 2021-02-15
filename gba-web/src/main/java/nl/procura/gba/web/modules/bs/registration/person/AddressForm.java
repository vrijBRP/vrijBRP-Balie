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

package nl.procura.gba.web.modules.bs.registration.person;

import java.lang.annotation.ElementType;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;

import lombok.Data;

public class AddressForm extends GbaForm<AddressForm.AddressBean> {

  public AddressForm(String address) {
    setColumnWidths("220px", "");
    setOrder(AddressBean.F_ADDRESS);

    final AddressBean bean = new AddressBean();
    bean.setAddress(address);
    setBean(bean);
  }

  @Data
  @FormFieldFactoryBean(accessType = ElementType.FIELD)

  public static class AddressBean {

    public static final String F_ADDRESS = "address";

    @Field(type = Field.FieldType.LABEL, caption = "Adres")
    private String address;
  }
}
