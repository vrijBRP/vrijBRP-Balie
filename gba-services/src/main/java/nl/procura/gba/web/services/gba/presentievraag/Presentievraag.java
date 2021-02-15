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

import static nl.procura.standard.Globalfunctions.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.jpa.personen.db.BaseEntity;
import nl.procura.gba.jpa.personen.db.Location;
import nl.procura.gba.jpa.personen.db.PresVraag;
import nl.procura.gba.jpa.personen.db.Usr;
import nl.procura.gba.web.components.fields.values.UsrFieldValue;
import nl.procura.gba.web.services.beheer.locatie.Locatie;
import nl.procura.gba.web.services.interfaces.DatumTijdInvoer;
import nl.procura.java.reflection.ReflectionUtil;
import nl.procura.proweb.rest.utils.JsonUtils;

public class Presentievraag extends PresVraag implements DatumTijdInvoer {

  private static final Logger LOGGER = LoggerFactory.getLogger(Presentievraag.class);

  private String  zaakOmschrijving = "";
  private Locatie locatieInvoer    = null;

  public Presentievraag() {
    setUsr(new Usr(BaseEntity.DEFAULT));
  }

  @Override
  public DateTime getDatumTijdInvoer() {
    return new DateTime(getdIn(), gettIn());
  }

  @Override
  public void setDatumTijdInvoer(DateTime dt) {
    setdIn(toBigDecimal(dt.getLongDate()));
    settIn(toBigDecimal(dt.getLongTime()));
  }

  public UsrFieldValue getIngevoerdDoor() {
    return new UsrFieldValue(getUsr().getCUsr(), getUsr().getUsrfullname());
  }

  public void setIngevoerdDoor(UsrFieldValue ingevoerdDoor) {
    setUsr(new Usr(along(ingevoerdDoor.getValue()), ingevoerdDoor.getDescription()));
  }

  public Locatie getLocatieInvoer() {
    if (locatieInvoer == null && getLocation() != null) {
      locatieInvoer = ReflectionUtil.deepCopyBean(Locatie.class, getLocation());
    }

    return locatieInvoer;
  }

  public void setLocatieInvoer(Locatie locatieInvoer) {
    this.locatieInvoer = locatieInvoer;
    setLocation(ReflectionUtil.deepCopyBean(Location.class, locatieInvoer));
  }

  public PresentievraagAntwoord getPresentievraagAntwoord() {
    try {
      return JsonUtils.toObject(getAntwoord().getBytes(), PresentievraagAntwoord.class);
    } catch (Exception e) {
      LOGGER.error("Fout bij inlezen XML", e);
      return null;
    }
  }

  public PresentievraagVersie getPresentievraagVersie() {
    return PresentievraagVersie.get(super.getVersie());
  }

  public void setPresentievraagVersie(PresentievraagVersie versie) {
    super.setVersie(versie.getCode());
  }

  public String getZaakOmschrijving() {
    return zaakOmschrijving;
  }

  public void setZaakOmschrijving(String zaakOmschrijving) {
    this.zaakOmschrijving = zaakOmschrijving;
  }

  public boolean heeftZaakId() {
    return fil(getZaakId());
  }
}
