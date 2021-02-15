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

package nl.procura.gba.web.components.layouts.navigation;

import com.vaadin.terminal.ExternalResource;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.application.ProcessChangeInterceptor;
import nl.procura.gba.web.common.annotations.ModuleAnnotation;
import nl.procura.gba.web.components.EmbeddedResource;
import nl.procura.gba.web.components.layouts.lazyloading.LazyLayout;
import nl.procura.gba.web.services.AbstractService;
import nl.procura.gba.web.services.ServiceEvent;
import nl.procura.gba.web.services.ServiceListener;
import nl.procura.gba.web.services.beheer.profiel.actie.ProfielActieType;
import nl.procura.gba.web.services.zaken.ServiceControle;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.gba.web.services.zaken.algemeen.ZaakNumbers;
import nl.procura.gba.web.windows.GbaWindow;
import nl.procura.gba.web.windows.home.modules.MainModuleContainer;
import nl.procura.vaadin.component.layout.accordion.AccordionLink;
import nl.procura.vaadin.component.layout.accordion.AccordionTab;
import nl.procura.vaadin.component.window.Message;
import nl.procura.vaadin.functies.VaadinUtils;

public class GbaAccordionTab extends AccordionTab {

  private GbaApplication application = null;

  public GbaAccordionTab() {
  }

  public GbaAccordionTab(String caption, GbaApplication application) {
    setCaption(caption);
    this.application = application;
  }

  public AccordionLink addLink(Class<?> c) {

    final ModuleAnnotation annotation = c.getAnnotation(ModuleAnnotation.class);
    final ZaakNumbers aantallenInterface = getService(annotation, ZaakNumbers.class);
    final ServiceControle controleInterface = getService(annotation, ServiceControle.class);
    final ZaakType[] zaakTypes = annotation.zaakTypes();

    AccordionLink accordionLink = new AccordionLink(c, annotation.url(), annotation.caption(), annotation.icon());

    if (!application.isProfielActie(ProfielActieType.SELECT, annotation.profielActie())) {
      return accordionLink;
    }

    if (!isLinkAllowed(application, zaakTypes)) {
      return accordionLink;
    }

    AccordionLink link = addLink(accordionLink);

    final String windowName = application.getCurrentWindow().getName();

    if (aantallenInterface != null) {

      final Label label = new Label("", Label.CONTENT_XHTML);
      label.setWidth("40px");
      label.setValue(getCount(zaakTypes, aantallenInterface));
      link.addComponent(label);

      aantallenInterface.setListener(new ServiceListener() {

        @Override
        public void action(ServiceEvent event) {

          if (event == ServiceEvent.CHANGE) {
            label.setValue(getCount(zaakTypes, aantallenInterface));
          }
        }

        @Override
        public String getId() {
          return "countListener" + windowName;
        }
      });
    }

    if (controleInterface != null) {

      final EmbeddedResource warnIcon = new EmbeddedResource(getStatus(true));

      LazyLayout warnIconLayout = new LazyLayout(warnIcon, 1000, "24px", "20px") {

        @Override
        public void onVisible() {
          warnIcon.setSource(new ThemeResource(getStatus(controleInterface.isCorrect())));
        }
      }.setIndicatorNone();

      controleInterface.setListener(new ServiceListener() {

        @Override
        public void action(ServiceEvent event) {
          if (event == ServiceEvent.CHANGE) {
            warnIcon.setSource(new ThemeResource(getStatus(controleInterface.isCorrect())));
          }
        }

        @Override
        public String getId() {
          return "bsmListener" + windowName;
        }
      });

      link.addComponent(warnIconLayout);
      link.setComponentAlignment(warnIconLayout, Alignment.MIDDLE_LEFT);
    }

    return link;
  }

  @Override
  public AccordionTab addTab(AccordionTab node) {

    if (node.getComponentCount() > 0) {

      super.addTab(node);
    }

    return node;
  }

  @Override
  public GbaApplication getApplication() {
    return (GbaApplication) super.getApplication();
  }

  @Override
  public void onLinkSelect(AccordionLink link) {

    getApplication().getProcess().intercept(new ProcessChangeInterceptor(getWindow(), link) {

      @Override
      protected void proceed(AccordionLink link) {
        doLinkSelect(link);
      }
    });
  }

  protected String getCount(ZaakType[] zaakTypes, ZaakNumbers zdb) {

    int count = 0;

    if (application != null) {

      // Zoek huidige pl op
      BasePLExt pl = application.getServices().getPersonenWsService().getHuidige();

      // Geeft aanvragen terug
      ZaakArgumenten z = new ZaakArgumenten(pl);
      z.addTypen(zaakTypes);
      count = zdb.getZakenCount(z);
    }

    return (count > 0) ? ("<b>" + count + "</b>") : "0";
  }

  /**
   * Is de link met dit zaakType toegestaan?
   */
  @SuppressWarnings("unused")
  protected boolean isLinkAllowed(GbaApplication application, ZaakType[] zaakTypes) { // overriden
    return true;
  }

  private void doLinkSelect(AccordionLink link) {

    if (link.getUrl().isFragment()) {

      // Fragment
      link.activate();

      // Wijzig fragment in URL zonder fragmentChangeListener te alarmeren
      ((GbaWindow) getWindow()).getFragmentUtility().setFragment(link.getUrl().getFragment(), false);

      if (link.getId() != null) {

        MainModuleContainer mainModule = VaadinUtils.getChild(getWindow(), MainModuleContainer.class);
        mainModule.getNavigation().getPages().clear();
        mainModule.getNavigation().addPage((Class<?>) link.getId());
      } else {
        new Message(getWindow(), "Geen pagina.", Message.TYPE_WARNING_MESSAGE);
      }
    } else if (link.getUrl().isExternal()) {

      // Interne link
      getWindow().open(new ExternalResource(link.getUrl().getValue()));
    } else {

      // Externe link
      getWindow().open(new ExternalResource(getApplication().getExternalURL(link.getUrl().getValue())));
    }
  }

  @SuppressWarnings("unchecked")
  private <T> T getService(ModuleAnnotation annotation, Class<T> specificInterface) {
    if ((application != null) && specificInterface.isAssignableFrom(annotation.service())) {
      Class<AbstractService> cl = (Class<AbstractService>) annotation.service();
      return (T) application.getServices().get(cl);
    }

    return null;
  }

  private String getStatus(boolean ok) {
    return ok ? "icons/bullet-green.png" : "icons/bullet-red.png";
  }
}
