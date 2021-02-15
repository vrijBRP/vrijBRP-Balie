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

package nl.procura.gba.web.services.zaken.algemeen.controle;

import static nl.procura.standard.Globalfunctions.trim;

import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.ZaakUtils;

public class ZaakControle<T extends Zaak> extends StandaardControle {

  private Zaak zaak;

  public ZaakControle(String onderwerp, Zaak zaak) {
    super(onderwerp, ZaakUtils.getTypeEnOmschrijving(zaak));
    setId(zaak.getZaakId());
    this.zaak = zaak;
  }

  @Override
  public List<String> getOpmerkingen() {
    List<String> opmerkingen = new ArrayList<>();
    opmerkingen.addAll(super.getOpmerkingen());
    opmerkingen.addAll(zaak.getZaakHistorie().getWijzigingen());
    return opmerkingen;
  }

  public String getString() {
    return trim(getZaak().getZaakHistorie().getWijzigingenTekst());
  }

  public T getZaak() {
    return (T) zaak;
  }

  public void setZaak(T aanvraag) {
    this.zaak = aanvraag;
  }

  @Override
  public boolean isGewijzigd() {
    return getZaak().getZaakHistorie().getWijzigingen().size() > 0;
  }
}
