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

package nl.procura.gba.web.services.zaken.algemeen.dms;

import static nl.procura.standard.Globalfunctions.equalsIgnoreCase;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;

import nl.procura.standard.ProcuraDate;

public class IndexBestand {

  public static final String  INDEX         = "index.csv";
  private static final String TAB_SEPARATOR = "\t";
  private static final int    COLUMN_COUNT  = 6;
  private List<IndexRegel>    rows          = new ArrayList<>();

  public IndexBestand() {
  }

  /**
   * Lees indexfile uit. Alleen bestaande regels worden gebruikt.
   */
  public IndexBestand(File dir) {

    for (String l : getIndexFileLines(dir)) {
      String[] row = l.split(TAB_SEPARATOR);

      if ((row != null) && (row.length > COLUMN_COUNT)) {

        IndexRegel r = new IndexRegel();
        r.setBestandsnaam(row[0]);
        r.setExtensie(row[1]);
        r.setTitel(row[2]);
        r.setAangemaaktDoor(row[3]);
        r.setDatum(row[4]);
        r.setTijd(row[5]);
        r.setDataType(row[6]);

        if (row.length >= 8) {
          r.setZaakId(row[7]);
        }

        if (row.length >= 9) {
          r.setDmsNaam(row[8]);
        }

        if (row.length >= 10) {
          r.setVertrouwelijkheid(row[9]);
        }

        getRows().add(r);
      }
    }
  }

  public IndexRegel getIndexRow(File f) {
    String name = f.getName();
    return rows.parallelStream()
        .filter(row -> equalsIgnoreCase(name, row.getVolledigeBestandsnaam()))
        .findAny()
        .orElseGet(() -> {
          IndexRegel ir = new IndexRegel(new ProcuraDate(new Date(f.lastModified())));
          ir.setTitel(name);
          ir.setAangemaaktDoor("Onbekend");
          ir.setZaakId("N.v.t.");
          return ir;
        });
  }

  public List<IndexRegel> getRowsByZaakId(String zaakId) {
    return rows.parallelStream()
        .filter(row -> equalsIgnoreCase(zaakId, row.getZaakId()))
        .collect(Collectors.toList());
  }

  public List<IndexRegel> getRows() {
    return rows;
  }

  public void setRows(List<IndexRegel> rows) {
    this.rows = rows;
  }

  private synchronized List<String> getIndexFileLines(File dir) {

    try {
      if (dir.isDirectory()) {
        IOFileFilter indexFilter = FileFilterUtils.nameFileFilter(INDEX, IOCase.SENSITIVE);
        for (File file : FileUtils.listFiles(dir, indexFilter, null)) {
          return FileUtils.readLines(file);
        }
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return new ArrayList<>();
  }

  public static class IndexRegel {

    private String bestandsnaam      = "";
    private String volledigeNaam     = "";
    private String extensie          = "";
    private String titel             = "";
    private String aangemaaktDoor    = "";
    private String datum             = "";
    private String tijd              = "";
    private String dataType          = "";
    private String zaakId            = "";
    private String dmsNaam           = "";
    private String vertrouwelijkheid = "";

    public IndexRegel() {
    }

    public IndexRegel(ProcuraDate pd) {

      setDatum(pd.getSystemDate());
      setTijd(pd.getSystemTime());
    }

    public String getAangemaaktDoor() {
      return aangemaaktDoor;
    }

    public void setAangemaaktDoor(String aangemaaktDoor) {
      this.aangemaaktDoor = aangemaaktDoor;
    }

    public String getBestandsextensie() {
      return extensie;
    }

    public String getBestandsnaam() {
      return bestandsnaam;
    }

    public void setBestandsnaam(String bestandsnaam) {
      this.bestandsnaam = bestandsnaam;
      volledigeNaam = (bestandsnaam + "." + extensie);
    }

    public String getDataType() {
      return dataType;
    }

    public void setDataType(String dataType) {
      this.dataType = dataType;
    }

    public String getDatum() {
      return datum;
    }

    public void setDatum(String datum) {
      this.datum = datum;
    }

    public String getDmsNaam() {
      return dmsNaam;
    }

    public void setDmsNaam(String dmsNaam) {
      this.dmsNaam = dmsNaam;
    }

    public String getTijd() {
      return tijd;
    }

    public void setTijd(String tijd) {
      this.tijd = tijd;
    }

    public String getTitel() {
      return titel;
    }

    public void setTitel(String titel) {
      this.titel = titel;
    }

    public String getVolledigeBestandsnaam() {
      return volledigeNaam;
    }

    public String getZaakId() {
      return zaakId;
    }

    public void setZaakId(String zaakId) {
      this.zaakId = zaakId;
    }

    public void setExtensie(String extensie) {
      this.extensie = extensie;
      volledigeNaam = (bestandsnaam + "." + extensie);
    }

    public String getVertrouwelijkheid() {
      return vertrouwelijkheid;
    }

    public void setVertrouwelijkheid(String vertrouwelijkheid) {
      this.vertrouwelijkheid = vertrouwelijkheid;
    }

    @Override
    public String toString() {
      return String.format("%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s", bestandsnaam, extensie, titel,
          aangemaaktDoor, datum, tijd, dataType, zaakId, dmsNaam, vertrouwelijkheid);
    }
  }
}
