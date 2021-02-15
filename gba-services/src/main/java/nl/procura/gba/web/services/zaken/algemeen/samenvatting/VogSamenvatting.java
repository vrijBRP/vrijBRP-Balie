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

import static nl.procura.standard.Globalfunctions.*;

import nl.procura.diensten.gba.ple.openoffice.formats.Adresformats;
import nl.procura.diensten.gba.ple.openoffice.formats.Geboorteformats;
import nl.procura.diensten.gba.ple.openoffice.formats.Naamformats;
import nl.procura.gba.web.services.zaken.algemeen.samenvatting.ZaakSamenvatting.ZaakItemRubriek;
import nl.procura.gba.web.services.zaken.vog.*;

/**
 * Maakt samenvatting van gegevens over een specifieke vog aanvraag
 */
public class VogSamenvatting extends ZaakSamenvattingTemplate<VogAanvraag> {

  public VogSamenvatting(ZaakSamenvatting zaakSamenvatting, VogAanvraag zaak) {
    super(zaakSamenvatting, zaak);
  }

  @Override
  public void addDeelzaken(VogAanvraag z) {
  }

  @Override
  public void addZaakItems(VogAanvraag z) {

    ZaakItemRubriek rubriek;
    rubriek = addRubriek("Nummers");
    rubriek.add("COVOG id", z.getVogNummer().getCOVOGNummerFormatted());
    rubriek.add("Aanvraagnr.", pos(z.getAanvraagId()) ? z.getAanvraagId() : "Wordt bepaald na verzending");

    setAanvrager(z);
    setBelanghebbende(z);
    setDoel(z);
    setScreening(z);
    setOpmerkingen(z);
  }

  public String getFunctiegebiedenSamenvatting(VogAanvraagScreening s) {
    StringBuilder sb = new StringBuilder();
    for (VogFunctie fg : s.getFunctiegebieden()) {
      sb.append(fg.getVogFuncTab());
      sb.append(" (");
      sb.append(fg.getOms());
      sb.append(")\n");
    }

    return trim(sb.toString());
  }

  private String opm(String s) {
    return fil(s) ? ("Ja - " + s) : "Nee";
  }

  private void setAanvrager(VogAanvraag z) {
    ZaakItemRubriek rubriek = addRubriek("Aanvrager");
    VogAanvrager a = z.getAanvrager();
    Naamformats nf = new Naamformats(a.getVoornamen(), a.getGeslachtsnaam(), a.getVoorvoegsel(), "", "", null);

    Geboorteformats g = new Geboorteformats();
    g.setValues(a.getDatumGeboorte().getFormatDate(), a.getPlaatsGeboren().getDescription(),
        a.getLandGeboren().getDescription());

    Adresformats af = new Adresformats();
    af.setValues(a.getStraat(), astr(a.getHnr()), a.getHnrL(), a.getHnrT(), "", "", "", "", "", "", "", "", "", "",
        "", "");

    rubriek.add("Naam", nf.getNaam_naamgebruik_eerste_voornaam());
    rubriek.add("BSN", a.getBurgerServiceNummer().getDescription());
    rubriek.add("Geboren", g.getDatum_te_plaats_land());
    rubriek.add("Aanschrijfnaam", a.getAanschrijf());
    rubriek.add("Adres", af.getAdres());
    rubriek.add("Postcode", a.getPc().getDescription());
    rubriek.add("Plaats / land", "{0} / {1}", a.getPlaats(), a.getLand().getDescription());
  }

  private void setBelanghebbende(VogAanvraag z) {

    ZaakItemRubriek rubriek = addRubriek("Belanghebbende");
    VogAanvraagBelanghebbende a = z.getBelanghebbende();

    rubriek.add("Naam", a.getNaam());
    rubriek.add("Vertegenwoordiger", a.getVertegenwoordiger());

    Adresformats af = new Adresformats();
    af.setValues(a.getStraat(), astr(a.getHnr()), a.getHnrL(), a.getHnrT(), "", "", "", "", "", "", "", "", "", "",
        "", "");

    rubriek.add("Adres", af.getAdres());
    rubriek.add("Postcode / plaats", a.getPc() + " " + a.getPlaats());
    rubriek.add("Land", a.getLand().getDescription());
    rubriek.add("Telefoonnummer", a.getTel());
  }

  private void setDoel(VogAanvraag z) {

    ZaakItemRubriek rubriek = addRubriek("Doel");
    VogAanvraagDoel d = z.getDoel();
    rubriek.add("Doel", astr(d.getDoel().getCVogDoelTab()) + " - " + astr(d.getDoel().getOms()));
    rubriek.add("Functie", d.getFunctie());
    rubriek.add("Omschrijving", d.getDoelTekst());
  }

  private void setOpmerkingen(VogAanvraag z) {

    ZaakItemRubriek rubriek = addRubriek("Opmerkingen");

    VogAanvraagOpmerkingen o = z.getOpmerkingen();
    rubriek.add("Advies", o.getBurgemeesterAdvies().getOms());
    rubriek.add("Bijzonderheden", o.getByzonderhedenTekst());
    rubriek.add("Covogadvies", o.getCovogAdviesTekst());
    rubriek.add("Persisteren", o.getPersisterenTekst());
    rubriek.add("Toelichting", o.getToelichtingTekst());
  }

  private void setScreening(VogAanvraag z) {

    ZaakItemRubriek rubriek = addRubriek("Screening");

    VogAanvraagScreening s = z.getScreening();

    if (s.getProfiel() != null && pos(s.getProfiel().getVogProfTab())) {
      rubriek.add("Profiel", s.getProfiel().getVogProfTab() + " - " + s.getProfiel().getOms());
    } else {
      rubriek.add("Profiel", "Niet van toepassing");
    }

    rubriek.add("Omstandigheden", opm(s.getOmstandighedenTekst()));

    String screenbasis = "";
    if (s.isGebruikProfiel()) {
      screenbasis = "Op basis van een screeningsprofiel";
    }

    if (s.isGebruikFunctiegebied()) {
      screenbasis = "Op basis van functiegebieden";
    }

    rubriek.add("Screening", screenbasis);

    if (pos(s.getFunctiegebieden().size())) {
      rubriek.add("Functiegebieden", getFunctiegebiedenSamenvatting(s));
    } else {
      rubriek.add("Functiegebieden", "Niet van toepassing");
    }
  }
}
