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

package examples.nl.procura.gbav.afnemers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.procura.diensten.gba.ple.base.*;
import nl.procura.diensten.gba.ple.procura.arguments.PLEArgs;
import nl.procura.diensten.gbav.utils.GbavResultaat;
import nl.procura.diensten.gbav.utils.GbavService;
import nl.procura.diensten.gbav.utils.acties.GbavAfnemerIndicatiesActie;
import nl.procura.diensten.gbav.utils.vraag.GbavVraagAntwoord;

public class TestAfnemerIndicatiesZoeken {

  private final static Logger LOGGER = LoggerFactory.getLogger(TestAfnemerIndicatiesZoeken.class.getName());

  public TestAfnemerIndicatiesZoeken() {

    try {
      String url = "https://<ip>/gba-v/online/lo3services/vraagAI";
      GbavService gbav = new GbavService("<username>", "<password>", url);

      GbavAfnemerIndicatiesActie actie = gbav.getActies().getAfnemerIndicatiesActie();
      GbavVraagAntwoord antwoord = actie.zoek(getArgumenten(), new BasePLBuilder());

      new Formatter(antwoord.getBasisPLHandler().getResult());
      showResult(antwoord.getResultaat());
      BasePLViewer.view(antwoord.getBasisPLHandler().getResult().getBasePLs());

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    new TestAfnemerIndicatiesZoeken();
  }

  public PLEArgs getArgumenten() {
    PLEArgs args = new PLEArgs();
    args.addNummer("*1351");
    args.setReasonForIndications("I");
    return args;
  }

  public void showResult(GbavResultaat result) {
    LOGGER.info("Result");
    LOGGER.info("---------");
    LOGGER.info("Code........: " + result.getCode());
    LOGGER.info("Letter......: " + result.getLetter());
    LOGGER.info("Omschrijving: " + result.getOmschrijving());
    LOGGER.info("Referentie..: " + result.getReferentie());
  }

  class Formatter extends BasePLFormatter {

    public Formatter(PLEResult ple) {
      super(ple);
    }

    @Override
    public String getNationalValue(int elementCode, BasePLValue value) {
      return "xyz";
    }
  }
}
