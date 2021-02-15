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

package nl.procura.gba.web.services.beheer.persoonhistorie;

import static nl.procura.gba.common.MiscUtils.copyList;
import static nl.procura.standard.Globalfunctions.along;

import java.util.List;

import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.gba.jpa.personen.dao.PlHistDao;
import nl.procura.gba.web.services.AbstractService;
import nl.procura.gba.web.services.aop.Transactional;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;

public class PersoonHistorieService extends AbstractService {

  public PersoonHistorieService() {
    super("Persoonshistorie");
  }

  public List<PersoonHistorie> getPersoonHistorie(PersoonHistorieType type, Gebruiker gebruiker) {
    return copyList(PlHistDao.find(type.getCode(), gebruiker.getCUsr()), PersoonHistorie.class);
  }

  public boolean isFavoriet(BasePLExt pl, Gebruiker gebruiker) {
    long nr = along(pl.getPersoon().getNummer().getVal());
    return PlHistDao.find(PersoonHistorieType.FAVORIETEN.getCode(), gebruiker.getCUsr(), nr).size() > 0;
  }

  @Transactional
  public void change(BasePLExt pl, Gebruiker gebruiker, PersoonHistorieType type, boolean toevoegen) {

    if (toevoegen) {
      long nr = along(pl.getPersoon().getNummer().getVal());
      String oms = pl.getPersoon().getNaam().getNaamNaamgebruikEersteVoornaam();
      long databron = pl.getDatasource().getCode();

      PlHistDao.add(type.getCode(), gebruiker.getCUsr(), nr, oms, databron);

      if (type == PersoonHistorieType.HISTORIE) {
        PlHistDao.cleanUp(gebruiker.getCUsr());
      }
    } else {
      PlHistDao.remove(type.getCode(), gebruiker.getCUsr(), along(pl.getPersoon().getNummer().getVal()));
    }
  }
}
