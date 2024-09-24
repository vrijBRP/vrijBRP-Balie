/*
 * Copyright 2023 - 2024 Procura B.V.
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

package nl.procura.gba.web.services.bs.algemeen.interfaces;

import static nl.procura.standard.Globalfunctions.pos;

import java.util.List;

import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.erkenning.EersteKindType;
import nl.procura.gba.web.services.bs.erkenning.KindLeeftijdsType;
import nl.procura.gba.web.services.bs.erkenning.NaamsPersoonType;
import nl.procura.gba.web.services.bs.erkenning.NaamskeuzeVanToepassingType;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public interface DossierNamenrecht {

  Dossier getDossier();

  /**
   * Is dit het eerste kind
   */
  EersteKindType getEersteKindType();

  void setEersteKindType(EersteKindType eersteKindType);

  /**
   * Geslachtsnaam
   */

  String getOrigineleKeuzeNaam();

  String getKeuzeGeslachtsnaam();

  void setKeuzeGeslachtsnaam(String geslachtsnaam);

  String getKeuzeNaam();

  /**
   * Titel/predikaat
   */
  FieldValue getKeuzeTitel();

  void setKeuzeTitel(FieldValue titel);

  /**
   * Voorvoegsel
   */
  String getKeuzeVoorvoegsel();

  void setKeuzeVoorvoegsel(String voorvoegsel);

  List<DossierPersoon> getKinderen();

  /**
   * Type leeftijd
   */
  KindLeeftijdsType getKindLeeftijdsType();

  /**
   * Land
   */
  FieldValue getLandNaamRecht();

  void setLandNaamRecht(FieldValue land);

  DossierPersoon getMoeder();

  /**
   * De persoon die de naam levert
   */
  NaamsPersoonType getNaamskeuzePersoon();

  void setNaamskeuzePersoon(NaamsPersoonType naamsPersoonType);

  /**
   * Is naamskeuze van toepassing
   */
  NaamskeuzeVanToepassingType getNaamskeuzeType();

  void setNaamskeuzeType(NaamskeuzeVanToepassingType naamskeuze);

  DossierPersoon getVaderErkenner();

  boolean isGeborenBinnenHuwelijk();

  default boolean isNaamRechtBepaald() {
    return getLandNaamRecht() != null && pos(getLandNaamRecht().getValue());
  }

}
