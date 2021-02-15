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

package nl.procura.gba.web.services.zaken.algemeen.dms;

import java.io.File;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.web.services.AbstractService;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.documenten.printen.PrintActie;
import nl.procura.standard.exceptions.NotSupportedException;

public class ExterneDmsService extends AbstractService implements DmsOptiesService {

  // private static int BEGIN = 19000101;

  public ExterneDmsService() {
    super("Externe DMS");
  }

  @Override
  public DmsStream getBestand(DmsDocument record) {

    /*
    DmsStream stream = new DmsStream ();
    
    Session   session = null;
    
    try {
    
        session = getSessie ();
        for (Node document : NodeUtils.getNodes (session, ArchiefDocumentFuncties.getDocumentMetNaam (record.getPad ()))) {
            for (Node file : NodeUtils.getNodesByType (document, NodeType.NT_FILE)) {
                for (Node content : NodeUtils.getNodesByType (file, NodeType.NT_RESOURCE)) {
                    stream.setInputStream (PropertyUtils.getDataStream (content));
                    stream.setUitvoernaam ("document." + record.getExtensie ());
                    }
                }
            }
        }
    finally {
    
        ArchiefClient.close (session);
        }
    */

    throw new NotSupportedException();
  }

  @Override
  public int getAantalDocumenten(BasePLExt pl) {
    throw new NotSupportedException();
  }

  @Override
  public int getAantalDocumenten(Zaak zaak) {
    throw new NotSupportedException();
  }

  @Override
  public DmsResultaat getDocumenten(BasePLExt pl) {

    /*
    if (pl == null) {
        return new DmsResultaat ();
        }
    
    Session session = null;
    
    try {
    
        session = getSessie ();
        Keyword bsn = new Keyword (ProcuraProperty.TYPES.BSN, pl.getPersoon ().getBsn ().getCode ());
        Keyword anr = new Keyword (ProcuraProperty.TYPES.ANR, pl.getPersoon ().getAnr ().getCode ());
        return toDmsResult (NodeUtils.getNodes (session, ArchiefDocumentFuncties.getDocumentsMetKeyword (BEGIN, bsn, anr)));
        }
    finally {
    
        ArchiefClient.close (session);
        }
            */

    throw new NotSupportedException();
  }

  @Override
  public DmsResultaat getDocumenten(Zaak zaak) {

    /*
    if (zaak == null) {
        return new DmsResultaat ();
        }
    
    Session session = null;
    
    try {
    
        session = getSessie ();
        Keyword zaakId = new Keyword (ProcuraProperty.TYPES.ZAAKID, zaak.getZaakId ());
        return toDmsResult (NodeUtils.getNodes (session, ArchiefDocumentFuncties.getDocumentsMetKeyword (BEGIN, zaakId)));
        }
    finally {
    
        ArchiefClient.close (session);
        }
            */

    throw new NotSupportedException();
  }

  @Override
  public void save(byte[] bytes, String titel, String extensie, String gebruiker, String datatype, String id,
      String zaakId, String dmsNaam, String vertrouwelijkheid) {
    throw new NotSupportedException();
  }

  /**
   * Voor uploads
   */
  @Override
  public void save(File bestand, String titel, String bestandsnaam, String aangemaaktDoor, BasePLExt pl,
      String vertrouwelijkheid) {

    /*
    List<ArchiefDocument> documenten = new ArrayList<> ();
    
    ArchiefDocument       document   = new ArchiefDocument ();
    document.setAangemaaktOp (Calendar.getInstance ());
    document.setAangemaaktDoor (aangemaaktDoor);
    document.setAangepastOp (Calendar.getInstance ());
    document.setAangepastDoor (aangemaaktDoor);
    document.setTitel (titel);
    document.setOmschrijving (bestandsnaam);
    document.setDataType (DocumentType.PL.getType ());
    
    Keyword kwBsn     = new Keyword (BSN, pl.getPersoon ().getBsn ().getWaarde ());
    Keyword kwAnr     = new Keyword (ANR, pl.getPersoon ().getAnr ().getWaarde ());
    Keyword kwBestand = new Keyword (BESTAND, bestand.getName ());
    
    document.addKeyword (kwBsn);
    document.addKeyword (kwAnr);
    document.addKeyword (kwBestand);
    
    ArchiefBestand av = new ArchiefBestand ();
    av.setNaam (bestand.getName ());
    av.setBestand (bestand);
    document.setFile (av);
    
    document.addArchiveActivity (new ProwebPersonenDocumentStatusUpdate ());
    
    documenten.add (document);
    
    opslaan (documenten);
    */

    throw new NotSupportedException();
  }

