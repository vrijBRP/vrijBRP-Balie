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

package examples.nl.procura.gbav.wachtwoord;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.procura.diensten.gbav.exceptions.GbavArgumentException;
import nl.procura.diensten.gbav.exceptions.GbavConfigException;
import nl.procura.diensten.gbav.utils.GbavAntwoord;
import nl.procura.diensten.gbav.utils.GbavResultaat;
import nl.procura.diensten.gbav.utils.GbavService;
import nl.procura.diensten.gbav.utils.acties.GbavWachtwoordActie;

public class TestVolledigeGbavWachtwoord {

  private final static Logger LOGGER = LoggerFactory.getLogger(TestVolledigeGbavWachtwoord.class.getName());

  public TestVolledigeGbavWachtwoord() {

    try {
      String endpoint = "https://<ip>/gba-v/online/lo3services/adhoc";
      GbavService gbav = new GbavService("<username>", "<password>", endpoint);

      GbavWachtwoordActie actie = gbav.getActies().getWachtwoordActie();
      GbavAntwoord antwoord = actie.wijzig("12345");
      showResult(antwoord.getResultaat());

      LOGGER.info("Vraagbericht...: " + antwoord.getXml().getVraag());
      LOGGER.info("Antwoordbericht: " + antwoord.getXml().getAntwoord());

    } catch (GbavConfigException e) {
      LOGGER.info(e.getMessage());
    } catch (GbavArgumentException e) {
      LOGGER.info(e.getMessage());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    new TestVolledigeGbavWachtwoord();
  }

  public void showResult(GbavResultaat result) {

    LOGGER.info("Result");
    LOGGER.info("---------");
    LOGGER.info("Fout........: " + result.isFout());
    LOGGER.info("Code........: " + result.getCode());
    LOGGER.info("Letter......: " + result.getLetter());
    LOGGER.info("Omschrijving: " + result.getOmschrijving());
    LOGGER.info("Referentie..: " + result.getReferentie());
  }
}
