/*
 * Copyright 2023 - 2024 Procura B.V.
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

package nl.procura.gba.web.modules.zaken.reisdocument.page10;

import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.trim;

import nl.procura.burgerzaken.vrsclient.model.AutoriteitVerstrekkingDto;
import nl.procura.burgerzaken.vrsclient.model.ReisdocumentInformatieDocumentnummerUitgevendeInstantiesResponse;
import nl.procura.burgerzaken.vrsclient.model.ReisdocumentInformatieDocumentnummerUitgevendeInstantiesResponsePersonalisatiegegevens;
import nl.procura.burgerzaken.vrsclient.model.ReisdocumentInformatieDocumentnummerUitgevendeInstantiesResponsePersoon;
import nl.procura.burgerzaken.vrsclient.model.ReisdocumentInformatieDocumentnummerUitgevendeInstantiesResponseReisdocument;
import nl.procura.burgerzaken.vrsclient.model.ReisdocumentsoortDto;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.components.layouts.form.ReadOnlyForm;
import nl.procura.gba.web.services.zaken.reisdocumenten.Aanvraagnummer;
import nl.procura.standard.ProcuraDate;
import nl.procura.validation.Bsn;

public class VrsDocumentForm1 extends ReadOnlyForm<VrsDocumentBean1> {

  public VrsDocumentForm1(ReisdocumentInformatieDocumentnummerUitgevendeInstantiesResponse response, String caption,
      String... fields) {
    setCaption(caption);
    setOrder(fields);
    setReadThrough(true);
    setReadonlyAsText(false);
    setColumnWidths("150px", "");

    ReisdocumentInformatieDocumentnummerUitgevendeInstantiesResponseReisdocument reisdocument = response
        .getReisdocument();
    ReisdocumentsoortDto documentsoort = reisdocument.getDocumentsoort();

    VrsDocumentBean1 bean = new VrsDocumentBean1();
    bean.setDocumentnummer(reisdocument.getDocumentnummer());
    bean.setDocumentSoort(documentsoort.getCode() + " - " + documentsoort.getOmschrijving());
    bean.setDocumentAfgifte(DateTime.of(reisdocument.getDocumentAfgifte()).getFormatDate());
    bean.setDocumentGeldigTot(DateTime.of(reisdocument.getDocumentGeldigTot()).getFormatDate());

    ReisdocumentInformatieDocumentnummerUitgevendeInstantiesResponsePersoon persoon = response.getPersoon();
    bean.setBsn(new Bsn(persoon.getBsn()).getFormatBsn());
    bean.setNaam(
        trim(persoon.getVoornamen() + " " + persoon.getVoorvoegselGeslachtsnaam() + " " + persoon.getGeslachtsnaam()));
    bean.setGeboortedatum(new ProcuraDate(persoon.getGeboortedatum()).getFormatDate());
    bean.setGeboorteplaats(persoon.getGeboorteplaats());
    bean.setNationaliteit(persoon.getNationaliteit().getOmschrijving());
    bean.setGeslacht(persoon.getGeslacht().getOmschrijving());

    ReisdocumentInformatieDocumentnummerUitgevendeInstantiesResponsePersonalisatiegegevens persGegevens = response
        .getPersoon()
        .getPersonalisatiegegevens();

    bean.setLengte(astr(persGegevens.getLengte()));
    bean.setStaatloos(Boolean.TRUE.equals(persGegevens.getStaatloze()) ? "Ja" : "Nee");
    bean.setAanvraagnummer(new Aanvraagnummer(astr(persGegevens.getAanvraag().getAanvraagnummer())).getFormatNummer());

    bean.setAutoriteitVanAfgifte(response.getAutoriteitVanAfgifte());
    AutoriteitVerstrekkingDto autVer = response.getAutoriteitVerstrekking();
    bean.setAutoriteitVerstrekking(autVer.getAutoriteitVerstrekkingCode()
        + " - " + autVer.getAutoriteitVerstrekkingOmschrijving());

    setBean(bean);
  }
}
