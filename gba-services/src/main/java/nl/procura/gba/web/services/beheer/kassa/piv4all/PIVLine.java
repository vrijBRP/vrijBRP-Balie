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

package nl.procura.gba.web.services.beheer.kassa.piv4all;

import static nl.procura.gba.web.services.beheer.kassa.piv4all.PIVElementType.*;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jetbrains.annotations.NotNull;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class PIVLine {

  private String           line;
  private String           klantId;
  private String           klantnaam;
  private String           zendendSysteem;
  private String           gebruikerID;
  private Integer          kassaID;
  private Integer          productCode;
  private Integer          datum;
  private Integer          tijd;
  private Integer          geleverdAantal;
  private Integer          prijs;
  private List<PIVElement> elements;

  public byte[] toFileBytes() {
    return (toLine() + "\r\n").getBytes();
  }

  public String toLine() {
    return elements.stream()
        .map(PIVElement::getValue)
        .collect(Collectors.joining());
  }

  public String toLineWithElements() {
    return elements.stream()
        .map(element -> StringUtils.rightPad(element.getType().getLabel(), 30)
            + " [" + element.getValue() + "]\n")
        .collect(Collectors.joining());
  }

  private String getValue(PIVElementType type) {
    return elements.stream()
        .filter(e -> e.getType() == type)
        .map(PIVElement::getValue)
        .findFirst().orElse("");
  }

  public static PIVLine.PIVLineBuilder builder() {

    return new PIVLineBuilder() {

      @Override
      public PIVLine build() {
        PIVLine pivFile = super.build();
        pivFile.elements = new ArrayList<>();
        List<PIVElement> elements = pivFile.elements;
        if (isNotBlank(pivFile.line)) {
          for (PIVElementType type : PIVElementType.values()) {
            elements.add(PIVElement.fromLine(type, pivFile.line));
          }
          pivFile.setKlantId(pivFile.getValue(KLANTEN_IDENTIFICATIE));
          pivFile.setKlantnaam(pivFile.getValue(KLANTENNAAM));
          pivFile.setZendendSysteem(pivFile.getValue(ZENDEND_SYSTEEM));
          pivFile.setGebruikerID(pivFile.getValue(GEBRUIKERS_ID));
          pivFile.setKassaID(toInteger(pivFile, KASSA_ID));
          pivFile.setDatum(toInteger(pivFile, DATUM));
          pivFile.setTijd(toInteger(pivFile, TIJD));
          pivFile.setProductCode(toInteger(pivFile, PRODUCTCODE));
          pivFile.setGeleverdAantal(toInteger(pivFile, GELEVERD_AANTAL));
          pivFile.setPrijs(toInteger(pivFile, PRIJS));
        } else {
          elements.add(PIVElement.fromInput(KLANTEN_IDENTIFICATIE, pivFile.klantId));
          elements.add(PIVElement.fromInput(NAMESPACE, ""));
          elements.add(PIVElement.fromInput(KLANTENNAAM, pivFile.klantnaam));
          elements.add(PIVElement.fromInput(ZENDEND_SYSTEEM, pivFile.zendendSysteem));
          elements.add(PIVElement.fromInput(GEBRUIKERS_ID, pivFile.gebruikerID));
          elements.add(PIVElement.fromInput(KASSA_ID, pivFile.kassaID));
          elements.add(PIVElement.fromInput(DATUM, toDate(pivFile.getDatum())));
          elements.add(PIVElement.fromInput(TIJD, toTime(pivFile.getTijd())));
          elements.add(PIVElement.fromInput(PRODUCTCODE, pivFile.productCode));
          elements.add(PIVElement.fromInput(TEKEN_GELEVERD_AANTAL, "+"));
          elements.add(PIVElement.fromInput(GELEVERD_AANTAL, pivFile.geleverdAantal));
          elements.add(PIVElement.fromInput(INDICATIE_PRIJSBETALING, "O"));
          elements.add(PIVElement.fromInput(TEKEN_PRIJS, "+"));
          elements.add(PIVElement.fromInput(PRIJS, pivFile.getPrijs()));
        }
        return pivFile;
      }

      @NotNull
      private Integer toInteger(PIVLine pivFile, PIVElementType type) {
        return NumberUtils.toInt(pivFile.getValue(type).trim(), 0);
      }

      private Integer toDate(Integer datum) {
        return datum != null ? datum : Integer.valueOf(LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd")));
      }

      private Integer toTime(Integer tijd) {
        return tijd != null ? tijd : Integer.valueOf(LocalTime.now().format(DateTimeFormatter.ofPattern("HHmm")));
      }
    };
  }
}
