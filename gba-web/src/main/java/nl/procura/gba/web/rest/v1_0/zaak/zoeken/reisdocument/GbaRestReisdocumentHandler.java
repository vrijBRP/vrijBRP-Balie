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

package nl.procura.gba.web.rest.v1_0.zaak.zoeken.reisdocument;

import static nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType.*;
import static nl.procura.standard.Globalfunctions.aval;
import static nl.procura.standard.Globalfunctions.fil;

import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElement;
import nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElementType;
import nl.procura.gba.web.rest.v1_0.zaak.GbaRestElementHandler;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.beheer.locatie.Locatie;
import nl.procura.gba.web.services.beheer.parameter.ParameterConstant;
import nl.procura.gba.web.services.zaken.algemeen.Zaak;
import nl.procura.gba.web.services.zaken.inhoudingen.DocumentInhouding;
import nl.procura.gba.web.services.zaken.reisdocumenten.*;
import nl.procura.gba.web.services.zaken.reisdocumenten.clausule.StaatloosType;
import nl.procura.gba.web.services.zaken.reisdocumenten.clausule.TekenenType;
import nl.procura.gba.web.services.zaken.reisdocumenten.clausule.VermeldPartnerType;
import nl.procura.gba.web.services.zaken.reisdocumenten.clausule.VermeldTitelType;

public class GbaRestReisdocumentHandler extends GbaRestElementHandler {

  public GbaRestReisdocumentHandler(Services services) {
    super(services);
  }

  private static void addClausules(ReisdocumentAanvraag zaak, GbaRestElement reisdocument) {

    GbaRestElement clausules = reisdocument.add(GbaRestElementType.CLAUSULES);
    TekenenType ot = zaak.getClausules().getOndertekening();
    VermeldPartnerType vp = zaak.getClausules().getVermeldingPartner();
    StaatloosType sl = zaak.getClausules().getStaatloos();

    add(clausules, GbaRestElementType.GELDIG_VOOR_REIZEN, zaak.getClausules().getGeldigVoorReizen());
    add(clausules, GbaRestElementType.PSEUDONIEM, zaak.getClausules().getPseudoniem());
    add(clausules, GbaRestElementType.TVV, zaak.getClausules().getTvv());
    add(clausules, GbaRestElementType.UITZONDERING_LANDEN, zaak.getClausules().getUitzonderingLanden());

    add(clausules, GbaRestElementType.TYPE_ONDERTEKENING, ot.getCode(), ot.getOms());
    add(clausules, GbaRestElementType.TYPE_STAATLOOS, sl.getCode(), sl.getOms());
    add(clausules, GbaRestElementType.TYPE_VERMELDING_PARTNER, vp.getCode(), vp.getOms());
  }

  private static void addDeelzaken(GbaRestElement gbaZaak, ReisdocumentAanvraag zaak) {

    GbaRestElement deelZaken = gbaZaak.add(DEELZAKEN);
    GbaRestElement deelZaak = deelZaken.add(DEELZAAK);

    add(deelZaak, BSN, zaak.getBurgerServiceNummer());
    add(deelZaak, ANR, zaak.getAnummer());
  }

  private static void addRdwStatus(GbaRestElement reisdocument, ReisdocumentAanvraag zaak) {

    ReisdocumentStatus rdStatus = zaak.getReisdocumentStatus();
    GbaRestElement element = reisdocument.add(GbaRestElementType.REISDOCUMENT_STATUS);

    DateTime dAfsl = rdStatus.getDatumTijdAfsluiting();
    DateTime dLev = rdStatus.getDatumTijdLevering();
    SluitingType sa = rdStatus.getStatusAfsluiting();
    LeveringType sl = rdStatus.getStatusLevering();

    add(element, GbaRestElementType.DATUM_AFSLUITING, dAfsl.getLongDate(), dAfsl.getFormatDate());
    add(element, GbaRestElementType.TIJD_AFSLUITING, dAfsl.getLongTime(), dAfsl.getFormatTime());
    add(element, GbaRestElementType.STATUS_AFSLUITING, sa.getCode(), sa.getOms());
    add(element, GbaRestElementType.NR_NEDERLANDS_DOCUMENT, rdStatus.getNrNederlandsDocument());
    add(element, GbaRestElementType.STATUS_LEVERING, sl.getCode(), sl.getOms());
    add(element, GbaRestElementType.DATUM_LEVERING, dLev.getLongDate(), dLev.getFormatDate());
    add(element, GbaRestElementType.TIJD_LEVERING, dLev.getLongTime(), dLev.getFormatTime());
  }

  private static void addToestemming(GbaRestElement elementen, String id, Toestemming toestemming) {

    if (toestemming != null && toestemming.getType() != null) {
      ToestemmingType tt = toestemming.getType();
      String anr = toestemming.getAnummer();
      String naam = toestemming.getTekstNaam();

      if (tt != null && (fil(anr) || fil(naam))) {
        GbaRestElement element = elementen.add(id);
        add(element, ANR, anr);
        add(element, NAAM, naam);
        add(element, TOELICHTING, toestemming.getToelichting());
        add(element, GEGEVEN, toestemming.getGegeven().getCode(), toestemming.getGegeven().getOms());
        add(element, TYPE, tt.getCode(), tt.getOms());
      }
    }
  }

