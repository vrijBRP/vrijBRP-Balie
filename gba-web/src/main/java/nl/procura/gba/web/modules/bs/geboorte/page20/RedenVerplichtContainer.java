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

package nl.procura.gba.web.modules.bs.geboorte.page20;

import static nl.procura.gba.web.services.bs.geboorte.RedenVerplicht.*;

import nl.procura.gba.common.ZaakType;
import nl.procura.gba.web.components.containers.GbaContainer;
import nl.procura.gba.web.services.gba.functies.Geslacht;

public class RedenVerplichtContainer extends GbaContainer {

  public RedenVerplichtContainer() {
  }

  public RedenVerplichtContainer(ZaakType zaakType, Geslacht geslacht) {

    removeAllItems();

    switch (zaakType) {
      case LEVENLOOS:
        levenloos(geslacht);
        break;

      default:
        geboorte(geslacht);
    }
  }

  private void geboorte(Geslacht geslacht) {
    switch (geslacht) {
      case MAN:
        addItems(VADER, AANWEZIG_BIJ_GEBOORTE, GEBOORTE_IN_MIJN_WONING, GEBOORTE_IN_MIJN_INSTELLING,
            BURGEMEESTER, BEGRAFENIS_ONDERNEMER);
        break;

      case VROUW:
        addItems(DUO_MOEDER, AANWEZIG_BIJ_GEBOORTE, GEBOORTE_IN_MIJN_WONING, GEBOORTE_IN_MIJN_INSTELLING,
            MOEDER, BURGEMEESTER, BEGRAFENIS_ONDERNEMER);
        break;

      default:
        addItems(VADER, DUO_MOEDER, AANWEZIG_BIJ_GEBOORTE, GEBOORTE_IN_MIJN_WONING, GEBOORTE_IN_MIJN_INSTELLING,
            MOEDER, BURGEMEESTER, BEGRAFENIS_ONDERNEMER);
    }
  }

  private void levenloos(Geslacht geslacht) {
    switch (geslacht) {
      case MAN:
        addItems(BEGRAFENIS_ONDERNEMER, VADER, BURGEMEESTER, KENNISDRAGER);
        break;

      case VROUW:
        addItems(BEGRAFENIS_ONDERNEMER, DUO_MOEDER, MOEDER, BURGEMEESTER, KENNISDRAGER);
        break;

      default:
        addItems(BEGRAFENIS_ONDERNEMER, VADER, DUO_MOEDER, MOEDER, BURGEMEESTER);
        break;
    }
  }

}
