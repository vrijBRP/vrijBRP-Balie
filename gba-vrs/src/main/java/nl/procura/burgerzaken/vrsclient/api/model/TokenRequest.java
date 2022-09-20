package nl.procura.burgerzaken.vrsclient.api.model;

public class TokenRequest {

  private String clientId;
  private String clientSecret;
  private String scope;
  private String resourceServer;

  public String clientId() {
    return clientId;
  }

  public TokenRequest clientId(String clientId) {
    this.clientId = clientId;
    return this;
  }

  public String clientSecret() {
    return clientSecret;
  }

  public TokenRequest clientSecret(String clientSecret) {
    this.clientSecret = clientSecret;
    return this;
  }

  public String scope() {
    return scope;
  }

  public TokenRequest scope(String scope) {
    this.scope = scope;
    return this;
  }

  public String resourceServer() {
    return resourceServer;
  }

  public TokenRequest resourceServer(String resourceServer) {
    this.resourceServer = resourceServer;
    return this;
  }
}
