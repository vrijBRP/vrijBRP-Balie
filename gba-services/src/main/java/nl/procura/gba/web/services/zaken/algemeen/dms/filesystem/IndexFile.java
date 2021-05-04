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

package nl.procura.gba.web.services.zaken.algemeen.dms.filesystem;

import static nl.procura.standard.Globalfunctions.equalsIgnoreCase;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;

import nl.procura.standard.ProcuraDate;

public class IndexFile {

  public static final String  INDEX         = "index.csv";
  private static final String TAB_SEPARATOR = "\t";
  private static final int    COLUMN_COUNT  = 6;
  private List<IndexLine>     lines         = new ArrayList<>();

  public IndexFile(File dir) {
    for (String line : getIndexFileLines(dir)) {
      String[] row = line.split(TAB_SEPARATOR);
      if (row.length > COLUMN_COUNT) {
        IndexLine r = new IndexLine();
        r.setName(row[0]);
        r.setExtension(row[1]);
        r.setTitle(row[2]);
        r.setUser(row[3]);
        r.setDate(row[4]);
        r.setTime(row[5]);
        r.setDataType(row[6]);

        if (row.length >= 8) {
          r.setZaakId(row[7]);
        }

        if (row.length >= 9) {
          r.setDocumentTypeDescription(row[8]);
        }

        if (row.length >= 10) {
          r.setConfidentiality(row[9]);
        }

        if (row.length >= 11) {
          r.setAlias(row[10]);
        }

        getLines().add(r);
      }
    }
  }

  public IndexLine toIndexRow(File file) {
    String name = file.getName();
    IndexLine indexLine = lines.parallelStream()
        .filter(row -> equalsIgnoreCase(name, row.getFileName()))
        .findAny()
        .orElseGet(() -> {
          IndexLine ir = new IndexLine(new ProcuraDate(new Date(file.lastModified())));
          ir.setTitle(name);
          ir.setUser("Onbekend");
          ir.setZaakId("N.v.t.");
          return ir;
        });
    indexLine.setName(FilenameUtils.getBaseName(file.getName()));
    indexLine.setExtension(FilenameUtils.getExtension(file.getName()));
    return indexLine;
  }

  public List<IndexLine> getRowsByZaakId(String zaakId) {
    return lines.parallelStream()
        .filter(row -> equalsIgnoreCase(zaakId, row.getZaakId()))
        .collect(Collectors.toList());
  }

  public List<IndexLine> getLines() {
    return lines;
  }

  public void setLines(List<IndexLine> lines) {
    this.lines = lines;
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
}
