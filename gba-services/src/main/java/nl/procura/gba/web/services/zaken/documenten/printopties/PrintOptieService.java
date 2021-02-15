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

import static nl.procura.gba.common.MiscUtils.copy;
import static nl.procura.gba.common.MiscUtils.copyList;
import static nl.procura.standard.Globalfunctions.pos;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;

import nl.procura.gba.jpa.personen.dao.PrintoptieDao;
import nl.procura.gba.jpa.personen.db.Printoptie;
import nl.procura.gba.web.services.AbstractService;
import nl.procura.gba.web.services.aop.ThrowException;
import nl.procura.gba.web.services.aop.Transactional;
import nl.procura.gba.web.services.beheer.KoppelActie;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.gba.web.services.beheer.locatie.Locatie;
import nl.procura.gba.web.services.zaken.documenten.DocumentRecord;
import nl.procura.gba.web.services.zaken.documenten.KoppelbaarAanPrintOptie;
import nl.procura.gba.web.services.zaken.documenten.UitvoerformaatType;
import nl.procura.standard.exceptions.ProException;
import nl.procura.standard.exceptions.ProExceptionType;

public class PrintOptieService extends AbstractService {

  public PrintOptieService() {
    super("PrintOptie");
  }

  @Transactional
  @ThrowException("Fout bij het verwijderen van printopties")
  public void delete(PrintOptie printOptie) {
    removeEntity(printOptie);
  }

  public Set<DocumentRecord> getPrintoptieDocumenten(PrintOptie printOptie) {
    return new HashSet<>(copyList(printOptie.getDocuments(), DocumentRecord.class));
  }

  public Set<Locatie> getPrintoptieLocaties(PrintOptie printOptie) {
    return new HashSet<>(copyList(printOptie.getLocations(), Locatie.class));
  }

  @ThrowException("Fout bij het tonen van printopties")
  public List<PrintOptie> getPrintOpties() {
    return copyList(PrintoptieDao.findPrintopties(), PrintOptie.class);
  }

  public List<PrintOptie> getPrintOpties(Gebruiker gebruiker, DocumentRecord document) {
    List<PrintOptie> printOpties = new ArrayList<>(getUitvoerFormaten(document));
    for (Printoptie printOptie : document.getPrintopties()) {
      PrintOptie printOptieImpl = copy(printOptie, PrintOptie.class);
      // De standaard actie die van toepassing is op dit document.
      if (isStandaard(printOptieImpl, gebruiker)) {
        printOptieImpl.setStandaard(true);
      }
      printOpties.add(printOptieImpl);
    }

    return printOpties;
  }

  @ThrowException("Fout bij het tonen van printopties")
  public List<PrintOptie> getPrintOpties(PrintOptieType type) {
    List<PrintOptie> list = new ArrayList<>();
    for (PrintOptie po : copyList(PrintoptieDao.findPrintopties(), PrintOptie.class)) {
      if (po.getPrintType() == type) {
        list.add(po);
      }
    }

    return list;
  }

  @Transactional
  @ThrowException("Fout bij het koppelen van record")
  public void koppelActie(List<? extends KoppelbaarAanPrintOptie> koppelList,
      List<PrintOptie> poList,
      KoppelActie koppelActie) {

    for (KoppelbaarAanPrintOptie koppelObject : koppelList) {
      for (PrintOptie printOptie : poList) {
        if (koppelActie.isPossible(koppelObject.isGekoppeld(printOptie))) {
          koppelObject.koppelActie(printOptie, koppelActie);
          printOptie.koppelActie(koppelObject, koppelActie);
          saveEntity(printOptie);
        }
      }

      saveEntity(koppelObject);
    }
  }

  @Transactional
  @ThrowException("Fout bij het opslaan van printoptie")
  public void save(PrintOptie printoptie) {
    saveEntity(printoptie);
  }

  /**
   * De normale uitvoerformaten: pdf, doc, etc
   */
  private List<PrintOptie> getUitvoerFormaten(DocumentRecord document) {

    try {
      List<PrintOptie> l = new ArrayList<>();
      Properties formats = new Properties();
      formats.load(new ByteArrayInputStream(document.getFormats().getBytes()));

      for (UitvoerformaatType type : UitvoerformaatType.values()) {
        if (formats.entrySet().stream()
            .filter(e -> type.getId().equals(e.getKey()))
            .anyMatch(e -> pos(e.getValue()))) {

          PrintOptie po = new PrintOptie();
          po.setOms(type.getOms());
          po.setUitvoerformaatType(type);
          po.setStandaard(false);
          l.add(po);
        }
      }

      return l;
    } catch (IOException e) {
      throw new ProException(ProExceptionType.UNKNOWN, "Fout bij ophalen uitvoerformaten", e);
    }
  }

  /**
   * Is de printOptie standaard voor de locatie van de gebruiker?
   */
  private boolean isStandaard(PrintOptie printOptie, Gebruiker gebruiker) {
    if (gebruiker != null && printOptie.getLocations() != null && gebruiker.getLocatie() != null) {
      return printOptie.getLocations().contains(gebruiker.getLocatie());
    }
    return false;
  }
}
