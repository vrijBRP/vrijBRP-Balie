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

import org.jetbrains.annotations.NotNull;

import nl.procura.gba.jpa.personen.db.KiesrStem;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.beheer.verkiezing.Stempas;

public class ROSExport3 implements ROSExport {

  @Override
  public String getBestandsnaam() {
    return "ROS-StembureauApp";
  }

  @Override
  public String getTitel() {
    return "Bestand voor de StembureauApp (CSV)";
  }

  @Override
  public List<String[]> getExport(List<KiesrStem> stempassen, Services services) {
    List<String[]> lines = new ArrayList<>();
    for (KiesrStem kiesrStem : stempassen) {
      Stempas stempas = new Stempas(kiesrStem);
      lines.add(new String[]{ getQrText(stempas)
      });
    }
    return lines;
  }

  @NotNull
  private String getQrText(Stempas stempas) {
    KiesrStem stem = stempas.getStem();
    return stem.getKiesrVerk().getAfkVerkiezing() + "/" + stem.getPasNr();
  }

  @Override
  public String toString() {
    return getTitel();
  }
}
