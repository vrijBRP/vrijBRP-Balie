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

package nl.procura.gba.web.modules.bs.common.modules;

import static nl.procura.standard.Globalfunctions.emp;
import static nl.procura.standard.Globalfunctions.fil;
import static nl.procura.standard.exceptions.ProExceptionSeverity.ERROR;
import static nl.procura.standard.exceptions.ProExceptionType.DATABASE;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;

import nl.procura.gba.web.components.layouts.ModuleTemplate;
import nl.procura.gba.web.modules.bs.common.BsProces;
import nl.procura.gba.web.modules.bs.common.BsProcesListener;
import nl.procura.gba.web.modules.bs.common.BsProcessen;
import nl.procura.gba.web.modules.bs.common.pages.BsPage;
import nl.procura.gba.web.modules.bs.common.pages.BsProcesStatus;
import nl.procura.gba.web.modules.bs.common.pages.documentpage.BsDocumentWindow;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.aantekening.ZaakAantekeningWindow;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.algemeen.ZaakDossier;
import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.component.dialog.ModalWindow;
import nl.procura.vaadin.component.label.Ruler;
import nl.procura.vaadin.component.layout.page.PageContainer;
import nl.procura.vaadin.component.layout.page.PageLayout;
import nl.procura.vaadin.component.layout.page.PageNavigation;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

import lombok.Getter;
import lombok.Setter;

public class BsModule extends ModuleTemplate implements Button.ClickListener {

  private BsProcessen    processen;
  private ModuleTemplate module;

  private Button buttonDocs;
  private Button buttonAant;
  private Button buttonClose;

  /**
   * Store the state of the 'verzoek' popups
   */
  @Getter
  @Setter
  private Set<Class<?>> verzoekPopupStates = new HashSet<>();

  public BsModule() {
  }

  @Override
  public void buttonClick(ClickEvent event) {

    if (event.getButton() == buttonDocs) {
      getApplication().getParentWindow()
          .addWindow(new BsDocumentWindow(processen.getDossier(), this::updateDocumentCaption));
    }

    if (event.getButton() == buttonAant) {
      if (emp(processen.getDossier().getZaakId())) {
        throw new ProException(ERROR, "Er kan geen aantekening worden gemaakt, " +
            "omdat deze zaak nog geen zaak-id heeft");
      }

      getApplication().getParentWindow()
          .addWindow(new ZaakAantekeningWindow(processen.getDossier(), this::updateAantCaption));
    }

    if (event.getButton() == buttonClose) {
      ((ModalWindow) getWindow()).closeWindow();
    }
  }

  public void checkButtons() {

    updateDocumentCaption();
    updateAantCaption();

    // Aanzetten als zaakId is gevuld
    Dossier dossier = processen.getDossier();
    String zaakId = dossier.getZaakId();
    buttonDocs.setEnabled(fil(zaakId));
    buttonAant.setEnabled(fil(zaakId));
  }

  @Override
  public void event(PageEvent event) {

    super.event(event);

    if (event.isEvent(InitPage.class)) {

      // Vul het dossier met een dossiernr
      processen.initStatusses(getApplication());
      processen.getListeners().add(new ClickListener());

      HorizontalLayout hLayout = new HorizontalLayout();

      VerticalLayout vLayout = new VerticalLayout();
      vLayout.setSpacing(true);
      vLayout.setMargin(true, false, true, true);
      vLayout.setWidth("200px");

      VerticalLayout vPLayout = new VerticalLayout();
      vPLayout.setStyleName("bslayout");
      vPLayout.setSizeFull();

      for (BsProces proces : processen.getProcessen()) {
        vPLayout.addComponent(proces.getButton());
      }

      vLayout.addComponent(vPLayout);

      setButtons();

      vLayout.addComponent(new Ruler());
      vLayout.addComponent(buttonDocs);
      vLayout.addComponent(buttonAant);

      setExtraButtons(vLayout);

      checkButtons();

      if (getWindow().isModal()) {
        vLayout.addComponent(buttonClose);
      }

      hLayout.addComponent(vLayout);

      module = new ModuleTemplate() {

        @Override
        public void event(PageEvent event) {

          try {
            if (event.isEvent(InitPage.class)) {

              // Zet de eerste pagina op de laatste pagina die 'enabled' is.

              BsProces lastProces = processen.getLastProces();
              getPages().getNavigation().goToPage(lastProces.getPage());
              processen.setCurrentProces(lastProces);

              // Werk styles bij
              // processen.update ();
            }
          } catch (Exception e) {
            throw new ProException(DATABASE, ERROR, "Fout", e);
          }

          super.event(event);
        }
      };

      module.setPages(new CustomPageContainer());

      hLayout.addComponent(module);
      hLayout.setExpandRatio(module, 1L);
      hLayout.setWidth("100%");
      addExpandComponent(hLayout);
    }
  }

