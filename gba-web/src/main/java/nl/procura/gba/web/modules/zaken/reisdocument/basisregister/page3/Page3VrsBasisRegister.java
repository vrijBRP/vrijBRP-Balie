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

package nl.procura.gba.web.modules.zaken.reisdocument.basisregister.page3;

import com.vaadin.ui.VerticalLayout;
import java.io.Serializable;
import java.lang.annotation.ElementType;
import lombok.Data;
import nl.procura.burgerzaken.vrsclient.api.VrsActieType;
import nl.procura.burgerzaken.vrsclient.api.VrsMeldingRedenType;
import nl.procura.burgerzaken.vrsclient.api.VrsMeldingType;
import nl.procura.gba.common.DateTime;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.modules.zaken.reisdocument.basisregister.page4.Page4VrsBasisRegister;
import nl.procura.gba.web.services.zaken.inhoudingen.DocumentInhouding;
import nl.procura.gba.web.services.zaken.inhoudingen.InhoudingType;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.Field.FieldType;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.component.layout.Fieldset;
import nl.procura.vaadin.component.layout.info.InfoLayout;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;
import nl.procura.vaadin.theme.twee.ProcuraTheme;

public class Page3VrsBasisRegister extends NormalPageTemplate {

  private       Page3VrsBasisRegisterForm1 form1;
  private       Page3VrsBasisRegisterForm2 form2;
  private final VrsActieType               actie;
  private final DocumentInhouding          inhouding;
  private ConfirmationLayout confirmationLayout;

  public Page3VrsBasisRegister(VrsActieType actie, DocumentInhouding inhouding) {
    this.actie = actie;
    this.inhouding = inhouding;
    addButton(buttonPrev, 1F);
    addButton(buttonNext);
  }

  @Override
  public void event(PageEvent event) {
    if (event.isEvent(InitPage.class)) {
      form1 = new Page3VrsBasisRegisterForm1(actie, inhouding, inhoudingType -> form2.updateBrpActie(inhoudingType));
      form2 = new Page3VrsBasisRegisterForm2(inhouding);
      confirmationLayout = new ConfirmationLayout();
      confirmationLayout.setVisible(false);

      addComponent(form1);
      addComponent(form2);
      addComponent(confirmationLayout);
      super.event(event);
    }
    getWindow().setWidth("800px");
  }


  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }

  @Override
  public void onNextPage() {
    form1.commit();
    confirmationLayout.setVisible(true);
    confirmationLayout.commit();
    continueNext();
  }

  private void continueNext() {
    DateTime dateTime = new DateTime();
    inhouding.setVrsDIn(dateTime.getLongDate());
    inhouding.setVrsTIn(dateTime.getLongTime());
    inhouding.setVrsDmelding(form1.getBean().getDatumRedenMelding().getLongValue());
    inhouding.setVrsMeldingType(form1.getBean().getMeldingType().getCode());
    inhouding.setVrsDinlever(-1L);
    inhouding.setVrsRedenType(form1.getBean().getRedenType().getCode());
    getServices().getDocumentInhoudingenService().registreerMelding(inhouding);

    String message = "De melding is geregistreerd.";

    if (Boolean.TRUE.equals(form1.getBean().getIngeleverd())) {
      inhouding.setVrsMeldingType(VrsMeldingType.RDO.getCode());
      inhouding.setVrsRedenType(VrsMeldingRedenType.REBO.getCode());
      inhouding.setInhoudingType(InhoudingType.INHOUDING);
      inhouding.setVrsDinlever(form1.getBean().getDatumInlevering().getLongValue());
      getServices().getDocumentInhoudingenService().registreerMelding(inhouding);
      message += " Het document is ingeleverd.";
    }

    getServices().getDocumentInhoudingenService().save(inhouding);
    successMessage(message);
    getNavigation().removeOtherPages();
    getNavigation().goToPage(new Page4VrsBasisRegister(inhouding));
    super.onNextPage();
  }

  private static class ConfirmationLayout extends VerticalLayout {

    private final Form form = new Form();

    public ConfirmationLayout() {
      setSpacing(true);
      InfoLayout infoLayout = new InfoLayout(null, ProcuraTheme.ICOON_24.QUESTION,
          "De registratie van een melding kan een statusovergang van het reisdocument veroorzaken, "
              + "welke niet ongedaan gemaakt kan worden. <br/>Controleer de ingevoerde meldinggegevens voordat u verder gaat."
              + "<p><b>Wilt u de melding registreren?</b></p>");

      addComponent(new Fieldset("Bevestiging"));
      addComponent(infoLayout);
      addComponent(form);
    }

    public void commit() {
      form.commit();
    }

    public static class Form extends GbaForm<Page3VrsBasisRegisterBean1> {

      public Form() {
        setColumnWidths("200px", "");
        setBean(new Bean());
      }
    }

    @Data
    @FormFieldFactoryBean(accessType = ElementType.FIELD)
    public static class Bean implements Serializable {

      @Field(type = FieldType.CHECK_BOX,
          caption = "Ja, ik wil de melding registreren",
          required = true)
      private Boolean bevestiging = null;
    }
  }
}
