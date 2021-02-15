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

import nl.procura.gba.web.modules.zaken.common.ZaakMultiWindow;

public abstract class ZaakPersonenMultiWindow extends ZaakMultiWindow {

  private final ZaakPersoonType[] types;

  public ZaakPersonenMultiWindow(ZaakPersoonType... types) {
    super("Selecteer een persoon");
    this.types = types;
  }

  public abstract void onSelectPersoon(ZaakPersoonType type);

  @Override
  protected void setKeuzes() {

    if (types != null) {
      for (ZaakPersoonType type : types) {
        addKeuze(new KeuzePersoon(type.getCaption(), type));
      }
    }
  }

  public class KeuzePersoon extends Keuze {

    private final ZaakPersoonType type;

    public KeuzePersoon(String titel, ZaakPersoonType type) {
      super(titel);
      this.type = type;
    }

    @Override
    public void onClick() {
      onSelectPersoon(type);
    }
  }
}