  private static void addToestemmingen(ReisdocumentAanvraag zaak, GbaRestElement reisdocument) {

    Toestemmingen toestemmingen = zaak.getToestemmingen();

    GbaRestElement elementen = reisdocument.add(GbaRestElementType.TOESTEMMINGEN);

    add(elementen, GbaRestElementType.AANTAL, toestemmingen.getAantal());
    add(elementen, GbaRestElementType.OMSCHRIJVING, toestemmingen.getOmschrijving());

    addToestemming(elementen, GbaRestElementType.OUDER_1, toestemmingen.getToestemmingOuder1());
    addToestemming(elementen, GbaRestElementType.OUDER_2, toestemmingen.getToestemmingOuder2());
    addToestemming(elementen, GbaRestElementType.CURATOR, toestemmingen.getToestemmingCurator());
    addToestemming(elementen, GbaRestElementType.DERDE, toestemmingen.getToestemmingDerde());
  }

  private static void addVermissingen(ReisdocumentAanvraag zaak, GbaRestElement reisdocument) {

    GbaRestElement elementen = reisdocument.add(GbaRestElementType.VERMISSINGEN);
    for (DocumentInhouding v : zaak.getVermissingen()) {
      GbaRestElement element = elementen.add(GbaRestElementType.VERMISSING);
      add(element, GbaRestElementType.ZAAKID, v.getZaakId());
    }
  }

  public void convert(GbaRestElement gbaZaak, Zaak zaakParm) {

    ReisdocumentAanvraag zaak = (ReisdocumentAanvraag) zaakParm;

    GbaRestElement reisdocument = gbaZaak.add(GbaRestElementType.REISDOCUMENT);
    VermeldTitelType vt = zaak.getVermeldingTitel();
    boolean vermGebLand = zaak.isVermeldingLand();

    String bewijsIdentiteit = getServices().getParameterService().getParm(ParameterConstant.RAAS_IDENT_BEWIJS);
    if (zaak.getIdentificatie().isVastgesteld()) {
      bewijsIdentiteit = zaak.getIdentificatie().getKorteOmschrijving();
    }

    add(reisdocument, GbaRestElementType.DATUM_EINDE_GELDIGHEID, zaak.getDatumEindeGeldigheid());
    add(reisdocument, GbaRestElementType.DATUM_EINDE_GELDIGHEID_VB, zaak.getDatumEindeGeldigheidVb());
    add(reisdocument, GbaRestElementType.DATUM_VERSTREKKING, zaak.getDatumVerstrek());
    add(reisdocument, GbaRestElementType.GELDIGHEID, zaak.getGeldigheid());
    add(reisdocument, GbaRestElementType.NR_VB_DOCUMENT, zaak.getNrVbDocument());
    add(reisdocument, GbaRestElementType.REDEN_AFWEZIG, zaak.getRedenAfwezig());
    add(reisdocument, GbaRestElementType.STATUS_SLUITING, zaak.getSluitingStatus());
    add(reisdocument, GbaRestElementType.BEWIJSSTUKKEN_IDENTITEIT, bewijsIdentiteit);
    add(reisdocument, GbaRestElementType.AANVRAAGNUMMER, zaak.getAanvraagnummer().getNummer(),
        zaak.getAanvraagnummer().getFormatNummer());

    add(reisdocument, GbaRestElementType.CODE_RAAS, zaak.getCodeRaas());
    add(reisdocument, GbaRestElementType.GEBRUIKER_AANVRAAG, zaak.getGebruikerAanvraag());
    add(reisdocument, GbaRestElementType.GEBRUIKER_UITGIFTE, zaak.getGebruikerUitgifte());
    add(reisdocument, GbaRestElementType.TERMIJN_GELDIGHEID, aval(zaak.getGeldigheidsTermijn()));
    add(reisdocument, GbaRestElementType.LENGTE, aval(zaak.getLengte()));
    add(reisdocument, GbaRestElementType.TYPE_VERMELDING_TITEL, vt.getCode(), vt.getOms());
    add(reisdocument, GbaRestElementType.VERMELDING_GEB_LAND, vermGebLand);

    Locatie la = zaak.getLocatieAfhaal();
    Locatie li = zaak.getLocatieInvoer();
    add(reisdocument, LOCATIE_AFHAAL, la.getCLocation(), la.getLocatie(), la.getOmschrijving());
    add(reisdocument, LOCATIE_INVOER, li.getCLocation(), li.getLocatie(), li.getOmschrijving());

    add(reisdocument, GbaRestElementType.REISDOCUMENT_TYPE, zaak.getReisdocumentType().getCode(),
        zaak.getReisdocumentType().getOms());
    add(reisdocument, GbaRestElementType.SPOED, zaak.getSpoed().getCode(), zaak.getSpoed().getOms());

    addClausules(zaak, reisdocument);
    addRdwStatus(reisdocument, zaak);
    addToestemmingen(zaak, reisdocument);
    addVermissingen(zaak, reisdocument);
    addDeelzaken(gbaZaak, zaak);
  }
}
