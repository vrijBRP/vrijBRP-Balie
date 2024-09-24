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

package nl.procura.gba.web.services.applicatie.onderhoud;

import static java.text.MessageFormat.format;
import static nl.procura.commons.liquibase.utils.LbConnectionUtils.getConnection;
import static nl.procura.commons.liquibase.utils.LbConnectionUtils.getDatabase;
import static nl.procura.gba.web.services.applicatie.meldingen.ServiceMeldingCategory.FAULT;
import static nl.procura.gba.web.services.applicatie.meldingen.ServiceMeldingCategory.SYSTEM;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.GEMEENTE_CODES;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.SSL_PROXY_URL;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.SYSTEM_MIN_HD_SIZE;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.TEST_OMGEVING;
import static nl.procura.standard.Globalfunctions.along;
import static nl.procura.standard.Globalfunctions.aval;
import static nl.procura.standard.Globalfunctions.isTru;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.ERROR;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.WARNING;
import static nl.procura.commons.core.exceptions.ProExceptionType.CONFIG;

import java.io.File;
import java.io.FileFilter;
import java.math.BigInteger;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.io.filefilter.RegexFileFilter;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;

import nl.procura.burgerzaken.gba.StringUtils;
import nl.procura.commons.liquibase.connections.LbDatabaseType;
import nl.procura.gba.config.GbaConfig;
import nl.procura.gba.jpa.personen.dao.AppDao;
import nl.procura.gba.jpa.personen.db.App;
import nl.procura.gba.web.rest.client.GbaRestClient;
import nl.procura.gba.web.services.AbstractService;
import nl.procura.gba.web.services.aop.Transactional;
import nl.procura.gba.web.services.applicatie.DatabaseConfig;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.gba.web.services.beheer.gebruiker.GebruikerService;
import nl.procura.gba.web.services.beheer.parameter.ParameterService;
import nl.procura.proweb.rest.v1_0.gebruiker.ProRestGebruiker;
import nl.procura.proweb.rest.v1_0.gebruiker.ProRestGebruikerAntwoord;
import nl.procura.proweb.rest.v1_0.meldingen.ProRestMelding;
import nl.procura.proweb.rest.v1_0.sessie.ProRestTicketAntwoord;
import nl.procura.ssl.web.rest.client.SslWebRestClient;
import nl.procura.ssl.web.rest.client.SslWebRestClientResponse;
import nl.procura.ssl.web.rest.v1_0.certificates.SslRestCertificate;
import nl.procura.ssl.web.rest.v1_0.certificates.SslRestCertificatesResponse;
import nl.procura.ssl.web.rest.v1_0.connections.SslRestConnectionsResponse;
import nl.procura.commons.core.exceptions.ProException;

public class OnderhoudService extends AbstractService {

  public static final Object TICKET = "ticket";
  private static final long  KB     = 1024L;

  public OnderhoudService() {
    super("Onderhoud");
  }

  @Override
  public void check() {
    getSSLCertificates();
    getFileSystemInfo();
    getAppsInfo();
  }

  public void checkConnection(DatabaseConfig config) {
    try {
      String type = config.getType();
      String server = config.getServer();
      String port = config.getPort();
      String schema = config.getSchema();
      String sid = config.getSid();
      String username = config.getUsername();
      String pw = config.getPassword();
      getDatabase(getConnection(LbDatabaseType.get(type), server, aval(port), schema, sid, username, pw)).close();
    } catch (Exception e) {
      throw new ProException(CONFIG, ERROR, "Kan geen verbinding maken", e);
    }
  }

  public List<DatabaseConfig> getDatabases() {

    List<DatabaseConfig> databases = new ArrayList<>();
    File dir = new File(GbaConfig.getPath().getConfigDir());
    FileFilter fileFilter = new RegexFileFilter("^db.(.*)");

    for (File file : Objects.requireNonNull(dir.listFiles(fileFilter))) {
      databases.add(new DatabaseConfig(file));
    }

    return databases;
  }

