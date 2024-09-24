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

package nl.procura.gba.web.services.zaken.rijbewijs.converters;

import static nl.procura.gba.web.common.tables.GbaTables.LAND;
import static nl.procura.gba.web.common.tables.GbaTables.PLAATS;
import static nl.procura.gba.web.common.tables.GbaTables.TITEL;
import static nl.procura.gba.web.common.tables.GbaTables.VBT;
import static nl.procura.standard.Globalfunctions.along;
import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.aval;
import static nl.procura.standard.Globalfunctions.date2str;
import static nl.procura.standard.Globalfunctions.fil;
import static nl.procura.standard.Globalfunctions.isTru;
import static nl.procura.standard.Globalfunctions.pad_right;
import static nl.procura.standard.Globalfunctions.pos;
import static nl.procura.standard.Globalfunctions.time2str;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.gba.functies.Geslacht;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsAanvraag;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsAanvraagAntwoord;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsStatusType;
import nl.procura.rdw.processen.p1654.f02.AANVRRYBKGEG;
import nl.procura.rdw.processen.p1654.f02.AANVRRYBKRT;
import nl.procura.rdw.processen.p1654.f02.ADRESNATPGEG;
import nl.procura.rdw.processen.p1654.f02.NATPERSOONGEG;
import nl.procura.rdw.processen.p1654.f02.STATRYBKGEG;
import nl.procura.validation.Anr;
import nl.procura.validation.Bsn;

public class P1654ToDocumentAanvraag {

  private final static Logger LOGGER = LoggerFactory.getLogger(P1654ToDocumentAanvraag.class.getName());

