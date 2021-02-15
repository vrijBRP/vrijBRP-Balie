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

package nl.procura.gba.web.services.gba.presentievraag;

import lombok.Data;

@Data
public class PresentievraagRequest {

  private Afzender afzender = new Afzender();
  private Vraag    vraag    = new Vraag();

  @Data
  public static class Afzender {

    private String berichtNr = "";
    private String afzender  = "";
  }

  @Data
  public static class Identiteitsgegevens {

    private String voornamen;
    private String voorvoegselGeslachtsnaam;
    private String geslachtsnaam;
    private String geboortedatum;
    private String geboorteplaats;
    private String geboorteland;
    private String geslachtsaanduiding;
    private String nationaliteit;
    private String buitenlandsPersoonsnummer;
    private String gemeenteVanInschrijving;
    private String datumAanvangAdresBuitenland;
  }

  @Data
  public static class Vraag {

    private String              vraagnummer         = "";
    private Identiteitsgegevens identiteitsgegevens = new Identiteitsgegevens();
  }
}
