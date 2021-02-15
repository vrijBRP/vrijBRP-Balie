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

package nl.procura.gba.web.modules.zaken.verhuizing.page11;

import static nl.procura.gba.web.modules.zaken.verhuizing.page11.Page11VerhuizingBean1.*;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.modules.zaken.verhuizing.VerhuisAdres;
import nl.procura.gba.web.services.zaken.verhuizing.VerhuisType;

public class Page11VerhuizingForm1 extends GbaForm<Page11VerhuizingBean1> {

  public Page11VerhuizingForm1(VerhuisType verhuisType, VerhuisAdres verhuisAdres) {

    setReadonlyAsText(true);
    setCaption("Zoek adres en bewoners");

    if (verhuisType.isHervestiging()) {
      setOrder(TYPEVERHUIZING, BSN, GEBOORTEDATUM, GESLACHTSNAAM);
    } else {
      setOrder(TYPEVERHUIZING, ADRES, ADRESIND, BSN, GEBOORTEDATUM, GESLACHTSNAAM);
    }

    setColumnWidths(WIDTH_130, "");

    Page11VerhuizingBean1 b = new Page11VerhuizingBean1();
    b.setType(verhuisType.getOms());
    b.setAdres(verhuisAdres.getAddressLabel());

    setBean(b);

    getField(BSN).focus();
  }
}
