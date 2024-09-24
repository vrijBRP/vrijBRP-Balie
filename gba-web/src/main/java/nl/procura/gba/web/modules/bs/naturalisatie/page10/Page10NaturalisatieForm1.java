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

package nl.procura.gba.web.modules.bs.naturalisatie.page10;

import static nl.procura.gba.common.MiscUtils.getLeeftijd;
import static nl.procura.gba.web.modules.bs.naturalisatie.page10.Page10NaturalisatieBean.F_CURATELE;
import static nl.procura.gba.web.modules.bs.naturalisatie.page10.Page10NaturalisatieBean.F_EERSTE_VESTIGING_NL;
import static nl.procura.gba.web.modules.bs.naturalisatie.page10.Page10NaturalisatieBean.F_GEBOREN;
import static nl.procura.gba.web.modules.bs.naturalisatie.page10.Page10NaturalisatieBean.F_LAATSTE_VESTIGING_NL;
import static nl.procura.gba.web.modules.bs.naturalisatie.page10.Page10NaturalisatieBean.F_NATIONALITEIT;
import static nl.procura.gba.web.modules.bs.naturalisatie.page10.Page10NaturalisatieBean.F_PARTNER;
import static nl.procura.gba.web.modules.bs.naturalisatie.page10.Page10NaturalisatieBean.F_VERBLIJFSTITEL;
import static nl.procura.standard.Globalfunctions.date2str;
import static org.apache.commons.lang3.StringUtils.defaultIfBlank;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.base.BasePLValue;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.web.components.layouts.form.GbaForm;

import lombok.Data;

public class Page10NaturalisatieForm1 extends GbaForm<Page10NaturalisatieBean> {

  public Page10NaturalisatieForm1() {
    setCaption("Extra gegevens");
    setReadThrough(true);
    setColumnWidths("200px", "");
    setOrder(F_VERBLIJFSTITEL, F_NATIONALITEIT, F_GEBOREN, F_EERSTE_VESTIGING_NL,
        F_LAATSTE_VESTIGING_NL, F_CURATELE, F_PARTNER);
    setBean(new Page10NaturalisatieBean());
  }

  @Override
  public Page10NaturalisatieBean getNewBean() {
    return new Page10NaturalisatieBean();
  }

  public void updateExtraAangeverInfo(BasePLExt pl, String naamPartner) {
    Page10NaturalisatieBean bean = getNewBean();
    if (pl != null) {
      BasePLValue vbt = pl.getVerblijfstitel().getVerblijfstitel();
      bean.setVerblijfstitel(isBlank(vbt.getVal()) ? "N.v.t." : vbt.getVal() + ": " + vbt.getDescr());
      bean.setNationaliteit(pl.getNatio().getNationaliteiten());
      bean.setGeboren(pl.getPersoon().getGeboorte().getDatumLeeftijdPlaatsLand());
      bean.setEersteVestigingNL(new InNL(pl, true).toString());
      bean.setLaatsteVestigingNL(new InNL(pl, false).toString());
      bean.setCuratele(pl.getPersoon().isStaatOnderCuratele() ? "Ja" : "Nee");
      bean.setPartner(naamPartner);
    }
    setBean(bean);
  }

  @Data
  private static class InNL {

    private BasePLExt pl;
    private String    dImmigratie;
    private String    dGeb;

    public InNL(BasePLExt pl, boolean reverse) {
      this.dImmigratie = getRecs(pl, reverse);
      this.dGeb = pl.getPersoon().getGeboorte().getGeboortedatum().getVal();
    }

    private static String getRecs(BasePLExt pl, boolean reverse) {
      List<BasePLRec> recs = new ArrayList<>(pl.getCat(GBACat.VB).getSets().get(0).getRecs());
      if (reverse) {
        Collections.reverse(recs);
      }
      return recs.stream()
          .filter(r -> isNotBlank(r.getElemVal(GBAElem.DATUM_VESTIGING_IN_NL).getVal()))
          .findFirst()
          .orElse(new BasePLRec())
          .getElemVal(GBAElem.DATUM_VESTIGING_IN_NL).getVal();
    }

    private String getDate() {
      return defaultIfBlank(dImmigratie, dGeb);
    }

    @Override
    public String toString() {
      if (isNotBlank(dImmigratie)) {
        return String.format("%s (immigratiedatum %d jaar geleden)", date2str(getDate()), getLeeftijd(getDate(), ""));

      } else if (isNotBlank(dGeb)) {
        return String.format("%s (geboortedatum)", date2str(dGeb));
      } else {
        return "";
      }
    }
  }
}
