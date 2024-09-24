/*
 * Copyright 2022 - 2023 Procura B.V.
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

package nl.procura.gba.web.services.beheer.fileimport;

import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.capitalize;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.Data;

@Data
public class FileImportRecord {

  private Long                         id;
  private Long                         fileImportId;
  private boolean                      reference;
  private String                       template;
  private Map<String, FileImportValue> values;
  private List<String>                 remarks = new ArrayList<>();
  private String                       uuid;

  public FileImportValue get(String value) {
    return values.get(value);
  }

  public String getValue(String value) {
    return ofNullable(values.get(value))
        .filter(FileImportValue::isValid)
        .map(FileImportValue::getValue)
        .orElse("");
  }

  public boolean isValid() {
    return values.values().stream().allMatch(FileImportValue::isValid);
  }

  public String getRemarksAsString() {
    return capitalize(values.values().stream()
        .filter(value -> !value.isValid())
        .map(FileImportValue::getRemark)
        .collect(Collectors.joining(", ")).toLowerCase());
  }
}
