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

package nl.procura.gba.web.modules.persoonslijst.overzicht.page1;

import static nl.procura.standard.Globalfunctions.pos;

import java.text.MessageFormat;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.base.BasePLSet;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.extensions.Cat1PersoonExt;
import nl.procura.gba.web.modules.persoonslijst.overig.PlPage;

public class Page1OverzichtPersoonslijst extends PlPage {

  public Page1OverzichtPersoonslijst() {

    super("Overzicht");

    addComponent(new Page1PersoonForm() {

      @Override
      public void setBean(Object bean) {

        BasePLExt pl = getPl();
        Cat1PersoonExt persoon = pl.getPersoon();
        Page1PersoonBean b = new Page1PersoonBean();

        b.setAnr(persoon.getAnr().getDescr());
        b.setBsn(persoon.getBsn().getDescr());
        b.setGeslachtsnaam(persoon.getNaam().getGeslachtsnaam().getValue().getDescr());
        b.setNationaliteit(pl.getNatio().getNationaliteit().getValue().getDescr());
        b.setVoorvoegsel(persoon.getNaam().getVoorvoegsel().getValue().getDescr());
        b.setGeboren(persoon.getGeboorte().getDatumLeeftijd());
        b.setTitel(persoon.getTitel().getValue().getDescr());
        b.setVoornaam(persoon.getNaam().getVoornamen().getValue().getDescr());

        if (!persoon.getNaam().getNaamgebruik().getValue().getCode().isEmpty()) {
          b.setNaamgebruik(persoon.getNaam().getNaamgebruik().getValue().getCode() + ": " +
              persoon.getNaam().getNaamgebruik().getValue().getDescr());
        }

        super.setBean(b);
      }
    });

    addComponent(new Page1PartnerForm() {

      @Override
      public void setBean(Object bean) {

        BasePLExt pl = getPl();

        Page1PartnerBean b = new Page1PartnerBean();

        if (pl.getHuwelijk().isActueelOfMutatieRecord()) {

          BasePLExt partnerPl = getPartner(pl);

          if (partnerPl != null) {

            Cat1PersoonExt persoon = partnerPl.getPersoon();

            b.setAnr(persoon.getAnr().getDescr());
            b.setBsn(persoon.getBsn().getDescr());
            b.setGeslachtsnaam(persoon.getNaam().getGeslachtsnaam().getValue().getDescr());
            b.setNationaliteit(partnerPl.getNatio().getNationaliteit().getValue().getDescr());
            b.setVoorvoegsel(persoon.getNaam().getVoorvoegsel().getValue().getDescr());
            b.setGeboren(persoon.getGeboorte().getDatumLeeftijd());
            b.setTitel(persoon.getTitel().getValue().getDescr());
            b.setVoornaam(persoon.getNaam().getVoornamen().getValue().getDescr());

            if (!persoon.getNaam().getNaamgebruik().getValue().getCode().isEmpty()) {
              b.setNaamgebruik(persoon.getNaam().getNaamgebruik().getValue().getCode() + ": " +
                  persoon.getNaam().getNaamgebruik().getValue().getDescr());
            }

            BasePLSet set = pl.getCat(GBACat.HUW_GPS)
                .getSets()
                .get(0);
            BasePLRec sluiting = pl.getHuwelijk().getSluiting(set, "");
            BasePLRec ontbinding = pl.getHuwelijk().getOntbinding(set, "");

            b.setSluiting(getSluiting(sluiting));
            b.setOntbinding(getOntbinding(ontbinding));
          }
        }

        super.setBean(b);
      }

      private BasePLExt getPartner(BasePLExt pl) {
        if (pl.getHuwelijk().isActueelOfMutatieRecord()) {
          if (pos(pl.getHuwelijk().getNummer().getCode())) {
            BasePLExt partnerPl = getPl(pl.getHuwelijk().getNummer().getCode());
            if (partnerPl != null) {
              return partnerPl;
            }
          }
        }
        return null;
      }

      private String getOntbinding(BasePLRec sluiting) {

        String d = sluiting.getElemVal(GBAElem.DATUM_ONTBINDING)
            .getDescr();
        String p = sluiting.getElemVal(GBAElem.PLAATS_ONTBINDING)
            .getDescr();
        String l = sluiting.getElemVal(GBAElem.LAND_ONTBINDING)
            .getDescr();
        String r = sluiting.getElemVal(GBAElem.REDEN_ONTBINDING)
            .getDescr();

        return d.isEmpty() ? "" : MessageFormat.format("{0}, {1}, {2} ({3})", d, p, l, r);
      }

      private String getSluiting(BasePLRec sluiting) {

        String d = sluiting.getElemVal(GBAElem.DATUM_VERBINTENIS)
            .getDescr();
        String p = sluiting.getElemVal(GBAElem.PLAATS_VERBINTENIS)
            .getDescr();
        String l = sluiting.getElemVal(GBAElem.LAND_VERBINTENIS)
            .getDescr();
        String s = sluiting.getElemVal(GBAElem.SOORT_VERBINTENIS).getDescr();

        return d.isEmpty() ? "" : MessageFormat.format("{0}, {1}, {2} ({3})", d, p, l, s);
      }
    });
  }
}
