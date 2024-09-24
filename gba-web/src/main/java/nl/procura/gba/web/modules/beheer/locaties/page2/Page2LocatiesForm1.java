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

package nl.procura.gba.web.modules.beheer.locaties.page2;

import static nl.procura.gba.web.modules.beheer.locaties.page2.Page2LocatiesBean.*;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.beheer.locatie.Locatie;
import nl.procura.gba.web.services.beheer.locatie.LocatieType;

public class Page2LocatiesForm1 extends GbaForm<Page2LocatiesBean> {

  public Page2LocatiesForm1(Locatie locatie) {

    setCaption("Locatie");
    setOrder(TYPE, LOCATIE, OMSCHRIJVING, ZYNYO_DEVICE_ID);
    setColumnWidths("200px", "");

    Page2LocatiesBean bean = new Page2LocatiesBean();

    if (locatie.isStored()) {
      bean.setType(locatie.getLocatieType());
      bean.setLocatie(locatie.getLocatie());
      bean.setOmschrijving(locatie.getOmschrijving());
      bean.setZynyoDeviceId(locatie.getZynyoDeviceId());
    }

    setBean(bean);
  }

  @Override
  public void setBean(Object bean) {

    super.setBean(bean);

    if (getField(TYPE) != null) {

      getField(TYPE)
          .addListener((ValueChangeListener) event -> onTypeChange((LocatieType) event.getProperty().getValue()));

      onTypeChange(((Page2LocatiesBean) bean).getType());
    }

  }

  @SuppressWarnings("unused")
  protected void onTypeChange(LocatieType type) {
  }
}
