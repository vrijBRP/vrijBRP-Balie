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

package nl.procura.gba.web.rest.v2.model.zaken.base;

import java.util.List;

import lombok.Data;

@Data
public class GbaRestZaakAlgemeen {

  private String                zaakId;
  private String                meestRelevanteZaakId;
  private GbaRestZaakType       type;
  private String                soort;
  private String                omschrijving;
  private GbaRestZaakStatusType status;
  private String                bron;
  private String                leverancier;
  private Integer               datumIngang;
  private Integer               datumInvoer;
  private Integer               tijdInvoer;
  private GbaRestTabelWaarde    locatieInvoer;
  private GbaRestTabelWaarde    gebruikerInvoer;

  private List<GbaRestZaakId>        ids;
  private List<GbaRestZaakStatus>    statussen;
  private List<GbaRestZaakAttribuut> extraAttributen;
  private List<GbaRestZaakRelatie>   gekoppeldeZaken;
}
