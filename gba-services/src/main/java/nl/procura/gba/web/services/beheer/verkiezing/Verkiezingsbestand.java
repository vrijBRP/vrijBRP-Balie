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

package nl.procura.gba.web.services.beheer.verkiezing;

import static nl.procura.standard.exceptions.ProExceptionSeverity.ERROR;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import nl.procura.gba.jpa.personen.db.KiesrVerk;
import nl.procura.standard.exceptions.ProException;

public class Verkiezingsbestand {

  private final List<Bestandsregel> nieuweRegels    = new ArrayList<>();
  private final List<Bestandsregel> bestaandeRegels = new ArrayList<>();
  private final List<Bestandsregel> regels          = new ArrayList<>();
  private final KiesrVerk           verkiezing;

  public Verkiezingsbestand(KiesrVerk verkiezing) {
    this.verkiezing = verkiezing;
  }

  public void add(int nr, String[] line) {
    if (line.length != 20) {
      throw new ProException(ERROR, String.format("Regel %d heeft niet 20 kolommen maar " + line.length, nr));
    }
    Bestandsregel regel = new Bestandsregel();
    regel.setCGem(getColumn(line, 0));
    regel.setSGem(getColumn(line, 1));
    regel.setAfkVerk(getColumn(line, 2));
    regel.setSVerk(getColumn(line, 3));
    regel.setCStem(Long.valueOf(getColumn(line, 4)));
    regel.setSStem(getColumn(line, 5));
    regel.setAStem(getColumn(line, 6));
    regel.setAnr(getColumn(line, 7));
    regel.setVStem(Long.valueOf(getColumn(line, 8)));
    regel.setVoorn(getColumn(line, 9));
    regel.setNaam(getColumn(line, 10));
    regel.setStraat(getColumn(line, 11));
    regel.setHnr(getColumn(line, 12));
    regel.setHnrL(getColumn(line, 13));
    regel.setHnrT(getColumn(line, 14));
    regel.setPc(getColumn(line, 15));
    regel.setWpl(getColumn(line, 16));
    regel.setDGeb(getColumn(line, 17));
    regel.setGeslacht(getColumn(line, 18));
    regel.setPasNr(getColumn(line, 19));
    regels.add(regel);
  }

  public void checkDuplicates(KiezersregisterService service) {
    Set<String> kiezers = service.getStempassenWithMax(verkiezing, -1).stream()
        .map(kiezer -> toKiezerId(kiezer.getVnr()))
        .collect(Collectors.toSet());
    bestaandeRegels.clear();
    nieuweRegels.clear();
    for (Bestandsregel regel : regels) {
      if (isOpgeslagen(kiezers, regel)) {
        bestaandeRegels.add(regel);
      } else {
        nieuweRegels.add(regel);
      }
    }
  }

  private boolean isOpgeslagen(Set<String> kiezers, Bestandsregel regel) {
    return kiezers.contains(toKiezerId(regel.getVStem()));
  }

  private String toKiezerId(Long vnr) {
    return vnr.toString();
  }

  private String getColumn(String[] line, int i) {
    return line[i].trim();
  }

  public KiesrVerk getVerkiezing() {
    return verkiezing;
  }

  public String getVerkiezingInfo() {
    if (!regels.isEmpty()) {
      Bestandsregel regel = regels.get(0);
      return String.format("%s / %s / %s", regel.getSGem(), regel.getAfkVerk(), regel.getSVerk());
    }
    return "Leeg bestand";
  }

  public List<Bestandsregel> getNieuweRegels() {
    return nieuweRegels;
  }

  public List<Bestandsregel> getBestaandeRegels() {
    return bestaandeRegels;
  }
}
