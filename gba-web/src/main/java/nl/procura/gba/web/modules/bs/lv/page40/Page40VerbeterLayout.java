package nl.procura.gba.web.modules.bs.lv.page40;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;

import nl.procura.gba.web.components.fields.GbaTextField;
import nl.procura.gba.web.services.bs.lv.afstamming.DossierLvVerbetering;
import nl.procura.vaadin.component.layout.HLayout;
import nl.procura.vaadin.component.layout.VLayout;
import nl.procura.vaadin.component.layout.table.TableLayout;

import lombok.Data;

public class Page40VerbeterLayout extends VLayout {

  private final List<Row> rows = new ArrayList<>();

  public Page40VerbeterLayout(List<DossierLvVerbetering> verbeteringen) {
    setWidth("100%");
    addStyleName("lv-verbetering");

    if (verbeteringen.isEmpty()) {
      rows.add(new Row());
    } else {
      verbeteringen.stream()
          .map(Row::new)
          .forEach(rows::add);
    }
    buildLayout();
  }

  private void buildLayout() {
    for (Row row : rows) {
      row.getLabelField().setParent(null);
      row.getValueField().setParent(null);
    }

    removeAllComponents();
    TableLayout layout = new TableLayout();
    layout.addStyleName("v-form tableform v-form-error");
    layout.setWidth("100%");
    layout.setColumnWidths("330px", "430px", "");

    layout.addLabel("Omschrijving");
    layout.addLabel("Nieuwe waarde");
    layout.addLabel("");

    for (Row row : rows) {
      layout.addData(row.getLabelField());
      layout.addData(row.getValueField());
      layout.addData(getButtonLayout(row));
    }

    add(layout);
  }

  private HLayout getButtonLayout(Row row) {
    HLayout hLayout = new HLayout();
    Button minusButton = new Button("-", (ClickListener) event -> {
      rows.remove(row);
      buildLayout();
    });
    minusButton.setEnabled(rows.size() > 1);

    hLayout.add(new Button("+", (ClickListener) event -> {
      rows.add(new Row());
      buildLayout();
    }));
    hLayout.add(minusButton);
    return hLayout;
  }

  public List<DossierLvVerbetering> getVerbeteringen() {
    List<DossierLvVerbetering> verbeteringen = new ArrayList<>();
    for (Row row : rows) {
      String label = row.getLabelField().getValue().toString();
      String value = row.getValueField().getValue().toString();
      if (StringUtils.isNoneBlank(label, value)) {
        verbeteringen.add(new DossierLvVerbetering(label, value));
      }
    }
    return verbeteringen;
  }

  public boolean validate() {
    boolean ok = true;
    for (Row row : rows) {
      try {
        row.getLabelField().setStyleName("");
        row.getLabelField().validate();
      } catch (Exception e) {
        row.getLabelField().setStyleName("v-textfield-error v-select-error v-filterselect-error");
        ok = false;
      }
      try {
        row.getValueField().setStyleName("");
        row.getValueField().validate();
      } catch (Exception e) {
        row.getValueField().setStyleName("v-textfield-error v-select-error v-filterselect-error");
        ok = false;
      }
    }
    return ok;
  }

  @Data
  private static class Row {

    private GbaTextField labelField = new GbaTextField();
    private GbaTextField valueField = new GbaTextField();

    public Row() {
      labelField.setRequired(true);
      valueField.setRequired(true);
      labelField.setWidth("300px");
      valueField.setWidth("400px");
    }

    public Row(String label, String value) {
      this();
      labelField.setValue(label);
      valueField.setValue(value);
    }

    public Row(DossierLvVerbetering verbetering) {
      this(verbetering.getOmschrijving(), verbetering.getWaarde());
    }
  }
}
