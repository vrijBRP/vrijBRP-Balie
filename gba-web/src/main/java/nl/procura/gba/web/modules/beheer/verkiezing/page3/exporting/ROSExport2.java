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
import nl.procura.raas.message.elements.Voornamen;
import nl.procura.standard.ProcuraDate;
import nl.procura.validation.Postcode;

/**
 * Lichtbestand
 */
public class ROSExport2 implements ROSExport {

  @Override
  public String getBestandsnaam() {
    return "lichtbestand";
  }

  @Override
  public String getTitel() {
    return "Lichtbestand (CSV)";
  }

  @Override
  public List<String[]> getExport(List<KiesrStem> stempassen, Services services) {
    List<String[]> lines = new ArrayList<>();
    lines.add(
        new String[]{ "A-nummer", "Nummer", "Mutatiesoort", "Mutatiedatum", "Opgemaakte naam",
            "Voorletters", "Straat", "Huisnummer", "Huisletter", "Toevoeging",
            "Postcode", "Woonplaats" });
    for (KiesrStem kiesrStem : stempassen) {
      Stempas stempas = new Stempas(kiesrStem);
      lines.add(new String[]{
          stempas.getAnr().getAnummer(),
          getNummer(stempas),
          stempas.getAanduidingType().getType(),
          getMutatiedatum(stempas),
          stempas.getStem().getNaam(),
          stempas.getStem().getVoorn(),
          stempas.getStem().getStraat(),
          stempas.getStem().getHnr().toString(),
          stempas.getStem().getHnrL(),
          stempas.getStem().getHnrT(),
          stempas.getPostcode(),
          stempas.getStem().getWpl()
      });
    }
    return lines;
  }

  private String getMutatiedatum(Stempas stempas) {
    return new ProcuraDate(stempas.getStem().getdAand().intValue()).getFormatDate("dd-MM-yyyy");
  }

  private String getNummer(Stempas stempas) {
    return stempas.getStem().getKiesrVerk().getAfkVerkiezing()
        + " " + stempas.getPasnummer();
  }

  @Override
  public String toString() {
    return getTitel();
  }
}
