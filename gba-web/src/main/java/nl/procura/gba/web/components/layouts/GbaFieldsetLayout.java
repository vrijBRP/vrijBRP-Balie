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

package nl.procura.gba.web.components.layouts;

import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.info.InfoLayout;

public class GbaFieldsetLayout extends GbaVerticalLayout {

  private InfoLayout tekst = new InfoLayout();

  public GbaFieldsetLayout(String header) {
    addComponent(new Fieldset(header));
  }

  public void setToelichting(String oms) {

    InfoLayout nieuwTekst = new InfoLayout("", oms);

    if (tekst.getParent() == null) {
      addComponent(nieuwTekst, 1);
    } else {
      replaceComponent(tekst, nieuwTekst);
    }

    tekst = nieuwTekst;
  }
}
