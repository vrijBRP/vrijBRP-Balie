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

package nl.procura.gba.web.services.zaken.selectie;

import static nl.procura.gba.common.MiscUtils.copyList;
import static nl.procura.gba.web.services.beheer.profiel.actie.ProfielActie.SELECT_SELECTIES;

import java.util.List;

import nl.procura.gba.jpa.personen.dao.SelDao;
import nl.procura.gba.web.services.AbstractService;
import nl.procura.gba.web.services.aop.ThrowException;
import nl.procura.gba.web.services.aop.Transactional;
import nl.procura.gba.web.services.applicatie.meldingen.types.SelectieMelding;

public class SelectieService extends AbstractService {

  public SelectieService() {
    super("Selectie");
  }

  @Override
  public void check() {
    if (getServices().getGebruiker().getProfielen().isProfielActie(SELECT_SELECTIES)) {
      for (Selectie selectie : getSelecties()) {
        int count = 0;
        boolean onlyUserMessages = getServices().getMeldingService().isShowOnlyUser();
        for (SelDao.Row row : SelDao.getFromStatement(selectie.getStatement()).getRows()) {
          SelDao.Value gebruiker = row.getValue(SelectieColumn.GEBRUIKER.getName());
          if (!onlyUserMessages
              || (gebruiker != null
                  && gebruiker.getValue().equals(getServices()
                      .getGebruiker()
                      .getNaam()))) {
            count++;
          }
        }

        if (count > 0) {
          getServices().getMeldingService().add(new SelectieMelding(selectie, count));
        }
      }
    }
  }

  @ThrowException("Fout bij het ophalen van de selecties")
  public List<Selectie> getSelecties() {
    return copyList(SelDao.findSels(), Selectie.class);
  }

  @Transactional
  @ThrowException("Fout bij het verwijderen")
  public void delete(Selectie selectie) {
    removeEntity(selectie);
  }

  @Transactional
  @ThrowException("Fout bij het opslaan")
  public void save(Selectie selectie) {
    saveEntity(selectie);
  }
}
