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

package nl.procura.gba.web.rest.v1_0.zaak.zoeken.bs.nationaliteit;

import static nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType.NATIONALITEIT;
import static nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType.OPTIE;
import static nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType.PROCEDURE;

import nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElement;
import nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType;
import nl.procura.gba.web.rest.v1_0.zaak.zoeken.bs.GbaRestDossierHandler;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.naturalisatie.DossierNaturalisatie;

public class GbaRestNationaliteitHandler extends GbaRestDossierHandler {

  public GbaRestNationaliteitHandler(Services services) {
    super(services);
  }

  @Override
  protected void addDossierElementen(GbaRestElement dossierElement, Dossier zaak) {
    GbaRestElement nationaliteit = dossierElement.add(NATIONALITEIT);
    addAlgemeneElementen(nationaliteit, zaak);
    addProcedure(nationaliteit, zaak);
  }

  protected void addAlgemeneElementen(GbaRestElement parent, Dossier dossier) {
    if (dossier.getZaakDossier() instanceof DossierNaturalisatie) {
      DossierNaturalisatie dossierNaturalisatie = (DossierNaturalisatie) dossier.getZaakDossier();
      GbaRestElement personenElement = parent.add(GbaRestElementType.PERSONEN);
      for (DossierPersoon betrokkene : dossierNaturalisatie.getAlleVerzoekers()) {
        addPersoon(personenElement, getSoort(betrokkene.getDossierPersoonType()), betrokkene);
      }
    }
  }

  private void addProcedure(GbaRestElement parent, Dossier dossier) {
    DossierNaturalisatie naturalisatie = (DossierNaturalisatie) dossier.getZaakDossier();
    GbaRestElement procedureElement = parent.add(PROCEDURE);
    Boolean optie = naturalisatie.getOptie();
    add(procedureElement, OPTIE, Boolean.TRUE.equals(optie));
  }
}
