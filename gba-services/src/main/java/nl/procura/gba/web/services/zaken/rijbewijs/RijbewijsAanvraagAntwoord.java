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

package nl.procura.gba.web.services.zaken.rijbewijs;

import static ch.lambdaj.Lambda.join;
import static java.util.Arrays.asList;
import static nl.procura.standard.Globalfunctions.*;

import java.util.List;

import nl.procura.diensten.gba.ple.openoffice.DocumentPL;
import nl.procura.diensten.gba.ple.openoffice.formats.Adresformats;
import nl.procura.diensten.gba.ple.openoffice.formats.Naamformats;
import nl.procura.gba.web.services.beheer.locatie.Locatie;
import nl.procura.standard.ProcuraDate;

public class RijbewijsAanvraagAntwoord {

  private Locatie        locatieInvoer  = new Locatie();
  private Locatie        locatieAfhaal  = new Locatie();
  private String         ondertekening  = "";
  private Aanvrager      aanvrager      = new Aanvrager();
  private Rijb_gegevens  rijb_gegevens  = new Rijb_gegevens();
  private Pers_gegevens  pers_gegevens  = new Pers_gegevens();
  private Adr_gegevens   adr_gegevens   = new Adr_gegevens();
  private Aanvr_gegevens aanvr_gegevens = new Aanvr_gegevens();
  private Stat_gegevens  stat_gegevens  = new Stat_gegevens();
  private Cat_gegevens[] cat_gegevens   = new Cat_gegevens[0];

  public Aanvr_gegevens getAanvr_gegevens() {
    return aanvr_gegevens;
  }

  public void setAanvr_gegevens(Aanvr_gegevens aanvr_gegevens) {
    this.aanvr_gegevens = aanvr_gegevens;
  }

  public Aanvrager getAanvrager() {
    return aanvrager;
  }

  public void setAanvrager(Aanvrager aanvrager) {
    this.aanvrager = aanvrager;
  }

  public Adr_gegevens getAdr_gegevens() {
    return adr_gegevens;
  }

  public void setAdr_gegevens(Adr_gegevens adr_gegevens) {
    this.adr_gegevens = adr_gegevens;
  }

  public Cat_gegevens[] getCat_gegevens() {
    return cat_gegevens;
  }

  public void setCat_gegevens(Cat_gegevens[] cat_gegevens) {
    this.cat_gegevens = cat_gegevens;
  }

  public Locatie getLocatieAfhaal() {
    return locatieAfhaal;
  }

  public void setLocatieAfhaal(Locatie locatie) {
    this.locatieAfhaal = locatie;
  }

  public Locatie getLocatieInvoer() {
    return locatieInvoer;
  }

  public void setLocatieInvoer(Locatie locatie) {
    this.locatieInvoer = locatie;
  }

  public String getOndertekening() {
    return ondertekening;
  }

  public void setOndertekening(String ondertekening) {
    this.ondertekening = ondertekening;
  }

  public Pers_gegevens getPers_gegevens() {
    return pers_gegevens;
  }

  public void setPers_gegevens(Pers_gegevens pers_gegevens) {
    this.pers_gegevens = pers_gegevens;
  }

  public Rijb_gegevens getRijb_gegevens() {
    return rijb_gegevens;
  }

  public void setRijb_gegevens(Rijb_gegevens rijb_gegevens) {
    this.rijb_gegevens = rijb_gegevens;
  }

  public Stat_gegevens getStat_gegevens() {
    return stat_gegevens;
  }

  public void setStat_gegevens(Stat_gegevens stat_gegevens) {
    this.stat_gegevens = stat_gegevens;
  }

