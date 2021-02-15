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

import static ch.lambdaj.Lambda.*;
import static nl.procura.standard.Globalfunctions.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.ui.Button;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.extensions.PLResultComposite;
import nl.procura.diensten.gba.ple.procura.arguments.PLEArgs;
import nl.procura.gba.web.components.layouts.tablefilter.GbaIndexedTableFilterLayout;
import nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.tab1.result.Tab1ResultPage;
import nl.procura.gba.web.services.beheer.parameter.ParameterConstant;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.info.InfoLayout;
import nl.procura.vaadin.component.layout.page.pageEvents.AfterBackwardReturn;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;

import ch.lambdaj.group.Group;

public class ZoekResultPage extends ZoekTabPage {

  private final static Logger LOGGER = LoggerFactory.getLogger(ZoekResultPage.class.getName());

  protected final Button buttonToonAlles = new Button("");

  private final InfoLayout  info                = new InfoLayout();
  private ZoekAdresTable    adresTable          = null;
  private ZoekPersoonTable  persoonTable        = null;
  private PLEArgs           arguments;
  private PLResultComposite result;
  private Adres             adres;
  private boolean           rekeninghoudenAdres = false;

  public ZoekResultPage(String title, Adres adres) {
    this(title);
    this.adres = adres;
  }

  public ZoekResultPage(String title, PLEArgs arguments, PLResultComposite result) {
    this(title);
    this.arguments = arguments;
    this.result = result;
  }

  private ZoekResultPage(String title) {
    super(title);
    setSizeFull();
    addStyleName("yyyy");
    setMargin(true);
    addButton(buttonPrev);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      List<Adres> adressen = getAdressen();

      String geheimMelding;

      if (adressen.size() > 1) {

        adresTable = new ZoekAdresTable(adressen) {

          @Override
          public void onClick(Record record) {

            selectAdres((Adres) record.getObject());
          }
        };

        addButton(buttonNext);

        setResultInfo();

        addComponent(new Fieldset(
            "Verdeling van de " + result.getBasisPLWrappers().size() + " persoonlijsten over " + adressen.size()
                + " adressen"));

        addExpandComponent(adresTable);

        adresTable.focus();

        getButtonLayout().addComponent(new GbaIndexedTableFilterLayout(adresTable));

        geheimMelding = adresTable.getGeheimMelding();
      } else {

        persoonTable = new ZoekPersoonTable(adressen) {

          @Override
          public void onClick(Record record) {
            selectPersoonslijst(record);
          }
        };

        setResultInfo();

        setSizeFull();

        persoonTable.setSizeFull();

        addExpandComponent(persoonTable);

        persoonTable.focus();

        getButtonLayout().addComponent(new GbaIndexedTableFilterLayout(persoonTable));

        geheimMelding = persoonTable.getGeheimMelding();
      }

      if (fil(geheimMelding)) {

        info.append(geheimMelding);

        if (info.getParent() == null) {

          addComponent(info, getComponentIndex(getButtonLayout()) + 1);
        }
      }
    } else if (event.isEvent(AfterBackwardReturn.class)) {

      if (persoonTable != null) {

        persoonTable.init();
      } else {
        adresTable.init();
      }
    }

