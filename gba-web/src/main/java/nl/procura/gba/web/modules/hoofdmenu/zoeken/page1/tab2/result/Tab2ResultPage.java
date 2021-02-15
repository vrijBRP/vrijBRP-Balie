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

package nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.tab2.result;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.extensions.PLResultComposite;
import nl.procura.diensten.gba.ple.procura.arguments.PLEArgs;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.ZoekResultPage;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;

public class Tab2ResultPage extends ZoekResultPage {

  public Tab2ResultPage(PLEArgs arguments, PLResultComposite result) {
    super("Zoekresultaat landelijk", arguments, result);
  }

  @Override
  protected void selectPersoonslijst(Record record) {

    BasePLExt pl = (BasePLExt) record.getObject();
    getApplication().goToPl(getWindow(), "", pl.getDatasource(), pl);
  }
}
