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

package nl.procura.gba.web.services.beheer.verkiezing;

import nl.procura.gba.jpa.personen.db.KiesrVerk;

import lombok.Builder;
import lombok.Getter;
import nl.procura.validation.Anr;

@Getter
@Builder(builderMethodName = "internalBuilder")
public class StempasQuery {

  private int                   max;
  private Long                  vnr;
  private Long                  datumAanduidingVan;
  private Long                  datumAanduidingTm;
  private Boolean               opgenomenInROS;
  private StempasAanduidingType aanduidingType;
  private KiesrVerk             verkiezing;
  private Anr                   anrKiesgerechtigde;
  private Anr                   anrGemachtigde;
  private boolean               aflopend;

  public static StempasQueryBuilder builder(KiesrVerk kiesrVerk) {
    return internalBuilder().verkiezing(kiesrVerk);
  }
}
