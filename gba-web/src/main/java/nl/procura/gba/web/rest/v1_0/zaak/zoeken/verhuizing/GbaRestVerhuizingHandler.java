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

package nl.procura.gba.web.rest.v1_0.zaak.zoeken.verhuizing;

import static nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType.*;

import nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElement;
import nl.procura.gba.web.rest.v1_0.zaak.GbaRestElementHandler;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.verhuizing.*;
import nl.procura.vaadin.component.field.fieldvalues.AnrFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class GbaRestVerhuizingHandler extends GbaRestElementHandler {

  public GbaRestVerhuizingHandler(Services services) {
    super(services);
  }

  public void convert(GbaRestElement gbaZaak, Zaak zaakParm) {

    VerhuisAanvraag zaak = (VerhuisAanvraag) zaakParm;

    GbaRestElement verhuizing = gbaZaak.add(VERHUIZING);

    add(verhuizing, TYPE, zaak.getTypeVerhuizing().getCode(), zaak.getTypeVerhuizing().getOms());
    add(verhuizing, ADRESSEERBAAR_OBJECT, zaak.getAanduidingAdresseerbaarObject());
    add(verhuizing, CODE_NUMMERAANDUIDING, zaak.getIdCodeNummeraanduiding());
    add(verhuizing, BESTEMMMING_HUIDIGE_BEWONERS, zaak.getBestemmingHuidigeBewoners());
    add(verhuizing, SPRAKE_VAN_INWONING, zaak.isSprakeVanInwoning());

    GbaRestElement adresElement = verhuizing.add(ADRES);
    GbaRestElement huidigAdresElement = adresElement.add(HUIDIG);

    VerhuisHoofdbewoner hoofdbewoner = zaak.getHoofdbewoner();
    if (hoofdbewoner != null) {
      GbaRestElement hbElement = verhuizing.add(HOOFDBEWONER);
      add(hbElement, BSN, hoofdbewoner.getBurgerServiceNummer());
    }

    VerhuisAangever aangever = zaak.getAangever();
    if (aangever != null) {
      GbaRestElement tgElement = verhuizing.add(AANGEVER);
      add(tgElement, ANR, aangever.getAnummer());
      add(tgElement, BSN, aangever.getBurgerServiceNummer());
      add(tgElement, AMBTSHALVE, aangever.isAmbtshalve() ? "J" : "N");
      add(tgElement, HOOFD_INSTELLING, aangever.isHoofdInstelling() ? "J" : "N");
      add(tgElement, TOELICHTING, aangever.getToelichting());
    }

    VerhuisToestemminggever toestemminggever = zaak.getToestemminggever();
    if (toestemminggever != null) {
      GbaRestElement tgElement = verhuizing.add(TOESTEMMINGEVER);
      add(tgElement, BSN, toestemminggever.getBurgerServiceNummer());
      add(tgElement, ANDERS, toestemminggever.getAnders());
      add(tgElement, AANGIFTE_STATUS, toestemminggever.getAangifteStatus().name());
      add(tgElement, TOESTEMMING_STATUS, toestemminggever.getToestemmingStatus().name());
    }

    VerhuisHerVestiging hervestiging = zaak.getHervestiging();
    if (hervestiging != null && zaak.getTypeVerhuizing().is(VerhuisType.HERVESTIGING)) {
      GbaRestElement hervElement = verhuizing.add(HERVESTIGING);
      add(hervElement, DATUM_HERVESTIGING, hervestiging.getDatumHervestiging());
      add(hervElement, DUUR_HERVESTIGING, hervestiging.getDuur());
      add(hervElement, LAND_HERVESTIGING, hervestiging.getLand());
      add(hervElement, RECHTSFEITEN_HERVESTIGING, hervestiging.getRechtsfeiten());
    }

    VerhuisEmigratie emigratie = zaak.getEmigratie();

    if (emigratie != null && zaak.getTypeVerhuizing().is(VerhuisType.EMIGRATIE)) {
      GbaRestElement emigratieElement = verhuizing.add(EMIGRATIE);
      add(emigratieElement, ADRES, emigratie.getAdres());
      add(emigratieElement, ADRES1, emigratie.getAdres1());
      add(emigratieElement, ADRES2, emigratie.getAdres2());
      add(emigratieElement, ADRES3, emigratie.getAdres3());
      add(emigratieElement, DATUM_VERTREK, emigratie.getDatumVertrek());
      add(emigratieElement, DUUR, (emigratie.getDuur().toLowerCase().contains("langer") ? "L" : "K"),
          emigratie.getDuur());
      add(emigratieElement, LAND, emigratie.getLand());
    }

    VerhuisAanvraagAdres ha = zaak.getHuidigAdres();

    if (ha != null) {
      FunctieAdres fa = ha.getFunctieAdres();
      add(huidigAdresElement, GEMEENTE, ha.getGemeente());
      add(huidigAdresElement, GEMEENTEDEEL, ha.getGemeenteDeel());
      add(huidigAdresElement, LOCATIE, ha.getLocatie());
      add(huidigAdresElement, POSTCODE, ha.getPc());
      add(huidigAdresElement, STRAAT, ha.getStraat());
      add(huidigAdresElement, WOONPLAATS, ha.getWoonplaats());
      add(huidigAdresElement, AANTAL_PERSONEN, ha.getAantalPersonen());
      add(huidigAdresElement, FUNCTIEADRES, fa.getCode(), fa.getOms());
      add(huidigAdresElement, HNR, ha.getHnr());
      add(huidigAdresElement, HNR_A, ha.getHnrA());
      add(huidigAdresElement, HNR_L, ha.getHnrL());
      add(huidigAdresElement, HNR_T, ha.getHnrT());
    }

    VerhuisAanvraagAdres na = zaak.getNieuwAdres();

    if (na != null && !zaak.getTypeVerhuizing().is(VerhuisType.EMIGRATIE)) {
      FunctieAdres fa = na.getFunctieAdres();
      GbaRestElement nieuwAdresElement = adresElement.add(NIEUW);
      add(nieuwAdresElement, GEMEENTE, na.getGemeente());
      add(nieuwAdresElement, GEMEENTEDEEL, na.getGemeenteDeel());
      add(nieuwAdresElement, LOCATIE, na.getLocatie());
      add(nieuwAdresElement, POSTCODE, na.getPc());
      add(nieuwAdresElement, STRAAT, na.getStraat());
      add(nieuwAdresElement, WOONPLAATS, na.getWoonplaats());
      add(nieuwAdresElement, AANTAL_PERSONEN, na.getAantalPersonen());
      add(nieuwAdresElement, FUNCTIEADRES, fa.getCode(), fa.getOms());
      add(nieuwAdresElement, HNR, na.getHnr());
      add(nieuwAdresElement, HNR_A, na.getHnrA());
      add(nieuwAdresElement, HNR_L, na.getHnrL());
      add(nieuwAdresElement, HNR_T, na.getHnrT());
    }

    GbaRestElement deelZaken = gbaZaak.add(DEELZAKEN);

    for (VerhuisPersoon persoon : zaak.getPersonen()) {

      GbaRestElement deelZaak = deelZaken.add(DEELZAAK);

      AnrFieldValue anrFieldValue = persoon.getAnummer();
      BsnFieldValue bsn = persoon.getBurgerServiceNummer();
      FieldValue gemHerkomst = persoon.getGemeenteHerkomst();
      boolean geenVerwerking = persoon.isGeenVerwerking();

      add(deelZaak, AANGIFTE, persoon.getAangifte().getCode(), persoon.getAangifte().getOms());
      add(deelZaak, BSN, bsn);
      add(deelZaak, ANR, anrFieldValue);
      add(deelZaak, GEEN_VERWERKING, geenVerwerking);

      if (zaak.getTypeVerhuizing().is(VerhuisType.INTERGEMEENTELIJK)) {
        add(deelZaak, GEMEENTE_HERKOMST, gemHerkomst);
      }
    }
  }
}
