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

package nl.procura.gba.web.modules.beheer.parameters.layout;

import static nl.procura.standard.Globalfunctions.astr;

import com.vaadin.ui.Button;

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.modules.beheer.parameters.layout.geo.GeoTestDialog;
import nl.procura.gba.web.services.beheer.parameter.ParameterConstant;
import nl.procura.gba.web.services.beheer.parameter.ParameterService;
import nl.procura.geo.rest.client.GeoRestClient;
import nl.procura.geo.rest.domain.ngr.wfs.WfsSearchRequest;
import nl.procura.geo.rest.domain.ngr.wfs.types.FeatureType;
import nl.procura.geo.rest.domain.pdok.locationserver.LocationServerRequest;
import nl.procura.geo.rest.domain.pdok.locationserver.SearchType;

public class GeoParameterLayout extends DatabaseParameterLayout {

  private final Button buttonTest = new Button("Test verbinding");

  public GeoParameterLayout(GbaApplication application, String naam, String category) {
    super(application, naam, category);
    addButton(buttonTest);
  }

  @Override
  public void handleEvent(Button button, int keyCode) {
    if (button == buttonTest) {
      onTest();
    }

    super.handleEvent(button, keyCode);
  }

  private void onTest() {

    getForm().commit();

    getWindow().addWindow(new GeoTestDialog() {

      @Override
      public String onTest1(String postcode, String hnr) {
        ParameterService parameterService = getServices().getParameterService();
        String baseUrl = parameterService.getSysteemParameter(ParameterConstant.GEO_ENDPOINT).getValue();
        GeoRestClient client = new GeoRestClient(baseUrl);
        LocationServerRequest request = new LocationServerRequest();
        request.search(SearchType.POSTCODE, postcode);
        request.search(SearchType.HUISNUMMER, hnr);
        request.setRequestorName("Procura Burgerzaken");
        return astr(client.getPdok().getLocationServer().search(request).getResponse().getDocs().size()
            + " verblijfsobject(en)");
      }

      @Override
      public String onTest2(String postcode, String hnr) {
        ParameterService parameterService = getServices().getParameterService();
        String baseUrl = parameterService.getSysteemParameter(ParameterConstant.GEO_ENDPOINT).getValue();
        GeoRestClient client = new GeoRestClient(baseUrl);
        WfsSearchRequest request = new WfsSearchRequest();
        request.search(nl.procura.geo.rest.domain.ngr.wfs.SearchType.POSTCODE, postcode);
        request.search(nl.procura.geo.rest.domain.ngr.wfs.SearchType.HUISNUMMER, hnr);
        request.setRequestorName("Procura Burgerzaken");
        request.setFeatureType(FeatureType.VERBLIJFSOBJECT);
        return astr(client.getPdok().getWfs().search(request).getTotalFeatures() + " verblijfsobject(en)");
      }
    });
  }
}
