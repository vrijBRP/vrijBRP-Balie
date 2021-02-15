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

package nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.tab1.result;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.extensions.PLResultComposite;
import nl.procura.diensten.gba.ple.procura.arguments.PLEArgs;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.ZoekResultPage;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;

public class Tab1ResultPage extends ZoekResultPage {

  public Tab1ResultPage(PLEArgs arguments, boolean rekeninghoudenAdres, PLResultComposite result) {
    super("Zoekresultaat gemeentelijk", arguments, result);
    setRekeninghoudenAdres(rekeninghoudenAdres);
  }

  private Tab1ResultPage(Adres adres) {
    super("Zoekresultaat gemeentelijk", adres);
  }

  @Override
  protected void selectAdres(Adres adres) {
    getNavigation().goToPage(new Tab1ResultPage(adres));
    super.selectAdres(adres);
  }

  @Override
  protected void selectPersoonslijst(Record record) {
    BasePLExt pl = (BasePLExt) record.getObject();
    getApplication().goToPl(getWindow(), "", pl.getDatasource(), pl);
  }
}
