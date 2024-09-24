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
  private final Label           label  = new Label();
  private final GbaNativeSelect select = new GbaNativeSelect();

  public RegistrantImportTableFilter(FileImport fileImport, IndexedTable table) {
    this.fileImport = fileImport;
    this.table = table;
    align(Alignment.MIDDLE_LEFT);
    // Table
    table.getFilterListeners().add((o, filter) -> count());
    table.setFilter(RegistrantImportTableFilter.class, getFilter("", Select.NIEUW));

    // Textfield
    TextField textField = new TextField();
    textField.setWidth("264px");
    textField.setInputPrompt("Zoeken ...");
    textField.setTextChangeEventMode(LAZY);
    textField.setTextChangeTimeout(200);
    textField.addListener((TextChangeListener) event -> {
      table.setFilter(RegistrantImportTableFilter.class, getFilter(event.getText(), (Select) select.getValue()));
    });
    add(textField);

    // Select
    select.setImmediate(true);
    select.setWidth("260px");
    select.setNullSelectionAllowed(false);
    select.setItemCaptionPropertyId(GbaContainer.OMSCHRIJVING);
    select.addListener((ValueChangeListener) event -> {
      Filter filter = getFilter((String) textField.getValue(), (Select) select.getValue());
      table.setFilter(RegistrantImportTableFilter.class, filter);
    });
    add(select);

    // Label
    label.setWidth("150px");
    add(label);
  }

  public class SelectContainer extends GbaContainer {

    public SelectContainer() {
      Count count = getApplication().getServices().getFileImportService().countRecords(fileImport);
      addItem(Select.NIEUW, String.format("Nieuwe aanmeldingen (%d)", count.getNewRecords()));
      addItem(Select.ALLE, String.format("Alle aanmeldingen (%d)", count.getTotalRecords()));
    }
  }

  @Getter
  private enum Select {

    NIEUW("Nieuwe aanmeldingen"),
    ALLE("Alle aanmeldingen");

    private final String descr;

    Select(String descr) {
      this.descr = descr;
    }

    @Override
    public String toString() {
      return descr;
    }
  }

  @Override
  public void attach() {
    select.setContainerDataSource(new SelectContainer());
    select.setValue(Select.NIEUW);
    super.attach();
  }

  @NotNull
  private static Filter getFilter(String pattern, Select select) {
    return new Filter() {

      @Override
      public boolean passesFilter(Object o, Item item) throws UnsupportedOperationException {
        IndexedTable.Record record = (IndexedTable.Record) item.getItemProperty("Record").getValue();
        FileImportRecord fir = record.getObject(FileImportRecord.class);
        if (Select.NIEUW == select && fir.isReference()) {
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
