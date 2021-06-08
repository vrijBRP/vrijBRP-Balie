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

package nl.procura.gba.web.services.zaken.rijbewijs;

import static nl.procura.gba.web.services.zaken.inhoudingen.InhoudingType.VERMISSING;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.procura.arguments.PLEDatasource;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.web.components.fields.values.UsrFieldValue;
import nl.procura.gba.web.services.zaken.ZaakServiceTest;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.inhoudingen.DocumentInhoudingenService;
import nl.procura.gbaws.testdata.Testdata;
import nl.procura.standard.ProcuraDate;
import nl.procura.vaadin.component.field.fieldvalues.AnrFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;

public class RijbewijsServiceTest extends ZaakServiceTest {

  @Test
  public void testZaak() {
    test(getAanvraagUitgereikt());
    test(getAanvraagNietUitgereikt());
    test(getVermisteRijbewijzen());
    test(getVermisteRijbewijzenInDocInh());
  }

  private Zaak getAanvraagUitgereikt() {
    UsrFieldValue gebruiker = new UsrFieldValue(0, "Test");

    RijbewijsAanvraagStatus status = new RijbewijsAanvraagStatus();
    status.setDatumTijdRdw(new DateTime());
    status.setStatus(RijbewijsStatusType.RIJBEWIJS_UITGEREIKT);
    status.setGebruiker(gebruiker);

    RijbewijsAanvraag zaak = getRijbewijsAanvraag();
    zaak.setAanvraagNummer("456");
    zaak.setGebruikerAanvraag(gebruiker);
    zaak.setGebruikerUitgifte(gebruiker);
    zaak.getStatussen().addStatus(status);
    return zaak;
  }

  private Zaak getAanvraagNietUitgereikt() {
    UsrFieldValue gebruiker = new UsrFieldValue(0, "Test");

    RijbewijsAanvraagStatus status = new RijbewijsAanvraagStatus();
    status.setStatus(RijbewijsStatusType.RIJBEWIJS_NIET_UITGEREIKT);
    status.setGebruiker(gebruiker);

    RijbewijsAanvraag zaak = getRijbewijsAanvraag();
    zaak.setAanvraagNummer("123");
    zaak.setGebruikerAanvraag(gebruiker);
    zaak.setGebruikerUitgifte(gebruiker);
    zaak.getStatussen().addStatus(status);
    return zaak;
  }

  private Zaak getVermisteRijbewijzen() {
    UsrFieldValue gebruiker = new UsrFieldValue(0, "Test");

    RijbewijsAanvraagStatus status = new RijbewijsAanvraagStatus();
    status.setStatus(RijbewijsStatusType.RIJBEWIJS_NIET_UITGEREIKT);
    status.setGebruiker(gebruiker);

    RijbewijsAanvraag zaak = getRijbewijsAanvraag();
    zaak.setRedenAanvraag(RijbewijsAanvraagReden.WEGENS_VERLIES_OF_DIEFSTAL);
    zaak.setAanvraagNummer("789");
    zaak.setGebruikerAanvraag(gebruiker);
    zaak.setGebruikerUitgifte(gebruiker);
    zaak.getStatussen().addStatus(status);
    return zaak;
  }

  private void test(Zaak zaak) {
    assertTrue(verwijder(zaak));
    assertTrue(opslaan(zaak));
    assertTrue(status(zaak));
  }

  private Zaak getVermisteRijbewijzenInDocInh() {
    BasePLExt pl = services.getPersonenWsService().getPersoonslijst(Testdata.TEST_BSN_10.toString());
    String dIn = new ProcuraDate().getSystemDate();
    DocumentInhoudingenService documentInhoudingenService = services.getDocumentInhoudingenService();
    return documentInhoudingenService.setRijbewijsInhouding(pl, dIn, "1234", "4567", VERMISSING);
  }

  private RijbewijsAanvraag getRijbewijsAanvraag() {
    // Aanvrager bepalen
    services.getPersonenWsService().getPersoonslijst(true, PLEDatasource.PROCURA, Testdata.TEST_BSN_10.toString());

    String zaakId = getNewZaakId(ZaakType.RIJBEWIJS);

    // Nieuwe zaak maken
    final RijbewijsService service = services.getRijbewijsService();
    final RijbewijsAanvraag zaak = (RijbewijsAanvraag) service.getNewZaak();
    zaak.setZaakId(zaakId);

    // Standaard
    zaak.setBasisPersoon(services.getPersonenWsService().getPersoonslijst(Testdata.TEST_BSN_10.toString()));
    zaak.setAnummer(new AnrFieldValue(zaak.getBasisPersoon().getPersoon().getAnr().getVal()));
    zaak.setBurgerServiceNummer(new BsnFieldValue(zaak.getBasisPersoon().getPersoon().getBsn().getVal()));
    zaak.setDatumIngang(new DateTime());
    return zaak;
  }
}
