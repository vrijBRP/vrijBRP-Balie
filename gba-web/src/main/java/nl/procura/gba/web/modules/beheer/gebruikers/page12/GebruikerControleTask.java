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

package nl.procura.gba.web.modules.beheer.gebruikers.page12;

import static nl.procura.standard.Globalfunctions.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.procura.gba.config.GbaConfig;
import nl.procura.gba.jpa.personen.utils.GbaJpaStorageWrapper;
import nl.procura.gba.web.modules.beheer.gebruikers.page12.Page12Gebruikers.Check;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.gba.web.services.beheer.gebruiker.GebruikerService;
import nl.procura.gba.web.services.interfaces.GeldigheidStatus;
import nl.procura.standard.threadlocal.ThreadLocalStorage;
import nl.procura.vaadin.functies.task.VaadinTask;
import nl.procura.vaadin.functies.task.VaadinTaskLogger;

public class GebruikerControleTask implements VaadinTask {

  private static final Logger    LOGGER  = LoggerFactory.getLogger(GebruikerControleTask.class.getName());
  private final boolean          failed  = false;
  private final List<Check>      checks;
  private final GebruikerService serviceGebruikers;
  private boolean                success = false;
  private double                 percentage;
  private boolean                doStop  = false;

  public GebruikerControleTask(List<Check> checks, GebruikerService serviceGebruikers) {
    this.checks = checks;
    this.serviceGebruikers = serviceGebruikers;
  }

  @Override
  public void doStopNow() {
    doStop = true;
  }

  @Override
  public void execute() {

    try {
      ThreadLocalStorage.init(new GbaJpaStorageWrapper(GbaConfig.getProperties()));

      List<Gebruiker> gebruikers = serviceGebruikers.getGebruikers(GeldigheidStatus.ALLES, false);

      int nr = 0;
      int aantalGeenMail = 0;
      int aantalDubbelMail = 0;
      int aantalGeenProfiel = 0;

      for (Gebruiker gebruiker : gebruikers) {

        if (doStop) {
          break;
        }

        nr++;

        serviceGebruikers.reload(gebruiker);

        StringBuilder msg = new StringBuilder();
        String geenEmail = checkGeenEmail(gebruiker);
        String dubbelEmail = checkDubbelEmail(gebruiker, gebruikers);
        String geenProfiel = checkGeenProfiel(gebruiker);

        if (fil(geenEmail)) {
          aantalGeenMail++;
        }

        if (fil(dubbelEmail)) {
          aantalDubbelMail++;
        }

        if (fil(geenProfiel)) {
          aantalGeenProfiel++;
        }

        msg.append(geenEmail);
        msg.append(dubbelEmail);
        msg.append(checkDubbeleNaam(gebruiker, gebruikers));
        msg.append(geenProfiel);

        checks.add(new Check(trim(msg.toString()), gebruiker));

        setPercentage((100.00 / gebruikers.size()) * nr);
      }

      LOGGER.debug("Aantal zonder e-mail    : " + (aantalGeenMail + " / " + gebruikers.size()));
      LOGGER.debug("Aantal met dubbel e-mail: " + (aantalDubbelMail + " / " + gebruikers.size()));
      LOGGER.debug("Aantal zonder profiel   : " + (aantalGeenProfiel + " / " + gebruikers.size()));

      if (!doStop) {
        success = true;
        setPercentage(100.00);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public List<VaadinTaskLogger> getLoggers() {
    List<VaadinTaskLogger> loggers = new ArrayList<>();
    loggers.add(new VaadinTaskLogger(this.getClass().getName(), Level.DEBUG));
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
  }

  private String checkDubbelEmail(Gebruiker gebruiker, List<Gebruiker> gebruikers) {

    for (Gebruiker g : gebruikers) {

      boolean isGevuld = fil(g.getEmail()) && fil(gebruiker.getEmail());
      boolean isDubbel = g.getEmail().equals(gebruiker.getEmail());

      if (!g.equals(gebruiker) && isGevuld && isDubbel) {
        return "hetzelfde e-mailadres met gebruiker " + g.getGebruikersnaam() + ", ";
      }
    }

    return "";
  }

  private String checkDubbeleNaam(Gebruiker gebruiker, List<Gebruiker> gebruikers) {

    for (Gebruiker g : gebruikers) {

      boolean isGevuld = fil(g.getNaam()) && fil(gebruiker.getNaam());
      boolean isDubbel = g.getNaam().equals(gebruiker.getNaam());

      if (!g.equals(gebruiker) && isGevuld && isDubbel) {
        return "dezelfde naam als gebruiker " + g.getGebruikersnaam() + ", ";
      }
    }

    return "";
  }

  private String checkGeenEmail(Gebruiker gebruiker) {
    return emp(gebruiker.getEmail()) ? "geen e-mailadres, " : "";
  }

  private String checkGeenProfiel(Gebruiker gebruiker) {

    if (gebruiker.getProfielen().getAlle().isEmpty()) {
      return "geen profiel, ";
    }

    return "";
  }
}
