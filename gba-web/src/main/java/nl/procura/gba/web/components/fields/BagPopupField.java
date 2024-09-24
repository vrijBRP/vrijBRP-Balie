package nl.procura.gba.web.components.fields;

import static org.apache.commons.lang3.StringUtils.isBlank;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.event.FieldEvents.FocusListener;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.TextField;

import nl.procura.gba.common.MiscUtils;
import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.beheer.bag.PdokLocationServiceAddress;
import nl.procura.gba.web.services.interfaces.address.Address;
import nl.procura.gba.web.theme.GbaWebTheme;
import nl.procura.geo.rest.client.GeoRestClient;
import nl.procura.geo.rest.domain.pdok.locationserver.LocationServerDocResponse;
import nl.procura.geo.rest.domain.pdok.locationserver.LocationServerRequest;
import nl.procura.geo.rest.domain.pdok.locationserver.LocationServerResponse;
import nl.procura.vaadin.component.dialog.ModalWindow;
import nl.procura.vaadin.component.layout.HLayout;
import nl.procura.vaadin.component.layout.VLayout;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BagPopupField extends TextField {

  @Setter
  @Getter
  @Accessors(chain = true)
  private GeoRestClient geoRestClient;

  @Setter
  @Getter
  @Accessors(chain = true)
  private RequestListener requestListener;

  public BagPopupField() {
    setNullRepresentation("");
    addListener((FocusListener) event -> openPopup());
  }

  private void openPopup() {
    ((GbaApplication) BagPopupField.this.getApplication()).getParentWindow().addWindow(new BagPopupWindow(this));
  }

  @Override
  public Class<Address> getType() {
    return Address.class;
  }

  @Override
  public Address getValue() {
    Object value = super.getValue();
    return value instanceof Address ? (Address) value : null;
  }

  public interface RequestListener {

    LocationServerRequest getRequest(String value);
  }

  public static class BagPopupWindow extends ModalWindow {

    private String text = "";

    private final BagPopupField popupField;
    private final TextField     searchField = new TextField();
    private final Label         errorLabel  = new Label("", Label.CONTENT_XHTML);
    private final Table         table       = new Table();

    public BagPopupWindow(BagPopupField popupField) {
      this.popupField = popupField;
      setCaption("Zoeken in de BAG (Escape om te sluiten)");
      setWidth("600px");

      searchField.setInputPrompt("Zoeken ...");
      searchField.setNullRepresentation("");
      searchField.setWidth("100%");
      searchField.focus();
      searchField.setTextChangeEventMode(TextChangeEventMode.LAZY);
      searchField.setTextChangeTimeout(500);

      table.setSizeFull();
      table.setHeight("250px");

      VLayout layout = new VLayout();
      layout.spacing(true);
      layout.margin(true);
      layout.add(new HLayout(searchField, new ClearButton())
          .spacing(true)
          .expand(searchField, 1F)
          .widthFull());
      layout.add(errorLabel);
      layout.add(table);

      setContent(layout);

      searchField.addListener((TextChangeListener) event -> {
        text = event.getText();
        table.init();
      });

      if (popupField.getValue() instanceof Address) {
        text = popupField.getValue().getLabel();
        searchField.setValue(text);
      }
    }

    public class ClearButton extends NativeButton {

      public ClearButton() {
        setStyleName(GbaWebTheme.BUTTON_LINK);
        addStyleName("pl-url-button");
        setIcon(new ThemeResource("../gba-web/buttons/img/pl-delete.png"));
        setDescription("Maak veld leeg");
        setWidth("30px");
        addListener((ClickListener) event -> {
          searchField.setValue("");
          text = "";
          table.init();
        });
      }
    }

    public class Table extends GbaTable {

      @Override
      public void setColumns() {
        setClickable(true);
        setSelectable(true);
        addColumn("Adressen");
        super.setColumns();
      }

      @Override
      public void onClick(Record record) {
        popupField.setValue(record.getObject(Address.class));
        close();
        super.onClick(record);
      }

      @Override
      public void setRecords() {
        try {
          if (isBlank(text)) {
            popupField.setValue(null);
            addInfo("Geef minimaal 3 tekens in");

          } else if (text.length() < 3) {
            addInfo("Geef minimaal 3 tekens in");

          } else {
            LocationServerRequest request = popupField.getRequestListener().getRequest(text);
            LocationServerResponse response = popupField.getGeoRestClient()
                .getPdok()
                .getLocationServer()
                .search(request);

            if (response != null) {
              LocationServerDocResponse dr = response.getResponse();
              if (dr.getDocs().isEmpty()) {
                addInfo("Geen adressen gevonden");

              } else {
                if (dr.getDocs().size() < dr.getNumFound()) {
                  addInfo("Eerste " + dr.getDocs().size() + " gevonden adressen worden getoond");
                } else {
                  if (dr.getNumFound() == 1) {
                    addInfo("1 adres gevonden");
                  } else {
                    addInfo(dr.getNumFound() + " adressen gevonden");
                  }
                }
                dr.getDocs().forEach(doc -> addAddress(new PdokLocationServiceAddress(doc)));
              }
            }
          }
        } catch (RuntimeException e) {
          log.error("Fout bij zoeken", e);
          addError("Fout bij zoeken: " + e.getMessage());
        }

        super.setRecords();
      }

      private void addInfo(String error) {
        errorLabel.setValue(error);
      }

      private void addError(String error) {
        errorLabel.setValue(MiscUtils.setClass(false, error));
      }

      private void addAddress(Address address) {
        if (StringUtils.isNotBlank(address.getLabel())) {
          Record record = addRecord(address);
          record.addValue(address.getLabel());
        }
      }
    }
  }
}
