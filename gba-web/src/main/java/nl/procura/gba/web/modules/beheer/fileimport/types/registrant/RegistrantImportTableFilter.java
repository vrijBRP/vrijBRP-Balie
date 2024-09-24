package nl.procura.gba.web.modules.beheer.fileimport.types.registrant;

import static com.vaadin.ui.AbstractTextField.TextChangeEventMode.LAZY;

import java.util.Arrays;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;

import nl.procura.gba.jpa.personen.db.FileImport;
import nl.procura.gba.web.components.containers.GbaContainer;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.modules.beheer.fileimport.types.FileImportTable;
import nl.procura.gba.web.modules.beheer.fileimport.types.FileImportTableFilter;
import nl.procura.gba.web.services.beheer.fileimport.FileImportRecord;
import nl.procura.gba.web.services.beheer.fileimport.FileImportService.Count;
import nl.procura.gba.web.services.beheer.fileimport.FileImportValue;
import nl.procura.standard.ProcuraDate;
import nl.procura.vaadin.component.table.indexed.IndexedTable;

import lombok.Getter;

public class RegistrantImportTableFilter extends FileImportTableFilter {

  private final FileImport      fileImport;
  private final IndexedTable    table;
  private final Label           label        = new Label();
  private final GbaNativeSelect statusSelect = new GbaNativeSelect();
  private final GbaNativeSelect sortSelect   = new GbaNativeSelect();

  public RegistrantImportTableFilter(FileImport fileImport, FileImportTable table) {
    this.fileImport = fileImport;
    this.table = table;
    align(Alignment.MIDDLE_LEFT);
    // Table
    table.getFilterListeners().add((o, filter) -> count());
    table.setFilter(RegistrantImportTableFilter.class, getFilter("", StatusSelect.NIEUW));

    // Textfield
    TextField textField = new TextField();
    textField.setWidth("264px");
    textField.setInputPrompt("Zoeken ...");
    textField.setTextChangeEventMode(LAZY);
    textField.setTextChangeTimeout(200);
    textField.addListener((TextChangeListener) event -> table.setFilter(RegistrantImportTableFilter.class,
        getFilter(event.getText(), (StatusSelect) statusSelect.getValue())));
    add(textField);

    // Select
    statusSelect.setImmediate(true);
    statusSelect.setWidth("260px");
    statusSelect.setNullSelectionAllowed(false);
    statusSelect.setItemCaptionPropertyId(GbaContainer.OMSCHRIJVING);
    statusSelect.addListener((ValueChangeListener) event -> {
      Filter filter = getFilter((String) textField.getValue(), (StatusSelect) statusSelect.getValue());
      table.setFilter(RegistrantImportTableFilter.class, filter);
    });
    add(statusSelect);

    // Select
    sortSelect.setImmediate(true);
    sortSelect.setWidth("260px");
    sortSelect.setNullSelectionAllowed(false);
    sortSelect.setItemCaptionPropertyId(GbaContainer.OMSCHRIJVING);
    sortSelect.addListener((ValueChangeListener) event -> {
      table.sort(event.getProperty().getValue());
    });
    add(sortSelect);

    // Label
    label.setWidth("150px");
    add(label);
  }

  public class StatusSelectContainer extends GbaContainer {

    public StatusSelectContainer() {
      Count count = getApplication().getServices().getFileImportService().countRecords(fileImport);
      addItem(StatusSelect.NIEUW, String.format("Status: nieuwe aanmeldingen (%d)", count.getNewRecords()));
      addItem(StatusSelect.ALLE, String.format("Status: alle aanmeldingen (%d)", count.getTotalRecords()));
    }
  }

  public static class SortSelectContainer extends GbaContainer {

    public SortSelectContainer() {
      Arrays.stream(SortSelect.values()).forEach(value -> addItem(value, "Sortering: " + value.descr));
    }
  }

  @Getter
  private enum StatusSelect {

    NIEUW("Nieuwe aanmeldingen"),
    ALLE("Alle aanmeldingen");

    private final String descr;

    StatusSelect(String descr) {
      this.descr = descr;
    }

    @Override
    public String toString() {
      return descr;
    }
  }

  @Getter
  public enum SortSelect {

    DATUM_BEWON_ASC("Datum bewoning (oud-nieuw)"),
    DATUM_BEWON_DESC("Datum bewoning (nieuw-oud)"),
    DATUM_INVOER_ASC("Datum invoer (oud-nieuw)"),
    DATUM_INVOER_DESC("Datum invoer (nieuw-oud)");

    private final String descr;

    SortSelect(String descr) {
      this.descr = descr;
    }

    @Override
    public String toString() {
      return descr;
    }
  }

  @Override
  public void attach() {
    statusSelect.setContainerDataSource(new StatusSelectContainer());
    statusSelect.setValue(StatusSelect.NIEUW);
    sortSelect.setContainerDataSource(new SortSelectContainer());
    sortSelect.setValue(SortSelect.DATUM_BEWON_ASC);
    super.attach();
  }

  @NotNull
  private static Filter getFilter(String pattern, StatusSelect select) {
    return new Filter() {

      @Override
      public boolean passesFilter(Object o, Item item) throws UnsupportedOperationException {
        IndexedTable.Record record = (IndexedTable.Record) item.getItemProperty("Record").getValue();
        FileImportRecord fir = record.getObject(FileImportRecord.class);
        if (StatusSelect.NIEUW == select && fir.isReference()) {
          return false;
        }
        return Arrays.stream(StringUtils.split(pattern, " "))
            .filter(StringUtils::isNotBlank)
            .allMatch(split -> fir.getValues().entrySet().stream()
                .anyMatch(fileImportValue -> isMatch(split, fileImportValue)));
      }

      private boolean isMatch(String split, Entry<String, FileImportValue> fileImportValue) {
        String value = fileImportValue.getValue().getValue();
        if (fileImportValue.getKey().contains("datum")) {
          value = new ProcuraDate(value).setAllowedFormatExceptions(true).getFormatDate();
        }
        return value.toLowerCase().contains(split.trim().toLowerCase());
      }

      @Override
      public boolean appliesToProperty(Object o) {
        return false;
      }
    };
  }

  private void count() {
    int count = this.table.getContainerDataSource().getItemIds().size();
    this.label.setValue("Aantal: " + count);
  }
}
