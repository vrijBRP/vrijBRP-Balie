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

import static nl.procura.standard.Globalfunctions.emp;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.WARNING;
import static nl.procura.commons.core.exceptions.ProExceptionType.SELECT;

import java.util.Collections;
import java.util.List;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.web.services.beheer.kassa.KassaApplicationType;
import nl.procura.gba.web.services.beheer.kassa.KassaFile;
import nl.procura.gba.web.services.beheer.kassa.KassaParameters;
import nl.procura.gba.web.services.beheer.kassa.KassaProductAanvraag;
import nl.procura.commons.core.exceptions.ProException;

public class GKasFile implements KassaFile {

  private final KassaParameters parameters;
  private final StringBuilder   content = new StringBuilder();

  public GKasFile(KassaParameters parameters) {
    this.parameters = parameters;
  }

  public static List<KassaFile> of(KassaParameters parameters, List<KassaProductAanvraag> aanvragen) {

    String naw1;
    String naw2;
    String naw3;

    if (aanvragen.isEmpty()) {
      throw new ProException(SELECT, WARNING, "Er zijn geen producten geselecteerd.");
    }

    KassaProductAanvraag kpa = aanvragen.get(0);
    BasePLExt pl = kpa.getPl();

    naw1 = pl.getPersoon().getNaam().getNaamNaamgebruikEersteVoornaam();
    naw2 = pl.getVerblijfplaats().getAdres().getAdres();
    naw3 = pl.getVerblijfplaats().getAdres().getPcWpl();

    if (emp(parameters.getKassaId()) || emp(naw2) || emp(naw3)) {
      throw new ProException(SELECT, WARNING,
          "Kan geen kassa bestand maken. Het ID of de NAW-gegevens zijn niet ingevuld.");
    }

    GKasFile bestand = new GKasFile(parameters);
    bestand.append(parameters.getKassaId());
    bestand.append(naw1);
    bestand.append(naw2);
    bestand.append(naw3);

    for (KassaProductAanvraag a : aanvragen) {
      bestand.append(a.getKassaProduct().getKassa());
    }

    return Collections.singletonList(bestand);
  }

  @Override
  public String getContent() {
    return content.toString();
  }

  @Override
  public KassaApplicationType getKassaApplicationType() {
    return KassaApplicationType.GKAS;
  }

  @Override
  public String getFilename() {
    return parameters.getFilename();
  }

  @Override
  public int getNr() {
    return 1;
  }

  private void append(String line) {
    content.append(line).append("\n");
  }
}
