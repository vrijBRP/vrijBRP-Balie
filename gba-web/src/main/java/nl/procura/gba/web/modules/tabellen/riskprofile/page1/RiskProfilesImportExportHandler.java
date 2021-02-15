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

package nl.procura.gba.web.modules.tabellen.riskprofile.page1;

import static java.util.stream.Collectors.toSet;
import static nl.procura.gba.web.modules.tabellen.riskprofile.page1.RiskProfileExport.Rp;
import static nl.procura.gba.web.modules.tabellen.riskprofile.page1.RiskProfileExport.RpRule;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import nl.procura.gba.common.Serializer;
import nl.procura.gba.jpa.personen.dao.RiskProfileDao;
import nl.procura.gba.jpa.personen.db.RiskProfile;
import nl.procura.gba.jpa.personen.db.RiskProfileRule;
import nl.procura.gba.web.common.misc.ImportExportHandler;
import nl.procura.gba.web.modules.tabellen.riskprofile.page1.RiskProfileExport.RpType;
import nl.procura.gba.web.services.bs.riskanalysis.RiskAnalysisService;
import nl.procura.gba.web.windows.GbaWindow;
import nl.procura.vaadin.functies.downloading.DownloadHandlerImpl;

import lombok.AllArgsConstructor;
import lombok.Data;

public class RiskProfilesImportExportHandler extends ImportExportHandler {

  private final RiskAnalysisService riskAnalysisService;

  public RiskProfilesImportExportHandler(RiskAnalysisService riskAnalysisService) {
    this.riskAnalysisService = riskAnalysisService;
  }

  /**
   * Imports riskprofiles from a zip-file
   */
  public List<String> importProfiles(final ImportRiskProfileArguments args) {

    final List<String> meldingen = new ArrayList<>();

    readZip(args.getFile(), (zis, entry, dir, name) -> {

      RiskProfileExport im = Serializer.deSerialize(zis, RiskProfileExport.class);

      for (Rp rp : im.getList()) {

        RiskProfile riskProfile = new RiskProfile();
        riskProfile.setName(getNewName(rp, 0));
        riskProfile.setThreshold(BigDecimal.valueOf(rp.getThreshold()));

        riskAnalysisService.save(riskProfile, rp.getTypes().stream()
            .map(RpType::toRiskProfileRelatedCaseType)
            .collect(toSet()));

        for (RpRule rpRule : rp.getRules()) {
          RiskProfileRule rule = new RiskProfileRule();
          rule.setName(rpRule.getName());
          rule.setAttr(rpRule.getAttr());
          rule.setRiskProfile(riskProfile);
          rule.setScore(BigDecimal.valueOf(rpRule.getScore()));
          rule.setVnr(BigDecimal.valueOf(rpRule.getVnr()));
          rule.setType(BigDecimal.valueOf(rpRule.getType()));

          riskAnalysisService.save(rule);
        }

        meldingen.add(String.format("Geïmporteerd risicoprofiel: %s", riskProfile.getName()));
      }
    });

    return meldingen;
  }

  private static String getNewName(Rp rp, int nr) {
    String name = nr > 0 ? MessageFormat.format("{0} ({1})", rp.getName(), nr) : rp.getName();
    List<RiskProfile> existing = RiskProfileDao.findByName(name);
    return existing.isEmpty() ? name : getNewName(rp, ++nr);
  }

  /**
   * Exports the profiles to a zip-bestand
   */
  public void exportProfiles(GbaWindow window, List<RiskProfile> profiles) {

    RiskProfileExport export = new RiskProfileExport();
    profiles.forEach(profile -> export.getList().add(exportProfile(profile)));

    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    Serializer.serialize(bos, export);
    Map<String, byte[]> map = new HashMap<>();
    map.put("inhoud.ser", bos.toByteArray());
    exportObject(map, "risicoprofielen.zip", new DownloadHandlerImpl(window));
  }

  private Rp exportProfile(RiskProfile riskProfile) {

    Rp rp = new Rp();
    rp.setName(riskProfile.getName());
    rp.setThreshold(riskProfile.getThreshold().longValue());

    rp.setTypes(riskAnalysisService.getTypes(riskProfile).stream()
        .map(RpType::of)
        .collect(Collectors.toList()));

    for (RiskProfileRule rule : riskProfile.getRules()) {
      RpRule rpRule = new RpRule();
      rpRule.setName(rule.getName());
      rpRule.setType(rule.getType().longValue());
      rpRule.setScore(rule.getScore().longValue());
      rpRule.setVnr(rule.getVnr().longValue());
      rpRule.setAttr(rule.getAttr());
      rp.getRules().add(rpRule);
    }

    return rp;
  }

  @Data
  @AllArgsConstructor
  public static class ImportRiskProfileArguments {

    private GbaWindow window = null;
    private File      file   = null;
  }
}
