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

package nl.procura.gba.web.modules.beheer.profielen.components;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import nl.procura.gba.common.Serializer;
import nl.procura.gba.jpa.personen.dao.GenericDao;
import nl.procura.gba.jpa.personen.db.*;
import nl.procura.gba.jpa.personen.utils.GbaDaoUtils;
import nl.procura.gba.jpa.personen.utils.GbaJpa;
import nl.procura.gba.web.common.misc.ImportExportHandler;
import nl.procura.gba.web.modules.beheer.profielen.components.ProfielExport.P;
import nl.procura.gba.web.services.beheer.parameter.Parameter;
import nl.procura.gba.web.services.beheer.profiel.Profiel;
import nl.procura.gba.web.services.beheer.profiel.actie.Actie;
import nl.procura.gba.web.services.beheer.profiel.gba_categorie.PleCategorie;
import nl.procura.gba.web.services.beheer.profiel.gba_element.PleElement;
import nl.procura.gba.web.services.beheer.profiel.veld.Veld;
import nl.procura.gba.web.windows.GbaWindow;
import nl.procura.java.reflection.ReflectionUtil;
import nl.procura.standard.ProcuraDate;
import nl.procura.vaadin.functies.downloading.DownloadHandlerImpl;

public class ProfielImportExportHandler extends ImportExportHandler {

  /**
   * Importeert profielen vanuit een zip-bestand
   */
  public static List<String> importProfielen(final ImportProfielArgumenten args) {

    final List<String> meldingen = new ArrayList<>();

    readZip(args.getBestand(), (zis, entry, dir, name) -> {

      ProfielExport im = Serializer.deSerialize(zis, ProfielExport.class);

      EntityManager em = GbaJpa.getManager();

      for (P p : im.getpList()) {

        em.getTransaction().begin();

        Profiel profiel = new Profiel();
        profiel.setProfiel(p.getProfiel());
        profiel.setOmschrijving(
            p.getOms() + " (import " + new ProcuraDate().getFormatDate("dd-MM-yyyy - HH:mm") + ")");

        meldingen.add("Importeren profiel: " + profiel.getProfiel() + " - " + profiel.getOmschrijving());

        GenericDao.saveEntity(profiel);

        if (args.isImportGebruikers()) {
          addUsers(em, p, profiel);
          meldingen.add("Gebruikers toegevoegd: " + p.getUsernames().size());
        } else {
          meldingen.add("Geen gebruikers toegevoegd");
        }

        if (args.isImportActies()) {
          addActions(em, p, profiel);
          meldingen.add("Acties toegevoegd: " + p.getActies().size());
        } else {
          meldingen.add("Geen acties toegevoegd");
        }

        if (args.isImportParameters()) {
          addParameters(em, p, profiel);
          meldingen.add("Parameters toegevoegd: " + p.getActies().size());
        } else {
          meldingen.add("Geen parameters toegevoegd");
        }

        if (args.isImportVelden()) {
          addFields(em, p, profiel);
          meldingen.add("Velden toegevoegd: " + p.getVelden().size());
        } else {
          meldingen.add("Geen velden toegevoegd");
        }

        if (args.isImportElementen()) {
          addElements(em, p, profiel);
          meldingen.add("Elementen toegevoegd: " + p.getElementen().size());
        } else {
          meldingen.add("Geen elementen toegevoegd");
        }

        if (args.isImportCategorieen()) {
          addCategories(em, p, profiel);
          meldingen.add("Categorieën toegevoegd: " + p.getElementen().size());
        } else {
          meldingen.add("Geen categorieën toegevoegd");
        }

        GenericDao.saveEntity(profiel);

        em.getTransaction().commit();
      }
    });

    return meldingen;
  }

  private static void addActions(EntityManager em, P p, Profiel profiel) {

    for (Actie a : p.getActies()) {

      Action example = new Action();
      example.setAction(a.getAction());
      example.setActionType(a.getType().getType());

      Action action = findOrAdd(example);
      action.getProfiles().add(em.find(Profile.class, profiel.getCProfile()));
      profiel.getActions().add(action);
      em.merge(action);
    }
  }

