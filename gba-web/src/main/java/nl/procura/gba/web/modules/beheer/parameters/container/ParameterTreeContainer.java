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

package nl.procura.gba.web.modules.beheer.parameters.container;

import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_ALGEMEEN;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_ALGEMENE_INSTELLINGEN;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_APPS;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_BSM;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_CONFIGURATIEBESTAND;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_CONNECT;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_CONTACT;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_COVOG;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_CURATELE;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_DATABASES;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_DIENSTEN;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_DOCUMENTEN;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_EMAIL;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_GEO;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_GPK;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_GV;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_HANDLEIDINGEN;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_INBOX;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_KASSA;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_KENNISBANK;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_MIDOFFICE;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_MIJN_OVERHEID;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_ONDERZOEK;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_ONTBINDING;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_OVERIG;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_PORTAAL;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_PRESENTIEVRAAG;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_PRINT;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_PROBEV;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_PROCURA;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_PROTOCOLLERING;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_RAAS;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_REISDOCUMENTEN;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_RIJBEWIJZEN;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_RISKANALYSIS;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_SMS;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_SYSTEM;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_TERUGMELDINGEN;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_VERHUIZING;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_VERIFICATIEVRAAG;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_VOORRAAD;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_WERKPROCES;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_ZAKEN;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_ZAKEN_ALGEMEEN;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_ZAKEN_BEHANDELEN;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_ZAKEN_DMS;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_ZAKEN_DMS_TYPES;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_ZAKEN_STATUS;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_ZOEKEN;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_ZOEKEN_ALGEMEEN;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_ZOEKEN_PROFIEL_GBAV_PLUS;
import static nl.procura.gba.web.services.beheer.parameter.ParameterGroup.GROUP_ZOEKEN_PROFIEL_STANDAARD;

import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.Collection;

import com.vaadin.data.Item;
import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;

import nl.procura.gba.web.modules.beheer.parameters.bean.ParameterBean;
import nl.procura.gba.web.modules.beheer.parameters.layout.AppInstanceLayout;
import nl.procura.gba.web.modules.beheer.parameters.layout.AvailableDatabasesLayout;
import nl.procura.gba.web.modules.beheer.parameters.layout.ConfigParameterLayout;
import nl.procura.gba.web.modules.beheer.parameters.layout.DatabaseParameterLayout;
import nl.procura.gba.web.modules.beheer.parameters.layout.EmailParameterLayout;
import nl.procura.gba.web.modules.beheer.parameters.layout.GenericParametersLayout;
import nl.procura.gba.web.modules.beheer.parameters.layout.GeoParameterLayout;
import nl.procura.gba.web.modules.beheer.parameters.layout.RaasParameterLayout;
import nl.procura.gba.web.modules.beheer.parameters.layout.ZakenBehandelingParametersLayout;
import nl.procura.gba.web.modules.beheer.parameters.layout.ZakenDmsParametersLayout;
import nl.procura.gba.web.modules.beheer.parameters.layout.ZakenStatusParametersLayout;
import nl.procura.gba.web.modules.beheer.parameters.layout.ZoekenAlgemeenParametersLayout;
import nl.procura.gba.web.modules.beheer.parameters.layout.ZoekenProfielParametersLayout;
import nl.procura.gba.web.services.beheer.gebruiker.Gebruiker;
import nl.procura.gba.web.services.beheer.parameter.ParameterGroup;
import nl.procura.gba.web.services.beheer.parameter.ParameterType;
import nl.procura.gba.web.services.beheer.parameter.annotations.ParameterAnnotation;
import nl.procura.gba.web.services.beheer.profiel.Profiel;
import nl.procura.vaadin.component.container.ProcuraContainer;
import nl.procura.vaadin.component.formfieldfactory.BeanAnnotationUtil;

public class ParameterTreeContainer extends HierarchicalContainer implements ProcuraContainer {

  public static final String LEAF_CLASS = "Waarde";

  private final Gebruiker gebruiker;
  private final Profiel   profiel;

