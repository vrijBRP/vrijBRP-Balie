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

import java.lang.reflect.AnnotatedElement;
import java.util.Comparator;

import nl.procura.vaadin.annotation.layout.Position;
import nl.procura.vaadin.component.formfieldfactory.BeanAnnotationUtil;

public class PositionComparator implements Comparator<Object> {

  private static final int O1_AFTER_O2   = 1;
  private static final int O1_BEFORE_O2  = -1;
  private static final int O1_SAME_AS_O2 = 0;
  private Class<?>         beanClass;

  public PositionComparator() {
  }

  public PositionComparator(Class<?> beanClass) {
    setBeanClass(beanClass);
  }

  @Override
  public int compare(Object o1, Object o2) {

    Position position1 = getPosition(getBeanClass(), o1);
    Position position2 = getPosition(getBeanClass(), o2);

    if (position1 == null && position2 != null) {
      return O1_AFTER_O2;
    } else if (position1 != null && position2 == null) {
      return O1_BEFORE_O2;
    } else if (position1 == null && position2 == null) {
      return O1_SAME_AS_O2;
    } else if (position1 != null && position2 != null) {
      return position1.order().compareTo(position2.order());
    } else {
      return O1_SAME_AS_O2;
    }
  }

  private Class<?> getBeanClass() {
    return beanClass;
  }

  private void setBeanClass(Class<?> beanClass) {
    this.beanClass = beanClass;
  }

  private Position getPosition(Class<?> beanClass, Object propertyId) {

    try {

      AnnotatedElement member = BeanAnnotationUtil.getMember(beanClass, propertyId);

      if (member.isAnnotationPresent(Position.class)) {
        return member.getAnnotation(Position.class);
      }

      return null;
    } catch (SecurityException e) {
      throw new RuntimeException(
          "SecurityException while searching for member with propertyId '" + propertyId + "' ", e);
    } catch (NoSuchMethodException e) {

      throw new IllegalArgumentException(
          "Unable to find the method with propertyId '" + propertyId
              + "'. Did you set the accesType of the @FormBean correctly?",
          e);
    } catch (NoSuchFieldException e) {

      throw new IllegalArgumentException(
          "Unable to find the field with propertyId '" + propertyId
              + "'. Did you set the accesType of the @FormBean correctly?",
          e);
    }
  }
}