  @Override
  public DmsDocument save(File bestand, String titel, String bestandsnaam, String aangemaaktDoor, Zaak zaak,
      String vertrouwelijkheid) {

    /*
    List<ArchiefDocument> documenten = new ArrayList<> ();
    
    ArchiefDocument       document   = new ArchiefDocument ();
    document.setAangemaaktOp (Calendar.getInstance ());
    document.setAangemaaktDoor (aangemaaktDoor);
    document.setAangepastOp (Calendar.getInstance ());
    document.setAangepastDoor (aangemaaktDoor);
    document.setTitel (titel);
    document.setOmschrijving (bestandsnaam);
    document.setDataType (DocumentType.PL.getType ());
    
    Keyword kwBsn     = new Keyword (BSN, zaak.getBurgerServiceNummer ().getStringValue ());
    Keyword kwAnr     = new Keyword (ANR, zaak.getAnummer ().getStringValue ());
    Keyword kwZaakId  = new Keyword (ZAAKID, zaak.getZaakId ());
    Keyword kwBestand = new Keyword (BESTAND, bestand.getName ());
    
    document.addKeyword (kwBsn);
    document.addKeyword (kwAnr);
    document.addKeyword (kwZaakId);
    document.addKeyword (kwBestand);
    
    ArchiefBestand av = new ArchiefBestand ();
    av.setNaam (bestand.getName ());
    av.setBestand (bestand);
    document.setFile (av);
    
    document.addArchiveActivity (new ProwebPersonenDocumentStatusUpdate ());
    
    documenten.add (document);
    
    opslaan (documenten);
    */

    throw new NotSupportedException();
  }

  @Override
  public DmsDocument save(Zaak zaak, File bestand, DmsDocument dmsDocument) {
    throw new NotSupportedException();
  }

  @Override
  public void save(PrintActie printActie, byte[] documentBytes) {

    /*
    if (printActie.getZaak () != null) {
    
        String                bsn            = printActie.getZaak ().getBurgerServiceNummer ().getStringValue ();
        String                anr            = printActie.getZaak ().getAnummer ().getStringValue ();
        String                zaakId         = printActie.getZaak ().getZaakId ();
        String                extensie       = printActie.getPrintOptie ().getUitvoerformaatType ().getType ();
        String                bestandsnaam   = NodeUtils.getTimestamp () + "." + extensie;
        String                aangemaaktDoor = getServices ().getGebruiker ().getNaam ();
        Calendar              nu             = Calendar.getInstance ();
        String                titel          = printActie.getDocument ().getDocument ();
    
        List<ArchiefDocument> documenten     = new ArrayList<> ();
        ArchiefDocument       document       = new ArchiefDocument ();
        document.setAangemaaktOp (nu);
        document.setAangemaaktDoor (aangemaaktDoor);
        document.setAangepastOp (nu);
        document.setAangepastDoor (aangemaaktDoor);
        document.setTitel (titel);
        document.setOmschrijving (titel);
        document.setDataType (printActie.getDocument ().getType ());
    
        Keyword kwBsn     = new Keyword (BSN, bsn);
        Keyword kwAnr     = new Keyword (ANR, anr);
        Keyword kwZaakId  = new Keyword (ZAAKID, zaakId);
        Keyword kwBestand = new Keyword (BESTAND, bestandsnaam);
    
        document.addKeyword (kwBsn);
        document.addKeyword (kwAnr);
        document.addKeyword (kwZaakId);
        document.addKeyword (kwBestand);
    
        ArchiefBestand av = new ArchiefBestand ();
        av.setNaam (bestandsnaam);
        av.setBinary (NodeUtils.toBinary (documentBytes));
        document.setFile (av);
    
        document.addArchiveActivity (new ProwebPersonenDocumentStatusUpdate ());
        documenten.add (document);
    
        opslaan (documenten);
        }
    */

    throw new NotSupportedException();
  }

