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

package nl.procura.diensten.gba.ple.extensions.formats;

import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DATUM_ONTBINDING;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.DATUM_VERBINTENIS;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.GESLACHTSNAAM;
import static nl.procura.burgerzaken.gba.core.enums.GBAElem.REDEN_ONTBINDING;
import static nl.procura.diensten.gba.ple.extensions.formats.BurgerlijkeStaatType.ACHTERGEBLEVEN;
import static nl.procura.diensten.gba.ple.extensions.formats.BurgerlijkeStaatType.GESCHEIDEN;
import static nl.procura.diensten.gba.ple.extensions.formats.BurgerlijkeStaatType.HUWELIJK;
import static nl.procura.diensten.gba.ple.extensions.formats.BurgerlijkeStaatType.ONBEKEND;
import static nl.procura.diensten.gba.ple.extensions.formats.BurgerlijkeStaatType.ONGEHUWD;
import static nl.procura.diensten.gba.ple.extensions.formats.BurgerlijkeStaatType.ONTBONDEN;
import static nl.procura.diensten.gba.ple.extensions.formats.BurgerlijkeStaatType.PARTNERSCHAP;
import static nl.procura.diensten.gba.ple.extensions.formats.BurgerlijkeStaatType.WEDUWE;
import static nl.procura.standard.Globalfunctions.along;
import static nl.procura.standard.Globalfunctions.fil;

import org.apache.commons.lang3.StringUtils;

import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.base.BasePLSet;
import nl.procura.diensten.gba.ple.base.UnknownGBAElementException;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BurgerlijkeStaat {

  private final BasePLExt pl;

  public BurgerlijkeStaat(BasePLExt pl) {
    this.pl = pl;
  }

  public BurgerlijkeStandStatus getStatus() {

    try {
      BasePLSet set = pl.getHuwelijk().getHuwelijkSet();
      BasePLRec huwRec = set.getLatestRec();

      if (huwRec != null && huwRec.hasElems()) {
        String marriageType = huwRec.getElem(GBAElem.SOORT_VERBINTENIS).getValue().getCode().toLowerCase();
        String naam = huwRec.getElem(GESLACHTSNAAM).getValue().getCode().toLowerCase();

        // Type of marriage is not allowed. So no state can be derived
        if (!huwRec.getElem(GBAElem.SOORT_VERBINTENIS).isAllowed()) {
          return new BurgerlijkeStandStatus(ONBEKEND, 0);
        } else
        // Type of marriage is not returned from the webservice while the name is returned
        if (StringUtils.isNotBlank(naam) && StringUtils.isBlank(marriageType)) {
          return new BurgerlijkeStandStatus(ONBEKEND, 0);
        }

        long dHuw = huwRec.getElemVal(DATUM_VERBINTENIS).toLong();
        long dOntb = huwRec.getElemVal(DATUM_ONTBINDING).toLong();
        String rOntb = huwRec.getElemVal(REDEN_ONTBINDING)
            .getVal()
            .toLowerCase();

        boolean dissolved = fil(rOntb);
        if (marriageType.matches("\\.")) {
          return new BurgerlijkeStandStatus(ONBEKEND, 0);
        }

        if ("h".equals(marriageType)) {
          if (dissolved) {
            if (rOntb.matches("o|r|(.*)overlijden(.*)")) {
              return new BurgerlijkeStandStatus(WEDUWE, dOntb); // Weduwe / weduwnaar
            }

            return new BurgerlijkeStandStatus(GESCHEIDEN, dOntb); // Gescheiden
          }

          return new BurgerlijkeStandStatus(HUWELIJK, dHuw); // Actueel huwelijk

        } else if ("p".equals(marriageType)) { // Gereg. partnerschap
          if (dissolved) {
            if (rOntb.matches("o|r|(.*)overlijden(.*)")) {
              return new BurgerlijkeStandStatus(ACHTERGEBLEVEN, dOntb); // Achtergebleven
            }

            return new BurgerlijkeStandStatus(ONTBONDEN, dOntb); // Ontbonden partnerschap
          }

          return new BurgerlijkeStandStatus(PARTNERSCHAP, dHuw); // Actueel partnerschap
        }
      }
    } catch (UnknownGBAElementException e) {
      log.error(e.getMessage(), e);
    }

    long dGeb = along(pl.getPersoon().getGeboorte().getGeboortedatum().getVal());
    return new BurgerlijkeStandStatus(ONGEHUWD, dGeb);
  }

  public class BurgerlijkeStandStatus {

    private final BurgerlijkeStaatType type;
    private final long                 datum;

    BurgerlijkeStandStatus(BurgerlijkeStaatType type, long datum) {
      this.type = type;
      this.datum = datum;
    }

    public BurgerlijkeStaatType getType() {
      return type;
    }

    public long getDatum() {
      return datum;
    }
  }
}
