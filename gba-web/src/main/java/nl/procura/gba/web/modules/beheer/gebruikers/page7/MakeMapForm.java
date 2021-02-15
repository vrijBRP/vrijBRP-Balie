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

package nl.procura.gba.web.modules.beheer.gebruikers.page7;

import static nl.procura.gba.common.MiscUtils.cleanPath;
import static nl.procura.gba.web.modules.beheer.gebruikers.page7.MakeMapBean.AANTALGEBR;
import static nl.procura.gba.web.modules.beheer.gebruikers.page7.MakeMapBean.MAP;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.vaadin.ui.ComboBox;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.vaadin.component.container.ArrayListContainer;

public class MakeMapForm extends GbaForm<MakeMapBean> {

  public MakeMapForm(List<Gebruiker> selectedUserList) {

    setOrder(AANTALGEBR, MAP);
    initFields(selectedUserList);
  }

  @Override
  public void attach() {
    setGebruikermapContainer();
    super.attach();
  }

  protected void setGebruikermapContainer() {

    ComboBox map = (ComboBox) getField(MakeMapBean.MAP);
    map.setContainerDataSource(new GebruikermapContainer());
    map.setNewItemsAllowed(true);
    map.setValue(cleanPath(getBean().getMap()));
  }

  /**
   * Kijkt of er een gemeenschappelijke map te vinden is voor de geselecteerde gebruikers.
   */

  private String getCommonUserPath(List<Gebruiker> userList) {

    String path = userList.get(0).getPad(); // we nemen het eerste pad als referentie

    for (Gebruiker gebruiker : userList) {

      String userPath = gebruiker.getPad();

      if (path.startsWith(userPath)) {
        path = userPath;
      } else if (!userPath.startsWith(path)) { // geen gemeenschappelijk pad!
        return "";
      }
    }

    return path;
  }

  private void initFields(List<Gebruiker> gebrList) {

    String commonUserPath = getCommonUserPath(gebrList);

    MakeMapBean bean = new MakeMapBean();
    bean.setAantalGebruikers(gebrList.size());
    bean.setMap(commonUserPath);

    setBean(bean);
  }

  private final class GebruikermapContainer extends ArrayListContainer {

    private GebruikermapContainer() {

      List<Gebruiker> usrList = getApplication().getServices().getGebruikerService().getGebruikers(false);
      List<String> pathList = new ArrayList<>();

      for (Gebruiker g : usrList) {
        String cleanedPath = cleanPath(g.getPad());

        if (!pathList.contains(cleanedPath)) {
          pathList.add(cleanedPath);
        }
      }

      Collections.sort(pathList);

      for (String map : pathList) {
        addItem(map);
      }
    }
  }
}
