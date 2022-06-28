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

package nl.procura.gba.web.modules.zaken.personmutations.page2;

import static nl.procura.gba.web.modules.zaken.personmutations.page2.Page2PersonListMutationsBean.*;

import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Field;
import com.vaadin.ui.NativeSelect;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.base.BasePLSet;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.modules.zaken.personmutations.page2.containers.*;
import nl.procura.gba.web.services.beheer.personmutations.PersonListActionType;
import nl.procura.vaadin.component.layout.table.TableLayout;

public class Page2PersonListMutationsForm extends GbaForm<Page2PersonListMutationsBean> {

  private Page2PersonListMutationsNavLayout catNav;
  private Page2PersonListMutationsNavLayout setNav;
  private Page2PersonListMutationsNavLayout recNav;
  private Page2PersonListMutationsNavLayout actNav;
  private final BasePLExt                      pl;
  private final Page2PersonListMutationsLayout layout;

  public Page2PersonListMutationsForm(BasePLExt pl, Page2PersonListMutationsLayout layout) {
    this.pl = pl;
    this.layout = layout;

    setCaption("Gegevens");
    setColumnWidths("100px", "400px", "100px", "");
    setReadonlyAsText(false);

    setOrder(CAT, RECORD, SET, ACTION);
    setBean(new Page2PersonListMutationsBean());

    ValueChangeListener listener = this;
    getCatField().addListener(listener);
    getSetField().addListener(listener);
    getRecField().addListener(listener);
    getActField().addListener(listener);
  }

  @Override
  public void attach() {

    if (getActField().getContainerDataSource().getItemIds().isEmpty()) {
      CategoryContainer catContainer = new CategoryContainer();
      getCatField().setContainerDataSource(catContainer);
      catNav.update(catContainer.size());
      onCatFieldChange();
      getField(CAT).focus();
    }

    super.attach();
  }

  @Override
  public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {

    if (event.getProperty() == getField(CAT)) {
      onCatFieldChange();

    } else if (event.getProperty() == getField(SET)) {
      onSetFieldChange();

    } else if (event.getProperty() == getField(RECORD)) {
      onRecFieldChange();

    } else if (event.getProperty() == getField(ACTION)) {
      onActFieldChange();
    }
  }

  @Override
  public void afterSetColumn(TableLayout.Column column, Field field, Property property) {
    if (property.is(CAT)) {
      catNav = addNav(column, getCatField());

    } else if (property.is(SET)) {
      setNav = addNav(column, getSetField());

    } else if (property.is(RECORD)) {
      recNav = addNav(column, getRecField());

    } else if (property.is(ACTION)) {
      actNav = addNav(column, getActField());
    }

    super.afterSetColumn(column, field, property);
  }

  public ContainerItem<GBACat> getCatValue() {
    return getContainerValue(getCatField());
  }

  public ContainerItem<BasePLRec> getRecValue() {
    return getContainerValue(getRecField());
  }

  public ContainerItem<PersonListActionType> getActValue() {
    return getContainerValue(getActField());
  }

  public ContainerItem<BasePLSet> getSetValue() {
    return getContainerValue(getSetField());
  }

  private Page2PersonListMutationsNavLayout addNav(TableLayout.Column column, NativeSelect field) {
    Page2PersonListMutationsNavLayout nav = new Page2PersonListMutationsNavLayout(field, current -> {
      IndexedContainer container = (IndexedContainer) field.getContainerDataSource();
      if (container instanceof MutationFieldContainer) {
        field.setValue(((MutationFieldContainer) container).getByIndex(current));
      }
    });
    column.addComponent(nav);
    return nav;
  }

  private void onCatFieldChange() {
    ContainerItem<GBACat> cat = getCatValue();

    getSetField().setContainerDataSource(null);
    if (cat != null) {
      SetContainer container = new SetContainer(cat.getItem(), pl.getCat(cat.getItem()));
      getSetField().setContainerDataSource(container);
      getBean().setSet(getSetValue());
      setNav.update(container.size());
      catNav.setCurrent(cat);
    }
  }

  private void onSetFieldChange() {
    ContainerItem<GBACat> cat = getCatValue();
    ContainerItem<BasePLSet> set = getSetValue();

    getRecField().setContainerDataSource(null);
    if (cat != null && set != null) {
      RecordContainer container = new RecordContainer(set);
      getRecField().setContainerDataSource(container);
      getBean().setRecord(getRecValue());
      recNav.update(container.size());
      setNav.setCurrent(set);
    }
  }

  private void onRecFieldChange() {
    ContainerItem<GBACat> cat = getCatValue();
    ContainerItem<BasePLSet> set = getSetValue();
    ContainerItem<BasePLRec> rec = getRecValue();

    if (cat != null && rec != null) {
      ActionContainer container = new ActionContainer(getApplication(), pl, cat, set, rec);
      getActField().setContainerDataSource(container);
      actNav.update(container.size());
      recNav.setCurrent(rec);
    }
  }

  private void onActFieldChange() {
    actNav.setCurrent(getActValue());
    ContainerItem<PersonListActionType> act = getActValue();
    ContainerItem<BasePLRec> rec = getRecValue();
    if (rec != null && act != null) {
      layout.update(act.getItem(), pl, rec.getItem());
    }
  }

  private <T> ContainerItem<T> getContainerValue(NativeSelect setField) {
    return (ContainerItem<T>) setField.getValue();
  }

  private NativeSelect getActField() {
    return (NativeSelect) getField(ACTION);
  }

  private NativeSelect getCatField() {
    return (NativeSelect) getField(CAT);
  }

  private NativeSelect getSetField() {
    return (NativeSelect) getField(SET);
  }

  private NativeSelect getRecField() {
    return (NativeSelect) getField(RECORD);
  }
}