  public static RijbewijsAanvraagAntwoord get(AANVRRYBKRT process, Services services) {

    RijbewijsAanvraagAntwoord aanvraag = new RijbewijsAanvraagAntwoord();

    LOGGER.debug("Convert Proces 1654 Functie 2");

    // rijbewijsgegevens
    // =================

    aanvraag.getRijb_gegevens().setRijb_nr("0");
    aanvraag.getRijb_gegevens().setDatum_einde_geldig("0");
    aanvraag.getRijb_gegevens().setDatum_afgifte_geldig("0");
    aanvraag.getRijb_gegevens().setDatum_verlies_diefstal("0");
    aanvraag.getRijb_gegevens().setAutoriteit("");

    // Persoonsgegevens
    // ================

    NATPERSOONGEG pers_geg = process.getNatpersoongeg();

    String predikaat = TITEL.get(pers_geg.getAdelprednatp()).getDescription();
    String predikaatp = TITEL.get(pers_geg.getAdelpredechtg()).getDescription();

    aanvraag.getPers_gegevens().setCrbsleutel(astr(pers_geg.getNatperssl()));
    aanvraag.getPers_gegevens().setSnr(new Bsn(astr(pers_geg.getFiscnrnatp())).getFormatBsn());
    aanvraag.getPers_gegevens().setAnr(new Anr(astr(pers_geg.getGbanrnatp())).getFormatAnummer());
    aanvraag.getPers_gegevens().setNaam(pers_geg.getGeslnaamnatp());
    aanvraag.getPers_gegevens().setVoorvoegsel(pers_geg.getVoorvoegnatp());

    if (fil(pers_geg.getVoornamennatp())) {
      aanvraag.getPers_gegevens().setVoornamen(pers_geg.getVoornamennatp());
      String geslacht = Geslacht.getByRdwCode(pers_geg.getGeslaandnatp().intValue()).getNormaal();
      aanvraag.getPers_gegevens().setGeslacht(geslacht);
    } else {
      aanvraag.getPers_gegevens().setVoornamen(pers_geg.getVoornaamnatp());
      aanvraag.getPers_gegevens().setGeslacht("N.v.t.");
    }

    aanvraag.getPers_gegevens().setInitialen(pers_geg.getVoorletnatp());
    aanvraag.getPers_gegevens().setAdelpredikaat(predikaat);

    String geboren = "";

    geboren += date2str(astr(pers_geg.getGebdatnatp()));

    if (fil(pers_geg.getGebplbuitenl())) {
      geboren += ", " + pers_geg.getGebplbuitenl();
    } else if (pos(pers_geg.getAutorcgebpl())) {
      geboren += ", " + PLAATS.get(pers_geg.getAutorcgebpl().toString());
    }

    aanvraag.getPers_gegevens().setGeboren(geboren);
    aanvraag.getPers_gegevens().setNaamgebruik(aval(astr(pers_geg.getNaamgebrnatp())));
    aanvraag.getPers_gegevens().setBurgstaat(aval(astr(pers_geg.getBurgstnatp())));

    // Partner
    // =======

    aanvraag.getPers_gegevens().setPartner_naam(pers_geg.getGeslnaamechtg());
    aanvraag.getPers_gegevens().setPartner_voorvoegsel(pers_geg.getVoorvoegechtg());
    aanvraag.getPers_gegevens().setPartner_adelpredikaat(predikaatp);

    aanvraag.getPers_gegevens().loadFormats(fil(pers_geg.getVoornamennatp()));

    // Adresgegevens
    // =============

    ADRESNATPGEG adr_geg = process.getAdresnatpgeg();

    String adres = adr_geg.getLocregelnatp() + " ";
    adres += adr_geg.getStraatnatp() + " ";
    adres += adr_geg.getHuisnrnatp() + " ";
    adres += adr_geg.getHuistvnatp();

    aanvraag.getAdr_gegevens().setAdres(adres);

    aanvraag.getAdr_gegevens().setStraat(astr(adr_geg.getStraatnatp()));
    aanvraag.getAdr_gegevens().setHuisnummer(astr(adr_geg.getHuisnrnatp()));
    aanvraag.getAdr_gegevens().setHuistoevoeging(adr_geg.getHuistvnatp());
    aanvraag.getAdr_gegevens().setLocatie(adr_geg.getLocregelnatp());
    aanvraag.getAdr_gegevens().setWoonw(adr_geg.getWwabverwnp());
    aanvraag.getAdr_gegevens().setPostcode(adr_geg.getPostcnnatp() + " " + adr_geg.getPostcanatp());
    aanvraag.getAdr_gegevens().setWoonplaats(adr_geg.getWoonplnatp());
    aanvraag.getAdr_gegevens().setLand(LAND.get(adr_geg.getLandcodenatp().toString()).getDescription());

    aanvraag.getAdr_gegevens().loadFormats();

    // Aanvraaggegevens
    // ================

    AANVRRYBKGEG aanvr_geg = process.getAanvrrybkgeg();

    aanvraag.getAanvr_gegevens().setAanvraag_nr(astr(aanvr_geg.getAanvrnrrybk()));
    aanvraag.getAanvr_gegevens().setDatum(date2str(astr(aanvr_geg.getAanvrdatrybk())));
    aanvraag.getAanvr_gegevens().setTijd(time2str(pad_right(astr(aanvr_geg.getAanvrtydrybk()), "0", 6)));
    aanvraag.getAanvr_gegevens().setSoort(aval(aanvr_geg.getSrtaanvrrybk()));
    aanvraag.getAanvr_gegevens().setReden(0); // Geen reden
    aanvraag.getAanvr_gegevens().setSpoed("Nee"); // Geen spoed
    aanvraag.getAanvr_gegevens().setBestendig(isTru(aanvr_geg.getGbabestind()) ? "Ja " : "Nee");
    aanvraag.getAanvr_gegevens().setVervangt_rijb(aanvr_geg.getRybnrvervang());
    aanvraag.getAanvr_gegevens()
        .setAutoriteit(
            astr(aanvr_geg.getAutorarybk()) + ": " + PLAATS.get(aanvr_geg.getAutorarybk().toString()));
    aanvraag.getAanvr_gegevens().setLoc(astr(aanvr_geg.getGemlocarybk()));

    // Statusgegevens
    // ==============

    STATRYBKGEG stat_geg = process.getStatrybkgeg();

    aanvraag.getStat_gegevens().setStatusCode(aval(astr(stat_geg.getStatcoderybk())));
    aanvraag.getStat_gegevens()
        .setStatus(stat_geg.getStatcoderybk() + ": " +
            RijbewijsStatusType.get(along(stat_geg.getStatcoderybk())).getOms());
    aanvraag.getStat_gegevens().setDatum(date2str(stat_geg.getStatdatrybk().toString()));
    aanvraag.getStat_gegevens().setTijd(time2str(pad_right(stat_geg.getStattydrybk().toString(), "0", 6)));

    // Extra gegevens
    // ==============

    aanvraag.setLocatieInvoer(services.getGebruiker().getLocatie());

    String aanvr_nr = aanvraag.getAanvr_gegevens().getAanvraag_nr();

    if (fil(aanvr_nr)) {

      ZaakArgumenten z = new ZaakArgumenten(aanvr_nr);

      for (Zaak zaak : services.getZakenService()
          .getVolledigeZaken(services.getRijbewijsService().getMinimalZaken(z))) {

        RijbewijsAanvraag rec = (RijbewijsAanvraag) zaak;

        aanvraag.getAanvrager().setSoortIDBewijs(rec.getSoortId());
        aanvraag.getAanvrager().setNationaliteiten(rec.getNationaliteiten());
        aanvraag.getAanvrager().setCode_verblijfstatus(rec.getCodeVerblijfstitel());

        aanvraag.getAanvrager().setDatum_vestiging(rec.getDatumVestiging().getLongDate());
        aanvraag.getAanvrager().setDatum_vertrek(rec.getDatumVertrek().getLongDate());
        aanvraag.getAanvrager().setCode_land_vertrek(rec.getLandVertrek());
        aanvraag.getAanvrager().setCode_land_vestiging(rec.getLandVestiging());
        aanvraag.getAanvrager().setLand_vertrek(LAND.get(astr(rec.getLandVertrek())).getDescription());
        aanvraag.getAanvrager().setLand_vestiging(LAND.get(astr(rec.getLandVestiging())).getDescription());
        aanvraag.getAanvrager().setVerblijfstatus(VBT.get(astr(rec.getCodeVerblijfstitel())).getDescription());

        aanvraag.getAanvrager().setTelnr_thuis(rec.getTelnrThuis());
        aanvraag.getAanvrager().setTelnr_mob(rec.getTelnrMob());
        aanvraag.getAanvrager().setTelnr_werk(rec.getTelnrWerk());
        aanvraag.getAanvrager().setEmail(rec.getEmail());
        aanvraag.getAanvrager().setPersoon(rec.getPersoon());

        aanvraag.setLocatieAfhaal(rec.getLocatieAfhaal());

        break;
      }
    }

    return aanvraag;
  }
}
