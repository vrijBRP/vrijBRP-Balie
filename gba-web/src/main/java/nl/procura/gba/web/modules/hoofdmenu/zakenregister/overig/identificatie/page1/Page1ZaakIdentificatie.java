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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.identificatie.page1;

import static nl.procura.standard.exceptions.ProExceptionSeverity.WARNING;

import nl.procura.gba.web.components.dialogs.DeleteProcedure;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.identificatie.page2.Page2ZaakIdentificatie;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.identificatie.ZaakIdentificatie;
import nl.procura.gba.web.services.zaken.algemeen.identificatie.ZaakIdentificatieService;
import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.component.layout.page.pageEvents.AfterReturn;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public abstract class Page1ZaakIdentificatie extends NormalPageTemplate {

  private final Zaak                  zaak;
  private Page1ZaakIdentificatieTable table;

  public Page1ZaakIdentificatie(Zaak zaak) {
    this.zaak = zaak;
    setSpacing(true);
    setMargin(false);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      addButton(buttonNew);
      addButton(buttonDel);

      setInfo("Zaakidentificaties", "Identificaties van de zaak zoals ze in verschillende systemen bekend zijn.");

      table = new Page1ZaakIdentificatieTable(zaak) {

        @Override
        public void onDoubleClick(Record record) {

          ZaakIdentificatieService db = getServices().getZaakIdentificatieService();

          ZaakIdentificatie id = record.getObject(ZaakIdentificatie.class);

          if (db.isProwebPersonen(id)) {
            throw new ProException(WARNING, "Het zaak-id van Proweb Personen kan niet worden gewijzigd");
          }

          getNavigation().goToPage(new Page2ZaakIdentificatie(zaak, id) {

            @Override
            public void onReload() {
              Page1ZaakIdentificatie.this.onReload();
            }
          });
        }
      };

      addComponent(table);
    } else if (event.isEvent(AfterReturn.class)) {
      table.init();
    }

    super.event(event);
  }

  @Override
  public void onDelete() {

    for (ZaakIdentificatie id : table.getSelectedValues(ZaakIdentificatie.class)) {

      ZaakIdentificatieService db = getServices().getZaakIdentificatieService();

      if (db.isProwebPersonen(id)) {
        throw new ProException(WARNING, "Het zaak-id van Proweb Personen is niet te verwijderen");
      }
    }

    new DeleteProcedure<ZaakIdentificatie>(table, false) {

      @Override
      public void deleteValue(ZaakIdentificatie id) {

        ZaakIdentificatieService db = getServices().getZaakIdentificatieService();

        if (db.isProwebPersonen(id)) {
          throw new ProException(WARNING, "Het zaak-id van Proweb Personen is niet te verwijderen");
        }

        db.delete(id);
      }

      @Override
      protected void afterDelete() {
        onReload();
      }
    };

    super.onDelete();
  }

  @Override
  public void onNew() {

    ZaakIdentificatie id = new ZaakIdentificatie();
    id.setInternId(zaak.getZaakId());

    getNavigation().goToPage(new Page2ZaakIdentificatie(zaak, id) {

      @Override
      public void onReload() {
        Page1ZaakIdentificatie.this.onReload();
      }
    });

    super.onNew();
  }

  public abstract void onReload();
}
