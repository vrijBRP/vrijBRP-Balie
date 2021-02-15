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

package nl.procura.gba.web.modules.beheer.kassa.page2;

import static nl.procura.gba.web.services.beheer.kassa.KassaType.*;
import static nl.procura.standard.exceptions.ProExceptionType.UNKNOWN;

import nl.procura.gba.web.components.layouts.GbaVerticalLayout;
import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.services.beheer.kassa.KassaProduct;
import nl.procura.gba.web.services.beheer.kassa.KassaType;
import nl.procura.gba.web.services.zaken.documenten.DocumentRecord;
import nl.procura.gba.web.services.zaken.reisdocumenten.SoortReisdocument;
import nl.procura.gba.web.services.zaken.rijbewijs.RijbewijsAanvraagSoort;
import nl.procura.standard.exceptions.ProException;
import nl.procura.vaadin.component.layout.Fieldset;

public class Page2Kassa extends NormalPageTemplate {

  private final Form         form;
  private KassaProduct       kassaProduct = null;
  private final BundelLayout bundelLayout;

  public Page2Kassa(KassaProduct kassaProduct) {

    super("Toevoegen / muteren kassa");
    setKassaProduct(kassaProduct);

    addButton(buttonPrev, buttonNew, buttonSave);

    form = new Form(kassaProduct);
    addComponent(form);

    bundelLayout = new BundelLayout();
    addExpandComponent(bundelLayout);
  }

  public KassaProduct getKassaProduct() {
    return kassaProduct;
  }

  public void setKassaProduct(KassaProduct kassaProduct) {
    this.kassaProduct = kassaProduct;
  }

  @Override
  public void onNew() {

    form.reset();

    kassaProduct = new KassaProduct();
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }

  @Override
  public void onSave() {

    form.commit();

    try {
      Page2KassaBean b = form.getBean();

      getKassaProduct().setKassa(b.getKassa());
      getKassaProduct().setKassaType(b.getKassaType());
      getKassaProduct().setAnders("");
      getKassaProduct().setProductgroep("");
      getKassaProduct().setKassaDocument(DocumentRecord.getDefault());
      getKassaProduct().setKassaReisdocument(SoortReisdocument.getDefault());
      getKassaProduct().setKassaRijbewijs(RijbewijsAanvraagSoort.ONBEKEND);

      if (REISDOCUMENT.equals(b.getKassaType())) {
        getKassaProduct().setKassaReisdocument(b.getReisdocument());
      } else if (RIJBEWIJS.equals(b.getKassaType())) {
        getKassaProduct().setKassaRijbewijs(b.getRijbewijs());
      }
      if (ANDERS.equals(b.getKassaType())) {
        getKassaProduct().setAnders(b.getAnders());
        getKassaProduct().setProductgroep(b.getProductgroep());
        getKassaProduct().setKassaBundel(b.getBundel());
      } else if (isDocType(b.getKassaType())) {
        getKassaProduct().setKassaDocument(b.getDocument());
        getKassaProduct().setKassaBundel(false);
      }

      getServices().getKassaService().save(getKassaProduct());
      successMessage("Kassaprodukt is opgeslagen.");

      form.checkBundel();
    } catch (Exception e) {
      throw new ProException(UNKNOWN, "Fout bij opslaan kassaproduct.", e);
    }
  }

  private boolean isDocType(KassaType kassaType) {
    return kassaType.is(UITTREKSEL);
  }

  public class BundelLayout extends GbaVerticalLayout {

    private final Page2KassaTable table;

    public BundelLayout() {

      table = new Page2KassaTable();

      addComponent(new Fieldset("De kassaproducten"));
      addComponent(new Page2KassaTableFilter(table));
      addExpandComponent(table);
    }

    public void update(Boolean bundel, KassaProduct kassaProduct) {

      setVisible(bundel);

      if (bundel) {
        table.init(kassaProduct);
      }
    }
  }

  public class Form extends Page2KassaForm {

    public Form(KassaProduct kassa) {

      super(kassa);
    }

    @Override
    protected void checkBundel(Boolean bundel) {

      bundelLayout.update(bundel, kassaProduct);

      super.checkBundel(bundel);
    }
  }
}