    super.event(event);
  }

  public Adres getAdres() {
    return adres;
  }

  public void setAdres(Adres adres) {
    this.adres = adres;
  }

  public PLResultComposite getResult() {
    return result;
  }

  @Override
  public void handleEvent(Button button, int keyCode) {

    if (button == buttonToonAlles) {
      toonAlles();
    }

    super.handleEvent(button, keyCode);
  }

  public boolean isRekeninghoudenAdres() {
    return rekeninghoudenAdres;
  }

  public void setRekeninghoudenAdres(boolean rekeninghoudenAdres) {
    this.rekeninghoudenAdres = rekeninghoudenAdres;
  }

  @Override
  public void onEnter() {

    if (adresTable != null) {
      selectAdres((Adres) adresTable.getRecord().getObject());
    } else {
      selectPersoonslijst(persoonTable.getRecord());
    }
  }

  @Override
  public void onNextPage() {

    if (getAdressen().size() > 1) {

      getNavigation().goToPage(new Tab1ResultPage(arguments, false, result));
    }

    super.onNextPage();
  }

  @SuppressWarnings("unused")
  protected void selectAdres(Adres adres) {
  }

  @SuppressWarnings("unused")
  protected void selectPersoonslijst(Record record) {
  }

  private List<Adres> getAdressen() {

    List<Adres> adresTypes = new ArrayList<>();

    if (adres != null) {

      adresTypes.add(adres);
    } else if (result != null) {

      if (isRekeninghoudenAdres()) {

        List<BasePLExt> pls = result.getBasisPLWrappers();

        Group<BasePLExt> adressen = group(pls,
            by(on(BasePLExt.class).getVerblijfplaats().getAdres().getAdresPc()));

        for (Group<BasePLExt> adres : adressen.subgroups()) {

          adresTypes.add(new Adres(astr(adres.key()), adres.findAll()));
        }
      } else {

        adresTypes.add(new Adres(astr(""), result.getBasisPLWrappers()));
      }
    }

    Collections.sort(adresTypes);

    return adresTypes;
  }

  private void setResultInfo() {

    info.clear();

    if (result != null) {

      buttonNext.setCaption("Toon alle " + result.getTotaalAantal() + " personen (F2)");

      if (result.getTotaalAantal() > result.getMaxAantal()) {

        addButton(buttonToonAlles);

        buttonNext.setCaption("Toon de eerste " + result.getMaxAantal() + " personen (F2)");

        buttonToonAlles.setCaption("Toon alle " + result.getTotaalAantal() + " personen");

        info.setHeader("Meer zoekresultaten dan maximum.");
        info.setMessage(
            "Er zijn <b>%s</b> persoonslijsten gevonden. "
                + "Daarvan worden de eerste <b>%s</b> getoond. Specifieer de zoekopdracht verder.",
            result.getTotaalAantal(), result.getMaxAantal());

      } else {

        info.setMessage("Er zijn <b>%s</b> persoonslijsten gevonden.", result.getTotaalAantal());

        removeButton(buttonToonAlles);
      }

      addComponent(info);
    } else if (adres != null) {

      info.setMessage("Er zijn <b>%s</b> persoonslijsten gevonden op dit adres.", adres.getPls().size());

      addComponent(info, getComponentIndex(getButtonLayout()) + 1);
    }

    // Controle of de gebruiker wel alles mag tonen
    if (!isTru(getServices().getParameterService().getParm(ParameterConstant.ZOEK_ALL_RECORDS))) {
      removeButton(buttonToonAlles);
    }
  }

  /**
   * Toont alle persoonslijsten
   */
  private void toonAlles() {

    LOGGER.warn(
        "Gebruiker: " + getApplication().getServices().getGebruiker().getGebruikersnaam() + " zoekt naar alle "
            + result.getTotaalAantal() + " personen");

    long startTime = System.currentTimeMillis();

    arguments.setMaxFindCount(result.getTotaalAantal() * 2);

    getNavigation().goToPage(new Tab1ResultPage(null, false,
        getApplication().getServices().getPersonenWsService().getPersoonslijsten(
            arguments, false)));

    long endTime = System.currentTimeMillis();

    LOGGER.warn("Dit duurde " + (endTime - startTime) + " ms.");
  }

  public class Adres implements Comparable<Adres> {

    private String          omschrijving = "";
    private List<BasePLExt> pls          = new ArrayList<>();

    public Adres() {
    }

    private Adres(String omschrijving, List<BasePLExt> pls) {
      this.omschrijving = omschrijving;
      this.pls = pls;
    }

    @Override
    public int compareTo(Adres adres) {
      return getOmschrijving().compareToIgnoreCase(adres.getOmschrijving());
    }

    public String getOmschrijving() {
      return omschrijving;
    }

    public void setOmschrijving(String omschrijving) {
      this.omschrijving = omschrijving;
    }

    public List<BasePLExt> getPls() {
      return pls;
    }

    public void setPls(List<BasePLExt> pls) {
      this.pls = pls;
    }
  }
}
