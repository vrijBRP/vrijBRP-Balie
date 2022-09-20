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

package nl.procura.gba.web.common.csv;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import nl.procura.gba.web.common.csv.CsvHeader.CsvHeaderBuilder;
import nl.procura.gba.web.common.csv.CsvParser.CsvHeaders;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CsvConfig {

  private byte[]     content;
  private CsvHeaders headers;

  public static CsvConfigBuilder builder() {

    return new CsvConfigBuilder() {

      @Override
      public CsvConfig build() {
        super.headers = new CsvHeaders();
        super.headers.addAll(super.configHeaders.stream()
            .map(CsvHeaderBuilder::build)
            .collect(Collectors.toList()));
        return super.build();
      }
    };
  }

  public static class CsvConfigBuilder {

    private final List<Function<String, String>> csvConverters = new ArrayList<>();
    private final List<CsvHeaderBuilder>         configHeaders = new ArrayList<>();

    public CsvHeaderBuilder header(String name) {
      CsvHeaderBuilder builder = CsvHeader.builder(this).name(name);
      configHeaders.add(builder);
      return builder;
    }

    public CsvConfigBuilder converter(Function<String, String> converter) {
      this.csvConverters.add(converter);
      return this;
    }

    public List<Function<String, String>> getCsvConverters() {
      return csvConverters;
    }
  }
}
