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

package nl.procura.gba.web.services.zaken.documenten;

import static ch.lambdaj.Lambda.*;
import static java.util.Arrays.asList;
import static nl.procura.gba.common.MiscUtils.*;
import static nl.procura.gba.common.MiscUtils.sort;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;

import nl.procura.gba.common.UniqueList;
import nl.procura.gba.jpa.personen.dao.DocumentDao;
import nl.procura.gba.jpa.personen.dao.GenericDao;
import nl.procura.gba.jpa.personen.db.Document;
import nl.procura.gba.jpa.personen.utils.GbaDaoUtils;
import nl.procura.gba.web.services.aop.ThrowException;
import nl.procura.gba.web.services.aop.Transactional;
import nl.procura.gba.web.services.beheer.KoppelActie;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.gba.web.services.beheer.parameter.ParameterConstant;
import nl.procura.gba.web.services.interfaces.GeldigheidStatus;
import nl.procura.gba.web.services.zaken.algemeen.koppelenumeratie.KoppelEnumeratie;
import nl.procura.gba.web.services.zaken.algemeen.koppelenumeratie.KoppelEnumeraties;
import nl.procura.gba.web.services.zaken.documenten.afnemers.DocumentAfnemer;
import nl.procura.gba.web.services.zaken.documenten.doelen.DocumentDoel;
import nl.procura.gba.web.services.zaken.documenten.kenmerk.DocumentKenmerk;
import nl.procura.gba.web.services.zaken.documenten.kenmerk.DocumentKenmerkType;
import nl.procura.gba.web.services.zaken.documenten.kenmerk.DocumentKenmerken;
import nl.procura.gba.web.services.zaken.documenten.printen.DocumentenPrintenService;
import nl.procura.gba.web.services.zaken.documenten.printopties.PrintOptie;
import nl.procura.gba.web.services.zaken.documenten.stempel.DocumentStempel;

public class DocumentService extends DocumentenPrintenService {

  public DocumentService() {
    setName("Documenten");
  }

  /**
   * geeft de lijst met alle documentAfnemeren
   */
  @ThrowException("Fout bij het zoeken van de documentafnemers")
  public List<DocumentAfnemer> getAfnemers() {
    return sort(copyList(DocumentDao.findAfnemers(), DocumentAfnemer.class));
  }

  /**
   * Documenten die NIET specifiek aan een gebruiker zijn gekoppeld
   */
  @ThrowException("Fout bij ophalen van de documenten")
  public List<DocumentRecord> getAlgemeneDocumenten() {
    return copyList(DocumentDao.findGeneralDocuments(), DocumentRecord.class);
  }

  @ThrowException("Fout bij het ophalen van de documentsoorten.")
  public List<DocumentSoort> getAlleDocumentSoorten(boolean isAttributen) {
    return getDocumentSoorten(GeldigheidStatus.ACTUEEL, isAttributen);
  }

  public DocumentRecord getDocument(long documentCode) {
    return copy(GenericDao.find(Document.class, documentCode), DocumentRecord.class);
  }

  public List<DocumentRecord> getDocumentenByDmsNaam(String dmsNaam) {
    return getDocumenten(GeldigheidStatus.ACTUEEL, false)
        .stream()
        .filter(r -> r.getDmsNaam().equalsIgnoreCase(dmsNaam))
        .collect(Collectors.toList());

  }

  public List<DocumentRecord> getDocumenten(boolean isAttributen) {
    return getDocumenten(GeldigheidStatus.ACTUEEL, isAttributen);
  }

  /**
   * Deze functie geeft alle documenten van het meegegeven type terug.
   *
   * @return lijstje van documenten
   */
  @ThrowException("Fout bij het zoeken van de documentsoorten.")
  public List<DocumentRecord> getDocumenten(DocumentType... documentTypes) {

    List<DocumentRecord> documenten = new ArrayList<>();
    List<DocumentSoort> documentSoorten = getDocumentSoorten(GeldigheidStatus.ACTUEEL, false);

    for (DocumentSoort docSoort : documentSoorten) {
      for (DocumentType documentType : documentTypes) {
        if (docSoort.getType().equals(documentType)) {
          documenten.addAll(docSoort.getDocumenten());
        }
      }
    }

    return documenten;
  }

