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

package nl.procura.gba.web.modules.zaken.curatele.page1;

import static nl.procura.gba.web.modules.zaken.curatele.page1.CurateleUtils.getDatum;
import static nl.procura.gba.web.modules.zaken.curatele.page1.CurateleUtils.getElement;

import nl.procura.diensten.gba.ple.base.BasePLElem;
import nl.procura.diensten.gba.ple.extensions.formats.Geboorte;
import nl.procura.diensten.gba.ple.extensions.formats.Naam;
import nl.rechtspraak.namespaces.ccr.CCRWS;
import nl.rechtspraak.namespaces.ccr.CCRWS.Curandus;

public class CuratelePersoon {

  private final CCRWS    persoon;
  private final Naam     naam;
  private final Geboorte geboorte;

  public CuratelePersoon(CCRWS persoon) {

    Curandus c = persoon.getCurandus();

    this.persoon = persoon;
    this.naam = getNaam(c.getCurVoornamen(), c.getCurAchternaam(), c.getCurVoorvoegsels());
    this.geboorte = getGeboorte(getDatum(c.getGeboorteDatum()), c.getGeboortePlaats(), c.getGeboorteLand());
  }

  public Geboorte getGeboorte() {
    return geboorte;
  }

  public Naam getNaam() {
    return naam;
  }

  public String getOverlijdensDatum() {

    if (persoon.getCurandus().getOverlijdenDatum() != null) {
      return getDatum(persoon.getCurandus().getOverlijdenDatum()).getValue().getDescr();
    }
    return "";
  }

  public CCRWS getPersoon() {
    return persoon;
  }

  private Geboorte getGeboorte(BasePLElem datum, String plaats, String land) {
    return new Geboorte(datum, getElement(plaats), getElement(land));
  }

  private Naam getNaam(String vnaam, String anaam, String voorv) {
    return new Naam(getElement(vnaam), getElement(anaam), getElement(voorv), getElement(""), getElement(""), null);
  }
}
