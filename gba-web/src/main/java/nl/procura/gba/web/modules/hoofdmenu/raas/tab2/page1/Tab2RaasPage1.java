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

package nl.procura.gba.web.modules.hoofdmenu.raas.tab2.page1;

import static nl.procura.gba.web.modules.hoofdmenu.raas.tab1.page2.subpages.Tab1RaasPage2Bean.AANVRAAGNUMMER;
import static nl.procura.standard.exceptions.ProExceptionSeverity.INFO;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.vaadin.ui.Button;

import nl.procura.dto.raas.aanvraag.DocAanvraagDto;
import nl.procura.dto.raas.bestand.RaasBestandDto;
import nl.procura.gba.web.components.dialogs.DeleteProcedure;
import nl.procura.gba.web.components.layouts.OptieLayout;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.modules.hoofdmenu.raas.tab1.page2.subpages.Tab1RaasPage2Form;
import nl.procura.gba.web.modules.hoofdmenu.raas.tab2.page2.Tab2RaasPage2;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.beheer.profiel.actie.ProfielActie;
import nl.procura.raas.rest.domain.ResponseMessage;
import nl.procura.raas.rest.domain.raas.bestand.DeleteRaasBestandRequest;
import nl.procura.raas.rest.domain.raas.bestand.ProcessRaasBestandRequest;
import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.info.InfoLayout;
import nl.procura.vaadin.component.layout.page.pageEvents.AfterReturn;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;

public class Tab2RaasPage1 extends NormalPageTemplate {

  protected final Button buttonTest     = new Button("Test verbinding");
  protected final Button buttonDownload = new Button("Berichten ophalen");

  private Tab2RaasPage1Layout raasLayout;
  private DocAanvraagDto      aanvraag;

  public Tab2RaasPage1() {
    super("Berichten van en naar het RAAS");
  }

  public Tab2RaasPage1(DocAanvraagDto aanvraag) {
    this.aanvraag = aanvraag;
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      raasLayout = new Tab2RaasPage1Layout(aanvraag) {

        @Override
        public void selectRecord(Record record) {
          Tab2RaasPage1.this.selectRecord(record);
        }

        @Override
        public void infoMessage(String msg) {
          Tab2RaasPage1.this.infoMessage(msg);
        }

        @Override
        public Services getServices() {
          return Tab2RaasPage1.this.getServices();
        }
      };

      if (getServices().getRaasService().isRaasServiceActive()) {

        if (getWindow().isModal()) {
          addButton(buttonClose);
        }

        if (aanvraag != null) {
          addComponent(new Fieldset("Reisdocumentaanvraag"));
          addComponent(new Form(aanvraag));
          addComponent(new Fieldset("Berichten van en naar het RAAS"));
        }

        addComponent(raasLayout.getPagingLayout());
        buttonNext.setCaption("Verwerk bericht (F2)");

        OptieLayout optieLayout = new OptieLayout();
        optieLayout.getRight().setWidth("160px");

        optieLayout.getLeft().addExpandComponent(raasLayout.getTable());
        optieLayout.getRight().addButton(buttonNext, this);
        optieLayout.getRight().addButton(buttonDownload, this);

        if (getApplication().isProfielActie(ProfielActie.DELETE_HOOFD_INZAGE_RAAS)) {
          optieLayout.getRight().addButton(buttonDel, this);
        }

        optieLayout.getRight().addButton(buttonTest, this);
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
  public void handleEvent(Button button, int keyCode) {
    if (button == buttonTest) {
      onTest();
    }
    if (button == buttonDownload) {
      onDownload();
    }
    super.handleEvent(button, keyCode);
  }

  private void onDownload() {
    process(Collections.singletonList(-1));
  }

  private void onTest() {
    getServices().getRaasService().test();
    successMessage("De verbinding is met het RAAS werkt");
  }

  @Override
  public void onClose() {
    getWindow().closeWindow();
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }

  @Override
  public void onSearch() {
    raasLayout.search();
  }

  @Override
  public void onDelete() {
    if (getServices().getRaasService().isRaasServiceActive()) {
      new DeleteProcedure<RaasBestandDto>(raasLayout.getTable()) {

        @Override
        public void afterDelete() {
          raasLayout.search();
        }

        @Override
        public void deleteValue(RaasBestandDto bestand) {
          DeleteRaasBestandRequest request = new DeleteRaasBestandRequest(bestand.getId().getValue());
          getServices().getRaasService().delete(request);
        }
      };
    }
  }

  @Override
  public void onNextPage() {

    if (getServices().getRaasService().isRaasServiceActive()) {
      List<RaasBestandDto> records = raasLayout.getTable().getSelectedValues(RaasBestandDto.class);
      if (records.isEmpty()) {
        throw new ProException(INFO, "Geen records geselecteerd");
      }

      process(records.stream().map(r -> r.getId().getValue()).collect(Collectors.toList()));
    }
  }

  @Override
  public void onEnter() {
    raasLayout.search();
  }

  private void selectRecord(Record record) {
    RaasBestandDto bestand = record.getObject(RaasBestandDto.class);
    if (aanvraag == null) {
      getNavigation().goToPage(new Tab2RaasPage2(bestand));
    } else {
      getNavigation().goToPage(new Tab2RaasPage2(aanvraag, bestand));
    }
  }

  private void process(List<Integer> ids) {
    try {
      ProcessRaasBestandRequest request = new ProcessRaasBestandRequest();
      request.setIds(ids);
      List<ResponseMessage> messages = getServices().getRaasService().process(request);
      if (messages.isEmpty()) {
        infoMessage("Geen berichten opgehaald of verwerkt");
      } else {
        if (messages.size() == 1) {
          successMessage("1 bericht opgehaald of verwerkt");
        } else {
          successMessage(messages.size() + " berichten opgehaald of verwerkt");
        }
      }
    } finally {
      raasLayout.search();
    }
  }

  private class Form extends Tab1RaasPage2Form {

    public Form(DocAanvraagDto documentAanvraag) {
      super(documentAanvraag);
      setColumnWidths("200px", "");
    }

    @Override
    protected void createFields() {
      setOrder(AANVRAAGNUMMER);
    }
  }
}
