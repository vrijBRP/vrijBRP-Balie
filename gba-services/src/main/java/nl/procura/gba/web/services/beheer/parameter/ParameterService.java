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

package nl.procura.gba.web.services.beheer.parameter;

import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.on;
import static java.util.Arrays.asList;
import static nl.procura.gba.common.MiscUtils.copy;
import static nl.procura.gba.common.MiscUtils.copyList;
import static nl.procura.standard.Globalfunctions.aval;
import static nl.procura.standard.Globalfunctions.pos;
import static org.apache.commons.lang3.StringUtils.defaultIfBlank;

import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.config.GbaConfig;
import nl.procura.gba.jpa.personen.dao.ParmDao;
import nl.procura.gba.jpa.personen.dao.UsrDao;
import nl.procura.gba.jpa.personen.db.BaseEntity;
import nl.procura.gba.jpa.personen.db.Parm;
import nl.procura.gba.jpa.personen.db.Usr;
import nl.procura.gba.web.services.AbstractService;
import nl.procura.gba.web.services.aop.ThrowException;
import nl.procura.gba.web.services.aop.Transactional;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.gba.web.services.beheer.profiel.Profiel;
import nl.procura.gba.web.services.interfaces.GeldigheidStatus;

public class ParameterService extends AbstractService {

  private static final String ERROR_RETRIEVING_PARMS = "Fout bij ophalen parameters";

  public ParameterService() {
    super("Parameters");
  }

  @ThrowException(ERROR_RETRIEVING_PARMS)
  public Parameters getAllParameters() {
    Parameters params = new Parameters();
    params.getAlle().addAll(copyList(addDefaultParameters(findEntity(new Parm())), Parameter.class));
    return params;
  }

  @ThrowException(ERROR_RETRIEVING_PARMS)
  public Parameters getGebruikerParameters(Gebruiker gebruiker) {
    List<Long> cProfiles = extract(gebruiker.getProfielen().getAlle(), on(Profiel.class).getCProfile());
    List<Parm> findParameters = ParmDao.findParameters(asList(gebruiker.getCUsr()), cProfiles, null);
    Parameters params = new Parameters();
    for (Parm parm : addDefaultParameters(findParameters)) {
      Parameter parameter = copy(parm, Parameter.class);
      // Remove existing
      new ArrayList<>(params.getAlle())
          .stream()
          .filter(p -> p.isParm(parameter.getParm()))
          .forEach(p -> params.getAlle().remove(p));
      //add new one from usr
      params.getAlle().add(parameter);
    }
    return params;
  }

  /**
   * updates a system wide parameter
   */
  public void setSysteemParameter(ParameterType parm, String value) {
    saveParameter(parm, value, null, null, true);
  }

  /**
   * returns a system wide parameter directly from the database
   */
  @ThrowException(ERROR_RETRIEVING_PARMS)
  public Parameter getSysteemParameter(ParameterType parameterType) {

    List<Parm> parameters = ParmDao.findParameters(asList(BaseEntity.DEFAULT),
        asList(BaseEntity.DEFAULT), parameterType.getKey());

    return parameters.stream()
        .findFirst()
        .map(p -> copy(p, Parameter.class))
        .orElse(copy(getDefault(parameterType), Parameter.class));
  }

  /**
   * All users with a specific parameter and status
   */
  @ThrowException(ERROR_RETRIEVING_PARMS)
  public List<Gebruiker> getParameterGebruikers(ParameterType parameter, GeldigheidStatus status) {

    List<Usr> usrs = new ArrayList<>();
    for (Parm parm : ParmDao.findParameters(parameter.getKey())) {

      boolean isWel = pos(parm.getValue());
      boolean isNiet = aval(parm.getValue()) == 0;
      boolean isProfiel = pos(parm.getProfile().getCProfile());
      boolean isGebruiker = pos(parm.getUsr().getCUsr());

      if (!isProfiel && !isGebruiker) {
        if (isWel) {
          usrs.addAll(UsrDao.find());
        } else if (isNiet) {
          usrs.removeAll(UsrDao.find());
        }
      } else if (isProfiel) {
        if (isWel) {
          usrs.addAll(parm.getProfile().getUsrs());
        } else if (isNiet) {
          usrs.removeAll(parm.getProfile().getUsrs());
        }
      } else if (isGebruiker) {
        if (isWel) {
          usrs.add(parm.getUsr());
        } else if (isNiet) {
          usrs.remove(parm.getUsr());
        }
      }
    }

    List<Gebruiker> gebruikers = copyList(usrs, Gebruiker.class);
    return getServices().getGebruikerService().getGebruikers(gebruikers, status, false);
  }

  @ThrowException("Fout bij opslaan parameter")
  @Transactional(classes = Parm.class)
  public void saveParameter(ParameterType parm, String value, Gebruiker gebr, Profiel profiel,
      boolean updateGebruikerParameters) {

    long cUsr = (gebr == null) ? 0 : gebr.getCUsr();
    long cProfile = (profiel == null) ? 0 : profiel.getCProfile();
    ParmDao.saveParameter(parm.getKey(), value, cUsr, cProfile);

    // Update user parameters
    if (updateGebruikerParameters) {
      Gebruiker u = getServices().getGebruiker();
      u.setParameters(getGebruikerParameters(u));
    }
  }

  @ThrowException("Fout bij opslaan parameter")
  @Transactional
  public void saveParameter(String parm, String value, long cUsr, long cProfile) {
    ParmDao.saveParameter(parm, value, cUsr, cProfile);
  }

  @ThrowException("Fout bij opslaan parameter")
  @Transactional
  public void saveParameter(ParameterType parm, String value, long cUsr, long cProfile) {
    ParmDao.saveParameter(parm.getKey(), value, cUsr, cProfile);
  }

  /**
   * Adds possible default value
   */
  private List<Parm> addDefaultParameters(List<Parm> dbParameters) {
    List<Parm> parms = new ArrayList<>(dbParameters);
    for (ParameterType parameterType : ParameterConstant.values()) {
      // If the parameter does not exist in the list than add the default parameter
      if (parms.stream().noneMatch(p -> p.isParm(parameterType.getKey()))) {
        parms.add(getDefault(parameterType));
      }
    }

    return parms;
  }

  /**
   * Returns parm from config if value in DB is empty
   */
  private Parm getDefault(ParameterType parameterType) {
    String configParm = GbaConfig.getConfigParm("PARM_" + parameterType.getKey());
    return Parm.newDefault(parameterType.getKey(), defaultIfBlank(parameterType.getDefaultValue(), configParm));
  }
}
