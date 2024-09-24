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

package nl.procura.gba.web.services.bs.naturalisatie;

import static nl.procura.gba.jpa.personen.dao.GenericDao.createQuery;

import javax.persistence.TypedQuery;

import org.junit.Test;

import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.jpa.personen.db.Doss;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.zaken.ZaakServiceTest;

public class NaturalisatieServiceTest extends ZaakServiceTest {

  // TODO: Naturalisatie: test verder uitwerken

  //  private static final int        START_DATE          = 2018_01_01;
  //  private static final BigDecimal EXPECTED_START_DATE = new BigDecimal("20180101");
  //  private static final String     FIRST_NAME          = "Aangever";
  //  private static final int        NUMBER_OF_PEOPLE    = 1;

  private final NaturalisatieService naturalisatieService;

  public NaturalisatieServiceTest() {
    naturalisatieService = services.getNaturalisatieService();
  }

  @Test
  public void saveMustCreateNaturalisaties() {
    mustCreateNormalNaturalisatie();
    mustCreateNormalNaturalisatieWithVerwerkStatus();
  }

  private void mustCreateNormalNaturalisatie() {
    createNaturalisatie();
  }

  private void mustCreateNormalNaturalisatieWithVerwerkStatus() {
    Dossier zaak = createNaturalisatie();
    services.getNaturalisatieService().updateStatus(zaak, zaak.getStatus(), ZaakStatusType.VERWERKT, "");
  }

  private Dossier createNaturalisatie() {
    Dossier dossier = (Dossier) naturalisatieService.getNewZaak();
    //    dossier.setDatumIngang(new DateTime(START_DATE));
    //    DossierOnderzoek naturalisatie = (DossierOnderzoek) dossier.getZaakDossier();
    //    DossierPersoon aangever = naturalisatie.getAangever();
    //    aangever.setVoornaam(FIRST_NAME);
    //    aangever.toevoegenNationaliteit(NATIONALITY_1);

    // when
    naturalisatieService.save(dossier);

    // then generic dossier must be created
    TypedQuery<Doss> query = createQuery("SELECT d FROM Doss d " +
        "where d.zaakId = :zaakid", Doss.class);
    query.setParameter("zaakid", dossier.getZaakId());

    //    List<Doss> dossiers = query.getResultList();
    //    assertEquals(1, dossiers.size());
    //    assertEquals(EXPECTED_START_DATE, dossiers.get(0).getDIn());
    //
    //    // then both persons must be linked
    //    TypedQuery<DossPer> query1 = createQuery("SELECT d FROM DossPer d " +
    //        "where d.doss.zaakId = :zaakid", DossPer.class);
    //    query1.setParameter("zaakid", dossier.getZaakId());
    //
    //    List<DossPer> dossierPers = query1.getResultList();
    //    assertEquals(NUMBER_OF_PEOPLE, dossierPers.size());
    //    DossPer actualAangever = dossierPers.stream()
    //        .filter(p -> FIRST_NAME.equals(p.getVoorn()))
    //        .findFirst().orElseThrow(AssertionError::new);
    //    assertEquals(EXPECTED_START_DATE, actualAangever.getDoss().getDIn());

    return dossier;
  }
}
