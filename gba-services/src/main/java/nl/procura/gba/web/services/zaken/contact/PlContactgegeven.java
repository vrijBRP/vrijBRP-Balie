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

package nl.procura.gba.web.services.zaken.contact;

import java.io.Serializable;

import nl.procura.gba.common.DateTime;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PlContactgegeven implements Serializable {

  private static final long serialVersionUID = 4163358834703952912L;
  private String            persoon          = "Aangever";
  private long              anr              = -1;
  private long              bsn              = -1;
  private long              country          = -1;
  private String            aant             = "";
  private DateTime          datum            = new DateTime(0);
  private Contactgegeven    contactgegeven   = new Contactgegeven();

  public PlContactgegeven() {
  }

  public PlContactgegeven(long anr, long bsn, String persoon, DateTime datum, Contactgegeven contactgegeven,
      String aant, long country) {
    this.anr = anr;
    this.bsn = bsn;
    this.persoon = persoon;
    this.datum = datum;
    this.contactgegeven = contactgegeven;
    this.aant = aant;
    this.country = country;
  }

  @Override
  public String toString() {
    return "PlContactgegeven [anr=" + anr + ", aant=" + aant + ", contactgegeven=" + contactgegeven + "]";
  }
}
