package nl.procura.gba.web.modules.bs.lv.page40;

import static java.util.Optional.ofNullable;
import static nl.procura.standard.Globalfunctions.astr;

import java.util.function.Consumer;

import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;

import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.bs.lv.afstamming.LvField;

public class Page40LvForm extends GbaForm<Page40LvBean1> {

  public Field getLvField(LvField field) {
    return getOrder().length > 0 ? getField(field.getName()) : null;
  }

  public <T extends AbstractField> T getLvField(LvField field, Class<T> clazz) {
    return getField(field.getName(), clazz);
  }

  public void ifLvField(LvField field, Consumer<Field> consumer) {
    ofNullable(getField(field.getName())).ifPresent(consumer);
  }

  public boolean isVisibleLvField(LvField field) {
    return ofNullable(getLvField(field)).map(Component::isVisible).orElse(false);
  }

  public <T> void ifLvFieldValue(LvField field, Consumer<String> consumer) {
    ifLvFieldValue(field, consumer, String.class);
  }

  public <T> void ifLvFieldValue(LvField field, Consumer<T> consumer, Class<T> clazz) {
    ofNullable(getLvField(field))
        .filter(Component::isVisible)
        .filter(f -> f.getValue() != null)
        .ifPresent(visibleField -> {
          if (clazz == String.class) {
            consumer.accept(clazz.cast(astr(visibleField.getValue())));
          } else {
            consumer.accept(clazz.cast(visibleField.getValue()));
          }
        });
  }
}
