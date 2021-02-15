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

package nl.procura.gba.web.modules.bs.common.utils;

import static nl.procura.burgerzaken.gba.core.enums.GBACat.HUW_GPS;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.*;
import static nl.procura.standard.Globalfunctions.*;

import java.util.ArrayList;
import java.util.List;

import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.base.BasePLSet;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.web.components.containers.Container;
import nl.procura.gba.web.services.Services;
import nl.procura.gba.web.services.bs.algemeen.enums.SoortVerbintenis;
import nl.procura.gba.web.services.bs.algemeen.functies.BsUtils;
import nl.procura.gba.web.services.bs.geboorte.GezinssituatieType;
import nl.procura.standard.ProcuraDate;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.TabelFieldValue;

public class BsHuwelijkUtils extends BsUtils {

  private static final int MAXIMALE_TERMIJN_GEBOORTE = 306; // Dagen

  public static GezinssituatieType getGezinssituatie(Services services, long datumGeboorte, BsnFieldValue bsnMoeder) {

    if (bsnMoeder.isCorrect()) {

      BasePLExt moeder = getPl(services, bsnMoeder);

      for (Partnerschap partnerschap : getPartnerschappen(moeder)) {

        boolean isActueel = partnerschap.isBinnenPartnerschap(datumGeboorte);
        int dagen = partnerschap.getAantalDagenNaDatumOntbinding(datumGeboorte);

        if (isActueel || (dagen > 0 && dagen <= MAXIMALE_TERMIJN_GEBOORTE && partnerschap.isOverlijden())) {
          return GezinssituatieType.BINNEN_HETERO_HUWELIJK;
        }
      }

      return GezinssituatieType.BUITEN_HUWELIJK;
    }

    return GezinssituatieType.ONBEKEND;
  }

  private static List<Partnerschap> getPartnerschappen(BasePLExt moeder) {

    long bsn = along(moeder.getPersoon().getBsn().getCode());

    List<Partnerschap> huwelijken = new ArrayList<>();

    for (BasePLSet set : moeder.getCat(HUW_GPS).getSets()) {

      BasePLRec sluit = moeder.getHuwelijk().getSluiting(set, "");
      BasePLRec ontb = moeder.getHuwelijk().getOntbinding(set, "");

      int dHuw = aval(sluit.getElemVal(DATUM_VERBINTENIS).getVal());
      int dOntb = aval(ontb.getElemVal(DATUM_ONTBINDING).getVal());
      String rOntb = ontb.getElemVal(REDEN_ONTBINDING).getVal();

      Partnerschap huw = new Partnerschap();

      huw.setBsn(bsn);
      huw.setBsnPartner(aval(sluit.getElemVal(BSN).getVal()));
      huw.setSoortVerbintenis(SoortVerbintenis.get(sluit.getElemVal(SOORT_VERBINTENIS).getVal()));
      huw.setRedenOntbinding(Container.REDEN_HUW_ONTB.get(rOntb));
      huw.setGeslachtPartner(sluit.getElemVal(GESLACHTSAAND).getVal());

      if (aval(dHuw) >= 0) { // datum huwelijk is gezet
        huw.setDatumHuwelijk(dHuw);

        if (huw.getDatumHuwelijk() >= huw.getDatumOntbinding()) {
          huw.setDatumOntbinding(-1); // Datum ontbinding niet van toepassing
        }
      }

      if (aval(dOntb) >= 0) { // datum ontbinding is gezet
        huw.setDatumOntbinding(dOntb);
      }

      if (aval(huw.getDatumHuwelijk()) >= 0) {
        huwelijken.add(huw);
      }

      break; // Alleen het eerste huwelijk van toepassing, dus break
    }

    return huwelijken;
  }

  protected static class Partnerschap {

    private static final String OVERLIJDEN = "O";
    private static final String GESCHEIDEN = "S";

    private long             bsn              = 0;
    private long             bsnPartner       = 0;
    private String           geslachtPartner  = "";
    private int              datumHuwelijk    = -1;
    private int              datumOntbinding  = -1;
    private SoortVerbintenis soortVerbintenis = SoortVerbintenis.ONBEKEND;
    private TabelFieldValue  redenOntbinding  = null;
    private boolean          omzetting        = false;

    public int getAantalDagenNaDatumOntbinding(long peilDatum) {
      return new ProcuraDate(astr(datumOntbinding)).diffInDays(astr(peilDatum));
    }

    public long getBsn() {
      return bsn;
    }

    public void setBsn(long bsn) {
      this.bsn = bsn;
    }

    public long getBsnPartner() {
      return bsnPartner;
    }

    public void setBsnPartner(long bsnPartner) {
      this.bsnPartner = bsnPartner;
    }

    public int getDatumHuwelijk() {
      return datumHuwelijk;
    }

    public void setDatumHuwelijk(int datumHuwelijk) {
      this.datumHuwelijk = datumHuwelijk;
    }

    public int getDatumOntbinding() {
      return datumOntbinding;
    }

    public void setDatumOntbinding(int datumOntbinding) {
      this.datumOntbinding = datumOntbinding;
    }

    public String getGeslachtPartner() {
      return geslachtPartner;
    }

    public void setGeslachtPartner(String geslachtPartner) {
      this.geslachtPartner = geslachtPartner;
    }

    public TabelFieldValue getRedenOntbinding() {
      return redenOntbinding;
    }

    public void setRedenOntbinding(TabelFieldValue redenOntbinding) {
      this.redenOntbinding = redenOntbinding;
    }

    public SoortVerbintenis getSoortVerbintenis() {
      return soortVerbintenis;
    }

    public void setSoortVerbintenis(SoortVerbintenis soortVerbintenis) {
      this.soortVerbintenis = soortVerbintenis;
      if (!isOmzetting() && this.soortVerbintenis != SoortVerbintenis.ONBEKEND
          && this.soortVerbintenis != soortVerbintenis) {
        setOmzetting(true);
      }
    }

    public boolean isBinnenPartnerschap(long peilDatum) {

      boolean binnenDePeriode = false;

      if (datumHuwelijk >= 0) { // Actueel huwelijk

        binnenDePeriode = true;

        if (datumOntbinding >= 0) { // Ontbonden

          if (datumOntbinding == 0) { // Datum ontbinding is onbekend.

            binnenDePeriode = false;
          } else {

            boolean isBegonnen = new ProcuraDate(astr(peilDatum)).isExpired(
                new ProcuraDate(astr(datumHuwelijk)));
            boolean isNogNietVerlopen = new ProcuraDate(astr(datumOntbinding)).isExpired(
                new ProcuraDate(astr(peilDatum)));

            binnenDePeriode = isBegonnen && isNogNietVerlopen;
          }
        }
      }

      return binnenDePeriode;
    }

    public boolean isOmzetting() {
      return omzetting;
    }

    public void setOmzetting(boolean omzetting) {
      this.omzetting = omzetting;
    }

    public boolean isOverlijden() {
      return getRedenOntbinding().getStringValue().equalsIgnoreCase(OVERLIJDEN);
    }

    public boolean isScheiding() {
      return getRedenOntbinding().getStringValue().equalsIgnoreCase(GESCHEIDEN);
    }

    @Override
    public String toString() {
      return "Partnerschap [bsn=" + bsn + ", bsnPartner=" + bsnPartner + ", geslachtPartner=" + geslachtPartner
          + ", datumHuwelijk=" + datumHuwelijk + ", datumOntbinding=" + datumOntbinding + ", soortVerbintenis="
          + soortVerbintenis + ", omzetting=" + omzetting + "]";
    }
  }
}
