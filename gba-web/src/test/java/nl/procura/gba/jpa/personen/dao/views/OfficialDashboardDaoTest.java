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

package nl.procura.gba.jpa.personen.dao.views;

import static nl.procura.gba.jpa.personen.dao.views.OfficialDashboardDao.*;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;

import nl.procura.gba.web.services.bs.onderzoek.OnderzoekServiceTest;
import nl.procura.gba.web.services.zaken.inhoudingen.DocumentInhoudingenServiceTest;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentServiceTest;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsServiceTest;
import nl.procura.gba.web.services.zaken.tmv.TerugmeldingServiceTest;
import org.junit.Assert;
import org.junit.Test;

import nl.procura.gba.jpa.personen.dao.ZaakKey;
import nl.procura.gba.web.services.bs.erkenning.ErkenningServiceTest;
import nl.procura.gba.web.services.bs.geboorte.GeboorteServiceTest;
import nl.procura.gba.web.services.bs.registration.RegistrationServiceTest;
import nl.procura.gba.web.services.zaken.documenten.aanvragen.DocumentZakenServiceTest;
import nl.procura.gba.web.services.zaken.geheim.VerstrekkingsBeperkingServiceTest;
import nl.procura.gba.web.services.zaken.naamgebruik.NaamgebruikWijzigingServiceTest;
import nl.procura.gba.web.services.zaken.verhuizing.VerhuizingServiceTest;
import nl.procura.gba.web.services.zaken.vog.VogsServiceTest;

public class OfficialDashboardDaoTest {

  @Test
  public void mustFindGeboortes() {
    new GeboorteServiceTest().saveMustCreateValidDossierEntities();
    test(OfficialDashboardDao::getGeboorten12);
  }

  @Test
  public void mustFindErkenningen() {
    new ErkenningServiceTest().saveMustCreateValidDossierEntities();
    test(OfficialDashboardDao::getErkenningen13);
  }

  @Test
  public void mustFindUittreksels() {
    new DocumentZakenServiceTest().testZaak();
    Assert.assertEquals(2, getUittreksels31(false, getPeriodeThisYear()).size());
    Assert.assertEquals(0, getUittreksels31(false, getPeriodeLastYear()).size());
    Assert.assertEquals(0, getUittreksels31(true, getPeriodeThisYear()).size());
    Assert.assertEquals(1, getUittreksels31(true, getPeriodeThisYearMidoffice()).size());
  }

  @Test
  public void mustFindVogAanvragen() {
    new VogsServiceTest().testZaak();
    test(OfficialDashboardDao::getVog33);
  }

  @Test
  public void mustFindNaamgebruik() {
    new NaamgebruikWijzigingServiceTest().testZaak();
    Assert.assertEquals(1, getNaamgebruik34(false, getPeriodeThisYear()).size());
    Assert.assertEquals(0, getNaamgebruik34(true, getPeriodeLastYear()).size());
    Assert.assertEquals(0, getNaamgebruik34(true, getPeriodeThisYearMidoffice()).size());
  }

  @Test
  public void mustFindGeheimhouding() {
    new VerstrekkingsBeperkingServiceTest().testZaak();
    Assert.assertEquals(1, getGeheimhouding35(false, getPeriodeThisYear()).size());
    Assert.assertEquals(0, getGeheimhouding35(true, getPeriodeLastYear()).size());
    Assert.assertEquals(0, getGeheimhouding35(true, getPeriodeThisYearMidoffice()).size());
  }

  @Test
  public void mustFindVerhuizingen() {
    new VerhuizingServiceTest().testZaak();
    Assert.assertEquals(1, getVerhuizingen41(false, getPeriodeThisYear()).size());
    Assert.assertEquals(0, getVerhuizingen41(true, getPeriodeLastYear()).size());
    Assert.assertEquals(0, getVerhuizingen41(true, getPeriodeThisYearMidoffice()).size());
  }

