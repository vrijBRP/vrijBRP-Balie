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

package nl.procura.gba.web.modules.zaken.personmutationsindex.page1;

import static nl.procura.gba.web.modules.zaken.personmutationsindex.page1.Page1MutationsIndexBean.*;

import java.util.Arrays;
import java.util.function.Consumer;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Field;

import nl.procura.bsm.rest.v1_0.objecten.gba.probev.mutations.MutationType;
import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.page4.telling.Vandaag;
import nl.procura.vaadin.component.container.ProcuraContainer;
import nl.procura.vaadin.component.field.fieldvalues.AnrFieldValue;
import nl.procura.vaadin.component.layout.table.TableLayout;

public class Page1MutationsIndexForm extends GbaForm<Page1MutationsIndexBean> {

  private final BasePLExt                   pl;
  private Consumer<Page1MutationsIndexBean> beanChangeListener;

  public Page1MutationsIndexForm(BasePLExt pl, Page1MutationsIndexBean bean) {
    this.pl = pl;
    setCaption("Zoeken");
    setColumnWidths("100px", "400px", "100px", "");
    setReadonlyAsText(false);
    setOrder(PERSOON, ANR, RELATIES, DATUM_MUTATIE, CAT, STATUS);
    setBean(bean);
  }

  @Override
  public void afterSetBean() {
    ValueChangeListener beanChangeListener = event -> {
      commit();
      this.beanChangeListener.accept(getBean());
    };
    PersoonContainer persoonContainer = new PersoonContainer();
    StatusContainer statusContainer = new StatusContainer();
    getField(PERSOON, GbaNativeSelect.class).setContainerDataSource(persoonContainer);
    getField(PERSOON, GbaNativeSelect.class).setValue(persoonContainer.getItemIds().stream().findFirst().orElse(null));
    getField(PERSOON, GbaNativeSelect.class).addListener((ValueChangeListener) this::enableAnrField);
    getField(STATUS, GbaNativeSelect.class).setContainerDataSource(statusContainer);
    getField(DATUM_MUTATIE, GbaNativeSelect.class).setValue(new Vandaag());
    getField(CAT, GbaNativeSelect.class).setContainerDataSource(new CatContainer());
    getFields(PERSOON, ANR, RELATIES, STATUS, CAT, DATUM_MUTATIE)
        .forEach(field -> field.addListener(beanChangeListener));
  }

  private void enableAnrField(com.vaadin.data.Property.ValueChangeEvent event) {
    getField(ANR).setValue("");
    getField(ANR).setVisible(false);
    if (event.getProperty().getValue() != null) {
      getField(RELATIES).setReadOnly(false);
      if (event.getProperty().getValue() instanceof String) {
        getField(ANR).setVisible(true);
      }
    } else {
      getField(RELATIES).setValue(null);
      getField(RELATIES).setReadOnly(true);
    }
    repaint();
  }

  @Override
  public void setColumn(TableLayout.Column column, Field field, Property property) {
    if (property.is(ANR)) {
      column.setAppend(true);
    }
    if (property.is(STATUS)) {
      column.setColspan(3);
    }
    super.setColumn(column, field, property);
  }

  public void setBeanChangeListener(Consumer<Page1MutationsIndexBean> beanChangeListener) {
    this.beanChangeListener = beanChangeListener;
  }

  public class PersoonContainer extends IndexedContainer implements ProcuraContainer {

    public PersoonContainer() {
      addContainerProperty(OMSCHRIJVING, String.class, "");

      Item item1 = addItem(new AnrFieldValue(pl.getPersoon().getAnr().getVal()));
      item1.getItemProperty(OMSCHRIJVING).setValue(pl.getPersoon().getNaam().getNaamNaamgebruikEersteVoornaam());

      Item item2 = addItem("Anders");
      item2.getItemProperty(OMSCHRIJVING).setValue("Anders");
    }
  }

  public static class CatContainer extends IndexedContainer implements ProcuraContainer {

    public CatContainer() {
      addContainerProperty(OMSCHRIJVING, String.class, "");
      Arrays.stream(GBACat.values()).forEach(this::add);
    }

    private void add(GBACat cat) {
      if (cat.getCode() > 0) {
        Item item = addItem(cat);
        item.getItemProperty(OMSCHRIJVING).setValue(cat.getCode() + ": " + cat.getDescr());
      }
    }
  }

  public static class StatusContainer extends IndexedContainer implements ProcuraContainer {

    public StatusContainer() {
      addContainerProperty(OMSCHRIJVING, String.class, "");
      add(MutationType.NIET_GOEDGEKEURD);
      add(MutationType.GOEDGEKEURD);
      add(MutationType.GOEDGEKEURD_EN_SPONTAAN_VOORBEREID);
      add(MutationType.GOEDGEKEURD_EN_SPONTAAN_VERWERKT);
      add(MutationType.GOEDGEKEURD_EN_SPONTAAN_MUTATIE);
      add(MutationType.NIET_MOGELIJK);
    }

    private void add(MutationType type) {
      Item item = addItem(type);
      item.getItemProperty(OMSCHRIJVING).setValue(type.getCode() + ": " + type.getDescription());
    }
  }
}
