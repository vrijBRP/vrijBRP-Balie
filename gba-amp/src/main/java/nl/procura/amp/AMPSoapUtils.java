/*
 * Copyright 2024 - 2025 Procura B.V.
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

package nl.procura.amp;

import java.util.List;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import nl.amp.api.domain.ArrayOfInTeHoudenReisdocument;
import nl.amp.api.domain.InTeHoudenReisdocument;
import nl.amp.api.domain.ObjectFactory;

public class AMPSoapUtils {

  /*
    * Convert a list of InTeHoudenReisdocument to a JAXBElement<ArrayOfInTeHoudenReisdocument>
    * Is needed because the method in ObjectFactory used the wrong localPart
   */
  public static JAXBElement<ArrayOfInTeHoudenReisdocument> toJaxbElement(List<InTeHoudenReisdocument> docs) {
    ObjectFactory of = new ObjectFactory();
    QName qNAme = new QName("http://thuisbezorgendocumenten.nl/reisdocument/v1/", "InTeHoudenReisdocumenten");
    ArrayOfInTeHoudenReisdocument value = of.createArrayOfInTeHoudenReisdocument().withInTeHoudenReisdocument(docs);
    return new JAXBElement<>(qNAme, ArrayOfInTeHoudenReisdocument.class, null, value);
  }
}
