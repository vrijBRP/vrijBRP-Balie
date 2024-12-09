package nl.procura.gba.web.services.zaken.inhoudingen;

import lombok.Getter;
import nl.procura.burgerzaken.vrsclient.api.VrsDocumentStatusType;
import nl.procura.burgerzaken.vrsclient.model.ReisdocumentInformatiePersoonsGegevensInstantieResponseReisdocumentPersoonsgegevensResponse;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.common.MiscUtils;
import nl.procura.gba.web.services.zaken.reisdocumenten.ReisdocumentType;

@Getter
public class BasisregisterDocument {

  private final ReisdocumentType      soort;
  private final String                nummerDocument;
  private final DateTime              datumAfgifte;
  private final DateTime              geldigTot;
  private final String                autoriteit;
  private final VrsDocumentStatusType status;

  public BasisregisterDocument(ReisdocumentInformatiePersoonsGegevensInstantieResponseReisdocumentPersoonsgegevensResponse doc) {
    this.soort = ReisdocumentType.get(doc.getDocumentsoort().getCode());
    this.nummerDocument = doc.getDocumentnummer();
    this.datumAfgifte = new DateTime(MiscUtils.toDate(doc.getDatumafgifte()));
    this.geldigTot = new DateTime(MiscUtils.toDate(doc.getDatumGeldigTot()));
    this.autoriteit = doc.getAutoriteitVerstrekking().getOmschrijving();
    this.status = VrsDocumentStatusType.getByCode(doc.getStatusMeestRecent().getDocumentstatusCode());
  }
}