  public ParameterTreeContainer(Gebruiker gebruiker, Profiel profiel) {

    this.gebruiker = gebruiker;
    this.profiel = profiel;

    addContainerProperty(OMSCHRIJVING, String.class, "");
    addContainerProperty(LEAF_CLASS, Class.class, "");
    removeAllItems();

    addTreeItem(GROUP_ALGEMEEN, true, HorizontalLayout.class);
    addTreeItem(GROUP_ALGEMENE_INSTELLINGEN, GROUP_ALGEMEEN, false, GenericParametersLayout.class);
    addTreeItem(GROUP_APPS, GROUP_ALGEMEEN, false, AppInstanceLayout.class);
    addTreeItem(GROUP_DIENSTEN, true, HorizontalLayout.class);
    addTreeItem(GROUP_OVERIG, true, HorizontalLayout.class);
    addTreeItem(GROUP_CONFIGURATIEBESTAND, GROUP_ALGEMEEN, false, ConfigParameterLayout.class);
    addTreeItem(GROUP_DATABASES, GROUP_ALGEMEEN, false, AvailableDatabasesLayout.class);

    // Diensten
    addTreeItem(GROUP_BSM, GROUP_DIENSTEN, false, DatabaseParameterLayout.class);
    addTreeItem(GROUP_COVOG, GROUP_DIENSTEN, false, DatabaseParameterLayout.class);
    addTreeItem(GROUP_CURATELE, GROUP_DIENSTEN, false, DatabaseParameterLayout.class);
    addTreeItem(GROUP_GEO, GROUP_DIENSTEN, false, GeoParameterLayout.class);
    addTreeItem(GROUP_GPK, GROUP_DIENSTEN, false, DatabaseParameterLayout.class);
    addTreeItem(GROUP_GV, GROUP_DIENSTEN, false, DatabaseParameterLayout.class);
    addTreeItem(GROUP_KENNISBANK, GROUP_DIENSTEN, false, DatabaseParameterLayout.class);
    addTreeItem(GROUP_MIDOFFICE, GROUP_DIENSTEN, false, DatabaseParameterLayout.class);
    addTreeItem(GROUP_MIJN_OVERHEID, GROUP_DIENSTEN, false, DatabaseParameterLayout.class);
    addTreeItem(GROUP_ONTBINDING, GROUP_DIENSTEN, false, DatabaseParameterLayout.class);
    addTreeItem(GROUP_ONDERZOEK, GROUP_DIENSTEN, false, DatabaseParameterLayout.class);
    addTreeItem(GROUP_PRESENTIEVRAAG, GROUP_DIENSTEN, false, DatabaseParameterLayout.class);
    addTreeItem(GROUP_RAAS, GROUP_DIENSTEN, false, RaasParameterLayout.class);
    addTreeItem(GROUP_REISDOCUMENTEN, GROUP_DIENSTEN, false, DatabaseParameterLayout.class);
    addTreeItem(GROUP_RIJBEWIJZEN, GROUP_DIENSTEN, false, DatabaseParameterLayout.class);
    addTreeItem(GROUP_RISKANALYSIS, GROUP_DIENSTEN, false, DatabaseParameterLayout.class);
    addTreeItem(GROUP_SMS, GROUP_DIENSTEN, false, DatabaseParameterLayout.class);
    addTreeItem(GROUP_TERUGMELDINGEN, GROUP_DIENSTEN, false, DatabaseParameterLayout.class);
    addTreeItem(GROUP_VERIFICATIEVRAAG, GROUP_DIENSTEN, false, DatabaseParameterLayout.class);
    addTreeItem(GROUP_VERHUIZING, GROUP_DIENSTEN, false, DatabaseParameterLayout.class);
    addTreeItem(GROUP_INBOX, GROUP_DIENSTEN, false, DatabaseParameterLayout.class);
    addTreeItem(GROUP_ZAKEN, GROUP_DIENSTEN, true, DatabaseParameterLayout.class);
    addTreeItem(GROUP_ZAKEN_ALGEMEEN, GROUP_ZAKEN, false, ZakenStatusParametersLayout.class);
    addTreeItem(GROUP_ZAKEN_STATUS, GROUP_ZAKEN, false, ZakenStatusParametersLayout.class);
    addTreeItem(GROUP_ZAKEN_BEHANDELEN, GROUP_ZAKEN, false, ZakenBehandelingParametersLayout.class);
    addTreeItem(GROUP_ZAKEN_DMS, GROUP_ZAKEN, false, DatabaseParameterLayout.class);
    addTreeItem(GROUP_ZAKEN_DMS_TYPES, GROUP_ZAKEN, false, ZakenDmsParametersLayout.class);
    addTreeItem(GROUP_ZOEKEN, GROUP_DIENSTEN, true, DatabaseParameterLayout.class);
    addTreeItem(GROUP_ZOEKEN_ALGEMEEN, GROUP_ZOEKEN, false, ZoekenAlgemeenParametersLayout.class);
    addTreeItem(GROUP_ZOEKEN_PROFIEL_STANDAARD, GROUP_ZOEKEN, false, ZoekenProfielParametersLayout.class);
    addTreeItem(GROUP_ZOEKEN_PROFIEL_GBAV_PLUS, GROUP_ZOEKEN, false, ZoekenProfielParametersLayout.class);

    // Overige
    addTreeItem(GROUP_PRINT, GROUP_OVERIG, false, DatabaseParameterLayout.class);
    addTreeItem(GROUP_CONNECT, GROUP_OVERIG, false, DatabaseParameterLayout.class);
    addTreeItem(GROUP_CONTACT, GROUP_OVERIG, false, DatabaseParameterLayout.class);
    addTreeItem(GROUP_DOCUMENTEN, GROUP_OVERIG, false, DatabaseParameterLayout.class);
    addTreeItem(GROUP_EMAIL, GROUP_OVERIG, false, EmailParameterLayout.class);
    addTreeItem(GROUP_HANDLEIDINGEN, GROUP_OVERIG, false, DatabaseParameterLayout.class);
    addTreeItem(GROUP_KASSA, GROUP_OVERIG, false, DatabaseParameterLayout.class);
    addTreeItem(GROUP_PROBEV, GROUP_OVERIG, false, DatabaseParameterLayout.class);
    addTreeItem(GROUP_PROCURA, GROUP_OVERIG, false, DatabaseParameterLayout.class);
    addTreeItem(GROUP_PORTAAL, GROUP_OVERIG, false, DatabaseParameterLayout.class);
    addTreeItem(GROUP_SYSTEM, GROUP_OVERIG, false, DatabaseParameterLayout.class);
    addTreeItem(GROUP_PROTOCOLLERING, GROUP_OVERIG, false, DatabaseParameterLayout.class);
    addTreeItem(GROUP_VOORRAAD, GROUP_OVERIG, false, DatabaseParameterLayout.class);
    addTreeItem(GROUP_WERKPROCES, GROUP_OVERIG, false, DatabaseParameterLayout.class);

    removeEmptyLeafs();
  }

