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

package nl.procura.gbaws.web.vaadin.layouts;

import static nl.procura.standard.Globalfunctions.fil;

import com.github.wolfie.refresher.Refresher;
import com.github.wolfie.refresher.Refresher.RefreshListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TabSheet;

import nl.procura.gbaws.web.vaadin.layouts.pages.ButtonPageTemplate;
import nl.procura.gbaws.web.vaadin.layouts.pages.RefreshButton;
import nl.procura.vaadin.component.dialog.ModalWindow;
import nl.procura.vaadin.component.label.H2;
import nl.procura.vaadin.functies.VaadinUtils;

public class DefaultPageLayout extends ButtonPageTemplate {

  protected final RefreshButton buttonRefresh = new RefreshButton();

  private static final long REFRESH_INTERVAL = 2000;
  private final boolean     modalWindow;

  public DefaultPageLayout() {
    this("");
  }

  public DefaultPageLayout(String title) {
    this(title, false);
  }

  public DefaultPageLayout(String title, boolean modalWindow) {

    this.modalWindow = modalWindow;

    setSpacing(true);
    setMargin(true);
    setWidth("100%");
    setTitle(title);

    if (isModalWindow()) {

      if (fil(title)) {

        H2 titleLayout = new H2(title);
        HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setSizeFull();
        topLayout.addComponent(titleLayout);
        topLayout.addComponent(buttonClose);

        buttonClose.addListener(this);

        topLayout.setComponentAlignment(titleLayout, Alignment.MIDDLE_LEFT);
        topLayout.setComponentAlignment(buttonClose, Alignment.MIDDLE_RIGHT);
        addComponent(topLayout);
        setComponentAlignment(topLayout, Alignment.TOP_LEFT);
      }
    } else {

      if (fil(title)) {

        H2 titleLayout = new H2(title);
        addComponent(titleLayout);
        setComponentAlignment(titleLayout, Alignment.TOP_LEFT);
      }
    }
  }

  protected void addRefresher() {
    Refresher refresher = new Refresher();
    refresher.addListener(new ActionRefreshListener());
    refresher.setRefreshInterval(REFRESH_INTERVAL);
    addComponent(refresher);
  }

  protected void onRefresh() {
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
    super.onPreviousPage();
  }

  @Override
  public void attach() {

    // Geen margin als page onder een tabsheet valt
    if (VaadinUtils.getParent(this, TabSheet.class) != null) {
      setMargin(true, false, false, false);
    }

    super.attach();

    getWindow().center();
  }

  @Override
  public void onClose() {
    if (getWindow() instanceof ModalWindow) {
      getWindow().closeWindow();
    }
    super.onClose();
  }

  protected boolean isModalWindow() {
    return modalWindow;
  }

  protected boolean hasPreviousPage() {
    return getNavigation().getPreviousPage() != null;
  }

  private class ActionRefreshListener implements RefreshListener {

    @Override
    public void refresh(Refresher source) {
      if (buttonRefresh != null && buttonRefresh.isRefresh()) {
        onRefresh();
      }
    }
  }
}