  public Titel getTitel() {

    Titel titel = new Titel();

    switch (getAanvr_gegevens().getSoort()) {

      case 1:
        titel.setTitel1("Eerste aanvraag rijbewijs");
        titel.setTitel3("3 E 0390a");
        break;

      case 2:
        titel.setTitel1("Aanvraag rijbewijs");
        titel.setTitel2("uitbreiding categorie(ën)");
        titel.setTitel3("3 E 0391a");
        break;

      case 3:
        titel.setTitel1("Aanvraag rijbewijs wegens");
        titel.setTitel3("3 E 0391a");

        switch (getAanvr_gegevens().getReden()) {
          case 1:
            titel.setTitel2("vermissing, diefstal of vernieuwing");
            titel.setTitel3("3 E 0393a");
            break;

          case 2:
            titel.setTitel2("beschadiging of onleesbaarheid");
            titel.setTitel3("3 E 0392a");
            break;

          case 3:
            titel.setTitel2("vermissing, diefstal of vernieuwing");
            titel.setTitel3("3 E 0393a");
            break;

          case 4:
            titel.setTitel2("vermissing, diefstal of vernieuwing");
            titel.setTitel3("3 E 0393a");
            break;

          default:
            break;
        }

        break;

      case 4:
        titel.setTitel1("Aanvraag rijbewijs wegens");

        switch (getAanvr_gegevens().getReden()) {
          case 1:
            titel.setTitel2("vermissing, diefstal of vernieuwing");
            titel.setTitel3("3 E 0393a");
            break;

          case 2:
            titel.setTitel2("beschadiging of onleesbaarheid");
            titel.setTitel3("3 E 0392a");
            break;

          case 3:
            titel.setTitel2("");
            titel.setTitel3("");
            break;

          case 4:
            titel.setTitel2("");
            titel.setTitel3("");
            break;

          default:
            break;
        }

        break;

      case 5:
        titel.setTitel1("Aanvraag rijbewijs");
        titel.setTitel2("na ongeldigverklaring");
        titel.setTitel3("3 E 0396a");
        break;

      default:
        titel.setTitel1("Aanvraag omwisseling");
        titel.setTitel2("voor Nederlands rijbewijs");
        titel.setTitel3("3 E 0397a");
    }

    return titel;
  }

  public class Aanvr_gegevens {

    private String aanvraag_nr   = "";
    private String autoriteit    = "";
    private String bestendig     = "";
    private String collo_nr      = "";
    private String datum         = "";
    private String loc           = "";
    private int    reden         = 0;
    private int    soort         = 0;
    private String spoed         = "";
    private String tijd          = "";
    private String vervangt_rijb = "";

    public String getAanvraag_nr() {
      return aanvraag_nr;
    }

    public void setAanvraag_nr(String aanvraag_nr) {
      this.aanvraag_nr = aanvraag_nr;
    }

    public String getAutoriteit() {
      return autoriteit;
    }

    public void setAutoriteit(String autoriteit) {
      this.autoriteit = autoriteit;
    }

    public String getBestendig() {
      return bestendig;
    }

    public void setBestendig(String bestendig) {
      this.bestendig = bestendig;
    }

    public String getCollo_nr() {
      return collo_nr;
    }

    public void setCollo_nr(String collo_nr) {
      this.collo_nr = collo_nr;
    }

    public String getDatum() {
      return datum;
    }

    public void setDatum(String datum) {
      this.datum = datum;
    }

    public String getLoc() {
      return loc;
    }

    public void setLoc(String loc) {
      this.loc = loc;
    }

    public int getReden() {
      return reden;
    }

    public void setReden(int reden) {
      this.reden = reden;
    }

    public int getSoort() {
      return soort;
    }

    public void setSoort(int soort) {
      this.soort = soort;
    }

    public String getSpoed() {
      return spoed;
    }

    public void setSpoed(String spoed) {
      this.spoed = spoed;
    }

    public String getTijd() {
      return tijd;
    }

    public void setTijd(String tijd) {
      this.tijd = tijd;
    }

    public String getVervangt_rijb() {
      return vervangt_rijb;
    }

    public void setVervangt_rijb(String vervangt_rijb) {
      this.vervangt_rijb = vervangt_rijb;
    }

    public boolean isBijzondereSoort() {
      return getSoort() >= RijbewijsStatusType.GEREGISTREERD.getCode();
    }
  }

  public class Aanvrager {

