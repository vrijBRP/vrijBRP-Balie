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

package nl.procura.gba.web.modules.bs.onderzoek.aanschrijving;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.List;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;

import nl.procura.gba.web.services.bs.onderzoek.DossierOnderzoekBron;
import nl.procura.standard.Globalfunctions;
import nl.procura.vaadin.component.container.ProcuraContainer;

public class ExterneBronContainer extends IndexedContainer implements ProcuraContainer {

  public ExterneBronContainer(List<DossierOnderzoekBron> bronnen) {
    addContainerProperty(OMSCHRIJVING, String.class, "");
    removeAllItems();

    if (bronnen.isEmpty()) {
      Item item = addItem(new DossierOnderzoekBron());
      item.getItemProperty(OMSCHRIJVING).setValue("<Geen externe bron>");

    } else {
      for (DossierOnderzoekBron bron : bronnen) {
        Item item = addItem(bron);
        String inst = bron.getInst();
        String tav = bron.getInstTav();
        StringBuilder naam = new StringBuilder();

        if (isNotBlank(inst)) {
          naam.append(inst);
        }

        if (isNotBlank(bron.getInstAfdeling())) {
          naam.append(" ");
          naam.append(bron.getInstAfdeling());
        }

        if (isNotBlank(tav)) {
          naam.append(" (t.a.v. ");
          naam.append(tav);
          naam.append(")");
        }

        item.getItemProperty(OMSCHRIJVING).setValue(Globalfunctions.trim(naam.toString()));
      }
    }
  }
}
