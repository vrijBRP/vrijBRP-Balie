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

package nl.procura.gba.web.modules.bs.geboorte.page90;

import nl.procura.gba.web.modules.bs.common.pages.aktepage.page1.BsAktePage1;
import nl.procura.gba.web.services.bs.geboorte.DossierGeboorte;

/**
 * Geboorte
 */

public class Page90Geboorte<T extends DossierGeboorte> extends BsAktePage1<T> {

  public Page90Geboorte() {
    this("Geboorte - aktenummer");
  }

  public Page90Geboorte(String caption) {
    super(caption);
  }

  @Override
  public void onPreviousPage() {
    goToPreviousProces();
  }
}
