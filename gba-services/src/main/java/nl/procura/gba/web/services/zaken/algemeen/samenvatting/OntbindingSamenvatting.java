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

import static nl.procura.gba.web.services.bs.algemeen.enums.SoortVerbintenis.HUWELIJK;
import static nl.procura.gba.web.services.bs.ontbinding.WijzeBeeindigingVerbintenis.RECHTERLIJKE_UITSPRAAK;
import static nl.procura.standard.Globalfunctions.*;

import nl.procura.burgerzaken.gba.core.enums.GBATable;
import nl.procura.diensten.gba.ple.openoffice.formats.Naamformats;
import nl.procura.gba.web.common.misc.Landelijk;
import nl.procura.gba.web.services.bs.algemeen.akte.DossierAkte;
import nl.procura.gba.web.services.bs.algemeen.enums.SoortVerbintenis;
import nl.procura.gba.web.services.bs.algemeen.functies.BsDossierNaamgebruikUtils;
import nl.procura.gba.web.services.bs.algemeen.functies.BsPersoonUtils;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.ontbinding.DossierOntbinding;
import nl.procura.gba.web.services.zaken.algemeen.samenvatting.ZaakSamenvatting.Deelzaak;
import nl.procura.gba.web.services.zaken.algemeen.samenvatting.ZaakSamenvatting.Deelzaken;
import nl.procura.gba.web.services.zaken.algemeen.samenvatting.ZaakSamenvatting.ZaakItemRubriek;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

/**
 * Maakt samenvatting van gegevens over een specifieke aanvraag voor een ontbinding/einde huwelijk/gps
 */
public class OntbindingSamenvatting extends ZaakSamenvattingTemplate<DossierOntbinding> {

  public OntbindingSamenvatting(ZaakSamenvatting zaakSamenvatting, DossierOntbinding zaak) {
    super(zaakSamenvatting, zaak);
  }

  @Override
  public void addDeelzaken(DossierOntbinding zaak) {

    Deelzaken deelZaken = addDeelzaken(zaak.getDossier().getPersonen().size());
    for (DossierAkte akte : zaak.getDossier().getAktes()) {
      for (DossierPersoon person : BsPersoonUtils.sort(akte.getPersonen())) {

        String naam = person.getNaam().getPred_adel_voorv_gesl_voorn();
        String adres = person.getWoongemeente().getDescription();

        if (person.isRNI()) {
          adres = "RNI";
        } else if (!Landelijk.isNederland(person.getLand())) {
          adres = person.getLand().getDescription();
        }

        Deelzaak deelZaak = new Deelzaak();
        deelZaak.add("Persoon",
            (fil(naam) ? naam : "Nog niet aangegeven") + " (" + person.getDossierPersoonType() + ")");
        deelZaak.add("BSN", person.getBurgerServiceNummer().getDescription());
        deelZaak.add("Ingeschreven", (fil(adres) ? adres : "-") + "\n");
        deelZaken.add(deelZaak);
      }
    }
  }

  @Override
  public void addZaakItems(DossierOntbinding zaak) {
    addRubriek("Gegevens ontbinding/einde huwelijk/GPS");
    setSluitingsgegevens(zaak);
    setWijzeBeeindiging(zaak);
    setBrondocument(zaak);
    setAktes(zaak.getDossier());
    setNaamgebruik(zaak);
  }

  private String getTitelOmschrijving(String tp) {
    StringBuilder out = new StringBuilder(tp);
    if (BsDossierNaamgebruikUtils.isPredikaat(new FieldValue(tp))) {
      out.append(" (predikaat)");
    }
    if (BsDossierNaamgebruikUtils.isAdel(new FieldValue(tp))) {
      out.append(" (adelijke titel)");
    }
    return out.toString();
  }

