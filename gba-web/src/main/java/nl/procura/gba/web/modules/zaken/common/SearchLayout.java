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

package nl.procura.gba.web.modules.zaken.common;

import static java.util.Arrays.asList;
import static nl.procura.standard.Globalfunctions.along;
import static nl.procura.standard.Globalfunctions.astr;

import org.apache.commons.lang3.math.NumberUtils;

import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;

import nl.procura.gba.web.common.misc.ZaakPeriode;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.layouts.tablefilter.sort.ZaakSortField;
import nl.procura.gba.web.components.listeners.FieldChangeListener;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.gba.web.services.zaken.algemeen.ZaakSortering;
import nl.procura.standard.ProcuraDate;
import nl.procura.vaadin.component.container.ArrayListContainer;
import nl.procura.vaadin.component.field.NumberField;
import nl.procura.vaadin.component.layout.HLayout;

public class SearchLayout extends HLayout {

  private static final int      DEFAULT_MAX = 50;
  private final NumberField     numberField;
  private final GbaNativeSelect periodeField;
  private final ZaakSortField   sortField;

  SearchLayout() {
    numberField = new NumberField();
    numberField.setWidth("40px");
    numberField.setValue(DEFAULT_MAX);
    numberField.setImmediate(true);
    numberField.setMaxLength(3);

    sortField = new ZaakSortField();

    periodeField = new GbaNativeSelect();
    PeriodeContainer periodeContainer = new PeriodeContainer();
    periodeField.setContainerDataSource(periodeContainer);
    periodeField.setWidth("155px");
    periodeField.setImmediate(true);

    align(Alignment.MIDDLE_RIGHT);
    addComponent(new Label("Aantal tonen:"));
    addComponent(numberField);
    addComponent(periodeField);
    addComponent(sortField);
  }

  void addReloadListener(final ReloadListener reloadListener) {
    periodeField.addListener((ValueChangeListener) pe -> reloadListener.onReload());

    numberField.addListener((TextChangeListener) event -> {
      if (event.getText() != null) {
        numberField.setValue(event.getText());
        reloadListener.onReload();
      }
    });

    sortField.addListener(new FieldChangeListener<ZaakSortering>() {

      @Override
      public void onChange(ZaakSortering value) {
        reloadListener.onReload();
      }
    });
  }

  public void addToLayout(HLayout layout) {
    layout.setWidth("100%");
    layout.add(this);
    layout.setComponentAlignment(this, Alignment.MIDDLE_RIGHT);
    layout.setExpandRatio(this, 1f);
  }

  public ZaakArgumenten getZaakArgumenten() {
    return getZaakArgumenten(new ZaakArgumenten());
  }

  public ZaakArgumenten getZaakArgumenten(ZaakArgumenten zaakArgumenten) {

    if (sortField.getValue() != null) {
      zaakArgumenten.setSortering(sortField.getValue());
    }

    zaakArgumenten.setdInvoerVanaf(-1);
    zaakArgumenten.setdInvoerTm(-1);

    if (periodeField.getValue() != null) {
      ZaakPeriode periode = (ZaakPeriode) periodeField.getValue();
      zaakArgumenten.setdInvoerVanaf(periode.getdFrom());
      zaakArgumenten.setdInvoerTm(periode.getdTo());
    }

    int max = NumberUtils.toInt(astr(numberField.getValue()), DEFAULT_MAX);
    numberField.setValue(max);
    zaakArgumenten.setMax(max);
    return zaakArgumenten;
  }

  public interface ReloadListener {

    void onReload();
  }

  public class PeriodeContainer extends ArrayListContainer {

    PeriodeContainer() {
      setSort(false);
      addItems(asList(new Periode(7), new Periode(30), new Periode(60), new Periode(90), new Periode(365)));
    }
  }

  public class Periode extends ZaakPeriode {

    Periode(int dagen) {
      ProcuraDate d1 = new ProcuraDate();
      setdTo(along(d1.getSystemDate()));
      d1.addDays(-dagen);
      setdFrom(along(d1.getSystemDate()));
      setDescr("Laatste " + dagen + " dagen");
    }
  }
}
