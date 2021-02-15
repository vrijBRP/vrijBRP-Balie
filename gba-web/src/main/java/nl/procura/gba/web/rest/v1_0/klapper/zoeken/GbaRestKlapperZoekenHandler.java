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

package nl.procura.gba.web.rest.v1_0.klapper.zoeken;

import static java.util.Arrays.asList;
import static nl.procura.standard.Globalfunctions.pos;

import java.util.List;

import nl.procura.gba.common.ZaakStatusType;
import nl.procura.gba.common.ZaakType;
import nl.procura.gba.web.rest.v1_0.GbaRestHandler;
import nl.procura.gba.web.rest.v1_0.algemeen.GbaRestElement;
import nl.procura.gba.web.rest.v1_0.klapper.GbaRestKlapperVraag;
import nl.procura.gba.web.rest.v1_0.zaak.zoeken.bs.GbaRestDossierAkteHandler;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.bs.algemeen.akte.AkteService;
import nl.procura.gba.web.services.bs.algemeen.akte.DossierAkte;
import nl.procura.gba.web.services.bs.algemeen.akte.KlapperZoekargumenten;
import nl.procura.gba.web.services.zaken.algemeen.ZaakArgumenten;

public class GbaRestKlapperZoekenHandler extends GbaRestHandler {

  public GbaRestKlapperZoekenHandler(Services services) {
    super(services);
  }

  /**
   * Geeft alle ZaakArgumenten terug
   */
  public ZaakArgumenten getAlleZaakArgumenten(ZaakArgumenten za) {
    za.getTypen().addAll(ZaakType.getAll());
    za.getStatussen().addAll(ZaakStatusType.getAll());
    return za;
  }

  /**
   * Zoek klappers in Proweb Personen
   */
  public List<DossierAkte> getKlappers(KlapperZoekargumenten za) {

    AkteService zdb = getServices().getAkteService();

    return zdb.getAktes(za);
  }

  /**
   * Geeft klappers terug als REST objecten
   */
  public List<GbaRestElement> getRestKlappers(GbaRestKlapperVraag vraag) {
    List<DossierAkte> klappers = getKlappers(getZoekArgumenten(vraag));
    return asList(new GbaRestDossierAkteHandler(getServices()).getAktes(klappers));
  }

  /**
   * Vertaal de REST vraag naar ZaakArgumentne
   */
  public KlapperZoekargumenten getZoekArgumenten(GbaRestKlapperVraag vraag) {

    KlapperZoekargumenten za = new KlapperZoekargumenten();

    if (pos(vraag.getDatumInvoerVanaf())) {
      za.setDatumVanaf(vraag.getDatumInvoerVanaf());
    }

    if (pos(vraag.getDatumInvoerTm())) {
      za.setDatumTm(vraag.getDatumInvoerTm());
    }

    return za;
  }
}
