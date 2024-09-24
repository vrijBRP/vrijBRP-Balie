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

package nl.procura.gba.web.modules.beheer.logbestanden.page2;

import static nl.procura.gba.web.modules.beheer.logbestanden.page2.RegelSelectieBean.REGELSELECTIE;
import static nl.procura.gba.web.modules.beheer.logbestanden.page2.RegelSelectieBean.ZOEKINFILE;
import static nl.procura.standard.Globalfunctions.fil;
import static nl.procura.commons.core.exceptions.ProExceptionSeverity.WARNING;
import static nl.procura.commons.core.exceptions.ProExceptionType.DOCUMENTS;
import static nl.procura.commons.core.exceptions.ProExceptionType.ENTRY;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.ui.AbstractTextField.TextChangeEventMode;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.TextField;

import nl.procura.gba.web.components.layouts.page.NormalPageTemplate;
import nl.procura.gba.web.modules.beheer.logbestanden.page2.RegelSelectieContainer.RegelSelectie;
import nl.procura.commons.core.exceptions.ProException;
import nl.procura.vaadin.component.layout.page.pageEvents.InitPage;
import nl.procura.vaadin.component.layout.page.pageEvents.PageEvent;

public class Page2Logbestanden extends NormalPageTemplate {

  private final String[]    bcolors       = { "red", "green", "blue", "black" };
  private final String[]    fcolors       = { "white", "white", "white", "white" };
  private RegelSelectieForm form          = null;
  private CustomLayout      cl            = null;
  private List<String>      completeFile  = new ArrayList<>();
  private final File        file;
  private String            zoekString    = null;
  private int               searchResults = 0;

  public Page2Logbestanden(File file) {

    super("Logbestand");
    addButton(buttonPrev);
    this.file = file;

  }

  @Override
  public void event(PageEvent event) {

    if (event.isEvent(InitPage.class)) {

      setForm(new RegelSelectieForm());
      addComponent(getForm());
      readFile(file);

      showFile();

      getForm().getField(REGELSELECTIE).addListener((ValueChangeListener) event12 -> showFile());

      TextField zoekVeld = (TextField) getForm().getField(ZOEKINFILE);
      zoekVeld.setTextChangeEventMode(TextChangeEventMode.LAZY);
      zoekVeld.setTextChangeTimeout(200);
      zoekVeld.addListener((TextChangeListener) event1 -> {

        setZoekString(event1.getText());

        checkZoekString();

        showFile();

        getForm().getResultaatLabel().setValue(fil(getZoekString()) ? geefMelding(searchResults) : "");
      });
    }

    super.event(event);
  }

  public CustomLayout getCl() {
    return cl;
  }

  public void setCl(CustomLayout cl) {
    this.cl = cl;
  }

  public List<String> getCompleteFile() {
    return completeFile;
  }

  public void setCompleteFile(List<String> completeFile) {
    this.completeFile = completeFile;
  }

  public RegelSelectieForm getForm() {
    return form;
  }

  public void setForm(RegelSelectieForm form) {
    this.form = form;
  }

  public String getZoekString() {
    return zoekString;
  }

  public void setZoekString(String zoekString) {
    this.zoekString = zoekString;
  }

  @Override
  public void onPreviousPage() {
    getNavigation().goBackToPreviousPage();
  }

  private StringBuilder buildFile() {

    int aantalRegels = ((RegelSelectie) getForm().getField(REGELSELECTIE).getValue()).getValue();
    StringBuilder sb = new StringBuilder();

    sb.append("<table style=\"width:100%\" class=\"logbestand\">");

    List<String> selectedLines = getSubList(getCompleteFile(), aantalRegels);

    int lineCount = getCompleteFile().size() - selectedLines.size();

    for (String line : selectedLines) {

      if (getZoekString() != null) {
        String[] patterns = getZoekString().split(",");
        line = kleurZoekresultaten(patterns, line);
      }

      sb.append("<tr>");
      sb.append("<td class=\"nr\">" + lineCount + "</td>");
      sb.append("<td class=\"line\">" + line + "</td>");
      sb.append("</tr>");
      lineCount++;
    }

    sb.append("</table>");

    return sb;
  }

  private void checkZoekString() {

    String[] patterns = getZoekString().split(",");
    if (patterns.length > 4) {
      throw new ProException(ENTRY, WARNING, "Maximaal 4 zoektermen invullen a.u.b.");
    }
  }

  /**
   * Per zoekterm (pattern) kijken we naar de letters die voorkomen en tellen we hoe vaak een letter
   * in een match voorkomt. Dit bepaalt de kleur van de letter in de output
   */

  private List<Letter> findLetters(String[] patterns, String line) {

    List<Letter> result = new ArrayList<>();

    for (String s : patterns) {
      if (fil(s)) {

        Pattern p = Pattern.compile(Pattern.quote(s.trim()), Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(line);

        while (m.find()) {

          int start = m.start();
          int end = start + m.group().length();

          for (int i = start; i < end; i++) {

            Letter letter = new Letter(i, line.substring(i, i + 1));

            if (result.contains(letter)) {
              letter = result.get(result.indexOf(letter));
            }

            letter.setAantal(letter.getAantal() + 1);

            if (!result.contains(letter)) {
              result.add(letter);
            }
          }
          searchResults++;
        }
      }
    }

    return result;
  }

  private String geefMelding(int searchResults) {

    switch (searchResults) {
      case 0:
        return "Geen zoekresultaten";

      case 1:
        return "1 zoekresultaat";

      default:
        return searchResults + " zoekresultaten";
    }
  }

  private List<String> getSubList(List<String> allLines, int max) {

    if (max == 0 || max >= allLines.size()) {
      return allLines;
    }

    return allLines.subList(allLines.size() - max, allLines.size());
  }

  /**
   * Groepeer letters tot woorden, wanneer ze naast elkaar staan en dezelfde kleur hebben.
   */

  private void groupLetters(List<Letter> letters, List<Word> words) {

    for (Letter l : letters) {

      if (!isPartOfWord(l, words)) {

        words.add(new Word(l));
      }
    }
  }

  /**
   * Is letter l deel van een woord?
   */
  private boolean isPartOfWord(Letter l, List<Word> words) {

    for (Word word : words) {

      if (word.isNaast(l)) {

        word.getLetters().add(0, l);

        return true;
      }
    }
    return false;
  }

  private String kleurZoekresultaten(String[] patterns, String line) {

    List<Word> words = new ArrayList<>();

    List<Letter> letters = findLetters(patterns, line);

    Collections.sort(letters);

    groupLetters(letters, words);

    StringBuilder newLine = new StringBuilder(line);

    for (Word word : words) {
      newLine.replace(word.getStartIndex(), word.getLastIndex() + 1,
          "<span style='background: " + bcolors[word.getAantal() - 1] + ";color: " + fcolors[word.getAantal() - 1]
              + "'>" + word.getWord() + "</span>");
    }

    line = newLine.toString();

    return line;
  }

  private void readFile(File file) {
    try {

      for (String line : FileUtils.readLines(file)) {
        getCompleteFile().add(line);
      }
    } catch (IOException e) {
      throw new ProException(DOCUMENTS, "Fout bij lezen bestand", e);
    }
  }

  private void showFile() {

    searchResults = 0;

    if (getCl() != null) {
      removeComponent(getCl());
    }

    StringBuilder fileText = buildFile();

    try {
      setCl(new CustomLayout(new ByteArrayInputStream(fileText.toString().getBytes())));
    } catch (IOException e) {
      throw new ProException(DOCUMENTS, "Fout bij laden bestand", e);
    }

    addComponent(getCl());
  }
}
