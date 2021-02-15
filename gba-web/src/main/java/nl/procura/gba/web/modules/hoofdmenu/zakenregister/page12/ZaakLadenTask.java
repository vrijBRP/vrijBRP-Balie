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

package nl.procura.gba.web.modules.hoofdmenu.zakenregister.page12;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;

import nl.procura.gba.config.GbaConfig;
import nl.procura.gba.jpa.personen.utils.GbaJpaStorageWrapper;
import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.ZakenService;
import nl.procura.standard.threadlocal.ThreadLocalStorage;
import nl.procura.vaadin.functies.task.VaadinTask;
import nl.procura.vaadin.functies.task.VaadinTaskLogger;

public class ZaakLadenTask implements VaadinTask {

  private final GbaApplication application;
  private final List<Zaak>     zaken;
  private boolean              success = false;
  private boolean              failed  = false;
  private boolean              abort   = false;
  private double               percentage;

  public ZaakLadenTask(GbaApplication application, List<Zaak> zaken) {
    this.application = application;
    this.zaken = zaken;
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
      ZakenService service = services.getZakenService();

      int index = 0;

      for (Zaak zaak : zaken) {

        if (abort) {
          continue;
        }

        service.getVolledigeZaak(zaak);

        index++;

        setPercentage((100.00 / zaken.size()) * index);
      }

      success = true;
      setPercentage(100.00);
    } catch (Exception e) {
      failed = true;
      e.printStackTrace();
      doStopNow();
    }
  }

  @Override
  public List<VaadinTaskLogger> getLoggers() {
    List<VaadinTaskLogger> loggers = new ArrayList<>();
    loggers.add(new VaadinTaskLogger(ZaakLadenTask.class.getSimpleName(), Level.DEBUG));
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

  @Override
  public void onDone() {
    System.out.println("Done!!!");
  }
}
