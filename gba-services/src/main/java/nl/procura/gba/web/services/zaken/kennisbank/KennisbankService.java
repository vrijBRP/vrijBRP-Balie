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

package nl.procura.gba.web.services.zaken.kennisbank;

import static nl.procura.standard.Globalfunctions.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.procura.gba.config.GbaConfig;
import nl.procura.gba.web.services.AbstractService;
import nl.procura.gba.web.services.beheer.parameter.ParameterConstant;
import nl.procura.gba.web.services.zaken.kennisbank.KennisbankRecord.KennisbankURL;
import nl.procura.standard.Resource;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.commons.core.exceptions.ProExceptionSeverity;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

import au.com.bytecode.opencsv.CSVReader;

public class KennisbankService extends AbstractService {

  public static final String  BESTAND = "kennisbank.csv";
  private final static Logger LOGGER  = LoggerFactory.getLogger(KennisbankService.class.getName());

  public KennisbankService() {
    super("Kennisbank");
  }

  /**
   * Geeft kennisbank.csv terug
   */
  public File getBestand() {
    File file = getBestandInConfig();
    return file.exists() ? file : new File(Resource.getURL(BESTAND).getFile());
  }

  /**
   * Is er een bestand geupload
   */
  public boolean isBestandInConfig() {
    return getBestandInConfig().exists();
  }

  /**
   * Kennisbank.csv in config map
   */
  public File getBestandInConfig() {
    return new File(GbaConfig.getPath().getConfigDir(), BESTAND);
  }

  public FieldValue getLand(FieldValue natio) {
    for (KennisbankRecord kb : getRecords()) {
      if (kb.getNatioCode() == along(natio.getValue())) {
        return new FieldValue(kb.getLandCode(), kb.getLandOmsOfficieel());
      }
    }

    return new FieldValue();
  }

  public FieldValue getNationaliteit(FieldValue land) {
    for (KennisbankRecord kb : getRecords()) {
      if (kb.getLandCode() == along(land.getValue())) {
        return new FieldValue(kb.getNatioCode(), kb.getLandOmsOfficieel());
      }
    }

    return new FieldValue();
  }

  public String getUitgave() {

    CSVReader reader = null;
    try {
      reader = new CSVReader(new FileReader(getBestand()));
      for (String[] line : reader.readAll()) {
        if (astr(line[0]).equalsIgnoreCase("uitgave")) {
          return line[1];
        }
      }
    } catch (IOException e) {
      throw new ProException(ProExceptionSeverity.ERROR, "Fout bij inlezen kennisbankgegevens");
    } finally {
      IOUtils.closeQuietly(reader);
    }

    return "Onbekend";
  }

  /**
   * Geeft alle records in kennisbank.csv terug
   */
  public List<KennisbankRecord> getRecords() {

    List<KennisbankRecord> records = new ArrayList<>();
    CSVReader reader = null;
    try {
      reader = new CSVReader(new FileReader(getBestand()));
      for (String[] line : reader.readAll()) {
        KennisbankRecord r = new KennisbankRecord();

        r.setNatioCode(along(line[0]));
        r.setNatioOmsOfficieel(line[1]);
        r.setLandCode(along(line[2]));
        r.setLandOmsOfficieel(line[3]);
        r.setLandOmsKennisbank(line[4]);

        r.getUrls().add(new KennisbankURL(KennisBankDoel.AFSTAMMING, line[5]));
        r.getUrls().add(new KennisbankURL(KennisBankDoel.NATIONALITEIT, line[6]));
        r.getUrls().add(new KennisbankURL(KennisBankDoel.NAMENRECHT, line[7]));

        if (r.getNatioCode() > 0 || r.getLandCode() > 0) {
          records.add(r);
        }
      }
    } catch (IOException e) {
      throw new ProException(ProExceptionSeverity.ERROR, "Fout bij inlezen kennisbankgegevens");
    } finally {
      IOUtils.closeQuietly(reader);
    }

    return records;
  }

  /**
   * Geeft volledige URL terug
   */
  public String getURL(KennisBankBron bron, KennisBankDoel doel, long code) {

    LOGGER.debug("Kennisbank bron: " + bron + ", doel: " + doel + ", code: " + code);

    for (KennisbankRecord r : getRecords()) {

      boolean isNatio = (bron == KennisBankBron.NATIONALITEIT && r.getNatioCode() == code);
      boolean isLand = (bron == KennisBankBron.LAND && r.getLandCode() == code);

      if (isNatio || isLand) {
        for (KennisbankURL url : r.getUrls()) {
          if (fil(url.getUrl()) && url.getDoel() == doel) {
            return getURL(url.getUrl());
          }
        }
      }
    }

    return "";
  }

  public String getURL(String refUrl) {

    if (emp(refUrl)) {
      refUrl = getParm(ParameterConstant.KENNISBANK_URL, true);
    }

    //      String token = getToken();
    //      String statusURL = getParm(ParameterConstant.KENNISBANK_STATUS_URL, "URL van de kennisbank status server");
    //      return statusURL + "?ticket=" + token + "&szreturl=" + refUrl;
    return refUrl;
  }

  //    private String getToken() {
  //
  //        String gebruiker = getParm(ParameterConstant.KENNISBANK_USERNAME, "gebruikersnaam van uw kennisbank account");
  //        String ww        = getParm(ParameterConstant.KENNISBANK_PW, "wachtwoord van de uw kennisbank account");
  //        String authUrl   = getParm(ParameterConstant.KENNISBANK_AUTH_URL, "URL van de kennisbank authenticatie server");
  //
  //        return new ReedSSOSoapHandler(authUrl, gebruiker, ww).getToken();
  //    }
}
