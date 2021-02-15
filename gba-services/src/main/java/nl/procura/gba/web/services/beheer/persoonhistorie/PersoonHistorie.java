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

package nl.procura.gba.web.services.beheer.persoonhistorie;

import static nl.procura.standard.Globalfunctions.toBigDecimal;

import nl.procura.diensten.gba.ple.procura.arguments.PLEDatasource;
import nl.procura.gba.jpa.personen.db.PlHist;
import nl.procura.gba.jpa.personen.db.PlHistPK;

public class PersoonHistorie extends PlHist {

  private static final long serialVersionUID = 3741935973988662802L;

  public PLEDatasource getDatabron() {
    return PLEDatasource.get(getBron().intValue());
  }

  public void setDatabron(PLEDatasource databron) {
    setBron(toBigDecimal(databron.getCode()));
  }

  @Override
  public PlHistPK getId() {
    if (super.getId() == null) {
      super.setId(new PlHistPK());
    }

    return super.getId();
  }

  public long getNummer() {
    return getId().getNr();
  }

  public void setNummer(long nummer) {
    getId().setNr(nummer);
  }

  public String getOmschrijving() {
    return getOms();
  }

  public void setOmschrijving(String omschrijving) {
    setOms(omschrijving);
  }
}
