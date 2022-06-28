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

package nl.procura.gba.web.rest.v2.services;

import static java.util.Optional.ofNullable;
import static nl.procura.gba.web.rest.v2.converters.GbaRestBaseTypeConverter.toTableRecord;
import static nl.procura.gba.web.rest.v2.model.base.GbaRestEnum.toEnum;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import nl.procura.gba.web.components.fields.values.GbaDateFieldValue;
import nl.procura.gba.web.rest.v2.model.base.GbaRestGeslacht;
import nl.procura.gba.web.rest.v2.model.zaken.base.akte.GbaRestAkte;
import nl.procura.gba.web.rest.v2.model.zaken.base.akte.GbaRestAkteRegisterSoort;
import nl.procura.gba.web.rest.v2.model.zaken.base.natio.GbaRestNatioDatumVanafType;
import nl.procura.gba.web.rest.v2.model.zaken.base.natio.GbaRestNationaliteit;
import nl.procura.gba.web.rest.v2.model.zaken.base.persoon.GbaRestPersoon;
import nl.procura.gba.web.rest.v2.model.zaken.base.persoon.GbaRestPersoonAdres;
import nl.procura.gba.web.rest.v2.model.zaken.geboorte.GbaRestBurgerlijkeStaatType;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.bs.algemeen.Dossier;
import nl.procura.gba.web.services.bs.algemeen.akte.DossierAkte;
import nl.procura.gba.web.services.bs.algemeen.enums.DossierPersoonType;
import nl.procura.gba.web.services.bs.algemeen.nationaliteit.DossierNationaliteit;
import nl.procura.gba.web.services.bs.algemeen.nationaliteit.DossierNationaliteiten;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;

public class GbaRestDossierService extends GbaRestAbstractService {

  public GbaRestDossierService(GbaRestServices restServices, Services services) {
    super(restServices, services);
  }

  public GbaRestPersoon getRestPersoon(Dossier zaak, DossierPersoonType persoonType) {
    return zaak.getPersonen(persoonType)
        .stream()
        .findFirst()
        .filter(DossierPersoon::isVolledig)
        .map(this::toRestPersoon)
        .orElse(null);
  }

  public GbaRestPersoon getRestPersoon(DossierPersoon persoon) {
    return toRestPersoon(persoon);
  }

  public List<GbaRestAkte> toGbaRestAktes(Dossier zaak) {
    List<DossierAkte> aktes = zaak.getAktes();
    if (aktes != null && !aktes.isEmpty()) {
      List<GbaRestAkte> restAktes = new ArrayList<>();
      for (DossierAkte akte : aktes) {
        GbaRestAkte restAkte = new GbaRestAkte();
        restAkte.setVolledigAktenummer(akte.getAkte());
        restAkte.setJaar(akte.getJaar().intValueExact());
        restAkte.setPlaats("");
        restAkte.setDatumAkte(akte.getDatumIngang().getIntDate());
        restAkte.setDatumFeit(akte.getDatumFeit().toInt());
        restAkte.setRegisterDeel(akte.getRegisterdeel());
        restAkte.setRegisterSoort(toEnum(GbaRestAkteRegisterSoort.values(),
            akte.getAkteRegistersoort().getCode()));
        restAkte.setVolgnummer(akte.getVnr().intValue());
        restAktes.add(restAkte);
      }
      return restAktes;
    }
    return null;
  }

  public List<GbaRestNationaliteit> toGbaRestPersoonNatios(DossierNationaliteiten dossierNationaliteitObject) {
    List<DossierNationaliteit> natios = dossierNationaliteitObject.getNationaliteiten();
    if (natios != null && !natios.isEmpty()) {
      List<GbaRestNationaliteit> restNatios = new ArrayList<>();
      for (DossierNationaliteit natio : natios) {
        GbaRestNationaliteit restNatio = new GbaRestNationaliteit();
        restNatio.setDatumVerkrijging(natio.getDatumVerkrijging().getIntDate());
        restNatio.setNationaliteit(toTableRecord(natio.getNationaliteit()));
        restNatio.setDatumVerkrijgingVanafType(toEnum(GbaRestNatioDatumVanafType.values(),
            natio.getVerkrijgingType().getCode()));
        restNatio.setRedenVerkrijgingNL(toTableRecord(natio.getRedenverkrijgingNederlanderschap()));
        restNatios.add(restNatio);
      }
      return restNatios;
    }
    return null;
  }

