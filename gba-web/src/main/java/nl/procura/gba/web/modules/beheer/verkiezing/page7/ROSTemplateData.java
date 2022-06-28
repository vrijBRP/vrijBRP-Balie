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

package nl.procura.gba.web.modules.beheer.verkiezing.page7;

import static nl.procura.gba.web.services.beheer.verkiezing.StempasAanduidingType.*;
import static nl.procura.standard.Globalfunctions.date2str;

import java.util.stream.Collectors;

import nl.procura.gba.jpa.personen.db.KiesrStem;
import nl.procura.gba.jpa.personen.db.KiesrVerk;
import nl.procura.gba.web.services.beheer.verkiezing.Stempas;
import nl.procura.gba.web.services.beheer.verkiezing.StempasResult;
import nl.procura.gba.web.services.beheer.verkiezing.Verkiezing;
import nl.procura.gba.web.services.zaken.documenten.printen.DocumentTemplateData;
import nl.procura.standard.ProcuraDate;

public class ROSTemplateData extends DocumentTemplateData {

  public ROSTemplateData(KiesrVerk kiesrVerk, StempasResult result) {
    Verkiezing verkiezing = new Verkiezing(kiesrVerk);
    put("verkiezing", new VerkiezingTemplateData(verkiezing));
    put("gemeente", new GemeenteTemplateData(verkiezing));
    put("stempassen", result.getStempassen().stream()
        .map(this::toStempasTemplate)
        .collect(Collectors.toList()));
  }

  private StempasTemplateData toStempasTemplate(KiesrStem stempas) {
    return new StempasTemplateData(new Stempas(stempas));
  }

  private static class StempasTemplateData extends DocumentTemplateData {

    public StempasTemplateData(Stempas stempas) {
      put("pasnr", stempas.getPasnummer());
      put("vnr", stempas.getVolgnr());
      put("aanduiding", new AanduidingTemplateData(stempas));
      put("naam", stempas.getStem().getNaam() + ", " + stempas.getStem().getVoorn());
      put("geslacht", stempas.getStem().getGeslacht());
      put("geboortedatum", date2str(stempas.getStem().getdGeb()));
    }
  }

  private static class VerkiezingTemplateData extends DocumentTemplateData {

    public VerkiezingTemplateData(Verkiezing verkiezing) {
      put("afk", verkiezing.getVerk().getAfkVerkiezing());
      put("omschrijving", verkiezing.getVerk().getVerkiezing());
      put("datum", new ProcuraDate(verkiezing.getVerk().getdVerk()));
    }
  }

  private static class GemeenteTemplateData extends DocumentTemplateData {

    public GemeenteTemplateData(Verkiezing verkiezing) {
      put("code", String.format("%04d", verkiezing.getVerk().getCodeGemeente()));
      put("omschrijving", verkiezing.getVerk().getGemeente());
    }
  }

  private static class AanduidingTemplateData extends DocumentTemplateData {

    public AanduidingTemplateData(Stempas stempas) {
      put("code", stempas.getAanduidingType().getCode());
      put("type", stempas.getAanduidingType().getType());
      put("omschrijving", stempas.getAanduidingType().getOms());
      put("volmachtbewijs", AAND_VOLMACHTBEWIJS.equals(stempas.getAanduidingType()));
      put("toegevoegd", stempas.isToegevoegd());
      put("kiezerspas", AAND_KIEZERSPAS.equals(stempas.getAanduidingType()));
      put("briefstembewijs", AAND_BRIEFSTEMBEWIJS.equals(stempas.getAanduidingType()));
      put("vervanging", AAND_VERVANGEN.equals(stempas.getAanduidingType()));
      put("vervanging_vnr", stempas.getVnrVervanging());
      put("verlies_diefstal", AAND_INTREKKEN_VERLIES.equals(stempas.getAanduidingType()));
      put("overlijden", AAND_ONGELDIG_OVERL.equals(stempas.getAanduidingType()));
      put("niet_kiesgerechtigd", AAND_ONGELDIG_KIESG.equals(stempas.getAanduidingType()));
      put("overig", AAND_INTREKKEN_OVERIG.equals(stempas.getAanduidingType()));
    }
  }
}
