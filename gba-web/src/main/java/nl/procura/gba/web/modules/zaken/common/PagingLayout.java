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
import static nl.procura.standard.Globalfunctions.*;

import java.util.Date;

import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.FieldEvents.BlurListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;

import nl.procura.gba.web.common.misc.ZaakPeriode;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.standard.ProcuraDate;
import nl.procura.vaadin.component.container.ArrayListContainer;
import nl.procura.vaadin.component.field.NumberField;
import nl.procura.vaadin.component.layout.HLayout;
import nl.procura.vaadin.component.layout.table.TableLayout;

import lombok.Data;

public class PagingLayout extends HLayout {

  private final Paging       paging = new Paging();
  private NumberField        pageSizeField;
  private NumberField        pageField;
  private GbaNativeSelect    periodeField;
  private GbaNativeSelect    sortField;
  private TableLayout        layout;
  private ReloadListener     reloadListener;
  private PagingLayoutConfig config;

  public PagingLayout(PagingLayoutConfig config) {
    this();
    this.config = config;
  }

  public PagingLayout() {
    this.config = PagingLayoutConfig.getDefault();
    setWidth("100%");
    align(Alignment.MIDDLE_RIGHT);
    layout = getLayout();
    addComponent(layout);
  }

  public void update() {
    if (layout.getParent() != null) {
      removeComponent(layout);
    }

    layout = getLayout();
    addComponent(layout);
  }

  public void setConfig(PagingLayoutConfig config) {
    this.config = config;
    update();
  }

  private TableLayout getLayout() {

    PeriodeContainer periodeContainer = new PeriodeContainer();
    SortContainer sortContainer = new SortContainer();

    Object periodeValue = null;
    if (periodeField != null) {
      periodeValue = periodeField.getValue();
    }

    Object sortValue = null;
    if (sortField != null) {
      sortValue = sortField.getValue();
    }

    pageSizeField = new NumberField();
    pageSizeField.setWidth("40px");
    pageSizeField.setImmediate(true);
    pageSizeField.setMaxLength(3);

    pageField = new NumberField();
    pageField.setWidth("40px");
    pageField.setImmediate(true);
    pageField.setMaxLength(3);

    Button prevButton = new Button("Vorige");
    Button nextButton = new Button("Volgende");
    prevButton.setWidth("70px");
    nextButton.setWidth("70px");

    pageField.setValue(getPaging().getPage() + 1);
    pageSizeField.setValue(getPaging().getPageSize());
    prevButton.setEnabled(!getPaging().isFirst());
    nextButton.setEnabled(!getPaging().isLast());
    prevButton.setWidth("100%");
    nextButton.setWidth("100%");

    if (config.isShowPeriod()) {
      periodeField = new GbaNativeSelect();
      periodeField.setContainerDataSource(periodeContainer);
      periodeField.setWidth("155px");
      periodeField.setImmediate(true);
      periodeField.setValue(periodeValue);
    }

    if (config.isShowSorting()) {
      sortField = new GbaNativeSelect();
      sortField.setContainerDataSource(sortContainer);
      sortField.setImmediate(true);
      sortField.setNullSelectionAllowed(false);
      sortField.setValue(sortValue != null ? sortValue : sortContainer.getIdByIndex(0));
    }

    layout = new TableLayout();
    layout.setWidth("100%");
    layout.setColumnWidths(config.getColumnWidths());
    layout.addData(new HLayout(prevButton, nextButton).widthFull());
    layout.addLabel("Aantal records");
    Label label = new Label(astr(getPaging().getTotalSize()));
    label.setWidth("70px");
    layout.addData(label);

    layout.addLabel("Pagina");
    layout.addData(new HLayout(pageField, new Label(" van " + getPaging().getPages())));

    layout.addLabel("Per pagina");
    layout.addData(pageSizeField);

    if (config.isShowPeriod()) {
      layout.addLabel("Periode");
      layout.addData(periodeField);

      periodeField.addListener((ValueChangeListener) e -> {
        Periode periode = (Periode) e.getProperty().getValue();
        getPaging().setFrom(null);
        getPaging().setUntil(null);
        if (periode != null) {
          getPaging().setFrom(new ProcuraDate(aval(periode.getdFrom())).getDateFormat());
          getPaging().setUntil(new ProcuraDate(aval(periode.getdTo())).getDateFormat());
        }
        reloadListener.onReload();
      });
    }

    pageField.addListener((BlurListener) e -> {
      getPaging().setPage(aval(pageField.getValue()) - 1);
      reloadListener.onReload();
    });

    pageSizeField.addListener((BlurListener) e -> {
      getPaging().setPageSize(aval(pageSizeField.getValue()));
      reloadListener.onReload();
    });

    if (config.isShowSorting()) {
      layout.addLabel("Sortering");
      layout.addData(sortField);

      sortField.addListener((ValueChangeListener) e -> {
        getPaging().setAscending(((Sort) sortField.getValue()).isAscending());
        reloadListener.onReload();
      });
    }

    prevButton.addListener((ClickListener) e -> {
      getPaging().setPage(getPaging().getPage() - 1);
      reloadListener.onReload();
    });

    nextButton.addListener((ClickListener) e -> {
      getPaging().setPage(getPaging().getPage() + 1);
      reloadListener.onReload();
    });

    return layout;
  }

  public void addReloadListener(final ReloadListener reloadListener) {
    this.reloadListener = reloadListener;
  }

  public Paging getPaging() {
    return paging;
  }

  public interface ReloadListener {

    void onReload();
  }

  @Data
  public static class Sort {

    private boolean ascending;
    private String  name;

    Sort(boolean ascending, String name) {
      this.ascending = ascending;
      this.name = name;
    }

    @Override
    public String toString() {
      return name;
    }
  }

  @Data
  public class Paging {

    private int     page      = 0;
    private int     pages     = 0;
    private int     pageSize  = 50;
    private long    totalSize = 0;
    private boolean first     = true;
    private boolean last      = true;
    private boolean ascending = false;
    private Date    from      = null;
    private Date    until     = null;
  }

  public class PeriodeContainer extends ArrayListContainer {

    PeriodeContainer() {
      setSort(false);
      addItems(asList(new Periode(0, 1, "Vandaag"), new Periode(-1, 0, "Gisteren"),
          new Periode(-7, 1, "Laatste 7 dagen"), new Periode(-30, 1, "Laatste 30 dagen"),
          new Periode(-60, 1, "Laatste 60 dagen"), new Periode(-90, 1, "Laatste 90 dagen"),
          new Periode(-365, 1, "Laatste jaar")));
    }
  }

  public class Periode extends ZaakPeriode {

    Periode(int start, int end, String descr) {
      setdFrom(along(new ProcuraDate().addDays(start).getSystemDate()));
      setdTo(along(new ProcuraDate().addDays(end).getSystemDate()));
      setDescr(descr);
    }
  }

  public class SortContainer extends ArrayListContainer {

    SortContainer() {
      setSort(false);
      addItem(new Sort(false, "Aflopend"));
      addItem(new Sort(true, "Oplopend"));
    }
  }
}
