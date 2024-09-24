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

package nl.procura.gba.web.modules.beheer.profielen.page4;

import static nl.procura.commons.core.exceptions.ProExceptionSeverity.ERROR;
import static nl.procura.commons.core.exceptions.ProExceptionType.DATABASE;

import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.ui.Tree;

import nl.procura.commons.core.exceptions.ProException;
import nl.procura.gba.web.modules.beheer.profielen.container.ActieFormContainer;
import nl.procura.gba.web.services.beheer.profiel.Profiel;
import nl.procura.gba.web.services.beheer.profiel.actie.ProfielActieType;

public class ActionTree extends Tree {

  private Profiel profiel;

  public ActionTree(Profiel profiel) {

    setProfiel(profiel);
    setImmediate(true);
    setContainerDataSource(new ActieFormContainer());
  }

  public Profiel getProfiel() {
    return profiel;
  }

  public void setProfiel(Profiel profiel) {
    this.profiel = profiel;
  }

  @Override
  public void setContainerDataSource(Container newDataSource) {

    super.setContainerDataSource(newDataSource);

    if (newDataSource instanceof ActieFormContainer) {

      openAll();

      addListener((ValueChangeListener) event -> {

        Property profielActieType = getContainerProperty(getValue(), ActieFormContainer.PROFIELACTIETYPE);

        if (profielActieType != null) {

          try {
            selectComponent(profielActieType.getValue());
          } catch (Exception e) {
            throw new ProException(DATABASE, ERROR, "Kan component niet laden.", e);
          }
        }
      });

      select(ActieFormContainer.ALLE);
    }
  }

  @SuppressWarnings("unused")
  public void setNewComponent(ProfielActieLayout layout) {
  } // Override

  private void openAll() {

    for (Object id : rootItemIds()) {
      expandItemsRecursively(id);
    }
  }

  private void selectComponent(Object object) {

    ProfielActieLayout l;

    if (object instanceof ProfielActieType) {

      l = new ProfielActieLayout(getProfiel(), (ProfielActieType) object);

    } else {

      l = new ProfielActieLayout(getProfiel(), null);
    }

    setNewComponent(l);
  }
}
