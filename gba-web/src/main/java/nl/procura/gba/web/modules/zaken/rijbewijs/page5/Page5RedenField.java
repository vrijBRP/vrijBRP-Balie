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

import com.vaadin.data.Container;

import nl.procura.gba.web.components.fields.GbaNativeSelect;

public class Page5RedenField extends GbaNativeSelect {

  public Page5RedenField() {
  }

  @Override
  public void setContainerDataSource(Container ds) {

    super.setContainerDataSource(ds);

    // Als er maar 1 reden. Dan die selecteren

    if (ds.getItemIds().size() == 1) {

      select(ds.getItemIds().iterator().next());
    }
  }
}
