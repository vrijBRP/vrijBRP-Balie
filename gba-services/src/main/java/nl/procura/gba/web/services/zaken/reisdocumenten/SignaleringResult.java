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

import static java.util.Collections.emptyList;
import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.List;
import java.util.Optional;

import nl.procura.burgerzaken.vrsclient.model.Persoon;
import nl.procura.burgerzaken.vrsclient.model.Signalering;
import nl.procura.burgerzaken.vrsclient.model.SignaleringResponse.ResultaatCodeEnum;

import lombok.Builder;
import lombok.Getter;

/**
 * Signalering with short description.
 */
@Builder
@Getter
public class SignaleringResult {

  private final ResultaatCodeEnum resultaatCode;
  private final String            resultaatOmschrijving;
  private final String            mededelingRvIG;
  private final Persoon           persoon;

  @Builder.Default
  private final List<Signalering> signaleringen = emptyList();

  public Optional<String> getMededelingRvIG() {
    return isBlank(mededelingRvIG) ? Optional.empty() : Optional.of(mededelingRvIG);
  }

  public boolean isHit() {
    return ResultaatCodeEnum.NO_HIT != resultaatCode;
  }
}