    private DocumentPL persoon             = null;
    private long       code_land_vertrek   = -1;
    private long       code_land_vestiging = -1;
    private long       code_verblijfstatus = -1;
    private long       datum_vertrek       = 0;
    private long       datum_vestiging     = 0;
    private String     email               = "";
    private String     land_vertrek        = "";
    private String     land_vestiging      = "";
    private String     nationaliteiten     = "";
    private String     soortIDBewijs       = "";
    private String     telnr_mob           = "";
    private String     telnr_thuis         = "";
    private String     telnr_werk          = "";
    private String     verblijfstatus      = "";

    public long getCode_land_vertrek() {
      return code_land_vertrek;
    }

    public void setCode_land_vertrek(long codeLandVertrek) {
      code_land_vertrek = codeLandVertrek;
    }

    public long getCode_land_vestiging() {
      return code_land_vestiging;
    }

    public void setCode_land_vestiging(long codeLandVestiging) {
      code_land_vestiging = codeLandVestiging;
    }

    public long getCode_verblijfstatus() {
      return code_verblijfstatus;
    }

    public void setCode_verblijfstatus(long codeVerblijfstatus) {
      code_verblijfstatus = codeVerblijfstatus;
    }

    public long getDatum_vertrek() {
      return datum_vertrek;
    }

    public void setDatum_vertrek(long datumVertrek) {
      datum_vertrek = datumVertrek;
    }

    public long getDatum_vestiging() {
      return datum_vestiging;
    }

    public void setDatum_vestiging(long datumVestiging) {
      datum_vestiging = datumVestiging;
    }

    public String getEmail() {
      return email;
    }

    public void setEmail(String email) {
      this.email = email;
    }

    public Formats getFormats() {
      return new Formats();
    }

    public String getLand_vertrek() {
      return land_vertrek;
    }

    public void setLand_vertrek(String land_vertrek) {
      this.land_vertrek = land_vertrek;
    }

    public String getLand_vestiging() {
      return land_vestiging;
    }

    public void setLand_vestiging(String land_vestiging) {
      this.land_vestiging = land_vestiging;
    }

    public String getNationaliteiten() {
      return nationaliteiten;
    }

    public void setNationaliteiten(String nationaliteiten) {
      this.nationaliteiten = nationaliteiten;
    }

    public DocumentPL getPersoon() {
      return persoon;
    }

    public void setPersoon(DocumentPL persoon) {
      this.persoon = persoon;
    }

    public String getSoortIDBewijs() {
      return soortIDBewijs;
    }

    public void setSoortIDBewijs(String soortIDBewijs) {
      this.soortIDBewijs = soortIDBewijs;
    }

    public String getTelnr_mob() {
      return telnr_mob;
    }

    public void setTelnr_mob(String telnr_mob) {
      this.telnr_mob = telnr_mob;
    }

    public String getTelnr_thuis() {
      return telnr_thuis;
    }

    public void setTelnr_thuis(String telnr_thuis) {
      this.telnr_thuis = telnr_thuis;
    }

    public String getTelnr_werk() {
      return telnr_werk;
    }

    public void setTelnr_werk(String telnr_werk) {
      this.telnr_werk = telnr_werk;
    }

    public String getVerblijfstatus() {
      return fil(verblijfstatus) ? verblijfstatus : "0 (Geen)";
    }

    public void setVerblijfstatus(String verblijfstatus) {
      this.verblijfstatus = verblijfstatus;
    }

    public class Formats {

      public String getDatumVertrek() {

        return new ProcuraDate(astr(getDatum_vertrek())).getFormatDate();
      }

      public String getDatumVestiging() {

        return new ProcuraDate(astr(getDatum_vestiging())).getFormatDate();
      }
    }
  }

  public class Adr_gegevens {

    private String  adres          = "";
    private String  huisnummer     = "";
    private String  huistoevoeging = "";
    private String  land           = "";
    private String  locatie        = "";
    private String  postcode       = "";
    private String  straat         = "";
    private String  woonplaats     = "";
    private String  woonw          = "";
    private Formats formats        = new Formats();

    public String getAdres() {
      return adres;
    }

    public void setAdres(String adres) {
      this.adres = adres;
    }

    public Formats getFormats() {
      return formats;
    }

