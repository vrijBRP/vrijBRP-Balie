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

package nl.procura.gba.web.services.zaken.protocol;

import static nl.procura.standard.Globalfunctions.along;
import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.aval;
import static nl.procura.standard.Globalfunctions.date2str;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.procura.burgerzaken.gba.core.enums.GBAGroupElements;
import nl.procura.diensten.gba.ple.base.BasePLCat;
import nl.procura.diensten.gba.ple.base.BasePLElem;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.procura.arguments.PLEArgs;
import nl.procura.diensten.gba.ple.procura.arguments.PLEDatasource;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.jpa.personen.dao.ProtNewDao;
import nl.procura.gba.jpa.personen.db.ProtNew;
import nl.procura.gba.jpa.personen.db.ProtNewSearch;
import nl.procura.gba.web.common.misc.ZaakPeriode;
import nl.procura.gba.web.components.fields.values.UsrFieldValue;
import nl.procura.gba.web.services.AbstractService;
import nl.procura.gba.web.services.aop.ThrowException;
import nl.procura.gba.web.services.aop.Transactional;
import nl.procura.gba.web.services.beheer.parameter.ParameterConstant;
import nl.procura.standard.ProcuraDate;
import nl.procura.vaadin.component.field.fieldvalues.AnrFieldValue;
import nl.procura.vaadin.component.field.fieldvalues.FieldValue;
import nl.procura.validation.Anr;
import nl.procura.validation.Bsn;

public class ProtocolleringService extends AbstractService {

  private final static Logger logger = LoggerFactory.getLogger(ProtocolleringService.class.getName());

  private static final int  ANR           = 2;
  private static final int  CATEGORIEEN   = 3;
  private static final int  ELEMENTEN     = 4;
  private static final long PERSOONSLIJST = 0;

  public ProtocolleringService() {
    super("Protocollering");
  }

  @ThrowException("Fout bij het ophalen van protocolleringsgegevens")
  public List<ProtocolZoekopdracht> getProtocolGroepen(UsrFieldValue gebruiker, FieldValue nummer,
      ZaakPeriode periode, ProtocolleringGroep groep) {
    List<ProtocolZoekopdracht> l = new ArrayList<>();

    if (new Bsn(nummer.getStringValue()).isCorrect()) {
      PLEArgs args = new PLEArgs();
      args.setDatasource(PLEDatasource.STANDAARD);
      args.addNummer(nummer.getStringValue());

      for (BasePLExt pl : getServices().getPersonenWsService().getPersoonslijsten(args,
          false).getBasisPLWrappers()) {
        nummer = new FieldValue(pl.getPersoon().getAnr().getVal());
        break;
      }
    }

    long cUsr = gebruiker != null ? along(gebruiker.getValue()) : 0;
    long nr = along(nummer.getValue());
    long dFrom = periode != null ? periode.getdFrom() : 0;
    long dTo = periode != null ? periode.getdTo() : 0;

    for (Object[] p : ProtNewDao.findGroup(cUsr, nr, dFrom, dTo, groep.getGroep())) {

      ProtocolZoekopdracht ps = new ProtocolZoekopdracht();
      ps.setNummer(nummer);
      ps.setGebruiker(gebruiker);
      ps.setPeriode(periode);
      ps.setAantal(along(p[0]));
      ps.setGroep(groep);

      switch (groep) {
        default:
        case DATUM:
          ps.setWaarde(along(p[1]));
          ps.setOmschrijving(date2str(along(p[1])));
          break;

        case ANUMMER:
          ps.setWaarde(along(p[1]));
          ps.setOmschrijving(new Anr(astr(p[1])).getFormatAnummer());
          break;

        case GEBRUIKER:
          ps.setWaarde(along(p[1]));
          ps.setOmschrijving(astr(p[2]));
          break;
      }

      l.add(ps);
    }

    return l;
  }

  @ThrowException("Fout bij het ophalen van protocolleringsgegevens")
  public List<ProtocolRecord> getProtocollen(ProtocolZoekopdracht zoekopdracht) {

    List<ProtocolRecord> l = new ArrayList<>();
    long cUsr = zoekopdracht.getGebruiker() != null ? along(zoekopdracht.getGebruiker().getValue()) : 0;
    long anr = zoekopdracht.getAnummer() != null ? along(zoekopdracht.getAnummer().getValue()) : 0;
    long dFrom = zoekopdracht.getPeriode() != null ? zoekopdracht.getPeriode().getdFrom() : 0;
    long dTo = zoekopdracht.getPeriode() != null ? zoekopdracht.getPeriode().getdTo() : 0;

    for (ProtNewSearch p : ProtNewDao.find(cUsr, anr, dFrom, dTo)) {
      ProtocolRecord pNew = new ProtocolRecord(p);
      pNew.setDatum(new DateTime(p.getProtNew().getdIn(), p.getTIn()));
      pNew.setAnummer(new AnrFieldValue(astr(p.getProtNew().getAnr())));
      pNew.setGebruiker(p.getProtNew().getUsr().getUsrfullname());
      l.add(pNew);
    }

    return l;
  }

  @Transactional
  @ThrowException("Fout bij protocolleren gegevens")
  public void save(BasePLExt bpl) {

    int type = aval(getServices().getParameterService().getParm(ParameterConstant.PROT_STORE_PL));

    /*
     * 1 = niets
     * 2 = a-nummers
     * 3 = a-nummers + categorieen
     * 4 = a-nummers + categorieen + elementen
     */

    ProcuraDate date = new ProcuraDate();
    long dIn = along(date.getSystemDate());
    long tIn = along(date.getSystemTime());
    long cUsr = getServices().getGebruiker().getCUsr();
    long anr = along(bpl.getPersoon().getAnr().getCode());

    logger.debug("Protocolleren van gezochte persoonslijst met type " + type);

    if (type == ANR || type == CATEGORIEEN || type == ELEMENTEN) {

      ProtNew protNew = ProtNewDao.getProtNew(anr, cUsr, dIn);
      ProtNewSearch protNewSearch = ProtNewDao.getProtNewSearch(protNew, PERSOONSLIJST, tIn);

      if (type == CATEGORIEEN || type == ELEMENTEN) {
        for (BasePLCat cat : bpl.getCats()) {
          if (cat.hasSets() && cat.getLatestRec().hasElems()) {
            if (type == ELEMENTEN) {
              for (BasePLElem elem : cat.getLatestRec().getElems()) {

                String element = String.format("%04d",
                    elem.getElemCode()) + " ("
                    + GBAGroupElements.getByCat(elem.getCatCode(), elem.getElemCode()).getElem().getDescr() + ")";

                ProtNewDao.addProtNewSearchAttr(protNewSearch, cat.getCatType().getDescr(), element);
              }
            } else if (type == CATEGORIEEN) {
              ProtNewDao.addProtNewSearchAttr(protNewSearch, cat.getCatType().getDescr(), "");
            }
          }
        }
      }
    }
  }
}
