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

package nl.procura.gba.web.rest.v2.services;

import static nl.procura.gba.web.services.zaken.contact.ContactgegevensService.*;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import nl.procura.gba.web.rest.v2.model.base.HeeftContactgegevens;
import nl.procura.gba.web.rest.v2.model.zaken.verhuizing.GbaRestContactgegevens;
import nl.procura.gba.web.rest.v2.model.zaken.verhuizing.GbaRestTelefoonBuitenland;
import nl.procura.gba.web.services.zaken.contact.ContactgegevensService;
import nl.procura.gba.web.services.zaken.contact.PlContactgegeven;
import nl.procura.sms.rest.DutchPhoneNumberParser;
import nl.procura.validation.Bsn;

public class GbaRestContactService extends GbaRestAbstractService {

  public void setContactGegevens(HeeftContactgegevens aangever) {
    Bsn bsn = new Bsn(aangever.getBsn().toString());
    GbaRestContactgegevens contactgegevens = aangever.getContactgegevens();
    if (bsn.isCorrect() && contactgegevens != null) {
      String email = contactgegevens.getEmail();
      String telMobiel = contactgegevens.getTelefoonMobiel();
      String telThuis = contactgegevens.getTelefoonThuis();

      // If value is valid mobile number then store in mobile field. Could then be used for things like SMS.
      if (StringUtils.isBlank(telMobiel) && StringUtils.isNotBlank(telThuis)) {
        DutchPhoneNumberParser.MobileNumber telMobile = DutchPhoneNumberParser.getMobileNumber(telThuis);
        if (telMobile.isValid()) {
          telMobiel = telMobile.getFormats().standard();
          telThuis = "";
        }
      }

      ContactgegevensService service = getServices().getContactgegevensService();
      if (StringUtils.isNotBlank(email)) {
        service.setContactWaarde(-1, bsn.getLongBsn(), ContactgegevensService.EMAIL, email, -1);
      }
      if (StringUtils.isNotBlank(telMobiel)) {
        service.setContactWaarde(-1, bsn.getLongBsn(), ContactgegevensService.TEL_MOBIEL, telMobiel, -1);
      }
      if (StringUtils.isNotBlank(telThuis)) {
        service.setContactWaarde(-1, bsn.getLongBsn(), ContactgegevensService.TEL_THUIS, telThuis, -1);
      }
    }
  }

  public GbaRestContactgegevens toGbaRestContactgegevens(HeeftContactgegevens hasContactgegevens) {
    List<PlContactgegeven> gegevens = getServices()
        .getContactgegevensService()
        .getContactgegevens(-1, hasContactgegevens.getBsn(), "");
    if (!gegevens.isEmpty()) {
      GbaRestContactgegevens contact = new GbaRestContactgegevens();
      for (PlContactgegeven aant : gegevens) {
        String gegeven = aant.getContactgegeven().getGegeven();
        String waarde = aant.getAant();
        if (ContactgegevensService.TEL_MOBIEL.equalsIgnoreCase(gegeven)) {
          contact.setTelefoonMobiel(waarde);
        } else if (ContactgegevensService.TEL_MOBIEL_BL.equalsIgnoreCase(gegeven)) {
          GbaRestTelefoonBuitenland telBuitenland = new GbaRestTelefoonBuitenland();
          telBuitenland.setLandCode((int) aant.getCountry());
          telBuitenland.setTelefoon(waarde);
          contact.setTelefoonBuitenland(telBuitenland);
        } else if (ContactgegevensService.TEL_THUIS.equalsIgnoreCase(gegeven)) {
          contact.setTelefoonThuis(waarde);
        } else if (ContactgegevensService.TEL_WERK.equalsIgnoreCase(gegeven)) {
          contact.setTelefoonWerk(waarde);
        } else if (ContactgegevensService.EMAIL.equalsIgnoreCase(gegeven)) {
          contact.setEmail(waarde);
        }
      }
      return contact;
    }
    return null;
  }

  public void addContactInfo(HeeftContactgegevens persoon) {
    if (persoon != null) {
      GbaRestContactgegevens cg = persoon.getContactgegevens();
      if (cg != null) {
        ContactgegevensService contact = getServices().getContactgegevensService();
        long bsn = persoon.getBsn();
        addContactInfoValue(contact, bsn, TEL_THUIS, cg.getTelefoonThuis(), -1);
        addContactInfoValue(contact, bsn, TEL_MOBIEL, cg.getTelefoonMobiel(), -1);
        addContactInfoValue(contact, bsn, TEL_WERK, cg.getTelefoonWerk(), -1L);
        addContactInfoValue(contact, bsn, EMAIL, cg.getEmail(), -1L);
        GbaRestTelefoonBuitenland telefoonBuitenland = cg.getTelefoonBuitenland();
        if (telefoonBuitenland != null) {
          addContactInfoValue(contact, bsn, TEL_MOBIEL_BL,
              telefoonBuitenland.getTelefoon(),
              telefoonBuitenland.getLandCode());
        }
      }
    }
  }

  private void addContactInfoValue(ContactgegevensService service,
      long bsn,
      String type,
      String value,
      long landCode) {
    service.setContactWaarde(-1, bsn, type, value, landCode);
  }
}
