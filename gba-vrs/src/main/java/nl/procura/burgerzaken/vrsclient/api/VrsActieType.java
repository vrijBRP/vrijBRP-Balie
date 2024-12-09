package nl.procura.burgerzaken.vrsclient.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum VrsActieType {

  DEF_ONTREKK("Definitieve onttrekking"),
  RECHTSW_VERM("Van rechtswege vervallen (vermissing)"),
  RECHTSW_OVERIG("Van rechtswege vervallen (andere reden)"),
  ONBEKEND("");

  private final String description;
}
