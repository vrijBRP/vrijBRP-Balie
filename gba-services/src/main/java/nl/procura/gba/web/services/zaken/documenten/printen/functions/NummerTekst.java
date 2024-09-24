/*
 * Copyright 2024 - 2025 Procura B.V.
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

package nl.procura.gba.web.services.zaken.documenten.printen.functions;

import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.aval;
import static nl.procura.standard.Globalfunctions.pos;

import java.util.List;

import nl.procura.gba.common.NormalNumberConverter;
import nl.procura.gba.common.NormalNumberConverter.Taal;

import freemarker.template.TemplateMethodModelEx;
import lombok.AllArgsConstructor;
import lombok.Data;

public class NummerTekst implements TemplateMethodModelEx {

  @Override
  public Result exec(List args) {
    if (!args.isEmpty()) {
      String nr = astr(args.get(0));
      if (pos(nr)) {
        String nl = NormalNumberConverter.toString(Taal.NL, aval(nr));
        String fr = NormalNumberConverter.toString(Taal.FRIES, aval(nr));
        return new Result(nl, fr);
      }
    }
    return new Result("", "");
  }

  @Data
  @AllArgsConstructor
  public static class Result {

    private final String nl;
    private final String fries;
  }
}