  @Test
  public void mustFindVestigingen() {
    new RegistrationServiceTest().saveRegistrationMustSaveGenericDossierAndSpecificDossier();
    new VerhuizingServiceTest().testZaak();
    Assert.assertEquals(1, getVerhuizingen42(getPeriodeThisYear()).size());
    Assert.assertEquals(0, getVerhuizingen42(getPeriodeLastYear()).size());
  }

  @Test
  public void mustFindReisdocumenten() {
    new ReisdocumentServiceTest().testZaak();
    Assert.assertEquals(3, getReisdocumenten61(getPeriodeThisYear()).size());
    Assert.assertEquals(2, getReisdocumenten62(getPeriodeThisYear()).size());
    Assert.assertEquals(0, getReisdocumenten62(getPeriodeLastYear()).size());
  }

  @Test
  public void mustFindInhoudingen() {
    new DocumentInhoudingenServiceTest().testZaak();
    Assert.assertEquals(1, getReisdocumenten63(getPeriodeThisYear()).size());
    Assert.assertEquals(0, getReisdocumenten63(getPeriodeLastYear()).size());
  }

  @Test
  public void mustFindRijbewijzen() {
    new RijbewijsServiceTest().testZaak();
    Assert.assertEquals(3, getRijbewijzen71(getPeriodeThisYear()).size());
    Assert.assertEquals(1, getRijbewijzen72(getPeriodeThisYear()).size());
    Assert.assertEquals(2, getRijbewijzen73(getPeriodeThisYear()).size());
    Assert.assertEquals(0, getRijbewijzen71(getPeriodeLastYear()).size());
  }

  @Test
  public void mustFindOnderzoeken() {
    new OnderzoekServiceTest().saveMustCreateOnderzoeken();
    Assert.assertEquals(2, getOnderzoeken102(getPeriodeThisYear()).size());
    Assert.assertEquals(1, getOnderzoeken103(getPeriodeThisYear()).size());
    Assert.assertEquals(0, getRijbewijzen71(getPeriodeLastYear()).size());
  }

  @Test
  public void mustFindTerugmeldingen() {
    new TerugmeldingServiceTest().testZaak();
    Assert.assertEquals(1, getTerugmeldingen105(getPeriodeThisYear()).size());
    Assert.assertEquals(0, getTerugmeldingen105(getPeriodeLastYear()).size());
  }

  private DashboardDao.DashboardPeriode getPeriodeLastYear() {
    int year = LocalDate.now().minusYears(-1).getYear();
    long from = Long.valueOf(year + "0101");
    long to = Long.valueOf(year + "1231");
    return new DashboardDao.DashboardPeriode(from, to, "BRON", "LEVERANCIER");
  }

  private DashboardDao.DashboardPeriode getPeriodeThisYear() {
    int year = LocalDate.now().getYear();
    long from = Long.valueOf(year + "0101");
    long to = Long.valueOf(year + "1231");
    return new DashboardDao.DashboardPeriode(from, to, "BRON", "LEVERANCIER");
  }

  private DashboardDao.DashboardPeriode getPeriodeThisYearMidoffice() {
    int year = LocalDate.now().getYear();
    long from = Long.valueOf(year + "0101");
    long to = Long.valueOf(year + "1231");
    return new DashboardDao.DashboardPeriode(from, to, "MY_BRON", "MY_LEV");
  }

  private void hasNone(Function<DashboardDao.DashboardPeriode, List<ZaakKey>> mapper) {
    Assert.assertEquals(0, test(mapper));
  }

  private void hasOne(Function<DashboardDao.DashboardPeriode, List<ZaakKey>> mapper) {
    Assert.assertEquals(1, test(mapper));
  }

  private int test(Function<DashboardDao.DashboardPeriode, List<ZaakKey>> mapper) {
    Assert.assertEquals(0, mapper.apply(getPeriodeLastYear()).size());
    return mapper.apply(getPeriodeThisYear()).size();
  }
}
