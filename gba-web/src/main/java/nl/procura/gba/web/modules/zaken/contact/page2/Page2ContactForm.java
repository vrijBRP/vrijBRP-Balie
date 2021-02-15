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

package nl.procura.gba.web.modules.zaken.contact.page2;

import static nl.procura.gba.web.modules.zaken.contact.page2.Page2ContactBean.LAND;
import static nl.procura.gba.web.modules.zaken.contact.page2.Page2ContactBean.VELD;
import static nl.procura.gba.web.services.zaken.contact.ContactgegevensService.EMAIL;
import static nl.procura.gba.web.services.zaken.contact.ContactgegevensService.TEL_MOBIEL;
import static nl.procura.standard.Globalfunctions.astr;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.data.Validator;
import com.vaadin.ui.Field;

import nl.procura.gba.web.common.tables.GbaTables;
import nl.procura.gba.web.common.validators.GbaEmailValidator;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.components.validators.GbaMobilePhoneValidator;
import nl.procura.gba.web.components.validators.VaadinValidator;
import nl.procura.gba.web.services.zaken.contact.ContactgegevensService;
import nl.procura.gba.web.services.zaken.contact.PlContactgegeven;

public class Page2ContactForm extends GbaForm<Page2ContactBean> {

  private PlContactgegeven contactgegeven;

  public Page2ContactForm(PlContactgegeven contactgegeven) {
    setContactgegeven(contactgegeven);
    setOrder(VELD, LAND);
    setColumnWidths("170px", "");
    Page2ContactBean bean = new Page2ContactBean(getContactgegeven().getAant());
    bean.setVeld(getContactgegeven().getAant());
    bean.setLand(GbaTables.LAND.get(getContactgegeven().getCountry()));
    setBean(bean);
  }

  @Override
  public void commit() throws SourceException, Validator.InvalidValueException {

    boolean isBlank = StringUtils.isBlank(astr(getField(Page2ContactBean.VELD).getValue()));
    getField(LAND).setRequired(!isBlank);

    super.commit();
  }

  @Override
  public void afterSetBean() {
    if (getContactgegeven().getContactgegeven().isGegeven(EMAIL)) {
      getField(VELD).addValidator(VaadinValidator.of(new GbaEmailValidator()));

    } else if (getContactgegeven().getContactgegeven().isGegeven(TEL_MOBIEL)) {
      getField(VELD).addValidator(new GbaMobilePhoneValidator());
    }
    super.afterSetBean();
  }

  public PlContactgegeven getContactgegeven() {
    return contactgegeven;
  }

  public void setContactgegeven(PlContactgegeven contactgegeven) {
    this.contactgegeven = contactgegeven;
  }

  @Override
  public Field newField(Field field, Property property) {
    if (property.is(VELD)) {
      field.setCaption(getContactgegeven().getContactgegeven().getOms());
    }

    if (property.is(LAND)) {
      field.setVisible(getContactgegeven().getContactgegeven().isGegeven(ContactgegevensService.TEL_MOBIEL_BL));
    }
    return super.newField(field, property);
  }
}
