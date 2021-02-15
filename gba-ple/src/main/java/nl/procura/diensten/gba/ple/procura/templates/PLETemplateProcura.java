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

import nl.procura.burgerzaken.gba.core.enums.GBAElem;
import nl.procura.diensten.gba.ple.base.BasePLElem;
import nl.procura.diensten.gba.ple.base.BasePLValue;
import nl.procura.diensten.gba.ple.procura.arguments.PLEArgs;
import nl.procura.diensten.gba.ple.procura.utils.PLEElementFormatter;
import nl.procura.diensten.gba.ple.procura.utils.SortableObject;
import nl.procura.diensten.gba.ple.procura.utils.jpa.PLEJpaManager;
import nl.procura.diensten.gba.ple.utils.AbstractPLETemplate;

public class PLETemplateProcura<T> extends AbstractPLETemplate {

  protected static final int BRON_XINW     = 2;
  protected static final int BRON_INW      = 1;
  protected static final int BRON_VERW     = 0;
  protected static final int BRON_ONBEKEND = -1;

  private PLEJpaManager       entityManager = null;
  private PLEElementFormatter elementFormatter;
  private PLEArgs             arguments     = new PLEArgs();

  public PLETemplateProcura init(PLETemplateProcura t) {

    setEntityManager(t.getEntityManager());
    setArguments(t.getArguments());

    if (t.getElementFormatter() == null) {
      setElementFormatter(new PLEElementFormatter(this));
    } else {
      setElementFormatter(t.getElementFormatter());
    }

    super.init(t);
    return this;
  }

  public <T> Object expand(Class<T> c, Number code) {
    try {
      T o = getEntityManager().getManager().find(c, code.longValue());
      if (o == null) {
        return c.newInstance();
      } else {
        return o;
      }
    } catch (Exception e1) {
      throw new IllegalArgumentException("Cannot expand " + c, e1);
    }
  }

  public BasePLElem addElem(GBAElem element, Object input) {
    return getBuilder().setElem(element, getElementFormatter().format(getCat(), element.getCode(), input));
  }

  public BasePLElem addElem(GBAElem element, BasePLValue waarde) {
    return getBuilder().setElem(element, waarde);
  }

  @SuppressWarnings("unused")
  protected void parse(SortableObject<T> o) {
  } //should be overridden

  @SuppressWarnings("unused")
  protected void parse(int nr, SortableObject<T> o) {
  } //should be overridden

  public PLEElementFormatter getElementFormatter() {
    return elementFormatter;
  }

  public void setElementFormatter(PLEElementFormatter elementFormatter) {
    this.elementFormatter = elementFormatter;
  }

  public PLEJpaManager getEntityManager() {
    return entityManager;
  }

  public void setEntityManager(PLEJpaManager entityManager) {
    this.entityManager = entityManager;
  }

  public PLEArgs getArguments() {
    return arguments;
  }

  public void setArguments(PLEArgs arguments) {
    this.arguments = arguments;
  }
}
