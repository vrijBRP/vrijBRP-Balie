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

import static nl.procura.diensten.gba.ple.extensions.formats.BurgerlijkeStaatType.*;

import org.junit.Assert;
import org.junit.Test;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.burgerzaken.gba.core.enums.GBARecStatus;
import nl.procura.diensten.gba.ple.base.BasePL;
import nl.procura.diensten.gba.ple.base.BasePLBuilder;
import nl.procura.diensten.gba.ple.base.BasePLValue;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.extensions.formats.BurgerlijkeStaat;
import nl.procura.diensten.gba.ple.extensions.formats.BurgerlijkeStaatType;
import nl.procura.diensten.gba.ple.procura.arguments.PLEDatasource;

public class PLCivilStateTest {

  @Test
  public void testCivilStates() {
    Assert.assertEquals(ONBEKEND, toExt(getUnknownPartnership1()));
    Assert.assertEquals(ONBEKEND, toExt(getUnknownPartnership2()));
    Assert.assertEquals(ONBEKEND, toExt(getUnknownPartnership3()));
    Assert.assertEquals(ONGEHUWD, toExt(getNoCommitments()));
    Assert.assertEquals(ONGEHUWD, toExt(getEmptyCommitment()));
    Assert.assertEquals(HUWELIJK, toExt(getActualMarriage()));
    Assert.assertEquals(GESCHEIDEN, toExt(getSeparatedMarriage()));
    Assert.assertEquals(WEDUWE, toExt(getDeceasedMarriage()));
    Assert.assertEquals(PARTNERSCHAP, toExt(getActualPartnership()));
    Assert.assertEquals(ONTBONDEN, toExt(getSeparatedPartner()));
    Assert.assertEquals(ACHTERGEBLEVEN, toExt(getDeceasedPartner()));
  }

  private BurgerlijkeStaatType toExt(BasePLBuilder builder) {
    BasePL pl = builder.finishPL().getPL();
    BasePLExt ext = new BasePLExt(pl);
    return new BurgerlijkeStaat(ext).getStatus().getType();
  }

  /**
   * One current marriage
   */
  private BasePLBuilder getActualMarriage() {
    BasePLBuilder builder = new BasePLBuilder();
    builder.addNewPL(PLEDatasource.PROCURA);

    builder.addCat(GBACat.HUW_GPS, 10, GBARecStatus.CURRENT);
    builder.setElem(GBAElem.DATUM_VERBINTENIS, new BasePLValue("20150101"));
    builder.setElem(GBAElem.SOORT_VERBINTENIS, new BasePLValue("H"));
    builder.setElem(GBAElem.GESLACHTSNAAM, new BasePLValue("Jansen"));
    return builder;
  }

  /**
   * Marriage ended by divorce
   */
  private BasePLBuilder getSeparatedMarriage() {
    BasePLBuilder builder = new BasePLBuilder();
    builder.addNewPL(PLEDatasource.PROCURA);

    builder.addCat(GBACat.HUW_GPS, 10, GBARecStatus.CURRENT);
    builder.setElem(GBAElem.DATUM_VERBINTENIS, new BasePLValue("20150101"));
    builder.setElem(GBAElem.DATUM_ONTBINDING, new BasePLValue("20160101"));
    builder.setElem(GBAElem.REDEN_ONTBINDING, new BasePLValue("S"));
    builder.setElem(GBAElem.SOORT_VERBINTENIS, new BasePLValue("H"));
    builder.setElem(GBAElem.GESLACHTSNAAM, new BasePLValue("Jansen"));
    return builder;
  }

  /**
   * Partnership ended by divorce
   */
  private BasePLBuilder getSeparatedPartner() {
    BasePLBuilder builder = new BasePLBuilder();
    builder.addNewPL(PLEDatasource.PROCURA);

    builder.addCat(GBACat.HUW_GPS, 10, GBARecStatus.CURRENT);
    builder.setElem(GBAElem.DATUM_VERBINTENIS, new BasePLValue("20150101"));
    builder.setElem(GBAElem.DATUM_ONTBINDING, new BasePLValue("20160101"));
    builder.setElem(GBAElem.REDEN_ONTBINDING, new BasePLValue("S"));
    builder.setElem(GBAElem.SOORT_VERBINTENIS, new BasePLValue("P"));
    builder.setElem(GBAElem.GESLACHTSNAAM, new BasePLValue("Jansen"));
    return builder;
  }

