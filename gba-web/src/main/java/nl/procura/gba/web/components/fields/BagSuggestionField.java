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

package nl.procura.gba.web.components.fields;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.data.util.filter.UnsupportedFilterException;
import com.vaadin.ui.ComboBox;

import nl.procura.gba.web.services.beheer.bag.PdokLocationServiceAddress;
import nl.procura.gba.web.services.interfaces.address.Address;
import nl.procura.geo.rest.client.GeoRestClient;
import nl.procura.geo.rest.domain.pdok.locationserver.LocationServerDocResponse;
import nl.procura.geo.rest.domain.pdok.locationserver.LocationServerRequest;
import nl.procura.geo.rest.domain.pdok.locationserver.LocationServerResponse;
import nl.procura.standard.exceptions.ProException;
import nl.procura.standard.exceptions.ProExceptionType;

import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

/**
 * Autocompletefield for BAG addresses
 *
 * Replaced with BagPopupField
 */
@Slf4j
@Deprecated
public class BagSuggestionField extends ComboBox {

  private static final String LABEL = "propertyId";

  @Setter
  @Accessors(chain = true)
  private GeoRestClient geoRestClient;

  @Setter
  @Accessors(chain = true)
  private RequestListener requestListener;

  public BagSuggestionField() {
    setImmediate(true);
    setTextInputAllowed(true);
    setNullSelectionAllowed(false);
    setItemCaptionMode(ITEM_CAPTION_MODE_PROPERTY);
    setItemCaptionPropertyId(LABEL);
    setFilteringMode(FILTERINGMODE_CONTAINS);
    setContainerDataSource(new BagContainer());
    addContainerProperty(LABEL, String.class, "");
  }

  @Override
  public Address getValue() {
    Object value = super.getValue();
    return value instanceof Address ? (Address) value : null;
  }

  @Override
  protected Filter buildFilter(String filterString, int filteringMode) {
    return new BagFilter(getItemCaptionPropertyId(), filterString);
  }

  public interface RequestListener {

    LocationServerRequest getRequest(String value);
  }

  public class BagContainer extends IndexedContainer {

    @Override
    public void addContainerFilter(Filter filter) throws UnsupportedFilterException {
      removeAllItems();
      if (getValue() != null) {
        addAddress(getValue());
      }
      if (geoRestClient == null) {
        throw new ProException(ProExceptionType.PROGRAMMING, "geoRestClient is not set");
      }
      if (requestListener == null) {
        throw new ProException(ProExceptionType.PROGRAMMING, "requestListener is not set");
      }
      String value = ((BagFilter) filter).getFilterString();
      if (StringUtils.isNotBlank(value)) {
        if (value.trim().length() < 3) {
          addError("Geef minimaal 3 tekens in ...");
        } else {
          try {
            LocationServerRequest request = requestListener.getRequest(value);
            LocationServerResponse response = geoRestClient
                .getPdok()
                .getLocationServer()
                .search(request);

            if (response != null) {
              LocationServerDocResponse dr = response.getResponse();
              if (dr.getDocs().isEmpty()) {
                addError("Geen zoekresultaten ...");
              } else {
                dr.getDocs().forEach(doc -> addAddress(new PdokLocationServiceAddress(doc)));
              }
            }
          } catch (RuntimeException e) {
            log.error("Fout bij zoeken", e);
            addError("Fout bij zoeken ... ");
          }
        }
      }
      super.addContainerFilter(filter);
    }

    private void addAddress(Address address) {
      addItem(address);
      getContainerProperty(address, LABEL).setValue(address.getLabel());
    }

    private void addError(String error) {
      addItem(error);
      getContainerProperty(error, LABEL).setValue(error);
    }
  }

  @Data
  public class BagFilter implements Filter {

    private final Object propertyId;
    private final String filterString;

    public BagFilter(Object propertyId, String filterString) {
      this.propertyId = propertyId;
      this.filterString = filterString;
    }

    @Override
    public boolean passesFilter(Object itemId, Item item) throws UnsupportedOperationException {
      return true; // Filtering is already done by BAG service
    }

    @Override
    public boolean appliesToProperty(Object propertyId) {
      return true;
    }

    public String getFilterString() {
      return filterString;
    }
  }
}
