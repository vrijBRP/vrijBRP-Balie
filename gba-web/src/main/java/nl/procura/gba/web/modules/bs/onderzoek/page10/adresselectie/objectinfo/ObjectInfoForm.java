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

package nl.procura.gba.web.modules.bs.onderzoek.page10.adresselectie.objectinfo;

import static nl.procura.gba.web.modules.bs.onderzoek.page10.adresselectie.objectinfo.ObjectInfoBean.ADRESINDICATIE;
import static nl.procura.gba.web.modules.bs.onderzoek.page10.adresselectie.objectinfo.ObjectInfoBean.AON;
import static nl.procura.gba.web.modules.bs.onderzoek.page10.adresselectie.objectinfo.ObjectInfoBean.BUURTCODE;
import static nl.procura.gba.web.modules.bs.onderzoek.page10.adresselectie.objectinfo.ObjectInfoBean.DATUMINGANG;
import static nl.procura.gba.web.modules.bs.onderzoek.page10.adresselectie.objectinfo.ObjectInfoBean.INA;
import static nl.procura.gba.web.modules.bs.onderzoek.page10.adresselectie.objectinfo.ObjectInfoBean.PANDCODE;
import static nl.procura.gba.web.modules.bs.onderzoek.page10.adresselectie.objectinfo.ObjectInfoBean.PPDCODE;
import static nl.procura.gba.web.modules.bs.onderzoek.page10.adresselectie.objectinfo.ObjectInfoBean.STEMDISTRICT;
import static nl.procura.gba.web.modules.bs.onderzoek.page10.adresselectie.objectinfo.ObjectInfoBean.SUBBUURTCODE;
import static nl.procura.gba.web.modules.bs.onderzoek.page10.adresselectie.objectinfo.ObjectInfoBean.WONINGINDICATIE;
import static nl.procura.gba.web.modules.bs.onderzoek.page10.adresselectie.objectinfo.ObjectInfoBean.WOONPLAATS;
import static nl.procura.gba.web.modules.zaken.woningkaart.window.WoningObjectBean.ADRES;
import static nl.procura.gba.web.modules.zaken.woningkaart.window.WoningObjectBean.DATUMEINDE;
import static nl.procura.gba.web.modules.zaken.woningkaart.window.WoningObjectBean.OPENBARE_RUIMTE;
import static nl.procura.gba.web.modules.zaken.woningkaart.window.WoningObjectBean.OPMERKING;
import static nl.procura.gba.web.modules.zaken.woningkaart.window.WoningObjectBean.WIJK;
import static nl.procura.gba.web.modules.zaken.woningkaart.window.WoningObjectBean.WONINGSOORT;

import com.vaadin.ui.Field;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.beheer.bag.ProcuraInhabitantsAddress;
import nl.procura.vaadin.component.layout.table.TableLayout;

public class ObjectInfoForm extends GbaForm<ObjectInfoBean> {

  public ObjectInfoForm(ProcuraInhabitantsAddress address) {
    setOrder(ADRES, OPMERKING, OPENBARE_RUIMTE, AON, WOONPLAATS, INA, WONINGSOORT, WONINGINDICATIE, ADRESINDICATIE,
        PPDCODE, STEMDISTRICT, PANDCODE, WIJK, BUURTCODE, SUBBUURTCODE, DATUMINGANG, DATUMEINDE);

    setColumnWidths("160px", "", "150px", "250px");
    setReadonlyAsText(true);
    setBean(new ObjectInfoBean(address));
  }

  @Override
  public Field newField(Field field, Property property) {
    super.newField(field, property);
    if (property.is(WONINGSOORT, OPENBARE_RUIMTE, WIJK)) {
      getLayout().addBreak();
    }

    return field;
  }

  @Override
  public void setColumn(TableLayout.Column column, Field field, Property property) {
    if (property.is(ADRES, OPMERKING, DATUMEINDE)) {
      column.setColspan(3);
    }

    super.setColumn(column, field, property);
  }
}
