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

package nl.procura.gba.web.modules.bs.geboorte.page60;

import nl.procura.gba.web.common.misc.Landelijk;
import nl.procura.gba.web.modules.bs.common.utils.BsRechtContainer;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.bs.geboorte.DossierGeboorte;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class Page60RechtContainer extends BsRechtContainer {

  public Page60RechtContainer(Services services, DossierGeboorte geboorte, FieldValue land) {

    super(services);

    add(land);
    add(Landelijk.getNederland());
    addPersoonNationaliteiten(geboorte.getVader());
    addPersoonNationaliteiten(geboorte.getMoeder());
    addPersoonLand(geboorte.getVader());
    addPersoonLand(geboorte.getMoeder());
  }
}
