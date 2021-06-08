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

package nl.procura.gba.web.services.zaken.inhoudingen;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import nl.procura.diensten.gba.ple.base.BasePLValue;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.web.services.zaken.ZaakServiceTest;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.reisdocumenten.Reisdocument;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentType;
import nl.procura.gbaws.testdata.Testdata;
import nl.procura.standard.ProcuraDate;

public class DocumentInhoudingenServiceTest extends ZaakServiceTest {

  @Test
  public void testZaak() {
    test(getIngehoudenReisdocumenten());
    test(getVermisteReisdocumenten());
    test(getVermisteRijbewijzen());
  }

  private Zaak getIngehoudenReisdocumenten() {
    BasePLExt pl = services.getPersonenWsService().getPersoonslijst(Testdata.TEST_BSN_10.toString());
    Reisdocument reisdoc = new Reisdocument();
    reisdoc.setNummerDocument(new BasePLValue("456"));
    reisdoc.setNederlandsReisdocument(new BasePLValue(ReisdocumentType.EERSTE_NATIONAAL_PASPOORT.getCode()));
    return services.getDocumentInhoudingenService().setReisdocumentInhouding(pl, reisdoc, InhoudingType.INHOUDING);
  }

  private Zaak getVermisteReisdocumenten() {
    BasePLExt pl = services.getPersonenWsService().getPersoonslijst(Testdata.TEST_BSN_10.toString());
    Reisdocument reisdoc = new Reisdocument();
    reisdoc.setNummerDocument(new BasePLValue("123"));
    reisdoc.setNederlandsReisdocument(new BasePLValue(ReisdocumentType.EERSTE_NATIONAAL_PASPOORT.getCode()));
    return services.getDocumentInhoudingenService().setReisdocumentInhouding(pl, reisdoc, InhoudingType.VERMISSING);
  }

  private Zaak getVermisteRijbewijzen() {
    BasePLExt pl = services.getPersonenWsService().getPersoonslijst(Testdata.TEST_BSN_10.toString());
    String dIn = new ProcuraDate().getSystemDate();
    return services.getDocumentInhoudingenService().setRijbewijsInhouding(pl,
        dIn, "65646", "213123", InhoudingType.VERMISSING);
  }

  private void test(Zaak zaak) {
    assertTrue(verwijder(zaak));
    assertTrue(opslaan(zaak));
    assertTrue(status(zaak));
  }
}
