/*
 * Copyright 2023 - 2024 Procura B.V.
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

package nl.procura.gba.web.common.csv;

import static java.lang.String.format;
import static nl.procura.gba.web.common.csv.CsvParser.MISSING_HEADER;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.ERROR;

import java.util.ArrayList;
import java.util.Optional;

import nl.procura.commons.core.exceptions.ProException;

public class CsvHeaders extends ArrayList<CsvHeader> {

  public CsvHeader getByName(String header) {
    return findByName(header)
        .orElseThrow(() -> new ProException(ERROR, format(MISSING_HEADER,
            CsvParser.cleanHeader(header))));
  }

  public Optional<CsvHeader> findByName(String header) {
    return stream()
        .filter(h -> h.isName(header))
        .findFirst();
  }
}
