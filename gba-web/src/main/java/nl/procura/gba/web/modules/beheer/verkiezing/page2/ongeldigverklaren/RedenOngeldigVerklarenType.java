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

package nl.procura.gba.web.modules.beheer.verkiezing.page2.ongeldigverklaren;

import static nl.procura.gba.web.services.beheer.verkiezing.KiezersregisterActieType.ACT_INTREKKEN_OVERIG;
import static nl.procura.gba.web.services.beheer.verkiezing.KiezersregisterActieType.ACT_ONGELDIG_OVERL;
import static nl.procura.gba.web.services.beheer.verkiezing.StempasAanduidingType.AAND_INTREKKEN_OVERIG;
import static nl.procura.gba.web.services.beheer.verkiezing.StempasAanduidingType.AAND_ONGELDIG_OVERL;

import java.text.MessageFormat;

import nl.procura.gba.jpa.personen.db.KiesrVerk;
import nl.procura.gba.web.services.beheer.verkiezing.KiezersregisterActieType;
import nl.procura.gba.web.services.beheer.verkiezing.StempasAanduidingType;
import nl.procura.standard.ProcuraDate;

public enum RedenOngeldigVerklarenType {

  REDEN_OVERLIJDEN(ACT_ONGELDIG_OVERL, AAND_ONGELDIG_OVERL,
      "Overleden sinds de dag van de kandidaatstelling ({0})",
      "select t.id.a1, t.id.a2, t.id.a3 from Overl t where t.hist = ''A'' and t.dOverl >= {0}"),
  REDEN_UITGESCHREVEN(ACT_INTREKKEN_OVERIG, AAND_INTREKKEN_OVERIG, "" +
      "Vertrokken vlak voor de dag van de kandidaatstelling ({0}), maar verwerking erna",
      "select t.id.a1, t.id.a2, t.id.a3 from Verw t where t.hist = ''A'' and t.dUitschr < {0} and t.dGba >= {0}");

  private final KiezersregisterActieType actieType;
  private final StempasAanduidingType    aanduidingType;
  private final String                   label;
  private final String                   sql;

  RedenOngeldigVerklarenType(KiezersregisterActieType actieType,
      StempasAanduidingType aanduidingType,
      String label,
      String sql) {
    this.actieType = actieType;
    this.aanduidingType = aanduidingType;
    this.label = label;
    this.sql = sql;
  }

  public String getSql(KiesrVerk verk) {
    return MessageFormat.format(sql, getDagKandidaatstelling(verk).getSystemDate());
  }

  public String getLabel(KiesrVerk verk) {
    return MessageFormat.format(label, getDagKandidaatstelling(verk).getFormatDate());
  }

  public KiezersregisterActieType getActieType() {
    return actieType;
  }

  public StempasAanduidingType getAanduidingType() {
    return aanduidingType;
  }

  private ProcuraDate getDagKandidaatstelling(KiesrVerk verk) {
    return new ProcuraDate(verk.getdKand());
  }
}
