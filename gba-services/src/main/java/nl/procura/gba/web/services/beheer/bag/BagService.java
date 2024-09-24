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

package nl.procura.gba.web.services.beheer.bag;

import static java.util.Objects.requireNonNull;
import static nl.procura.geo.rest.domain.ngr.wfs.types.FeatureType.LIGPLAATS;
import static nl.procura.geo.rest.domain.ngr.wfs.types.FeatureType.STANDPLAATS;
import static nl.procura.geo.rest.domain.ngr.wfs.types.FeatureType.VERBLIJFSOBJECT;
import static nl.procura.geo.rest.domain.pdok.locationserver.SearchType.HUISLETTER;
import static nl.procura.geo.rest.domain.pdok.locationserver.SearchType.HUISNUMMER;
import static nl.procura.geo.rest.domain.pdok.locationserver.SearchType.HUISNUMMERTOEVOEGING;
import static nl.procura.geo.rest.domain.pdok.locationserver.SearchType.POSTCODE;
import static nl.procura.geo.rest.domain.pdok.locationserver.SearchType.TYPE;
import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.isTru;
import static org.apache.commons.lang3.StringUtils.isNoneBlank;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.jetbrains.annotations.NotNull;

import nl.procura.gba.web.services.AbstractService;
import nl.procura.gba.web.services.aop.ThrowException;
import nl.procura.gba.web.services.beheer.parameter.ParameterConstant;
import nl.procura.gba.web.services.beheer.parameter.ParameterService;
import nl.procura.gba.web.services.interfaces.address.Address;
import nl.procura.gba.web.services.interfaces.address.AddressRequest;
import nl.procura.geo.rest.client.GeoRestClient;
import nl.procura.geo.rest.domain.ngr.wfs.SearchParam;
import nl.procura.geo.rest.domain.ngr.wfs.SearchType;
import nl.procura.geo.rest.domain.ngr.wfs.WfsFeature;
import nl.procura.geo.rest.domain.ngr.wfs.WfsSearchRequest;
import nl.procura.geo.rest.domain.ngr.wfs.WfsSearchResponse;
import nl.procura.geo.rest.domain.ngr.wfs.types.FeatureType;
import nl.procura.geo.rest.domain.pdok.locationserver.LocationServerDoc;
import nl.procura.geo.rest.domain.pdok.locationserver.LocationServerDocResponse;
import nl.procura.geo.rest.domain.pdok.locationserver.LocationServerRequest;
import nl.procura.geo.rest.domain.pdok.locationserver.ServiceType;

public class BagService extends AbstractService {

  public BagService() {
    super("Geo/BAG");
  }

  public GeoRestClient getGeoClient() {
    ParameterService parameterService = getServices().getParameterService();
    String baseUrl = parameterService.getSysteemParameter(ParameterConstant.GEO_ENDPOINT).getValue();
    String username = parameterService.getSysteemParameter(ParameterConstant.GEO_USERNAME).getValue();
    String pw = parameterService.getSysteemParameter(ParameterConstant.GEO_PW).getValue();
    return isNoneBlank(username, pw)
        ? new GeoRestClient(baseUrl, username, pw)
        : new GeoRestClient(baseUrl);
  }

  public boolean isGeoServiceActive() {
    return isTru(getServices().getParameterService().getSysteemParameter(ParameterConstant.GEO_ENABLED).getValue());
  }

  /**
   * Search both PDOK services with all possible variations and then
   * merge the results
   */
  @ThrowException("Fout bij ophalen GEO berichten")
  public List<Address> search(AddressRequest request) {
    List<Address> addresses = new ArrayList<>();
    // Search all 3 types
    searchWfs(addresses, request, VERBLIJFSOBJECT);
    searchWfs(addresses, request, LIGPLAATS);
    searchWfs(addresses, request, STANDPLAATS);
    // Search location service and combine results
    // from both services
    searchLocationService(addresses, request);
    return addresses;
  }

  @ThrowException("Fout bij ophalen GEO berichten")
  public WfsSearchResponse searchWfsService(WfsSearchRequest request) {
    return getGeoClient().getPdok().getWfs().search(request);
  }

