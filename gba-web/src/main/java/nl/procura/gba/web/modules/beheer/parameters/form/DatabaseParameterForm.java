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

package nl.procura.gba.web.modules.beheer.parameters.form;

import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.fil;
import static nl.procura.standard.exceptions.ProExceptionSeverity.ERROR;
import static nl.procura.standard.exceptions.ProExceptionType.PROGRAMMING;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.util.*;
import java.util.Map.Entry;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.modules.beheer.parameters.bean.ParameterBean;
import nl.procura.gba.web.modules.beheer.parameters.form.popup.ParameterWarningWindow;
import nl.procura.gba.web.modules.beheer.parameters.page1.PositionComparator;
import nl.procura.gba.web.services.applicatie.meldingen.ServiceMelding;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.gba.web.services.beheer.parameter.ParameterType;
import nl.procura.gba.web.services.beheer.parameter.ParameterValidator;
import nl.procura.gba.web.services.beheer.parameter.annotations.ParameterAnnotation;
import nl.procura.gba.web.services.beheer.profiel.Profiel;
import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.annotation.field.Immediate;
import nl.procura.vaadin.component.formfieldfactory.BeanAnnotationUtil;

public class DatabaseParameterForm extends GbaForm<ParameterBean> {

  private String    category;
  private Gebruiker gebruiker;
  private Profiel   profiel;

  public DatabaseParameterForm(String category) {
    setCategory(category);
  }

  @Override
  public void afterSetBean() {
    super.afterSetBean();
    addChangeListener();
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public boolean isGebruiker() {
    return gebruiker != null;
  }

  public boolean isProfiel() {
    return profiel != null;
  }

  @Override
  public com.vaadin.ui.Field newField(com.vaadin.ui.Field field, Property property) {

    // Als het algemene parameters zijn dan geen lege keuze geven.
    if ((field instanceof GbaNativeSelect) && !isGebruiker() && !isProfiel()) {
      ((GbaNativeSelect) field).setNullSelectionAllowed(false);
    }

    return super.newField(field, property);
  }

  public void save(long cUsr, long cProfile) {

    commit();

    validateAndSaveParameters(cUsr, cProfile);
  }

  public void setBean(Object bean, Gebruiker gebruiker, Profiel profiel) {

    this.gebruiker = gebruiker;
    this.profiel = profiel;

    setColumnWidths("250px", "");

    Collection<?> annotations = getFilteredOrderFromAnnotations(bean.getClass());
    setOrder(annotations.toArray(new String[0]));

    super.setBean(bean);
  }

  private String getFieldCaption(ParameterType type) {
    try {
      return ParameterBean.getFields((b) -> b.isType(type))
          .stream()
          .findFirst()
          .get().getFieldAnn().caption();
    } catch (SecurityException e) {
      throw new ProException(PROGRAMMING, ERROR, "Fout bij opvragen annotatie", e);
    }
  }

  private void addChangeListener() {
    for (Field field : getBean().getClass().getDeclaredFields()) {
      if (field.isAnnotationPresent(Immediate.class)) {
        com.vaadin.ui.Field foundField = getField(field.getName());
        if (foundField != null) {
          final ParameterAnnotation parameter = field.getAnnotation(ParameterAnnotation.class);
          final ParameterChangeListener changeListener = new ParameterChangeListener();
          foundField.addListener((ValueChangeListener) event -> {
            Object value = event.getProperty().getValue();
            changeListener.onChange(parameter.value(), value, DatabaseParameterForm.this);
          });
          changeListener.onChange(parameter.value(), foundField.getValue(), DatabaseParameterForm.this);
        }
      }
    }
  }

  private Collection<?> getFilteredOrderFromAnnotations(Class<?> beanClass) {

    Collection<?> propertyIds = getOrderFromAnnotations(beanClass);

    List<String> result = new ArrayList<>();

    for (Object propertyId : propertyIds) {
      if (propertyId instanceof String) {

        try {
          AnnotatedElement element = BeanAnnotationUtil.getMember(beanClass, propertyId);
          if (element.isAnnotationPresent(ParameterAnnotation.class)) {
            ParameterAnnotation annotation = element.getAnnotation(ParameterAnnotation.class);
            if (isAnnotationCorrect(annotation.value(), category, isGebruiker(), isProfiel())) {
              result.add((String) propertyId);
            }
          }
        } catch (SecurityException | NoSuchFieldException | NoSuchMethodException e) {
          e.printStackTrace();
        }
      }
    }

    return result;
  }

  private Collection<?> getOrderFromAnnotations(Class<?> beanClass) {

    List<Object> propertyIds = BeanAnnotationUtil.getPropertyIdsForAnnotatedFields(beanClass);

    propertyIds.sort(new PositionComparator(beanClass));

    return propertyIds;
  }

  private ParameterAnnotation getParameterAnnotation(String id) {
    try {
      Field field = getBean().getClass().getDeclaredField(id);
      return field.getAnnotation(ParameterAnnotation.class);
    } catch (SecurityException | NoSuchFieldException e) {
      throw new ProException(PROGRAMMING, ERROR, "Fout bij opvragen annotatie", e);
    }
  }

  private ParameterValidator getParameterValidator() {
    return getParameterAnnotation(astr(getItemPropertyIds().iterator().next())).value().getCategory().getValidator();
  }

  private boolean isAnnotationCorrect(ParameterType type, String category,
      boolean showUser, boolean showProfile) {

    boolean isCategory = (type.getCategory() != null) && type.getCategory().toString().equals(category);
    boolean isUserParameter = showUser && type.isShowUser();
    boolean isProfileParameter = showProfile && type.isShowProfile();
    boolean isAppParameter = !showUser && !showProfile && type.isShowApplication();

    return isCategory && (isAppParameter || isProfileParameter || isUserParameter);
  }

  private void saveParameter(ParameterType parameterType, String val, long cUsr, long cProfile) {
    getApplication().getServices().getParameterService().saveParameter(parameterType, val, cUsr, cProfile);
  }

  /**
   * Valideer de parameters.
   * Kijk of er een ParameterValidator is en gooi indien nodig popup op.
   */
  private void validateAndSaveParameters(final long cUsr, final long cProfile) {

    ParameterValidator parameterValidator = getParameterValidator();

    Map<ParameterType, String> map = new HashMap<>();

    for (Object fieldId : getItemPropertyIds()) {
      ParameterType type = getParameterAnnotation(astr(fieldId)).value();
      String value = astr(getField(fieldId).getValue());
      map.put(type, value);
    }

    for (final Entry<ParameterType, String> entry : map.entrySet()) {

      final ParameterType type = entry.getKey();
      final String val = entry.getValue();

      if (parameterValidator != null) {

        String warning = "";
        String cause = "";

        try {
          parameterValidator.validate(type, cUsr, map, getApplication().getServices().getParameterService());
        } catch (RuntimeException e) {
          warning = e.getMessage();

          if (e.getCause() != null) {
            cause = e.getCause().toString();
          }

          if (!(e instanceof ProException)) {
            ServiceMelding melding = new ServiceMelding().setSeverity(ERROR)
                .setMelding(warning)
                .setException(e);
            getApplication().getServices().getMeldingService().add(melding);
          }
        }

        if (fil(warning)) {
          String caption = getFieldCaption(type);
          getWindow().addWindow(new ParameterWarningWindow(caption, warning, cause));
        }

        saveParameter(type, val, cUsr, cProfile);
      }
    }

    getApplication().getServices().reloadGebruiker();
  }
}