  public boolean isGebruiker() {
    return gebruiker != null;
  }

  public boolean isProfiel() {
    return profiel != null;
  }

  private void addTreeItem(ParameterGroup itemId, boolean childrenAllowed,
      Class<? extends Component> componentClass) {

    boolean isApp = (!isGebruiker() && !isProfiel());
    boolean isParmLayout = DatabaseParameterLayout.class.isAssignableFrom(componentClass);

    // Niet parameters layout tonen in algemene parameters
    if (!childrenAllowed && !isParmLayout && !isApp) {
      return;
    }

    // Parameter layout niet tonen als er geen parameters zijn
    if (!childrenAllowed && isParmLayout && !hasParameter(ParameterBean.class, itemId, isGebruiker(), isProfiel())) {
      return;
    }

    Item item = addItem(itemId);
    setChildrenAllowed(itemId, childrenAllowed);
    item.getItemProperty(OMSCHRIJVING).setValue(itemId.getCaption());
    item.getItemProperty(LEAF_CLASS).setValue(componentClass);
  }

  private void addTreeItem(ParameterGroup itemId, ParameterGroup parentId, boolean childrenAllowed,
      Class<? extends Component> componentClass) {

    addTreeItem(itemId, childrenAllowed, componentClass);
    setParent(itemId, parentId);
  }

  /**
   * De categorie heeft een (gebruiker) parameter.
   */
  private boolean hasParameter(Class<?> beanClass, ParameterGroup category, boolean showUser, boolean showProfile) {
    Collection<?> propertyIds = BeanAnnotationUtil.getPropertyIdsForAnnotatedFields(beanClass);
    for (Object propertyId : propertyIds) {
      if (propertyId instanceof String) {
        try {
          AnnotatedElement element = BeanAnnotationUtil.getMember(beanClass, propertyId);
          if (element.isAnnotationPresent(ParameterAnnotation.class)) {
            ParameterAnnotation annotation = element.getAnnotation(ParameterAnnotation.class);
            if (isAnnotationCorrect(annotation, category, showUser, showProfile)) {
              return true;
            }
          }
        } catch (SecurityException | NoSuchFieldException | NoSuchMethodException e) {
          e.printStackTrace();
        }
      }
    }

    return false;
  }

  private boolean isAnnotationCorrect(ParameterAnnotation annotation, ParameterGroup category,
      boolean showUser, boolean showProfile) {

    ParameterType type = annotation.value();
    boolean isCategory = (type.getCategory() != null) && type.getCategory() == category;
    boolean isUserParameter = showUser && type.isShowUser();
    boolean isProfileParameter = showProfile && type.isShowProfile();
    boolean isAppParameter = !showUser && !showProfile && type.isShowApplication();
    return isCategory && (isAppParameter || isProfileParameter || isUserParameter);
  }

  /**
   * Opschonen lege categorieen
   */
  private void removeEmptyLeafs() {
    for (Object itemId : new ArrayList<>(getItemIds())) {
      if (!hasChildren(itemId) && areChildrenAllowed(itemId)) {
        removeItem(itemId);
      }
    }
  }
}