  /**
   * Geef de ssl certificaten terug
   */
  public SslRestCertificatesResponse getSSLCertificates() {
    try {
      String parm = getParm(SSL_PROXY_URL, false);
      if (StringUtils.isNotBlank(parm)) {
        SslWebRestClient client = new SslWebRestClient(parm, "", "", "");
        try {
          SslWebRestClientResponse<SslRestCertificatesResponse> response = client.getCertificates().get();
          ClientResponse clientResponse = response.getClientResponse();

          if (clientResponse.getStatus() == Status.OK.getStatusCode()) {
            SslRestCertificatesResponse antwoord = response.getEntity();

            for (ProRestMelding melding : antwoord.getMeldingen()) {
              switch (melding.getType()) {
                case WAARSCHUWING:
                  addMessage(true, SYSTEM, WARNING, melding.getOmschrijving());
                  break;

                case FOUT:
                  addMessage(true, SYSTEM, ERROR, melding.getOmschrijving());
                  break;
              }
            }

            for (SslRestCertificate certificate : antwoord.getCertificates()) {
              if (certificate.getDaysValid() <= 0) {
                addMessage(true, SYSTEM, ERROR,
                    MessageFormat.format("Certificaat ({0}) is verlopen.", certificate.getDescription()));
              } else if (certificate.getDaysValid() <= 30) {
                addMessage(true, SYSTEM, WARNING,
                    MessageFormat.format("Certificaat ({0}) verloopt over {1} {2}.",
                        certificate.getDescription(),
                        certificate.getDaysValid(),
                        (certificate.getDaysValid() > 1 ? "dagen" : "dag")));
              }
            }

            antwoord.getCertificates().sort((o1, o2) -> o1.getDaysValid() < o2.getDaysValid()
                ? BigInteger.ONE.intValue()
                : BigInteger.ZERO.intValue());

            return antwoord;
          }
        } catch (Exception e) {
          String message = "Kan geen verbinding maken met de proxy (SSL) server. controleer de URL in de parameters";
          String cause = "Foutieve url wellicht? (" + client.getUrl() + ")";
          addMessage(false, FAULT, ERROR, message, cause, e);
        }
      }
    } catch (Exception e) {
      addMessage(false, FAULT, ERROR, e.getMessage(), "", e);
    }

    return new SslRestCertificatesResponse();
  }

  public SslRestConnectionsResponse getSSLConnections(boolean withCheck) {
    try {
      SslWebRestClient client = new SslWebRestClient(getParm(SSL_PROXY_URL, true), "", "", "");

      try {
        SslWebRestClientResponse<SslRestConnectionsResponse> response = withCheck
            ? client.getConnections().check()
            : client.getConnections().get();
        ClientResponse clientResponse = response.getClientResponse();

        if (clientResponse.getStatus() == Status.OK.getStatusCode()) {
          SslRestConnectionsResponse antwoord = response.getEntity();

          for (ProRestMelding melding : antwoord.getMeldingen()) {
            switch (melding.getType()) {
              case WAARSCHUWING:
                addMessage(true, SYSTEM, WARNING, melding.getOmschrijving());
                break;

              case FOUT:
                addMessage(true, SYSTEM, ERROR, melding.getOmschrijving());
                break;
            }
          }

          return antwoord;
        }
      } catch (Exception e) {
        String message = "Kan geen verbinding maken met de proxy (SSL) server. controleer de URL in de parameters";
        String cause = "Foutieve url wellicht? (" + client.getUrl() + ")";
        addMessage(false, FAULT, ERROR, message, cause, e);
      }
    } catch (Exception e) {
      addMessage(false, FAULT, ERROR, e.getMessage(), "", e);
    }

    return new SslRestConnectionsResponse();
  }

  private ClientResponse check(ClientResponse response) {
    switch (Status.fromStatusCode(response.getStatus())) {
      case OK:
        return response;

      case NOT_FOUND:
        throw new ProException(WARNING, "De BSM kan niet worden benaderd (controleer de parameters)");

      case UNAUTHORIZED:
        throw new ProException(WARNING, "U heeft momenteel geen toegang tot de taakplanner (BSM)");

      default:
        throw new ProException(ERROR, response.getStatusInfo().getReasonPhrase());
    }
  }

  public void getAppsInfo() {
    for (Application application : getActiveApps(true)) {
      if (!application.isLoginOk()) {
        addMessage(true, FAULT, ERROR,
            format("Kan geen verbinding maken met {0}: {1}", application.getEntity().getName(),
                application.getProblem()));
        break;
      }
    }
  }

