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

package nl.procura.gba.web.services.bs.algemeen.akte;

import static nl.procura.standard.Globalfunctions.along;
import static nl.procura.standard.Globalfunctions.fil;

import nl.procura.diensten.gba.ple.openoffice.formats.Naamformats;
import nl.procura.gba.jpa.personen.db.DossAkte;
import nl.procura.gba.web.components.fields.values.GbaDateFieldValue;
import nl.procura.gba.web.services.gba.functies.Geslacht;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class DossierAktePersoon {

  private final boolean  partner;
  private final DossAkte dossAkte;

  public DossierAktePersoon(DossAkte dossAkte, boolean partner) {
    this.dossAkte = dossAkte;
    this.partner = partner;
  }

  public BsnFieldValue getBurgerServiceNummer() {
    return new BsnFieldValue(along(partner ? dossAkte.getpBsn() : dossAkte.getBsn()));
  }

  public void setBurgerServiceNummer(BsnFieldValue bsn) {
    if (partner) {
      dossAkte.setpBsn(FieldValue.from(bsn).getBigDecimalValue());
    } else {
      dossAkte.setBsn(FieldValue.from(bsn).getBigDecimalValue());
    }
  }

  public GbaDateFieldValue getGeboortedatum() {
    return new GbaDateFieldValue(partner ? dossAkte.getPDGeb() : dossAkte.getDGeb());
  }

  public void setGeboortedatum(GbaDateFieldValue datum) {
    if (partner) {
      dossAkte.setPDGeb(FieldValue.from(datum).getBigDecimalValue());
    } else {
      dossAkte.setDGeb(FieldValue.from(datum).getBigDecimalValue());
    }
  }

  public Geslacht getGeslacht() {
    return Geslacht.get(partner ? dossAkte.getpGeslacht() : dossAkte.getGeslacht());
  }

  public void setGeslacht(Geslacht geslacht) {
    if (partner) {
      dossAkte.setpGeslacht(geslacht.getAfkorting());
    } else {
      dossAkte.setGeslacht(geslacht.getAfkorting());
    }
  }

  public String getGeslachtsnaam() {
    return partner ? dossAkte.getpGeslachtsnaam() : dossAkte.getGeslachtsnaam();
  }

  public void setGeslachtsnaam(String naam) {
    if (partner) {
      dossAkte.setpGeslachtsnaam(naam);
    } else {
      dossAkte.setGeslachtsnaam(naam);
    }
  }

  public Naamformats getNaam() {
    return new Naamformats(getVoornaam(), getGeslachtsnaam(), getVoorvoegsel(), "", "E", null);
  }

  public String getVoornaam() {
    return partner ? dossAkte.getpVoorn() : dossAkte.getVoorn();
  }

  public void setVoornaam(String voornaam) {
    if (partner) {
      dossAkte.setpVoorn(voornaam);
    } else {
      dossAkte.setVoorn(voornaam);
    }
  }

  public String getVoorvoegsel() {
    return partner ? dossAkte.getpVoorv() : dossAkte.getVoorv();
  }

  public void setVoorvoegsel(String voorvoegsel) {
    if (partner) {
      dossAkte.setpVoorv(voorvoegsel);
    } else {
      dossAkte.setVoorv(voorvoegsel);
    }

  }

  public boolean isCorrect() {
    return fil(getGeslachtsnaam()) && fil(getVoornaam());
  }

  public void set(DossierAktePersoon aktePersoon) {
    setBurgerServiceNummer(aktePersoon.getBurgerServiceNummer());
    setGeslacht(aktePersoon.getGeslacht());
    setGeslachtsnaam(aktePersoon.getGeslachtsnaam());
    setVoornaam(aktePersoon.getVoornaam());
    setVoorvoegsel(aktePersoon.getVoorvoegsel());
  }
}