  /**
   * Alle documenten op basis van gebruiker en type
   */
  @ThrowException("Fout bij ophalen van de documenten bij de gebruiker")
  public List<DocumentRecord> getDocumenten(Gebruiker gebruiker, DocumentType... types) {

    List<DocumentRecord> documenten = new UniqueList<>();

    documenten.addAll(getGekoppeldeDocumenten(gebruiker));
    documenten.addAll(getAlgemeneDocumenten());

    for (DocumentType type : types) {
      for (DocumentRecord document : documenten) {
        if (equals(document, type)) {
          if (new File(getSjablonenMap(), document.getBestand()).exists()) {
            laadAttributen(gebruiker, document);
            documenten.add(document);
          }
        }
      }
    }

    return documenten;
  }

  public List<DocumentRecord> getDocumenten(GeldigheidStatus recordStatus, boolean isAttributen) {

    List<DocumentRecord> documenten = new ArrayList<>();
    List<DocumentSoort> documentSoorten = getAlleDocumentSoorten(recordStatus, isAttributen);

    for (DocumentSoort docs : documentSoorten) {
      documenten.addAll(docs.getDocumenten());
    }
    return documenten;
  }

  @ThrowException("Fout bij ophalen van de documenten bij de gebruiker")
  public List<DocumentRecord> getDocumentenByKenmerk(List<DocumentRecord> documenten, DocumentKenmerkType... types) {
    List<DocumentRecord> list = new ArrayList<>();
    for (DocumentRecord document : documenten) {
      if (document.getDocumentKenmerken().is(types)) {
        list.add(document);
      }
    }

    return list;
  }

  @ThrowException("Fout bij ophalen van de documenten bij de gebruiker")
  public List<DocumentSoort> getDocumentenBySoort(List<DocumentRecord> documenten) {
    List<DocumentSoort> list = new UniqueList<>();
    for (DocumentRecord document : documenten) {
      list = addDocumentToSoort(document, list);
    }

    return list;
  }

  /**
   * Geeft verzameling van alle gebruikers bij een document.
   */
  public Set<Gebruiker> getDocumentGebruikers(DocumentRecord document) {
    return new HashSet<>(copyList(document.getUsrs(), Gebruiker.class));
  }

  /**
   * Geeft verzameling van alle kenmerken bij een document.
   */
  public Set<DocumentKenmerk> getDocumentKenmerken(DocumentRecord document) {
    return new HashSet<>(copyList(document.getKenmerken(), DocumentKenmerk.class));
  }

  /**
   * Geeft verzameling van gekoppelde printopties aan een document
   */
  public Set<PrintOptie> getDocumentPrintopties(DocumentRecord document) {
    return new HashSet<>(
        copyList(document.getPrintopties(), PrintOptie.class));
  }

  /**
   * Alle documenten op basis van gebruiker en type
   */
  @ThrowException("Fout bij ophalen van de documenten bij de gebruiker")
  public List<DocumentSoort> getDocumentSoorten(Gebruiker gebruiker, DocumentType... types) {

    List<DocumentSoort> list = new UniqueList<>();
    List<DocumentRecord> documenten = new UniqueList<>();

    documenten.addAll(getGekoppeldeDocumenten(gebruiker));
    documenten.addAll(getAlgemeneDocumenten());

    for (DocumentType type : types) {
      for (DocumentRecord document : documenten) {
        if (equals(document, type)) {
          if (new File(getSjablonenMap(), document.getBestand()).exists()) {
            laadAttributen(gebruiker, document);
            list = addDocumentToSoort(document, list);
          }
        }
      }
    }

    return list;
  }

  /**
   * Alle documenten op basis van gebruiker en type
   */
  @ThrowException("Fout bij ophalen van de documenten bij de gebruiker")
  public List<DocumentSoort> getDocumentSoorten(Gebruiker gebruiker, List<DocumentRecord> documenten) {
    List<DocumentSoort> list = new UniqueList<>();
    for (DocumentRecord document : documenten) {
      laadAttributen(gebruiker, document);
      list = addDocumentToSoort(document, list);
    }

    return list;
  }

