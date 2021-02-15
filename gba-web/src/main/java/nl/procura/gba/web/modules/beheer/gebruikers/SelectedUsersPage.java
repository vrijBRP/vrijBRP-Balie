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

package nl.procura.gba.web.modules.beheer.gebruikers;

import static nl.procura.gba.common.MiscUtils.setClass;
import static nl.procura.standard.Globalfunctions.fil;
import static nl.procura.standard.Globalfunctions.trim;

import java.util.List;
import java.util.Set;

import nl.procura.gba.common.MiscUtils;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.gba.web.services.beheer.gebruiker.GebruikerService;
import nl.procura.gba.web.services.beheer.gebruiker.KoppelbaarAanGebruiker;
import nl.procura.standard.exceptions.ProException;
import nl.procura.standard.exceptions.ProExceptionSeverity;
import nl.procura.standard.exceptions.ProExceptionType;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.LoadPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;

public class SelectedUsersPage extends NormalPageTemplate {

  private final int                        MAX_TO_SHOW = 3;
  private final KoppelbaarAanGebruikerType type;
  private GbaTable                         table;
  private final List<Gebruiker>            usrList;

  public SelectedUsersPage(List<Gebruiker> usrList, KoppelbaarAanGebruikerType type) {

    this.usrList = usrList;
    this.type = type;
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      table = new GbaTable() {

        @Override
        public void setColumns() {

          addColumn("Naam").setUseHTML(true);
          addColumn("Gebruiker");
          addColumn("Aantal");
          addColumn(type);
        }

        private void addColumn(KoppelbaarAanGebruikerType type) {

          if (KoppelbaarAanGebruikerType.DOCUMENT.equals(type)) {
            addColumn("Gekoppelde documenten");
          } else if (KoppelbaarAanGebruikerType.LOCATIE.equals(type)) {
            addColumn("Gekoppelde locaties");
          } else if (KoppelbaarAanGebruikerType.PROFIEL.equals(type)) {
            addColumn("Gekoppelde profielen");
          }
        }
      };

      addComponent(table);
    } else if (event.isEvent(LoadPage.class)) {

      table.getRecords().clear();

      for (Gebruiker g : usrList) {

        // gebruiker uit database!
        Gebruiker gebruiker = getServices().getGebruikerService().getGebruikerByGebruiker(g);
        Record r = table.addRecord(gebruiker);
        Set<? extends KoppelbaarAanGebruiker> coupledObjects = getCoupledObjects(gebruiker);

        String errors = getErrorString(gebruiker);
        String objectsToShow = MiscUtils.abbreviate(coupledObjects, MAX_TO_SHOW);

        r.addValue(gebruiker.getNaam() + errors);
        r.addValue(gebruiker.getGebruikersnaam());
        r.addValue(coupledObjects.size());
        r.addValue(objectsToShow);

      }

      table.reloadRecords();
    }

    super.event(event);
  }

  private Set<? extends KoppelbaarAanGebruiker> getCoupledObjects(Gebruiker user) {

    GebruikerService dG = getServices().getGebruikerService();

    if (KoppelbaarAanGebruikerType.DOCUMENT.equals(type)) {
      return dG.getGebruikerDocumenten(user);
    } else if (KoppelbaarAanGebruikerType.LOCATIE.equals(type)) {
      return dG.getGebruikerLocaties(user);
    } else if (KoppelbaarAanGebruikerType.PROFIEL.equals(type)) {
      return dG.getGebruikerProfielen(user);
    } else {
      throw new ProException(ProExceptionType.PROGRAMMING, ProExceptionSeverity.ERROR,
          "Er is iets fout gegaan bij het tonen van de gekoppelde objecten");
    }
  }

  private String getErrorString(Gebruiker gebruiker) {

    String blok = (gebruiker.isGeblokkeerd() ? "geblokkeerd" : "");
    String ww = (gebruiker.isWachtwoordVerlopen() ? ", wacht. verlopen" : "");
    String error = trim(blok + ww);

    return (fil(error) ? setClass("red", " (" + error.trim() + ")") : "");
  }
}
