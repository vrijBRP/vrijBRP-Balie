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

package nl.procura.gba.web.modules.hoofdmenu.zoeken.page1;

import static nl.procura.burgerzaken.gba.core.enums.GBACat.*;
import static nl.procura.standard.Globalfunctions.*;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.extensions.PLResultComposite;
import nl.procura.diensten.gba.ple.procura.arguments.PLEArgs;
import nl.procura.diensten.gba.ple.procura.arguments.PLEDatasource;
import nl.procura.diensten.gba.ple.procura.templates.custom.CustomTemplate;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.tab2.result.Tab2ResultPage;
import nl.procura.gba.web.services.beheer.parameter.ParameterConstant;
import nl.procura.vaadin.component.layout.info.InfoLayout;
import nl.procura.vaadin.component.window.Message;

public class ZoekTabPage extends NormalPageTemplate {

  public ZoekTabPage(String title) {
    super(title);
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }

  /**
   * De inlogopmerking
   */
  protected void setInlogOpmerking() {

    String opmerking = getApplication().getParmValue(ParameterConstant.INLOGOPMERKING);
    if (fil(opmerking)) {
      addComponent(new InfoLayout("Ter informatie", opmerking));
    }
  }

  protected void zoekLandelijk(ZoekBean b) {

    getServices().getPersonenWsService().getOpslag().clear();

    PLEArgs arguments = new PLEArgs();
    arguments.addNummer(b.getBsn().getStringValue());
    arguments.addNummer(b.getAnr().getStringValue());
    arguments.setGeboortedatum(b.getGeboortedatum().getStringValue());

    if (b.getGeslacht() != null) {
      arguments.setGeslacht(b.getGeslacht().getAfkorting());
    }

    arguments.setGeslachtsnaam(astr(b.getGeslachtsnaam()));

    if (b.getVoorvoegsel() != null) {
      arguments.setVoorvoegsel(b.getVoorvoegsel().getDescription());
    }

    arguments.setVoornaam(b.getVoornaam());
    arguments.setTitel(b.getTitel());

    if (b.getStraatLand() != null) {
      arguments.setStraat(b.getStraatLand());
    }

    arguments.setHuisnummer(b.getHnr());
    arguments.setPostcode(b.getPostcode());
    arguments.setHuisletter(b.getHnrl());
    arguments.setHuisnummertoevoeging(b.getHnrt());

    if (b.getGemeente() != null) {
      arguments.setGemeente(b.getGemeente().getValue().toString());
    }

    arguments.setSearchOnAddress(b.isAdresind());
    arguments.setShowHistory(false);
    arguments.setShowArchives(false);

    if (emp(b.getPostcode()) && emp(b.getAnr().getDescription()) && emp(b.getBsn().getDescription()) && emp(
        b.getGeslachtsnaam())) {
      new Message(getWindow(), "Voorwaarden landelijke zoekactie",
          "Geef minimaal een BSN, A-nummer, Geslachtsnaam of Postcode in.", Message.TYPE_INFO);
    } else {

      arguments.addCat(PERSOON, VB, INSCHR, VERW);
      arguments.setCat(HUW_GPS, isTru(getApplication().getServices().getGebruiker().getParameters().get(
          ParameterConstant.ZOEK_PLE_NAAMGEBRUIK).getValue()));
      arguments.setCustomTemplate(CustomTemplate.PERSON);
      arguments.setDatasource(PLEDatasource.GBAV);
      arguments.setMaxFindCount(aval(getApplication().getServices().getGebruiker().getParameters().get(
          ParameterConstant.ZOEK_MAX_FOUND_COUNT).getValue()));

      PLResultComposite result = getApplication().getServices().getPersonenWsService().getPersoonslijsten(
          arguments, false);

      switch (result.getBasisPLWrappers().size()) {

        case 0:
          new Message(getWindow(), "Geen zoekresultaten", Message.TYPE_WARNING_MESSAGE);
          break;

        case 1:
          // Als gebruiker GBA-V plus heeft dan niet cache weggooien
          BasePLExt pl = result.getBasisPLWrappers().get(0);
          getApplication().goToPl(getWindow(), "", pl.getDatasource(), pl);
          break;

        default:
          getNavigation().goToPage(new Tab2ResultPage(arguments, result));
          break;
      }
    }
  }
}