  public FileSystemInfo getFileSystemInfo() {

    FileSystemInfo info = new FileSystemInfo();
    File file = new File(System.getProperty("catalina.home"));

    if (file.exists()) {

      long free = file.getFreeSpace();
      long total = file.getTotalSpace();
      long used = total - free;

      int freePerc = (int) ((100.00 / total) * free);
      int usedPerc = (int) ((100.00 / total) * used);

      ParameterService parameterService = getServices().getParameterService();
      long minHdSize = along(parameterService.getSysteemParameter(SYSTEM_MIN_HD_SIZE).getValue());
      long minHdSizeBytes = minHdSize * KB * KB;
      long freeMb = free / KB / KB;

      info.setFreeSpace(free);
      info.setFreeSpacePerc(freePerc);
      info.setUsedSpace(used);
      info.setUsedSpacePerc(usedPerc);
      info.setTotalSpace(total);
      info.setAlmostFull(free <= minHdSizeBytes);
      info.setRequiredFreeSpace(minHdSizeBytes);

      if (info.isAlmostFull()) {
        addMessage(true, SYSTEM, WARNING,
            format("De hardeschijf van de server is voor {0}% vol. Er is nog {1}MB vrij.", usedPerc,
                freeMb));
      }
    }

    return info;
  }

  @Transactional
  public void delete(Application syncInstance) {
    removeEntity(syncInstance.getEntity());
  }

  /**
   * Store the sync entity
   */
  @Transactional
  public void save(Application syncInstance) {
    saveEntity(syncInstance.getEntity());
  }

  public List<Application> getActiveApps(boolean check) {
    return getApps(check).stream()
        .filter(Application::isActive)
        .collect(Collectors.toList());
  }

  /**
   * Returns a syncInstance Wrapper
   */
  public List<Application> getApps(boolean check) {
    List<Application> apps = new ArrayList<>();
    for (App sync : AppDao.findAllApps()) {
      apps.add(getApp(sync, check));
    }

    return apps;
  }

  /**
   * Returns a ticket from a different app
   */
  public String getAppTicket(Application app) {
    App sync = app.getEntity();
    Gebruiker gebruiker = getServices().getGebruiker();
    String wachtwoord = getServices().getGebruikerService().getCurrentPassword(gebruiker).orElse(null);

    GbaRestClient client = new GbaRestClient(sync.getUrl(), sync.getName(), gebruiker.getGebruikersnaam(),
        wachtwoord);
    return check(client.getTicket()).getEntity(ProRestTicketAntwoord.class).getTicket();
  }

  /**
   * Returns Synchronisatie instances of this application
   */
  public Application getApp(App app, boolean check) {
    GebruikerService gebruikerService = getServices().getGebruikerService();
    Gebruiker gebruiker = gebruikerService.getGebruikerByNaam(app.getUsername());
    Application application = new Application(app);

    if (gebruiker != null) {
      application.setUserOk(gebruiker.isInlogbaar());
      application.setProblem(gebruiker.getAccountProbleem());

      if (gebruiker.isInlogbaar()) {
        gebruikerService.getCurrentPassword(gebruiker).ifPresent(password -> {
          GbaRestClient client = new GbaRestClient(app.getUrl(), app.getName(), app.getUsername(), password);
          application.setLoginOk(check && getLoginOk(app, application, client));
          application.setClient(client);
        });
      }
    } else {
      application.setProblem(format("Gebruiker {0} bestaat niet", app.getUsername()));
    }

    return application;
  }

  public String getGemeenteCodes() {
    ParameterService parameterService = getServices().getParameterService();
    return parameterService.getSysteemParameter(GEMEENTE_CODES).getValue();
  }

  public boolean isTestEnvironment() {
    ParameterService parameterService = getServices().getParameterService();
    return isTru(parameterService.getSysteemParameter(TEST_OMGEVING).getValue());
  }

  private boolean getLoginOk(App sync, Application app, GbaRestClient client) {
    try {
      ClientResponse response = client.getGebruiker(sync.getUsername());
      if (response != null) {
        if (Status.OK.getStatusCode() == response.getStatus()) {
          ProRestGebruiker remoteUser = response.getEntity(ProRestGebruikerAntwoord.class).getGebruiker();
          if (remoteUser != null) {
            return true;
          }
        }
      }
      app.setProblem(format("Gebruiker {0} bestaat niet in {1}", sync.getUsername(), sync.getName()));
    } catch (RuntimeException e) {
      app.setProblem("Fout: " + e.getMessage());
    }

    return false;
  }
}
