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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.bsm;

import static nl.procura.gba.common.MiscUtils.setClass;
import static nl.procura.gba.common.MiscUtils.to;
import static nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.bsm.BsmUitvoerenBean.*;

import java.util.ArrayList;
import java.util.List;

import com.github.wolfie.refresher.Refresher;
import com.github.wolfie.refresher.Refresher.RefreshListener;
import com.vaadin.ui.Alignment;

import nl.procura.bsm.rest.v1_0.objecten.log.BsmRestLog;
import nl.procura.bsm.rest.v1_0.objecten.taak.BsmRestTaak;
import nl.procura.bsm.rest.v1_0.objecten.taak.BsmRestTaakLog;
import nl.procura.bsm.rest.v1_0.objecten.taak.BsmRestTaakStatus;
import nl.procura.bsm.rest.v1_0.objecten.taak.BsmRestTaakVraag;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.modules.hoofdmenu.zakenregister.overig.bsm.log.BsmLogWindow;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public abstract class BsmUitvoerenPage extends NormalPageTemplate {

  private static final long      REFRESH_INTERVAL = 1000;
  private final BsmUitvoerenBean progressBean     = new BsmUitvoerenBean();
  private final ProgressLabel    progressLabel    = new ProgressLabel();
  private final Refresher        refresher        = new Refresher();
  private List<BsmRestLog>       logs             = new ArrayList<>();
  private BsmUitvoerenForm       progressForm     = null;
  private String                 sessie;
  private boolean                isDone           = false;
  private Exception              exception        = null;

  private GbaForm betreftForm;

  public BsmUitvoerenPage() {
    super("");
  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      buttonNext.setCaption("Toon log (F2)");
      buttonNext.setEnabled(false);

      addButton(buttonNext);
      addButton(buttonClose);

      getButtonLayout().addComponent(progressLabel);
      getButtonLayout().setSizeFull();
      getButtonLayout().setComponentAlignment(buttonNext, Alignment.MIDDLE_LEFT);
      getButtonLayout().setComponentAlignment(buttonClose, Alignment.MIDDLE_LEFT);
      getButtonLayout().setComponentAlignment(progressLabel, Alignment.MIDDLE_RIGHT);
      getButtonLayout().setExpandRatio(progressLabel, 1f);

      betreftForm = getBetreftForm();
      progressForm = new BsmUitvoerenForm("Verwerking van de berichten service module", false, STATUS, RESULTAAT,
          MELDING);

      startVerwerking();
      addRefresher();

      addComponent(betreftForm);
      addComponent(progressForm);
    }

    super.event(event);

    buttonClose.focus();
  }

  public abstract GbaForm getBetreftForm();

  public abstract BsmRestTaak getBsmRestTaak();

  public abstract BsmRestTaakVraag getBsmVraag();

  public BsmUitvoerenBean getProgressBean() {
    return progressBean;
  }

  public synchronized String getSessie() {
    return sessie;
  }

  public synchronized void setSessie(String sessie) {
    this.sessie = sessie;
  }

  @Override
  public void onClose() {
    to(getWindow(), GbaModalWindow.class).closeWindow();
    super.onClose();
  }

  @Override
  public void onNextPage() {

    if (logs != null && !logs.isEmpty()) {
      getParentWindow().addWindow(new BsmLogWindow(logs));
    }

    super.onNextPage();
  }

  public abstract void reloadZaak();

  protected void addRefresher() {

    refresher.addListener(new ActionRefreshListener());
    refresher.setRefreshInterval(REFRESH_INTERVAL);
    addComponent(refresher);
  }

  protected void onRefresh() {

    try {
      progressBean.setResultaat("");
      progressBean.setMelding("");

      if (exception != null) {

        progressBean.set("Onbekend", "Niets uitgevoerd", exception.getMessage(), false);
        progressLabel.refresh(BsmRestTaakStatus.ONDERBROKEN);
        removeComponent(refresher);
      } else {

        BsmRestTaak bsmRestTaak = getBsmRestTaak();

        if (bsmRestTaak == null) {

          progressBean.set("Onbekend", "Niets uitgevoerd", "Geen taak gevonden die deze zaak kan verwerken.",
              false);
          progressLabel.refresh(BsmRestTaakStatus.ONDERBROKEN);
          isDone = true;
        } else {

          // Bsm taak WEL gevonden
          progressLabel.refresh(bsmRestTaak.getStatus());

          if (!isDone) {

            if (bsmRestTaak.getStatus() == BsmRestTaakStatus.GEBLOKKEERD) {
              progressBean.setStatus("Bezig ...");
            } else {

              if (bsmRestTaak.getStatus() == BsmRestTaakStatus.ONDERBROKEN) {
                if (bsmRestTaak.isUitgezet()) {
                  progressBean.setStatus("Uitgezet");
                } else {
                  progressBean.setStatus(setClass(false, "Onderbroken"));
                }
              } else if (bsmRestTaak.getStatus() == BsmRestTaakStatus.NORMAAL) {
                progressBean.setStatus("Normaal");
              }

              isDone = true;
            }
          } else {

            verwerkTaakLogs(getServices().getBsmService().getBsmTaakLog(sessie));

            progressLabel.refresh(BsmRestTaakStatus.VOLTOOID);

            reloadZaak();
          }
        }
      }
    } finally {
      betreftForm.setBean(progressBean);
      progressForm.setBean(progressBean);
    }
  }

  private void startVerwerking() {

    try {
      logs = new ArrayList<>();
      BsmRestTaak bsmRestTaak = getBsmRestTaak();

      if (bsmRestTaak != null) {
        progressBean.setTaak(bsmRestTaak.getOmschrijving());
        setSessie(getServices().getBsmService().uitvoeren(getBsmVraag()));
      }
    } catch (Exception e) {
      this.exception = e;
    } finally {
      onRefresh();
    }
  }

  private void verwerkTaakLogs(List<BsmRestTaakLog> taakLogs) {

    if (taakLogs.isEmpty()) {

      progressBean.setResultaat("Niets uitgevoerd");
      progressBean.setMelding("Geen zaken gevonden die verwerkt kunnen worden.");
    } else {

      buttonNext.setEnabled(true);

      for (BsmRestTaakLog taakLog : taakLogs) {

        logs = taakLog.getLog().getSubLogs();

        switch (taakLog.getLog().getResultaat()) {
          case FOUT:
            progressBean.setResultaat(setClass(false, "Fout"));
            progressBean.setMelding(setClass(false, taakLog.getLog().getEersteFout()));
            break;

          case SUCCES:
            progressBean.setResultaat(setClass(true, "Succes"));
            progressBean.setMelding(setClass(true, "De verwerking is gelukt."));
            break;

          case WAARSCHUWING:
            progressBean.setResultaat(setClass(true, "Onbekend"));
            progressBean.setMelding(setClass(true, ""));
            break;

          default:
          case INFO:
          case ONBEKEND:
        }
      }
    }

    removeComponent(refresher);
  }

  private class ActionRefreshListener implements RefreshListener {

    @Override
    public void refresh(Refresher source) {
      onRefresh();
    }
  }
}
