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

package nl.procura.gba.web.modules.hoofdmenu.raas.tab1.page1;

import static nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.bsm.BsmUitvoerenBean.TAAK;

import nl.procura.bsm.rest.v1_0.objecten.taak.BsmRestTaak;
import nl.procura.bsm.rest.v1_0.objecten.taak.BsmRestTaakVraag;
import nl.procura.bsm.taken.BsmTaak;
import nl.procura.bsm.taken.GbaBsmTaak;
import nl.procura.dto.raas.aanvraag.DocAanvraagDto;
import nl.procura.gba.web.components.dialogs.DeleteProcedure;
import nl.procura.gba.web.components.layouts.OptieLayout;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.modules.hoofdmenu.raas.tab1.page2.Tab1RaasMenuPage;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.bsm.BsmUitvoerenForm;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.bsm.BsmUitvoerenWindow;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.beheer.profiel.actie.ProfielActie;
import nl.procura.raas.rest.domain.aanvraag.DeleteAanvraagRequest;
import nl.procura.vaadin.component.layout.info.InfoLayout;
import nl.procura.vaadin.component.layout.page.pageEvents.AfterReturn;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;

public class Tab1RaasPage1 extends NormalPageTemplate {

  private Tab1Page1RaasLayout raasLayout;

  public Tab1RaasPage1() {
    super("Reisdocumentaanvragen");
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      raasLayout = new Tab1Page1RaasLayout() {

        @Override
        public void selectRecord(Record record) {
          Tab1RaasPage1.this.selectRecord(record);
        }

        @Override
        public void infoMessage(String msg) {
          Tab1RaasPage1.this.infoMessage(msg);
        }

        @Override
        public Services getServices() {
          return Tab1RaasPage1.this.getServices();
        }
      };

      if (getServices().getRaasService().isRaasServiceActive()) {

        addComponent(raasLayout.getPagingLayout());
        buttonSave.setCaption("Start verwerking (F9)");

        OptieLayout optieLayout = new OptieLayout();
        optieLayout.getRight().setWidth("160px");
        optieLayout.getLeft().addExpandComponent(raasLayout.getTable());
        optieLayout.getRight().addButton(buttonSave, this);

        if (getApplication().isProfielActie(ProfielActie.DELETE_HOOFD_INZAGE_RAAS)) {
          optieLayout.getRight().addButton(buttonDel, this);
        }

        addExpandComponent(optieLayout);

      } else {
        addComponent(new InfoLayout("Ter informatie", "De RAAS service is niet geactiveerd in de parameters."));
      }

      raasLayout.search();
    }

    if (event.isEvent(AfterReturn.class)) {
      raasLayout.search();
    }

    super.event(event);
  }

  @Override
  public void onSave() {

    BsmTaak bsmTaak = GbaBsmTaak.PROBEV_ZAAK_REISDOCUMENT_1_0;
    BsmRestTaak taak = getServices().getBsmService().getBsmRestTaak(bsmTaak);
    BsmRestTaakVraag bsmVraag = new BsmRestTaakVraag(taak.getTaak());

    BsmUitvoerenForm form = new BsmUitvoerenForm("Betreft", true, TAAK);
    form.getBean().setTaak(taak.getOmschrijving());

    getWindow().addWindow(new BsmUitvoerenWindow(form, bsmTaak, bsmVraag) {

      @Override
      protected void reload() {
        onSearch();
      }
    });
  }

  @Override
  public void onSearch() {
    raasLayout.search();
  }

  @Override
  public void onDelete() {
    if (getServices().getRaasService().isRaasServiceActive()) {
      new DeleteProcedure<DocAanvraagDto>(raasLayout.getTable()) {

        @Override
        public void afterDelete() {
          raasLayout.search();
        }

        @Override
        public void deleteValue(DocAanvraagDto message) {
          Long aanvraagNummer = message.getAanvraagNr().getValue();
          DeleteAanvraagRequest request = new DeleteAanvraagRequest(aanvraagNummer);
          getServices().getRaasService().delete(request);
        }
      };
    }
  }

  @Override
  public void onEnter() {
    raasLayout.search();
  }

  private void selectRecord(Record record) {
    DocAanvraagDto aanvraag = record.getObject(DocAanvraagDto.class);
    getNavigation().goToPage(new Tab1RaasMenuPage(aanvraag));
  }
}
