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

package nl.procura.gba.web.modules.bs.common.layouts.relocation;

import static nl.procura.gba.web.modules.bs.common.layouts.relocation.MunicipalAddressBean.*;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.GEO_SEARCH_DEFAULT;
import static nl.procura.gba.web.services.interfaces.address.AddressSourceType.BAG;
import static nl.procura.gba.web.services.interfaces.address.AddressSourceType.BRP;
import static nl.procura.geo.rest.domain.pdok.locationserver.SearchType.*;
import static nl.procura.geo.rest.domain.pdok.locationserver.ServiceType.SUGGEST;
import static nl.procura.standard.Globalfunctions.isTru;

import com.vaadin.ui.Field;

import nl.procura.gba.web.components.fields.BagSuggestionBox;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.components.listeners.FieldChangeListener;
import nl.procura.gba.web.modules.zaken.common.ToelichtingButton;
import nl.procura.gba.web.modules.zaken.common.ToelichtingWindow;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.interfaces.address.AddressSourceType;
import nl.procura.gba.web.services.zaken.algemeen.consent.ConsentProvider;
import nl.procura.gba.web.services.zaken.verhuizing.FunctieAdres;
import nl.procura.geo.rest.domain.pdok.locationserver.LocationServerRequest;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.vaadin.component.layout.table.TableLayout;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;

public class MunicipalAddressForm extends GbaForm<MunicipalAddressBean> {

  private static final String WOONADRES_INFO = "<ul> <li> Het adres waar betrokkene woont, waaronder begrepen het adres van een <br> woning die "
      + "zich in een voertuig of vaartuig bevindt, indien het voertuig of <br> vaartuig een vaste "
      + "stand- of ligplaats heeft, of, indien betrokkene op meer <br> dan één adres woont, het "
      + "adres waar hij naar redelijke verwachting <br> gedurende een half jaar de meeste malen "
      + "zal overnachten; </li>"
      + "<li> Het adres waar, bij het ontbreken van een adres als bedoeld onder 1, <br> betrokkene naar "
      + "redelijke verwachting gedurende drie maanden ten minste <br> twee derde van de tijd zal overnachten.</li> </ul>";

  private static final String BRIEFADRES_INFO = "<ul> <li> Geen beschikking over woonadres (zie voorwaarden \"woonadres\"); </li>"
      + "<li>Verblijf in aangewezen instelling (voor gezondheidszorg, op het gebied van <br> "
      + "kinderbescherming of penitentiaire inrichting) of door"
      + " B&W aangewezen <br> instelling op het terrein van maatschappelijke opvang;</li>"
      + "<li> In bijzondere gevallen om veiligheidsredenen (naar oordeel van de <br> burgemeester).</li>";

  private Services services;

  MunicipalAddressForm(final RelocationAddress address, Services services) {
    this.services = services;
    setColumnWidths("200px", "");
    setOrder(F_STREET, F_HOUSE_NUMBER, F_HOUSE_NUMBER_L, F_HOUSE_NUMBER_T, F_HOUSE_NUMBER_A, F_POSTAL_CODE, F_RESIDENCE,
        F_ADDRESS_FUNCTION, F_CONSENT_PROVIDER, F_NO_OF_PEOPLE);

    MunicipalAddressBean bean = new MunicipalAddressBean();
    bean.setSource(address.getSource());
    bean.setStreet(address.getStreet());
    bean.setHouseNumber(address.getHouseNumber());
    bean.setHouseNumberL(address.getHouseNumberL());
    bean.setHouseNumberT(address.getHouseNumberT());
    bean.setHouseNumberA(address.getHouseNumberA());
    bean.setPostalCode(address.getPostalCode());
    bean.setResidence(address.getResidence());
    bean.setAddressFunction(address.getAddressFunction());
    bean.setNoOfPeople(address.getNoOfPeople());
    bean.setConsentProvider(address.getConsentProvider());
    bean.setBagAddress(address.getBagAddress());
    setBean(bean);
    setFields(address.getSource());
  }

  @Override
  public void reset() {
    super.reset();
    getBean().setNoOfPeople(new FieldValue());
    setFields(getDefaultAddressSource());
  }

