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

package nl.procura.gba.web.modules.zaken.rijbewijs.page3;

import static nl.procura.gba.web.modules.zaken.rijbewijs.page3.Page3RijbewijsBean1.*;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.web.components.fields.values.GbaDateFieldValue;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.vaadin.component.field.fieldvalues.AnrFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;

public class Page3RijbewijsForm1 extends GbaForm<Page3RijbewijsBean1> {

  public Page3RijbewijsForm1(BasePLExt pl) {

    setCaption("Starten aanvraag");

    setOrder(ANR, BSN, GEBOORTEDATUM, VOORNAMEN, GESLACHTSNAAM);

    setColumnWidths(WIDTH_130, "");

    Page3RijbewijsBean1 b = new Page3RijbewijsBean1();
    b.setAnr(new AnrFieldValue(pl.getPersoon().getAnr().getVal()));
    b.setBsn(new BsnFieldValue(pl.getPersoon().getBsn().getVal()));
    b.setGeboortedatum(new GbaDateFieldValue(pl.getPersoon().getGeboorte().getGeboortedatum()));
    b.setGeslachtsnaam(pl.getPersoon().getNaam().getGeslachtsnaam().getValue().getVal());
    b.setVoornamen(pl.getPersoon().getNaam().getVoornamen().getValue().getVal());

    setBean(b);
  }
}
