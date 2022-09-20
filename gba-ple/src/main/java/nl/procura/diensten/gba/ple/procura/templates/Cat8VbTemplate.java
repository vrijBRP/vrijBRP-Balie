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

package nl.procura.diensten.gba.ple.procura.templates;

import static nl.procura.burgerzaken.gba.core.enums.GBAElem.AAND_GEG_IN_ONDERZ;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.ADRES_BUITENL_1;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.ADRES_BUITENL_2;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.ADRES_BUITENL_3;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DATUM_AANVANG_ADRESH;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DATUM_EINDE_ONDERZ;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DATUM_INGANG_ONDERZ;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DATUM_INSCHR;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DATUM_VAN_OPNEMING;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DATUM_VERTREK_UIT_NL;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DATUM_VESTIGING_IN_NL;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.FUNCTIE_ADRES;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.GEM_DEEL;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.GEM_INSCHR;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.GEM_INSCHR_CODE;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.HNR;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.HNR_A;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.HNR_L;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.HNR_T;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.IDCODE_NUMMERAAND;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.ID_VERBLIJFPLAATS;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.IND_DOC;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.IND_ONJUIST;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.INGANGSDAT_GELDIG;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.LAND_VERTREK;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.LAND_VESTIGING;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.LOCATIEBESCHR;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.OMSCHR_VAN_DE_AANGIFTE_ADRESH;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.OPENB_RUIMTE;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.POSTCODE;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.STRAATNAAM;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.VOLGCODE_GELDIG;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.WPL_NAAM;

import java.util.ArrayList;
import java.util.List;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.diensten.gba.ple.procura.utils.PLEWoningObject;
import nl.procura.diensten.gba.ple.procura.utils.SortableObject;
import nl.procura.gba.jpa.probev.db.AbstractVb;

public class Cat8VbTemplate extends PLETemplateProcura<AbstractVb> {

  private List<PLEWoningObject> woningObjecten = new ArrayList<>();

  @Override
  public void parse(SortableObject<AbstractVb> so) {

    AbstractVb vb = so.getObject();
    addCat(GBACat.VB, so);

    addElem(GEM_INSCHR, vb.getGInschr());
    addElem(GEM_INSCHR_CODE, vb.getGInschr().getCPlaats());
    addElem(DATUM_INSCHR, vb.getDInschr());

    if (vb.getDVertrek().intValue() != -1) { // groep 13 komt voor
      addElem(LAND_VERTREK, vb.getLVertrek());
      addElem(DATUM_VERTREK_UIT_NL, vb.getDVertrek());
      addElem(ADRES_BUITENL_1, vb.getBAdres1());
      addElem(ADRES_BUITENL_2, vb.getBAdres2());
      addElem(ADRES_BUITENL_3, vb.getBAdres3());

    } else { // groep 13 komt niet voor

      if (vb.getDAanv().intValue() != -1) { // groep 10 komt voor

        addElem(FUNCTIE_ADRES, vb.getFuncAdr());
        addElem(GEM_DEEL, vb.getGemDeel());
        addElem(DATUM_AANVANG_ADRESH, vb.getDAanv());

        PLEWoningObject woningobject = new PLEWoningObject();
        woningobject.setRef_pl(getBuilder().getResult().getBasePLs().size() - 1);
        woningobject.setC_gem_deel(vb.getGemDeel().getCGemDeel().intValue());
        woningobject.setC_locatie(vb.getLocatie().getCLocatie().intValue());
        woningobject.setC_straat(vb.getStraat().getCStraat().intValue());
        woningobject.setHnr(vb.getHnr().intValue());
        woningobject.setHnr_a(vb.getHnrA());
        woningobject.setHnr_l(vb.getHnrL());
        woningobject.setHnr_t(vb.getHnrT());
        getWoningObjecten().add(woningobject);

        if (vb.getLocatie().getCLocatie() != -1) {
          addElem(LOCATIEBESCHR, vb.getLocatie());

        } else {
          addElem(STRAATNAAM, vb.getStraat());
          addElem(HNR, vb.getHnr());
          addElem(HNR_L, vb.getHnrL());
          addElem(HNR_T, vb.getHnrT());
          addElem(HNR_A, vb.getHnrA());
          addElem(POSTCODE, vb.getPc());
        }

        addElem(OPENB_RUIMTE, vb.getObr());
        addElem(WPL_NAAM, vb.getWpl());
        addElem(ID_VERBLIJFPLAATS, vb.getAon());
        addElem(IDCODE_NUMMERAAND, vb.getIna());
        addElem(LAND_VESTIGING, vb.getLVestiging());
        addElem(DATUM_VESTIGING_IN_NL, vb.getDVestiging());
      }
    }

    addElem(OMSCHR_VAN_DE_AANGIFTE_ADRESH, vb.getAangifte());

    addElem(IND_DOC, vb.getIndDoc());

    addElem(AAND_GEG_IN_ONDERZ, vb.getIndBezw());
    addElem(DATUM_INGANG_ONDERZ, vb.getDBezwIn());
    addElem(DATUM_EINDE_ONDERZ, vb.getDBezwEnd());
    addElem(IND_ONJUIST, vb.getIndO());
    addElem(INGANGSDAT_GELDIG, vb.getId().getDGeld());
    addElem(VOLGCODE_GELDIG, vb.getId().getVGeld());
    addElem(DATUM_VAN_OPNEMING, vb.getDGba());

  }

  public List<PLEWoningObject> getWoningObjecten() {
    return woningObjecten;
  }

  public void setWoningObjecten(ArrayList<PLEWoningObject> woningObjecten) {
    this.woningObjecten = woningObjecten;
  }
}