  @Override
  public void delete(DmsDocument record) {

    /*
    Session session = null;
    
    try {
    
        session = getSessie ();
        NodeUtils.deleteNodes (NodeUtils.getNodes (session, ArchiefDocumentFuncties.getDocumentMetNaam (record.getPad ())));
        NodeUtils.save (session);
        }
    finally {
    
        ArchiefClient.close (session);
        }
    */

    throw new NotSupportedException();
  }

  /*
  private ArchiefConnection getConnectie () {
  
      try {
  
          String endpoint       = getParm (ParameterConstant.DOC_DMS_ARCHIEF_ENDPOINT, "Proweb Archief endpoint");
          String workspace      = getParm (ParameterConstant.DOC_DMS_ARCHIEF_WORKSPACE, "Proweb Archief workspace");
          String gebruikersnaam = getParm (ParameterConstant.DOC_DMS_ARCHIEF_GEBRUIKER, "Proweb Archief gebruikersnaam");
          String wachtwoord     = getParm (ParameterConstant.DOC_DMS_ARCHIEF_WW, "Proweb Archief wachtwoord");
  
          return ArchiefClient.getConnection (endpoint, workspace, gebruikersnaam, wachtwoord);
          }
      catch (ProException e) {
          throw new ProException ("Er kan momenteel geen verbinding worden gemaakt met Proweb Archief", e);
          }
      }
  
  private Session getSessie () {
      return getConnectie ().getSession ();
      }
  
  private int getAantalDocumenten (BasisPLWrapper pl) {
  
      if (pl == null) {
          return 0;
          }
  
      Session session = null;
  
      try {
  
          session = getSessie ();
          Keyword bsn = new Keyword (ProcuraProperty.TYPES.BSN, pl.getPersoon ().getBsn ().getCode ());
          Keyword anr = new Keyword (ProcuraProperty.TYPES.ANR, pl.getPersoon ().getAnr ().getCode ());
          return NodeUtils.getSize (session, ArchiefDocumentFuncties.getDocumentsMetKeyword (BEGIN, bsn, anr));
          }
      finally {
          ArchiefClient.close (session);
          }
      }
  
  private void opslaan (List<ArchiefDocument> documenten) {
  
      Session session = null;
  
      try {
  
          session = getSessie ();
  
          Node prowebNode     = NodeUtils.getOrAddNode (session.getRootNode (), ProcuraProperty.SYSTEMEN.PROWEB_PERSONEN);
          Node documentenNode = NodeUtils.getOrAddNode (prowebNode, ProcuraProperty.LIJSTEN.DOCUMENTEN);
  
          for (ArchiefDocument document : documenten) {
              ArchiefDocumentFuncties.addArchiefDocument (documentenNode, document);
              }
  
          NodeUtils.save (session);
          }
      catch (RepositoryException e) {
          throw new ProException ("Fout bij toevoegen node", e);
          }
      finally {
          ArchiefClient.close (session);
          }
      }
  
  private DmsResultaat toDmsResult (List<Node> nodes) {
  
      DmsResultaat dmsResultaat = new DmsResultaat ();
  
      for (Node node : nodes) {
  
          ArchiefDocument l           = NodeToBean.toBean (node, new ArchiefDocument ());
  
          DmsDocument     dmsDocument = new DmsDocument ();
          dmsDocument.setAangemaaktDoor (l.getAangemaaktDoor ());
          dmsDocument.setDatatype (l.getDataType ());
          dmsDocument.setDatum (NodeUtils.toLongDate (l.getAangemaaktOp ()));
          dmsDocument.setExtensie (l.getExtensie ());
          dmsDocument.setBestandsnaam (l.getFile ().getNaam ());
          dmsDocument.setPad (l.getNaam ());
          dmsDocument.setTijd (NodeUtils.toLongTime (l.getAangemaaktOp ()));
          dmsDocument.setTitel (l.getTitel ());
          dmsDocument.setZaakId (l.getKeyword (ProcuraProperty.TYPES.ZAAKID));
  
          dmsResultaat.getDocumenten ().add (dmsDocument);
          }
  
      Collections.sort (dmsResultaat.getDocumenten ());
  
      return dmsResultaat;
      }
   */
}
