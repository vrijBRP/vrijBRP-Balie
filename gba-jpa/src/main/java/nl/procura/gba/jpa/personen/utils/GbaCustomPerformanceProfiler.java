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

package nl.procura.gba.jpa.personen.utils;

import static nl.procura.standard.Globalfunctions.fil;

import java.util.List;

import org.eclipse.persistence.tools.profiler.PerformanceProfiler;
import org.eclipse.persistence.tools.profiler.Profile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GbaCustomPerformanceProfiler extends PerformanceProfiler {

  private static final Logger LOGGER = LoggerFactory.getLogger(GbaCustomPerformanceProfiler.class);

  public GbaCustomPerformanceProfiler() {
    super(false);
  }

  @Override
  protected void addProfile(Profile profile) {

    super.addProfile(profile);
    String name = "";

    if (profile.getDomainClass() != null) {
      name = profile.getDomainClass().getSimpleName();
    } else if (profile.getQueryClass() != null) {
      name = profile.getQueryClass().getSimpleName();
    }

    if (fil(name)) {

      long totalTime = profile.getTotalTime() / 1000000;
      long instances = profile.getNumberOfInstancesEffected();

      Object property = getSession().getActiveSession().getProperty(GbaCustomSessionLogger.SQL_STATEMENT);
      if (property != null) {
        getSession().getActiveSession().removeProperty(GbaCustomSessionLogger.SQL_STATEMENT);

        for (String sql : (List<String>) property) {
          LOGGER.debug(sql);
        }

        StringBuilder command = new StringBuilder();
        command.append("Profile ");
        command.append("time: ");
        command.append(totalTime);
        command.append("ms. entity: ");
        command.append(name);
        command.append(" ms, objects: ");
        command.append(instances);
        command.append("\n");
        LOGGER.debug(command.toString());
      }
    }
  }
}
