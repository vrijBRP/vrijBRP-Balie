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

package nl.procura.gba.web.modules.beheer.profielen.page12.tab2.page2;

import static nl.procura.gba.common.ZaakType.*;
import static nl.procura.gba.web.modules.beheer.profielen.page12.tab2.page2.Page2ZaakConfiguratiesBean.*;
import static nl.procura.gba.web.modules.hoofdmenu.zakenregister.page4.zoeken.Page4ZakenBean.ZAAKTYPES;
import static org.apache.commons.lang3.BooleanUtils.isTrue;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import nl.procura.gba.common.ZaakType;
import nl.procura.gba.web.components.fields.SimpleMultiField;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.zaken.algemeen.zaakconfiguraties.ZaakConfiguratie;
import nl.procura.vaadin.component.container.ArrayListContainer;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class Page2ZaakConfiguratiesForm1 extends GbaForm<Page2ZaakConfiguratiesBean> {

  public Page2ZaakConfiguratiesForm1() {
    setOrder(TITEL, BRON, LEVERANCIER, ZAAKTYPES, IND_ZAAKSYSTEEM_ID);
    setColumnWidths("160px", "");
  }

  public void setConfiguratie(ZaakConfiguratie configuratie) {
    Page2ZaakConfiguratiesBean bean = new Page2ZaakConfiguratiesBean();
    bean.setTitel(configuratie.getZaakConf());
    bean.setBron(configuratie.getBron());
    bean.setLeverancier(configuratie.getLeverancier());
    bean.setIndZaaksysteemId(isTrue(configuratie.getIndZaaksysteemId()));
    setBean(bean);
    addZaakTypesContainer(configuratie.getZaakTypesAsList());
  }

  private List<FieldValue> getSelectedZaakTypes(List<ZaakType> zaakTypes) {
    return zaakTypes.stream()
        .filter(zt -> !zt.is(ONBEKEND))
        .map(zt -> new FieldValue(zt, zt.getOms()))
        .collect(Collectors.toList());
  }

  private void addZaakTypesContainer(List<ZaakType> zaakTypes) {
    getField(ZAAKTYPES, SimpleMultiField.class).setContainer(new ArrayListContainer(getSelectedZaakTypes()));
    getField(ZAAKTYPES, SimpleMultiField.class).setValues(getSelectedZaakTypes(zaakTypes));
  }

  private List<FieldValue> getSelectedZaakTypes() {
    return Stream
        .of(VERHUIZING, GEGEVENSVERSTREKKING, ONDERZOEK, OVERLIJDEN_IN_GEMEENTE, LIJKVINDING,
            LEVENLOOS, UITTREKSEL, VERSTREKKINGSBEPERKING, NAAMGEBRUIK, INHOUD_VERMIS, COVOG)
        .map(type -> new FieldValue(type, type.getOms()))
        .collect(Collectors.toList());
  }
}
