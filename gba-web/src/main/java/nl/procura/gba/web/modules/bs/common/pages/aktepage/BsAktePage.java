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

package nl.procura.gba.web.modules.bs.common.pages.aktepage;

import static nl.procura.standard.exceptions.ProExceptionSeverity.WARNING;

import nl.procura.gba.web.modules.bs.common.pages.BsPage;
import nl.procura.gba.web.modules.bs.common.pages.aktepage.page2.BsAktePage2;
import nl.procura.gba.web.services.bs.algemeen.ZaakDossier;
import nl.procura.gba.web.services.bs.algemeen.akte.DossierAkte;
import nl.procura.standard.exceptions.ProException;

public class BsAktePage<T extends ZaakDossier> extends BsPage<T> {

  public BsAktePage(String title) {
    super(title);
  }

  protected boolean isAktesCorrect() {

    for (DossierAkte akte : getZaakDossier().getDossier().getAktes()) {
      if (!akte.isCorrect()) {
        return false;
      }
    }

    return true;
  }

  protected void naarVolgendeAkte() {

    for (DossierAkte akte : getZaakDossier().getDossier().getAktes()) {
      if (!akte.isCorrect()) {
        selectAkte(akte);
        return;
      }
    }
  }

  protected void selectAkte(DossierAkte akte) {

    if (getServices().getAkteService().getAkteRegisterDeel(akte) == null) {
      throw new ProException(WARNING, "Er is nog geen registerdeel ingesteld voor deze registersoort.");
    }

    getNavigation().removePage(BsAktePage2.class);
    getNavigation().goToPage(new BsAktePage2<T>(akte));
  }
}
