package nl.procura.burgerzaken.vrsclient.api;

import nl.procura.burgerzaken.vrsclient.model.SignaleringRequestV1Bsn;
import nl.procura.burgerzaken.vrsclient.model.SignaleringRequestV1PersoonsGegevens;
import nl.procura.burgerzaken.vrsclient.model.SignaleringRequestV2Bsn;
import nl.procura.burgerzaken.vrsclient.model.SignaleringRequestV2PersoonsGegevens;

public class SignaleringRequest {

  private String accessToken;
  private String pseudoniem;
  private String instantieCode;

  private SignaleringRequestV1Bsn              v1Bsn;
  private SignaleringRequestV1PersoonsGegevens v1PersoonsGegevens;

  private SignaleringRequestV2Bsn              v2Bsn;
  private SignaleringRequestV2PersoonsGegevens v2PersoonsGegevens;

  public String getAccessToken() {
    return accessToken;
  }

  public SignaleringRequest accessToken(String accessToken) {
    this.accessToken = accessToken;
    return this;
  }

  public String pseudoniem() {
    return pseudoniem;
  }

  public SignaleringRequest pseudoniem(String pseudoniem) {
    this.pseudoniem = pseudoniem;
    return this;
  }

  public String instantieCode() {
    return instantieCode;
  }

  public SignaleringRequest instantieCode(String instantieCode) {
    this.instantieCode = instantieCode;
    return this;
  }

  public SignaleringRequestV1Bsn v1Bsn() {
    return v1Bsn;
  }

  public SignaleringRequest v1Bsn(SignaleringRequestV1Bsn bsn) {
    this.v1Bsn = bsn;
    return this;
  }

  public SignaleringRequestV1PersoonsGegevens v1PersoonsGegevens() {
    return v1PersoonsGegevens;
  }

  public SignaleringRequest v1PersoonsGegevens(SignaleringRequestV1PersoonsGegevens persoonsGegevens) {
    this.v1PersoonsGegevens = persoonsGegevens;
    return this;
  }

  public SignaleringRequestV2Bsn v2Bsn() {
    return v2Bsn;
  }

  public SignaleringRequest v2Bsn(SignaleringRequestV2Bsn bsn) {
    this.v2Bsn = bsn;
    return this;
  }

  public SignaleringRequestV2PersoonsGegevens v2PersoonsGegevens() {
    return v2PersoonsGegevens;
  }

  public SignaleringRequest v2PersoonsGegevens(SignaleringRequestV2PersoonsGegevens persoonsGegevens) {
    this.v2PersoonsGegevens = persoonsGegevens;
    return this;
  }
}
