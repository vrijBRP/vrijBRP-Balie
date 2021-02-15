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

package nl.procura.gba.web.modules.beheer.parameters.page1;

import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.exceptions.ProExceptionSeverity.ERROR;
import static nl.procura.standard.exceptions.ProExceptionType.DATABASE;

import java.util.Iterator;

import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.ui.Component;
import com.vaadin.ui.Tree;

import nl.procura.gba.web.application.GbaApplication;
import nl.procura.gba.web.modules.beheer.parameters.bean.ParameterBean;
import nl.procura.gba.web.modules.beheer.parameters.container.ParameterTreeContainer;
import nl.procura.gba.web.modules.beheer.parameters.form.DatabaseParameterForm;
import nl.procura.gba.web.modules.beheer.parameters.layout.DatabaseParameterLayout;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.gba.web.services.beheer.parameter.ParameterGroup;
import nl.procura.gba.web.services.beheer.profiel.Profiel;
import nl.procura.standard.exceptions.ProException;

public class ParameterTree extends Tree {

  private ParameterBean   parameterBean = new ParameterBean();
  private final Gebruiker gebruiker;
  private final Profiel   profiel;

  public ParameterTree(ParameterBean parameterBean, Gebruiker gebruiker, Profiel profiel) {

    setParameterBean(parameterBean);

    this.gebruiker = gebruiker;
    this.profiel = profiel;

    setImmediate(true);
    setItemCaptionPropertyId(ParameterTreeContainer.OMSCHRIJVING);
    setContainerDataSource(new ParameterTreeContainer(gebruiker, profiel));
  }

  public ParameterBean getParameterBean() {
    return parameterBean;
  }

  public void setParameterBean(ParameterBean parameterBean) {
    this.parameterBean = parameterBean;
  }

  @Override
  @SuppressWarnings("unchecked")
  public void setContainerDataSource(Container newDataSource) {

    super.setContainerDataSource(newDataSource);

    openAll();

    addListener((ValueChangeListener) event -> {

      if (hasLeafChildren(event)) {
        selectFirstLeaf(event);
      } else {
        ParameterGroup group = (ParameterGroup) getValue();
        Property classProperty = getContainerProperty(getValue(), ParameterTreeContainer.LEAF_CLASS);

        if (classProperty != null) {
          Class<? extends Component> leafClass = (Class<? extends Component>) classProperty.getValue();

          try {
            String naam = "";
            if (gebruiker != null) {
              naam = gebruiker.getNaam();
            } else if (profiel != null) {
              naam = profiel.getProfiel();
            }

            selectComponent(leafClass.getConstructor(GbaApplication.class, String.class,
                String.class).newInstance(getApplication(), naam,
                    group.getId()));
          } catch (Exception e) {
            throw new ProException(DATABASE, ERROR, "Kan component niet laden.", e);
          }
        }
      }
    });

    select(ParameterGroup.GROUP_ALGEMEEN);
  }

  @SuppressWarnings("unused")
  protected void setNewComponent(Component component) {
  } // Override

  private boolean hasLeafChildren(com.vaadin.data.Property.ValueChangeEvent event) {
    Tree tree = (Tree) event.getProperty();
    return tree.areChildrenAllowed(tree.getValue());
  }

  private void openAll() {
    for (Object id : rootItemIds()) {
      expandItemsRecursively(id);
    }
  }

  private void selectComponent(Component component) {

    if (component instanceof DatabaseParameterLayout) {
      DatabaseParameterLayout layout = (DatabaseParameterLayout) component;
      DatabaseParameterForm form = layout.getForm();
      form.setCategory(astr(getValue()));
      form.setBean(getParameterBean(), gebruiker, profiel);
    }

    setNewComponent(component);
  }

  private void selectFirstLeaf(com.vaadin.data.Property.ValueChangeEvent event) {

    Tree tree = (Tree) event.getProperty();
    if (tree.areChildrenAllowed(tree.getValue()) && tree.hasChildren(tree.getValue())) {
      Iterator<?> children = tree.getChildren(tree.getValue()).iterator();
      if ((children != null) && children.hasNext()) {
        tree.select(children.next());
      }
    }
  }
}
