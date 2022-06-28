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

package nl.procura.gba.web.modules.hoofdmenu.klapper.page3;

import java.util.List;
import java.util.function.Consumer;

import nl.procura.gba.web.services.bs.algemeen.akte.DossierAkte;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.VLayout;

public class KlapperKoppelLayout extends VLayout {

  public KlapperKoppelLayout(List<DossierAkte> dossierAktes, Consumer<DossierAkte> consumer) {
    setSizeFull();
    addComponent(new Fieldset("Gekoppelde klappers"));
    addExpandComponent(new Page3KlapperTable(dossierAktes) {

      @Override
      public void onClick(Record record) {
        consumer.accept(record.getObject(DossierAkte.class));
      }
    });
  }
}
