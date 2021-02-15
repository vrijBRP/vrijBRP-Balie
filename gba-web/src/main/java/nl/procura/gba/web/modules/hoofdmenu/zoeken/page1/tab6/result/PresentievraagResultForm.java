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

package nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.tab6.result;

import static nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.tab6.result.PresentievraagResultBean.*;
import static nl.procura.standard.Globalfunctions.*;

import com.vaadin.ui.Field;

import nl.bprbzk.bcgba.v14.BeheerIdenGegVraagDE;
import nl.bprbzk.bcgba.v14.MatchIdenGegAntwoordDE;
import nl.procura.bcgba.v14.misc.BcGbaVraagbericht;
import nl.procura.diensten.gba.ple.openoffice.formats.Adresformats;
import nl.procura.diensten.gba.ple.openoffice.formats.Geboorteformats;
import nl.procura.gba.web.components.layouts.form.ReadOnlyForm;
import nl.procura.gba.web.services.gba.functies.Geslacht;
import nl.procura.gba.web.services.gba.presentievraag.Presentievraag;
import nl.procura.gba.web.services.gba.presentievraag.PresentievraagAntwoord;
import nl.procura.gba.web.services.gba.presentievraag.PresentievraagMatch;
import nl.procura.gba.web.services.zaken.verhuizing.FunctieAdres;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;
import nl.procura.validation.Bsn;

public class PresentievraagResultForm extends ReadOnlyForm {

  public PresentievraagResultForm(Presentievraag presentievraag, PresentievraagMatch presentievraagMatch,
      String... order) {

    setReadonlyAsText(true);
    setOrder(order);
    setColumnWidths("200px", "300px", "180px", "");

    PresentievraagAntwoord antwoord = presentievraag.getPresentievraagAntwoord();

    PresentievraagResultBean b = new PresentievraagResultBean();
    b.setZaakOmschrijving(presentievraag.getZaakOmschrijving());
    b.setZaakTijdstip(presentievraag.getDatumTijdInvoer().toString());
    b.setZaakGebruiker(presentievraag.getIngevoerdDoor().getDescription());
    b.setZaakLocatie(presentievraag.getLocatieInvoer().getOmschrijving());
    b.setZaakToelichting(presentievraag.getToelichting());

    if (antwoord != null) {

      b.setVraag(BcGbaVraagbericht.get(antwoord.getVraagBericht()).getOms());
      b.setVerwerking(antwoord.toVerwerkingString());
      b.setResultaat(antwoord.toResultaatString(true));
      b.setResultaatPn(antwoord.toResultaatPn(true));

      BeheerIdenGegVraagDE vraag = antwoord.getGegevensVraag();

      if (vraag != null) {

        b.setVraagVoornamen(vraag.getVoornamen());
        b.setVraagVoorvoegsel(vraag.getVoorvoegselGeslachtsnaam());
        b.setVraagGeslachtsnaam(vraag.getGeslachtsnaam());
        b.setVraagGeboortedatum(date2str(vraag.getGeboortedatum()));
        b.setVraagGeboorteplaats(vraag.getGeboorteplaats());
        b.setVraagGeboorteland(vraag.getGeboorteland());
        b.setVraagGeslachtsaanduiding(Geslacht.get(vraag.getGeslachtsaanduiding()).getNormaal());
        b.setVraagGemeenteInschrijving(vraag.getGemeenteVanInschrijving());
        b.setVraagBuitenlandsPersoonsnummer(vraag.getBuitenlandsPersoonsnummer());
        b.setVraagNationaliteit(vraag.getNationaliteit());
      }

      if (presentievraagMatch != null) {

        MatchIdenGegAntwoordDE match = presentievraagMatch.getMatch();

        if (match != null) {

          b.setVolgnr(astr(match.getVolgnummerMatch()));
          b.setRegistratie(match.getRegistratie());
          b.setScore(astr(match.getScore()));

          Bsn antwoordBsn = new Bsn(astr(match.getBsn()));
          if (antwoordBsn.isCorrect()) {
            StringBuilder bsnMsg = new StringBuilder(antwoordBsn.getFormatBsn());
            b.setBsn(bsnMsg.toString());
          }

          b.setVoornamen(match.getVoornamen());
          b.setVoorvoegsel(match.getVoorvoegselGeslachtsnaam());
          b.setGeslachtsnaam(match.getGeslachtsnaam());
          b.setAdelijketitel(match.getAdellijkeTitelPredikaat());

          Geboorteformats geboorte = new Geboorteformats();
          geboorte.setValues(date2str(match.getGeboortedatum()), match.getGeboorteplaats(),
              match.getGeboorteland());

          b.setGeboren(geboorte.getDatum_te_plaats_land());
          b.setGeslacht(getGeslacht(match.getGeslachtsaanduiding()));
          b.setNationaliteit(trimQuotes(match.getNationaliteit()));
          b.setBuitenlandsPersoonsnummer(trimQuotes(match.getBuitenlandsPersoonsnummer()));
          b.setGemeenteVanInschrijving(match.getGemeenteVanInschrijving());
          b.setDatumVertrekNL(match.getDatumAanvangAdresBuitenland());
          b.setDatumOverlijden(date2str(match.getDatumOverlijden()));
          b.setIndicatieGeheim(pos(match.getIndicatieGeheim()) ? "Ja" : "Nee");
          b.setOpschorting(getRedenOpschorting(match.getOmschrijvingRedenOpschorting()));

          if (fil(match.getFunctieAdres())) {
            b.setFunctieAdres(FunctieAdres.get(match.getFunctieAdres()).getOms());
          }

          b.setGemeenteDeel(match.getGemeentedeel());

          Adresformats adres = new Adresformats();
          adres.setValues(astr(match.getStraatnaam()), astr(match.getHuisnummer()),
              astr(match.getHuisletter()), astr(match.getHuisnummertoevoeging()),
              astr(match.getAanduidingBijHuisnummer()), "", "", match.getGemeentedeel(),
              match.getWoonplaatsnaam(), "", "", "", "", "", "", "");

          Adresformats adresBuitenland = new Adresformats();
          adresBuitenland.setValues("", "", "", "", "", "", "", "", "", "", "", "", "", "",
              match.getRegel2AdresBuitenland(), match.getRegel3AdresBuitenland());

          b.setLocatie(match.getLocatiebeschrijving());
          b.setAdres(adres.getAdres_pc_wpl_gem());
          b.setAdresBuitenland(adresBuitenland.getAdres());
          b.setLandVanVertrek(match.getLandVanwaarIngeschreven());
        }
      }
    }

    setBean(b);
  }

  @Override
  public PresentievraagResultBean getBean() {
    return (PresentievraagResultBean) super.getBean();
  }

  @Override
  public void setColumn(Column column, Field field, Property property) {

    if (property.is(GEMEENTEVANINSCHRIJVING, VR_GESLACHT, ZAAK_TOELICHTING)) {
      column.setColspan(3);
    }

    super.setColumn(column, field, property);
  }

  private String getGeslacht(String code) {
    return Geslacht.get(code).getNormaal();
  }

  private String getRedenOpschorting(String code) {

    String oms = "";
    if (code != null) {
      switch (code) {
        case "O":
          oms = "Overlijden";
          break;

        case "E":
          oms = "Emigratie";
          break;

        case "M":
          oms = "ministrieel besluit";
          break;

        case "R":
          oms = "PL aangelegd in de RNI";
          break;

        default:
          break;
      }
    }
    return oms;
  }

  /**
   * Fix om overbodige ; tekens te verwijderen
   */
  private String trimQuotes(String s) {
    return astr(s).replaceAll(";+$", "");
  }
}
