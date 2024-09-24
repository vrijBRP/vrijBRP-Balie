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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.page9;

import static nl.procura.standard.Globalfunctions.pad_right;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.procura.gba.config.GbaConfig;
import nl.procura.gba.jpa.personen.utils.GbaJpaStorageWrapper;
import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.zaken.algemeen.ControleerbareService;
import nl.procura.gba.web.services.zaken.algemeen.controle.Controles;
import nl.procura.gba.web.services.zaken.algemeen.controle.ControlesListener;
import nl.procura.standard.threadlocal.ThreadLocalStorage;
import nl.procura.vaadin.functies.task.VaadinTask;
import nl.procura.vaadin.functies.task.VaadinTaskLogger;

public abstract class ServiceControlesTask implements VaadinTask {

  private final static Logger  LOGGER  = LoggerFactory.getLogger(ServiceControlesTask.class.getName());
  private              boolean failed  = false;
  private final GbaApplication application;
  private final Controles      controles;
  private boolean              success = false;
  private double               percentage;
  private boolean              abort;

  public ServiceControlesTask(GbaApplication application, Controles controles) {
    this.application = application;
    this.controles = controles;
  }

  @Override
  public void doStopNow() {
    abort = true;
  }

  @Override
  public void execute() {

    try {
      ThreadLocalStorage.init(new GbaJpaStorageWrapper(GbaConfig.getProperties()));

      Services services = application.getServices();
      List<ControleerbareService> dbs = services.getServices(ControleerbareService.class);

      int i = 0;

      LOGGER.debug("Aantal: " + dbs.size());

      success = false;
      failed = false;

      for (ControleerbareService db : dbs) {

        if (abort) {
          continue;
        }

        long st = System.currentTimeMillis();

        try {
          LOGGER.debug(pad_right("", "-", 50));
          LOGGER.debug("Controle: " + db.getName());
          LOGGER.debug(pad_right("", "-", 50));

          ControleListener controleListener = new ControleListener();
          Controles dbControles = db.getControles(controleListener);

          LOGGER.debug("Aantal controles uitgevoerd: " + dbControles.size());
          LOGGER.debug("Aantal wijzigingen uitgevoerd: " + dbControles.getGewijzigdeControles().size());

          controles.addAll(dbControles);

          i++;

          setPercentage((100.00 / dbs.size()) * i);
          long et = System.currentTimeMillis();
          LOGGER.debug("Tijd: " + (et - st) + " ms.");

        } catch (Exception e) {
          LOGGER.error("Fout bij controle: " + db.getName(), e);
          failed = true;
        }
      }

      setPercentage(100.00);

      if (!failed) {
        success = true;
      }

    } finally {
      onFinished();
    }
  }

  @Override
  public List<VaadinTaskLogger> getLoggers() {
    List<VaadinTaskLogger> loggers = new ArrayList<>();
    loggers.add(new VaadinTaskLogger(ServiceControlesTask.class.getName(), Level.DEBUG));
    return loggers;
  }

  @Override
  public double getPercentage() {
    return percentage;
  }

  public void setPercentage(double percentage) {
    this.percentage = percentage;
  }

  @Override
  public double getSubPercentage() {
    return 0;
  }

  @Override
  public String getTaskActivityLabel() {
    return "";
  }

  public boolean isAskedToStop() {
    return Thread.currentThread().isInterrupted();
  }

  @Override
  public boolean isFailed() {
    return failed;
  }

  @Override
  public boolean isSuccess() {
    return success;
  }

  public abstract void onFinished();

  @Override
  public void onDone() {
    System.out.println("Done!!!");
  }

  public static class ControleListener implements ControlesListener {

    @Override
    public void info(String message) {
      LOGGER.debug(message);
    }
  }
}