  private void setBrondocument(DossierOntbinding zaak) {

    boolean isHuwelijk = HUWELIJK == zaak.getSoortVerbintenis();
    boolean isRechterlijkeUitspraak = RECHTERLIJKE_UITSPRAAK == zaak.getWijzeBeeindigingVerbintenis();
    ZaakItemRubriek rubriek = addRubriek("Brondocument - gegevens ontvangen document(en)");

    if (isHuwelijk || isRechterlijkeUitspraak) {

      rubriek.add("Uitspraak door", zaak.getUitspraakDoor());
      rubriek.add("Datum kracht van gewijsde", zaak.getDatumGewijsde());
      rubriek.add("Verzoek tot inschrijving door", zaak.getVerzoekTotInschrijvingDoor());
      rubriek.add("Verzoek ontvangen op", zaak.getDatumVerzoek());
      rubriek.add("Binnen termijn", zaak.isBinnenTermijn() ? "Ja" : "Nee");
    } else {
      rubriek.add("Verklaring ontvangen op", zaak.getDatumVerklaring());
      rubriek.add("Ondertekend door partijen en", zaak.getOndertekendDoor());
      rubriek.add("Ondertekend op", zaak.getDatumOndertekening());
    }
  }

  private void setNaamgebruik(DossierOntbinding zaak) {

    String tp1 = astr(zaak.getTitelPartner1());
    String naam1 = zaak.getNaamPartner1();
    String voorv1 = zaak.getVoorvPartner1();
    String naamgebruik1 = zaak.getNaamGebruikPartner1();

    String tp2 = astr(zaak.getTitelPartner2());
    String naam2 = zaak.getNaamPartner2();
    String voorv2 = zaak.getVoorvPartner2();
    String naamgebruik2 = zaak.getNaamGebruikPartner2();

    setNaamgebruik("1", tp1, naam1, voorv1, naamgebruik1);
    setNaamgebruik("2", tp2, naam2, voorv2, naamgebruik2);
  }

  private void setNaamgebruik(String partner, String tp, String naam, String voorv, String naamgebruik) {
    Naamformats nf = new Naamformats("", naam, voorv, tp, naamgebruik, null);
    String gewijzigdTekst = "gewijzigd in " + naamgebruik + " (" + GBATable.AAND_NAAMGEBRUIK.get(
        naamgebruik).getValue() + ")";

    ZaakItemRubriek rubriek = addRubriek("Naam(gebruik) partner " + partner);
    rubriek.add("Titel / predikaat na sluiting", getTitelOmschrijving(tp));
    rubriek.add("Naam na sluiting", trim(nf.getTitel_voorv_gesl()));
    rubriek.add("Naamgebruik na sluiting", emp(naamgebruik) ? "Niet gewijzigd" : gewijzigdTekst);
  }

  private void setSluitingsgegevens(DossierOntbinding zaak) {

    StringBuilder sluiting = new StringBuilder();
    sluiting.append(zaak.getSoortVerbintenis());
    sluiting.append(": ");
    sluiting.append(zaak.getDatumVerbintenis());
    sluiting.append(" ");
    sluiting.append(zaak.getPlaatsVerbintenis());
    sluiting.append(",");
    sluiting.append(zaak.getLandVerbintenis());

    StringBuilder akte = new StringBuilder();
    akte.append(zaak.getBsAkteNummerVerbintenis());
    akte.append(", ");
    akte.append(zaak.getAktePlaatsVerbintenis());
    akte.append(", ");
    akte.append(zaak.getAkteJaarVerbintenis());

    ZaakItemRubriek rubriek = addRubriek("Huidige sluitingsgegevens");
    rubriek.add("Huwelijk/GPS sluitingsgegevens", sluiting.toString());
    rubriek.add("Huwelijk/GPS aktegegevens", akte.toString());
  }

  private void setWijzeBeeindiging(DossierOntbinding zaak) {
    if (SoortVerbintenis.GPS.equals(zaak.getSoortVerbintenis())) {
      ZaakItemRubriek rubriek = addRubriek("Brondocument - gegevens ontvangen document(en)");
      rubriek.add("Brondocument - wijze van beëindiging", zaak.getWijzeBeeindigingVerbintenis().getOms());
    }
  }
}
