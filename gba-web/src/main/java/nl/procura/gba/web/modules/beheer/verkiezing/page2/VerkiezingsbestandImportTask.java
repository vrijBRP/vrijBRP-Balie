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

package nl.procura.gba.web.modules.beheer.verkiezing.page2;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.ListUtils;
import org.apache.log4j.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.procura.gba.config.GbaConfig;
import nl.procura.gba.jpa.personen.db.KiesrStem;
import nl.procura.gba.jpa.personen.utils.GbaJpaStorageWrapper;
import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.beheer.verkiezing.Bestandsregel;
import nl.procura.gba.web.services.beheer.verkiezing.KiezersregisterService;
import nl.procura.gba.web.services.beheer.verkiezing.Verkiezingsbestand;
import nl.procura.gba.web.services.zaken.algemeen.controle.ControlesListener;
import nl.procura.standard.threadlocal.ThreadLocalStorage;
import nl.procura.vaadin.functies.task.VaadinTask;
import nl.procura.vaadin.functies.task.VaadinTaskLogger;

public class VerkiezingsbestandImportTask implements VaadinTask {

  private final static Logger LOGGER = LoggerFactory.getLogger(VerkiezingsbestandImportTask.class.getName());

  private final GbaApplication     application;
  private final Verkiezingsbestand verkiezingsbestand;

  private boolean success = false;
  private boolean abort;
  private double  percentage;

  public VerkiezingsbestandImportTask(GbaApplication application, Verkiezingsbestand verkiezingsbestand) {
    this.application = application;
    this.verkiezingsbestand = verkiezingsbestand;
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
      KiezersregisterService service = services.getKiezersregisterService();
      int added = 0;
      int skipped = 0;
      int nr = 0;
      List<Bestandsregel> nieuweRegels = verkiezingsbestand.getNieuweRegels();
      List<Bestandsregel> bestaandeRegels = verkiezingsbestand.getBestaandeRegels();
      if (nieuweRegels.isEmpty() && !bestaandeRegels.isEmpty()) {
        LOGGER.info("Alle " + bestaandeRegels.size() + " regels in dit bestand zijn al verwerkt.");
      } else {
        LIST: for (List<Bestandsregel> partition : ListUtils.partition(nieuweRegels, 500)) {
          List<KiesrStem> kiezers = new ArrayList<>();
          for (Bestandsregel regel : partition) {
            if (abort) {
              break LIST;
            }
            try {
              kiezers.add(service.toKiezerPers(verkiezingsbestand.getVerkiezing(), regel));
              added++;
            } catch (Exception e) {
              skipped++;
              LOGGER.debug("Fout bij verwerken regel " + regel.getPasNr() + ": " + e.getMessage());
            }
            nr++;
            setPercentage((100.00 / nieuweRegels.size()) * nr);
          }
          service.save(kiezers);
        }
        LOGGER.info(toRegel(added) + " verwerkt.");
        LOGGER.info(toRegel(nieuweRegels.size() - added) + " nog niet verwerkt.");
        LOGGER.info(toRegel(skipped) + " overgeslagen wegens een fout.");
      }
      success = true;
      setPercentage(100.00);
    } catch (Exception e) {
      e.printStackTrace();
      LOGGER.error("ERROR: De verwerking is afgebroken!");
    }
  }

  private String toRegel(int added) {
    return added == 1 ? "Er is 1 regel" : "Er zijn " + added + " regels";
  }

  @Override
  public List<VaadinTaskLogger> getLoggers() {
    List<VaadinTaskLogger> loggers = new ArrayList<>();
    loggers.add(new VaadinTaskLogger(VerkiezingsbestandImportTask.class.getName(), Level.DEBUG));
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
    return false;
  }

  @Override
  public boolean isSuccess() {
    return success;
  }

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
