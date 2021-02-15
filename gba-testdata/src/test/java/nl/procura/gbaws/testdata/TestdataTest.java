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

package nl.procura.gbaws.testdata;

import java.lang.reflect.Field;

import org.junit.Assert;
import org.junit.Test;

public class TestdataTest {

  @Test
  public void mustFindAllJsonFiles() throws IllegalAccessException {
    for (Field field : Testdata.class.getFields()) {
      long nr = Long.parseLong(String.valueOf(field.get(null)));
      Assert.assertEquals(1, Testdata.getPersonalData(nr).getBasePLs().size());
    }
  }
}
