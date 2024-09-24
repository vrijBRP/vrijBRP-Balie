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

package nl.procura.gba.web.services.zaken.voorraad;

import static nl.procura.gba.web.services.applicatie.meldingen.ServiceMeldingCategory.SYSTEM;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.MIN_VOORRAAD;
import static nl.procura.standard.Globalfunctions.*;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.ERROR;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.WARNING;
import static nl.procura.commons.core.exceptions.ProExceptionType.DATABASE;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.procura.gba.jpa.personen.db.Voorraad;
import nl.procura.gba.jpa.personen.utils.GbaJpa;
import nl.procura.gba.web.services.AbstractService;
import nl.procura.commons.core.exceptions.ProException;

public class VoorraadService extends AbstractService {

  private final static Logger LOGGER = LoggerFactory.getLogger(VoorraadService.class.getName());

  public VoorraadService() {
    super("Voorraad");
  }

  @Override
  public void check() {
    //    checkAantalNummers(VoorraadType.BSN);
    //    checkAantalNummers(VoorraadType.ANR);
  }

  public long getAantallen(VoorraadType type, VoorraadStatus status) {

    TypedQuery<Voorraad> query = GbaJpa.getManager().createNamedQuery("Voorraad.getCount", Voorraad.class);
    query.setParameter("type", type.getCode());
    query.setParameter("status", status.getCode());

    return aval(query.getResultList().get(0));
  }

  public long getNummer(VoorraadType type, VoorraadStatus status) {

    TypedQuery<Voorraad> query = GbaJpa.getManager().createNamedQuery("Voorraad.get", Voorraad.class);
    query.setParameter("type", type.getCode());
    query.setParameter("status", VoorraadStatus.BESCHIKBAAR.getCode());
    query.setMaxResults(1);

    List<Voorraad> list = query.getResultList();

    if (list.size() > 0) {

      Voorraad voorraad = query.getResultList().get(0);
      voorraad.setStatus(BigDecimal.valueOf(status.getCode()));
      saveEntity(voorraad);

      LOGGER.debug("Ophalen nummer voorraad: " + voorraad.getNummer() + " van status: " + status);

      return voorraad.getNummer();
    }

    throw new ProException(DATABASE, ERROR, "Geen voorraad van het type '" + type.getOms() + "' meer.");
  }

  public void setNummer(long nummer, VoorraadStatus status, String melding) {

    TypedQuery<Voorraad> query = GbaJpa.getManager().createNamedQuery("Voorraad.getByNumber", Voorraad.class);
    query.setParameter("nummer", nummer);
    query.setMaxResults(1);

    if (query.getResultList().size() > 0) {

      Voorraad voorraad = query.getResultList().get(0);
      voorraad.setStatus(BigDecimal.valueOf(status.getCode()));

      if (fil(melding)) {
        voorraad.setMelding(melding);
      }

      saveEntity(voorraad);
      LOGGER.debug("Update nummer voorraad: " + voorraad.getNummer() + " van status: " + status);
    }
  }

  private void checkAantalNummers(VoorraadType type) {

    try {

      int minAantal = aval(getParm(MIN_VOORRAAD));

      if (pos(minAantal)) {

        long aantalBeschikbaar = getAantallen(type, VoorraadStatus.BESCHIKBAAR);

        if (aantalBeschikbaar < minAantal) {

          String nummers = type == VoorraadType.ANR ? "a-nummers" : "burgerservicenummers";

          if (aantalBeschikbaar > 0) {
            addMessage(true, SYSTEM, WARNING,
                String.format("De voorraad %s is nog maar " + aantalBeschikbaar, nummers, type));
          } else {
            addMessage(true, SYSTEM, WARNING, String.format("De voorraad %s is op", nummers));
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
