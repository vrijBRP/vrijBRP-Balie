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

package nl.procura.gba.web.services.beheer.requestinbox.zaken.vog;

import static nl.procura.commons.core.exceptions.ProExceptionSeverity.ERROR;

import nl.procura.burgerzaken.requestinbox.api.model.InboxEnum;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.beheer.requestinbox.RequestInboxBody;
import nl.procura.gba.web.services.beheer.requestinbox.RequestInboxBodyProcessor;
import nl.procura.gba.web.services.beheer.requestinbox.RequestInboxItem;
import nl.procura.gba.web.services.zaken.algemeen.CaseProcessingResult;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

public class InboxVogProcessor extends RequestInboxBodyProcessor {

  public InboxVogProcessor(RequestInboxItem record, Services services) {
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
      InboxVogData data = RequestInboxBody.fromJson(getRecord(), InboxVogData.class);
      inboxBody.addBsn("BSN", data.getBsn())
          .add("Organisatie", data.getOrganisation())
          .add("Vertegenwoordiger", data.getRepresentative())
          .add("Straat", data.getStreet())
          .add("Huisnummer", data.getHouseNumber())
          .add("Huisletter", data.getHouseLetter())
          .add("Huisnummer toevoeging", data.getHouseNumberAddition())
          .add("Postcode", data.getPostalCode())
          .add("Plaats", data.getCity())
          .add("Land", data.getCountry())
          .add("Telefoonnummer", data.getPhoneNumber())
          .add("Doel", data.getPurpose())
          .add("Functie", data.getFunction())
          .add("Screeningsprofiel", data.getScreeningProfile())
          .add("Speciale omstandigheden", data.getSpecialCircumstances())
          .add("Opmerking", data.getComment());

    } catch (RuntimeException e) {
      log(ERROR, "Fout bij inlezen verzoek", e.getMessage());
      inboxBody.add("Fout bij inlezen verzoek", e.getMessage());
    }
    return inboxBody;
  }

  @Data
  private static class InboxVogData {

    private Long        bsn;
    private String      organisation;
    private String      representative;
    private String      street;
    private String      houseNumber;
    private String      houseLetter;
    private String      houseNumberAddition;
    private String      postalCode;
    private String      city;
    private String      country;
    private String      phoneNumber;
    private PurposeType purpose;
    private String      function;
    private String      screeningProfile;
    private Boolean     specialCircumstances;
    private String      comment;
  }

  @Getter
  @AllArgsConstructor
  private enum PurposeType implements InboxEnum<String> {

    WERKRELATIE("werkrelatie", "Werkrelatie"),
    OVERIG("overig", "Overig");

    private final String id;
    private final String descr;

    @Override
    public String toString() {
      return descr;
    }
  }
}
