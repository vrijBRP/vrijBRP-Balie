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

package nl.procura.gba.web.services.zaken.algemeen.samenvatting;

import static nl.procura.gba.web.common.tables.GbaTables.LAND;
import static nl.procura.gba.web.services.bs.algemeen.functies.BsDossierNaamgebruikUtils.getNormalizedNameWithAge;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import nl.procura.diensten.gba.ple.openoffice.formats.Adresformats;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.registration.*;
import nl.procura.gba.web.services.bs.registration.relations.Relation;
import nl.procura.gba.web.services.gba.ple.relatieLijst.Relatie;
import nl.procura.gba.web.services.zaken.algemeen.consent.ConsentProvider;
import nl.procura.gba.web.services.zaken.algemeen.samenvatting.ZaakSamenvatting.ZaakItemRubriek;
import nl.procura.gba.web.services.zaken.verhuizing.FunctieAdres;

/**
 * Maakt samenvatting van gegevens over een specifieke aanvraag voor een eerste inschrijving
 */
public class RegistrationSamenvatting extends ZaakSamenvattingTemplate<DossierRegistration> {

  public RegistrationSamenvatting(ZaakSamenvatting zaakSamenvatting, DossierRegistration zaak) {
    super(zaakSamenvatting, zaak);
  }

  @Override
  public void addDeelzaken(DossierRegistration zaak) {

    ZaakSamenvatting.Deelzaken deelZaken = addDeelzaken("Inschrijvers");

    List<DossierPersoon> persons = zaak.getDossier().getPersonen(DossierPersoonType.INSCHRIJVER);
    for (DossierPersoon dp : persons) {
      ZaakSamenvatting.Deelzaak deelZaak = new ZaakSamenvatting.Deelzaak();
      deelZaak.add("Naam", getNormalizedNameWithAge(dp));
      deelZaak.add("BSN", dp.getBurgerServiceNummer());
      deelZaken.add(deelZaak);
    }

    addRelations(zaak);
  }

  @Override
  public void addZaakItems(DossierRegistration zaak) {
    ZaakItemRubriek rubriek = addRubriek("Gegevens eerste inschrijving");
    rubriek.add("Herkomstsituatie", OriginSituationType.valueOfCode(zaak.getOriginSituation()));
    rubriek.add("Land van herkomst", LAND.get(zaak.getDepartureCountry().getStringValue()));
    rubriek.add("Adres", getAddress(zaak).getAdres_pc_wpl_gem());
    rubriek.add("Functieadres", FunctieAdres.get(zaak.getAddressFunction()));
    rubriek.add("Duur", StaydurationType.valueOfCode(zaak.getDuration()));
    rubriek.add("Toestemming", getConsent(zaak));
    rubriek.add("Aantal personen", zaak.getNoOfPeople());
    rubriek.add("Aangever", getDeclarant(zaak));
    if (StringUtils.isNotBlank(zaak.getDeclarantComment())) {
      rubriek.add("Toelichting aangever", zaak.getDeclarantComment());
    }
  }

  private void addRelations(DossierRegistration zaak) {

    RelationService service = Services.getInstance().getRelationService();
    List<DossierPersoon> people = zaak.getDossier().getPersonen();
    List<Relation> relations = Relation.fromDossPersRelations(service.findByPeople(people), people);

    ZaakItemRubriek rubriek = addRubriek("Relaties");
    int nr = 0;
    for (Relation r : relations) {
      nr++;
      String p1 = getNormalizedNameWithAge(r.getPerson());
      String p2 = getNormalizedNameWithAge(r.getRelatedPerson());
      String relation = r.getRelationType().getOms().toLowerCase();
      if (r.getRelationType().isRelated()) {
        rubriek.add("Relatie " + nr, p1 + " " + relation + " " + p2);
      } else {
        rubriek.add("Relatie " + nr, p1 + " " + relation);
      }
    }
  }

  private String getDeclarant(DossierRegistration zaak) {
    StringBuilder out = new StringBuilder();
    out.append(DeclarantType.valueOfCode(zaak.getDeclarant().getCode()).toString());
    if (zaak.getDeclarant().getPerson().isPresent()) {
      out.append(": ");
      out.append(zaak.getDeclarant().getPerson().get().getNaam().getPred_eerstevoorn_adel_voorv_gesl());
    }
    return out.toString();
  }

  private String getConsent(DossierRegistration zaak) {
    String out = "Niet aangegeven";
    ConsentProvider consent = zaak.getConsent();
    Relatie relation = consent.getBrpConsentProvider();
    if (relation != null) {
      out = relation.getPl().getPersoon().getNaam().getNaamNaamgebruikEersteVoornaam();
    } else if (StringUtils.isNotBlank(consent.getOtherConsentProvider())) {
      out = consent.getOtherConsentProvider();
    }
    return out;
  }

  private Adresformats getAddress(DossierRegistration zaak) {
    String straat = zaak.getStreet();
    String hnr = zaak.getHouseNumber().toString();
    String hnrL = zaak.getHouseNumberL();
    String hnrT = zaak.getHouseNumberT();
    String hnrA = zaak.getHouseNumberA();
    String pc = zaak.getPostalCode();
    String wpl = zaak.getResidence();

    return new Adresformats().setValues(straat, hnr, hnrL, hnrT, hnrA, "", pc, "", wpl, "", "", "", "", "", "", "");
  }
}
