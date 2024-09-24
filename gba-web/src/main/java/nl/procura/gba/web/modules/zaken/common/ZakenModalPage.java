/*
 * Copyright 2024 - 2025 Procura B.V.
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

package nl.procura.gba.web.modules.zaken.common;

import static nl.procura.standard.Globalfunctions.emp;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.ERROR;

import java.text.MessageFormat;

import com.vaadin.ui.Button;

import nl.procura.gba.web.components.dialogs.DeleteProcedure;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.aantekening.ZaakAantekeningWindow;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.status.ZaakStatusUpdater;
import nl.procura.gba.web.modules.zaken.common.ZakenListTable.ZaakRecord;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.vaadin.functies.VaadinUtils;

public class ZakenModalPage<Z extends Zaak> extends NormalPageTemplate {

  protected final ButtonStatus buttonStatus = new ButtonStatus();
  protected final Button       buttonAant   = new Button("Aantekeningen");
  private Zaak                 zaak         = null;

  public ZakenModalPage(String title) {
    super(title);
  }

  public ZakenModalPage(Zaak zaak, String title) {
    super(title);
    this.zaak = zaak;
  }

  public Z getZaak() {
    return (Z) zaak;
  }

  public void setZaak(Zaak zaak) {
    this.zaak = zaak;
  }

  @Override
  public void handleEvent(Button button, int keyCode) {
    if (button == buttonStatus) {
      onStatus();
    } else if (button == buttonAant) {
      onAantekening();
    }

    super.handleEvent(button, keyCode);
  }

  public void onAfterDelete() {
  }

  @Override
  public void onDelete() {

    final GbaTable table = VaadinUtils.getChild(this, GbaTable.class);

    new DeleteProcedure<Object>(table) {

      @Override
      public void afterDelete() {
        onAfterDelete();
      }

      @Override
      public void deleteValue(Object zaakObject) {
        if (zaakObject instanceof Zaak) {
          Zaak zaak = (Zaak) zaakObject;
          getServices().getZakenService().getService(zaak).delete(zaak);
        }
        if (zaakObject instanceof ZaakRecord) {
          Zaak zaak = ((ZaakRecord) zaakObject).getZaak();
          getServices().getZakenService().getService(zaak).delete(zaak);
        }
      }
    };
  }

  private void onAantekening() {
    if (emp(getZaak().getZaakId())) {
      throw new ProException(ERROR, "Er kan geen aantekening worden gemaakt, " +
          "omdat deze zaak nog geen zaak-id heeft");
    }

    getWindow().addWindow(new ZaakAantekeningWindow(getZaak(), () -> {
      Services services = getApplication().getServices();
      int countAantekeningen = services.getAantekeningService().getZaakAantekeningen(getZaak()).size();
      buttonAant.setCaption(MessageFormat.format("Aantekeningen ({0})", countAantekeningen));
    }));
  }

  private void onStatus() {

    final GbaTable table = VaadinUtils.getChild(this, GbaTable.class);

    new ZaakStatusUpdater(table) {

      @Override
      protected void reload() {
        table.init();
      }
    };
  }
}