  @Override
  public void setColumn(final TableLayout.Column column, final Field field, final Property property) {
    if (property.is(F_HOUSE_NUMBER, F_HOUSE_NUMBER_L, F_HOUSE_NUMBER_T, F_HOUSE_NUMBER_A, F_RESIDENCE)) {
      column.setAppend(true);
    }

    super.setColumn(column, field, property);
  }

  @Override
  public void afterSetColumn(Column column, Field field, Property property) {
    if (property.is(MunicipalAddressBean.F_ADDRESS_FUNCTION)) {
      column.addComponent(new ToelichtingButton() {

        @Override
        public void buttonClick(ClickEvent event) {
          final FunctieAdres functie = (FunctieAdres) getField(MunicipalAddressBean.F_ADDRESS_FUNCTION).getValue();
          if (FunctieAdres.WOONADRES == functie) {
            getWindow().addWindow(new ToelichtingWindow("Voorwaarden woonadres", WOONADRES_INFO));
          }
          if (FunctieAdres.BRIEFADRES == functie) {
            getWindow().addWindow(new ToelichtingWindow("Voorwaarden briefadres", BRIEFADRES_INFO));
          }
        }
      });
    }
  }

  @Override
  public void afterSetBean() {
    super.afterSetBean();
    Field source = getField(F_SOURCE);
    if (source != null) {
      source.addListener(new FieldChangeListener<AddressSourceType>() {

        @Override
        public void onChange(AddressSourceType value) {
          setFields(value);
        }
      });
      BagSuggestionBox suggestionBox = getField(F_BAG_ADDRESS, BagSuggestionBox.class);
      if (suggestionBox != null) {
        String gemCode = Services.getInstance().getGebruiker().getGemeenteCode();
        suggestionBox.setGeoRestClient(Services.getInstance().getGeoService().getGeoClient())
            .setRequestListener(value -> new LocationServerRequest()
                .setRequestorName("BRP-suggestionbox")
                .setServiceType(SUGGEST)
                .setOffset(0).setRows(10)
                .search(TYPE, "adres")
                .search(GEMEENTE_CODE, gemCode)
                .search(value)
                .filters(WEERGAVENAAM, ADRESSEERBAAR_OBJECT_ID));
      }
    }
  }

  void setConsentProvider(ConsentProvider consentProvider) {
    final MunicipalAddressBean bean = getBean();
    bean.setConsentProvider(consentProvider);
    setBean(bean);
  }

  private void setFields(AddressSourceType type) {
    if (AddressSourceType.UNKNOWN.is(type)) {
      type = getDefaultAddressSource();
    }
    if (isGeoServiceActive() && AddressSourceType.BAG == type) {
      showBagFields();
    } else {
      showBrpFields();
    }
  }

  private void showBagFields() {
    setOrder(F_SOURCE, F_BAG_ADDRESS, F_ADDRESS_FUNCTION, F_CONSENT_PROVIDER, F_NO_OF_PEOPLE);
    getBean().setSource(AddressSourceType.BAG);
    setBean(getBean());
  }

  private void showBrpFields() {
    if (isGeoServiceActive()) {
      setOrder(F_SOURCE, F_STREET, F_HOUSE_NUMBER, F_HOUSE_NUMBER_L, F_HOUSE_NUMBER_T, F_HOUSE_NUMBER_A, F_POSTAL_CODE,
          F_RESIDENCE, F_ADDRESS_FUNCTION, F_CONSENT_PROVIDER, F_NO_OF_PEOPLE);
    } else {
      setOrder(F_STREET, F_HOUSE_NUMBER, F_HOUSE_NUMBER_L, F_HOUSE_NUMBER_T, F_HOUSE_NUMBER_A, F_POSTAL_CODE,
          F_RESIDENCE, F_ADDRESS_FUNCTION, F_CONSENT_PROVIDER, F_NO_OF_PEOPLE);
    }

    getBean().setSource(AddressSourceType.BRP);
    setBean(getBean());
  }

  private boolean isGeoServiceActive() {
    return services.getGeoService().isGeoServiceActive();
  }

  private AddressSourceType getDefaultAddressSource() {
    boolean isDefaultBag = isTru(services.getParameterService().getParm(GEO_SEARCH_DEFAULT));
    return isGeoServiceActive() && isDefaultBag ? BAG : BRP;
  }
}
