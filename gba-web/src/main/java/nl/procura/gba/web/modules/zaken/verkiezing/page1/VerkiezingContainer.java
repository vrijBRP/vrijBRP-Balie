/*
 * Copyright 2022 - 2023 Procura B.V.
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

package nl.procura.gba.web.modules.zaken.verkiezing.page1;

import java.util.List;

import org.apache.commons.lang3.builder.CompareToBuilder;

import com.vaadin.data.Item;

import nl.procura.gba.web.components.containers.GbaContainer;
import nl.procura.gba.web.services.beheer.verkiezing.Verkiezing;
import nl.procura.vaadin.component.container.ProcuraContainer;

public class VerkiezingContainer extends GbaContainer implements ProcuraContainer {

  public VerkiezingContainer(List<Verkiezing> verkiezingen) {
    addContainerProperty(OMSCHRIJVING, String.class, "");
    verkiezingen.sort((o1, o2) -> new CompareToBuilder()
        .append(o1.getVerk().getdVerk(), o2.getVerk().getdVerk())
        .append(o1.getVerk().getVerkiezing(), o2.getVerk().getVerkiezing())
        .build());

    verkiezingen.forEach(verkiezing -> {
      Item item = addItem(verkiezing);
      String descr = verkiezing.toString();
      if (verkiezing.getStempassen().isEmpty()) {
        descr += " (niet ingedeeld)";
      }
      item.getItemProperty(OMSCHRIJVING).setValue(descr);
    });
  }
}
