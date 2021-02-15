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

import static nl.procura.gba.web.modules.zaken.verhuizing.page10.Page10VerhuizingBean1.*;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.layouts.form.GbaForm;

public class Page10VerhuizingForm1 extends GbaForm<Page10VerhuizingBean1> {

  public Page10VerhuizingForm1() {

    setReadonlyAsText(true);
    setCaption("Nieuwe verhuizing");
    setOrder(TYPEVERHUIZING, HUIDIGADRES, AANGEVER, TOELICHTING);
    setColumnWidths(WIDTH_130, "");

    setBean(new Page10VerhuizingBean1());
  }

  public GbaNativeSelect getAangeverVeld() {
    return (GbaNativeSelect) getField(AANGEVER);
  }

  public GbaNativeSelect getHuidigAdresVeld() {
    return (GbaNativeSelect) getField(HUIDIGADRES);
  }

  public GbaNativeSelect getSoortVeld() {
    return (GbaNativeSelect) getField(TYPEVERHUIZING);
  }
}
