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

package nl.procura.gba.web.rest.v1_0.zaak.zoeken.bs.onderzoek;

import static nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType.*;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElement;
import nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType;
import nl.procura.gba.web.rest.v1_0.zaak.zoeken.bs.GbaRestDossierHandler;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.onderzoek.DossierOnderzoek;
import nl.procura.gba.web.services.bs.onderzoek.enums.AanduidingOnderzoekType;
import nl.procura.gba.web.services.bs.onderzoek.enums.OnderzoekBronType;

public class GbaRestOnderzoekHandler extends GbaRestDossierHandler {

  public GbaRestOnderzoekHandler(Services services) {
    super(services);
  }

  protected void addAlgemeneElementen(GbaRestElement parent, Dossier dossier) {

    if (dossier.getZaakDossier() instanceof DossierOnderzoek) {
      DossierOnderzoek dossierOnderzoek = (DossierOnderzoek) dossier.getZaakDossier();
      GbaRestElement personenElement = parent.add(GbaRestElementType.PERSONEN);
      addPersoon(personenElement, getSoort(DossierPersoonType.AANGEVER), dossierOnderzoek.getAangever());
      for (DossierPersoon betrokkene : dossierOnderzoek.getBetrokkenen()) {
        addPersoon(personenElement, getSoort(betrokkene.getDossierPersoonType()), betrokkene);
      }
    }
  }

  @Override
  protected void addDossierElementen(GbaRestElement dossierElement, Dossier zaak) {
    GbaRestElement onderzoek = dossierElement.add(ONDERZOEK);
    addAlgemeneElementen(onderzoek, zaak);
    addAanleiding(onderzoek, zaak);
    addBeoordeling(onderzoek, zaak);
  }

  private void addAanleiding(GbaRestElement parent, Dossier dossier) {
    DossierOnderzoek onderzoek = (DossierOnderzoek) dossier.getZaakDossier();
    GbaRestElement aanleidingElement = parent.add(AANLEIDING);
    OnderzoekBronType bron = onderzoek.getOnderzoekBron();
    add(aanleidingElement, BRON, bron.getCode(), bron.getOms());
    switch (bron) {
      case TMV:
        aanleidingElement.add(TMV).add(KENMERK).set(onderzoek.getAanlTmvNr());
        break;
      case INSTANTIE:
        aanleidingElement.add(INSTANTIE).add(NAAM).set(onderzoek.getAanlInst());
        break;
      case AMBTSHALVE:
        aanleidingElement.add(AMBTSHALVE).add(NAAM).set(onderzoek.getAanlAfdeling());
        break;
      default:
        break;
    }
  }

  private void addBeoordeling(GbaRestElement parent, Dossier dossier) {
    DossierOnderzoek onderzoek = (DossierOnderzoek) dossier.getZaakDossier();
    GbaRestElement beoordelingElement = parent.add(BEOORDELING);

    DateTime datumOnderzoek = onderzoek.getDatumAanvangOnderzoek();
    AanduidingOnderzoekType aandOnderz = onderzoek.getAanduidingGegevensOnderzoek();

    add(beoordelingElement, AANDUIDING_ONDERZOEK, aandOnderz.getCode(), aandOnderz.getOms());
    add(beoordelingElement, DATUM_ONDERZOEK, datumOnderzoek);
  }
}
