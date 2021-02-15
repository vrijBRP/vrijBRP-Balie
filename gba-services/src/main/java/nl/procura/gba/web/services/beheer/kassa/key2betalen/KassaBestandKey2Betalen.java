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

package nl.procura.gba.web.services.beheer.kassa.key2betalen;

import static nl.procura.standard.Globalfunctions.emp;
import static nl.procura.standard.Globalfunctions.pad_right;
import static nl.procura.standard.exceptions.ProExceptionSeverity.WARNING;
import static nl.procura.standard.exceptions.ProExceptionType.SELECT;

import java.util.ArrayList;
import java.util.List;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.web.services.beheer.kassa.KassaProductAanvraag;
import nl.procura.gba.web.services.beheer.kassa.gkas.KassaParameters;
import nl.procura.standard.exceptions.ProException;

/**
 * Stelt kassa bericht samen
 */
public class KassaBestandKey2Betalen {

  private final KassaParameters parameters;
  private StringBuilder         sb = null;

  public KassaBestandKey2Betalen(KassaParameters parameters) {
    this.parameters = parameters;
  }

  public List<String> getBestanden(List<KassaProductAanvraag> aanvragen) {

    List<String> bestanden = new ArrayList<>();

    if (aanvragen.isEmpty()) {
      throw new ProException(SELECT, WARNING, "Er zijn geen producten geselecteerd.");
    }

    for (KassaProductAanvraag aanvraag : aanvragen) {

      sb = new StringBuilder();

      BasePLExt pl = aanvraag.getPl();

      String anr = pl.getPersoon().getAnr().getVal();
      String naw1 = pl.getPersoon().getNaam().getNaamNaamgebruikEersteVoornaam();
      String naw2 = pl.getVerblijfplaats().getAdres().getAdres();
      String naw3 = pl.getVerblijfplaats().getAdres().getPcWpl();

      if (emp(parameters.getKassaId()) || emp(naw2) || emp(naw3)) {
        throw new ProException(SELECT, WARNING,
            "Kan geen kassa bestand maken. Het ID of de NAW-gegevens zijn niet ingevuld.");
      }

      add(parameters.getKassaId());
      add("A" + pad_right(aanvraag.getKassaProduct().getKassa(), " ", 10) + "+");
      add("N" + naw1);
      add("S" + naw2);
      add("P" + naw3);
      add("E");
      add(anr);
      sb.append("\u001a");

      bestanden.add(sb.toString());
    }

    return bestanden;
  }

  private void add(String line) {
    sb.append(line + "\r\n");
  }
}
