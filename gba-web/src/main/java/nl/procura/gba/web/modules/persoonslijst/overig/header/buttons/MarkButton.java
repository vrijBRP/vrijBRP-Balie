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

package nl.procura.gba.web.modules.persoonslijst.overig.header.buttons;

import java.util.Arrays;

import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.NativeButton;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.jpa.personen.db.RiskProfileSig;
import nl.procura.gba.web.modules.persoonslijst.overig.mark.ProfileMarkWindow;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.bs.riskanalysis.RiskAnalysisService;
import nl.procura.gba.web.theme.GbaWebTheme;

public class MarkButton extends NativeButton {

  private final RiskProfileSig signal;
  private boolean              checked = false;
  private final Services       services;

  public MarkButton(Services services) {
    this(services, null);
  }

  public MarkButton(Services services, RiskProfileSig newSignal) {
    super("");
    this.services = services;
    this.signal = newSignal;
    setStyleName(GbaWebTheme.BUTTON_LINK);
    addStyleName("pl-url-button");
    setMarked(false);

    if (newSignal == null) {
      addListener((Button.ClickListener) event -> openMarkWindow());
    } else {
      setMarked(newSignal.isEnabled());
      addListener((ClickListener) clickEvent -> {
        services.getRiskAnalysisService().switchSignaling(signal);
        setMarked(signal.isEnabled());
      });
    }
  }

  @Override
  public void attach() {
    if (!checked) {
      checkMarking();
      checked = true;
    }

    super.attach();
  }

  public void setMarked(boolean marked) {
    if (marked) {
      setIcon(new ThemeResource("../gba-web/buttons/img/marked.png"));
      setDescription("Deze persoon is gemarkeerd voor risicoanalyses");
    } else {
      setIcon(new ThemeResource("../gba-web/buttons/img/not-marked.png"));
      setDescription("Deze persoon is niet gemarkeerd voor risicoanalyses");
    }
  }

  private void openMarkWindow() {
    BasePLExt pl = services.getPersonenWsService().getHuidige();
    getWindow().addWindow(new ProfileMarkWindow(pl, this::checkMarking));
  }

  private void checkMarking() {
    if (signal == null) {
      RiskAnalysisService service = services.getRiskAnalysisService();
      BasePLExt pl = services.getPersonenWsService().getHuidige();
      RiskProfileSig bsnSignal = service.buildBsnSignal(pl);
      RiskProfileSig addressSignal = service.buildAddressSignal(pl);
      setMarked(service.getSignal(Arrays.asList(bsnSignal, addressSignal))
          .filter(RiskProfileSig::isEnabled)
          .isPresent());
    }
  }
}
