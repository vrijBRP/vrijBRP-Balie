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

package nl.procura.gba.web.rest.v1_0.zaak.zoeken.bs;

import static nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType.*;
import static nl.procura.gba.web.rest.v1_0.zaak.GbaRestElementHandler.add;
import static nl.procura.gba.web.services.bs.algemeen.akte.DossierAkteRegistersoort.*;
import static nl.procura.standard.exceptions.ProExceptionSeverity.ERROR;

import java.util.List;

import nl.procura.gba.web.rest.v1_0.GbaRestHandler;
import nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElement;
import nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.bs.algemeen.akte.DossierAkte;
import nl.procura.gba.web.services.bs.algemeen.akte.DossierAkteInvoerType;
import nl.procura.gba.web.services.bs.algemeen.akte.DossierAktePersoon;
import nl.procura.gba.web.services.bs.algemeen.akte.DossierAkteRegistersoort;
import nl.procura.standard.exceptions.ProException;

public class GbaRestDossierAkteHandler extends GbaRestHandler {

  public GbaRestDossierAkteHandler(Services services) {
    super(services);
  }

  public GbaRestElement getAktes(List<DossierAkte> aktes) {

    GbaRestElement aktesElement = new GbaRestElement(GbaRestElementType.AKTES);

    for (DossierAkte akte : aktes) {

      if (DossierAkteInvoerType.PROWEB_PERSONEN.is(akte.getInvoerType()) && !akte.isDossierVerwerkt()) {
        // Als wijze van invoer via Proweb Personen is, maar de zaak is niet meer gekoppeld aan een dossier
        // of is nog niet verwerkt dan overslaan.
        continue;
      }

      DossierAkteRegistersoort soort = DossierAkteRegistersoort.get(akte.getRegistersoort());
      GbaRestElement akteElement = aktesElement.add(GbaRestElementType.AKTE);
      add(akteElement, AKTE_NUMMER, akte.getAkte());
      add(akteElement, AKTE_BRP_NUMMER, akte.getBrpAkte());
      add(akteElement, AKTE_JAAR, akte.getJaar().longValue());
      add(akteElement, AKTE_PLAATS, "");
      add(akteElement, DATUM_AKTE, akte.getDatumIngang());
      add(akteElement, DATUM_FEIT, akte.getDatumFeit());
      add(akteElement, REGISTERDEEL, akte.getRegisterdeel());
      add(akteElement, REGISTERSOORT, soort.getCode(), soort.getOms());
      add(akteElement, VOLGNUMMER, akte.getFormatVnr());

      DossierAkteInvoerType invoerType = akte.getInvoerType();
      add(akteElement, GbaRestElementType.TYPE_INVOER, invoerType.getCode(), invoerType.getOms());

      GbaRestElement personenElement = akteElement.add(GbaRestElementType.PERSONEN);

      if (akte.getAkteRegistersoort().is(AKTE_ERKENNING_NAAMSKEUZE, AKTE_GEBOORTE)) {
        addAktePersoon(personenElement, KIND, akte.getAktePersoon());

      } else if (akte.getAkteRegistersoort().is(AKTE_HUWELIJK, AKTE_GPS)) {
        addAktePersoon(personenElement, PARTNER, akte.getAktePersoon());
        addAktePersoon(personenElement, PARTNER, akte.getAktePartner());

      } else if (akte.getAkteRegistersoort().is(AKTE_OVERLIJDEN)) {
        addAktePersoon(personenElement, OVERLEDENE, akte.getAktePersoon());

      } else {
        throw new ProException(ERROR, "Onbekende akteregistersoort: " + akte.getAkteRegistersoort());
      }
    }

    return aktesElement;
  }

  private void addAktePersoon(GbaRestElement personenElement, String soort, DossierAktePersoon p) {

    GbaRestElement persoonElement = personenElement.add(PERSOON);
    add(persoonElement, SOORT, soort);
    add(persoonElement, BSN, p.getBurgerServiceNummer());
    add(persoonElement, GESLACHTSNAAM, p.getGeslachtsnaam());
    add(persoonElement, VOORVOEGSEL, p.getVoorvoegsel());
    add(persoonElement, VOORNAMEN, p.getVoornaam());
    add(persoonElement, GESLACHTSAANDUIDING, p.getGeslacht());
    add(persoonElement.add(GEBOORTE), DATUM_GEBOORTE, p.getGeboortedatum());
  }
}
