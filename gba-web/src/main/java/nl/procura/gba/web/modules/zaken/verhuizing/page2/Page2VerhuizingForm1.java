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

package nl.procura.gba.web.modules.zaken.verhuizing.page2;

import static nl.procura.gba.web.modules.zaken.verhuizing.page2.Page2VerhuizingBean1.*;
import static nl.procura.standard.Globalfunctions.astr;

import nl.procura.gba.web.components.layouts.form.ReadOnlyForm;
import nl.procura.gba.web.modules.bs.common.layouts.relocation.NumberOfPersonsContainer;
import nl.procura.gba.web.services.zaken.verhuizing.FunctieAdres;
import nl.procura.gba.web.services.zaken.verhuizing.VerhuisAanvraag;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class Page2VerhuizingForm1 extends ReadOnlyForm {

  public Page2VerhuizingForm1(VerhuisAanvraag aanvraag) {

    setCaption("Nieuw adres");
    setOrder(ADRES, FUNCTIE_ADRES, PCPLAATS, AANTAL);
    setColumnWidths(WIDTH_130, "", "200px", "200px");

    Page2VerhuizingBean1 b = new Page2VerhuizingBean1();

    b.setAdres(aanvraag.getNieuwAdres().getAdres().getAdres());
    b.setPcPlaats(aanvraag.getNieuwAdres().getAdres().getPc_wpl_gem());

    NumberOfPersonsContainer container = new NumberOfPersonsContainer(false);
    b.setAantal(astr(container.getItemById(new FieldValue(aanvraag.getNieuwAdres().getAantalPersonen()))));

    FunctieAdres functieAdres = aanvraag.getNieuwAdres().getFunctieAdres();

    if (functieAdres == FunctieAdres.BRIEFADRES) {
      b.setFunctieadres("<b>" + functieAdres.toString() + "</b>");
    } else {
      b.setFunctieadres(functieAdres.toString());
    }

    setBean(b);
  }
}
