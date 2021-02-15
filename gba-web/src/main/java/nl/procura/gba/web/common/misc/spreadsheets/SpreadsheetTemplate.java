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

package nl.procura.gba.web.common.misc.spreadsheets;

import static nl.procura.standard.Globalfunctions.astr;

import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.web.services.zaken.documenten.UitvoerformaatType;

public class SpreadsheetTemplate implements Spreadsheet {

  private final List<String> record = new ArrayList<>();
  private List<String[]>     data   = new ArrayList<>();
  private String             name;
  private UitvoerformaatType type;

  public SpreadsheetTemplate(String name, UitvoerformaatType type) {
    this.name = name;
    this.type = type;
  }

  public SpreadsheetTemplate add(Object value) {
    record.add(astr(value).replaceAll("<.*?>", ""));
    return this;
  }

  public void clear() {
    data.clear();
    record.clear();
  }

  @Override
  public void compose() {
  }

  @Override
  public List<String[]> getData() {
    return data;
  }

  public void setData(List<String[]> data) {
    this.data = data;
  }

  @Override
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public UitvoerformaatType getType() {
    return type;
  }

  public void setType(UitvoerformaatType type) {
    this.type = type;
  }

  public void store() {
    getData().add(record.toArray(new String[0]));
    record.clear();
  }

  @Override
  public String toString() {
    return this.name;
  }
}
