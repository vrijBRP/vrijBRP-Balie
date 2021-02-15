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

package nl.procura.gba.web.modules.zaken.verhuizing.overzicht.bewoners.page1;

import java.text.MessageFormat;

import nl.procura.vaadin.component.layout.info.InfoLayout;

public class Page1BewonersInfoLayout extends InfoLayout {

  public void update(String adres, int aantal, int geheim) {

    StringBuilder message = new StringBuilder();

    switch (aantal) {
      case 0:
        message.append(MessageFormat.format("Op {0} wonen momenteel geen personen.", adres));
        break;

      case 1:
        message.append(MessageFormat.format("Op {0} woont momenteel <b>1</b> persoon.", adres));
        break;

      default:
        message.append(MessageFormat.format("Op {0} wonen momenteel <b>{1}</b> personen.", adres, aantal));
        break;
    }

    switch (geheim) {
      case 0:
        break;

      case 1:
        message.append("De persoon wordt echter niet getoond vanwege een verstrekkingsbeperking");
        break;

      default:
        message.append(
            "<b>" + geheim + "</b> personen worden echter niet getoond vanwege een verstrekkingsbeperking.");
    }

    setMessage(message.toString());
  }
}
