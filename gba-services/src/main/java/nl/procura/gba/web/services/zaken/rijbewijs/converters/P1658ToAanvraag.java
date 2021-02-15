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

package nl.procura.gba.web.services.zaken.rijbewijs.converters;

import static nl.procura.gba.web.common.tables.GbaTables.NATIO;
import static nl.procura.standard.Globalfunctions.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.common.DateTime.TimeType;
import nl.procura.gba.web.components.fields.values.UsrFieldValue;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.gba.web.services.zaken.rijbewijs.*;
import nl.procura.rdw.messages.P1658;
import nl.procura.rdw.processen.p1658.f02.AANVRRYBKGEG;
import nl.procura.rdw.processen.p1658.f02.AANVRRYBKRT;
import nl.procura.rdw.processen.p1658.f02.NATPERSOONGEG;
import nl.procura.rdw.processen.p1658.f02.STATRYBKGEG;
import nl.procura.vaadin.component.field.fieldvalues.AnrFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;

public class P1658ToAanvraag {

  private final static Logger LOGGER = LoggerFactory.getLogger(P1658ToAanvraag.class.getName());

  public static RijbewijsAanvraag get(P1658 proces, BasePLExt pl, Services services) {

    RijbewijsAanvraag aanvraag = (RijbewijsAanvraag) services.getRijbewijsService().getNewZaak();

    AANVRRYBKRT p1652f2 = (AANVRRYBKRT) proces.getResponse().getObject();

    LOGGER.debug("Convert Proces 1658 Functie 2");

    NATPERSOONGEG pers_geg = p1652f2.getNatpersoongeg();
    AANVRRYBKGEG aanvr_geg = p1652f2.getAanvrrybkgeg();
    STATRYBKGEG stat_geg = p1652f2.getStatrybkgeg();
    Gebruiker usr = services.getGebruiker();

    // Algemene gegevens

    aanvraag.setDatumVertrek(new DateTime(proces.getDatumVertrek()));
    aanvraag.setDatumVestiging(new DateTime(proces.getDatumVestiging()));
    aanvraag.setLandVertrek(proces.getCodeLandVertrek());
    aanvraag.setLandVestiging(proces.getCodeLandVestiging());
    aanvraag.setSoortId(proces.getSoortId());
    aanvraag.setEmail(proces.getEmail());
    aanvraag.setTelnrMob(proces.getTelMobiel());
    aanvraag.setTelnrThuis(proces.getTelThuis());
    aanvraag.setTelnrWerk(proces.getTelWerk());

    // Specifieke gegevens voor p1658

    aanvraag.setAanvraagNummer(astr(aanvr_geg.getAanvrnrrybk()));
    aanvraag.setAnummer(new AnrFieldValue(astr(pers_geg.getGbanrnatp())));
    aanvraag.setBurgerServiceNummer(new BsnFieldValue(astr(pers_geg.getFiscnrnatp())));
    aanvraag.setCodeRas(aval(usr.getLocatie().getCodeRas()));
    aanvraag.setCodeVerblijfstitel(along(pl.getVerblijfstitel().getVerblijfstitel().getVal()));
    aanvraag.setDatumTijdInvoer(new DateTime(along(aanvr_geg.getAanvrdatrybk()), along(aanvr_geg.getAanvrtydrybk()),
        TimeType.TIME_4_DIGITS));

    aanvraag.setGbaBestendig(isTru(aanvr_geg.getGbabestind()));
    aanvraag.setGebruikerAanvraag(new UsrFieldValue(usr.getCUsr(), usr.getNaam()));
    aanvraag.setGebruikerUitgifte(new UsrFieldValue(0, ""));
    aanvraag.setIndicatie185(true);

    aanvraag.setLocatieInvoer(usr.getLocatie());
    aanvraag.setNaamgebruik(NaamgebruikType.getByRdwCode(along(pers_geg.getNaamgebrnatp())));
    aanvraag.setNationaliteiten(NATIO.get(pers_geg.getNationalpers().toString()).getDescription());
    aanvraag.setProcesVerbaalVerlies("");
    aanvraag.setRedenAanvraag(RijbewijsAanvraagReden.get(along(aanvr_geg.getRedenaanrybk())));
    aanvraag.setSoortAanvraag(RijbewijsAanvraagSoort.get(along(aanvr_geg.getSrtaanvrrybk())));
    aanvraag.setRijbewijsnummer("");

    aanvraag.setSpoed(isTru(aanvr_geg.getSpoedafhind()));
    aanvraag.setVervangingsRbwNr(aanvr_geg.getRybnrvervang());

    // Status toevoegen

    long dIn = along(stat_geg.getStatdatrybk());
    long tIn = along(stat_geg.getStattydrybk());
    RijbewijsStatusType stat = RijbewijsStatusType.get(along(stat_geg.getStatcoderybk()));

    aanvraag.getStatussen().addStatus(new RijbewijsAanvraagStatus(dIn, tIn, stat, services.getGebruiker()));

    return aanvraag;
  }
}
