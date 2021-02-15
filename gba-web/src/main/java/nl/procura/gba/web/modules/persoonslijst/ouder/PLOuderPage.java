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

package nl.procura.gba.web.modules.persoonslijst.ouder;

import static nl.procura.gba.web.modules.persoonslijst.overig.data.PlDataBean.*;
import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.fil;

import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.base.BasePLSet;
import nl.procura.diensten.gba.ple.base.BasePLValue;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.extensions.formats.Geboorte;
import nl.procura.gba.web.modules.persoonslijst.overig.PlForm;
import nl.procura.gba.web.modules.persoonslijst.overig.data.PlDataBeanForm;
import nl.procura.gba.web.modules.persoonslijst.overig.data.PlDataPage;
import nl.procura.vaadin.component.field.fieldvalues.AnrFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.BsnFieldValue;

public class PLOuderPage extends PlDataPage {

  public PLOuderPage(String title, BasePLSet set) {
    super(title, set);
  }

  @Override
  public PlForm getMainForm() {

    return new PlOuderForm() {

      @Override
      public void setBean(Object bean) {

        PlOuderBean b = new PlOuderBean();

        BasePLValue bsn = getElementW(GBAElem.BSN);
        BasePLValue anr = getElementW(GBAElem.ANR);

        b.setBsn(new BsnFieldValue(astr(bsn.getVal()), bsn.getDescr()));
        b.setAnr(new AnrFieldValue(astr(anr.getVal()), anr.getDescr()));
        b.setGeslachtsnaam(getElementW(GBAElem.GESLACHTSNAAM).getDescr());
        b.setVoorvoegsel(getElementW(GBAElem.VOORV_GESLACHTSNAAM).getDescr());
        b.setTitel(getElementW(GBAElem.TITEL_PREDIKAAT).getDescr());
        b.setVoornaam(getElementW(GBAElem.VOORNAMEN).getDescr());
        b.setGeboren(new Geboorte(getGbaRecord()).getDatumLeeftijdPlaatsLand());
        b.setGeslacht(getElementW(GBAElem.GESLACHTSAAND).getDescr());
        b.setAfstamming(getElementW(GBAElem.DATUM_INGANG_FAM_RECHT_BETREK).getDescr());

        if (fil(bsn.getVal())) {

          BasePLExt ouder = getPl(bsn.getVal());

          if (ouder != null) {

            if (ouder.isToonBeperking()) {
              b.setStatus("Verstrekkingsbeperking");
            } else {
              b.setAdres(ouder.getVerblijfplaats().getAdres().getAdresPcWplGem());
              b.setStatus(ouder.getPersoon().getStatus().getOpsomming());
            }
          }
        }
        super.setBean(b);
      }
    };
  }

  @Override
  public PlDataBeanForm getMetaForm(BasePLRec record) {

    return new PlDataBeanForm(record) {

      @Override
      public String[] getOrder() {

        return new String[]{ GELDIGHEID, OPNAME, ONJUIST, INONDERZOEK, DATUMINGANG, DATUMEINDE, AKTE, ONTLENING,
            DOCUMENT };
      }
    };
  }
}
