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

package nl.procura.gba.web.modules.bs.ontbinding.page30;

import java.util.List;

import nl.procura.gba.web.services.gba.basistabellen.belanghebbende.Belanghebbende;
import nl.procura.vaadin.component.container.ArrayListContainer;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class AdvocatenkantorenContainer extends ArrayListContainer {

  public static final FieldValue NVT    = new FieldValue("nvt", "Niet van toepassing");
  public static final FieldValue ANDERS = new FieldValue("anders", "Anders");

  public AdvocatenkantorenContainer(List<Belanghebbende> belanghebbenden) {

    addItem(NVT);

    for (Belanghebbende belanghebbende : belanghebbenden) {
      addItem(new FieldValue(belanghebbende));
    }

    addItem(ANDERS);
  }
}
