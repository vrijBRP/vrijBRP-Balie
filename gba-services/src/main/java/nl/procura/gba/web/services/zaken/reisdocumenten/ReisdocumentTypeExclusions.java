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

package nl.procura.gba.web.services.zaken.reisdocumenten;

import static nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentType.*;

import java.util.Arrays;

/**
 * Reisdocumenten die niet samen mogen voorkomen
 */
public enum ReisdocumentTypeExclusions {

  NP(EERSTE_NATIONAAL_PASPOORT, EERSTE_ZAKENPASPOORT),
  ZP(EERSTE_ZAKENPASPOORT, EERSTE_NATIONAAL_PASPOORT),
  PB(VREEMDELINGEN_PASPOORT, VLUCHTELINGEN_PASPOORT),
  PV(VLUCHTELINGEN_PASPOORT, VREEMDELINGEN_PASPOORT),
  TN(TWEEDE_NATIONAAL_PASPOORT, TWEEDE_ZAKENPASPOORT),
  TE(TWEEDE_ZAKENPASPOORT, TWEEDE_NATIONAAL_PASPOORT);

  private ReisdocumentType   type;
  private ReisdocumentType[] types;

  ReisdocumentTypeExclusions(ReisdocumentType type, ReisdocumentType... types) {
    this.type = type;
    this.types = types;
  }

  public static boolean exclude(ReisdocumentType reisdocumentType, ReisdocumentType existingDocument) {
    if (reisdocumentType == existingDocument) {
      return true;
    }
    for (ReisdocumentTypeExclusions ex : values()) {
      if (ex.getType() == reisdocumentType) {
        if (Arrays.asList(ex.getTypes()).contains(existingDocument)) {
          return true;
        }
      }
    }
    return false;
  }

  public ReisdocumentType getType() {
    return type;
  }

  public void setType(ReisdocumentType type) {
    this.type = type;
  }

  public ReisdocumentType[] getTypes() {
    return types;
  }

  public void setTypes(ReisdocumentType[] types) {
    this.types = types;
  }
}
