package nl.procura.burgerzaken.vrsclient.api;

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum VrsDocumentStatusType {

  IN_AANVRAAG("IA", "In aanvraag"),
  GELDIG("GD", "Geldig"),
  ONGELDIG("OG", "Ongeldig"),
  DEFINITEF_ONTTROKKEN("DO", "Definitief onttrokken"),
  ONBEKEND("", "Onbekend");

  private final String code;
  private final String description;


  public static VrsDocumentStatusType getByCode(String code) {
    return Arrays.stream(values())
        .filter(a -> a.getCode().equals(code))
        .findFirst()
        .orElse(ONBEKEND);
  }

  @Override
  public String toString() {
    return description + (isBlank(code) ? "" : " (" + code + ")");
  }

  public boolean in(VrsDocumentStatusType... statusTypes) {
    return Arrays.stream(statusTypes).anyMatch(a -> a == this);
  }
}
