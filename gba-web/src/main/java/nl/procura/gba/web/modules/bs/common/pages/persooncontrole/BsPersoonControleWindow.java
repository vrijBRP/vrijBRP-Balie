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

package nl.procura.gba.web.modules.bs.common.pages.persooncontrole;

import java.util.function.Consumer;

import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.modules.bs.common.pages.persooncontrole.page1.BsPersoonControlePage1;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.windows.home.modules.MainModuleContainer;

public abstract class BsPersoonControleWindow extends GbaModalWindow {

  private final Dossier            dossier;
  private Consumer<DossierPersoon> changeListener;

  public BsPersoonControleWindow(Dossier dossier) {
    this(dossier, dossierPersoon -> {});
  }

  public BsPersoonControleWindow(Dossier dossier, Consumer<DossierPersoon> changeListener) {
    super("Personen van het dossier (Druk op escape om te sluiten)", "80%");
    this.dossier = dossier;
    this.changeListener = changeListener;
  }

  public abstract void afterBijwerken();

  @Override
  public void attach() {
    super.attach();
    BsPersoonControlePage1 page = new BsPersoonControlePage1(dossier, changeListener) {

      @Override
      public void afterBijwerken() {
        BsPersoonControleWindow.this.afterBijwerken();
      }

      @Override
      public void onGoToZaak() {
        BsPersoonControleWindow.this.onGoToZaak();
      }
    };

    MainModuleContainer moduleContainer = new MainModuleContainer(false, page);
    addComponent(moduleContainer);
  }

  public abstract void onGoToZaak();
}
