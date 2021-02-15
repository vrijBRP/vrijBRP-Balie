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

package nl.procura.gba.web.modules.persoonslijst.verblijfplaats.page1;

import nl.procura.vaadin.component.container.ArrayListContainer;

public class RecordHistorieContainer extends ArrayListContainer {

  public static final String ALLE_RECORDS   = "Toon alle records";
  public static final String JUISTE_RECORDS = "Verberg onjuiste / administratieve historie";

  public RecordHistorieContainer() {
    addItem(JUISTE_RECORDS);
    addItem(ALLE_RECORDS);
  }
}
