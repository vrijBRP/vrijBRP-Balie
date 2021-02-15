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

package nl.procura.gba.web.services.zaken.algemeen;

import java.io.Serializable;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.openoffice.DocumentPL;
import nl.procura.gba.jpa.personen.db.Location;
import nl.procura.gba.web.services.beheer.locatie.Locatie;
import nl.procura.gba.web.services.bs.algemeen.ZaakDossier;
import nl.procura.gba.web.services.gba.basistabellen.gemeente.Gemeente;
import nl.procura.gba.web.services.zaken.algemeen.commentaar.CommentaarZaak;
import nl.procura.gba.web.services.zaken.algemeen.commentaar.ZaakCommentaren;
import nl.procura.gba.web.services.zaken.algemeen.contact.ZaakContact;
import nl.procura.gba.web.services.zaken.identiteit.Identificatie;
import nl.procura.java.reflection.ReflectionUtil;

public class GenericZaak implements Serializable {

  private static final long serialVersionUID = -6854574296833486812L;

  private final ZaakHistorie zaakHistorie  = new ZaakHistorie();
  private ZaakContact        contact       = new ZaakContact();
  private ZaakDossier        zaakDossier   = null;
  private BasePLExt          basisPersoon  = null;
  private Identificatie      identificatie = null;
  private Locatie            locatieInvoer = null;
  private Locatie            locatieAfhaal = null;
  private DocumentPL         persoon       = null;
  private Gemeente           gemeente      = null;
  private ZaakCommentaren    commentaren   = null;

  public BasePLExt getBasisPersoon() {
    return basisPersoon;
  }

  public void setBasisPersoon(BasePLExt basisPersoon) {
    this.basisPersoon = basisPersoon;
  }

  public ZaakCommentaren getCommentaren(CommentaarZaak zaak) {

    if (commentaren == null) {
      commentaren = new ZaakCommentaren(zaak);
    }

    return commentaren;
  }

  public ZaakContact getContact() {
    return contact;
  }

  public void setContact(ZaakContact contact) {
    this.contact = contact;
  }

  public Gemeente getGemeente() {
    return gemeente;
  }

  public void setGemeente(Gemeente gemeente) {
    this.gemeente = gemeente;
  }

  public Identificatie getIdentificatie() {
    return identificatie;
  }

  public void setIdentificatie(Identificatie identificatie) {
    this.identificatie = identificatie;
  }

  public Locatie getLocatieAfhaal(Location location) {

    if (locatieAfhaal == null && location != null) {
      locatieAfhaal = ReflectionUtil.deepCopyBean(Locatie.class, location);
    }

    return locatieAfhaal;
  }

  public Locatie getLocatieInvoer(Location location) {

    if (locatieInvoer == null && location != null) {
      locatieInvoer = ReflectionUtil.deepCopyBean(Locatie.class, location);
    }

    return locatieInvoer;
  }

  public DocumentPL getPersoon() {

    if (persoon == null && getBasisPersoon() != null) {
      persoon = new DocumentPL(getBasisPersoon());
    }

    return persoon;
  }

  public void setPersoon(DocumentPL persoon) {
    this.persoon = persoon;
  }

  public ZaakDossier getZaakDossier() {
    return zaakDossier;
  }

  public void setZaakDossier(ZaakDossier zaakDossier) {
    this.zaakDossier = zaakDossier;
  }

  public ZaakHistorie getZaakHistorie() {
    return zaakHistorie;
  }

  public void setLocatieAfhaal(Locatie locatieAfhaal) {
    this.locatieAfhaal = locatieAfhaal;
  }

  public void setLocatieInvoer(Locatie locatieInvoer) {
    this.locatieInvoer = locatieInvoer;
  }
}
