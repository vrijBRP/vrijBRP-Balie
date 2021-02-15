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

package personen;

import org.junit.Assert;
import org.junit.Test;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.burgerzaken.gba.core.enums.GBARecStatus;
import nl.procura.diensten.gba.ple.base.BasePL;
import nl.procura.diensten.gba.ple.base.BasePLBuilder;
import nl.procura.diensten.gba.ple.base.BasePLValue;
import nl.procura.diensten.gba.ple.procura.arguments.PLEDatasource;

/**
 * This tests that the historic marriage records are added to the current divorce records
 */
public class PLCommitmentMergingTest {

  @Test
  public void mergeCommitmentWithDissolvement() {
    BasePL marriage = getEndedMarriage();

    // The first record should have the marriage elements
    Assert.assertEquals("20140101", getVal(marriage, 0, GBAElem.DATUM_VERBINTENIS));
    Assert.assertEquals("0361", getVal(marriage, 0, GBAElem.PLAATS_VERBINTENIS));
    Assert.assertEquals("6030", getVal(marriage, 0, GBAElem.LAND_VERBINTENIS));
    Assert.assertEquals("20150101", getVal(marriage, 0, GBAElem.DATUM_ONTBINDING));
    Assert.assertEquals("Parijs", getVal(marriage, 0, GBAElem.PLAATS_ONTBINDING));
    Assert.assertEquals("5002", getVal(marriage, 0, GBAElem.LAND_ONTBINDING));

    // The second record should also have the marriage records
    Assert.assertEquals("20140101", getVal(marriage, 1, GBAElem.DATUM_VERBINTENIS));
    Assert.assertEquals("0361", getVal(marriage, 1, GBAElem.PLAATS_VERBINTENIS));
    Assert.assertEquals("6030", getVal(marriage, 1, GBAElem.LAND_VERBINTENIS));
    Assert.assertEquals("20140601", getVal(marriage, 1, GBAElem.DATUM_ONTBINDING));
    Assert.assertEquals("Parijs", getVal(marriage, 1, GBAElem.PLAATS_ONTBINDING));
    Assert.assertEquals("5002", getVal(marriage, 1, GBAElem.LAND_ONTBINDING));

    // The third record should only have latest marriage record
    Assert.assertEquals("20140101", getVal(marriage, 2, GBAElem.DATUM_VERBINTENIS));
    Assert.assertEquals("0361", getVal(marriage, 2, GBAElem.PLAATS_VERBINTENIS));
    Assert.assertEquals("6030", getVal(marriage, 2, GBAElem.LAND_VERBINTENIS));
    Assert.assertEquals("", getVal(marriage, 2, GBAElem.DATUM_ONTBINDING));
    Assert.assertEquals("", getVal(marriage, 2, GBAElem.PLAATS_ONTBINDING));
    Assert.assertEquals("", getVal(marriage, 2, GBAElem.LAND_ONTBINDING));

    // The fourth record should only have older marriage record
    Assert.assertEquals("20130101", getVal(marriage, 3, GBAElem.DATUM_VERBINTENIS));
    Assert.assertEquals("0398", getVal(marriage, 3, GBAElem.PLAATS_VERBINTENIS));
    Assert.assertEquals("6030", getVal(marriage, 3, GBAElem.LAND_VERBINTENIS));
    Assert.assertEquals("", getVal(marriage, 3, GBAElem.DATUM_ONTBINDING));
    Assert.assertEquals("", getVal(marriage, 3, GBAElem.PLAATS_ONTBINDING));
    Assert.assertEquals("", getVal(marriage, 3, GBAElem.LAND_ONTBINDING));
  }

  private String getVal(BasePL marriage, int index, GBAElem gbaElem) {
    return marriage.getCat(GBACat.HUW_GPS).getSets().get(0).getRecs().get(index).getElemVal(gbaElem).getVal();
  }

  private BasePL getEndedMarriage() {

    BasePLBuilder builder = new BasePLBuilder();
    builder.addNewPL(PLEDatasource.PROCURA);

    // Most recent divorce record
    builder.addCat(GBACat.HUW_GPS, 10, GBARecStatus.CURRENT);
    builder.setElem(GBAElem.GESLACHTSNAAM, new BasePLValue("Jansen"));
    builder.setElem(GBAElem.SOORT_VERBINTENIS, new BasePLValue("H"));
    builder.setElem(GBAElem.DATUM_ONTBINDING, new BasePLValue("20150101"));
    builder.setElem(GBAElem.PLAATS_ONTBINDING, new BasePLValue("Parijs", "Parijs"));
    builder.setElem(GBAElem.LAND_ONTBINDING, new BasePLValue("5002", "Frankrijk"));

    // Overruled divorce record
    builder.addCat(GBACat.HUW_GPS, 10, GBARecStatus.HIST);
    builder.setElem(GBAElem.GESLACHTSNAAM, new BasePLValue("Jansen"));
    builder.setElem(GBAElem.SOORT_VERBINTENIS, new BasePLValue("H"));
    builder.setElem(GBAElem.DATUM_ONTBINDING, new BasePLValue("20140601"));
    builder.setElem(GBAElem.PLAATS_ONTBINDING, new BasePLValue("Parijs", "Parijs"));
    builder.setElem(GBAElem.LAND_ONTBINDING, new BasePLValue("5002", "Frankrijk"));

    // Most recent marriage record
    builder.addCat(GBACat.HUW_GPS, 10, GBARecStatus.HIST);
    builder.setElem(GBAElem.GESLACHTSNAAM, new BasePLValue("Jansen"));
    builder.setElem(GBAElem.DATUM_VERBINTENIS, new BasePLValue("20140101"));
    builder.setElem(GBAElem.SOORT_VERBINTENIS, new BasePLValue("H"));
    builder.setElem(GBAElem.PLAATS_VERBINTENIS, new BasePLValue("0361", "Alkmaar"));
    builder.setElem(GBAElem.LAND_VERBINTENIS, new BasePLValue("6030", "Nederland"));

    // Older marriage records that have been overruled
    builder.addCat(GBACat.HUW_GPS, 10, GBARecStatus.HIST);
    builder.setElem(GBAElem.GESLACHTSNAAM, new BasePLValue("Jansen"));
    builder.setElem(GBAElem.DATUM_VERBINTENIS, new BasePLValue("20130101"));
    builder.setElem(GBAElem.SOORT_VERBINTENIS, new BasePLValue("H"));
    builder.setElem(GBAElem.PLAATS_VERBINTENIS, new BasePLValue("0398", "Heerhugowaard"));
    builder.setElem(GBAElem.LAND_VERBINTENIS, new BasePLValue("6030", "Nederland"));

    return builder.finishPL().getPL();
  }
}
