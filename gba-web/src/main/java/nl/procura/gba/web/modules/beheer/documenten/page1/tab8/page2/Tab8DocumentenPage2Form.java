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

package nl.procura.gba.web.modules.beheer.documenten.page1.tab8.page2;

import static nl.procura.gba.common.ZaakType.LEGE_PERSOONLIJST;
import static nl.procura.gba.common.ZaakType.ONBEKEND;
import static nl.procura.gba.web.modules.beheer.documenten.page1.tab8.page2.Tab8DocumentenPage2Bean.OMSCHRIJVING;
import static nl.procura.gba.web.modules.beheer.documenten.page1.tab8.page2.Tab8DocumentenPage2Bean.ZAAK_TYPES;
import static nl.procura.gba.web.modules.hoofdmenu.zakenregister.page4.zoeken.Page4ZakenBean.ZAAKTYPES;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import nl.procura.gba.common.ZaakType;
import nl.procura.gba.web.components.fields.SimpleMultiField;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.zaken.documenten.dmstypes.DmsDocumentType;
import nl.procura.vaadin.component.container.ArrayListContainer;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class Tab8DocumentenPage2Form extends GbaForm<Tab8DocumentenPage2Bean> {

  public Tab8DocumentenPage2Form(DmsDocumentType documentType) {
    setOrder(OMSCHRIJVING, ZAAK_TYPES);
    setColumnWidths(WIDTH_130, "");
    initFields(documentType);
  }

  private void initFields(DmsDocumentType type) {
    Tab8DocumentenPage2Bean bean = new Tab8DocumentenPage2Bean();
    bean.setOmschrijving(type.getDocumentDmsType());
    setBean(bean);
    addZaakTypesContainer(type.getZaakTypesAsList());
  }

  @Override
  public void afterSetBean() {
    super.afterSetBean();
    addZaakTypesContainer(getBean().getZaakTypesAsList());
  }

  private void addZaakTypesContainer(List<ZaakType> zaakTypes) {
    getField(ZAAKTYPES, SimpleMultiField.class).setContainer(new ArrayListContainer(getSelectedZaakTypes()));
    getField(ZAAKTYPES, SimpleMultiField.class).setValues(getSelectedZaakTypes(zaakTypes));
  }

  private List<FieldValue> getSelectedZaakTypes(List<ZaakType> zaakTypes) {
    return zaakTypes.stream()
        .filter(zt -> !zt.is(ONBEKEND))
        .map(zt -> new FieldValue(zt, zt.getOms()))
        .collect(Collectors.toList());
  }

  private List<FieldValue> getSelectedZaakTypes() {
    return Stream.of(ZaakType.values())
        .filter(zt -> !zt.is(ONBEKEND, LEGE_PERSOONLIJST))
        .map(type -> new FieldValue(type, type.getOms()))
        .collect(Collectors.toList());
  }
}
