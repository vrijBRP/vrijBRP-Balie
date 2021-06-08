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

import static nl.procura.gbaws.testdata.Testdata.getPersonData;
import static nl.procura.gbaws.testdata.Testdata.getPersonDataAsBytes;
import static nl.procura.gbaws.testdata.Testdata.DataSet.DEMO;
import static nl.procura.gbaws.testdata.Testdata.DataSet.RVIG;

import java.lang.reflect.Field;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import nl.procura.gbaws.generator.GeneratePLJsonFiles;

public class TestdataTest {

  @Test
  public void mustFindAllJsonFilesFromDemo() throws IllegalAccessException {
    for (Field field : Testdata.class.getFields()) {
      long nr = Long.parseLong(String.valueOf(field.get(null)));
      Assert.assertEquals(1, getPersonData(nr, DEMO).getBasePLs().size());
    }
  }

  @Test
  public void mustFindAllJsonFilesFromRvigFile() {
    List<Long> numbers = GeneratePLJsonFiles.getRvIGBsns();
    Assert.assertEquals(436, getPersonData(numbers, RVIG).getBasePLs().size());
  }

  @Test
  public void mustFindSpecificFileFromRvigFile() {
    Assert.assertTrue(new String(getPersonDataAsBytes(9842L, RVIG)).contains("4537853450"));
  }

  @Test
  public void mustNotFindSpecificFileFromRvigFile() {
    try {
      getPersonDataAsBytes(1234L, RVIG);
    } catch (IllegalStateException e) {
      Assert.assertEquals("Personal data of 1234 has not been found", e.getMessage());
    }
  }
}
