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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig;

import com.vaadin.terminal.ThemeResource;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.modules.persoonslijst.overig.header.buttons.HeaderFormButton;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.beheer.persoonhistorie.PersoonHistorieType;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.theme.GbaWebTheme;
import nl.procura.gba.web.windows.home.navigatie.ZakenregisterAccordionTab;
import nl.procura.vaadin.theme.ProcuraWindow;

import static nl.procura.gba.web.modules.hoofdmenu.zakenregister.ZakenregisterUtils.getZakenregisterAccordionTab;

public class ZaakFavButton extends HeaderFormButton {

  private final Zaak zaak;

  private boolean favourite      = false;
  private boolean checkFavourite = false;

  public ZaakFavButton(Zaak zaak, Services services) {
    super(services);
    this.zaak = zaak;

    setStyleName(GbaWebTheme.BUTTON_LINK);
    addStyleName("pl-url-button");
    setFavourite(false);

    addListener((ClickListener) event -> {
      GbaApplication app = (GbaApplication) getApplication();
      ProcuraWindow parentWindow = app.getParentWindow();
      setFavourite(!isFavourite());
      getServices().getZaakAttribuutService().setFavoriet(zaak.getZaakId(), favourite);
      getZakenregisterAccordionTab(parentWindow).ifPresent(ZakenregisterAccordionTab::recountTree);
    });
  }

  @Override
  public void attach() {
    if (!checkFavourite) {
      setFavourite(getServices().getZaakAttribuutService().isFavoriet(zaak.getZaakId()));
      checkFavourite = true;
    }

    super.attach();
  }

  public boolean isFavourite() {
    return favourite;
  }

  public void setFavourite(boolean favourite) {
    this.favourite = favourite;
    if (favourite) {
      setIcon(new ThemeResource("../gba-web/buttons/img/favourite.png"));
      setDescription("Verwijderen uit de lijst met favorieten");
    } else {
      setIcon(new ThemeResource("../gba-web/buttons/img/not-favourite.png"));
      setDescription("Deze zaak toevoegen aan de lijst met favorieten");
    }
  }
}
