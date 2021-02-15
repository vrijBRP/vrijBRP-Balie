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

package nl.procura.gba.web.modules.zaken.rijbewijs.page5;

import static nl.procura.standard.Globalfunctions.along;

import java.util.List;

import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsAanvraagSoort;
import nl.procura.rdw.processen.p1651.f08.AANVRRYBKGEG;
import nl.procura.vaadin.component.container.ArrayListContainer;

public class RijbewijsAanvraagSoortContainer extends ArrayListContainer {

  public RijbewijsAanvraagSoortContainer(List<AANVRRYBKGEG> list) {
    for (AANVRRYBKGEG a : list) {
      addItem(RijbewijsAanvraagSoort.get(along(a.getSrtaanvrrybk())));
    }
  }
}
