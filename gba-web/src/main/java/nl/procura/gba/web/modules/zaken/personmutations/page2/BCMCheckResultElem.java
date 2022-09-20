package nl.procura.gba.web.modules.zaken.personmutations.page2;

public class BCMCheckResultElem {

  private final String code;
  private final String omschrijving;

  public BCMCheckResultElem() {
    this.code = "";
    this.omschrijving = "";
  }

  public BCMCheckResultElem(String code, String omschrijving) {
    this.code = code;
    this.omschrijving = omschrijving;
  }

  public String getCode() {
    return code;
  }

  public String getOmschrijving() {
    return omschrijving;
  }
}
