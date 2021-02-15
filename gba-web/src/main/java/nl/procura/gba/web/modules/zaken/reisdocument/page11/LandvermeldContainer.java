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

package nl.procura.gba.web.modules.zaken.reisdocument.page11;

import com.vaadin.data.util.IndexedContainer;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.vaadin.component.container.ProcuraContainer;

public class LandvermeldContainer extends IndexedContainer implements ProcuraContainer {

  public LandvermeldContainer(BasePLExt pl) {
    addContainerProperty(OMSCHRIJVING, String.class, "");

    String gebPlaats = pl.getLatestRec(GBACat.PERSOON).getElemVal(GBAElem.GEBOORTEPLAATS).getDescr();
    String gebLand = pl.getLatestRec(GBACat.PERSOON).getElemVal(GBAElem.GEBOORTELAND).getDescr();
    String value = gebPlaats + " (" + gebLand + ")";

    if (value.length() > 80) {
      addItem(false).getItemProperty(OMSCHRIJVING).setValue("Nee, want " + value + " is langer dan 80 tekens");
    } else {
      addItem(true).getItemProperty(OMSCHRIJVING).setValue("Ja, " + value);
      addItem(false).getItemProperty(OMSCHRIJVING).setValue("Nee");
    }
  }
}
