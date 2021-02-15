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

package nl.procura.gba.web.components.fields;

import nl.procura.gba.web.services.interfaces.GeldigheidStatus;
import nl.procura.vaadin.component.container.ArrayListContainer;

public abstract class GeldigheidField extends GbaNativeSelect {

  private ValueChangeListener changeListener;

  public GeldigheidField() {
    setNullSelectionAllowed(false);
    setContainerDataSource(new GeldigheidStatusContainer());
    setValue(GeldigheidStatus.ACTUEEL);
    setImmediate(true);
  }

  @Override
  public void attach() {

    changeListener = (ValueChangeListener) event -> onChangeValue((GeldigheidStatus) event.getProperty().getValue());

    addListener(changeListener);

    super.attach();
  }

  @Override
  public GeldigheidStatus getValue() {
    return (GeldigheidStatus) super.getValue();
  }

  public abstract void onChangeValue(GeldigheidStatus value);

  public class GeldigheidStatusContainer extends ArrayListContainer {

    public GeldigheidStatusContainer() {
      addItem(GeldigheidStatus.NOG_NIET_ACTUEEL);
      addItem(GeldigheidStatus.ACTUEEL);
      addItem(GeldigheidStatus.BEEINDIGD);
      addItem(GeldigheidStatus.ALLES);
    }
  }
}
