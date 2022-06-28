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

package nl.procura.gba.web.services.gba.basistabellen.gemeente;

import static nl.procura.gba.common.MiscUtils.copy;
import static nl.procura.standard.Globalfunctions.pos;
import static nl.procura.standard.Globalfunctions.toBigDecimal;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.config.GbaConfig;
import nl.procura.gba.jpa.personen.dao.GemDao;
import nl.procura.gba.jpa.personen.db.Gem;
import nl.procura.gba.web.services.AbstractService;
import nl.procura.gba.web.services.aop.ThrowException;
import nl.procura.gba.web.services.aop.Transactional;
import nl.procura.standard.Resource;
import nl.procura.standard.exceptions.ProException;
import nl.procura.standard.exceptions.ProExceptionSeverity;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.validation.Postcode;

import au.com.bytecode.opencsv.CSVReader;

public class GemeenteService extends AbstractService {

  public static final String BESTAND = "gemeenten.csv";

  public GemeenteService() {
    super("Gemeente");
  }

  @ThrowException("Fout bij het verwijderen van het record")
  @Transactional
  public void delete(Gemeente locatie) {
    removeEntity(locatie);
  }

  /**
   * Geeft kennisbank.csv terug
   */
  public File getBestand() {

    File file = getBestandInConfig();
    return file.exists() ? file : new File(Resource.getURL(BESTAND).getFile());
  }

  /**
   * Kennisbank.csv in config map
   */
  public File getBestandInConfig() {
    return new File(GbaConfig.getPath().getConfigDir(), BESTAND);
  }

  public Gemeente getGemeente(BasePLExt pl) {
    Gemeente gemeente = new Gemeente();
    if (pl != null) {
      BigDecimal cGem = toBigDecimal(pl.getVerblijfplaats().getAdres().getGemeente().getValue().getVal());
      gemeente = getGemeente(cGem);
    }
    return gemeente;
  }

  public Gemeente getGemeente(FieldValue gemeente) {
    return getGemeente(toBigDecimal(gemeente.getStringValue()));
  }

  public Gemeente getGemeente(BigDecimal cbsCode) {
    Gemeente gemeente = get(cbsCode);
    return gemeente != null ? gemeente : new Gemeente();
  }

  @ThrowException("Fout bij ophalen de records")
  public List<Gemeente> getGemeenten() {
    List<Gemeente> list = new ArrayList<>();
    for (Gem gem : GemDao.find()) {
      if (gem.getCGem() != null) {
        list.add(copy(gem, Gemeente.class));
      }
    }

    return list;
  }

  /**
   * Geeft alle records in kennisbank.csv terug
   */
  public List<Gemeente> getRecords() {
    List<Gemeente> records = new ArrayList<>();

    try {
      try (CSVReader reader = new CSVReader(new FileReader(getBestand()))) {
        for (String[] line : reader.readAll()) {
          Gemeente gemeente = new Gemeente();
          gemeente.setCbscode(toBigDecimal(line[0]));
          gemeente.setGemeente(line[1]);
          gemeente.setAdres(line[2]);
          gemeente.setPc(line[3]);
          gemeente.setPlaats(line[4]);
          records.add(gemeente);
        }
      }
    } catch (IOException e) {
      throw new ProException(ProExceptionSeverity.ERROR, "Fout bij inlezen gegevens");
    }

    return records;
  }

  @ThrowException("Fout bij het opslaan van het record")
  @Transactional
  public void save(Gemeente gemeente) {
    saveEntity(gemeente);
  }

  @Transactional
  public void update(File file) {

    List<Gemeente> gemeenten = getGemeenten(file);
    removeEntities(getGemeenten());

    for (Gemeente gemeente : gemeenten) {
      Gemeente bestaandeGemeente = get(gemeente.getCbscode());

      if (bestaandeGemeente != null) {
        bestaandeGemeente.setGemeente(gemeente.getGemeente());
        bestaandeGemeente.setAdres(gemeente.getAdres());
        bestaandeGemeente.setPc(gemeente.getPc());
        bestaandeGemeente.setPlaats(gemeente.getPlaats());

        save(bestaandeGemeente);

      } else {

        save(gemeente);
      }
    }
  }

  private List<Gemeente> getGemeenten(File file) {
    List<Gemeente> gemeenten = new ArrayList<>();
    for (String[] line : getLines(file)) {
      if (line.length == 5) {
        Gemeente gemeente = new Gemeente();
        if (pos(line[0])) {
          gemeente.setCbscode(toBigDecimal(line[0]));
          gemeente.setGemeente(line[1]);
          gemeente.setAdres(line[2]);
          gemeente.setPc(Postcode.getCompact(line[3]));
          gemeente.setPlaats(line[4]);
          gemeenten.add(gemeente);
        }
      } else {
        throw new ProException("Het bestand bevat meer of minder van 5 kolommen");
      }
    }

    return gemeenten;
  }

  private Gemeente get(BigDecimal cbscode) {
    return copy(GemDao.findByCbscode(cbscode), Gemeente.class);
  }

  /**
   * Read the CSV with both a , and ;
   */
  private List<String[]> getLines(File file) {
    List<String[]> lines = getLines(file, ';');
    if (lines.isEmpty() || lines.get(0).length != 5) {
      lines = getLines(file, ',');
    }
    return lines;
  }

  private List<String[]> getLines(File file, char separator) {
    CSVReader csvReader = null;
    FileReader fileReader = null;

    try {
      fileReader = new FileReader(file);
      csvReader = new CSVReader(fileReader, separator);
      return csvReader.readAll();
    } catch (IOException e) {
      throw new ProException("Kan bestand niet inlezen", e);
    } finally {
      IOUtils.closeQuietly(csvReader);
      IOUtils.closeQuietly(fileReader);
    }
  }
}
