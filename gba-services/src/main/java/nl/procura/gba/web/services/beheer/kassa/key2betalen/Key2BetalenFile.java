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
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.WARNING;
import static nl.procura.commons.core.exceptions.ProExceptionType.SELECT;

import java.util.ArrayList;
import java.util.List;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.web.services.beheer.kassa.KassaApplicationType;
import nl.procura.gba.web.services.beheer.kassa.KassaFile;
import nl.procura.gba.web.services.beheer.kassa.KassaParameters;
import nl.procura.gba.web.services.beheer.kassa.KassaProductAanvraag;
import nl.procura.commons.core.exceptions.ProException;

public class Key2BetalenFile implements KassaFile {

  private final int             nr;
  private final KassaParameters parameters;
  private final StringBuilder   content = new StringBuilder();

  private Key2BetalenFile(KassaParameters parameters, int nr) {
    this.parameters = parameters;
    this.nr = nr;
  }

  public static List<KassaFile> of(KassaParameters parameters, List<KassaProductAanvraag> aanvragen) {
    if (aanvragen.isEmpty()) {
      throw new ProException(SELECT, WARNING, "Er zijn geen producten geselecteerd.");
    }

    int nr = 0;
    List<KassaFile> bestanden = new ArrayList<>();
    for (KassaProductAanvraag aanvraag : aanvragen) {
      Key2BetalenFile bestand = new Key2BetalenFile(parameters, ++nr);

      BasePLExt pl = aanvraag.getPl();
      String anr = pl.getPersoon().getAnr().getVal();
      String naw1 = pl.getPersoon().getNaam().getNaamNaamgebruikEersteVoornaam();
      String naw2 = pl.getVerblijfplaats().getAdres().getAdres();
      String naw3 = pl.getVerblijfplaats().getAdres().getPcWpl();

      if (emp(parameters.getKassaId()) || emp(naw2) || emp(naw3)) {
        throw new ProException(SELECT, WARNING,
            "Kan geen kassa bestand maken. Het ID of de NAW-gegevens zijn niet ingevuld.");
      }

      bestand.append(parameters.getKassaId());
      bestand.append("A" + pad_right(aanvraag.getKassaProduct().getKassa(), " ", 10) + "+");
      bestand.append("N" + naw1);
      bestand.append("S" + naw2);
      bestand.append("P" + naw3);
      bestand.append("E");
      bestand.append(anr);
      bestand.content.append("\u001a");
      bestanden.add(bestand);
    }

    return bestanden;
  }

  @Override
  public String getContent() {
    return content.toString();
  }

  @Override
  public KassaApplicationType getKassaApplicationType() {
    return KassaApplicationType.KEY2BETALEN;
  }

  @Override
  public String getFilename() {
    return String.format("%s-%s-%d.001",
        parameters.getFilename(),
        parameters.getKassaLocatieId(), nr);
  }

  @Override
  public int getNr() {
    return nr;
  }

  private void append(String line) {
    content.append(line).append("\r\n");
  }
}