    public void setFormats(Formats formats) {
      this.formats = formats;
    }

    public String getHuisnummer() {
      return huisnummer;
    }

    public void setHuisnummer(String huisnummer) {
      this.huisnummer = huisnummer;
    }

    public String getHuistoevoeging() {
      return huistoevoeging;
    }

    public void setHuistoevoeging(String huistoevoeging) {
      this.huistoevoeging = huistoevoeging;
    }

    public String getLand() {
      return land;
    }

    public void setLand(String land) {
      this.land = land;
    }

    public String getLocatie() {
      return locatie;
    }

    public void setLocatie(String locatie) {
      this.locatie = locatie;
    }

    public String getPostcode() {
      return postcode;
    }

    public void setPostcode(String postcode) {
      this.postcode = postcode;
    }

    public String getStraat() {
      return straat;
    }

    public void setStraat(String straat) {
      this.straat = straat;
    }

    public String getWoonplaats() {
      return woonplaats;
    }

    public void setWoonplaats(String woonplaats) {
      this.woonplaats = woonplaats;
    }

    public String getWoonw() {
      return woonw;
    }

    public void setWoonw(String woonw) {
      this.woonw = woonw;
    }

    public void loadFormats() {

      getFormats().getAdres()
          .setValues(getStraat(), getHuisnummer(), "", getHuistoevoeging(), "", getLocatie(), getPostcode(),
              "", getWoonplaats(), "0", "", "", "", "", "", "");
    }

    public class Formats {

      private Adresformats adres = new Adresformats();

      public Adresformats getAdres() {
        return adres;
      }

      public void setAdres(Adresformats adres) {
        this.adres = adres;
      }
    }
  }

  public class Cat_gegevens {

    private String         categorie      = "";
    private String         melding        = "";
    private Gesch_gegevens gesch_gegevens = new Gesch_gegevens();
    private Rijv_gegevens  rijv_gegevens  = new Rijv_gegevens();

    public String getCategorie() {
      return categorie;
    }

    public void setCategorie(String categorie) {
      this.categorie = categorie;
    }

    public Gesch_gegevens getGesch_gegevens() {
      return gesch_gegevens;
    }

    public void setGesch_gegevens(Gesch_gegevens gesch_gegevens) {
      this.gesch_gegevens = gesch_gegevens;
    }

    public String getMelding() {
      return melding;
    }

    public void setMelding(String melding) {
      this.melding = melding;
    }

    public Rijv_gegevens getRijv_gegevens() {
      return rijv_gegevens;
    }

    public void setRijv_gegevens(Rijv_gegevens rijv_gegevens) {
      this.rijv_gegevens = rijv_gegevens;
    }

    public class Gesch_gegevens {

      private String beperking     = "";
      private String datum_afgifte = "";
      private String datum_einde   = "";
      private String status        = "";
      private String toelichting   = "";

      public String getBeperking() {
        return beperking;
      }

      public void setBeperking(String beperking) {
        this.beperking = beperking;
      }

      public String getDatum_afgifte() {
        return datum_afgifte;
      }

      public void setDatum_afgifte(String datum_afgifte) {
        this.datum_afgifte = datum_afgifte;
      }

      public String getDatum_einde() {
        return datum_einde;
      }

      public void setDatum_einde(String datum_einde) {
        this.datum_einde = datum_einde;
      }

      public String getStatus() {
        return status;
      }

      public void setStatus(String status) {
        this.status = status;
      }

      public String getToelichting() {
        return toelichting;
      }

      public void setToelichting(String toelichting) {
        this.toelichting = toelichting;
      }
    }

    public class Rijv_gegevens {

      private String automaat      = "";
      private String beperking     = "";
      private String datum_afgifte = "";
      private String status        = "";
      private String toelichting   = "";

      public String getAutomaat() {
        return automaat;
      }

      public void setAutomaat(String automaat) {
        this.automaat = automaat;
      }

      public String getBeperking() {
        return beperking;
      }

      public void setBeperking(String beperking) {
        this.beperking = beperking;
      }

      public String getDatum_afgifte() {
        return datum_afgifte;
      }

