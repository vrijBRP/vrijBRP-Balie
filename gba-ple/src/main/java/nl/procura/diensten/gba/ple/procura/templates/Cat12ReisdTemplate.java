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

import static nl.procura.burgerzaken.gba.core.enums.GBAElem.*;
import static nl.procura.burgerzaken.gba.core.enums.GBARecStatus.HIST;
import static nl.procura.standard.Globalfunctions.along;
import static nl.procura.standard.Globalfunctions.emp;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;

import nl.procura.burgerzaken.gba.core.enums.GBACat;
import nl.procura.diensten.gba.ple.base.BasePLRec;
import nl.procura.diensten.gba.ple.base.BasePLValue;
import nl.procura.diensten.gba.ple.procura.utils.SortableObject;
import nl.procura.diensten.gba.ple.procura.utils.diacrits.Diacrieten;
import nl.procura.diensten.gba.ple.procura.utils.diacrits.Diacs;
import nl.procura.gba.jpa.probev.db.*;
import nl.procura.standard.exceptions.ProException;

public class Cat12ReisdTemplate extends PLETemplateProcura<AbstractReisd> {

  private static int CODE_MUNICIPALITY_TABLE = 33;
  private static int CODE_COUNTRY_TABLE      = 34;

  @Override
  public void parse(SortableObject<AbstractReisd> so) {

    AbstractReisd reisd = so.getObject();
    BasePLRec cat = addCat(GBACat.REISDOC, so);

    if (reisd.getReisdoc().getCReisdoc() != -1L) {
      addElem(SOORT_NL_REISDOC, reisd.getReisdoc());
      addElem(NR_NL_REISDOC, reisd.getNrNlDoc());
      addElem(DATUM_UITGIFTE_NL_REISDOC, reisd.getDUitgifte());
      addElem(AUTORIT_VAN_AFGIFTE_NL_REISDOC, getAutority(reisd.getAutoriteit()));
      addElem(DATUM_EINDE_GELDIG_NL_REISDOC, reisd.getDGeldEnd());

      if (reisd.getDInneming().intValue() != -1) {
        addElem(DATUM_INH_VERMIS_NL_REISDOC, reisd.getDInneming());
        addElem(AAND_INH_VERMIS_NL_REISDOC, reisd.getAandVi());
      }

      addElem(LENGTE_HOUDER, reisd.getLengte());
    } else {
      addElem(SIG_MET_BETREK_TOT_VERSTREK_NL_REISDOC, reisd.getSignalering());
      addElem(AAND_BEZIT_BUITENL_REISDOC, reisd.getAandBuitenl());
    }

    addElem(GEMEENTE_DOC, reisd.getGOntl());
    addElem(DATUM_DOC, reisd.getDOntl());
    addElem(BESCHRIJVING_DOC, reisd.getDocOntl());

    addElem(AAND_GEG_IN_ONDERZ, reisd.getIndBezw());
    addElem(DATUM_INGANG_ONDERZ, reisd.getDBezwIn());
    addElem(DATUM_EINDE_ONDERZ, reisd.getDBezwEnd());

    addElem(INGANGSDAT_GELDIG, reisd.getId().getDGeld());
    addElem(VOLGCODE_GELDIG, reisd.getId().getVGeld());
    addElem(DATUM_VAN_OPNEMING, reisd.getDGba());

    // Historie in reisdocument is altijd administratief
    String indO = reisd.getIndO();
    if (emp(indO) && HIST == cat.getStatus()) {
      indO = "X";
    }

    addElem(IND_ONJUIST, indO);
  }

  /**
   * Returns the autoriteit based on the search Argument (B000);
   */
  private BasePLValue getAutority(String value) {

    String autority = "";
    String institution = "";

    EntityManager probev = getEntityManager().getManager();
    Diacrieten diacrieten = new Diacrieten(getEntityManager());

    // full match
    String sql = "select autorit from Autorit autorit where autorit.cAutorit = :cAutorit";
    TypedQuery<Autorit> query = probev.createQuery(sql, Autorit.class);
    query.setParameter(Autorit_.cAutorit.getName(), value);
    for (Autorit a : query.getResultList()) {
      autority = a.getAutorit();
    }

    if (StringUtils.isBlank(autority)) {
      // split letters and number
      String letters = value.replaceAll("\\d+", "");
      long number = along(value.replaceAll("\\D+", ""));

      if (number >= 0) {
        query = probev.createQuery(sql, Autorit.class);
        query.setParameter(Autorit_.cAutorit.getName(), letters);

        for (Autorit a : query.getResultList()) {
          autority = a.getAutorit() + " ";
          // If number is 33 then search in place table
          if (a.getTabel() != null) {
            if (a.getTabel().intValue() == CODE_MUNICIPALITY_TABLE) {
              Plaats plaats = probev.find(Plaats.class, number);
              if (plaats == null) {
                throw new ProException("Geen plaats gevonden met code " + number);
              }
              institution = diacrieten.merge(plaats, Diacs.PLAATS, plaats.getPlaats());
            }
            // If number is 34 then search in country table
            else if (a.getTabel().intValue() == CODE_COUNTRY_TABLE) {
              Land country = probev.find(Land.class, number);
              if (country == null) {
                throw new ProException("Geen land gevonden met code " + number);
              }
              institution = diacrieten.merge(country, Diacs.LAND, country.getLand());
            }
          }
        }
      }
    }

    return new BasePLValue(value, (autority + institution).trim());
  }
}
