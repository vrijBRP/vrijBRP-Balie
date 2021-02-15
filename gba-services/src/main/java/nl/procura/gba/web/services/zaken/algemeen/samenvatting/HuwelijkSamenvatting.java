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

import static nl.procura.gba.common.MiscUtils.setClass;
import static nl.procura.standard.Globalfunctions.*;

import nl.procura.burgerzaken.gba.core.enums.GBATable;
import nl.procura.diensten.gba.ple.openoffice.formats.Naamformats;
import nl.procura.gba.web.common.misc.Landelijk;
import nl.procura.gba.web.services.bs.algemeen.akte.DossierAkte;
import nl.procura.gba.web.services.bs.algemeen.functies.BsDossierNaamgebruikUtils;
import nl.procura.gba.web.services.bs.algemeen.functies.BsPersoonUtils;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.algemeen.vereiste.DossierVereiste;
import nl.procura.gba.web.services.bs.huwelijk.DossierHuwelijk;
import nl.procura.gba.web.services.bs.huwelijk.DossierHuwelijkOptie;
import nl.procura.gba.web.services.bs.huwelijk.StatusVerbintenis;
import nl.procura.gba.web.services.gba.basistabellen.huwelijkslocatie.HuwelijksLocatie;
import nl.procura.gba.web.services.zaken.algemeen.samenvatting.ZaakSamenvatting.Deelzaak;
import nl.procura.gba.web.services.zaken.algemeen.samenvatting.ZaakSamenvatting.Deelzaken;
import nl.procura.gba.web.services.zaken.algemeen.samenvatting.ZaakSamenvatting.ZaakItemRubriek;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

/**
 * Maakt samenvatting van gegevens over een specifieke aanvraag voor een huwelijk/GPS
 */
public class HuwelijkSamenvatting extends ZaakSamenvattingTemplate<DossierHuwelijk> {

  public HuwelijkSamenvatting(ZaakSamenvatting zaakSamenvatting, DossierHuwelijk zaak) {
    super(zaakSamenvatting, zaak);
  }

  @Override
  public void addDeelzaken(DossierHuwelijk zaak) {

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
  public void addZaakItems(DossierHuwelijk zaak) {
    addRubriek("Gegevens huwelijksdossier");
    setAktes(zaak.getDossier());
    setNaamgebruik(zaak);
    setPlanning(zaak);
    setBijzonderheden(zaak);
    setVereisten(zaak);
  }

  private String getDatumTijd(DossierHuwelijk zaak) {
    return zaak.getDatumVerbintenis().getFormatDate() + " vanaf " + zaak.getTijdVerbintenis().getFormatTime(
        "HH:mm");
  }

  private String getGetuigen(DossierHuwelijk zaak) {

    int count = zaak.getVolledigIngevuldeGetuigen().size();
    int correct = zaak.getHuwelijksLocatie().getLocatieSoort().getAantalGetuigenMin();

    switch (count) {

      case 0:
        return "Geen getuigen ingevuld";

      case 1:
        return count + " van de " + correct + " benodigde getuigen is ingevuld";

      default:
        return "Er zijn " + count + " getuigen ingevuld (min. " + correct + " is vereist";
    }
  }

  private String getLocatie(HuwelijksLocatie locatie) {
    return locatie.getHuwelijksLocatie() + " (" + locatie.getLocatieSoort().getOms() + ")";
  }

  private StringBuilder getOpties(DossierHuwelijk zaak) {
    StringBuilder opties = new StringBuilder();
    for (DossierHuwelijkOptie opt : zaak.getOpties()) {

      String waarde = opt.getWaarde();

      switch (opt.getOptie().getOptieType()) {
        case BOOLEAN:
          waarde = isTru(waarde) ? "Ja" : "Nee";
          break;

        default:
          break;
      }
      opties.append(opt.getOptie().getHuwelijksLocatieOptie());
      opties.append(" = ").append(waarde).append(", ");
    }
    return opties;
  }

  private String getSoort(DossierHuwelijk zaak) {
    return zaak.getSoortVerbintenis().getOms() + " voornemen op " + zaak.getDatumVoornemen().getFormatDate();
  }

  private String getStatus(DossierHuwelijk zaak) {
    String status = zaak.getStatusVerbintenis().getOms();
    if (zaak.getStatusVerbintenis() == StatusVerbintenis.OPTIE) {
      status = setClass(false, status + (" tot " + zaak.getEinddatumStatus().getFormatDate()));
    }
    return status;
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

  private void setBijzonderheden(DossierHuwelijk zaak) {
    ZaakItemRubriek rubriek = addRubriek("Bijzonderheden");
    rubriek.add("Getuigen", getGetuigen(zaak));
    rubriek.add("Gemeente getuigen", pos(zaak.getGemeenteGetuigen()) ? zaak.getGemeenteGetuigen() : "Geen");
  }

  private void setNaamgebruik(DossierHuwelijk zaak) {

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

  private void setPlanning(DossierHuwelijk zaak) {

    HuwelijksLocatie locatie = zaak.getHuwelijksLocatie();
    ZaakItemRubriek rubriek;
    rubriek = addRubriek("Planning");
    rubriek.add("Soort", getSoort(zaak));
    rubriek.add("Locatie", getLocatie(locatie));
    rubriek.add("Datum / tijd", getDatumTijd(zaak));
    rubriek.add("Status", getStatus(zaak));
    rubriek.add("Opties", trim(getOpties(zaak).toString()));
    rubriek.add("Toelichting", zaak.getToelichtingVerbintenis());
  }

  private void setVereisten(DossierHuwelijk zaak) {

    addRubriek("Vereisten");

    for (DossierVereiste v : zaak.getDossier().getVereisten()) {

      if (v.isHeeftVoldaan() && !v.isOverruled()) {
        continue;
      }

      ZaakItemRubriek rubriek = addRubriek(v.getDossierVereiste() + " - " + v.getNaam());

      String overruled = v.isOverruled() ? " (overruled)" : "";
      String voldaan = (v.isHeeftVoldaan() ? "Ja " : "Nee ") + overruled;
      rubriek.add("Voldaan", voldaan);
      rubriek.add("Toelichting", v.getToelichting());
    }
  }
}
