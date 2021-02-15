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

package nl.procura.diensten.gba.ple.extensions;

import static nl.procura.standard.Globalfunctions.aval;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.procura.diensten.gba.ple.base.BasePL;
import nl.procura.diensten.gba.ple.base.PLEMessage;
import nl.procura.diensten.gba.ple.base.PLEResult;
import nl.procura.diensten.gba.ple.procura.arguments.PLEDatasource;

public class PLResultComposite {

  private static final String AANTAL_MAX    = "max";
  private static final String AANTAL_TOTAAL = "totaal";

  private final List<BasePLExt> extensions = new ArrayList<>();
  private final PLEResult       result;
  private Map<String, Integer>  numbers    = new HashMap<>();

  public PLResultComposite(PLEResult result) {

    setMaxAantal(result.getBasePLs().size());
    setTotaalAantal(result.getBasePLs().size());

    for (BasePL pl : result.getBasePLs()) {
      BasePLExt ext = new BasePLExt(pl);
      extensions.add(ext);
    }

    for (PLEMessage message : result.getMessages()) {
      if (message.getCode() == 201) {
        String[] p = message.getDescr().split(",");
        if (p.length == 2) {
          setTotaalAantal(aval(p[0].replaceAll("\\D+", "")));
          setMaxAantal(aval(p[1].replaceAll("\\D+", "")));
        }
      }
    }

    this.result = result;
  }

  public List<PLEMessage> getMeldingen() {
    return result.getMessages();
  }

  public PLEDatasource getDatabron() {
    return result.getDatasource();
  }

  public List<BasePLExt> getBasisPLWrappers() {
    return extensions;
  }

  public int getMaxAantal() {
    return numbers.get(AANTAL_MAX);
  }

  private void setMaxAantal(int aantal) {
    numbers.put(AANTAL_MAX, aantal);
  }

  public int getTotaalAantal() {
    return numbers.get(AANTAL_TOTAAL);
  }

  private void setTotaalAantal(int aantal) {
    numbers.put(AANTAL_TOTAAL, aantal);
  }

  public Map<String, Integer> getNumbers() {
    return numbers;
  }

  public PLEResult getResult() {
    return result;
  }
}
