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

package nl.procura.curatele.soap;

import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import nl.procura.soap.listeners.DefaultListener;

/**
 * Wijzigt WS:TO in de URL zonder proxy
 */
public class WSASoapHandler extends DefaultListener {

  @Override
  public boolean handleRequest(SOAPMessageContext context) {

    try {
      NodeList nodeList = context.getMessage().getSOAPHeader().getChildNodes();
      for (int i = 0; i < nodeList.getLength(); i++) {
        Node node = nodeList.item(i);

        if ("to".equalsIgnoreCase(node.getNodeName())) {
          String[] values = node.getTextContent().split("url=");

          if (values.length > 1) {
            node.setTextContent(values[1]);
          }
        }
      }

    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    return true;
  }
}
