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

package nl.procura.gba.web.modules.bs.common.pages.persoonpage;

import static nl.procura.gba.common.ZaakType.*;
import static nl.procura.gba.web.modules.bs.common.pages.persoonpage.BsPersoonBean1.*;
import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.*;
import static nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType.ONBEKEND;

import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.common.ZaakType;
import nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType;

public class BsPersoonRequirementChecker {

  private List<BsPersoonRequirement> requirements = new ArrayList<>();

  public BsPersoonRequirementChecker() {

    // Algemeen

    // Aangever (niet meer wijzigen)
    BsPersoonRequirement p = add(new BsPersoonRequirement(AANGEVER));
    p.setZaakTypes(GEBOORTE, ERKENNING, NAAMSKEUZE, LEVENLOOS, HUWELIJK_GPS_GEMEENTE);
    p.setBeanFields(NAAM, GEBOORTEDATUM, GEBOORTEPLAATS_NL, GEBOORTEPLAATS_BL, GEBOORTELAND);
    p.setDefinitiefNaControle(true);

    // Aangever (wel wijzigen)
    p = add(new BsPersoonRequirement(AANGEVER));
    p.setZaakTypes(OVERLIJDEN_IN_BUITENLAND, OVERLIJDEN_IN_GEMEENTE, LIJKVINDING);
    p.setBeanFields(NAAM, GEBOORTEDATUM, GEBOORTEPLAATS_NL, GEBOORTEPLAATS_BL, GEBOORTELAND);
    p.setDefinitiefNaControle(false);

    p = add(new BsPersoonRequirement(PARTNER, EXPARTNER));
    p.setBeanFields(NAAM, BS);

    p = add(new BsPersoonRequirement(GETUIGE));
    p.setBeanFields(NAAM, GEBOORTEDATUM);

    // Geboorte
    p = add(new BsPersoonRequirement(MOEDER, VADER_DUO_MOEDER, ERKENNER));
    p.setZaakTypes(GEBOORTE, LEVENLOOS, ERKENNING, NAAMSKEUZE);
    p.setBeanFields(NAAM, GESLACHT, GEBOORTEDATUM, GEBOORTEPLAATS_NL, GEBOORTEPLAATS_BL,
        GEBOORTELAND, WOONLAND, BS);
    p.setNationaliteitenVerplicht(true);
    p.setDefinitiefNaControle(true);

    // Overlijden
    p = add(new BsPersoonRequirement(OVERLEDENE));
    p.setZaakTypes(OVERLIJDEN_IN_BUITENLAND, OVERLIJDEN_IN_GEMEENTE, LEVENLOOS);
    p.setBeanFields(NAAM, GESLACHT, BS, WOONLAND, WOONPLAATS_NL, WOONPLAATS_BL);
    p.setDefinitiefNaControle(true);

    // Lijkvinding
    p = add(new BsPersoonRequirement(OVERLEDENE));
    p.setZaakTypes(LIJKVINDING);
    p.setBeanFields(NAAM, GESLACHT, BS);
    p.setDefinitiefNaControle(true);

    p = add(new BsPersoonRequirement(KIND, OUDER, VADER_DUO_MOEDER, MOEDER));
    p.setZaakTypes(OVERLIJDEN_IN_BUITENLAND, OVERLIJDEN_IN_GEMEENTE, LIJKVINDING, LEVENLOOS);
    p.setBeanFields(NAAM);

    // Huwelijk
    p = add(new BsPersoonRequirement(PARTNER1, PARTNER2));
    p.setZaakTypes(HUWELIJK_GPS_GEMEENTE);
    p.setBeanFields(NAAM, GESLACHT, GEBOORTEDATUM, GEBOORTEPLAATS_NL, GEBOORTEPLAATS_BL, GEBOORTELAND, WOONLAND,
        BS);
    p.setNationaliteitenVerplicht(true);
    p.setDefinitiefNaControle(true);

    p = add(new BsPersoonRequirement(KIND, VADER_DUO_MOEDER, MOEDER));
    p.setZaakTypes(HUWELIJK_GPS_GEMEENTE);
    p.setBeanFields(NAAM, GESLACHT, GEBOORTEDATUM, GEBOORTEPLAATS_NL, GEBOORTEPLAATS_BL, GEBOORTELAND, WOONLAND,
        BS);

    p = add(new BsPersoonRequirement(OUDER));
    p.setZaakTypes(HUWELIJK_GPS_GEMEENTE);
    p.setBeanFields(NAAM);

    p = add(new BsPersoonRequirement(PARTNER, EXPARTNER));
    p.setZaakTypes(HUWELIJK_GPS_GEMEENTE);
    p.setBeanFields(NAAM, GESLACHT, BS);

    p = add(new BsPersoonRequirement(GETUIGE));
    p.setZaakTypes(HUWELIJK_GPS_GEMEENTE);
    p.setBeanFields(NAAM, GESLACHT, GEBOORTEDATUM);

    p = add(new BsPersoonRequirement(ONBEKEND));
    p.setBeanFields(NAAM, GEBOORTEDATUM, GEBOORTEPLAATS_NL, GEBOORTEPLAATS_BL, GEBOORTELAND);
  }

  public BsPersoonRequirement getRequirement(ZaakType zaakType, DossierPersoonType dossierPersoonType) {

    for (BsPersoonRequirement re : requirements) {

      for (DossierPersoonType type : re.getPersoonTypes()) {
        if (type != dossierPersoonType) {
          continue;
        }

        if (zaakType == null || re.getZaakTypes().isEmpty()) {
          return re;
        }

        for (ZaakType zt : re.getZaakTypes()) {
          if (zt == zaakType) {
            return re;
          }
        }
      }
    }

    return new BsPersoonRequirement(ONBEKEND);
  }

  public List<BsPersoonRequirement> getRequirements() {
    return requirements;
  }

  public void setRequirements(List<BsPersoonRequirement> requirements) {
    this.requirements = requirements;
  }

  private BsPersoonRequirement add(BsPersoonRequirement bsPersoonRequirement) {
    requirements.add(bsPersoonRequirement);
    return bsPersoonRequirement;
  }
}
