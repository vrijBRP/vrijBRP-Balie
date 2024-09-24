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

package nl.procura.gba.web.services.beheer.kassa.piv4all;

import static nl.procura.commons.core.exceptions.ProExceptionSeverity.WARNING;
import static nl.procura.commons.core.exceptions.ProExceptionType.SELECT;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.web.services.beheer.kassa.KassaApplicationType;
import nl.procura.gba.web.services.beheer.kassa.KassaFile;
import nl.procura.gba.web.services.beheer.kassa.KassaParameters;
import nl.procura.gba.web.services.beheer.kassa.KassaProductAanvraag;
import nl.procura.commons.core.exceptions.ProException;

public class PIV4AllFile implements KassaFile {

  private final int             nr;
  private final String          timestamp;
  private final KassaParameters parameters;
  private final StringBuilder   content = new StringBuilder();

  private PIV4AllFile(KassaParameters parameters, int nr) {
    this.parameters = parameters;
    this.timestamp = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss_SSS").format(new Date());
    this.nr = nr;
  }

  public static List<KassaFile> of(KassaParameters parameters, List<KassaProductAanvraag> aanvragen) {
    if (aanvragen.isEmpty()) {
      throw new ProException(SELECT, WARNING, "Er zijn geen producten geselecteerd.");
    }

    int nr = 0;
    List<KassaFile> bestanden = new ArrayList<>();
    for (KassaProductAanvraag aanvraag : aanvragen) {
      PIV4AllFile bestand = new PIV4AllFile(parameters, ++nr);

      BasePLExt pl = aanvraag.getPl();
      String bsn = pl.getPersoon().getBsn().getVal();
      String naam = pl.getPersoon().getNaam().getNaamNaamgebruikEersteVoornaam();

      PIVLine pivLine = PIVLine.builder()
          .klantId(bsn)
          .klantnaam(naam)
          .zendendSysteem("VrijBRP")
          .gebruikerID("")
          .kassaID(0)
          .productCode(Integer.valueOf(aanvraag.getKassaProduct().getKassa().replaceAll("\\D+", "")))
          .geleverdAantal(1)
          .prijs(0)
          .build();

      bestand.append(pivLine.toLine());
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
    return KassaApplicationType.PIV4ALL;
  }

  @Override
  public String getFilename() {
    return String.format("%s/kas_vrijbrp_%s_%02d.piv", parameters.getFilename(), timestamp, nr);
  }

  @Override
  public int getNr() {
    return nr;
  }

  private void append(String line) {
    content.append(line).append("\r\n");
  }
}
