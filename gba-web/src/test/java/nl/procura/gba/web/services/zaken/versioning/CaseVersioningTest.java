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

package nl.procura.gba.web.services.zaken.versioning;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.junit.Assert;
import org.junit.Test;

import nl.procura.gba.web.services.zaken.ZaakServiceTest;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.gba.web.services.zaken.inhoudingen.DocumentInhouding;
import nl.procura.gba.web.services.zaken.inhoudingen.DocumentInhoudingenService;
import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.component.field.fieldvalues.AnrFieldValue;

public class CaseVersioningTest extends ZaakServiceTest {

  @Test
  public void versionCheckShouldFindCorrectCase() {
    DocumentInhouding initialCase1 = createNewCase("111");
    DocumentInhouding initialCase2 = createNewCase("222");
    DocumentInhouding initialCase3 = createNewCase("333");

    Assert.assertTrue(opslaan(initialCase1));
    Assert.assertTrue(opslaan(initialCase2));
    Assert.assertTrue(opslaan(initialCase3));

    Assert.assertTrue(opslaan(initialCase1));
    Assert.assertTrue(opslaan(initialCase2));
    Assert.assertTrue(opslaan(initialCase3));
  }

  @Test
  public void versionShouldBeSetAfterSave() {
    DocumentInhouding initialCase = createNewCase("444");
    Assert.assertNull(initialCase.getVersionTs());
    opslaan(initialCase);

    long instanceVersionTs = initialCase.getVersionTs().longValue();
    Assert.assertTrue(instanceVersionTs > 0);
  }

  @Test
  public void exceptionShouldBeThrownOnOldVersionSave() {
    DocumentInhouding initialCase = createNewCase("555");
    opslaan(initialCase);

    ZaakArgumenten zaakArgumenten = new ZaakArgumenten(initialCase.getZaakId());
    List<DocumentInhouding> cases = getService().getMinimalZaken(zaakArgumenten);
    DocumentInhouding storedCase = cases.get(0);

    // Save the loading db and update versionTS
    opslaan(storedCase);
    long storedVersionTs = storedCase.getVersionTs().longValue();
    Assert.assertTrue(storedVersionTs > initialCase.getVersionTs());

    // Updating old case no longer allowed
    try {
      opslaan(initialCase);
      Assert.fail();
    } catch (ProException e) {
      Assert.assertTrue(ExceptionUtils.getStackTrace(e).contains("gewijzigd"));
    }
    opslaan(storedCase);
  }

  private DocumentInhoudingenService getService() {
    return services.getDocumentInhoudingenService();
  }

  private DocumentInhouding createNewCase(String nrDoc) {
    DocumentInhouding newCase = (DocumentInhouding) getService().getNewZaak();
    newCase.setNummerDocument(nrDoc);
    newCase.setDInneming(-1L);
    newCase.setDIn(BigDecimal.valueOf(-1));
    newCase.setTIn(BigDecimal.valueOf(-1));
    newCase.setAnummer(new AnrFieldValue(-1L));
    return newCase;
  }
}
