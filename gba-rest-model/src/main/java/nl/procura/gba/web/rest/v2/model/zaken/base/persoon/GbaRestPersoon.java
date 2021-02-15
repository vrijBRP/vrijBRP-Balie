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

package nl.procura.gba.web.rest.v2.model.zaken.base.persoon;

import java.util.List;

import nl.procura.gba.web.rest.v2.model.base.GbaRestGeslacht;
import nl.procura.gba.web.rest.v2.model.base.HeeftContactgegevens;
import nl.procura.gba.web.rest.v2.model.zaken.base.GbaRestTabelWaarde;
import nl.procura.gba.web.rest.v2.model.zaken.base.natio.GbaRestNationaliteit;
import nl.procura.gba.web.rest.v2.model.zaken.geboorte.GbaRestBurgerlijkeStaatType;
import nl.procura.gba.web.rest.v2.model.zaken.verhuizing.GbaRestContactgegevens;

import lombok.Data;

@Data
public class GbaRestPersoon implements HeeftContactgegevens {

  private Long                        bsn;
  private GbaRestContactgegevens      contactgegevens;
  private String                      voornamen;
  private String                      geslachtsnaam;
  private String                      voorvoegsel;
  private String                      titelPredikaat;
  private String                      aktenaam;
  private GbaRestGeslacht             geslacht;
  private GbaRestPersoonAdres         adres;
  private Integer                     geboortedatum;
  private GbaRestTabelWaarde          geboorteplaats;
  private String                      geboorteplaatsOpAkte;
  private GbaRestTabelWaarde          geboorteland;
  private String                      geboortelandOpAkte;
  private Integer                     immigratieDatum;
  private GbaRestTabelWaarde          immigratieLand;
  private Integer                     overlijdensDatum;
  private GbaRestBurgerlijkeStaatType burgerlijkeStaat;
  private Integer                     burgerlijkeStaatDatum;
  private Boolean                     verstrekkingsbeperking;
  private GbaRestTabelWaarde          verblijfstitel;
  private String                      toelichting;
  private List<GbaRestNationaliteit>  nationaliteiten;
}
