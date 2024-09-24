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

package nl.procura.gba.web.modules.hoofdmenu.zoeken.quicksearch.person;

import java.util.Map;

import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.validation.Anr;
import nl.procura.validation.Bsn;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

@Data
@Builder
public class QuickSearchPersonConfig {

  private SelectListener selectListener;
  private Anr            anummer;
  private Bsn            bsn;
  private boolean        sameAddress;

  public String getId() {
    return anummer != null ? anummer.getAnummer() : bsn.getDefaultBsn();
  }

  public boolean hasBsnOrAnr() {
    return (anummer != null && anummer.isCorrect()) || (bsn != null && bsn.isCorrect());
  }

  @Singular
  private Map<String, NormalPageTemplate> pages;
}
