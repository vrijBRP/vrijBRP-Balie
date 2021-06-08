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

package nl.procura.gba.web.services.zaken.tmv;

import static org.junit.Assert.assertTrue;

import nl.procura.gba.jpa.personen.db.TerugmTmvPK;
import nl.procura.gba.web.components.fields.values.UsrFieldValue;
import org.junit.Test;

import nl.procura.diensten.gba.ple.procura.arguments.PLEDatasource;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.web.services.zaken.ZaakServiceTest;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gbaws.testdata.Testdata;
import nl.procura.vaadin.component.field.fieldvalues.AnrFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;

import java.math.BigDecimal;

public class TerugmeldingServiceTest extends ZaakServiceTest {

  @Test
  public void testZaak() {
    TerugmeldingAanvraag zaak = getNieuweZaak();
    assertTrue(verwijder(zaak));
    assertTrue(opslaan(zaak));
    assertTrue(status(addRegistratie(zaak)));
  }

  private Zaak addRegistratie(TerugmeldingAanvraag zaak) {
    TerugmeldingRegistratie registratie = new TerugmeldingRegistratie();

    TerugmTmvPK id = new TerugmTmvPK();
    id.setDIn(2018_01_01);
    id.setTIn(101112);
    id.setDossiernummer(123);

    registratie.setId(id);
    registratie.getId().setDossiernummer(123);
    registratie.setBerichtcode("ONBV");
    registratie.setCUsr(BigDecimal.valueOf(0));

    final TerugmeldingService service = services.getTerugmeldingService();
    service.saveRegistratie(zaak, registratie);
    return zaak;
  }

  public TerugmeldingAanvraag getNieuweZaak() {

    // Aanvrager bepalen
    services.getPersonenWsService().getPersoonslijst(true, PLEDatasource.PROCURA, Testdata.TEST_BSN_10.toString());

    String zaakId = getNewZaakId(ZaakType.TERUGMELDING);

    // Nieuwe zaak maken
    final TerugmeldingService service = services.getTerugmeldingService();
    final TerugmeldingAanvraag zaak = (TerugmeldingAanvraag) service.getNewZaak();
    zaak.setZaakId(zaakId);

    // Standaard
    zaak.setBasisPersoon(services.getPersonenWsService().getPersoonslijst(Testdata.TEST_BSN_10.toString()));
    zaak.setAnummer(new AnrFieldValue(zaak.getBasisPersoon().getPersoon().getAnr().getVal()));
    zaak.setBurgerServiceNummer(new BsnFieldValue(zaak.getBasisPersoon().getPersoon().getBsn().getVal()));
    zaak.setDatumIngang(new DateTime());

    // Zaakspecifiek
    zaak.setTerugmelding("Toelichting");

    return zaak;
  }
}
