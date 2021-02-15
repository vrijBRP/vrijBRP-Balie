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

package nl.procura.gba.web.services.zaken.algemeen.consent;

import static java.util.Objects.requireNonNull;

import nl.procura.gba.web.services.gba.ple.relatieLijst.Relatie;

public final class ConsentProvider {

  private final Relatie brpConsentProvider;
  private final String  otherConsentProvider;
  private final Code    code;

  private ConsentProvider(Relatie consentProvider, String otherConsentProvider, Code code) {
    this.brpConsentProvider = consentProvider;
    this.otherConsentProvider = otherConsentProvider;
    this.code = code;
  }

  public Relatie getBrpConsentProvider() {
    return brpConsentProvider;
  }

  public String getOtherConsentProvider() {
    if (otherConsentProvider != null) {
      return otherConsentProvider;
    }
    return "";
  }

  public String getBsn() {
    if (brpConsentProvider != null) {
      return brpConsentProvider.getPl().getPersoon().getBsn().getVal();
    }
    return "";
  }

  public Long getConsentCode() {
    return code.toLong();
  }

  public static ConsentProvider consentProvider(Relatie consentProvider) {
    return new ConsentProvider(requireNonNull(consentProvider), null, Code.CONSENT);
  }

  public static ConsentProvider otherConsentProvider(String otherConsentProvider) {
    return new ConsentProvider(null, requireNonNull(otherConsentProvider), Code.CONSENT);
  }

  public static ConsentProvider notDeclared() {
    return new ConsentProvider(null, null, Code.NOT_DECLARED);
  }

  public static ConsentProvider from(Relatie brpConsentProvider, String otherConsentProvider) {
    if (otherConsentProvider != null && !otherConsentProvider.isEmpty()) {
      return otherConsentProvider(otherConsentProvider);
    }
    if (brpConsentProvider != null) {
      return consentProvider(brpConsentProvider);
    }
    return notDeclared();
  }

  // called to display value in UI
  @Override
  public String toString() {
    if (brpConsentProvider != null) {
      return brpConsentProvider.getPl().getPersoon().getNaam().getNaamNaamgebruikEersteVoornaam();
    }
    if (otherConsentProvider != null) {
      return otherConsentProvider;
    }
    return "Niet aangegeven";
  }

  public enum Code {

    NOT_DECLARED(-1L),
    DENIED(0L),
    CONSENT(1L);

    private final Long code;

    Code(Long code) {
      this.code = code;
    }

    private Long toLong() {
      return code;
    }
  }
}
