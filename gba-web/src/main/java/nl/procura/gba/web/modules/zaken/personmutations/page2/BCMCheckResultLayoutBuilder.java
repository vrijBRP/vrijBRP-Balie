package nl.procura.gba.web.modules.zaken.personmutations.page2;

import static nl.procura.commons.core.exceptions.ProExceptionSeverity.ERROR;
import static nl.procura.commons.core.exceptions.ProExceptionType.WEBSERVICE;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;

import nl.procura.bsm.rest.client.BsmRestClientException;
import nl.procura.bsm.rest.client.BsmRestClientResponse;
import nl.procura.bsm.rest.v1_0.objecten.algemeen.BsmRestElement;
import nl.procura.bsm.rest.v1_0.objecten.algemeen.BsmRestElementAntwoord;
import nl.procura.bsm.rest.v1_0.objecten.gba.probev.mutations.MutationApproveRequestRestElement;
import nl.procura.bsm.rest.v1_0.objecten.gba.probev.mutations.MutationApproveResponseRestElement;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.gba.web.application.GbaApplication;

public class BCMCheckResultLayoutBuilder {

  private static final String QUERY_ID = "procura.personen.probev.bcm.check";
  private static final String PL_OK    = "pl_ok";

  private final String         anummer;
  private final GbaApplication application;

  private String get(BsmRestElement mut) {
    return mut.getWaarde();
  }

  private String getBCMResponse() throws BsmRestClientException {
    MutationApproveRequestRestElement requestRestElement = new MutationApproveRequestRestElement();
    requestRestElement.setAnummer(anummer);

    BsmRestClientResponse<BsmRestElementAntwoord> clientResponse = application.getServices()
        .getBsmService()
        .getBsmClient(false).getAlgemeen()
        .get(QUERY_ID, requestRestElement);

    MutationApproveResponseRestElement responseElement = new MutationApproveResponseRestElement();
    responseElement.setAntwoordElement(clientResponse.check().getEntity().getAntwoordElement());
    return get(responseElement.getOutput());
  }

  public BCMCheckResultLayoutBuilder(String anummer, GbaApplication application) {
    this.anummer = anummer;
    this.application = application;
  }

  public Page2BCMCheckResultLayout getBcmCheckResultLayout() {
    Page2BCMCheckResultLayout bcmCheckResultLayout = new Page2BCMCheckResultLayout();
    String responseOutput = "";

    try {
      responseOutput = getBCMResponse();

      String resultaatCode = JsonPath.read(responseOutput, "$.[0].BCM.resultaatcode");
      String toelichting = JsonPath.read(responseOutput, "$.[0].BCM.toelichting");
      String header;

      if (resultaatCode.equals(PL_OK)) {
        header = "PL is correct";
      } else {
        header = "PL is niet correct - Toelichting " + toelichting;

        Integer details = JsonPath.read(responseOutput, "$.[0].BCM.details.detail.length()");
        for (int i = 0; i < details; i++) {
          String omschrijving = JsonPath.read(responseOutput, "$.[0].BCM.details.detail[" + i + "].omschrijving");
          String code = JsonPath.read(responseOutput, "$.[0].BCM.details.detail[" + i + "].code");

          bcmCheckResultLayout.getTable().update(code, omschrijving);
        }
      }

      bcmCheckResultLayout.addComponentsWithHeader(header);
      return bcmCheckResultLayout;

    } catch (PathNotFoundException e) {
      throw new ProException(WEBSERVICE, ERROR, responseOutput);
    } catch (Exception e) {
      throw new ProException(WEBSERVICE, ERROR, e.getMessage(), e.getCause());
    }
  }
}
