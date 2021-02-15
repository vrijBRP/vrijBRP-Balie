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

package nl.procura.gba.web.modules.beheer.gebruikers.page2;

import static nl.procura.gba.common.MiscUtils.cleanPath;
import static nl.procura.gba.web.modules.beheer.gebruikers.page2.Page2GebruikersBean.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.vaadin.ui.AbstractSelect.Filtering;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Field;

import nl.procura.gba.web.components.FormFieldset;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.gba.web.services.beheer.gebruiker.GebruikerService;
import nl.procura.vaadin.component.container.ArrayListContainer;

public class Page2GebruikersForm extends GbaForm<Page2GebruikersBean> {

  private final GebruikerService dG;

  public Page2GebruikersForm(Gebruiker gebruiker, GebruikerService dG) {

    setCaption("Gebruiker");
    setOrder(GEBRUIKERS_NAAM, VOLLEDIGE_NAAM, IS_APPLICATIE_BEHEERDER, EIND_GELD_WW, GEBLOKKEERD, EMAIL,
        TELEFOONNUMMER, AFDELING, MAP, EXTRA_INFO, INGANG_GELD_GEBR, EIND_GELD_GEBR);

    setColumnWidths("200px", "");
    this.dG = dG;

    initFields(gebruiker);
  }

  @Override
  public void attach() {

    setGebruikermapContainer();
    super.attach();
  }

  public void initFields(Gebruiker gebruiker) {

    Page2GebruikersBean bean = new Page2GebruikersBean(gebruiker);

    bean.setEindeGeldWw(dG.isWachtwoordKanVerlopen(gebruiker) ? dG.getWachtwoordVerloopdatum(
        gebruiker).getFormatDate() : "Niet van toepassing");

    bean.setEmail(getEmail());
    bean.setAfdeling(getAfdeling());
    bean.setTelefoonnummer(getTelefoonnummer());

    setBean(bean);

    getField(GEBRUIKERS_NAAM).setReadOnly(gebruiker.isStored());
    getField(EIND_GELD_WW).setReadOnly(true);

    repaint();
  }

  @Override
  public Field newField(Field field, Property property) {

    if (property.is(INGANG_GELD_GEBR)) {
      getLayout().addBreak(new FormFieldset("Geldigheid"));
    }

    return super.newField(field, property);
  }

  @Override
  public void reset() {

    super.reset();
    initFields(new Gebruiker());
    setGebruikermapContainer();
  }

  public void setGebruikermapContainer() {

    ComboBox map = (ComboBox) getField(Page2GebruikersBean.MAP);
    map.setContainerDataSource(new GebruikermapContainer());
    map.setNewItemsAllowed(true);
    map.setFilteringMode(Filtering.FILTERINGMODE_CONTAINS);
    map.setValue(cleanPath(getBean().getMap()));
  }

  protected String getAfdeling() {
    return "";
  }

  protected String getEmail() {
    return "";
  }

  protected String getTelefoonnummer() {
    return "";
  }

  private class GebruikermapContainer extends ArrayListContainer {

    private GebruikermapContainer() {

      List<Gebruiker> gebrList = getApplication().getServices().getGebruikerService().getGebruikers(false);
      List<String> mapList = new ArrayList<>();

      for (Gebruiker gebruiker : gebrList) {
        String mapGebruiker = cleanPath(gebruiker.getPad());
        if (!mapList.contains(mapGebruiker)) {
          mapList.add(mapGebruiker);
        }
      }

      Collections.sort(mapList);

      for (String map : mapList) {
        addItem(map);
      }
    }
  }
}
