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

package nl.procura.gba.web.modules.beheer.overzicht.page1;

import com.google.gwt.event.dom.client.KeyCodes;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.common.misc.Matrix;
import nl.procura.gba.web.common.misc.Matrix.Element;
import nl.procura.gba.web.common.misc.Matrix.MOVES;
import nl.procura.gba.web.components.layouts.ModuleTemplate;
import nl.procura.gba.web.components.layouts.page.ArrowKeylistenerHandler;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.modules.beheer.documenten.ModuleDocumenten;
import nl.procura.gba.web.modules.beheer.email.ModuleEmail;
import nl.procura.gba.web.modules.beheer.gebruikers.ModuleGebruikers;
import nl.procura.gba.web.modules.beheer.kassa.ModuleKassa;
import nl.procura.gba.web.modules.beheer.locaties.ModuleLocaties;
import nl.procura.gba.web.modules.beheer.log.ModuleLog;
import nl.procura.gba.web.modules.beheer.logbestanden.ModuleLogbestanden;
import nl.procura.gba.web.modules.beheer.onderhoud.ModuleOnderhoud;
import nl.procura.gba.web.modules.beheer.overzicht.ModuleOverzichtBeheer;
import nl.procura.gba.web.modules.beheer.parameters.ModuleParameters;
import nl.procura.gba.web.modules.beheer.profielen.ModuleProfielen;
import nl.procura.gba.web.modules.beheer.protocollering.ModuleProtocollering;
import nl.procura.gba.web.theme.GbaWebTheme;
import nl.procura.vaadin.component.label.Break;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page1OverzichtBeheer extends NormalPageTemplate {

  private final Panel        panel  = new Panel();
  private final Matrix<Zaak> matrix = new Matrix<>(3);

  public Page1OverzichtBeheer() {
    super("Overzicht beheer");
    addComponent(new Break());
  }

  @Override
  public void attach() {

    super.attach();
    panel.focus();
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      GridLayout gl1 = new GridLayout(3, 1);
      gl1.setStyleName("zaken_grid");
      gl1.setSizeFull();
      gl1.setSpacing(true);

      addZaak(new Zaak(ModuleOverzichtBeheer.class));
      addZaak(new Zaak(ModuleParameters.class));
      addZaak(new Zaak(ModuleLog.class));
      addZaak(new Zaak(ModuleGebruikers.class));
      addZaak(new Zaak(ModuleOnderhoud.class));
      addZaak(new Zaak(ModuleProfielen.class));
      addZaak(new Zaak(ModuleProtocollering.class));
      addZaak(new Zaak(ModuleDocumenten.class));
      addZaak(new Zaak(ModuleLocaties.class));
      addZaak(new Zaak(ModuleKassa.class));
      addZaak(new Zaak(ModuleLogbestanden.class));
      addZaak(new Zaak(ModuleEmail.class));

      for (Element<Zaak> element : matrix.getElements()) {

        Zaak zaak = element.getObject();

        gl1.addComponent(zaak);
      }

      panel.addComponent(new Fieldset("Modules"));
      panel.addComponent(gl1);

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

    super.onEnter();
  }

  private void addZaak(Zaak zaak) {

    if (zaak.isAllowed()) {
      matrix.add(zaak);
    }
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

  public class Zaak extends ZakenIcon {

    private Zaak(Class<? extends ModuleTemplate> clazz) {
      super(clazz, false);
    }

    @Override
    public String getCount() {
      return "";
    }

    @Override
    public boolean isAllowed() {
      return getGbaApplication().isProfielActie(getAnnotation().profielActie());
    }
  }
}
