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

package nl.procura.gba.web.modules.bs.onderzoek.page10.adresselectie.zoeken;

import static nl.procura.gba.web.modules.bs.onderzoek.page10.adresselectie.zoeken.AdresBean.HNR;
import static nl.procura.gba.web.modules.bs.onderzoek.page10.adresselectie.zoeken.AdresBean.HNRL;
import static nl.procura.gba.web.modules.bs.onderzoek.page10.adresselectie.zoeken.AdresBean.HNRT;
import static nl.procura.gba.web.modules.bs.onderzoek.page10.adresselectie.zoeken.AdresBean.PC;
import static nl.procura.gba.web.modules.bs.onderzoek.page10.adresselectie.zoeken.AdresBean.STRAAT;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.interfaces.address.Address;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class AdresForm2 extends GbaForm<AdresBean> {

  public AdresForm2(Address adres) {
    setCaption("Selecteer adres");
    setColumnWidths(WIDTH_130, "");
    setOrder(STRAAT, HNR, HNRL, HNRT, PC);
    update(adres);
  }

  public void update(Address adres) {
    AdresBean bean = new AdresBean();
    if (adres != null) {
      bean.setStraat(new FieldValue(adres.getStreet()));
      bean.setHnr(adres.getHnr());
      bean.setHnrL(adres.getHnrL());
      bean.setHnrT(adres.getHnrT());
      bean.setPc(new FieldValue(adres.getPostalCode()));
    }
    setBean(bean);
  }
}