  /**
   * Geeft verzameling van alle stempels bij een document.
   */
  public Set<DocumentStempel> getDocumentStempels(DocumentRecord document) {
    return new HashSet<>(copyList(document.getStempels(), DocumentStempel.class));
  }

  /**
   * Zoek een document op naam op. Retourneert het document zonder attributen.
   */
  public DocumentRecord getDocumentZonderAttributen(String document) {
    return selectFirst(getDocumenten(false),
        having(on(DocumentRecord.class).getDocument(), Matchers.equalTo(document)));
  }

  /**
   * geeft de lijst met alle documentDoelen
   */
  @ThrowException("Fout bij het zoeken van de doelen")
  public List<DocumentDoel> getDoelen() {
    return sort(copyList(DocumentDao.findDoelen(), DocumentDoel.class));
  }

  /**
   * Documenten die specifiek aan de gebruiker zijn gekoppeld
   */
  @ThrowException("Fout bij ophalen van de documenten")
  public List<DocumentRecord> getGekoppeldeDocumenten(Gebruiker gebruiker) {
    return copyList(DocumentDao.findUsrDocuments(gebruiker.getCUsr()), DocumentRecord.class);
  }

  /**
   * Geeft verzameling van alle koppelenumeraties bij een document.
   */
  @ThrowException("Fout bij het zoeken van de kernmerken")
  public KoppelEnumeraties getKoppelenumen(DocumentRecord document) {
    return new KoppelEnumeraties(copyList(to(document, DocumentRecord.class)
        .getKoppelenumen(), KoppelEnumeratie.class));
  }

  @Transactional
  @ThrowException("Fout bij het koppelen van record")
  public void koppelActie(List<? extends KoppelbaarAanDocument> koppelList,
      List<DocumentRecord> documenten,
      KoppelActie koppelActie) {

    for (DocumentRecord document : documenten) {
      for (KoppelbaarAanDocument koppelObject : koppelList) {
        KoppelbaarAanDocument dbKoppelObject = checkDatabaseRepresentation(koppelObject);
        if (koppelActie.isPossible(document.isGekoppeld(asList(dbKoppelObject)))) {
          dbKoppelObject.koppelActie(document, koppelActie);
          koppelObject.koppelActie(document, koppelActie);
          document.koppelActie(dbKoppelObject, koppelActie);
          saveEntity(dbKoppelObject);
        }
      }

      saveEntity(document);
    }
  }

  @ThrowException("Fout bij het opslaan van afnemer")
  public void save(DocumentAfnemer documentAfnemer) {
    saveEntity(documentAfnemer);
  }

  @ThrowException("Fout bij het opslaan van een doel")
  public void save(DocumentDoel documentDoel) {
    saveEntity(documentDoel);
  }

  @ThrowException("Fout bij het opslaan van een kenmerk")
  public void save(DocumentKenmerk documentKenmerk) {
    saveEntity(documentKenmerk);
  }

  public void save(DocumentRecord document) {
    save(asList(document));
  }

  @ThrowException("Fout bij het opslaan van een stempel")
  public void save(DocumentStempel documentStempel) {
    saveEntity(documentStempel);
  }

  @Transactional
  @ThrowException("Fout bij het opslaan van document")
  public void save(List<DocumentRecord> docList) {
    docList.forEach(o -> saveEntity(o));
  }

  @ThrowException("Fout bij het verwijderen van afnemer")
  public void delete(DocumentAfnemer documentAfnemer) {
    removeEntity(documentAfnemer);
  }

  @ThrowException("Fout bij het verwijderen van een doel")
  public void delete(DocumentDoel documentDoel) {
    removeEntity(documentDoel);
  }

  @ThrowException("Fout bij het verwijderen van een kenmerk")
  public void delete(DocumentKenmerk documentKenmerk) {
    removeEntity(documentKenmerk);
  }

  @ThrowException("Fout bij het verwijderen van documenten")
  public void delete(DocumentRecord document) {
    removeEntity(document);
  }

  @ThrowException("Fout bij het verwijderen van een stempel")
  public void delete(DocumentStempel documentStempel) {
    removeEntity(documentStempel);
  }

