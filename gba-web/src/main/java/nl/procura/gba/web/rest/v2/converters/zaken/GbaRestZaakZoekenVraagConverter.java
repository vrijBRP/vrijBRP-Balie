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

package nl.procura.gba.web.rest.v2.converters.zaken;

import static nl.procura.commons.core.utils.ProNumberUtils.isPos;
import static nl.procura.gba.web.services.zaken.algemeen.ZaakSortering.DATUM_INGANG_OUD_NIEUW;

import org.apache.commons.lang3.StringUtils;

import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.jpa.personen.dao.ZaakKey;
import nl.procura.gba.web.rest.v2.model.zaken.GbaRestZaakZoekenVraag;
import nl.procura.gba.web.rest.v2.model.zaken.base.GbaRestZaakAttribuut;
import nl.procura.gba.web.rest.v2.model.zaken.base.GbaRestZaakStatusType;
import nl.procura.gba.web.rest.v2.model.zaken.base.GbaRestZaakType;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;
import nl.procura.gba.web.services.zaken.algemeen.ZaakSortering;
import nl.procura.validation.Anummer;
import nl.procura.validation.Bsn;

public final class GbaRestZaakZoekenVraagConverter {

  private GbaRestZaakZoekenVraagConverter() {
  }

  public static ZaakArgumenten toZaakargumenten(GbaRestZaakZoekenVraag vraag) {

    ZaakArgumenten za = new ZaakArgumenten();

    // Als er een zaakId is met 1 type dan die combineren
    if (vraag.getZaakIds() != null) {
      for (String zaakId : vraag.getZaakIds()) {
        ZaakType zaakType = ZaakType.ONBEKEND;
        if (vraag.getZaakTypes() != null && vraag.getZaakTypes().size() == 1) {
          zaakType = toZaakType(vraag.getZaakTypes().get(0));
        }
        za.addZaakKey(new ZaakKey(zaakId, zaakType));
      }
    }
    if (vraag.getZaakTypes() != null) {
      for (GbaRestZaakType type : vraag.getZaakTypes()) {
        za.getTypen().add(toZaakType(type));
      }
    }

    // Zoeken op persoonnummer
    if (StringUtils.isNotBlank(vraag.getPersoonId())) {
      Bsn bsn = new Bsn(vraag.getPersoonId());
      if (bsn.isCorrect()) {
        za.setNummer(bsn.getDefaultBsn());
      } else {
        Anummer anr = new Anummer(vraag.getPersoonId());
        if (anr.isCorrect()) {
          za.setNummer(anr.getAnummer());
        } else {
          throw new IllegalArgumentException("Incorrecte BSN of a-nummer ingegeven.");
        }
      }
    }

    if (vraag.getZaakStatussen() != null) {
      for (GbaRestZaakStatusType status : vraag.getZaakStatussen()) {
        za.addStatussen(ZaakStatusType.get(status.getCode()));
      }
    }

    if (vraag.getZaakAttributen() != null) {
      for (GbaRestZaakAttribuut attribuut : vraag.getZaakAttributen()) {
        if (attribuut.isBestaat()) {
          za.addAttributen(attribuut.getWaarde());
        } else {
          za.addOntbrekendeAttributen(attribuut.getWaarde());
        }
      }
    }

    if (vraag.getInvoerDatum() != null) {
      if (isPos(vraag.getInvoerDatum().getVan())) {
        za.setdInvoerVanaf(vraag.getInvoerDatum().getVan());
      }
      if (isPos(vraag.getInvoerDatum().getTm())) {
        za.setdInvoerTm(vraag.getInvoerDatum().getTm());
      }
    }

    if (vraag.getIngangsDatum() != null) {
      if (isPos(vraag.getIngangsDatum().getVan())) {
        za.setdIngangVanaf(vraag.getIngangsDatum().getVan());
      }
      if (isPos(vraag.getIngangsDatum().getTm())) {
        za.setdIngangTm(vraag.getIngangsDatum().getTm());
      }
    }

    if (vraag.getMutatieDatum() != null) {
      if (isPos(vraag.getMutatieDatum().getVan())) {
        za.setdMutatieVanaf(vraag.getMutatieDatum().getVan());
      }
      if (isPos(vraag.getMutatieDatum().getTm())) {
        za.setdMutatieTm(vraag.getMutatieDatum().getTm());
      }
    }

    if (isPos(vraag.getMax())) {
      za.setMax(vraag.getMax());
    }

    if (vraag.getSortering() != null) {
      za.setSortering(ZaakSortering.get(vraag.getSortering().getCode(), DATUM_INGANG_OUD_NIEUW));
    }

    return za;
  }

  private static ZaakType toZaakType(GbaRestZaakType restZaakType) {
    switch (restZaakType) {
      case BINNENVERHUIZING:
      case BUITENVERHUIZING:
      case EMIGRATIE:
      case HERVESTIGING:
        return ZaakType.VERHUIZING;
      default:
        return ZaakType.get(restZaakType.getCode());
    }
  }
}