  /**
   * Marriage ended by death
   */
  private BasePLBuilder getDeceasedMarriage() {
    BasePLBuilder builder = new BasePLBuilder();
    builder.addNewPL(PLEDatasource.PROCURA);

    builder.addCat(GBACat.HUW_GPS, 10, GBARecStatus.CURRENT);
    builder.setElem(GBAElem.DATUM_VERBINTENIS, new BasePLValue("20150101"));
    builder.setElem(GBAElem.DATUM_ONTBINDING, new BasePLValue("20160101"));
    builder.setElem(GBAElem.REDEN_ONTBINDING, new BasePLValue("O"));
    builder.setElem(GBAElem.SOORT_VERBINTENIS, new BasePLValue("H"));
    builder.setElem(GBAElem.GESLACHTSNAAM, new BasePLValue("Jansen"));
    return builder;
  }

  /**
   * Partnership ended by death
   */
  private BasePLBuilder getDeceasedPartner() {
    BasePLBuilder builder = new BasePLBuilder();
    builder.addNewPL(PLEDatasource.PROCURA);

    builder.addCat(GBACat.HUW_GPS, 10, GBARecStatus.CURRENT);
    builder.setElem(GBAElem.DATUM_VERBINTENIS, new BasePLValue("20150101"));
    builder.setElem(GBAElem.DATUM_ONTBINDING, new BasePLValue("20160101"));
    builder.setElem(GBAElem.REDEN_ONTBINDING, new BasePLValue("O"));
    builder.setElem(GBAElem.SOORT_VERBINTENIS, new BasePLValue("P"));
    builder.setElem(GBAElem.GESLACHTSNAAM, new BasePLValue("Jansen"));
    return builder;
  }

  /**
   * No commitments category
   */
  private BasePLBuilder getNoCommitments() {
    BasePLBuilder builder = new BasePLBuilder();
    builder.addNewPL(PLEDatasource.PROCURA);
    return builder;
  }

  /**
   * Commitments category exist,
   * but has an emptied commitment
   */
  private BasePLBuilder getEmptyCommitment() {
    BasePLBuilder builder = new BasePLBuilder();
    builder.addNewPL(PLEDatasource.PROCURA);
    builder.addCat(GBACat.HUW_GPS, 10, GBARecStatus.CURRENT);
    return builder;
  }

  /**
   * One current marriage
   */
  private BasePLBuilder getActualPartnership() {
    BasePLBuilder builder = new BasePLBuilder();
    builder.addNewPL(PLEDatasource.PROCURA);

    builder.addCat(GBACat.HUW_GPS, 10, GBARecStatus.CURRENT);
    builder.setElem(GBAElem.DATUM_VERBINTENIS, new BasePLValue("20150101"));
    builder.setElem(GBAElem.SOORT_VERBINTENIS, new BasePLValue("P"));
    builder.setElem(GBAElem.GESLACHTSNAAM, new BasePLValue("Jansen"));
    return builder;
  }

  /**
   * One current marriage
   */
  private BasePLBuilder getUnknownPartnership1() {
    BasePLBuilder builder = new BasePLBuilder();
    builder.addNewPL(PLEDatasource.PROCURA);

    builder.addCat(GBACat.HUW_GPS, 10, GBARecStatus.CURRENT);
    builder.setElem(GBAElem.DATUM_VERBINTENIS, new BasePLValue("20150101"));
    builder.setElem(GBAElem.SOORT_VERBINTENIS, new BasePLValue("")).setAllowed(false); // Type is not set
    builder.setElem(GBAElem.GESLACHTSNAAM, new BasePLValue("Jansen"));
    return builder;
  }

  /**
   * One current marriage
   */
  private BasePLBuilder getUnknownPartnership3() {
    BasePLBuilder builder = new BasePLBuilder();
    builder.addNewPL(PLEDatasource.PROCURA);

    builder.addCat(GBACat.HUW_GPS, 10, GBARecStatus.CURRENT);
    builder.setElem(GBAElem.DATUM_VERBINTENIS, new BasePLValue("20150101"));
    builder.setElem(GBAElem.SOORT_VERBINTENIS, new BasePLValue("")); // Type is not set
    builder.setElem(GBAElem.GESLACHTSNAAM, new BasePLValue("Jansen"));
    return builder;
  }

  /**
   * One current marriage
   */
  private BasePLBuilder getUnknownPartnership2() {
    BasePLBuilder builder = new BasePLBuilder();
    builder.addNewPL(PLEDatasource.PROCURA);

    builder.addCat(GBACat.HUW_GPS, 10, GBARecStatus.CURRENT);
    builder.setElem(GBAElem.DATUM_VERBINTENIS, new BasePLValue("20150101"));
    builder.setElem(GBAElem.SOORT_VERBINTENIS, new BasePLValue(".")); // Type is default value
    builder.setElem(GBAElem.GESLACHTSNAAM, new BasePLValue("Jansen"));
    return builder;
  }
}
