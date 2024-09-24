package nl.procura.gba.web.modules.bs.lv.page40;

import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "code")
@AllArgsConstructor
public class KeuzeOuder {

  private final Long           code;
  private final DossierPersoon persoon;

  public KeuzeOuder(DossierPersoon persoon) {
    this(persoon.getCode(), persoon);
  }

  @Override
  public String toString() {
    String naam = persoon.getNaam().getNaam_naamgebruik_eerste_voornaam();
    String geslacht = persoon.getGeslacht().getNormaal();
    return naam + " (" + geslacht + ")";
  }
}
