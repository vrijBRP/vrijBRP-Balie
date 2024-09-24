/*
 * Copyright 2024 - 2025 Procura B.V.
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

package nl.procura.gba.web.services.zaken.documenten;

import static nl.procura.java.reflection.ReflectionUtil.deepCopyBean;
import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.pos;
import static nl.procura.standard.Globalfunctions.toBigDecimal;

import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.common.MiscUtils;
import nl.procura.gba.jpa.personen.db.BaseEntity;
import nl.procura.gba.jpa.personen.db.Document;
import nl.procura.gba.jpa.personen.db.Kenmerk;
import nl.procura.gba.jpa.personen.db.Koppelenum;
import nl.procura.gba.jpa.personen.db.Stempel;
import nl.procura.gba.web.services.beheer.KoppelActie;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.gba.web.services.beheer.gebruiker.KoppelbaarAanGebruiker;
import nl.procura.gba.web.services.interfaces.DatabaseTable;
import nl.procura.gba.web.services.interfaces.Geldigheid;
import nl.procura.gba.web.services.interfaces.GeldigheidStatus;
import nl.procura.gba.web.services.zaken.algemeen.koppelenumeratie.KoppelEnumeratie;
import nl.procura.gba.web.services.zaken.algemeen.koppelenumeratie.KoppelEnumeraties;
import nl.procura.gba.web.services.zaken.algemeen.koppelenumeratie.KoppelbaarAanKoppelEnumeratie;
import nl.procura.gba.web.services.zaken.documenten.afnemers.DocumentAfnemer;
import nl.procura.gba.web.services.zaken.documenten.doelen.DocumentDoel;
import nl.procura.gba.web.services.zaken.documenten.kenmerk.DocumentKenmerk;
import nl.procura.gba.web.services.zaken.documenten.kenmerk.DocumentKenmerken;
import nl.procura.gba.web.services.zaken.documenten.printopties.PrintOptie;
import nl.procura.gba.web.services.zaken.documenten.stempel.DocumentStempel;

public class DocumentRecord extends Document
    implements Comparable<DocumentRecord>, KoppelbaarAanGebruiker, KoppelbaarAanDocumentStempel,
    KoppelbaarAanDocumentKenmerk, KoppelbaarAanPrintOptie, KoppelbaarAanKoppelEnumeratie, Geldigheid, DatabaseTable {

  private static final long serialVersionUID = 8480080444350498646L;

  private static final String BEGIN_COUPLIN_ERROR_MESSAGE = "Object van type ";
  private static final String END_COUPLIN_ERROR_MESSAGE   = " kan niet gekoppeld worden aan een document.";

  private List<PrintOptie>      printOpties = new ArrayList<>();
  private List<DocumentAfnemer> afnemers    = new ArrayList<>();
  private List<DocumentDoel>    doelen      = new ArrayList<>();
  private List<DocumentStempel> stempels    = new ArrayList<>();

  private KoppelEnumeraties koppelenums = new KoppelEnumeraties();
  private DocumentKenmerken kenmerken   = new DocumentKenmerken();

  public DocumentRecord() {
  }

  public static DocumentRecord getDefault() {
    DocumentRecord g = new DocumentRecord();
    g.setCDocument(BaseEntity.DEFAULT);
    return g;
  }

  @Override
  public int compareTo(DocumentRecord o) {
    return getVDocument().compareTo(o.getVDocument());
  }

  public List<DocumentAfnemer> getAfnemers() {
    return afnemers;
  }

  public void setAfnemers(List<DocumentAfnemer> afnemers) {
    this.afnemers = afnemers;
  }

  @Override
  public DateTime getDatumEinde() {
    return new DateTime(getDEnd());
  }

  @Override
  public void setDatumEinde(DateTime time) {
    setDEnd(toBigDecimal(time.getLongDate()));
  }

  @Override
  public DateTime getDatumIngang() {
    return new DateTime(19900101); // Geen ingangsdatum geimplementeerd
  }

  @Override
  public void setDatumIngang(DateTime time) {
    // Geen ingangsdatum geimplementeerd
  }

  public DateTime getDatumVerval() {
    return new DateTime(getDEnd());
  }

  public void setDatumVerval(DateTime vervalDatum) {
    setDEnd(toBigDecimal(vervalDatum.getLongDate()));
  }

  public DocumentVertrouwelijkheid getVertrouwelijkheid() {
    return DocumentVertrouwelijkheid.get(astr(getVertr()));
  }

  public void setVertrouwelijkheid(DocumentVertrouwelijkheid vertrouwelijkheid) {
    setVertr(vertrouwelijkheid != null ? vertrouwelijkheid.getCode() : -1);
  }

  public DocumentKenmerken getDocumentKenmerken() {
    return kenmerken;
  }

  public void setDocumentKenmerken(DocumentKenmerken kenmerken) {
    this.kenmerken = kenmerken;
  }

  public DocumentSoort getDocumentSoort() {
    return new DocumentSoort(DocumentType.getType(getType()));
  }

  public List<DocumentStempel> getDocumentStempels() {
    return stempels;
  }

  public void setDocumentStempels(List<DocumentStempel> stempels) {
    this.stempels = stempels;
  }

  public DocumentType getDocumentType() {
    return DocumentType.getType(getType());
  }

  public List<DocumentDoel> getDoelen() {
    return doelen;
  }

  public void setDoelen(List<DocumentDoel> doelen) {
    this.doelen = doelen;
  }

  public String getEmailDocument(BestandType bestandtype) {
    return (getDocument().replaceAll("\\s+", "_") + "." + bestandtype.getType());
  }

  @Override
  public GeldigheidStatus getGeldigheidStatus() {
    return GeldigheidStatus.get(this);
  }

  public KoppelEnumeraties getKoppelElementen() {
    return koppelenums;
  }

  public void setKoppelElementen(KoppelEnumeraties koppelenums) {
    this.koppelenums = koppelenums;
  }

  public String getPad() {
    return getPath();
  }

  public void setPad(String pad) {
    setPath(pad);
  }

  public List<PrintOptie> getPrintOpties() {
    return printOpties;
  }

  public void setPrintOpties(List<PrintOptie> printOpties) {
    this.printOpties = printOpties;
  }

  public PrintOptie getStandaardPrintOptie() {

    for (Object i : getPrintOpties()) {
      if (i.toString().toLowerCase().contains("standaard")) {
        return (PrintOptie) i;
      }
    }
    return (getPrintOpties().size() > 0) ? getPrintOpties().iterator().next() : null;
  }

  public boolean isDocument(String... teksten) {
    for (String tekst : teksten) {
      if (getDocument().toLowerCase().contains(tekst.toLowerCase())) {
        return true;
      }
    }
    return false;
  }

  public boolean hasTranslations() {
    return getTranslation() != null && !getTranslation().getTranslations().isEmpty();
  }

  @Override
  public boolean isGekoppeld(DocumentKenmerk kenmerk) {
    return kenmerk.isGekoppeld(this);
  }

  @Override
  public boolean isGekoppeld(DocumentStempel stempel) {
    return stempel.isGekoppeld(this);
  }

  @Override
  public boolean isGekoppeld(Gebruiker gebruiker) {
    return gebruiker.isGekoppeld(this);
  }

  public <K extends KoppelbaarAanDocument> boolean isGekoppeld(K object) {

    if (object instanceof Gebruiker) {
      Gebruiker gebr = (Gebruiker) object;
      return isGekoppeld(gebr);
    }

    if (object instanceof PrintOptie) {
      PrintOptie po = (PrintOptie) object;
      return isGekoppeld(po);
    }

    if (object instanceof KoppelEnumeratie) {
      KoppelEnumeratie po = (KoppelEnumeratie) object;
      return isGekoppeld(po);
    }

    throw new IllegalArgumentException(BEGIN_COUPLIN_ERROR_MESSAGE + object.getClass() + END_COUPLIN_ERROR_MESSAGE);
  }

  @Override
  public boolean isGekoppeld(KoppelEnumeratie koppelenum) {
    return MiscUtils.contains(koppelenum, getKoppelenumen());
  }

  public <K extends KoppelbaarAanDocument> boolean isGekoppeld(List<K> objectList) {
    for (K object : objectList) {
      if (!isGekoppeld(object)) {
        return false;
      }
    }

    return true;
  }

  @Override
  public boolean isGekoppeld(PrintOptie printOptie) {
    return MiscUtils.contains(printOptie, getPrintOpties());
  }

  public boolean isGekoppeldAanGebruikers() {
    return getUsrs().size() > 0;
  }

  public boolean isIedereenToegang() {
    return pos(getAllAllowed());
  }

  public void setIedereenToegang(boolean iedereenToegang) {
    setAllAllowed(toBigDecimal(iedereenToegang ? 1 : 0));
  }

  public boolean isKopieOpslaan() {
    return pos(getSave());
  }

  public void setKopieOpslaan(boolean opslaan) {
    setSave(toBigDecimal(opslaan ? 1 : 0));
  }

  @Override
  public boolean isPrintOptiesGekoppeld(List<PrintOptie> printopties) {
    for (PrintOptie po : printopties) {
      if (!isGekoppeld(po)) {
        return false;
      }
    }
    return true;
  }

  public boolean isProtocolleren() {
    return pos(getProt());
  }

  public void setProtocolleren(boolean protocolleren) {
    setProt(toBigDecimal(protocolleren ? 1 : 0));
  }

  public boolean isStillbornAllowed() {
    return pos(getSbAllowed());
  }

  public void setStillbornAllowed(boolean stillbornAllowed) {
    setSbAllowed(toBigDecimal(stillbornAllowed ? 1 : 0));
  }

  public boolean isStandaardDocument() {
    return pos(getStandaard());
  }

  public void setStandaardDocument(boolean standaard) {
    setStandaard(toBigDecimal(standaard ? 1 : 0));
  }

  public <K extends KoppelbaarAanDocument> void koppelActie(K koppelObject, KoppelActie koppelActie) {
    if (koppelObject instanceof DocumentStempel) {
      koppelActie((DocumentStempel) koppelObject, koppelActie);
    } else if (koppelObject instanceof DocumentKenmerk) {
      koppelActie((DocumentKenmerk) koppelObject, koppelActie);
    } else if (koppelObject instanceof Gebruiker) {
      koppelActie((Gebruiker) koppelObject, koppelActie);
    } else if (koppelObject instanceof PrintOptie) {
      koppelActie((PrintOptie) koppelObject, koppelActie);
    } else if (koppelObject instanceof KoppelEnumeratie) {
      koppelActie((KoppelEnumeratie) koppelObject, koppelActie);
    } else {
      throw new IllegalArgumentException(
          "Object van type " + koppelObject.getClass() + " kan niet gekoppeld worden aan een document.");
    }
  }

  @Override
  public void koppelActie(DocumentKenmerk documentKenmerk, KoppelActie koppelActie) {
    if (KoppelActie.KOPPEL == koppelActie) {
      getKenmerken().add(deepCopyBean(Kenmerk.class, documentKenmerk));
    } else {
      getKenmerken().remove(documentKenmerk);
    }
  }

  @Override
  public void koppelActie(DocumentStempel documentStempel, KoppelActie koppelActie) {
    if (KoppelActie.KOPPEL == koppelActie) {
      getStempels().add(deepCopyBean(Stempel.class, documentStempel));
    } else {
      getStempels().remove(documentStempel);
    }
  }

  @Override
  public void koppelActie(Gebruiker gebruiker, KoppelActie koppelActie) {
    if (KoppelActie.KOPPEL == koppelActie) {
      getUsrs().add(deepCopyBean(nl.procura.gba.jpa.personen.db.Usr.class, gebruiker));
    } else {
      getUsrs().remove(gebruiker);
    }
  }

  @Override
  public void koppelActie(PrintOptie printOptie, KoppelActie koppelActie) {
    if (KoppelActie.KOPPEL == koppelActie) {
      getPrintopties().add(deepCopyBean(nl.procura.gba.jpa.personen.db.Printoptie.class, printOptie));
    } else {
      getPrintopties().remove(printOptie);
    }
  }

  @Override
  public void koppelActie(KoppelEnumeratie koppelenum, KoppelActie koppelActie) {
    if (KoppelActie.KOPPEL == koppelActie) {
      getKoppelenumen().add(deepCopyBean(Koppelenum.class, koppelenum));
    } else {
      getKoppelenumen().remove(koppelenum);
    }
  }

  public String toString() {
    String aantalAfdrukken = (getAantal() > 1 ? " (x" + getAantal() + ")" : "");
    return getDocument() + aantalAfdrukken;
  }
}
