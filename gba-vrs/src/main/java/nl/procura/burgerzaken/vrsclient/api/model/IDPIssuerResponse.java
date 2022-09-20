package nl.procura.burgerzaken.vrsclient.api.model;

public class IDPIssuerResponse {

  private String issuerBaseUri = "";
  private String issuerUri     = "";

  public String getIssuerBaseUri() {
    return issuerBaseUri;
  }

  public void setIssuerBaseUri(String issuerBaseUri) {
    this.issuerBaseUri = issuerBaseUri;
  }

  public String getIssuerUri() {
    return issuerUri;
  }

  public void setIssuerUri(String issuerUri) {
    this.issuerUri = issuerUri;
  }
}
