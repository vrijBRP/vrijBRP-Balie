/*
 * Copyright 2024 - 2025 Procura B.V.
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

package nl.procura.gba.web.components.layouts.form.document.sign;

import static nl.procura.gba.web.components.layouts.form.document.sign.DocSignBean.DOCUMENT;
import static nl.procura.gba.web.components.layouts.form.document.sign.DocSignBean.LANGUAGE;
import static nl.procura.gba.web.components.layouts.form.document.sign.DocSignBean.PERSON;
import static nl.procura.gba.web.components.layouts.form.document.sign.DocSignBean.PERSON_EMAIL;
import static nl.procura.gba.web.services.zaken.contact.ContactgegevensService.EMAIL;
import static org.apache.commons.lang3.StringUtils.defaultIfBlank;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.vaadin.ui.Button;
import com.vaadin.ui.Field;

import nl.procura.commons.core.exceptions.ProException;
import nl.procura.diensten.gba.ple.extensions.BasePLExt;
import nl.procura.diensten.gba.ple.extensions.Cat1PersoonExt;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.components.layouts.form.document.sign.DocSignBean.PersonValidator;
import nl.procura.gba.web.modules.zaken.contact.ContactWindow;
import nl.procura.gba.web.services.zaken.contact.Contact;
import nl.procura.vaadin.component.field.ProNativeSelect;
import nl.procura.vaadin.component.layout.table.TableLayout.Column;
import nl.procura.vaadin.functies.VaadinUtils;

import lombok.Data;

public class DocSignForm extends GbaForm<DocSignBean> {

  private final String          document;
  private final List<BasePLExt> persons;

  public DocSignForm(String document, List<BasePLExt> persons) {
    this.document = document;
    this.persons = persons;
    setReadonlyAsText(false);
    setOrder(DOCUMENT, LANGUAGE, PERSON, PERSON_EMAIL);
    setColumnWidths("150px", "");
    setBean(new DocSignBean());
  }

  @Override
  public void attach() {
    super.attach();
    DocSignBean bean = new DocSignBean();
    bean.setDocument(document);
    bean.setLanguage(DocSignInterfaceLanguage.NL);
    setBean(bean);
    setContainers();
  }

  @Override
  public void afterSetColumn(Column column, Field field, Property property) {
    if (property.is(PERSON)) {
      column.addComponent(new Button("Contactgegevens", event -> onChangeEmail()));
    }
    if (property.is(PERSON_EMAIL)) {
      field.addValidator(new PersonValidator(new Supplier<SignPerson>() {

        @Override
        public SignPerson get() {
          return (SignPerson) getField(PERSON, ProNativeSelect.class).getValue();
        }
      }));
    }
  }

  private void onChangeEmail() {
    DocSignWindow docSignWindow = VaadinUtils.getParent(this, DocSignWindow.class);
    SignPerson person = (SignPerson) getField(PERSON, ProNativeSelect.class).getValue();
    if (person != null) {
      Cat1PersoonExt persoon = person.getPl().getPersoon();
      final Contact contact = new Contact(person.getName(), persoon.getAnr().toLong(), persoon.getBsn().toLong());
      ContactWindow window = new ContactWindow(contact, null) {

        @Override
        public void closeWindow() {
          setContainers();
          super.closeWindow();
          VaadinUtils.resetHeight(docSignWindow);
        }
      };
      getApplication().getParentWindow().addWindow(window);
    } else {
      throw new ProException("Geen ondertekenaar geselecteerd");
    }
  }

  private void setContainers() {
    ProNativeSelect field = getField(PERSON, ProNativeSelect.class);
    List<SignPerson> list = persons.stream()
        .map(this::toSignPerson)
        .collect(Collectors.toList());
    DocSignPersonContainer container = new DocSignPersonContainer(list);
    field.setContainerDataSource(container);
    if (list.size() == 1) {
      field.setValue(list.get(0));
    }
  }

  private SignPerson toSignPerson(BasePLExt pl) {
    SignPerson person = new SignPerson();
    person.setPl(pl);
    person.setName(pl.getPersoon().getNaam().getNaamNaamgebruikEersteVoornaam());
    person.setEmail(getApplication().getServices().getContactgegevensService().getContactWaarde(pl, EMAIL));
    return person;
  }

  @Data
  public static class SignPerson {

    private BasePLExt pl;
    private String    name;
    private String    email;

    @Override
    public String toString() {
      return name + " (" + defaultIfBlank(email, "geen e-mailadres") + ")";
    }
  }
}
