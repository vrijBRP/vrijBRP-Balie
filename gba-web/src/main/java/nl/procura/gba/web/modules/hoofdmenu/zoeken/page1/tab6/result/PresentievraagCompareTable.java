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

import static nl.procura.standard.Globalfunctions.*;

import com.vaadin.ui.Embedded;

import nl.bprbzk.bcgba.v14.BeheerIdenGegVraagDE;
import nl.procura.gba.web.components.TableImage;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.gba.presentievraag.PresentievraagAntwoord;
import nl.procura.gba.web.services.gba.presentievraag.PresentievraagMatch;
import nl.procura.vaadin.theme.twee.Icons;
import nl.procura.validation.Bsn;

public class PresentievraagCompareTable extends GbaTable {

  private final PresentievraagAntwoord presentieVraagAntwoord;
  private final PresentievraagMatch    match;

  public PresentievraagCompareTable(PresentievraagAntwoord presentieVraagAntwoord, PresentievraagMatch match) {
    this.presentieVraagAntwoord = presentieVraagAntwoord;
    this.match = match;
  }

  @Override
  public void setColumns() {

    setSelectable(false);

    addColumn("", 20).setClassType(Embedded.class);
    addColumn("Gegeven", 250);
    addColumn("Vraag", 300);
    addColumn("Antwoord");

    super.setColumns();
  }

  @Override
  public void setRecords() {

    BeheerIdenGegVraagDE vraag = presentieVraagAntwoord.getGegevensVraag();

    if (vraag != null && match != null) {

      Bsn bsnVraag = new Bsn(presentieVraagAntwoord.getBsnRelatie());
      Bsn bsnAntwoord = new Bsn(match.getMatch().getBsn());

      if (bsnVraag.isCorrect()) {
        addRecord("Burgerservicenummer", bsnVraag.getFormatBsn(), bsnAntwoord.getFormatBsn());
      }

      addRecord("Voornamen", vraag.getVoornamen(), match.getMatch().getVoornamen());
      addRecord("Voorvoegsel", vraag.getVoorvoegselGeslachtsnaam(),
          match.getMatch().getVoorvoegselGeslachtsnaam());
      addRecord("Geslachtsnaam", vraag.getGeslachtsnaam(), match.getMatch().getGeslachtsnaam());
      addRecord("Geboortedatum", date2str(vraag.getGeboortedatum()),
          date2str(match.getMatch().getGeboortedatum()));
      addRecord("Geboorteplaats", vraag.getGeboorteplaats(), match.getMatch().getGeboorteplaats());
      addRecord("Geboorteland", vraag.getGeboorteland(), match.getMatch().getGeboorteland());
      addRecord("Geslachtsaanduiding", vraag.getGeslachtsaanduiding(), match.getMatch().getGeslachtsaanduiding());
      addRecord("Datum aanvang adres buitenland", date2str(vraag.getDatumAanvangAdresBuitenland()),
          date2str(match.getMatch().getDatumAanvangAdresBuitenland()));
      addRecord("Gemeente van inschrijving", vraag.getGemeenteVanInschrijving(),
          match.getMatch().getGemeenteVanInschrijving());

      addRecord("Buitenlands persoonsnummer", vraag.getBuitenlandsPersoonsnummer(),
          match.getMatch().getBuitenlandsPersoonsnummer());
      addRecord("Nationaliteit", vraag.getNationaliteit(), match.getMatch().getNationaliteit());
    }

    super.setRecords();
  }

  private void addRecord(String type, String vraag, String antwoord) {

    if (fil(vraag)) {

      String trimmedVraag = trimQuotes(vraag);
      String trimmedAntwoord = trimQuotes(antwoord);

      Record r = addRecord("");
      boolean match = trimmedAntwoord.toLowerCase().contains(trimmedVraag.toLowerCase());
      r.addValue(new TableImage(Icons.getIcon(match ? Icons.ICON_OK : Icons.ICON_WARN)));
      r.addValue(type);
      r.addValue(trimmedVraag);
      r.addValue(trimmedAntwoord);
    }
  }

  /**
   * Fix om overbodige ; tekens te verwijderen
   */
  private String trimQuotes(String s) {
    return astr(s).replaceAll(";+$", "");
  }
}
