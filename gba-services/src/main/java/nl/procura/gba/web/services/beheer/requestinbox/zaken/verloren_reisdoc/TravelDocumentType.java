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

package nl.procura.gba.web.services.beheer.requestinbox.zaken.verloren_reisdoc;

import static nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentType.EERSTE_NATIONAAL_PASPOORT;
import static nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentType.EERSTE_ZAKENPASPOORT;
import static nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentType.FACILITEITEN_PASPOORT;
import static nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentType.NEDERLANDSE_IDENTITEITSKAART;
import static nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentType.TWEEDE_NATIONAAL_PASPOORT;
import static nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentType.TWEEDE_ZAKENPASPOORT;
import static nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentType.VLUCHTELINGEN_PASPOORT;
import static nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentType.VREEMDELINGEN_PASPOORT;

import nl.procura.burgerzaken.requestinbox.api.model.InboxEnum;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentType;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TravelDocumentType implements InboxEnum<String> {

  PN(EERSTE_NATIONAAL_PASPOORT),
  NI(NEDERLANDSE_IDENTITEITSKAART),
  ZN(EERSTE_ZAKENPASPOORT),
  PB(VREEMDELINGEN_PASPOORT),
  PV(VLUCHTELINGEN_PASPOORT),
  TE(TWEEDE_ZAKENPASPOORT),
  TN(TWEEDE_NATIONAAL_PASPOORT),
  PF(FACILITEITEN_PASPOORT),
  ONBEKEND(ReisdocumentType.ONBEKEND);

  private final ReisdocumentType type;

  @Override
  public String toString() {
    return getDescr();
  }

  @Override
  public String getId() {
    return type.getCode();
  }

  @Override
  public String getDescr() {
    return type.getOms();
  }
}
