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

package nl.procura.gba.web.modules.zaken.gpk.page2;

import com.vaadin.data.util.IndexedContainer;

import nl.procura.gba.web.services.zaken.gpk.GpkKaartType;

public class GpkKaartTypeContainer extends IndexedContainer {

  public GpkKaartTypeContainer() {
    addItem(GpkKaartType.BESTUURDERSKAART);
    addItem(GpkKaartType.PASSAGIERSKAART);
    addItem(GpkKaartType.INSTELLINGENKAART);
    addItem(GpkKaartType.BESTUUR_OF_PASSAG_KAART);
  }
}
