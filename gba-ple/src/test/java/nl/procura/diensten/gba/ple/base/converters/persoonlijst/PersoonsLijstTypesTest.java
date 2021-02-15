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

package nl.procura.diensten.gba.ple.base.converters.persoonlijst;

import java.text.MessageFormat;

import org.junit.Assert;
import org.junit.Test;

import nl.procura.burgerzaken.gba.core.enums.GBAGroupElements;

public class PersoonsLijstTypesTest {

  @Test
  public void mustContainAllElements() {
    GBAGroupElements.getAll().forEach(elem -> Assert.assertNotNull(
        MessageFormat.format("Missing element ''{0}'' - ''{1}''",
            elem.getCat().getDescr(), elem.getElem().getDescr()),
        PersoonsLijstTypes.getName(elem.getCat().getCode(), elem.getElem().getCode())));
  }
}
