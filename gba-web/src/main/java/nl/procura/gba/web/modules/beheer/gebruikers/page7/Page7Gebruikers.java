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
import static nl.procura.gba.web.modules.beheer.gebruikers.page2.Page2GebruikersBean.MAP;

import java.util.List;

import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.modules.beheer.gebruikers.page1.Page1Gebruikers;
import nl.procura.gba.web.modules.beheer.gebruikers.page2.Page2GebruikersBean;
import nl.procura.gba.web.modules.beheer.overig.CheckPadEnOpslaanGebruiker;
import nl.procura.gba.web.modules.beheer.overig.TabelToonType;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;

public class Page7Gebruikers extends NormalPageTemplate {

  private final List<Gebruiker> gebrList;
  private final MakeMapForm     form;
  private final String          commonGebrPath;
  private final TabelToonType   toonType;

  public Page7Gebruikers(List<Gebruiker> gebrList, TabelToonType toonType) {

    super("Gebruikers: indelen in mappen");
    addButton(buttonPrev, buttonSave);

    this.gebrList = gebrList;
    form = new MakeMapForm(gebrList);
    addComponent(form);
    commonGebrPath = (String) form.getField(MAP).getValue();

    this.toonType = toonType;
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }

  @Override
  public void onSave() {
    form.commit();
    String cleanedPath = cleanPath(form.getBean().getMap());

    new CheckPadEnOpslaanGebruiker(cleanedPath, null,
        form) { // merk op dat de bean niet gebruikt wordt in dit geval

      @Override
      protected void nietOpslaanGebruikerActies() {
        form.setGebruikermapContainer();
        form.getField(MAP).setValue(commonGebrPath);
      }

      @Override
      protected void opslaanGebruiker(String userPath, Page2GebruikersBean bean) {
        saveGebruikers(userPath);
        showRelevantPage(userPath);
      }

      @Override
      protected void welOpslaanGebruikerActies(String userPath, Page2GebruikersBean bean) {
        saveGebruikers(userPath);
        showRelevantPage(userPath);
      }
    };
  }

  private void saveGebruikers(String gebruikerPath) {
    for (Gebruiker gebruiker : gebrList) {
      gebruiker.setPad(gebruikerPath);
    }

    getServices().getGebruikerService().save(gebrList);
  }

  private void showDirPage(String legalPath) {
    getNavigation().goToPage(new Page1Gebruikers(legalPath));
    getNavigation().removeOtherPages();
  }

  private void showRelevantPage(String gebruikersPath) {

    if (toonType == TabelToonType.MAPPEN) {
      showDirPage(gebruikersPath);
    } else { // lijstvorm: we gaan terug naar de vorige pagina.
      getNavigation().goBackToPreviousPage();
    }
  }
}
