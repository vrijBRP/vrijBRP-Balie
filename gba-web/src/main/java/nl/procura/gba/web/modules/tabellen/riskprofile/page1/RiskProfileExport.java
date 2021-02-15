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

package nl.procura.gba.web.modules.tabellen.riskprofile.page1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.jpa.personen.types.RiskProfileRelatedCaseType;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class RiskProfileExport implements Serializable {

  private static final long serialVersionUID = 1004073378075203302L;

  private List<Rp> list = new ArrayList<>();

  @Data
  public static class Rp implements Serializable {

    private static final long serialVersionUID = 14511884093938935L;

    private String       name      = "";
    private Long         threshold = -1L;
    private List<RpType> types     = new ArrayList<>();
    private List<RpRule> rules     = new ArrayList<>();
  }

  @Data
  public static class RpRule implements Serializable {

    private static final long serialVersionUID = 3487549927344678510L;

    private String name  = "";
    private Long   type  = -1L;
    private Long   score = -1L;
    private Long   vnr   = -1L;
    private String attr  = "";
  }

  @Data
  @AllArgsConstructor
  public static class RpType implements Serializable {

    private static final long serialVersionUID = 2079626399483425284L;

    private String type = "";

    public RiskProfileRelatedCaseType toRiskProfileRelatedCaseType() {
      return RiskProfileRelatedCaseType.valueOf(type);
    }

    public static RpType of(RiskProfileRelatedCaseType type) {
      return new RpType(type.name());
    }
  }
}
