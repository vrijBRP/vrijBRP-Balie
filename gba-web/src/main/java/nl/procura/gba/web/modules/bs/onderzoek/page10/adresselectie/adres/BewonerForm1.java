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

package nl.procura.gba.web.modules.bs.onderzoek.page10.adresselectie.adres;

import static nl.procura.gba.web.modules.bs.onderzoek.page10.adresselectie.adres.BewonerBean.ADRES;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.modules.bs.onderzoek.page10.adresselectie.zoeken.SelectieAdres;

public class BewonerForm1 extends GbaForm<BewonerBean> {

  public BewonerForm1(SelectieAdres adres) {
    setReadonlyAsText(true);
    setCaption("Adres");
    setColumnWidths(WIDTH_130, "");
    setOrder(ADRES);
    update(adres);
  }

  public void update(SelectieAdres adres) {
    BewonerBean bean = new BewonerBean();
    if (adres == null) {
      bean.setAdres("Selecteer een adres");
    } else {
      String adresText = adres.getAdres();
      if (adres.getBewoners().isEmpty()) {
        adresText += " (geen bewoners)";
      } else {
        adresText += " (" + adres.getBewoners().size() + " bewoners)";
      }
      bean.setAdres(adresText);
    }
    setBean(bean);
  }
}
