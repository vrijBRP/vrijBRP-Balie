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

package nl.procura.gba.web.components.containers;

public class Container {

  public static WoonplaatsContainer              WOONPLAATS        = new WoonplaatsContainer();
  public static PlaatsContainer                  PLAATS            = new PlaatsContainer();
  public static LandContainer                    LAND              = new LandContainer();
  public static NatioContainer                   NATIO             = new NatioContainer();
  public static WijkContainer                    WIJK              = new WijkContainer();
  public static BuurtContainer                   BUURT             = new BuurtContainer();
  public static SubbuurtContainer                SUBBUURT          = new SubbuurtContainer();
  public static LocatieContainer                 LOCATIE           = new LocatieContainer();
  public static GemdeelContainer                 GEMDEEL           = new GemdeelContainer();
  public static AanduidingContainer              AANDUIDING        = new AanduidingContainer();
  public static AdelijkeTitelPredikaatContainer  TITEL             = new AdelijkeTitelPredikaatContainer();
  public static VerblijfstitelContainer          VBT               = new VerblijfstitelContainer();
  public static RedenNationaliteitContainer      REDEN_VERKRIJGING = new RedenNationaliteitContainer();
  public static RedenHuwelijkOntbindingContainer REDEN_HUW_ONTB    = new RedenHuwelijkOntbindingContainer();
}
