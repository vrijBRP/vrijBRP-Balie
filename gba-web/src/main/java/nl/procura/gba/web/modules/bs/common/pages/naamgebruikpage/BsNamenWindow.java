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

package nl.procura.gba.web.modules.bs.common.pages.naamgebruikpage;

import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType;
import nl.procura.gba.web.services.bs.algemeen.interfaces.DossierNamenrecht;
import nl.procura.gba.web.windows.home.modules.MainModuleContainer;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public abstract class BsNamenWindow extends GbaModalWindow {

  private final DossierNamenrecht dossier;

  public BsNamenWindow(DossierNamenrecht dossier) {
    super("Namen (Druk op escape om te sluiten)", "900px");
    this.dossier = dossier;
  }

  @Override
  public void attach() {
    super.attach();
    addComponent(new MainModuleContainer(false, new BsNamenPage(this, dossier)));
  }

  public abstract void setNaam(String geslachtsnaam,
      FieldValue voorvoegsel,
      FieldValue titel,
      DossierPersoonType type);
}
