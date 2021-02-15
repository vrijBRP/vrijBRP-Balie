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

package nl.procura.gba.web.modules.zaken.document.page3;

import nl.procura.gba.web.components.dialogs.DeleteProcedure;
import nl.procura.gba.web.modules.zaken.document.DocumentenPage;
import nl.procura.gba.web.modules.zaken.document.DocumentenTabel;
import nl.procura.gba.web.services.beheer.profiel.actie.ProfielActie;
import nl.procura.gba.web.services.zaken.algemeen.dms.DmsDocument;
import nl.procura.gba.web.services.zaken.algemeen.dms.DmsResultaat;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page3Document extends DocumentenPage {

  private DocumentenTabel table = null;

  public Page3Document() {
    super("Documenten: archief");
    setMargin(true);
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {
      // Als je geen document mag verwijderen dan button uitzetten.
      if (getApplication().isProfielActie(ProfielActie.DELETE_ZAAK_DOCUMENT_ARCHIEF)) {
        addButton(buttonDel);
      } else {
        buttonDel.setVisible(false);
      }

      table = new DocumentenTabel() {

        @Override
        public DmsResultaat getOpgeslagenBestanden() {
          return getServices().getDmsService().getDocumenten(getPl());
        }
      };
      addExpandComponent(table);
    }

    super.event(event);
  }

  @Override
  public void onDelete() {

    new DeleteProcedure<DmsDocument>(table) {

      @Override
      public void afterDelete() {
        reloadCaptions();
      }

      @Override
      public void deleteValue(DmsDocument record) {
        getServices().getDmsService().delete(record);
      }
    };
  }

  @Override
  public void onEnter() {
    if (table.getRecord() != null) {
      table.openBestand(table.getRecord());
    }
  }
}
