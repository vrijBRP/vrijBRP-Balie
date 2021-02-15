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

package nl.procura.gba.web.rest.v1_0.persoon;

import static nl.procura.standard.Globalfunctions.pos;

import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.base.BasePLValue;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.extensions.Cat5HuwExt;
import nl.procura.diensten.gba.ple.extensions.formats.Adres;
import nl.procura.diensten.gba.ple.extensions.formats.Geboorte;
import nl.procura.diensten.gba.ple.extensions.formats.Naam;
import nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElement;
import nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType;
import nl.procura.gba.web.rest.v1_0.zaak.GbaRestElementHandler;
import nl.procura.gba.web.services.Services;
import nl.procura.vaadin.component.field.fieldvalues.DateFieldValue;

public class GbaRestPersoonslijstHandler1 extends GbaRestElementHandler {

  public GbaRestPersoonslijstHandler1(Services services) {
    super(services);
  }

  public void getPersoon(BasePLExt persoonslijst, GbaRestElement persoon) {

    addIdentificatie(persoonslijst, persoon);

    add(persoon, GbaRestElementType.GESLACHTSAANDUIDING, persoonslijst.getPersoon().getGeslacht());

    addNaam(persoonslijst.getPersoon().getNaam(), persoon);
    addGeboorte(persoonslijst.getPersoon().getGeboorte(), persoon);
    addPartner(persoonslijst.getHuwelijk(), persoon);
    addVerblijfplaats(persoonslijst, persoon);
  }

  private void addGeboorte(Geboorte geboorte, GbaRestElement persoon) {

    GbaRestElement gebElement = persoon.add(GbaRestElementType.GEBOORTE);
    add(gebElement, GbaRestElementType.DATUM_GEBOORTE, new DateFieldValue(geboorte.getDatum()));
    add(gebElement, GbaRestElementType.PLAATS, geboorte.getGeboorteplaats());
    add(gebElement, GbaRestElementType.LAND, geboorte.getGeboorteland());
  }

  private void addIdentificatie(BasePLExt persoonslijst, GbaRestElement persoon) {
    GbaRestElement element = persoon.add(GbaRestElementType.IDENTIFICATIE);

    BasePLValue bsn = persoonslijst.getPersoon().getBsn();
    BasePLValue anr = persoonslijst.getPersoon().getAnr();

    add(element, GbaRestElementType.BSN, bsn);
    add(element, GbaRestElementType.ANR, anr);
  }

  private void addNaam(Naam naam, GbaRestElement persoon) {

    GbaRestElement element = persoon.add(GbaRestElementType.NAAM);

    add(element, GbaRestElementType.VOORNAMEN, naam.getVoornamen());
    add(element, GbaRestElementType.INITIALEN, naam.getInitialen());
    add(element, GbaRestElementType.VOORVOEGSEL, naam.getVoorvoegsel());
    add(element, GbaRestElementType.GESLACHTSNAAM, naam.getGeslachtsnaam());
    add(element, GbaRestElementType.TITEL_PREDIKAAT, naam.getAdeltitel());
    add(element, GbaRestElementType.NAAMGEBRUIK, naam.getNaamgebruik());
  }

  private void addPartner(Cat5HuwExt huwelijk, GbaRestElement persoon) {
    if (pos(huwelijk.getNummer().getVal())) {
      BasePLRec huwelijkSet = huwelijk.getHuwelijkSet().getLatestRec();
      if (huwelijkSet != null && huwelijk.isActueelOfMutatieRecord()) {
        GbaRestElement partnerElement = persoon.add(GbaRestElementType.HUWELIJK);
        add(partnerElement, GbaRestElementType.SOORT, huwelijkSet.getElem(GBAElem.SOORT_VERBINTENIS));
        add(partnerElement, GbaRestElementType.BSN, huwelijk.getBsn());
        add(partnerElement, GbaRestElementType.ANR, huwelijk.getAnr());
      }
    }
  }

  private void addVerblijfplaats(BasePLExt persoonslijst, GbaRestElement persoon) {

    GbaRestElement element = persoon.add(GbaRestElementType.VERBLIJFSADRES);
    Adres adres = persoonslijst.getVerblijfplaats().getAdres();

    add(element, GbaRestElementType.STRAAT, adres.getStraat());
    add(element, GbaRestElementType.HNR, adres.getHuisnummer());
    add(element, GbaRestElementType.HNR_L, adres.getHuisletter());
    add(element, GbaRestElementType.HNR_A, adres.getHuisnummeraand());
    add(element, GbaRestElementType.HNR_T, adres.getHuisnummertoev());
    add(element, GbaRestElementType.POSTCODE, adres.getPostcode());
    add(element, GbaRestElementType.WOONPLAATS, adres.getWoonplaats());
  }
}
