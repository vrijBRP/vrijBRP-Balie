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

package nl.procura.gba.web.windows.account.navigation;

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.components.layouts.navigation.GbaAccordionTab;
import nl.procura.gba.web.modules.account.gebruikergegevens.pages.ModuleGebruikerGegevens;
import nl.procura.gba.web.modules.account.wachtwoord.pages.ModuleWachtwoord;

public class AccountAccordionTab extends GbaAccordionTab {

  private static final long serialVersionUID = -6569187948801785943L;

  public AccountAccordionTab() {
  }

  @Override
  public void attach() {

    addTab(new AccountAlgemeenTab(getApplication()));

    super.attach();
  }

  public static class AccountAlgemeenTab extends GbaAccordionTab {

    private static final long serialVersionUID = 8676732056765207963L;

    private AccountAlgemeenTab(GbaApplication application) {

      super("Account gegevens", application);

      addLink(ModuleWachtwoord.class);
      addLink(ModuleGebruikerGegevens.class);
    }
  }
}
