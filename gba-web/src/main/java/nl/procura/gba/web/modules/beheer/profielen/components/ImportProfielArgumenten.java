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

package nl.procura.gba.web.modules.beheer.profielen.components;

import java.io.File;

import nl.procura.gba.web.windows.GbaWindow;

public class ImportProfielArgumenten {

  private boolean   importGebruikers  = true;
  private boolean   importParameters  = true;
  private boolean   importActies      = true;
  private boolean   importVelden      = true;
  private boolean   importElementen   = true;
  private boolean   importCategorieen = true;
  private GbaWindow window            = null;
  private File      bestand           = null;

  public File getBestand() {
    return bestand;
  }

  public void setBestand(File bestand) {
    this.bestand = bestand;
  }

  public GbaWindow getWindow() {
    return window;
  }

  public void setWindow(GbaWindow window) {
    this.window = window;
  }

  public boolean isImportActies() {
    return importActies;
  }

  public void setImportActies(boolean importActies) {
    this.importActies = importActies;
  }

  public boolean isImportCategorieen() {
    return importCategorieen;
  }

  public void setImportCategorieen(boolean importCategorieen) {
    this.importCategorieen = importCategorieen;
  }

  public boolean isImportElementen() {
    return importElementen;
  }

  public void setImportElementen(boolean importElementen) {
    this.importElementen = importElementen;
  }

  public boolean isImportGebruikers() {
    return importGebruikers;
  }

  public void setImportGebruikers(boolean importGebruikers) {
    this.importGebruikers = importGebruikers;
  }

  public boolean isImportParameters() {
    return importParameters;
  }

  public void setImportParameters(boolean importParameters) {
    this.importParameters = importParameters;
  }

  public boolean isImportVelden() {
    return importVelden;
  }

  public void setImportVelden(boolean importVelden) {
    this.importVelden = importVelden;
  }
}
