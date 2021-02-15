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

import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.modules.zaken.contact.page1.Page1Contact;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.zaken.contact.ContactgegevensService;
import nl.procura.gba.web.services.zaken.contact.PlContactgegeven;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;

public class Page2Contact extends NormalPageTemplate {

  private Page2ContactForm form;

  public Page2Contact(Record record) {

    setSpacing(true);

    addButton(buttonPrev);
    addButton(buttonSave);

    PlContactgegeven gegeven = (PlContactgegeven) record.getObject();
    if (gegeven.getContactgegeven().isGegeven(ContactgegevensService.TEL_MOBIEL_BL)) {
      if (Services.getInstance().getSmsService().isSmsServiceActive()) {
        setInfo("", "Buitenlandse mobiele nummers worden niet gevalideerd. " +
            "</br>Bij ontbreken van een geldig Nederlands mobiel nummer wordt dit nummer gebruikt voor de SMS berichten.");
      }
    }

    form = new Page2ContactForm(gegeven);
    addComponent(form);
  }

  public Page2ContactForm getForm() {
    return form;
  }

  public void setForm(Page2ContactForm form) {
    this.form = form;
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goToPage(Page1Contact.class);
  }

  @Override
  public void onSave() {

    getForm().commit();

    PlContactgegeven g = getForm().getContactgegeven();
    ContactgegevensService contact = getApplication().getServices().getContactgegevensService();
    FieldValue land = getForm().getBean().getLand();
    contact.setContactWaarde(g.getAnr(), g.getBsn(), g.getContactgegeven().getGegeven(),
        getForm().getBean().getVeld(), land != null ? land.getLongValue() : -1);

    onPreviousPage();

  }
}
