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

package nl.procura.gba.web.modules.zaken.contact;

import static nl.procura.gba.web.services.zaken.contact.ContactgegevensService.TEL_MOBIEL;
import static nl.procura.gba.web.services.zaken.contact.ContactgegevensService.TEL_MOBIEL_BL;
import static nl.procura.standard.Globalfunctions.fil;

import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.common.MiscUtils;
import nl.procura.gba.web.common.tables.GbaTables;
import nl.procura.gba.web.common.validators.GbaEmailValidator;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.contact.ZaakContactpersoon;
import nl.procura.gba.web.services.zaken.contact.Contact;
import nl.procura.gba.web.services.zaken.contact.ContactgegevensService;
import nl.procura.gba.web.services.zaken.contact.PlContactgegeven;
import nl.procura.sms.rest.number.DutchPhoneNumberParser;

public class ContactTabel extends GbaTable {

  private Contact contact;

  private ZaakContactpersoon     persoon  = null;
  private Zaak                   zaak     = null;
  private List<PlContactgegeven> gegevens = null;

  protected ContactTabel(Contact contact) {
    this.contact = contact;
  }

  public ContactTabel(Zaak zaak) {
    this.zaak = zaak;
  }

  public ContactTabel(ZaakContactpersoon persoon) {
    this.persoon = persoon;
  }

  @Override
  public void attach() {
    getRecords().clear();
    super.attach();
  }

  public void resetGegevens() {
    gegevens = null;
    init();
  }

  @Override
  public void setColumns() {

    addColumn("Gegeven", 160);
    addColumn("Bijgewerkt op", 100);
    addColumn("waarde").setUseHTML(true);

    super.setColumns();
  }

  @Override
  public void setRecords() {

    try {
      if (gegevens == null) {
        gegevens = new ArrayList<>();

        if (contact != null) {
          gegevens.addAll(getApplication().getServices().getContactgegevensService().getContactgegevens(contact));
        } else if (zaak != null) {
          gegevens.addAll(getApplication().getServices().getContactgegevensService().getContactgegevens(zaak));
        } else if (persoon != null) {
          gegevens.addAll(persoon.getContactgegevens());
        }
      }

      if (persoon != null) {
        Record r = addRecord(null);
        r.addValue("Naam");
        r.addValue("");
        r.addValue(persoon.getNaam());
      }

      for (PlContactgegeven aant : gegevens) {
        Record r = addRecord(aant);
        r.addValue(aant.getContactgegeven().getOms());
        r.addValue(aant.getDatum() != null ? aant.getDatum().getFormatDate() : "");
        r.addValue(aant.getAant() + getValidMessage(aant));
      }
    } catch (Exception e) {
      getApplication().handleException(getWindow(), e);
    }
  }

  private String getValidMessage(PlContactgegeven aant) {
    if (fil(aant.getAant())) {
      boolean isEmail = new GbaEmailValidator().isValid(aant.getAant());
      boolean isMobile = DutchPhoneNumberParser.getMobileNumber(aant.getAant()).isValid();

      if (aant.getContactgegeven().isGegeven(ContactgegevensService.EMAIL)) {
        if (!isEmail) {
          return MiscUtils.setClass(false, " (Geen geldig e-mailadres)");
        }
      }
      if (aant.getContactgegeven().isGegeven(TEL_MOBIEL)) {
        if (!isMobile) {
          return MiscUtils.setClass(false, " (geen geldig Nederlands mobiel telefoonnummer)");
        }
      }
      if (aant.getContactgegeven().isGegeven(TEL_MOBIEL_BL)) {
        return " (" + GbaTables.LAND.get(aant.getCountry()).getDescription() + ")";
      }
    }
    return "";
  }
}
