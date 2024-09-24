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

package nl.procura.gba.web.modules.beheer.profielen.page5;

import static nl.procura.commons.core.exceptions.ProExceptionSeverity.ERROR;
import static nl.procura.commons.core.exceptions.ProExceptionType.DATABASE;

import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.ui.Tree;

import nl.procura.gba.web.modules.beheer.profielen.container.VeldFormContainer;
import nl.procura.gba.web.services.beheer.profiel.Profiel;
import nl.procura.gba.web.services.beheer.profiel.veld.ProfielVeldType;
import nl.procura.commons.core.exceptions.ProException;

public class FieldTree extends Tree {

  private Profiel profiel;

  public FieldTree(Profiel profiel) {

    setProfiel(profiel);
    setImmediate(true);
    setContainerDataSource(new VeldFormContainer());
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

    if (newDataSource instanceof VeldFormContainer) {

      openAll();

      addListener((ValueChangeListener) event -> {

        Property profielVeldType = getContainerProperty(getValue(), VeldFormContainer.PROFIELVELDTYPE);

        if (profielVeldType != null) {

          try {
            selectComponent(profielVeldType.getValue());
          } catch (Exception e) {
            throw new ProException(DATABASE, ERROR, "Kan component niet laden.", e);
          }
        }
      });

      select(VeldFormContainer.ALLE);
    }
  }

  @SuppressWarnings("unused")
  protected void setNewComponent(ProfielVeldLayout layout) {
  } // Override

  private void openAll() {

    for (Object id : rootItemIds()) {
      expandItemsRecursively(id);
    }
  }

  private void selectComponent(Object object) {

    ProfielVeldLayout l;

    if (object instanceof ProfielVeldType) {

      l = new ProfielVeldLayout(getProfiel(), (ProfielVeldType) object);
    } else {
      l = new ProfielVeldLayout(getProfiel(), null);
    }

    setNewComponent(l);
  }
}
