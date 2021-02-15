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

package nl.procura.gba.web.services.zaken.vog;

import static nl.procura.gba.common.MiscUtils.formatPostcode;
import static nl.procura.gba.web.services.beheer.parameter.ParameterConstant.*;
import static nl.procura.standard.Globalfunctions.*;
import static nl.procura.standard.exceptions.ProExceptionSeverity.*;
import static nl.procura.standard.exceptions.ProExceptionType.WEBSERVICE;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import nl.procura.covog.objecten.verzendAanvraagNp.*;
import nl.procura.covog.soap.VogSoapHandler;
import nl.procura.gba.web.common.misc.GbaLogger;
import nl.procura.gba.web.services.AbstractService;
import nl.procura.standard.exceptions.ProException;

public class VogBerichtService extends AbstractService {

  public VogBerichtService() {
    super("Vogberichten");
  }

  public VogAanvraag registreer(VogAanvraag zaak) {

    VogSoapHandler soapHandler = getSoapHandler();

    CovogAanvraag converted = convert(zaak);

    VogsService vogs = getServices().getVogService();

    try {
      zaak.setAanvraagId(soapHandler.send(converted));

      vogs.getZaakStatussen().updateStatus(zaak, zaak.getStatus(),
          vogs.getZaakStatussen().getInitieleStatus(zaak),
          "Aanvraag geregistreerd");

      return zaak;
    } catch (Exception e) {
      throw new ProException(WEBSERVICE, ERROR, "Foutmelding ontvangen." + e.getMessage(), e);
    } finally {
      GbaLogger.log("covog", soapHandler);
    }
  }

  public void findStatus(VogAanvraag aanvraag) {

    VogSoapHandler soapHandler = getSoapHandler();

    long cid;
    long id = along(aanvraag.getAanvraagId());

    try {
      cid = along(soapHandler.send(aanvraag.getVogNummer().getCOVOGNummer()));
    } catch (Exception e) {
      throw new ProException(WEBSERVICE, ERROR, "Foutmelding ontvangen." + e.getMessage(), e);
    } finally {
      GbaLogger.log("covog", soapHandler);
    }

    if (id == cid) {
      throw new ProException(WEBSERVICE, INFO,
          "Gevonden: het aanvraagnummer is bekend bij COVOG onder dezelfde code.</p>");
    } else if (pos(cid)) {
      throw new ProException(WEBSERVICE, INFO, MessageFormat.format(
          "Gevonden onder voorbehoud: het aanvraagnummer is bekend bij COVOG, maar onder een ander nummer, namelijk {0}.",
          astr(cid)));
    } else {
      throw new ProException(WEBSERVICE, INFO, "NIET gevonden: het aanvraagnummer is niet bekend bij COVOG");
    }
  }

