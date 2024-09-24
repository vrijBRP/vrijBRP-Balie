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

package nl.procura.gba.web.modules.bs.naturalisatie.aanschrijving;

import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.services.bs.naturalisatie.DossierNaturalisatie;
import nl.procura.gba.web.windows.home.modules.MainModuleContainer;

public class AanschrijvingWindow extends GbaModalWindow {

  private final DossierNaturalisatie zaakDossier;

  public AanschrijvingWindow(DossierNaturalisatie zaakDossier) {
    super("Aanschrijving / afdrukken (Escape om te sluiten)", "1000px");
    this.zaakDossier = zaakDossier;
    setHeight("400px");
  }

  @Override
  public void attach() {
    super.attach();
    addComponent(new MainModuleContainer(false, new AanschrijvingPage(zaakDossier)));
  }
}
