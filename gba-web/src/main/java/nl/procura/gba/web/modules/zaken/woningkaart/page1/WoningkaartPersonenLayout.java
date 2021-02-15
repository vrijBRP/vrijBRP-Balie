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

package nl.procura.gba.web.modules.zaken.woningkaart.page1;

import java.text.MessageFormat;

import nl.procura.diensten.gba.wk.extensions.BaseWKExt;
import nl.procura.vaadin.component.layout.info.InfoLayout;

/**
 * Toont het aantal bewoners in een infoLayout
 */
public class WoningkaartPersonenLayout extends InfoLayout {

  public WoningkaartPersonenLayout set(BaseWKExt wk) {

    int count = wk.getCurrentResidentsCount();
    StringBuilder message = new StringBuilder();

    switch (count) {
      case 0:
        message.append(MessageFormat.format("Op {0} wonen momenteel geen personen.", wk.getAdres()));
        break;

      case 1:
        message.append(MessageFormat.format("Op {0} woont momenteel <b>1</b> persoon.", wk.getAdres()));
        break;

      default:
        message.append(
            MessageFormat.format("Op {0} wonen momenteel <b>{1}</b> personen.", wk.getAdres(), count));
        break;
    }

    setMessage(message.toString());

    return this;
  }

  public WoningkaartPersonenLayout setNoResults() {

    StringBuilder message = new StringBuilder();

    message.append("Dit adres komt niet voor binnen de gemeente.");

    setMessage(message.toString());

    return this;
  }
}