  /**
   * Performs a search on the PDOK WFS service
   */
  private void searchWfs(List<Address> addresses, AddressRequest request, FeatureType type) {
    if (!addresses.isEmpty()) { // Not empty so already with other type
      return;
    }

    WfsSearchRequest req = new WfsSearchRequest()
        .setRequestorName("BRP / verhuizing")
        .setOffset(0)
        .setRows(10)
        .setFeatureType(type);

    requireNonNull(request.getPc(), "Postcode is verplicht"); // Minimal requirement
    requireNonNull(request.getHnr(), "Huisnummer is verplicht"); // Minimal requirement

    if (StringUtils.isNotBlank(request.getPc())) {
      req.search(SearchType.POSTCODE, request.getPc());
    }

    if (StringUtils.isNotBlank(request.getHnr())) {
      req.search(SearchType.HUISNUMMER, request.getHnr());
    }

    req.search(new SearchParam(SearchType.HUISLETTER, request
        .getHnrL()).setExclude(StringUtils.isBlank(request.getHnrL())));

    req.search(new SearchParam(SearchType.TOEVOEGING, request
        .getHnrT()).setExclude(StringUtils.isBlank(request.getHnrT())));

    List<WfsFeature> features = getGeoClient().getPdok().getWfs().search(req).getFeatures();
    for (WfsFeature feature : features) {
      addresses.add(new PdokWfsServiceAddress(feature));
    }
  }

  /**
   * Performs a search on the PDOK locationservice
   */
  private List<Address> searchLocationService(List<Address> addresses, AddressRequest request) {
    LocationServerRequest req = new LocationServerRequest()
        .setRequestorName("BRP-suggestionbox")
        .setServiceType(ServiceType.FREE)
        .setOffset(0)
        .setRows(10)
        .search(TYPE, "adres");

    Objects.nonNull(request.getPc()); // Minimal requirement
    Objects.nonNull(request.getHnr()); // Minimal requirement

    if (StringUtils.isNotBlank(request.getPc())) {
      req.search(POSTCODE, request.getPc());
    }
    if (StringUtils.isNotBlank(request.getHnr())) {
      req.search(HUISNUMMER, request.getHnr());
    }

    req.search(new nl.procura.geo.rest.domain.pdok.locationserver.SearchParam(HUISLETTER, request.getHnrL())
        .setExclude(StringUtils
            .isBlank(request.getHnrL())));

    req.search(new nl.procura.geo.rest.domain.pdok.locationserver.SearchParam(HUISNUMMERTOEVOEGING, request.getHnrT())
        .setExclude(StringUtils.isBlank(request.getHnrT())));

    LocationServerDocResponse resp = getGeoClient().getPdok().getLocationServer().search(req).getResponse();
    for (LocationServerDoc doc : resp.getDocs()) {
      Optional<Address> address = addresses.stream()
          .filter(isTheSameAddress(doc))
          .findFirst();
      if (address.isPresent()) {
        PdokWfsServiceAddress wfsAddress = (PdokWfsServiceAddress) address.get();
        merge(new PdokLocationServiceAddress(doc), wfsAddress);
      } else {
        addresses.add(new PdokLocationServiceAddress(doc));
      }
    }

    return addresses;
  }

  @NotNull
  private static Predicate<Address> isTheSameAddress(LocationServerDoc doc) {
    return address -> new EqualsBuilder()
        .append(astr(address.getPostalCode()), doc.getPostcode())
        .append(astr(address.getHnr()), astr(doc.getHuisnummer()))
        .append(astr(address.getHnrL()), astr(doc.getHuisletter()))
        .append(astr(address.getHnrT()), astr(doc.getToevoeging()))
        .build();
  }

  /**
   * Merges the locationServiceAddress into WfsAddress
   */
  private void merge(PdokLocationServiceAddress locationServiceAddress, PdokWfsServiceAddress wfsAddress) {
    wfsAddress.recidenceCode = locationServiceAddress.getResidenceCode();
    wfsAddress.recidenceName = locationServiceAddress.getResidenceName();
    wfsAddress.municipalityCode = locationServiceAddress.getMunicipalityCode();
    wfsAddress.municipalityName = locationServiceAddress.getMunicipalityName();
    wfsAddress.inaId = locationServiceAddress.getInaId();
    wfsAddress.district = locationServiceAddress.getDistrict();
    wfsAddress.neighborhood = locationServiceAddress.getNeighborhood();
  }
}
