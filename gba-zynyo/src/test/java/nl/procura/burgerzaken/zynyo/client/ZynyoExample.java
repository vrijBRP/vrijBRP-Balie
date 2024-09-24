/*
 * Copyright 2024 - 2025 Procura B.V.
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

package nl.procura.burgerzaken.zynyo.client;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;

import nl.procura.burgerzaken.zynyo.api.ApiClientConfig;
import nl.procura.burgerzaken.zynyo.api.SigningApi;
import nl.procura.burgerzaken.zynyo.api.model.AuthenticationMethod;
import nl.procura.burgerzaken.zynyo.api.model.DocumentInfo;
import nl.procura.burgerzaken.zynyo.api.model.SignDocumentResponse;
import nl.procura.burgerzaken.zynyo.api.model.SignMultiDocumentRequest;
import nl.procura.burgerzaken.zynyo.api.model.SignSingleDocumentRequest;
import nl.procura.burgerzaken.zynyo.api.model.Signatory;
import nl.procura.burgerzaken.zynyo.api.model.SignatoryLink;
import nl.procura.standard.Resource;
import nl.procura.standard.security.Base64;

import lombok.SneakyThrows;

public class ZynyoExample {

  public static void main(String[] args) {
    new ZynyoExample();
  }

  @SneakyThrows
  public ZynyoExample() {
    String endpoint = "";
    String apiKey = "";

    ApiClientConfig config = new ApiClientConfig()
        .baseUrl(endpoint)
        .apiKey(apiKey);

    sendSingleRequest(config, "example-zynyo1.pdf");
    sendSingleRequest(config, "example-zynyo2.pdf");
    sendMultiRequest(config, "example-zynyo1.pdf", "example-zynyo2.pdf");
  }

  public void sendSingleRequest(ApiClientConfig config, String document) throws IOException {
    SignSingleDocumentRequest request = new SignSingleDocumentRequest();
    DocumentInfo documentInfo = new DocumentInfo();
    documentInfo.setName("Test document");

    Signatory signatory = new Signatory();
    signatory.setName("Pietje Puk");
    signatory.setEmail("frits.janssen@shift2.nl");
    signatory.setLocale("nl_NL");

    List<AuthenticationMethod> authenticationMethods = new ArrayList<>();
    AuthenticationMethod authenticationMethod = new AuthenticationMethod();
    authenticationMethod.setType("documentanchor");
    authenticationMethod.setOrdernumber(0);
    authenticationMethods.add(authenticationMethod);
    signatory.setAuthenticationMethods(authenticationMethods);

    List<Signatory> signatories = new ArrayList<>();
    signatory.setPriority("DEFAULT");
    signatory.setSignatoryRole("SIGN");
    signatory.setDisableEmail(false);
    signatory.setDisableInvitation(true);
    signatory.setDisableStatusChange(true);
    signatories.add(signatory);

    request.documentInfo(documentInfo);
    request.signatories(signatories);
    request.reference(6545);

    request.content(Base64.encodeBytes(IOUtils.toByteArray(Resource.getAsInputStream(document))));
    SigningApi signingApi = new SigningApi(new OkHttpZynyoClient(config, Duration.ofSeconds(10)));
    SignDocumentResponse signDocumentRequest = signingApi.postSignDocumentRequest(request);

    System.out.println("signDocumentRequest.documentUUID: " + signDocumentRequest.documentUUID());
    for (SignatoryLink link : signDocumentRequest.signatoryLink()) {
      System.out.println("link.getEmail: " + link.getEmail());
      System.out.println("link.getRole: " + link.getRole());
      System.out.println("link.getSignatoryUUID: " + link.getSignatoryUUID());
      System.out.println("link.getDocumentLink: " + link.getDocumentLink());
    }
  }

  @SneakyThrows
  public void sendMultiRequest(ApiClientConfig config, String... documents) throws IOException {
    SignMultiDocumentRequest request = new SignMultiDocumentRequest();
    DocumentInfo documentInfo = new DocumentInfo();
    documentInfo.setName("Test document1");

    Signatory signatory = new Signatory();
    signatory.setName("Pietje Puk");
    signatory.setEmail("frits.janssen@shift2.nl");
    signatory.setLocale("nl_NL");

    List<AuthenticationMethod> authenticationMethods = new ArrayList<>();
    AuthenticationMethod authenticationMethod = new AuthenticationMethod();
    authenticationMethod.setType("documentanchor");
    authenticationMethod.setOrdernumber(0);
    authenticationMethods.add(authenticationMethod);
    signatory.setAuthenticationMethods(authenticationMethods);

    signatory.setPriority("DEFAULT");
    signatory.setSignatoryRole("SIGN");
    signatory.setDisableEmail(false);
    signatory.setDisableInvitation(true);
    signatory.setDisableStatusChange(true);

    List<Signatory> signatories = new ArrayList<>();
    signatories.add(signatory);

    request.documentInfo(documentInfo);
    request.signatories(signatories);
    request.reference(6545);

    request.content(Arrays.stream(documents)
        .map(ZynyoExample::toBase64)
        .collect(Collectors.toList()));
    request.documentNames(Arrays.asList(documents));
    request.mergeType("MERGE");

    SigningApi signingApi = new SigningApi(new OkHttpZynyoClient(config, Duration.ofSeconds(10)));
    SignDocumentResponse response = signingApi.postSignDocumentRequest(request);

    System.out.println("response.documentUUID: " + response.documentUUID());
    for (SignatoryLink link : response.signatoryLink()) {
      System.out.println("link.getEmail: " + link.getEmail());
      System.out.println("link.getRole: " + link.getRole());
      System.out.println("link.getSignatoryUUID: " + link.getSignatoryUUID());
      System.out.println("link.getDocumentLink: " + link.getDocumentLink());
    }
  }

  private static String toBase64(String document) {
    try {
      return Base64.encodeBytes(IOUtils.toByteArray(Resource.getAsInputStream(document)));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
