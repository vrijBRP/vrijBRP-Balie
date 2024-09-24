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

package nl.procura.gba.web.modules.zaken.overzicht.page1;

import com.google.gwt.event.dom.client.KeyCodes;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.procura.arguments.PLEDatasource;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.common.annotations.ModuleAnnotation;
import nl.procura.gba.web.common.misc.Matrix;
import nl.procura.gba.web.common.misc.Matrix.Element;
import nl.procura.gba.web.common.misc.Matrix.MOVES;
import nl.procura.gba.web.components.layouts.ModuleTemplate;
import nl.procura.gba.web.components.layouts.page.ArrowKeylistenerHandler;
import nl.procura.gba.web.modules.beheer.overzicht.page1.ZakenIcon;
import nl.procura.gba.web.modules.zaken.afstamming.ModuleAfstamming;
import nl.procura.gba.web.modules.zaken.common.ZakenPage;
import nl.procura.gba.web.modules.zaken.correspondentie.ModuleCorrespondentie;
import nl.procura.gba.web.modules.zaken.document.ModuleDocumenten;
import nl.procura.gba.web.modules.zaken.geheim.ModuleGeheim;
import nl.procura.gba.web.modules.zaken.gpk.ModuleGpk;
import nl.procura.gba.web.modules.zaken.gv.ModuleGv;
import nl.procura.gba.web.modules.zaken.huwelijk.ModuleHuwelijk;
import nl.procura.gba.web.modules.zaken.indicatie.ModuleIndicatie;
import nl.procura.gba.web.modules.zaken.inhouding.ModuleInhouding;
import nl.procura.gba.web.modules.zaken.naamgebruik.ModuleNaamgebruik;
import nl.procura.gba.web.modules.zaken.naturalisatie.ModuleNaturalisatie;
import nl.procura.gba.web.modules.zaken.onderzoek.ModuleOnderzoek;
import nl.procura.gba.web.modules.zaken.overlijden.ModuleOverlijden;
import nl.procura.gba.web.modules.zaken.protocol.ModuleProtocol;
import nl.procura.gba.web.modules.zaken.registration.ModuleRegistration;
import nl.procura.gba.web.modules.zaken.reisdocument.ModuleReisdocument;
import nl.procura.gba.web.modules.zaken.rijbewijs.ModuleRijbewijs;
import nl.procura.gba.web.modules.zaken.riskanalysis.ModuleRiskAnalysis;
import nl.procura.gba.web.modules.zaken.sms.ModuleSms;
import nl.procura.gba.web.modules.zaken.tmv.ModuleTmv;
import nl.procura.gba.web.modules.zaken.uittreksel.ModuleUittreksel;
import nl.procura.gba.web.modules.zaken.verhuizing.ModuleVerhuizing;
import nl.procura.gba.web.modules.zaken.verkiezing.ModuleVerkiezing;
import nl.procura.gba.web.modules.zaken.vog.ModuleVog;
import nl.procura.gba.web.modules.zaken.woningkaart.ModuleWoningkaart;
import nl.procura.gba.web.services.AbstractService;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.gba.web.services.zaken.algemeen.ZaakNumbers;
import nl.procura.gba.web.theme.GbaWebTheme;
import nl.procura.vaadin.component.label.Break;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page1OverzichtZaken extends ZakenPage {

  private final Panel        panel  = new Panel();
  private final Matrix<Zaak> matrix = new Matrix<>(3);

  public Page1OverzichtZaken() {
    super("Overzicht");
  }

  @Override
  public void attach() {
    super.attach();
    panel.focus();
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      GridLayout gl1 = new GridLayout(4, 1);
      gl1.setStyleName("zaken_grid");
      gl1.setSizeFull();
      gl1.setSpacing(true);

      GridLayout gl2 = new GridLayout(4, 1);
      gl2.setStyleName("zaken_grid");
      gl2.setSizeFull();
      gl2.setSpacing(true);

      addZaak(new Zaak(ModuleAfstamming.class, false));
      addZaak(new Zaak(ModuleCorrespondentie.class, false));
      addZaak(new Zaak(ModuleDocumenten.class, false));
      addZaak(new Zaak(ModuleRegistration.class, false));
      addZaak(new Zaak(ModuleGpk.class, false));
      addZaak(new Zaak(ModuleGv.class, false));
      addZaak(new Zaak(ModuleHuwelijk.class, false));
      addZaak(new Zaak(ModuleIndicatie.class, false));
      addZaak(new Zaak(ModuleInhouding.class, false));
      addZaak(new Zaak(ModuleNaamgebruik.class, false));
      addZaak(new Zaak(ModuleNaturalisatie.class, false));
      addZaak(new Zaak(ModuleOnderzoek.class, false));
      addZaak(new Zaak(ModuleRiskAnalysis.class, false));
      addZaak(new Zaak(ModuleOverlijden.class, false));
      addZaak(new Zaak(ModuleReisdocument.class, false));

      if (getServices().getRijbewijsService().isRijbewijzenServiceActive()) {
        addZaak(new Zaak(ModuleRijbewijs.class, false));
      }

      addZaak(new Zaak(ModuleTmv.class, false));
      addZaak(new Zaak(ModuleUittreksel.class, false));
      addZaak(new Zaak(ModuleVerhuizing.class, false));
      addZaak(new Zaak(ModuleGeheim.class, false));
      addZaak(new Zaak(ModuleVog.class, false));
      addZaak(new Zaak(ModuleProtocol.class, true));
      addZaak(new Zaak(ModuleWoningkaart.class, true));
      addZaak(new Zaak(ModuleSms.class, true));
      addZaak(new Zaak(ModuleVerkiezing.class, true));

      for (Element<Zaak> element : matrix.getElements()) {
        Zaak zaak = element.getObject();
        if (zaak.isAllowed()) {
          if (zaak.isOptie()) {
            gl2.addComponent(zaak);
          } else {
            gl1.addComponent(zaak);
          }
        }
      }

      if (gl1.getComponentCount() > 0) {
        panel.addComponent(new Fieldset("Zaken"));
        panel.addComponent(gl1);
        panel.addComponent(new Break());
      }

      if (gl2.getComponentCount() > 0) {
        panel.addComponent(new Fieldset("Opties"));
        panel.addComponent(gl2);
      }

      panel.setCaption(null);
      panel.setStyleName(GbaWebTheme.PANEL_LIGHT);
      panel.addStyleName("panel_zaken");

      VerticalLayout v = (VerticalLayout) panel.getContent();
      v.setMargin(false);
      v.setSpacing(true);

      ArrowKeylistenerHandler keylistenerHandler = new ArrowKeylistenerHandler() {

        @Override
        public void handleKey(int keyCode) {

          if (keyCode == KeyCodes.KEY_UP) {
            matrix.move(MOVES.UP);
          } else if (keyCode == KeyCodes.KEY_RIGHT) {
            matrix.move(MOVES.RIGHT);
          } else if (keyCode == KeyCodes.KEY_DOWN) {
            matrix.move(MOVES.DOWN);
          } else if (keyCode == KeyCodes.KEY_LEFT) {
            matrix.move(MOVES.LEFT);
          }

          focusZaak(matrix.getCurrentElement().getObject());
        }
      };

      focusZaak(matrix.getCurrentElement().getObject());
      panel.addActionHandler(keylistenerHandler);
      addComponent(panel);
    }

    super.event(event);
  }

  public GbaApplication getGbaApplication() {
    return getApplication();
  }

  @Override
  public void onEnter() {
    matrix.getCurrentElement().getObject().layoutClick(null);
  }

  private void addZaak(Zaak zaak) {
    matrix.add(zaak);
  }

  private void focusZaak(Zaak zaak) {
    for (Element<Zaak> k : matrix.getElements()) {
      if (k.getObject() == zaak) {
        k.getObject().addStyleName("zaken_active");
      } else {
        k.getObject().removeStyleName("zaken_active");
      }
    }
  }

  private String getCounts(ZaakType[] zaakTypes, ZaakNumbers zdb) {

    int count = 0;

    if (getApplication() != null) {

      // Zoek huidige pl op
      BasePLExt pl = getApplication().getServices().getPersonenWsService().getHuidige();

      // Geeft aanvragen terug
      count = zdb.getZakenCount(new ZaakArgumenten(pl, zaakTypes));
    }

    return (count > 0) ? ("<b>" + count + "</b>") : "0";
  }

  @SuppressWarnings("unchecked")
  private AbstractService getService(ModuleAnnotation annotation) {

    if ((getApplication() != null) && ZaakNumbers.class.isAssignableFrom(annotation.service())) {
      Class<AbstractService> cl = (Class<AbstractService>) annotation.service();
      return getApplication().getServices().get(cl);
    }

    return null;
  }

  private boolean isLinkAllowed(ZaakType[] zaakTypes) {
    BasePLExt pl = getPl();
    if (pl.heeftVerwijzing() || PLEDatasource.GBAV.equals(pl.getDatasource())) {
      for (ZaakType zaakType : zaakTypes) {
        switch (zaakType) {
          case GPK:
          case INHOUD_VERMIS:
          case INDICATIE:
          case REISDOCUMENT:
          case COVOG:
            return false;

          default:
            break;
        }
      }
    }

    return true;
  }

  public class Zaak extends ZakenIcon {

    private boolean optie;

    private Zaak(Class<? extends ModuleTemplate> moduleClass, boolean opties) {
      super(moduleClass, null, true);
      this.optie = opties;
    }

    @Override
    public String getCount() {
      ZaakNumbers zdb = (ZaakNumbers) getService(getAnnotation());
      if (zdb != null) {
        return getCounts(getAnnotation().zaakTypes(), zdb);
      }

      return "";
    }

    @Override
    public boolean isAllowed() {
      return isLinkAllowed(getAnnotation().zaakTypes()) &&
          getGbaApplication().isProfielActie(getAnnotation().profielActie());
    }

    public boolean isOptie() {
      return optie;
    }

    public void setOptie(boolean optie) {
      this.optie = optie;
    }
  }
}
