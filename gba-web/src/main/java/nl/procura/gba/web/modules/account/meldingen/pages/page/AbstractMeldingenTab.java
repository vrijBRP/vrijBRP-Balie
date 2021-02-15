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

package nl.procura.gba.web.modules.account.meldingen.pages.page;

import static nl.procura.gba.web.services.applicatie.meldingen.ServiceMeldingIds.GBA_V_BLOK;
import static nl.procura.gba.web.services.applicatie.meldingen.ServiceMeldingIds.GBA_V_VERLOOP;
import static nl.procura.gba.web.services.gba.templates.ZoekProfielType.PROFIEL_STANDAARD;
import static nl.procura.standard.exceptions.ProExceptionSeverity.ERROR;
import static nl.procura.standard.exceptions.ProExceptionSeverity.WARNING;

import java.util.List;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CheckBox;

import nl.procura.gba.web.components.TableImage;
import nl.procura.gba.web.components.dialogs.DeleteProcedure;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.account.meldingen.pages.page.stacktrace.StacktraceWindow;
import nl.procura.gba.web.modules.beheer.onderhoud.gbav.GbavWindow;
import nl.procura.gba.web.modules.beheer.onderhoud.gbav.GbavWindowListener;
import nl.procura.gba.web.modules.beheer.onderhoud.rdw.RdwWindow;
import nl.procura.gba.web.modules.beheer.onderhoud.rdw.RdwWindowListener;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.applicatie.meldingen.MeldingService;
import nl.procura.gba.web.services.applicatie.meldingen.ServiceMelding;
import nl.procura.gba.web.services.applicatie.meldingen.types.GbavMelding;
import nl.procura.gba.web.services.applicatie.meldingen.types.RdwMelding;
import nl.procura.gba.web.services.applicatie.meldingen.types.SelectieMelding;
import nl.procura.gba.web.services.gba.ple.PersonenWsService;
import nl.procura.gba.web.windows.home.HomeWindow;
import nl.procura.gbaws.web.rest.v1_0.gbav.account.GbaWsRestGbavAccount;
import nl.procura.vaadin.component.table.indexed.IndexedTable.Record;
import nl.procura.vaadin.functies.downloading.DownloadHandlerImpl;
import nl.procura.vaadin.theme.twee.Icons;

public abstract class AbstractMeldingenTab extends NormalPageTemplate {

  protected final Button buttonExport = new Button("Meldingen exporteren");
  protected GbaTable     table;

  public AbstractMeldingenTab() {
  }

  @Override
  public void onClose() {
    getWindow().closeWindow();
  }

  @Override
  public void onDelete() {
    new DeleteProcedure<ServiceMelding>(table, true) {

      @Override
      public void deleteValue(ServiceMelding melding) {
        getServices().getMeldingService().delete(melding);
      }
    };
  }

  @Override
  public void onEnter() {
    if (table.getRecord() != null) {
      selectRecord(table.getRecord());
    }
  }

  @Override
  public void handleEvent(Button button, int keyCode) {
    if (button == buttonExport) {
      onExport();
    }

    super.handleEvent(button, keyCode);
  }

  protected CheckBox getNotificationsCheckbox() {
    CheckBox checkBox = new CheckBox("Dit scherm tonen als er meldingen zijn tijdens inloggen");
    checkBox.setImmediate(true);
    MeldingService meldingService = getApplication().getServices().getMeldingService();
    checkBox.addListener((ClickListener) event -> {
      boolean value = (boolean) event.getButton().getValue();
      meldingService.enableShowMessagesPopup(value);
    });
    checkBox.setValue(meldingService.isShowMessagesPopup());
    return checkBox;
  }

  protected CheckBox getOnlyUserMessageCheckbox() {
    CheckBox checkBox = new CheckBox("Toon alleen mijn meldingen");
    checkBox.setImmediate(true);
    MeldingService meldingService = getApplication().getServices().getMeldingService();
    checkBox.addListener((ClickListener) event -> {
      boolean value = (boolean) event.getButton().getValue();
      meldingService.enableShowOnlyUser(value);
    });
    checkBox.setValue(meldingService.isShowOnlyUser());
    return checkBox;
  }

  protected TableImage getMeldingIcon(ServiceMelding m) {
    if (m.getSeverity() == ERROR) {
      return new TableImage(Icons.getIcon(Icons.ICON_ERROR));
    } else if (m.getSeverity() == WARNING) {
      return new TableImage(Icons.getIcon(Icons.ICON_WARN));
    } else {
      return new TableImage(Icons.getIcon(Icons.ICON_OK));
    }
  }

  protected void selectRecord(Record record) {
    ServiceMelding melding = record.getObject(ServiceMelding.class);
    if (melding instanceof RdwMelding) {
      rdwMelding(melding);
    } else if (melding instanceof GbavMelding) {
      gbavMelding(melding);
    } else if (melding instanceof SelectieMelding) {
      selectieMelding();
    } else {
      getApplication().getParentWindow().addWindow(new StacktraceWindow(record.getObject(ServiceMelding.class)));
    }
  }

  private void onExport() {
    getServices().getMeldingService().export(new DownloadHandlerImpl(getParentWindow()));
  }

  private void selectieMelding() {
    getApplication().openWindow(getApplication().getCurrentWindow(), new HomeWindow(), "?tab=selecties#zaken");
    getApplication().closeAllModalWindows(getApplication().getWindows());
  }

  private void gbavMelding(ServiceMelding melding) {

    GbavMelding gbavNotification = (GbavMelding) melding;
    GbavWindowListener gbavWindowListener = new GbavWindowListener() {

      @Override
      public void update() {
        checkGbavAccounts(getServices());
      }

      private void checkGbavAccounts(final Services services) {

        PersonenWsService personenWsService = services.getPersonenWsService();
        List<GbaWsRestGbavAccount> accounts = personenWsService.getGbavAccounts(PROFIEL_STANDAARD);

        for (GbaWsRestGbavAccount account : accounts) {

          if (!account.isGeblokkeerd()) {
            String id = GBA_V_BLOK + account.getCode();
            services.getMeldingService().delete(id);
          }

          if (account.getDagenGeldig() >= 10) {
            String id = GBA_V_VERLOOP + account.getCode();
            services.getMeldingService().delete(id);
          }
        }
      }
    };

    getApplication().getParentWindow().addWindow(new GbavWindow(gbavNotification.getAccount(), gbavWindowListener));
  }

  private void rdwMelding(ServiceMelding melding) {

    RdwWindowListener rdwWindowListener = new RdwWindowListener() {

      @Override
      public void update() {
        checkGbavAccounts(getServices());
      }

      private void checkGbavAccounts(final Services services) {

        PersonenWsService personenWsService = services.getPersonenWsService();
        List<GbaWsRestGbavAccount> accounts = personenWsService.getGbavAccounts(PROFIEL_STANDAARD);

        for (GbaWsRestGbavAccount account : accounts) {

          if (!account.isGeblokkeerd()) {
            String id = GBA_V_BLOK + account.getCode();
            services.getMeldingService().delete(id);
          }

          if (account.getDagenGeldig() >= 10) {
            String id = GBA_V_VERLOOP + account.getCode();
            services.getMeldingService().delete(id);
          }
        }
      }
    };

    getApplication().getParentWindow().addWindow(new RdwWindow(rdwWindowListener));
  }
}
