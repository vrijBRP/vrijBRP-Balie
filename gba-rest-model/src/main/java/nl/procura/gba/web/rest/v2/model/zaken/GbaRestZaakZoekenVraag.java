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

package nl.procura.gba.web.rest.v2.model.zaken;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nl.procura.gba.web.rest.v2.model.GbaRestJackson;
import nl.procura.gba.web.rest.v2.model.zaken.base.*;

import lombok.Data;

@Data
@GbaRestJackson
public class GbaRestZaakZoekenVraag {

  private String                       persoonId;
  private List<String>                 zaakIds        = new ArrayList<>();
  private List<GbaRestZaakType>        zaakTypes      = new ArrayList<>();
  private List<GbaRestZaakStatusType>  zaakStatussen  = new ArrayList<>();
  private List<GbaRestZaakAttribuut>   zaakAttributen = new ArrayList<>();
  private GbaRestPeriode               ingangsDatum;
  private GbaRestPeriode               invoerDatum;
  private GbaRestPeriode               mutatieDatum;
  private GbaRestZaakSorteringType     sortering;
  private Integer                      max;
  private List<GbaRestZaakZoekGegeven> zoekGegevens   = new ArrayList<>();

  @Transient
  public boolean isZoekGegeven(GbaRestZaakZoekGegeven... gegevens) {
    return this.zoekGegevens.stream()
        .anyMatch(gegeven -> Arrays.stream(gegevens)
            .anyMatch(val -> gegeven == val));
  }
}
