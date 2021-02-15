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

package nl.procura.gba.web.services.zaken.algemeen.aantekening;

import static nl.procura.standard.Globalfunctions.along;
import static nl.procura.standard.Globalfunctions.fil;

import nl.procura.gba.common.MiscUtils;
import nl.procura.gba.jpa.personen.db.AantekeningInd;
import nl.procura.gba.jpa.personen.db.Profile;
import nl.procura.gba.web.services.beheer.KoppelActie;
import nl.procura.gba.web.services.beheer.profiel.KoppelbaarAanProfiel;
import nl.procura.gba.web.services.beheer.profiel.Profiel;
import nl.procura.java.reflection.ReflectionUtil;

public class PlAantekeningIndicatie extends AantekeningInd implements KoppelbaarAanProfiel {

  public static final long  VRIJE_AANTEKENING   = 1000;
  private static final long serialVersionUID    = 5137360767613081609L;
  private boolean           kladblokAantekening = false;

  public PlAantekeningIndicatie() {
    setIndicatie("");
    setProbev("");
    setButton("");
    setOmschrijving("");
  }

  public PlAantekeningIndicatie(boolean kladblokAantekening) {
    this();
    setKladblokAantekening(kladblokAantekening);
    setOmschrijving("Kladblokaantekening (PROBEV)");
  }

  public PlAantekeningIndicatie(long cAaantekeningInd) {
    this();
    setCAantekeningInd(cAaantekeningInd);
  }

  public String getIndicatie() {
    return getAantekeningInd();
  }

  public void setIndicatie(String indicatie) {
    setAantekeningInd(indicatie);
  }

  public String getOmschrijving() {
    return getOms();
  }

  public void setOmschrijving(String omschrijving) {
    setOms(omschrijving);
  }

  public boolean isAantekening() {
    return isKladblokAantekening() || isVrijeAantekening();
  }

  @Override
  public boolean isGekoppeld(Profiel profiel) {
    return MiscUtils.contains(profiel, getProfiles());
  }

  public boolean isKladblokAantekening() {
    return kladblokAantekening;
  }

  public void setKladblokAantekening(boolean kladblokAantekening) {
    this.kladblokAantekening = kladblokAantekening;
  }

  public boolean isVrijeAantekening() {
    return VRIJE_AANTEKENING == along(getCAantekeningInd());
  }

  @Override
  public void koppelActie(Profiel profiel, KoppelActie koppelActie) {
    if (KoppelActie.KOPPEL == koppelActie) {
      getProfiles().add(ReflectionUtil.deepCopyBean(Profile.class, profiel));
    } else {
      getProfiles().remove(profiel);
    }
  }

  public String toString() {
    return getOmschrijving() + (fil(getProbev()) ? (" (" + getProbev() + ")") : "");
  }
}