      public void setDatum_afgifte(String datum_afgifte) {
        this.datum_afgifte = datum_afgifte;
      }

      public String getStatus() {
        return status;
      }

      public void setStatus(String status) {
        this.status = status;
      }

      public String getToelichting() {
        return toelichting;
      }

      public void setToelichting(String toelichting) {
        this.toelichting = toelichting;
      }
    }
  }

  public class Pers_gegevens {

    private String  adelpredikaat         = "";
    private String  anr                   = "";
    private int     burgstaat             = 0;
    private String  crbsleutel            = "";
    private String  geboren               = "";
    private String  initialen             = "";
    private String  naam                  = "";
    private String  geslacht              = "";
    private int     naamgebruik           = 0;
    private String  partner               = "";
    private String  partner_adelpredikaat = "";
    private String  partner_naam          = "";
    private String  partner_voorvoegsel   = "";
    private String  snr                   = "";
    private String  voornamen             = "";
    private String  voorvoegsel           = "";
    private Formats formats               = new Formats();

    public String getAdelpredikaat() {
      return adelpredikaat;
    }

    public void setAdelpredikaat(String titelpredikaat) {
      this.adelpredikaat = titelpredikaat;
    }

    public String getAnr() {
      return anr;
    }

    public void setAnr(String anr) {
      this.anr = anr;
    }

    public int getBurgstaat() {
      return burgstaat;
    }

    public void setBurgstaat(int burgstaat) {
      this.burgstaat = burgstaat;
    }

    public String getCrbsleutel() {
      return crbsleutel;
    }

    public void setCrbsleutel(String crbsleutel) {
      this.crbsleutel = crbsleutel;
    }

    public Formats getFormats() {
      return formats;
    }

    public void setFormats(Formats formats) {
      this.formats = formats;
    }

    public String getGeboren() {
      return geboren;
    }

    public void setGeboren(String geboren) {
      this.geboren = geboren;
    }

    public String getGeslacht() {
      return geslacht;
    }

    public void setGeslacht(String geslacht) {
      this.geslacht = geslacht;
    }

    public String getInitialen() {
      return initialen;
    }

    public void setInitialen(String initialen) {
      this.initialen = initialen;
    }

    public String getNaam() {
      return naam;
    }

    public void setNaam(String naam) {
      this.naam = naam;
    }

    public int getNaamgebruik() {
      return naamgebruik;
    }

    public void setNaamgebruik(int naamgebruik) {
      this.naamgebruik = naamgebruik;
    }

    public String getPartner() {
      return partner;
    }

    public void setPartner(String partner) {
      this.partner = partner;
    }

    public String getPartner_adelpredikaat() {
      return partner_adelpredikaat;
    }

    public void setPartner_adelpredikaat(String partner_adelpredikaat) {
      this.partner_adelpredikaat = partner_adelpredikaat;
    }

    public String getPartner_naam() {
      return partner_naam;
    }

    public void setPartner_naam(String partner_naam) {
      this.partner_naam = partner_naam;
    }

    public String getPartner_voorvoegsel() {
      return partner_voorvoegsel;
    }

    public void setPartner_voorvoegsel(String partner_voorvoegsel) {
      this.partner_voorvoegsel = partner_voorvoegsel;
    }

    public String getSnr() {
      return snr;
    }

    public void setSnr(String snr) {
      this.snr = snr;
    }

    public String getVoornamen() {
      return voornamen;
    }

    public void setVoornamen(String voornamen) {
      this.voornamen = voornamen;
    }

    public String getVoorvoegsel() {
      return voorvoegsel;
    }

    public void setVoorvoegsel(String voorvoegsel) {
      this.voorvoegsel = voorvoegsel;
    }

    public void loadFormats(boolean withVoornamen) {

      Naamformats nf = new Naamformats(withVoornamen ? getVoornamen() : getVoornaamEnInitialen(), getNaam(),
          getVoorvoegsel(), getAdelpredikaat(), ng(), null);
      Naamformats pnf = new Naamformats("", getPartner_naam(), getPartner_voorvoegsel(),
          getPartner_adelpredikaat(), ng(), null);

      getFormats().setNaam(nf);
      getFormats().setPartnernaam(pnf);
      setPartner(getFormats().getPartnernaam().getGesl_pred_init_nen_adel_voorv());
    }

