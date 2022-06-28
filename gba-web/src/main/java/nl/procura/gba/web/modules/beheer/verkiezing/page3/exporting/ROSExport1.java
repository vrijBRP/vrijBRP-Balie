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

package nl.procura.gba.web.modules.beheer.verkiezing.page3.exporting;

import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.jpa.personen.db.KiesrStem;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.beheer.verkiezing.Stempas;

public class ROSExport1 implements ROSExport {

  @Override
  public String getBestandsnaam() {
    return "ROS-formaat1";
  }

  @Override
  public String getTitel() {
    return "Uitvoerformaat 1 (CSV)";
  }

  @Override
  public List<String[]> getExport(List<KiesrStem> stempassen, Services services) {
    List<String[]> lines = new ArrayList<>();
    lines.add(
        new String[]{ "Gemeente", "Pasnummer", "Volgnummer",
            "Aanduiding (kort)", "Aanduiding (omschrijving)", "Tijdstip aanduiding",
            "Voorletters", "Naam", "Adres", "Postcode", "Woonplaats" });
    for (KiesrStem kiesrStem : stempassen) {
      Stempas stempas = new Stempas(kiesrStem);
      String gemeente = services.getGebruiker().getGemeente();
      lines.add(new String[]{
          gemeente,
          stempas.getPasnummer(),
          stempas.getVolgnr().toString(),
          stempas.getAanduidingType().getType(),
          stempas.getAanduidingType().getOms(),
          stempas.getAanduidingTijdstip(),
          stempas.getStem().getVoorn(),
          stempas.getStem().getNaam(),
          stempas.getAdres(),
          stempas.getPostcode(),
          stempas.getStem().getWpl(),
      });
    }
    return lines;
  }

  @Override
  public String toString() {
    return getTitel();
  }
}