  public BsProcessen getProcessen() {
    return processen;
  }

  public void setProcessen(BsProcessen processen) {
    this.processen = processen;
  }

  private void setButtons() {

    buttonDocs = new Button("Documenten");
    buttonDocs.setWidth("100%");
    buttonDocs.setEnabled(false);
    buttonDocs.addListener(this);

    buttonAant = new Button("Aantekeningen");
    buttonAant.setWidth("100%");
    buttonAant.setEnabled(false);
    buttonAant.addListener(this);

    buttonClose = new Button("Sluiten");
    buttonClose.setWidth("100%");
    buttonClose.addListener(this);
  }

  protected void setExtraButtons(Layout layout) {
  }

  /**
   * Update aant caption
   */
  private void updateAantCaption() {
    int countAantekeningen = BsModule.this.getApplication()
        .getServices()
        .getAantekeningService()
        .getZaakAantekeningen(processen.getDossier())
        .size();
    buttonAant.setCaption(MessageFormat.format("Aantekeningen ({0})", countAantekeningen));
  }

  /**
   * Update document caption
   */
  private void updateDocumentCaption() {
    Dossier dossier = processen.getDossier();
    String zaakId = dossier.getZaakId();
    Services services = getApplication().getServices();

    int countDocumenten = fil(zaakId) ? services.getDmsService().getDocumentsByZaak(dossier).size() : 0;
    buttonDocs.setCaption(MessageFormat.format("Documenten ({0})", countDocumenten));
  }

  public class ClickListener implements BsProcesListener {

    @Override
    public void onButtonClick(BsProces proces) {

      BsProces currentProces = getProcessen().getProces(
          module.getPages().getNavigation().getCurrentPage().getClass());

      if (currentProces != null && (proces.getStatus() == BsProcesStatus.DISABLED ||
          proces.getIncrement() > currentProces.getIncrement())) {
        return;
      }

      module.getPages().getNavigation().goToPage(proces.getPage());
    }
  }

  public class CustomPageContainer extends PageContainer {

    public CustomPageContainer() {
      setNavigation(new CustomPageNavigation(this));
    }
  }

  public final class CustomPageNavigation extends PageNavigation {

    private CustomPageNavigation(PageContainer pageContainer) {
      super(pageContainer);
    }

    @Override
    public PageNavigation goBackToPage(Class<? extends PageLayout> pageClass) {
      return goToPage(pageClass, false);
    }

    @Override
    public PageNavigation goToPage(Class<?> pageClass) {
      return goToPage(pageClass, true);
    }

    @SuppressWarnings("unchecked")
    private PageNavigation goToPage(Class<?> pageClass, boolean doCheck) {
      if (getCurrentPage() != null) {
        BsPage<ZaakDossier> currentPage = (BsPage<ZaakDossier>) getCurrentPage();
        BsProces currentProces = processen.getProces(currentPage.getClass());
        BsProces nextProces = processen.getProces(pageClass);

        try {
          if (doCheck) {
            if (!currentPage.checkPage()) {
              return null;
            }
            if (currentProces != null) {
              processen.updateStatus();
            }
            checkButtons();
          }

          processen.setCurrentProces(nextProces);
        } catch (RuntimeException e) {
          // Bij fout en check dan done op False
          if (doCheck && currentProces != null) {
            currentProces.setStatus(BsProcesStatus.EMPTY);
          }

          throw e;
        }
      }

      // Verwijder de huidige pagina uit de stack
      removePage(pageClass);
      return super.goToPage(pageClass);
    }
  }
}
