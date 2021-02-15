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

package nl.procura.gba.web.common.misc.licentie;

import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.emp;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nl.procura.gba.config.GbaConfig;
import nl.procura.gba.web.common.misc.java.DesEncrypter;
import nl.procura.java.collection.Collections;
import nl.procura.standard.ProcuraDate;

public class Licentie {

  private final DesEncrypter encryptor      = new DesEncrypter("39dsjdnd2e8rde382y4hdds82");
  private final String       licensekeyFile = "applicatie.lk";

  private ProcuraDate  datumEinde = new ProcuraDate();
  private List<String> modules    = new ArrayList<>();

  private String codeGemeente = "";
  private String gemeente     = "";
  private String applicatie   = "";
  private String licentie     = "";

  public Licentie() {

    File licenceFile = getLicentieBestand();

    if (licenceFile.exists()) {

      setLicentie(astr(encryptor.deserializeFromFile(licenceFile.getAbsolutePath())));

      String[] parts = encryptor.decrypt(getLicentie()).split("\\|");

      if (parts.length > 4) {

        setCodeGemeente(parts[0]);
        setGemeente(parts[1]);
        setApplicatie(parts[2]);
        setDatumEinde(new ProcuraDate(parts[3]));
      }

      if (parts.length > 5) {
        getModules().addAll(Collections.list(Arrays.copyOfRange(parts, 4, parts.length - 1)));
      }
    }
  }

  public String getApplicatie() {
    return applicatie;
  }

  public void setApplicatie(String applicatie) {
    this.applicatie = applicatie;
  }

  public String getCodeGemeente() {
    return codeGemeente;
  }

  public void setCodeGemeente(String codeGemeente) {
    this.codeGemeente = codeGemeente;
  }

  public ProcuraDate getDatumEinde() {
    return datumEinde;
  }

  public void setDatumEinde(ProcuraDate datumEinde) {
    this.datumEinde = datumEinde;
  }

  public String getGemeente() {
    return gemeente;
  }

  public void setGemeente(String gemeente) {
    this.gemeente = gemeente;
  }

  public String getLicentie() {
    return licentie;
  }

  public void setLicentie(String licentie) {
    this.licentie = licentie;
  }

  public List<String> getModules() {
    return modules;
  }

  public void setModules(List<String> modules) {
    this.modules = modules;
  }

  public boolean isCorrect(String s) {
    return encryptor.decrypt(s) != null;
  }

  public boolean isModule(LicentieModule t) {
    return getModules().contains(t.getType());
  }

  public void save() {

    if (emp(getLicentie())) {
      getLicentieBestand().delete();
    } else {
      encryptor.serializeToFile(getLicentie(), getLicentieBestand().getAbsolutePath());
    }
  }

  private File getLicentieBestand() {

    return new File(GbaConfig.getPath().getConfigDir(), licensekeyFile);
  }
}
