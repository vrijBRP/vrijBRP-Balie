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

package nl.procura.diensten.gba.ple.procura.arguments;

import java.util.ArrayList;
import java.util.List;

public class PLEArgsSplitter {

  public static List<PLEArgs> verdeel(PLEArgs z) {
    List<PLEArgs> array = new ArrayList<>();
    try {
      if (z.getNumbers().size() > 1) {
        for (PLNumber n : z.getNumbers()) {
          PLEArgs nz = new PLEArgs();
          nz.setGeboortedatum(z.getGeboortedatum());
          nz.setGemeente(z.getGemeente());
          nz.setGemeentedeel(z.getGemeentedeel());
          nz.setGeslachtsnaam(z.getGeslachtsnaam());
          nz.setHuisnummer(z.getHuisnummer());
          nz.setPostcode(z.getPostcode());
          nz.setStraat(z.getStraat());
          nz.setTitel(z.getTitel());
          nz.setVoornaam(z.getVoornaam());
          nz.setVoorvoegsel(z.getVoorvoegsel());
          PLNumber nn = new PLNumber(n.getA1(), n.getA2(), n.getA3(), n.getBsn(), n.getSource());
          nz.getNumbers().add(nn);
          array.add(nz);
        }
      } else {
        array.add(z);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return array;
  }
}
