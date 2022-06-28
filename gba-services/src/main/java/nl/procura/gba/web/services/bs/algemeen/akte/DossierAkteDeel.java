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

package nl.procura.gba.web.services.bs.algemeen.akte;

import static nl.procura.standard.Globalfunctions.*;

import nl.procura.gba.jpa.personen.db.DossAkteRd;
import nl.procura.gba.jpa.personen.db.DossAkteRdCat;
import nl.procura.java.reflection.ReflectionUtil;
import nl.procura.standard.ProcuraDate;

public class DossierAkteDeel extends DossAkteRd {

  private DossierAkteCategorie categorie = null;

  public DossierAkteDeel() {
    setRegisterdeel("");
    setOms("");
    setMin(toBigDecimal(1));
    setMax(toBigDecimal(9999));
    setdEnd(null);
  }

  public DossierAkteDeel(DossierAkteCategorie categorie) {
    this();
    setCategorie(categorie);
  }

  public DossierAkteCategorie getCategorie() {
    if (this.categorie == null) {
      this.categorie = ReflectionUtil.deepCopyBean(DossierAkteCategorie.class, getDossAkteRdCat());
    }
    return categorie;
  }

  public void setCategorie(DossierAkteCategorie categorie) {
    this.categorie = categorie;
    setDossAkteRdCat(ReflectionUtil.deepCopyBean(DossAkteRdCat.class, categorie));
  }

  public String getMaxFormat() {
    return pad_left(astr(getMax()), "0", 4);
  }

  public String getMinFormat() {
    return pad_left(astr(getMin()), "0", 4);
  }

  public String getOmschrijving() {
    return getOms();
  }

  public void setOmschrijving(String omschrijving) {
    setOms(omschrijving);
  }

  public DossierAkteRegistersoort getRegisterSoort() {
    return DossierAkteRegistersoort.get(getRegistersoort());
  }

  public void setRegisterSoort(DossierAkteRegistersoort soort) {
    setRegistersoort(toBigDecimal(soort.getCode()));
  }

  public boolean isNotExpired() {
    return !isExpired();
  }

  public boolean isExpired() {
    return getdEnd() != null && new ProcuraDate(getdEnd()).isExpired();
  }

  @Override
  public String toString() {
    return new StringBuilder()
        .append(getOmschrijving())
        .append(" (")
        .append(getDossAkteRdCat().getDossAkteRdCat())
        .append(isExpired() ? " / beëindigd" : "")
        .append(")")
        .toString();
  }
}
