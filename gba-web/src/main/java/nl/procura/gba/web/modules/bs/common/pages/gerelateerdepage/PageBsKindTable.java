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

package nl.procura.gba.web.modules.bs.common.pages.gerelateerdepage;

import java.util.List;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.extensions.Cat1PersoonExt;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.bs.common.utils.BsKindUtils;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.theme.GbaWebTheme;

/**
 * De kinderen van de moeder
 */
public class PageBsKindTable extends GbaTable {

  private final DossierPersoon moeder;

  public PageBsKindTable(DossierPersoon moeder) {
    this.moeder = moeder;
  }

  @Override
  public void setColumns() {

    addStyleName(GbaWebTheme.TABLE.NEWLINE_WRAP);

    addColumn("Naam");
    addColumn("Vader / Duo-moeder");
    addColumn("Geboren", 100);
    addColumn("Geslacht", 60);

    super.setColumns();
  }

  @Override
  public void setRecords() {

    for (BasePLExt pl : getKinderenMetPersoonslijst(moeder)) {
      Cat1PersoonExt persoon = pl.getPersoon();
      Record record = addRecord(pl);
      record.addValue(persoon.getNaam().getPredEerstevoornAdelVoorvGesl());
      record.addValue(getVaderOfDuoMoeder(pl));
      record.addValue(persoon.getGeboorte().getDatumLeeftijd());
      record.addValue(persoon.getGeslacht().getDescr());
    }

    for (DossierPersoon persoon : getKinderenZonderPersoonslijst(moeder)) {
      Record record = addRecord(persoon);
      record.addValue(persoon.getNaam().getPred_adel_voorv_gesl_voorn() + " (geen persoonslijst gevonden)");
      record.addValue("");
      record.addValue(persoon.getGeboorte().getDatum_leeftijd());
      record.addValue(persoon.getGeslacht().getVolledig());
    }

    super.setRecords();
  }

  private List<BasePLExt> getKinderenMetPersoonslijst(DossierPersoon moeder) {
    return BsKindUtils.getKinderenMetPersoonslijst(getApplication().getServices(), moeder);
  }

  private List<DossierPersoon> getKinderenZonderPersoonslijst(DossierPersoon moeder) {
    return BsKindUtils.getKinderenZonderPersoonslijst(getApplication().getServices(), moeder);
  }

  private String getVaderOfDuoMoeder(BasePLExt pl) {
    return pl.getOuders().getOuders()
        .stream()
        .filter(ouder -> moeder.getBsn().longValue() != ouder.getBsn().toLong())
        .findFirst()
        .map(ouder -> ouder.getNaam().getPredEerstevoornAdelVoorvGesl())
        .orElse("Geen vader / duo-moeder");
  }
}