  private static void addCategories(EntityManager em, P p, Profiel profiel) {

    for (PleCategorie e : p.getCategorieen()) {

      GbaCategory example = new GbaCategory();
      example.setCategory(e.getCategory());

      GbaCategory category = findOrAdd(example);
      category.getProfiles().add(em.find(Profile.class, profiel.getCProfile()));
      profiel.getGbaCategories().add(category);
      em.merge(category);
    }
  }

  private static void addElements(EntityManager em, P p, Profiel profiel) {

    for (PleElement e : p.getElementen()) {

      GbaElement example = new GbaElement();
      example.setElement(e.getElement());
      example.setCategory(e.getCategory());

      GbaElement element = findOrAdd(example);
      element.getProfiles().add(em.find(Profile.class, profiel.getCProfile()));
      profiel.getGbaElements().add(element);
      em.merge(element);
    }
  }

  private static void addFields(EntityManager em, P p, Profiel profiel) {

    for (Veld v : p.getVelden()) {

      Field example = new Field();
      example.setField(v.getField());
      example.setFieldType(v.getType().getType());

      Field field = findOrAdd(example);
      field.getProfiles().add(em.find(Profile.class, profiel.getCProfile()));
      profiel.getFields().add(field);
      em.merge(field);
    }
  }

  private static void addParameters(EntityManager em, P p, Profiel profiel) {

    for (Parameter a : p.getParameters()) {

      Parm example = new Parm();
      example.setParm(a.getParm());
      example.setValue(a.getValue());
      example.setProfile(em.find(Profile.class, profiel.getCProfile()));
      example.setUsr(em.find(Usr.class, 0L));

      Parm action = findOrAdd(example);
      profiel.getParms().add(action);
      em.merge(action);
    }
  }

  private static void addUsers(EntityManager em, P p, Profiel profiel) {

    for (String u : p.getUsernames()) {

      Usr example = new Usr();
      example.setUsr(u);

      for (Usr usr : GenericDao.findByExample(example)) {
        usr.getProfiles().add(em.find(Profile.class, profiel.getCProfile()));
        profiel.getUsrs().add(usr);
        em.merge(usr);
      }
    }
  }

  /**
   * Zoek basis entity op of voeg toe als deze nog niet bestaat.
   */
  @SuppressWarnings("unchecked")
  private static <T> T findOrAdd(T object) {

    for (Object entity : GenericDao.findByExample(GbaDaoUtils.getEntity(object))) {
      return (T) ReflectionUtil.deepCopyBean(object.getClass(), entity);
    }

    GenericDao.saveEntity(object);
    return object;
  }

  /**
   * Exporteert de profielen naar een zip-bestand
   */
  public void exportProfielen(GbaWindow window, ArrayList<Profiel> profielen) {

    ProfielExport export = new ProfielExport();

    for (Profiel profiel : profielen) {

      window.getGbaApplication().getServices().getProfielService().herlaadProfiel(profiel);

      Profiel profielImpl = profiel;

      P p = export.addP(profiel.getProfiel(), profiel.getOmschrijving());

      for (Usr u : profielImpl.getUsrs()) {
        p.getUsernames().add(u.getUsr());
      }

      p.setParameters(profielImpl.getParameters().getAlle());
      p.setActies(profielImpl.getActies().getAlle());
      p.setVelden(profielImpl.getVelden().getAlle());
      p.setElementen(profielImpl.getElementen().getAlle());
      p.setCategorieen(profielImpl.getCategorieen().getAlle());
    }

    ByteArrayOutputStream bos = new ByteArrayOutputStream();

    Serializer.serialize(bos, export);

    Map<String, byte[]> map = new HashMap<>();

    map.put("inhoud.ser", bos.toByteArray());

    exportObject(map, "profielen.zip", new DownloadHandlerImpl(window));
  }
}