  private CovogAanvraag convert(VogAanvraag aanvraag) {

    CovogAanvraag ca = new CovogAanvraag();

    VogAanvraag a = aanvraag;
    VogAanvraagBelanghebbende b = aanvraag.getBelanghebbende();
    VogNummer an = a.getVogNummer();
    VogAanvrager v = aanvraag.getAanvrager();
    VogAanvraagDoel d = aanvraag.getDoel();
    VogAanvraagScreening c = aanvraag.getScreening();
    VogAanvraagOpmerkingen o = aanvraag.getOpmerkingen();

    ca.setAanvraagNummer(an.getCOVOGNummer());
    ca.setGemeenteCode(astr(an.getCodeGemeente()));

    ca.setLocatieCode(astr(an.getCodeLocatie()));
    ca.setAanvraagdatum(astr(an.getDatumAanvraag()));

    ca.setDoelCode(d.getDoel().getCVogDoelTab());
    ca.setToelichtingDoel(count(d.getDoelTekst(), 100, "toelichting doel"));
    ca.setIndicatieOmstandigheden(getBVeld(c.isOmstandigheden()));
    ca.setIndicatiePersisteren(getBVeld(o.isPersisteren()));
    ca.setIndicatieBijzonderheden(getBVeld(o.isByzonderheden()));
    ca.setIndicatieCovogAdvies(getBVeld(o.isCovogAdvies()));
    ca.setBurgemeestersadvies(o.getBurgemeesterAdvies().getCode());

    ca.setOpmerkingGemeente(count(o.getOpmerkingenGemeente(), 2000, "opmerking gemeente"));

    // Aanvrager
    CovogOnderzoeksPersoonNP cv = new CovogOnderzoeksPersoonNP();
    cv.setBurgerServiceNummer(v.getBurgerServiceNummer().getStringValue());
    cv.setGeslachtsnaam(v.getGeslachtsnaam());
    cv.setFunctie(d.getFunctie());
    cv.setVoorvoegsel(v.getVoorvoegsel());
    cv.setVoornamen(v.getVoornamen());
    cv.setGeboortedatum(astr(v.getDatumGeboorte().getLongDate()));

    String gebP = v.getPlaatsGeboren().getStringValue();

    if (pos(gebP)) {
      cv.setGemeenteCodeGeboren(gebP);
    } else if (fil(gebP)) {
      cv.setGeboorteplaatsBuitenland(gebP);
    }

    cv.setLandCodeGeboren(v.getLandGeboren().getStringValue());

    if (v.getNationaliteiten().size() > 0) {

      List<String> natio = new ArrayList<>();

      for (VogNationaliteit vn : v.getNationaliteiten()) {

        natio.add(vn.getCode());
      }

      cv.setNationaliteiten(natio.toArray(new String[v.getNationaliteiten().size()]));
    }

    cv.setGeslacht(v.getGeslacht().getAfkorting());
    cv.setAanschrijfnaam(count(v.getAanschrijf(), 100, "aanschrijfnaam"));
    cv.setStraat(count(v.getStraat(), 35, "straat"));
    cv.setHuisnummer(astr(v.getHnr()));
    cv.setHuisnummerToevoeging(v.getHnrL() + " " + v.getHnrT());
    cv.setPostcode(formatPostcode(v.getPc().getStringValue()));
    cv.setPlaats(count(v.getPlaats(), 40, "plaats"));
    cv.setLandCode(v.getLand().getStringValue());

    if ((c.getProfiel() != null) && pos(c.getProfiel().getVogProfTab())) {
      cv.setScreeningsprofiel(astr(c.getProfiel().getVogProfTab()));
    }

    List<CovogFunctieaspect> l = new ArrayList<>();

    for (VogFunctie fc : c.getFunctiegebieden()) {
      l.add(new CovogFunctieaspect(fc.getVogFuncTab()));
    }

    if (pos(l.size())) {
      cv.setFunctieaspecten(l.toArray(new CovogFunctieaspect[l.size()]));
    }

    ca.setOnderzoeksPersoonNP(cv);

    // Belanghebbende
    CovogBelanghebbende cb = new CovogBelanghebbende();
    cb.setNaamBelanghebbende(count(b.getNaam(), 80, "naam belanghebbende"));
    cb.setNaamVertegenwoordiger(count(b.getVertegenwoordiger(), 80, "naam vertegenwoordiger"));
    cb.setStraat(count(b.getStraat(), 35, "straat"));
    cb.setHuisnummer(astr(b.getHnr()));
    cb.setHuisnummerToevoeging(b.getHnrL() + " " + b.getHnrT());
    cb.setPostcode(formatPostcode(b.getPc().getStringValue()));
    cb.setPlaats(count(b.getPlaats(), 40, "plaats"));
    cb.setLandCode(b.getLand().getStringValue());
    cb.setTelefoon(count(b.getTel(), 15, "telefoonnummer"));

    ca.setBelanghebbende(cb);

    // Historie

    List<CovogHistorie> lh = new ArrayList<>();

    for (VogAanvragerHistorie ah : a.getHistorie()) {

      CovogHistorie h = new CovogHistorie();

      h.setGeboortedatum(astr(ah.getDatumGeboorte().getLongDate()));

      String gem = ah.getGemeenteGeboren().getStringValue();

      if (pos(gem)) {
        h.setGemeenteCodeGeboren(gem);
      } else if (fil(gem)) {
        h.setGeboorteplaatsBuitenland(count(gem, 40, "geboorteplaats buitenland"));
      }

      h.setLandCodeGeboren(ah.getLandGeboren().getStringValue());

      h.setGeslacht(ah.getGeslacht().getAfkorting());
      h.setGeslachtsnaam(ah.getGeslachtsnaam());
      h.setVoornamen(ah.getVoornamen());
      h.setVoorvoegsel(ah.getVoorvoegsel());
      h.setBurgerServiceNummer(ah.getBurgerServiceNummer().getStringValue());
      lh.add(h);
    }

    if (lh.size() > 0) {
      cv.setHistories(lh.toArray(new CovogHistorie[0]));
    }

    return ca;
  }

  private String count(String value, int max, String caption) {
    if (astr(value).length() > max) {
      throw new ProException(WARNING,
          "Versturen van de Covog aanvraag is niet mogelijk.<br/>Het veld <b>" + caption + "</b> is te lang.");
    }
    return value;
  }

  private String getBVeld(boolean b) {

    return b ? "J" : "N";
  }

  private VogSoapHandler getSoapHandler() {

    String vogEndpoint = getProxyUrl(COVOG_URL, true);
    String vogRelatieId = getParm(COVOG_RELATIE_ID, true);
    String vogIdCode = getParm(COVOG_ID_CODE, true);

    return new VogSoapHandler(vogEndpoint, vogRelatieId, vogIdCode);
  }
}
