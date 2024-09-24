/*
 * Copyright 2022 - 2023 Procura B.V.
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

package nl.procura.gba.web.modules.beheer.verkiezing.page2.ongeldigverklaren;

import static java.lang.Long.parseLong;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.INFO;
import static nl.procura.gba.web.modules.beheer.verkiezing.page2.ongeldigverklaren.RedenOngeldigVerklarenContainer.OMSCHRIJVING;
import static nl.procura.gba.web.services.beheer.verkiezing.StempasAanduidingType.AAND_GEEN;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import nl.procura.commons.core.exceptions.ProException;
import nl.procura.gba.jpa.personen.db.KiesrStem;
import nl.procura.gba.jpa.personen.db.KiesrVerk;
import nl.procura.gba.jpa.personen.db.KiesrWijz;
import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.components.layouts.window.GbaModalWindow;
import nl.procura.gba.web.services.beheer.verkiezing.Stempas;
import nl.procura.gba.web.services.beheer.verkiezing.StempasQuery;
import nl.procura.gba.web.windows.home.modules.MainModuleContainer;
import nl.procura.vaadin.annotation.field.Field;
import nl.procura.vaadin.annotation.field.FormFieldFactoryBean;
import nl.procura.vaadin.annotation.field.Select;
import nl.procura.vaadin.component.field.ProTextField;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

import lombok.Data;
import nl.procura.validation.Anr;

public class OngeldigVerklarenWindow extends GbaModalWindow {

  private static final String F_REDEN             = "reden";
  private static final String F_AANTAL_PERSONEN   = "aantalPersonen";
  private static final String F_AANTAL_STEMPASSEN = "aantalStempassen";

  private List<OngeldigeStempas> ongeldigeStempassen = new ArrayList<>();
  private final KiesrVerk        verkiezing;

  public OngeldigVerklarenWindow(KiesrVerk verkiezing) {
    super("Stempassen ongeldig verklaren (Escape om te sluiten)", "800px");
    this.verkiezing = verkiezing;
  }

  @Override
  public void attach() {
    super.attach();
    addComponent(new MainModuleContainer(false, new Page()));
  }

  public class Page extends NormalPageTemplate {

    private Form form;

    public Page() {
      super("");
    }

    @Override
    public void event(PageEvent event) {
      if (event.isEvent(InitPage.class)) {
        form = new Form(() -> buttonSave.setEnabled(false));
        addButton(buttonSearch);
        addButton(buttonSave, 1f);
        addButton(buttonClose);
        addComponent(form);

        buttonSave.setCaption("Verwerken (F9)");
        buttonSave.setEnabled(false);
      }
      super.event(event);
      buttonClose.focus();
    }

    @Override
    public void onClose() {
      getWindow().closeWindow();
    }

    @Override
    public void onSearch() {
      form.commit();
      List<OngeldigeStempas> uitgeslotenPersonen = getUitgeslotenPersonen();
      List<OngeldigeStempas> stempassen = getServices().getKiezersregisterService()
          .getStempassen(StempasQuery
              .builder(verkiezing)
              .aanduidingType(AAND_GEEN)
              .build())
          .getStempassen()
          .stream()
          .map(OngeldigeStempas::new)
          .collect(Collectors.toList());

      ongeldigeStempassen = stempassen.stream()
          .filter(uitgeslotenPersonen::contains)
          .collect(Collectors.toList());

      form.getField(F_AANTAL_PERSONEN).setValue(uitgeslotenPersonen.size());
      form.getField(F_AANTAL_STEMPASSEN).setValue(ongeldigeStempassen.size());
      form.repaint();
      buttonSave.setEnabled(true);
    }

    private List<OngeldigeStempas> getUitgeslotenPersonen() {
      return getServices().getProbevSqlService()
          .find(form.getBean().getReden().getSql(verkiezing))
          .stream().map(res -> new OngeldigeStempas(res[0], res[1], res[2]))
          .collect(Collectors.toList());
    }

    @Override
    public void onSave() {
      if (ongeldigeStempassen.isEmpty()) {
        throw new ProException(INFO, "Er hoeven geen stempassen te worden gewijzigd");
      }
      form.commit();
      RedenOngeldigVerklarenType reden = form.getBean().getReden();
      for (OngeldigeStempas stempas : ongeldigeStempassen) {
        stempas.getStempas().setAanduiding(reden.getAanduidingType());
        KiesrWijz wijziging = getServices().getKiezersregisterService()
            .getNieuweWijziging(stempas.getStempas().getStem(), reden.getActieType(), reden.getLabel(verkiezing));
        getServices().getKiezersregisterService().save(stempas.getStempas().getStem(), wijziging);
      }

      successMessage("De stempassen zijn aangepast");
      buttonSave.setEnabled(false);
    }
  }

  @Data
  public static class OngeldigeStempas {

    private Stempas stempas;
    private long    anr;

    public OngeldigeStempas(KiesrStem kiesrStem) {
      this.anr = kiesrStem.getAnr();
      this.stempas = new Stempas(kiesrStem);
    }

    public OngeldigeStempas(String a1, String a2, String a3) {
      this.anr = new Anr(parseLong(a1), parseLong(a2), parseLong(a3)).getLongAnummer();
    }

    @Override
    public boolean equals(Object p) {
      if (p instanceof OngeldigeStempas) {
        return anr == ((OngeldigeStempas) p).getAnr();
      }
      return false;
    }
  }

  public class Form extends GbaForm<Bean> {

    private final Runnable changeListener;

    public Form(Runnable changeListener) {
      this.changeListener = changeListener;
      setOrder(F_REDEN, F_AANTAL_PERSONEN, F_AANTAL_STEMPASSEN);
      setColumnWidths("160px", "");
      setBean(new Bean());
    }

    @Override
    public void afterSetBean() {
      getField(F_REDEN, GbaNativeSelect.class).setContainerDataSource(new RedenOngeldigVerklarenContainer(verkiezing));
      getField(F_REDEN, GbaNativeSelect.class).addListener((ValueChangeListener) event -> changeListener.run());
    }
  }

  @Data
  @FormFieldFactoryBean(accessType = ElementType.FIELD)
  public static class Bean implements Serializable {

    @Field(customTypeClass = GbaNativeSelect.class,
        caption = "Reden ongeldig verklaren",
        width = "550px",
        required = true)
    @Select(itemCaptionPropertyId = OMSCHRIJVING)
    private RedenOngeldigVerklarenType reden;

    @Field(customTypeClass = ProTextField.class,
        caption = "Aantal personen",
        readOnly = true)
    private Object aantalPersonen = "";

    @Field(customTypeClass = ProTextField.class,
        caption = "Aantal stempassen",
        readOnly = true)
    private Object aantalStempassen = "";
  }
}
