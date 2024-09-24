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

package nl.procura.gba.web.services.beheer.requestinbox.zaken.adresonderzoek;

import static nl.procura.commons.core.exceptions.ProExceptionSeverity.ERROR;

import java.time.LocalDate;

import com.google.gson.annotations.SerializedName;

import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.beheer.requestinbox.RequestInboxBody;
import nl.procura.gba.web.services.beheer.requestinbox.RequestInboxBodyProcessor;
import nl.procura.gba.web.services.beheer.requestinbox.RequestInboxItem;
import nl.procura.gba.web.services.zaken.algemeen.CaseProcessingResult;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

public class InboxAdresOnderzoekProcessor extends RequestInboxBodyProcessor {

  public InboxAdresOnderzoekProcessor(RequestInboxItem record, Services services) {
    super(record, services);
  }

  @Override
  public CaseProcessingResult process() {
    return new CaseProcessingResult();
  }

  @Override
  public RequestInboxBody getBody() {
    RequestInboxBody inboxBody = new RequestInboxBody();
    try {
      AdresOnderzoekInboxData data = RequestInboxBody.fromJson(getRecord(), AdresOnderzoekInboxData.class);
      inboxBody.addBsn("BSN aanvrager", data.getBsnApplicant())
          .add("BSN", data.getBsn())
          .add("Voorletters", data.getInitials())
          .add("Achternaam", data.getSurname())
          .add("Geboortedatum", data.getDateOfBirth())
          .add("Telefoonnummer", data.getPhoneNumber())
          .add("E-mailadres", data.getEmail())
          .add("Minderjarige kinderen", data.getMinorChildrenIncluded())
          .add("Reden onderzoek", data.getReasonResearchRequest())
          .add("Vertrekdatum", data.getLeavingDate())
          .add("Toelichting", data.getExplanation());
    } catch (RuntimeException e) {
      log(ERROR, "Fout bij inlezen verzoek", e.getMessage());
      inboxBody.add("Fout bij inlezen verzoek", e.getMessage());
    }
    return inboxBody;
  }

  @Data
  private static class AdresOnderzoekInboxData {

    private Long                      bsnApplicant;
    private Long                      bsn;
    private String                    initials;
    private String                    surname;
    private LocalDate                 dateOfBirth;
    private String                    phoneNumber;
    private String                    email;
    private Boolean                   minorChildrenIncluded;
    private ReasonResearchRequestType reasonResearchRequest;
    private LocalDate                 leavingDate;
    private String                    explanation;
  }

  @Getter
  @AllArgsConstructor
  private enum ReasonResearchRequestType {

    @SerializedName("person-lives-on-other-address")
    PERSON_LIVES_ON_OTHER_ADDRESS("Persoon woont op ander adres"),
    @SerializedName("person-has-left-to-other-country")
    PERSON_HAS_LEFT_TO_OTHER_COUNTRY("Persoon vertrokken naar ander land"),
    @SerializedName("person-has-left-to-unknown-location")
    PERSON_HAS_LEFT_TO_UNKNOWN_LOCATION("Persoon vertrokken naar onbekende bestemming"),
    @SerializedName("other")
    OTHER("Overige");

    private final String dutch;

    @Override
    public String toString() {
      return dutch;
    }
  }
}
