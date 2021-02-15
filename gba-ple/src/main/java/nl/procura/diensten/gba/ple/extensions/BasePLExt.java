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

package nl.procura.diensten.gba.ple.extensions;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.diensten.gba.ple.base.BasePL;
import nl.procura.diensten.gba.ple.base.BasePLList;
import nl.procura.diensten.gba.ple.base.BasePLValue;

public class BasePLExt extends BasePL {

  private static final long serialVersionUID = 4404316245575207170L;

  private final BasePLList<BasePLCatExt> exts          = new BasePLList<>();
  private boolean                        toonbeperking = false;

  public BasePLExt() {
    exts.add(new Cat1PersoonExt(this));
    exts.add(new Cat2OudersExt(this));
    exts.add(new Cat4NatioExt(this));
    exts.add(new Cat5HuwExt(this));
    exts.add(new Cat6OverlExt(this));
    exts.add(new Cat8VbExt(this));
    exts.add(new Cat9KinderenExt(this));
    exts.add(new Cat10VbtExt(this));
    exts.add(new Cat11GezagExt(this));
    exts.add(new Cat12ReisdExt(this));
    exts.add(new Cat21VerwExt(this));
  }

  public BasePLExt(BasePL pl) {
    this();
    setCats(pl.getCats());
    setMetaInfo(pl.getMetaInfo());
  }

  public Cat1PersoonExt getPersoon() {
    return get(Cat1PersoonExt.class);
  }

  public Cat2OudersExt getOuders() {
    return get(Cat2OudersExt.class);
  }

  public Cat5HuwExt getHuwelijk() {
    return get(Cat5HuwExt.class);
  }

  public Cat8VbExt getVerblijfplaats() {
    return get(Cat8VbExt.class);
  }

  public Cat21VerwExt getVerwijzing() {
    return get(Cat21VerwExt.class);
  }

  public Cat4NatioExt getNatio() {
    return get(Cat4NatioExt.class);
  }

  public Cat10VbtExt getVerblijfstitel() {
    return get(Cat10VbtExt.class);
  }

  public Cat12ReisdExt getReisdoc() {
    return get(Cat12ReisdExt.class);
  }

  public Cat11GezagExt getGezag() {
    return get(Cat11GezagExt.class);
  }

  public Cat9KinderenExt getKinderen() {
    return get(Cat9KinderenExt.class);
  }

  public Cat6OverlExt getOverlijding() {
    return get(Cat6OverlExt.class);
  }

  public boolean heeftVerwijzing() {
    return getCat(GBACat.VERW).hasSets();
  }

  public boolean is(BasePLExt pl) {
    if (pl != null && pl.getPersoon() != null) {
      BasePLValue anr = pl.getPersoon().getAnr();
      BasePLValue bsn = pl.getPersoon().getBsn();
      return getPersoon().isNr(anr.getCode(), bsn.getCode());
    }
    return false;
  }

  public boolean isToonBeperking() {
    return toonbeperking;
  }

  public void setToonbeperking(boolean toonbeperking) {
    this.toonbeperking = toonbeperking;
  }

  @SuppressWarnings("unchecked")
  private <T extends BasePLCatExt> T get(Class<T> cl) {
    return (T) exts.stream().filter(db -> db.getClass().isAssignableFrom(cl)).findFirst().orElse(null);

  }
}
