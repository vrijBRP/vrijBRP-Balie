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

import java.util.Collections;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.burgerzaken.gba.core.enums.GBARecStatus;
import nl.procura.diensten.gba.ple.base.BasePL;
import nl.procura.diensten.gba.ple.base.BasePLBuilder;
import nl.procura.diensten.gba.ple.base.BasePLSet;
import nl.procura.diensten.gba.ple.base.BasePLValue;
import nl.procura.diensten.gba.ple.base.utils.Cat5MarriageSorter;
import nl.procura.diensten.gba.ple.procura.arguments.PLEDatasource;

public class PLCommitmentSortingTest {

  @Test
  public void sortMarriages() {
    // 100 iterations with shuffled sets should always return correct order
    for (int i = 0; i < 100; i++) {
      BasePL marriages = getMultipleMarriages();
      List<BasePLSet> sets = marriages.getCat(GBACat.HUW_GPS).getSets();
      Collections.shuffle(sets);
      Collections.sort(sets, new Cat5MarriageSorter());
      int externalIndex = sets.size();
      int internalIndex = 0;
      while (externalIndex > 0) {
        internalIndex = internalIndex + 10;
        Assert.assertEquals(internalIndex, getIntIndex(marriages, --externalIndex));
      }

      /**
       * Lowest externalIndex should have highest internalIndex.
       *
       * Assert.assertEquals(10, getIntIndex(marriages, 6));
       * Assert.assertEquals(20, getIntIndex(marriages, 5));
       * Assert.assertEquals(30, getIntIndex(marriages, 4));
       * ...
       */
    }
  }

  /**
   * One current marriage and one disolved marriage
   */
  private BasePL getMultipleMarriages() {

    BasePLBuilder builder = new BasePLBuilder();
    builder.addNewPL(PLEDatasource.PROCURA);

    // Married
    builder.addCat(GBACat.HUW_GPS, 70, GBARecStatus.CURRENT);
    builder.setElem(GBAElem.DATUM_VERBINTENIS, new BasePLValue("20150101"));
    builder.setElem(GBAElem.GESLACHTSNAAM, new BasePLValue("Jansen"));

    builder.addCat(GBACat.HUW_GPS, 70, GBARecStatus.HIST);
    builder.setElem(GBAElem.DATUM_VERBINTENIS, new BasePLValue("20140101"));
    builder.setElem(GBAElem.GESLACHTSNAAM, new BasePLValue("Jansen"));

    // Commitment ended
    builder.addCat(GBACat.HUW_GPS, 60, GBARecStatus.CURRENT);
    builder.setElem(GBAElem.DATUM_VERBINTENIS, new BasePLValue("20110101"));
    builder.setElem(GBAElem.DATUM_ONTBINDING, new BasePLValue("20120101"));
    builder.setElem(GBAElem.GESLACHTSNAAM, new BasePLValue("Jansen"));

    // Commitment ended
    builder.addCat(GBACat.HUW_GPS, 50, GBARecStatus.CURRENT);
    builder.setElem(GBAElem.DATUM_VERBINTENIS, new BasePLValue("20090101"));
    builder.setElem(GBAElem.DATUM_ONTBINDING, new BasePLValue("20100101"));
    builder.setElem(GBAElem.INGANGSDAT_GELDIG, new BasePLValue("20100102"));
    builder.setElem(GBAElem.GESLACHTSNAAM, new BasePLValue("Jansen"));

    // Commitment ended
    builder.addCat(GBACat.HUW_GPS, 40, GBARecStatus.CURRENT);
    builder.setElem(GBAElem.DATUM_VERBINTENIS, new BasePLValue("20090101"));
    builder.setElem(GBAElem.DATUM_ONTBINDING, new BasePLValue("20100101"));
    builder.setElem(GBAElem.INGANGSDAT_GELDIG, new BasePLValue("20100101"));
    builder.setElem(GBAElem.GESLACHTSNAAM, new BasePLValue("Jansen"));
    builder.setElem(GBAElem.DATUM_VAN_OPNEMING, new BasePLValue("20150101"));

    // Commitment ended
    builder.addCat(GBACat.HUW_GPS, 30, GBARecStatus.CURRENT);
    builder.setElem(GBAElem.DATUM_VERBINTENIS, new BasePLValue("20090101"));
    builder.setElem(GBAElem.DATUM_ONTBINDING, new BasePLValue("20100101"));
    builder.setElem(GBAElem.INGANGSDAT_GELDIG, new BasePLValue("20100101"));
    builder.setElem(GBAElem.DATUM_VAN_OPNEMING, new BasePLValue("20140101"));
    builder.setElem(GBAElem.GESLACHTSNAAM, new BasePLValue("Jansen"));

    builder.addCat(GBACat.HUW_GPS, 20, GBARecStatus.CURRENT);
    builder.setElem(GBAElem.DATUM_VERBINTENIS, new BasePLValue(""));
    builder.setElem(GBAElem.GESLACHTSNAAM, new BasePLValue("Jansen"));

    // Empty category
    builder.addCat(GBACat.HUW_GPS, 10, GBARecStatus.CURRENT);
    return builder.finishPL().getPL();
  }

  private int getIntIndex(BasePL pl, int nr) {
    return pl.getCats(GBACat.HUW_GPS).get(0).getSets().get(nr).getIntIndex();
  }
}
