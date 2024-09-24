/*
 * Copyright 2024 - 2025 Procura B.V.
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

package nl.procura.gba.web.modules.zaken.contact.page1;

import static nl.procura.commons.core.exceptions.ProExceptionSeverity.INFO;
import static nl.procura.standard.Globalfunctions.along;

import org.apache.commons.lang3.StringUtils;

import nl.procura.commons.core.exceptions.ProException;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.modules.zaken.contact.ContactTabel;
import nl.procura.gba.web.modules.zaken.contact.page2.Page2Contact;
import nl.procura.gba.web.services.beheer.parameter.ParameterConstant;
import nl.procura.gba.web.services.zaken.contact.Contact;
import nl.procura.gba.web.services.zaken.contact.ContactStatusListener;
import nl.procura.gba.web.services.zaken.contact.ContactVerplichtMate;
import nl.procura.vaadin.component.label.H2;
import nl.procura.vaadin.component.layout.page.pageEvents.AfterReturn;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;

public class Page1Contact extends NormalPageTemplate {

  private final Contact               contact;
  private final ContactStatusListener succesListener;
  private final String                warning;
  private ContactTabel                table = null;

  public Page1Contact(Contact contact, ContactStatusListener succesListener, String warning) {
    this.contact = contact;
    this.succesListener = succesListener;
    this.warning = warning;
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      table = new ContactTabel(contact) {

        @Override
        public void onClick(Record record) {
          selectRecord(record);
        }

        @Override
        public void setColumns() {
          setSelectable(true);
          super.setColumns();
        }
      };

      setSpacing(true);

      H2 h2 = new H2("Contactgegevens");

      buttonPrev.setCaption("Overslaan (F1)");
      buttonNext.setCaption("Akkoord (F2)");

      addButton(buttonPrev);
      addButton(buttonNext);
      addButton(buttonClose);
      getButtonLayout().addComponent(h2, getButtonLayout().getComponentIndex(buttonPrev));
      getButtonLayout().setExpandRatio(h2, 1f);
      getButtonLayout().setWidth("100%");

      String infoMsg = "Om verder te kunnen zullen de contactgegevens van de aanvrager moeten worden vastgesteld. "
          + "<br/>Vul minimaal één contactgegeven in.";

      if (StringUtils.isNotBlank(warning)) {
        infoMsg = warning;
      }

      setInfo("Contactgegevens van " + contact.getName(),
          infoMsg);

      addComponent(table);

      ContactVerplichtMate verplichtMate = ContactVerplichtMate.get(
          along(getApplication().getParmValue(ParameterConstant.CONTACT_VERPLICHT)));
      buttonNext.setVisible(
          succesListener != null && verplichtMate == ContactVerplichtMate.NIET_VERPLICHT_WEL_TONEN);

    } else if (event.isEvent(AfterReturn.class)) {

      table.resetGegevens();
    }

    super.event(event);
  }

  @Override
  public void onClose() {
    getWindow().closeWindow();
  }

  @Override
  public void onEnter() {

    if (table.getRecord() != null) {
      selectRecord(table.getRecord());
    }
  }

  @Override
  public void onNextPage() {

    if (!getServices().getContactgegevensService().isVastGesteld(contact)) {
      throw new ProException(INFO, "Geef minimaal één telefoonnummer of e-mailadres in.");
    }

    getServices().getContactgegevensService().updateContactWaarden(contact);
    setStatus(true);
    super.onNextPage();
  }

  @Override
  public void onPreviousPage() {

    setStatus(false);

    super.onPreviousPage();
  }

  private void selectRecord(Record record) {

    Page2Contact page2 = new Page2Contact(record);
    getNavigation().goToPage(page2);
  }

  private void setStatus(boolean saved) {

    if (succesListener != null) {
      succesListener.onStatus(saved, true);
    }

    onClose();
  }
}
