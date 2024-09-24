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

package nl.procura.gba.web.rest.v1_0.zaak.toevoegen;

import static nl.procura.standard.Globalfunctions.*;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.ERROR;

import java.util.List;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElement;
import nl.procura.gba.web.rest.v1_0.zaak.GbaRestElementHandler;
import nl.procura.gba.web.rest.v1_0.zaak.GbaRestZaakType;
import nl.procura.gba.web.rest.v1_0.zaak.GbaRestZaakVraag;
import nl.procura.gba.web.rest.v1_0.zaak.GbaRestZaakVraagType;
import nl.procura.gba.web.rest.v1_0.zaak.zoeken.GbaRestZaakZoekenHandler;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.gba.web.services.zaken.identiteit.Identificatie;
import nl.procura.gba.web.services.zaken.identiteit.IdentificatieType;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.vaadin.component.field.fieldvalues.AnrFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;

public class GbaRestZaakAlgemeenToevoegenHandler extends GbaRestElementHandler {

  public GbaRestZaakAlgemeenToevoegenHandler(Services services) {
    super(services);
  }

  /**
   * Algemene zaakgegevens toevoegen
   */
  public <T extends Zaak> T algemeen(GbaRestZaakToevoegenVraag vraag, T zaak) {

    // ZaakId vullen als deze is meegegeven

    if (vraag.isZaakId()) {
      zaak.setZaakId(vraag.getZaakId());
    }

    // Overige algemene waarden
    zaak.setAnummer(new AnrFieldValue(vraag.getAnummer()));
    zaak.setBurgerServiceNummer(new BsnFieldValue(vraag.getBurgerServiceNummer()));
    zaak.setDatumTijdInvoer(
        new DateTime(toBigDecimal(vraag.getDatumInvoer()), toBigDecimal(vraag.getTijdInvoer())));
    zaak.setBron(vraag.getBron());
    zaak.setLeverancier(vraag.getLeverancier());

    // Identificatie
    Identificatie id = new Identificatie();
    id.setExternGeverificeerd(true);
    id.setBurgerServiceNummer(zaak.getBurgerServiceNummer());
    id.addIdentificatieType(IdentificatieType.EXTERNE_APPLICATIE);
    getServices().getIdentificatieService().addIdentificatie(id);

    return zaak;
  }

  /**
   * Controleer of er al een zaak is met het zaak-id
   */
  public void checkZaakId(Zaak zaak) {

    String zaakId = zaak.getZaakId();

    if (fil(zaakId)) {

      ZaakArgumenten zaakArgumenten = new ZaakArgumenten(zaak);
      zaakArgumenten.setMax(1);

      if (pos(getServices().getZakenService().getAantalZaken(zaakArgumenten))) {
        throw new ProException(ERROR, "Er bestaat al een zaak met zaak-id: " + zaakId);
      }
    }
  }

  public List<GbaRestElement> getAntwoord(String zaakId, GbaRestZaakType zaakType) {

    GbaRestZaakVraag zoekVraag = new GbaRestZaakVraag();
    zoekVraag.setZaakId(zaakId);
    zoekVraag.setTypen(zaakType);
    zoekVraag.setVraagType(GbaRestZaakVraagType.MINIMAAL);

    return new GbaRestZaakZoekenHandler(getServices()).getZaken(zoekVraag);
  }

}