    /**
     * Voegt de voornaam samen met de voorletters
     */
    private String getVoornaamEnInitialen() {

      List<String> splits = asList(initialen.split("\\s+"));

      StringBuilder sb = new StringBuilder(voornamen);

      sb.append(" ");

      if (splits.size() > 1) {

        sb.append(join(splits.subList(1, splits.size()), " "));
      }

      return trim(sb.toString());
    }

    private String ng() {
      return NaamgebruikType.getByRdwCode(getNaamgebruik()).getAfk();
    }

    public class Formats {

      private Naamformats naam        = new Naamformats();
      private Naamformats partnernaam = new Naamformats();

      public Naamformats getNaam() {
        return naam;
      }

      public void setNaam(Naamformats naam) {
        this.naam = naam;
      }

      public Naamformats getPartnernaam() {
        return partnernaam;
      }

      public void setPartnernaam(Naamformats partnernaam) {
        this.partnernaam = partnernaam;
      }
    }
  }

  public class Rijb_gegevens {

    private String autoriteit             = "";
    private String datum_afgifte_geldig   = "";
    private String datum_einde_geldig     = "";
    private String datum_verlies_diefstal = "";
    private String rijb_nr                = "";

    public String getAutoriteit() {
      return autoriteit;
    }

    public void setAutoriteit(String autoriteit) {
      this.autoriteit = autoriteit;
    }

    public String getDatum_afgifte_geldig() {
      return datum_afgifte_geldig;
    }

    public void setDatum_afgifte_geldig(String datum_afgifte_geldig) {
      this.datum_afgifte_geldig = datum_afgifte_geldig;
    }

    public String getDatum_einde_geldig() {
      return datum_einde_geldig;
    }

    public void setDatum_einde_geldig(String datum_einde_geldig) {
      this.datum_einde_geldig = datum_einde_geldig;
    }

    public String getDatum_verlies_diefstal() {
      return datum_verlies_diefstal;
    }

    public void setDatum_verlies_diefstal(String datum_verlies_diefstal) {
      this.datum_verlies_diefstal = datum_verlies_diefstal;
    }

    public String getRijb_nr() {
      return rijb_nr;
    }

    public void setRijb_nr(String rijb_nr) {
      this.rijb_nr = rijb_nr;
    }
  }

  public class Stat_gegevens {

    private String RBW_nr     = "";
    private String datum      = "";
    private String gem_ref    = "";
    private String status     = "";
    private int    statusCode = 0;
    private String tijd       = "";

    public String getDatum() {
      return datum;
    }

    public void setDatum(String datum) {
      this.datum = datum;
    }

    public String getGem_ref() {
      return gem_ref;
    }

    public void setGem_ref(String gem_ref) {
      this.gem_ref = gem_ref;
    }

    public String getRBW_nr() {
      return RBW_nr;
    }

    public void setRBW_nr(String rbw_nr) {
      RBW_nr = rbw_nr;
    }

    public String getStatus() {
      return status;
    }

    public void setStatus(String status) {
      this.status = status;
    }

    public int getStatusCode() {
      return statusCode;
    }

    public void setStatusCode(int statusCode) {
      this.statusCode = statusCode;
    }

    public String getTijd() {
      return tijd;
    }

    public void setTijd(String tijd) {
      this.tijd = tijd;
    }
  }

  public class Titel {

    private String titel1 = "";
    private String titel2 = "";
    private String titel3 = "";

    public String getTitel1() {
      return titel1;
    }

    public void setTitel1(String titel1) {
      this.titel1 = titel1;
    }

    public String getTitel2() {
      return titel2;
    }

    public void setTitel2(String titel2) {
      this.titel2 = titel2;
    }

    public String getTitel3() {
      return titel3;
    }

    public void setTitel3(String titel3) {
      this.titel3 = titel3;
    }
  }
}
