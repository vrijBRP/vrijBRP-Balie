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

package nl.procura.gba.web.modules.hoofdmenu.zoeken.page1.tab6;

import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.modules.bs.registration.presenceq.PersonPresenceWindow;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.registration.DossierRegistration;
import nl.procura.gba.web.services.gba.presentievraag.PresentievraagService;
import nl.procura.gba.web.windows.home.modules.MainModuleContainer;
import nl.procura.vaadin.functies.VaadinUtils;

public class PresentievragenWindow extends GbaModalWindow {

  public PresentievragenWindow(DossierRegistration zaakDossier,
      DossierPersoon person) {
    super("Presentievragen", "1000px");
    Page page = new Page(zaakDossier, person, "Presentievragen - overzicht");
    addComponent(new MainModuleContainer(false, page));
  }

  private final class Page extends NormalPageTemplate {

    private Table               table;
    private DossierRegistration zaakDossier;
    private DossierPersoon      person;

    private Page(DossierRegistration zaakDossier,
        DossierPersoon person,
        String title) {

      super(title);

      this.zaakDossier = zaakDossier;
      this.person = person;
      buttonNew.addListener(this);
      buttonClose.addListener(this);

      getMainbuttons().addComponent(buttonNew);
      getMainbuttons().addComponent(buttonClose);
    }

    @Override
    protected void initPage() {
      table = new Table();
      addComponent(table);
      super.initPage();
    }

    @Override
    public void onNew() {
      PersonPresenceWindow window = new PersonPresenceWindow(zaakDossier,
          person, dossierPersoon -> {
            Page.this.getServices().getDossierService().savePersoon(dossierPersoon);
            table.init();
            VaadinUtils.resetHeight(getWindow());
          });
      getParentWindow().addWindow(window);
      super.onNew();
    }

    @Override
    public void onClose() {
      getWindow().closeWindow();
    }

    class Table extends PresentievragenTable {

      @Override
      public void setRecords() {
        PresentievraagService service = getServices().getPresentievraagService();
        setPresentievragen(service.getPresenceQuestionsByPerson(person));
        super.setRecords();
      }

      @Override
      public void setColumns() {
        addColumn("Nr", 30);
        addColumn("Tijdstip", 150);
        addColumn("Bericht");
        addColumn("Resultaat", 300).setUseHTML(true);
      }
    }
  }
}
