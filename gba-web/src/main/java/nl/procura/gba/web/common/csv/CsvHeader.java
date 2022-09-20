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

import nl.procura.gba.web.common.csv.CsvConfig.CsvConfigBuilder;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CsvHeader {

  private String                         name;
  private List<Function<String, String>> converters;

  public boolean isName(String value) {
    return name.trim().equalsIgnoreCase(value.trim());
  }

  @Override
  public String toString() {
    return CsvParser.cleanHeader(name);
  }

  public static CsvHeaderBuilder builder() {
    return new CsvHeaderBuilder();
  }

  public static CsvHeaderBuilder builder(CsvConfigBuilder configBuilder) {
    return new CsvHeaderBuilder(configBuilder) {

      @Override
      public CsvHeader build() {
        if (super.configBuilder != null) {
          CsvHeader header = super.build();
          header.converters = new ArrayList<>(super.configBuilder.getCsvConverters());
          header.converters.addAll(super.headerConverters);
          return header;
        } else {
          return super.build();
        }
      }
    };
  }

  public static class CsvHeaderBuilder {

    private final List<Function<String, String>> headerConverters = new ArrayList<>();
    private CsvConfigBuilder                     configBuilder;

    public CsvHeaderBuilder() {
    }

    public CsvHeaderBuilder(CsvConfigBuilder configBuilder) {
      this.configBuilder = configBuilder;
    }

    public CsvHeaderBuilder converter(Function<String, String> converter) {
      this.headerConverters.add(converter);
      return this;
    }

    public CsvConfigBuilder and() {
      return configBuilder;
    }
  }
}
