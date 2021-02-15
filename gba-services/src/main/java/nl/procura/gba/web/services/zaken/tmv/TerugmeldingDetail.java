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

package nl.procura.gba.web.services.zaken.tmv;

import static nl.procura.standard.Globalfunctions.aval;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.burgerzaken.gba.core.enums.GBAGroupElements;
import nl.procura.gba.jpa.personen.db.TerugmDetail;
import nl.procura.gba.jpa.personen.db.TerugmDetailPK;
import nl.procura.gba.web.common.misc.GBAElem2FieldConverter;

public class TerugmeldingDetail extends TerugmDetail {

  private static final long serialVersionUID = -284614943822297023L;

  public GBACat getCat() {
    return GBACat.getByCode(aval(getCCat()));
  }

  public long getCCat() {
    return aval(getId().getCCat());
  }

  public void setCCat(long cCat) {

    if (getId() == null) {
      setId(new TerugmDetailPK());
    }

    getId().setCCat(cCat);
  }

  public long getCElem() {
    return aval(getId().getCElem());
  }

  public void setCElem(long cElem) {

    if (getId() == null) {
      setId(new TerugmDetailPK());
    }

    getId().setCElem(cElem);
  }

  public String getFormatNieuw() {

    GBAElem2FieldConverter c = new GBAElem2FieldConverter();
    return c.getFormatValue(aval(getCCat()), aval(getCElem()), getValNieuw());
  }

  public String getFormatOrigineel() {

    GBAElem2FieldConverter c = new GBAElem2FieldConverter();
    return c.getFormatValue(aval(getCCat()), aval(getCElem()), getValOrigineel());
  }

  public GBAGroupElements.GBAGroupElem getPleType() {
    return GBAGroupElements.getByCat(aval(getCCat()), aval(getCElem()));
  }

  public long getVolgnr() {
    return getId().getVolgnr();
  }

  public void setVolgnr(long volgnr) {

    if (getId() == null) {
      setId(new TerugmDetailPK());
    }

    getId().setVolgnr(volgnr);
  }
}
