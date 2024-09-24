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

package nl.procura.gba.web.modules.beheer.gebruikers.page6;

import static nl.procura.gba.web.modules.beheer.gebruikers.page6.Page6GebruikersBean.*;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.WARNING;

import java.util.ArrayList;

import com.vaadin.data.Property.ValueChangeListener;

import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.gba.web.services.beheer.gebruiker.info.GebruikerInfo;
import nl.procura.gba.web.services.beheer.gebruiker.info.GebruikerInfoService;
import nl.procura.gba.web.services.beheer.gebruiker.info.GebruikerInfoType;
import nl.procura.commons.core.exceptions.ProException;

public class Page6Gebruikers extends NormalPageTemplate {

  private final Gebruiker           gebruiker;
  private final Page6GebruikersForm form;
  private final ValueChangeListener listener;
  private final GbaTable            table;
  private boolean                   newUserInfo = false;

  public Page6Gebruikers(Gebruiker gebruiker, GbaTable table) {

    this(gebruiker, null, table);
    newUserInfo = true;
  }

  public Page6Gebruikers(Gebruiker gebruiker, GebruikerInfo gebrInfo, GbaTable table) {

    super("Toevoegen / muteren extra gebruikergegevens");

    this.gebruiker = gebruiker;
    this.table = table;

    addButton(buttonPrev, buttonNew, buttonSave);

    form = new Page6GebruikersForm(gebruiker);
    listener = (ValueChangeListener) event -> {

      newUserInfo = false;

      listenerUpdate((GebruikerInfoType) event.getProperty().getValue());
    };

    setFields(gebrInfo);

    addComponent(form);
  }

  @Override
  public void onNew() {

    newUserInfo = true;
    form.reset();
    setFields(null);
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }

  @Override
  public void onSave() {

    form.commit();

    String term = form.getBean().getTerm();
    String algemeneWaarde = form.getBean().getWaarde();
    String gebruikerWaarde = form.getBean().getGebruikerWaarde();

    GebruikerInfo gebrInfo = new GebruikerInfo();
    gebrInfo.setInfo(term);
    gebrInfo.setOmschrijving(form.getBean().getOmschrijving());

    // een nieuw record met een al bestaande term
    if (newUserInfo && isBestaandeTerm(term)) {
      throw new ProException(WARNING,
          "De ingevoerde term '" + term + "' komt reeds voor, probeer een andere term a.u.b.");
    }

    setAndSaveInfo(algemeneWaarde, gebruikerWaarde, gebrInfo);
  }

  /**
   * Check of de meegegeven term al in de database voorkomt; dit willen we voorkomen.
   */
  private boolean isBestaandeTerm(String term) {

    ArrayList<GebruikerInfo> allTableValues = table.getAllValues(GebruikerInfo.class);

    for (GebruikerInfo g : allTableValues) {
      if (g.getInfo().equals(term)) {
        return true;
      }
    }

    return false;
  }

  private void listenerUpdate(GebruikerInfoType type) {

    GebruikerInfoService db = getServices().getGebruikerInfoService();
    GebruikerInfo g = db.getGebruikerInfo(gebruiker).getInfo(type);

    if (g == null) {
      g = new GebruikerInfo();
      g.setInfo(type.getKey());
      g.setOmschrijving(type.getDescr());
    }

    setFields(g);

    form.repaint();
  }

  private void setAndSaveInfo(String algemeneWaarde, String gebruikerWaarde, GebruikerInfo gebrInfo) {

    gebrInfo.setWaarde(algemeneWaarde);
    getServices().getGebruikerInfoService().save(gebrInfo);

    gebrInfo.setWaarde(gebruikerWaarde);
    gebrInfo.setGebruiker(gebruiker);
    getServices().getGebruikerInfoService().save(gebrInfo);

    successMessage("Extra gebruikergegevens opgeslagen");
    newUserInfo = false;
  }

  private void setFields(GebruikerInfo gebruikerInfo) {

    Page6GebruikersBean bean = new Page6GebruikersBean();

    if (gebruikerInfo != null) {

      bean.setTerm(gebruikerInfo.getInfo());
      bean.setOmschrijving(gebruikerInfo.getOmschrijving());
      bean.setWaarde(gebruikerInfo.getStandaardWaarde());
      bean.setGebruikerWaarde(gebruikerInfo.getGebruikerWaarde());
    }

    form.setBean(bean);

    form.getField(TERM).setReadOnly((gebruikerInfo != null) && GebruikerInfoType.exists(gebruikerInfo.getInfo()));
    form.getField(OMSCHRIJVING).setReadOnly(
        (gebruikerInfo != null) && GebruikerInfoType.exists(gebruikerInfo.getInfo()));
    form.getField(STANDAARDOPTIE).addListener(listener);
  }
}
