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

package nl.procura.gba.web.modules.bs.onderzoek.page30.gesprekwindow;

import nl.procura.gba.web.components.layouts.GbaVerticalLayout;
import nl.procura.gba.web.services.bs.onderzoek.DossierOnderzoekBron;
import nl.procura.gba.web.services.bs.onderzoek.enums.VermoedAdresType;

public abstract class AbstractBronLayout extends GbaVerticalLayout {

  public abstract void reset();

  public abstract void save();

  protected DossierOnderzoekBron resetBron(DossierOnderzoekBron bron) {
    bron.setInstNaam("");
    bron.setInstAfdeling("");
    bron.setInstAanhef("");
    bron.setInstNaam("");
    bron.setInstVoorl("");
    bron.setInstEmail("");
    bron.setAdr("");
    bron.setHnr("");
    bron.setHnrL("");
    bron.setHnrT("");
    bron.setHnrA("");
    bron.setPc("");
    bron.setPlaats("");
    bron.setAdresType(VermoedAdresType.ONBEKEND);
    return bron;
  }
}