  /**
   * @return the confidentiality that is in the parameters, otherwise the parm value or default value
   */
  public DocumentVertrouwelijkheid getStandaardVertrouwelijkheid(
      DocumentVertrouwelijkheid vertrouw,
      DocumentVertrouwelijkheid defaultVertrouw) {
    if (vertrouw != null && vertrouw != DocumentVertrouwelijkheid.ONBEKEND) {
      return vertrouw;
    } else {
      String vertrouwId = getParm(ParameterConstant.DOC_CONFIDENTIALITY);
      return StringUtils.isNotBlank(vertrouwId) ? DocumentVertrouwelijkheid.get(vertrouwId) : defaultVertrouw;
    }
  }

  private List<DocumentSoort> addDocumentToSoort(DocumentRecord document, List<DocumentSoort> documentSoorten) {
    DocumentSoort documentSoort = document.getDocumentSoort();
    if (documentSoorten.contains(documentSoort)) {
      documentSoorten.get(documentSoorten.indexOf(documentSoort)).getDocumenten().add(document);
    } else {
      documentSoort.getDocumenten().add(document);
      documentSoorten.add(documentSoort);
    }

    return documentSoorten;
  }

  /**
   * Deze functie controleert of koppelObject een key naar een bijbehorend jpa-object heeft.
   * Is koppelObject nog niet opgeslagen in de database dan wordt dit uitgevoerd.
   */
  private KoppelbaarAanDocument checkDatabaseRepresentation(KoppelbaarAanDocument koppelObject) {
    Optional<Object> entity = findEntity(GbaDaoUtils.getEntity(koppelObject)).stream().findFirst();
    if (entity.isPresent()) {
      return copy(entity.get(), koppelObject.getClass());
    }

    saveEntity(koppelObject);
    return koppelObject;
  }

  /**
   * true als type van document overeenkomt met documentType parameter
   */
  private boolean equals(DocumentRecord document, DocumentType type) {
    if (document != null && type != null) {
      return type.getType().equals(document.getType());
    }
    return false;
  }

  /**
   * Geeft verzameling van alle afnemeren bij een document.
   */
  @ThrowException("Fout bij het zoeken van de documentafnemers")
  private List<DocumentAfnemer> getAfnemers(DocumentRecord document) {
    boolean isProtocollering = document.isProtocolleren();
    return isProtocollering ? getAfnemers() : new ArrayList<>();
  }

  @ThrowException("Fout bij het ophalen van de documentsoorten.")
  private List<DocumentSoort> getAlleDocumentSoorten(GeldigheidStatus status, boolean isAttributen) {
    return getDocumentSoorten(status, isAttributen);
  }

  /**
   * Alle documenten los van gebruiker of type
   *
   * @return lijst van documentsoorten
   */
  private List<DocumentSoort> getDocumentSoorten(GeldigheidStatus status, boolean isAttributen) {
    List<DocumentSoort> list = new ArrayList<>();
    for (DocumentRecord document : copyList(DocumentDao.findAllDocuments(), DocumentRecord.class)) {
      if (document.getGeldigheidStatus().is(status)) {
        if (isAttributen) {
          laadAttributen(null, document);
        }

        list = addDocumentToSoort(document, list);
      }
    }

    sort(list);
    return list;
  }

  /**
   * Geeft verzameling van alle doelen bij een document.
   */
  @ThrowException("Fout bij het zoeken van de documentdoelen")
  private List<DocumentDoel> getDoelen(DocumentRecord document) {
    boolean isProtocollering = document.isProtocolleren();
    return isProtocollering ? getDoelen() : new ArrayList<>();
  }

  private void laadAttributen(Gebruiker gebruiker, DocumentRecord document) {
    document.setPrintOpties(getServices().getPrintOptieService().getPrintOpties(gebruiker, document));
    document.setAfnemers(getAfnemers(document));
    document.setDoelen(getDoelen(document));
    document.setDocumentStempels(new ArrayList(getDocumentStempels(document)));
    document.setKoppelElementen(getKoppelenumen(document));
    document.setDocumentKenmerken(new DocumentKenmerken(new ArrayList<>(getDocumentKenmerken(document))));
  }
}
