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

package nl.procura.gba.web.modules.bs.onderzoek.page20;

import java.util.Arrays;

import com.vaadin.data.Item;

import nl.procura.gba.web.components.containers.GbaContainer;
import nl.procura.gba.web.services.bs.onderzoek.enums.AanduidingOnderzoekType;
import nl.procura.vaadin.component.container.ProcuraContainer;

public class AanduidingOnderzoekContainer extends GbaContainer implements ProcuraContainer {

  public AanduidingOnderzoekContainer() {
    addContainerProperty(OMSCHRIJVING, String.class, "");
    Arrays.stream(AanduidingOnderzoekType.values()).forEach(aand -> {
      Item item = addItem(aand.getCode());
      item.getItemProperty(OMSCHRIJVING).setValue(aand.getOms());
    });
  }
}
