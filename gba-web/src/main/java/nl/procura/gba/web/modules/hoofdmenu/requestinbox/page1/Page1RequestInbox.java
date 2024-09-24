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

package nl.procura.gba.web.modules.hoofdmenu.requestinbox.page1;

import static nl.procura.burgerzaken.requestinbox.api.RequestInboxUtils.getVrijBRPChannel;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.INFO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;

import nl.procura.burgerzaken.requestinbox.api.ListItemsRequest;
import nl.procura.burgerzaken.requestinbox.api.RequestInboxUtils;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.modules.hoofdmenu.requestinbox.ModuleRequestInboxParams;
import nl.procura.gba.web.modules.hoofdmenu.requestinbox.RequestInboxStatusButton;
import nl.procura.gba.web.modules.hoofdmenu.requestinbox.page2.Page2RequestInbox;
import nl.procura.gba.web.services.beheer.requestinbox.RequestInboxItem;
import nl.procura.gba.web.services.beheer.requestinbox.RequestInboxItemResult;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.HLayout;
import nl.procura.vaadin.component.layout.VLayout;
import nl.procura.vaadin.component.layout.page.pageEvents.AfterReturn;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.validation.Bsn;

public class Page1RequestInbox extends NormalPageTemplate {

  private final ModuleRequestInboxParams params;
  private Page1RequestInboxForm          form;
  private Page1RequestInboxTable         table;

  private int       numberOfItems = 0;
  private int       numberOfPages = 1;
  private int       page          = 1;
  private final int pageSize      = 15;

  private final Button statusButton = new RequestInboxStatusButton(this::getSelectedValues, this::search);
  private final Label  pageLabel    = new Label("Totaal 0 verzoeken, pagina 1 van 1");

  public Page1RequestInbox(ModuleRequestInboxParams params) {
    super(params.getTitle());
    this.params = params;
  }

  @Override
  public void event(PageEvent event) {
    if (event.isEvent(InitPage.class)) {

      if (!getServices().getRequestInboxService().isEnabled()) {
        setInfo("De verzoeken service is niet geactiveerd in de parameters.");
      }

      addButton(statusButton);

      table = new Page1RequestInboxTable(this::gotoPage2);
      table.setHeight("300px");
      form = new Page1RequestInboxForm(params, () -> search(1));

      // Navigation
      buttonPrev.setCaption("Vorige pagina (F1)");
      buttonNext.setCaption("Volgende pagina (F2)");
      buttonNext.addListener(this);
      buttonPrev.addListener(this);

      HLayout tableNavLayout = new HLayout(buttonPrev, buttonNext);
      getButtonLayout().addComponent(tableNavLayout);
      getButtonLayout().setExpandRatio(tableNavLayout, 1F);
      getButtonLayout().setWidth("100%");

      if (getWindow().isModal()) {
        addButton(buttonClose);
      } else {
        pageLabel.setWidth("515px");
        getButtonLayout().addComponent(pageLabel);
        getButtonLayout().setComponentAlignment(pageLabel, Alignment.MIDDLE_RIGHT);
        getButtonLayout().setExpandRatio(pageLabel, 1F);
      }

      VLayout tableLayout = new VLayout();
      tableLayout.addComponent(new Fieldset("Verzoeken"));
      if (getWindow().isModal()) {
        tableLayout.addComponent(table);
      } else {
        tableLayout.addExpandComponent(table);
      }
      if (getWindow().isModal()) {
        tableLayout.addComponent(pageLabel);
      }

      // Filters
      VLayout filterLayout = new VLayout(new Fieldset("Filters", form));
      filterLayout.setSizeUndefined();
      filterLayout.setWidth("320px");

      HLayout hLayout = new HLayout().spacing(true);
      hLayout.addExpandComponent(tableLayout);
      hLayout.addComponent(filterLayout);
      addExpandComponent(hLayout);
      search(page);
    }

    if (event.isEvent(AfterReturn.class))

    {
      search(page);
    }

    super.event(event);
  }

  private void gotoPage2(RequestInboxItem inboxRecord) {
    getNavigation().goToPage(new Page2RequestInbox(getTitle(), inboxRecord));
  }

  @Override
  public void onPreviousPage() {
    search(--page);
  }

  @Override
  public void onNextPage() {
    search(++page);
  }

  private void search() {
    search(page);
  }

  private void search(int currentPage) {
    page = currentPage;
    if (page < 1) {
      page = 1;
    }
    ListItemsRequest request = new ListItemsRequest()
        .preferredChannel(getVrijBRPChannel())
        .status(form.getBean().getStatus())
        .types(Optional.ofNullable(form.getBean().getType())
            .map(item -> Collections.singletonList(item.getId()))
            .orElse(new ArrayList<>()))
        .requestHandler(Optional.ofNullable(form.getBean().getUser())
            .map(user -> RequestInboxUtils.getVrijBRPUser(user.getId()))
            .orElse(null))
        .customer(Optional.ofNullable(form.getBean().getBsn())
            .map(bsn -> new Bsn(bsn.getLongValue()))
            .filter(Bsn::isCorrect)
            .map(RequestInboxUtils::getVrijBRPCustomer)
            .orElse(null))
        .page(page)
        .itemsPerPage(pageSize);

    RequestInboxItemResult result = getApplication().getServices()
        .getRequestInboxService()
        .getRequestInboxItems(request);

    numberOfItems = result.getTotalItems();
    numberOfPages = (int) Math.ceil((double) numberOfItems / pageSize);
    table.setItems(page, pageSize, result.getItems());

    buttonPrev.setEnabled(page > 1);
    buttonNext.setEnabled(page < numberOfPages);
    setPageLabel();
    table.init();
    params.getUpdateListener().run();
  }

  private void setPageLabel() {
    pageLabel.setValue("Totaal " + numberOfItems + " verzoeken, pagina " + page + " van " + numberOfPages);
  }

  @Override
  public void onClose() {
    getWindow().closeWindow();
  }

  private List<RequestInboxItem> getSelectedValues() {
    ArrayList<RequestInboxItem> selectedValues = table.getSelectedValues(RequestInboxItem.class);
    if (selectedValues.isEmpty()) {
      throw new ProException(INFO, "Geen verzoeken geselecteerd");
    }
    return selectedValues;
  }
}
