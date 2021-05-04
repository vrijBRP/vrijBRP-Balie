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

package nl.procura.gba.web.rest.v1_0.document;

import static java.util.Collections.singletonList;
import static nl.procura.diensten.gba.ple.openoffice.DocumentPLConverter.convert;
import static nl.procura.gba.web.services.zaken.documenten.DocumentType.*;
import static nl.procura.standard.Globalfunctions.astr;

import java.util.List;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.openoffice.DocumentPL;
import nl.procura.diensten.gba.ple.openoffice.DocumentPLConverter;
import nl.procura.gba.common.ConditionalMap;
import nl.procura.gba.web.rest.v1_0.GbaRestHandler;
import nl.procura.gba.web.rest.v1_0.document.genereren.GbaRestDocumentGegeven;
import nl.procura.gba.web.rest.v1_0.document.genereren.GbaRestDocumentPersoon;
import nl.procura.gba.web.rest.v1_0.document.genereren.GbaRestDocumentZaak;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.gba.web.services.zaken.algemeen.ZakenService;
import nl.procura.gba.web.services.zaken.documenten.DocumentRecord;
import nl.procura.gba.web.services.zaken.documenten.DocumentType;
import nl.procura.gba.web.services.zaken.documenten.UitvoerformaatType;
import nl.procura.gba.web.services.zaken.documenten.printen.PrintActie;
import nl.procura.gba.web.services.zaken.documenten.printen.PrintModelUtils;
import nl.procura.gba.web.services.zaken.documenten.printopties.PrintOptie;

public class GbaRestDocumentGenererenHandler extends GbaRestHandler {

  public GbaRestDocumentGenererenHandler(Services services) {
    super(services);
  }

  protected byte[] getDocumentByAlias(DocumentRecord document, String type, GbaRestDocumentPersoon persoon) {
    // PL opvragen
    BasePLExt pl = getPersoonslijst(persoon.getBsn());
    List<DocumentPL> dps = convert(singletonList(pl), null);
    dps.stream().filter(dp -> !document.isStillbornAllowed()).forEach(DocumentPLConverter::removeStillborns);

    // Fill map
    ConditionalMap map = new ConditionalMap();
    map.put(DocumentType.PL_UITTREKSEL.getDoc(), dps);

    persoon.getGegevens().forEach(gegeven -> map.put(gegeven.getNaam(), gegeven.getWaarde()));
    document.setPrintOpties(getServices().getPrintOptieService()
        .getPrintOpties(getServices().getGebruiker(), document));

    PrintActie printActie = new PrintActie();
    printActie.setModel(map);
    printActie.setDocument(document);
    printActie.setZaak(null);

    PrintOptie printOptie = new PrintOptie();
    printOptie.setUitvoerformaatType(UitvoerformaatType.getById(type));
    printActie.setPrintOptie(printOptie);

    byte[] documentBytes = getServices().getDocumentService().preview(printActie);
    getServices().getDmsService().save(printActie, documentBytes);
    return documentBytes;
  }

  protected byte[] getDocumentByAlias(DocumentRecord document, String type, GbaRestDocumentZaak zaak) {
    // Zaak opvragen
    ZaakArgumenten z = new ZaakArgumenten(zaak.getZaakId());
    ZakenService service = getServices().getZakenService();
    List<Zaak> zaken = service.getVolledigeZaken(service.getMinimaleZaken(z));

    ConditionalMap map = new ConditionalMap();
    map.put(document.getType(), PrintModelUtils.getModel(zaken.get(0), zaken.get(0), document));

    for (GbaRestDocumentGegeven gegeven : zaak.getGegevens()) {
      map.put(gegeven.getNaam(), gegeven.getWaarde());
    }

    PrintActie printActie = new PrintActie();
    printActie.setModel(map);
    printActie.setDocument(document);
    printActie.setZaak(zaken.get(0));

    PrintOptie printOptie = new PrintOptie();
    printOptie.setUitvoerformaatType(UitvoerformaatType.getById(type));
    printActie.setPrintOptie(printOptie);

    byte[] documentBytes = getServices().getDocumentService().preview(printActie);
    getServices().getDmsService().save(printActie, documentBytes);
    return documentBytes;
  }

  protected DocumentRecord getDocumentByAlias(long documentCode) {
    DocumentType[] types = { PL_UITTREKSEL, PL_FORMULIER, PL_NATURALISATIE, PL_OPTIE, PL_ADRESONDERZOEK, ZAAK };
    for (DocumentRecord document : getServices().getDocumentService().getDocumenten(types)) {
      if (document.getCDocument().equals(documentCode)) {
        return document;
      }
    }

    throw new IllegalArgumentException(
        "Document met code " + documentCode + " niet gevonden of is niet gekoppeld aan deze gebruiker");
  }

  protected DocumentRecord getDocumentByAlias(String alias) {
    DocumentType[] types = { PL_UITTREKSEL, PL_FORMULIER, PL_NATURALISATIE, PL_OPTIE, PL_ADRESONDERZOEK, ZAAK };
    for (DocumentRecord document : getServices().getDocumentService().getDocumenten(types)) {
      if (astr(document.getAlias()).equalsIgnoreCase(alias)) {
        return document;
      }
    }

    throw new IllegalArgumentException(
        "Document met alias " + alias + " niet gevonden of is niet gekoppeld aan deze gebruiker");
  }
}
