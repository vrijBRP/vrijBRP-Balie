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

package nl.procura.gba.web.modules.zaken.rijbewijs.page15;

import static nl.procura.gba.web.modules.zaken.rijbewijs.page15.Page15RijbewijsBean1.*;

import java.math.BigInteger;

import nl.procura.gba.web.components.layouts.form.GbaForm;

public class Page15RijbewijsForm1 extends GbaForm<Page15RijbewijsBean1> {

  Page15RijbewijsForm1(BigInteger rijbewijsNummer) {
    setCaption("Gegevens ongeldigverklaring");
    setReadThrough(true);
    setColumnWidths("160px", "");
    setOrder(RIJBEWIJS_NR, DATUM_INLEV, REDEN_ONGELDIG);
    initForm();

    Page15RijbewijsBean1 bean = new Page15RijbewijsBean1();
    bean.setRijbewijsNr(rijbewijsNummer);
    setBean(bean);
  }

  public void initForm() {
  }

  void fill(OngeldigVerklaring ongeldigVerklaring) {
    ongeldigVerklaring.setDatumInlevering(getBean().getDatumInlevering());
    ongeldigVerklaring.setRedenOngeldig(getBean().getRedenOngeldig());
  }
}
