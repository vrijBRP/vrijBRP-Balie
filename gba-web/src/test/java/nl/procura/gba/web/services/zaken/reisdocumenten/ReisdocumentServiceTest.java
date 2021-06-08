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

package nl.procura.gba.web.services.zaken.reisdocumenten;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import nl.procura.diensten.gba.ple.procura.arguments.PLEDatasource;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.web.services.zaken.ZaakServiceTest;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gbaws.testdata.Testdata;
import nl.procura.vaadin.component.field.fieldvalues.AnrFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;

public class ReisdocumentServiceTest extends ZaakServiceTest {

  @Test
  public void testZaak() {
    test(getAanvraagNietAfgesloten());
    test(getAanvraagUitgereikt());
    test(getAanvraagUitgereiktAndereInstantie());
  }

  private Zaak getAanvraagNietAfgesloten() {
    final ReisdocumentAanvraag zaak = getReisdocumentAanvraag();
    zaak.getReisdocumentStatus().setStatusAfsluiting(SluitingType.AANVRAAG_NIET_AFGESLOTEN);
    return zaak;
  }

  private Zaak getAanvraagUitgereikt() {
    final ReisdocumentAanvraag zaak = getReisdocumentAanvraag();
    zaak.getReisdocumentStatus().setDatumTijdAfsluiting(new DateTime());
    zaak.getReisdocumentStatus().setStatusAfsluiting(SluitingType.DOCUMENT_UITGEREIKT);
    return zaak;
  }

  private Zaak getAanvraagUitgereiktAndereInstantie() {
    final ReisdocumentAanvraag zaak = getReisdocumentAanvraag();
    zaak.getReisdocumentStatus().setDatumTijdAfsluiting(new DateTime());
    zaak.getReisdocumentStatus().setStatusAfsluiting(SluitingType.DOCUMENT_UITGEREIKT_DOOR_ANDERE_INSTANTIE);
    return zaak;
  }

  private void test(Zaak zaak2) {
    assertTrue(verwijder(zaak2));
    assertTrue(opslaan(zaak2));
    assertTrue(status(zaak2));
  }

  private ReisdocumentAanvraag getReisdocumentAanvraag() {
    // Aanvrager bepalen
    services.getPersonenWsService().getPersoonslijst(true, PLEDatasource.PROCURA, Testdata.TEST_BSN_10.toString());

    String zaakId = getNewZaakId(ZaakType.REISDOCUMENT);

    // Nieuwe zaak maken
    final ReisdocumentService service = services.getReisdocumentService();
    final ReisdocumentAanvraag zaak = (ReisdocumentAanvraag) service.getNewZaak();
    zaak.setZaakId(zaakId);

    // Standaard
    zaak.setBasisPersoon(services.getPersonenWsService().getPersoonslijst(Testdata.TEST_BSN_10.toString()));
    zaak.setAnummer(new AnrFieldValue(zaak.getBasisPersoon().getPersoon().getAnr().getVal()));
    zaak.setBurgerServiceNummer(new BsnFieldValue(zaak.getBasisPersoon().getPersoon().getBsn().getVal()));
    zaak.setDatumIngang(new DateTime());
    return zaak;
  }
}