  public DossierPersoon toDossierPersoon(GbaRestPersoon restPersoon, DossierPersoon persoon) {
    persoon.setVoornaam(restPersoon.getVoornamen());
    persoon.setGeslachtsnaam(restPersoon.getGeslachtsnaam());
    persoon.setVoorvoegsel(restPersoon.getVoorvoegsel());
    persoon.setTitel(new FieldValue(restPersoon.getTitelPredikaat()));
    persoon.setDatumGeboorte(new GbaDateFieldValue(ofNullable(restPersoon.getGeboortedatum()).orElse(-1)));
    persoon.setToelichting(restPersoon.getToelichting());
    return persoon;
  }

  private GbaRestPersoon toRestPersoon(DossierPersoon persoon) {
    GbaRestPersoon restPersoon = new GbaRestPersoon();
    restPersoon.setBsn(persoon.getBsn().longValue());
    restPersoon.setContactgegevens(getRestServices().getContactService().toGbaRestContactgegevens(restPersoon));
    restPersoon.setVoornamen(persoon.getVoornaam());
    restPersoon.setGeslachtsnaam(persoon.getGeslachtsnaam());
    restPersoon.setVoorvoegsel(persoon.getVoorvoegsel());
    restPersoon.setTitelPredikaat(persoon.getTitel().getStringValue());
    restPersoon.setAktenaam(persoon.getAktenaam());

    if (StringUtils.isNotBlank(persoon.getGeslacht().getAfkorting())) {
      restPersoon.setGeslacht(toEnum(GbaRestGeslacht.values(), persoon.getGeslacht().getAfkorting()));
    }

    GbaRestPersoonAdres adres = new GbaRestPersoonAdres();
    adres.setStraat(persoon.getStraat());
    adres.setHnr((int) persoon.getHuisnummer());
    adres.setHnrL(persoon.getHnrL());
    adres.setHnrT(persoon.getHnrT());
    adres.setPostcode(persoon.getPostcode().getStringValue());
    adres.setWoonplaats(toTableRecord(persoon.getWoonplaats()));
    adres.setWoonplaatsOpAkte(persoon.getWoonplaatsAkte());
    adres.setLand(toTableRecord(persoon.getLand()));
    adres.setLandOpAkte(persoon.getWoonLandAkte());
    adres.setGemeente(toTableRecord(persoon.getWoongemeente()));
    restPersoon.setAdres(adres);

    restPersoon.setGeboortedatum(persoon.getDatumGeboorte().toInt());
    restPersoon.setGeboorteplaats(toTableRecord(persoon.getGeboorteplaats()));
    restPersoon.setGeboorteplaatsOpAkte(persoon.getGeboorteplaatsAkte());
    restPersoon.setGeboorteland(toTableRecord(persoon.getGeboorteland()));
    restPersoon.setGeboortelandOpAkte(persoon.getGeboortelandAkte());
    restPersoon.setImmigratieDatum(persoon.getDatumImmigratie().toInt());
    restPersoon.setImmigratieLand(toTableRecord(persoon.getImmigratieland()));
    restPersoon.setOverlijdensDatum(persoon.getDatumOverlijden().toInt());
    restPersoon.setBurgerlijkeStaat(toEnum(GbaRestBurgerlijkeStaatType.values(),
        persoon.getBurgerlijkeStaat().getBs()));
    restPersoon.setBurgerlijkeStaatDatum(persoon.getDatumBurgerlijkeStaat().getIntDate());
    restPersoon.setVerstrekkingsbeperking(persoon.isVerstrekkingsbeperking());
    restPersoon.setVerblijfstitel(toTableRecord(persoon.getVerblijfstitel()));
    restPersoon.setToelichting(persoon.getToelichting());
    restPersoon.setNationaliteiten(toGbaRestPersoonNatios(persoon));
    return restPersoon;
  }
}
