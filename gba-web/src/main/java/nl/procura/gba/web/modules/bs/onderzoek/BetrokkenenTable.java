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

package nl.procura.gba.web.modules.bs.onderzoek;

import static nl.procura.gba.common.MiscUtils.setClass;
import static nl.procura.standard.Globalfunctions.*;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.procura.arguments.PLEArgs;
import nl.procura.diensten.gba.ple.procura.arguments.PLEDatasource;
import nl.procura.gba.web.components.layouts.table.GbaTable;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.bs.algemeen.persoon.DossierPersoon;
import nl.procura.gba.web.services.bs.onderzoek.DossierOnderzoek;
import nl.procura.gba.web.services.bs.onderzoek.enums.AanduidingOnderzoekType;

public class BetrokkenenTable extends GbaTable {

  /**
   * Geeft de aanduiding van de categorie op de persoonslijst terug
   */
  protected String getAanduidingStatus(DossierPersoon dossierPersoon, List<BasePLExt> persoonslijsten) {
    return persoonslijsten.stream()
        .filter(pl -> pl.getPersoon().isNr(dossierPersoon.getBurgerServiceNummer().getStringValue()))
        .map(this::getAanduiding)
        .findFirst()
        .orElse("");
  }

  /**
   * Geeft de status van de persoonslijst terug
   */
  protected String getPersoonslijstStatus(DossierPersoon dossierPersoon, List<BasePLExt> persoonslijsten) {
    return persoonslijsten.stream()
        .filter(pl -> pl.getPersoon().isNr(dossierPersoon.getBurgerServiceNummer().getStringValue()))
        .map(pl -> setClass(false, pl.getPersoon().getStatus().getOpsomming()))
        .findFirst()
        .orElse("");
  }

  private String getAanduiding(BasePLExt pl) {
    String out = "Niet in onderzoek / geen deelresultaat";
    BasePLRec record = pl.getCat(GBACat.VB).getLatestRec();
    if (record.hasElems()) {

      String aand = record.getElemVal(GBAElem.AAND_GEG_IN_ONDERZ).getVal();
      String aandIn = record.getElemVal(GBAElem.DATUM_INGANG_ONDERZ).getVal();
      String aandEnd = record.getElemVal(GBAElem.DATUM_EINDE_ONDERZ).getVal();

      if (StringUtils.isNotBlank(aand) && emp(aandEnd)) {
        AanduidingOnderzoekType type = AanduidingOnderzoekType.get(aand);
        out = MessageFormat.format("{0}{1} per {2}",
            type.isDeelresultaat() ? "Deelresultaat " : "Onderzoek ",
            type.getOms(), date2str(aandIn));
      }

      if (StringUtils.isNotBlank(aand) && pos(aandEnd)) {
        AanduidingOnderzoekType type = AanduidingOnderzoekType.get(aand);
        out = MessageFormat.format("{0}{1} beëindigd per {2}",
            type.isDeelresultaat() ? "Deelresultaat " : "Onderzoek ",
            type.getOms(), date2str(aandIn));
      }
    }

    return out;
  }

  protected List<BasePLExt> getAanduidingen(DossierOnderzoek zaakDossier) {
    // Search
    List<BasePLExt> out = new ArrayList<>();
    PLEArgs args = new PLEArgs();
    args.setShowArchives(false);
    args.setShowRemoved(false);
    args.setDatasource(PLEDatasource.PROCURA);

    List<DossierPersoon> personen = zaakDossier.getBetrokkenen();
    Services services = getApplication().getServices();

    if (!personen.isEmpty()) {
      for (DossierPersoon dossierPersoon : personen) {
        args.addNummer(dossierPersoon.getBurgerServiceNummer().getStringValue());
      }
      out.addAll(services.getPersonenWsService().getPersoonslijsten(args, false).getBasisPLWrappers());
    }

    zaakDossier.setAantalOnderzoek(services.getOnderzoekService().getAantalInhoudingen(zaakDossier));
    return out;
  }
}
