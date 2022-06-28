/*
 * Copyright 2022 - 2023 Procura B.V.
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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.behandelaar.page1;

import static nl.procura.gba.web.services.beheer.profiel.actie.ProfielActie.DELETE_ZAAKBEHANDELAARS;
import static nl.procura.gba.web.services.beheer.profiel.actie.ProfielActie.UPDATE_ZAAKBEHANDELAARS;

import nl.procura.gba.web.components.dialogs.DeleteProcedure;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.behandelaar.page2.Page2ZaakBehandelaar;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.attribuut.ZaakBehandelaar;
import nl.procura.vaadin.component.layout.page.pageEvents.AfterBackwardReturn;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public abstract class Page1ZaakBehandelaar extends NormalPageTemplate {

  private final Zaak                zaak;
  private Page1ZaakBehandelaarTable table = null;

  public Page1ZaakBehandelaar(Zaak zaak) {
    this.zaak = zaak;

    setSpacing(true);
    setMargin(false);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {
      boolean allowUpdate = getApplication().isProfielActie(UPDATE_ZAAKBEHANDELAARS);
      boolean allowDelete = getApplication().isProfielActie(DELETE_ZAAKBEHANDELAARS);
      if (allowUpdate) {
        if (allowDelete) {
          addButton(buttonNew);
        }else {
          addButton(buttonNew, 1f);
        }
      }
      if (allowDelete) {
        addButton(buttonDel, 1f);
      }
      if (getWindow() instanceof GbaModalWindow) {
        addButton(buttonClose);
      }

      setInfo("Behandelaars", "De behandelaars die zijn toegewezen (geweest) aan deze zaak");

      table = new Page1ZaakBehandelaarTable(zaak) {

        @Override
        public void onDoubleClick(Record record) {
          getNavigation().addPage(new Page2ZaakBehandelaar(record.getObject(ZaakBehandelaar.class)) {

            @Override
            public void onReload() {
              Page1ZaakBehandelaar.this.onReload();
            }
          });
          super.onDoubleClick(record);
        }
      };
      addComponent(table);

    } else if (event.isEvent(AfterBackwardReturn.class)) {
      table.init();
    }

    super.event(event);
  }

  @Override
  public void onClose() {
    getWindow().closeWindow();
  }

  @Override
  public void onDelete() {

    new DeleteProcedure<ZaakBehandelaar>(table) {

      @Override
      public void deleteValue(ZaakBehandelaar behandelaar) {
        getServices().getZaakAttribuutService().delete(behandelaar);
      }

      @Override
      protected void afterDelete() {
        onReload();
      }
    };
  }

  @Override
  public void onNew() {

    ZaakBehandelaar behandelaar = new ZaakBehandelaar();
    behandelaar.setZaakId(zaak.getZaakId());

    getNavigation().goToPage(new Page2ZaakBehandelaar(behandelaar) {

      @Override
      public void onReload() {
        Page1ZaakBehandelaar.this.onReload();
      }
    });

    super.onNew();
  }

  public abstract void onReload();
}
