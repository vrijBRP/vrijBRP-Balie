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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.bijlage.page1;

import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.components.dialogs.DeleteProcedure;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.tablefilter.GbaIndexedTableFilterLayout;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.bijlage.BijlageReferentieType;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.bijlage.BijlageReferentieTypeContainer;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.bijlage.ZaakBestandenUploadWindow;
import nl.procura.gba.web.modules.zaken.document.DocumentenTabel;
import nl.procura.gba.web.services.beheer.profiel.actie.ProfielActie;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.dms.DMSDocument;
import nl.procura.gba.web.services.zaken.algemeen.dms.DMSResult;
import nl.procura.standard.exceptions.ProException;
import nl.procura.standard.exceptions.ProExceptionSeverity;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public abstract class Page1ZaakBijlage extends NormalPageTemplate {

  private BijlageReferentieType       type   = BijlageReferentieType.ZAAK;
  private final Zaak                  zaak;
  private DocumentenTabel             table  = null;
  private GbaIndexedTableFilterLayout filter = null;

  public Page1ZaakBijlage(Zaak zaak) {
    this.zaak = zaak;
    setSpacing(true);
    setMargin(false);
  }

  @Override
  public void event(PageEvent event) {
    if (event.isEvent(InitPage.class)) {
      if (getApplication().isProfielActie(ProfielActie.UPDATE_ZAAK_DOCUMENT_ARCHIEF)) {
        addButton(buttonNew);
      }
      if (getApplication().isProfielActie(ProfielActie.DELETE_ZAAK_DOCUMENT_ARCHIEF)) {
        addButton(buttonDel);
      }

      setInfo("Bijlagen", "De documenten die zijn gekoppeld aan de zaak");

      // Tabel
      table = new DocumentenTabel() {

        @Override
        public DMSResult getOpgeslagenBestanden() {
          return getBestanden(getApplication());
        }
      };

      // Select Field
      GbaNativeSelect select = new GbaNativeSelect();
      select.setNullSelectionAllowed(false);
      select.setImmediate(true);
      select.setContainerDataSource(new BijlageReferentieTypeContainer());
      select.addListener((ValueChangeListener) event1 -> {
        type = (BijlageReferentieType) event1.getProperty().getValue();
        table.init();
      });

      // Filter
      filter = new GbaIndexedTableFilterLayout(table);
      Label label = new Label();
      filter.setWidth("100%");

      filter.addComponent(label, 0);
      filter.addComponent(select, 3);
      filter.setComponentAlignment(select, Alignment.MIDDLE_CENTER);
      filter.setComponentAlignment(label, Alignment.BOTTOM_LEFT);
      filter.setExpandRatio(label, 1f);

      getButtonLayout().addComponent(filter);
      addComponent(table);
    }

    super.event(event);
  }

  public String getHeader(GbaApplication application) {
    return "Bijlagen (" + getBestanden(application).size() + ")";
  }

  @Override
  public void onDelete() {

    if (buttonDel.getParent() == null) {
      return;
    }

    new DeleteProcedure<DMSDocument>(table) {

      @Override
      public void afterDelete() {
        onReload();
      }

      @Override
      public void deleteValue(DMSDocument record) {
        getApplication().getServices().getDmsService().delete(record);
      }
    };
  }

  @Override
  public void onNew() {

    if (zaak.getStatus().isEindStatus()) {
      throw new ProException(ProExceptionSeverity.WARNING, "Bij zaken met een eindstatus kunnen geen " +
          "nieuwe documenten worden geupload");
    }

    if (buttonNew.getParent() == null) {
      return;
    }

    getParentWindow().addWindow(new ZaakBestandenUploadWindow(zaak) {

      @Override
      public void closeWindow() {
        table.init();
        onReload();
        super.closeWindow();
      }

      @Override
      protected void uploadSuccess() {
        table.init();
        onReload();
      }
    });
  }

  public abstract void onReload();

  private DMSResult getBestanden(GbaApplication application) {
    if (type == BijlageReferentieType.ZAAK) {
      return application.getServices().getDmsService().getDocumentsByZaak(zaak);
    }
    return application.getServices().getDmsService().getDocumentsByPL(zaak.getBasisPersoon());
  }
}
