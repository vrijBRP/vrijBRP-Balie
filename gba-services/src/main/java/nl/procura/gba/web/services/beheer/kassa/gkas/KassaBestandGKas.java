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

package nl.procura.gba.web.services.beheer.kassa.gkas;

import static java.util.Arrays.asList;
import static nl.procura.standard.Globalfunctions.emp;
import static nl.procura.standard.exceptions.ProExceptionSeverity.WARNING;
import static nl.procura.standard.exceptions.ProExceptionType.SELECT;

import java.util.List;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.web.services.beheer.kassa.KassaProductAanvraag;
import nl.procura.standard.exceptions.ProException;

/**
 * Stelt kassa bericht samen
 */
public class KassaBestandGKas {

  private final KassaParameters parameters;
  private final StringBuilder   sb = new StringBuilder();

  public KassaBestandGKas(KassaParameters parameters) {
    this.parameters = parameters;
  }

  public List<String> getBestanden(List<KassaProductAanvraag> aanvragen) {

    String naw1 = "";
    String naw2 = "";
    String naw3 = "";

    if (aanvragen.isEmpty()) {
      throw new ProException(SELECT, WARNING, "Er zijn geen producten geselecteerd.");
    }

    if (aanvragen.size() > 0) {

      KassaProductAanvraag kpa = aanvragen.get(0);
      BasePLExt pl = kpa.getPl();

      naw1 = pl.getPersoon().getNaam().getNaamNaamgebruikEersteVoornaam();
      naw2 = pl.getVerblijfplaats().getAdres().getAdres();
      naw3 = pl.getVerblijfplaats().getAdres().getPcWpl();
    }

    if (emp(parameters.getKassaId()) || emp(naw2) || emp(naw3)) {
      throw new ProException(SELECT, WARNING,
          "Kan geen kassa bestand maken. Het ID of de NAW-gegevens zijn niet ingevuld.");
    }

    add(parameters.getKassaId());
    add(naw1);
    add(naw2);
    add(naw3);

    for (KassaProductAanvraag a : aanvragen) {
      add(a.getKassaProduct().getKassa());
    }

    return asList(sb.toString());
  }

  private void add(String line) {
    sb.append(line + "\n");
  }
}
