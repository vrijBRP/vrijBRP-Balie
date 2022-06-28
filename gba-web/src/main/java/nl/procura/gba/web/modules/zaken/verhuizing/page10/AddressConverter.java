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

package nl.procura.gba.web.modules.zaken.verhuizing.page10;

import static nl.procura.standard.Globalfunctions.along;

import nl.procura.gba.web.modules.zaken.verhuizing.VerhuisAdres;
import nl.procura.gba.web.services.zaken.verhuizing.FunctieAdres;
import nl.procura.gba.web.services.zaken.verhuizing.VerhuisAanvraagAdres;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class AddressConverter {

  /**
   * Convert a relocationAddress to a relocation
   */
  public static void toVerhuisAanvraagAdres(VerhuisAanvraagAdres a, VerhuisAdres v) {
    a.setFunctieAdres(FunctieAdres.WOONADRES);
    a.setHnr(along(v.getAddress().getHnr()));
    a.setHnrL(v.getAddress().getHnrL());
    a.setHnrT(v.getAddress().getHnrT());
    a.setPc(new FieldValue(v.getAddress().getPostalCode()));
    a.setStraat(new FieldValue(v.getAddress().getStreet()));
    a.setWoonplaats(new FieldValue(v.getAddress().getResidenceName()));
    a.setGemeente(new FieldValue(v.getAddress().getMunicipalityCode(), v.getAddress().getMunicipalityName()));
  }
}
