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

package nl.procura.gba.web.modules.zaken.kassa.page2;

import static nl.procura.gba.web.modules.zaken.kassa.page2.Page2KassaBean2.*;
import static nl.procura.standard.Globalfunctions.astr;
import static nl.procura.standard.Globalfunctions.fil;

import java.util.ArrayList;
import java.util.List;

import nl.procura.gba.web.components.fields.GbaNativeSelect;
import nl.procura.gba.web.components.layouts.form.GbaForm;
import nl.procura.gba.web.services.beheer.kassa.KassaProduct;
import nl.procura.gba.web.services.beheer.kassa.KassaType;
import nl.procura.vaadin.component.container.ArrayListContainer;

public class Page2KassaForm2 extends GbaForm<Page2KassaBean2> {

  private List<KassaProduct> kassaProducten = new ArrayList<>();

  public Page2KassaForm2() {

    setCaption("Selecteer het nieuwe product");
    setOrder(KASSATYPE, DOCUMENT, REISDOCUMENT, RIJBEWIJS, PRODUCTGROEP, PRODUCT);
    setColumnWidths("100px", "");

    initFields();
  }

  @Override
  public void attach() {

    GbaNativeSelect t = ((GbaNativeSelect) getField(KASSATYPE));
    t.setDataSource(new TypeContainer());

    GbaNativeSelect d = ((GbaNativeSelect) getField(DOCUMENT));
    d.setDataSource(new DocumentContainer());

    GbaNativeSelect rd = ((GbaNativeSelect) getField(REISDOCUMENT));
    rd.setDataSource(new ReisdocumentContainer());

    GbaNativeSelect rb = ((GbaNativeSelect) getField(RIJBEWIJS));
    rb.setDataSource(new RijbewijsContainer());

    GbaNativeSelect pg = ((GbaNativeSelect) getField(PRODUCTGROEP));
    pg.setDataSource(new ProductgroepContainer());

    super.attach();
  }

  public KassaProduct getKassaProduct() {

    commit();

    Page2KassaBean2 b = getBean();

    for (KassaProduct kp : getKassaProducten()) {

      if (kp.getKassaType().is(b.getKassaType())) {

        if (KassaType.UITTREKSEL.is(kp.getKassaType())) {
          if (kp.getKassaDocument().getCDocument().equals(b.getDocument().getCDocument())) {
            return kp;
          }
        } else if (KassaType.RIJBEWIJS.is(kp.getKassaType())) {
          if (kp.getKassaRijbewijs() == b.getRijbewijs()) {
            return kp;
          }
        } else if (KassaType.REISDOCUMENT.is(kp.getKassaType())) {
          if (kp.getKassaReisdocument().getCReisdoc().equals(b.getReisdocument().getCReisdoc())) {
            return kp;
          }
        } else if (KassaType.ANDERS.is(kp.getKassaType())) {

          boolean isProductgroep = kp.getProductgroep().equalsIgnoreCase(b.getProductgroep());
          boolean isProduct = kp.getAnders().equalsIgnoreCase(b.getProduct());

          if (isProductgroep && isProduct) {
            return kp;
          }
        } else {
          return kp;
        }
      }
    }

    return null;
  }

  public List<KassaProduct> getKassaProducten() {

    if (kassaProducten.isEmpty()) {
      if (getApplication() != null) {
        kassaProducten = getApplication().getServices().getKassaService().getKassaProducten();
      }
    }

    return kassaProducten;
  }

  public void setKassaProducten(List<KassaProduct> kassaProducten) {
    this.kassaProducten = kassaProducten;
  }

  @Override
  public Page2KassaBean2 getNewBean() {
    return new Page2KassaBean2();
  }

  @Override
  public void reset() {
    super.reset();
    initFields();
  }

  private void initFields() {

    setBean(new Page2KassaBean2());

    ValueChangeListener changeListener = (ValueChangeListener) event -> updateFields();
    getField(KASSATYPE).addListener(changeListener);
    getField(PRODUCTGROEP).addListener(changeListener);

    updateFields();
  }

  private void updateFields() {

    KassaType t = (KassaType) getField(KASSATYPE).getValue();

    getField(DOCUMENT).setVisible(KassaType.UITTREKSEL.is(t));
    getField(REISDOCUMENT).setVisible(KassaType.REISDOCUMENT.is(t));
    getField(RIJBEWIJS).setVisible(KassaType.RIJBEWIJS.is(t));
    getField(PRODUCTGROEP).setVisible(KassaType.ANDERS.is(t));
    getField(PRODUCT).setVisible(KassaType.ANDERS.is(t));

    GbaNativeSelect product = (GbaNativeSelect) getField(PRODUCT);
    product.setContainerDataSource(new ProductContainer(astr(getField(PRODUCTGROEP).getValue())));

    repaint();
  }

  public class DocumentContainer extends ArrayListContainer {

    public DocumentContainer() {

      for (KassaProduct kp : getKassaProducten()) {
        if (KassaType.UITTREKSEL.is(kp.getKassaType())) {
          addItem(kp.getKassaDocument());
        }
      }

      sort();
    }
  }

  public class ProductContainer extends ArrayListContainer {

    private ProductContainer(String productgroep) {

      for (KassaProduct kp : getKassaProducten()) {
        if (KassaType.ANDERS.is(kp.getKassaType()) && astr(kp.getProductgroep()).equalsIgnoreCase(
            productgroep)) {
          if (fil(kp.getAnders())) {
            addItem(kp.getAnders());
          }
        }
      }

      sort();
    }
  }

  public class ProductgroepContainer extends ArrayListContainer {

    public ProductgroepContainer() {
      for (KassaProduct kp : getKassaProducten()) {
        if (KassaType.ANDERS.is(kp.getKassaType())) {
          if (fil(kp.getProductgroep())) {
            addItem(kp.getProductgroep());
          }
        }
      }

      sort();
    }
  }

  public class ReisdocumentContainer extends ArrayListContainer {

    public ReisdocumentContainer() {

      for (KassaProduct kp : getKassaProducten()) {
        if (KassaType.REISDOCUMENT.is(kp.getKassaType())) {
          addItem(kp.getKassaReisdocument());
        }
      }
    }
  }

  public class RijbewijsContainer extends ArrayListContainer {

    public RijbewijsContainer() {

      for (KassaProduct kp : getKassaProducten()) {
        if (KassaType.RIJBEWIJS.is(kp.getKassaType())) {
          addItem(kp.getKassaRijbewijs());
        }
      }
    }
  }

  public class TypeContainer extends ArrayListContainer {

    public TypeContainer() {

      for (KassaProduct kp : getKassaProducten()) {
        addItem(kp.getKassaType());
      }

      sort();
    }
  }
}
