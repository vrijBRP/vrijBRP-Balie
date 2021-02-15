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

package nl.procura.gba.web.services.bs.geboorte;

import static nl.procura.standard.Globalfunctions.fil;

import nl.procura.gba.web.services.bs.erkenning.ErkenningsType;
import nl.procura.gba.web.services.bs.naamskeuze.NaamskeuzeType;

public class DossierGeboorteVragen {

  private final DossierGeboorte geboorte;

  public DossierGeboorteVragen(DossierGeboorte geboorte) {
    this.geboorte = geboorte;
  }

  public boolean heeftErkenningBijGeboorte() {
    return geboorte.getErkenningBijGeboorte() != null;
  }

  public boolean heeftErkenningVoorGeboorte() {
    return geboorte.getErkenningVoorGeboorte() != null;
  }

  public boolean heeftErkenningBuitenProweb() {
    return fil(geboorte.getErkenningBuitenProweb().getAktenummer());
  }

  public boolean isHeeftGeenErkenning() {
    return geboorte.getErkenningsType().is(ErkenningsType.GEEN_ERKENNING, ErkenningsType.ONBEKEND);
  }

  public boolean magErkenningBijGeboorte() {
    return ErkenningsType.ERKENNING_BIJ_AANGIFTE.is(geboorte.getErkenningsType());
  }

  public boolean magErkenningBuitenProweb() {
    return ErkenningsType.ERKENNING_ONGEBOREN_VRUCHT.is(geboorte.getErkenningsType());
  }

  public boolean magErkenningVoorGeboorte() {
    return ErkenningsType.ERKENNING_ONGEBOREN_VRUCHT.is(geboorte.getErkenningsType());
  }

  public boolean heeftNaamskeuzeBuitenProweb() {
    return fil(geboorte.getNaamskeuzeBuitenProweb().getAktenummer());
  }

  public boolean heeftNaamskeuzeVoorGeboorte() {
    return geboorte.getNaamskeuzeVoorGeboorte() != null;
  }

  public boolean isHeeftGeenNaamskeuze() {
    return geboorte.getNaamskeuzeSoort().is(NaamskeuzeType.GEEN_NAAMSKEUZE, NaamskeuzeType.ONBEKEND);
  }
}
