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

package nl.procura.gba.web.services.beheer.verkiezing.document;

import static nl.procura.gba.web.services.beheer.verkiezing.StempasAanduidingType.*;

import java.util.List;

import nl.procura.diensten.gba.ple.openoffice.DocumentPL;
import nl.procura.gba.jpa.personen.db.KiesrVerkInfo;
import nl.procura.gba.web.services.beheer.verkiezing.Stempas;
import nl.procura.gba.web.services.zaken.documenten.printen.DocumentTemplateData;
import nl.procura.standard.ProcuraDate;

public class StempasTemplateData extends DocumentTemplateData {

  public StempasTemplateData(Stempas stempas) {
    put("verkiezing", new VerkiezingTemplateData(stempas));
    put("gemeente", new GemeenteTemplateData(stempas));

    put("pasnr", stempas.getPasnummer());
    put("vnr", stempas.getVolgnr());
    put("aanduiding", new AanduidingTemplateData(stempas));
    put("tekstblokken", new TekstblokTemplateData(stempas));

    // Personen
    put("stemgerechtigde", new DocumentPL(stempas.getStemgerechtigde()));
    if (stempas.getAnrGemachtigde().isCorrect()) {
      put("gemachtigde", new DocumentPL(stempas.getGemachtigde()));
    }
  }

  private static class TekstblokTemplateData extends DocumentTemplateData {

    public TekstblokTemplateData(Stempas stempas) {
      List<KiesrVerkInfo> infos = stempas.getVerkiezing().getVerk().getInfos();
      for (KiesrVerkInfo info : infos) {
        put(info.getNaam(), info.getInhoud());
      }
    }
  }

  private static class VerkiezingTemplateData extends DocumentTemplateData {

    public VerkiezingTemplateData(Stempas stempas) {
      put("afk", stempas.getVerkiezing().getVerk().getAfkVerkiezing());
      put("omschrijving", stempas.getVerkiezing().getVerk().getVerkiezing());
      put("datum", new ProcuraDate(stempas.getVerkiezing().getVerk().getdVerk()));
    }
  }

  private static class GemeenteTemplateData extends DocumentTemplateData {

    public GemeenteTemplateData(Stempas stempas) {
      put("code", String.format("%04d", stempas.getVerkiezing().getVerk().getCodeGemeente()));
      put("omschrijving", stempas.getVerkiezing().getVerk().getGemeente());
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
