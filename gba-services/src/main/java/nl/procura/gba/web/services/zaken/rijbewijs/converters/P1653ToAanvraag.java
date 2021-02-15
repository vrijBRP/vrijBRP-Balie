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
import nl.procura.rdw.messages.P1653;
import nl.procura.rdw.processen.p1653.f02.*;
import nl.procura.vaadin.component.field.fieldvalues.AnrFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;

public class P1653ToAanvraag {

  private final static Logger LOGGER = LoggerFactory.getLogger(P1653ToAanvraag.class.getName());

  public static RijbewijsAanvraag get(P1653 proces, RijbewijsAanvraag aanvraag, BasePLExt pl,
      Services services) {

    AANVRRYBKRT p1653f2 = (AANVRRYBKRT) proces.getResponse().getObject();

    LOGGER.debug("Convert Proces 1653 Functie 2");

    NATPERSOONGEG pers_geg = p1653f2.getNatpersoongeg();
    AANVRRYBKGEG aanvr_geg = p1653f2.getAanvrrybkgeg();
    STATRYBKGEG stat_geg = p1653f2.getStatrybkgeg();
    RYBGEG ryb_geg = p1653f2.getRybgeg();
    Gebruiker usr = services.getGebruiker();

    // Algemene gegevens

    aanvraag.setDatumVertrek(new DateTime(proces.getDatumVertrek()));
    aanvraag.setDatumVestiging(new DateTime(proces.getDatumVestiging()));
    aanvraag.setLandVertrek(proces.getCodeLandVertrek());
    aanvraag.setLandVestiging(proces.getCodeLandVestiging());
    aanvraag.setEmail(proces.getEmail());
    aanvraag.setSoortId(proces.getSoortId());
    aanvraag.setTelnrMob(proces.getTelMobiel());
    aanvraag.setTelnrThuis(proces.getTelThuis());
    aanvraag.setTelnrWerk(proces.getTelWerk());

    aanvraag.setAanvraagNummer(astr(aanvr_geg.getAanvrnrrybk()));
    aanvraag.setAnummer(new AnrFieldValue(astr(pers_geg.getGbanrnatp())));
    aanvraag.setBurgerServiceNummer(new BsnFieldValue(astr(pers_geg.getFiscnrnatp())));
    aanvraag.setCodeRas(aval(usr.getLocatie().getCodeRas()));
    aanvraag.setCodeVerblijfstitel(along(pl.getVerblijfstitel().getVerblijfstitel().getVal()));
    aanvraag.setBasisPersoon(pl);
    aanvraag.setDatumTijdInvoer(new DateTime(along(aanvr_geg.getAanvrdatrybk()), along(aanvr_geg.getAanvrtydrybk()),
        TimeType.TIME_4_DIGITS));

    // Specifiek gegevens

    aanvraag.setGbaBestendig(isTru(aanvr_geg.getGbabestind()));
    aanvraag.setGebruikerAanvraag(new UsrFieldValue(usr.getCUsr(), usr.getNaam()));
    aanvraag.setGebruikerUitgifte(new UsrFieldValue(0, ""));
    aanvraag.setIndicatie185(true);

    aanvraag.setLocatieInvoer(usr.getLocatie());
    aanvraag.setNaamgebruik(NaamgebruikType.getByRdwCode(along(pers_geg.getNaamgebrnatp())));
    aanvraag.setNationaliteiten(NATIO.get(pers_geg.getNationalpers().toString()).getDescription());
    aanvraag.setProcesVerbaalVerlies(ryb_geg.getProcvverld());
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
