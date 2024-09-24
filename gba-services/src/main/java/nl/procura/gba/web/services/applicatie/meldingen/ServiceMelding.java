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

package nl.procura.gba.web.services.applicatie.meldingen;

import static nl.procura.commons.core.exceptions.ProExceptionSeverity.ERROR;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.standard.ProcuraDate;
import nl.procura.commons.core.exceptions.ProExceptionSeverity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(of = { "id", "melding" })
public class ServiceMelding implements Comparable<ServiceMelding> {

  private String  id        = "";
  private String  melding   = "";
  private String  oorzaak   = "";
  private boolean adminOnly = false;

  private Throwable              exception;
  private Date                   datumtijd = new Date();
  private Gebruiker              gebruiker = Gebruiker.getDefault();
  private ProExceptionSeverity   severity  = ERROR;
  private ServiceMeldingCategory category  = ServiceMeldingCategory.FAULT;

  @Override
  public int compareTo(ServiceMelding o) {
    return (getDatumtijd().getTime() > o.getDatumtijd().getTime()) ? 1 : -1;
  }

  public String getDatumtijdString() {
    ProcuraDate d = new ProcuraDate().setDateFormat(getDatumtijd());
    return d.getFormatDate() + " / " + d.getFormatTime();
  }

  public String getGebruikerString() {
    return getGebruiker().getNaam();
  }

  public String getExceptionString() {
    if (getException() != null) {
      StringWriter sw = new StringWriter();
      getException().printStackTrace(new PrintWriter(sw));
      return sw.toString();
    }
    return "";
  }
}
