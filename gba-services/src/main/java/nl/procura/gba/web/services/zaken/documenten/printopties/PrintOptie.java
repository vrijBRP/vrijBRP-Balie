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

package nl.procura.gba.web.services.zaken.documenten.printopties;

import static nl.procura.gba.web.services.zaken.documenten.printopties.PrintOptieType.COMMAND;
import static nl.procura.standard.Globalfunctions.*;

import java.util.List;

import nl.procura.gba.common.MiscUtils;
import nl.procura.gba.jpa.personen.db.Document;
import nl.procura.gba.jpa.personen.db.Location;
import nl.procura.gba.jpa.personen.db.Printoptie;
import nl.procura.gba.web.services.beheer.KoppelActie;
import nl.procura.gba.web.services.beheer.locatie.KoppelbaarAanLocatie;
import nl.procura.gba.web.services.beheer.locatie.Locatie;
import nl.procura.gba.web.services.interfaces.DatabaseTable;
import nl.procura.gba.web.services.zaken.documenten.DocumentRecord;
import nl.procura.gba.web.services.zaken.documenten.KoppelbaarAanDocument;
import nl.procura.gba.web.services.zaken.documenten.KoppelbaarAanPrintOptie;
import nl.procura.gba.web.services.zaken.documenten.UitvoerformaatType;
import nl.procura.java.reflection.ReflectionUtil;

public class PrintOptie extends Printoptie implements KoppelbaarAanDocument, KoppelbaarAanLocatie, DatabaseTable {

  private static final long serialVersionUID = -512472265318873926L;

  private static final String BEGIN_COUPLIN_ERROR_MESSAGE = "Object van type ";
  private static final String END_COUPLIN_ERROR_MESSAGE   = " kan niet gekoppeld worden aan een gebruiker.";

  private boolean            standaard          = false;
  private UitvoerformaatType uitvoerformaatType = UitvoerformaatType.DEF;

  public String getBeschrijving() {
    switch (getPrintType()) {
      case COMMAND:
        return getCmd();

      default:
      case MIJN_OVERHEID:
      case POPUP:
      case LOCAL_PRINTER:
      case NETWORK_PRINTER:
        return trim(astr(getPrintoptie()) + ", " + astr(getMedia())
            + ", " + astr(getKleur()) + ", " + astr(getOrientatie()));
    }
  }

  public boolean isCommandPrinter() {
    return getPrintType() == COMMAND;
  }

  public PrintOptieType getPrintType() {
    if (isStored()) {
      // Zoek overeenkomstige types
      for (PrintOptieType type : PrintOptieType.values()) {
        if (type.is(getType())) {
          return type;
        }
        if (type.is(getPrintoptie())) {
          return type;
        }
      }

      if (fil(getCmd())) {
        return COMMAND;
      }

      return PrintOptieType.LOCAL_PRINTER;
    }

    return PrintOptieType.POPUP;
  }

  public UitvoerformaatType getUitvoerformaatType() {

    switch (getPrintType()) {
      case COMMAND:
      case LOCAL_PRINTER:
      case NETWORK_PRINTER:
      case MIJN_OVERHEID:
        return UitvoerformaatType.PDF;

      default:
        return uitvoerformaatType;
    }
  }

  public void setUitvoerformaatType(UitvoerformaatType uitvoerformaatType) {
    this.uitvoerformaatType = uitvoerformaatType;
  }

  @Override
  public boolean isGekoppeld(DocumentRecord document) {
    return MiscUtils.contains(document, getDocuments());
  }

  public <K extends KoppelbaarAanPrintOptie> boolean isGekoppeld(List<K> objectList) {

    for (K object : objectList) {
      if (!isObjectGekoppeld(object)) {
        return false;
      }
    }

    return true;
  }

  @Override
  public boolean isGekoppeld(Locatie locatie) {
    return MiscUtils.contains(locatie, getLocations());
  }

  public boolean isStandaard() {
    return standaard;
  }

  public void setStandaard(boolean standaard) {
    this.standaard = standaard;
  }

  @Override
  public void koppelActie(DocumentRecord document, KoppelActie koppelActie) {
    if (KoppelActie.KOPPEL == koppelActie) {
      getDocuments().add(ReflectionUtil.deepCopyBean(Document.class, document));
    } else {
      getDocuments().remove(document);
    }
  }

  public <K extends KoppelbaarAanPrintOptie> void koppelActie(K koppelObject, KoppelActie koppelActie) {

    if (koppelObject instanceof DocumentRecord) {
      koppelActie((DocumentRecord) koppelObject, koppelActie);
    } else if (koppelObject instanceof Locatie) {
      koppelActie((Locatie) koppelObject, koppelActie);
    } else {
      throw new IllegalArgumentException(
          BEGIN_COUPLIN_ERROR_MESSAGE + koppelObject.getClass() + END_COUPLIN_ERROR_MESSAGE);
    }
  }

  @Override
  public void koppelActie(Locatie locatie, KoppelActie koppelActie) {
    if (KoppelActie.KOPPEL == koppelActie) {
      getLocations().add(ReflectionUtil.deepCopyBean(Location.class, locatie));
    } else {
      getLocations().remove(locatie);
    }
  }

  public String toString() {
    return getOms() + (isStandaard() ? " (Standaard)" : "");
  }

  private <K extends KoppelbaarAanPrintOptie> boolean isObjectGekoppeld(K object) {

    if (object instanceof DocumentRecord) {
      return isGekoppeld((DocumentRecord) object);
    } else if (object instanceof Locatie) {
      return isGekoppeld((Locatie) object);
    }

    throw new IllegalArgumentException(BEGIN_COUPLIN_ERROR_MESSAGE + object.getClass() + END_COUPLIN_ERROR_MESSAGE);
  }
}
