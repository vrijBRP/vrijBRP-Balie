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

package nl.procura.gba.web.modules.beheer.overig;

import static nl.procura.gba.common.MiscUtils.cleanPath;
import static nl.procura.standard.Globalfunctions.*;
import static nl.procura.standard.exceptions.ProExceptionSeverity.WARNING;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nl.procura.standard.exceptions.ProException;

public class NavigationSorter {

  private List<NavFile<?>> navFiles = new ArrayList<>();

  /**
   * Deze functie wordt gebruikt om objecten in een mapstructuur te sorteren.
   * In ons geval kan T dus staan voor File (voor logbestanden en sjablonen),
   * Document of Gebruiker.
   */
  public <T> void add(String path, T obj) {

    if (path == null || obj == null) {
      return;
    }

    getNavFiles().add(new NavFile<>(path, obj));
  }

  public List<NavFile> getAllFiles(String path) {

    String currentPath = cleanPath(path);
    List<NavFile> navFiles = new ArrayList<>();

    for (NavFile file : getNavFiles()) {
      if (file.getPath().matches(currentPath) || file.getPath().startsWith(currentPath + "/")) {
        navFiles.add(file);
      }
    }

    return navFiles;
  }

  /**
   * Geeft de subdirectories direct onder de huidige directorie, gaat dus niet dieper dan 1 stap in
   * de directorieboom.
   * De directories worden gevonden door de observatie dat de padnamen van files
   * gevormd worden door de directories. De naam van de directorie komt in het pad te staan zodat we bij
   * klikken in de tabel een directorie verder springen. De string dirFiles geeft de locatie op de
   * harde schijf aan.
   *
   * @return Set<Dir> verzameling gevonden directories
   */

  public Set<Dir> getDirs(String path, String dirFiles) {

    path = cleanPath(path);

    Pattern pattern = Pattern.compile(emp(path) ? "" : "^" + path + "/+");
    Set<Dir> newDirs = new TreeSet<>();

    for (NavFile<?> file : getNavFiles()) {
      String docPath = file.getPath();

      if (!eq(docPath, path)) {

        String foundPath = "";
        Matcher m = pattern.matcher(docPath);

        while (m.find()) {
          foundPath = docPath.replaceAll(m.group(), "");
        }

        // Eerste directorie na de huidige
        String name = foundPath.split("/")[0];

        if (fil(name)) {
          addNewDir(path, name, dirFiles, newDirs);
        }
      }
    }

    return newDirs;
  }

  public int getFileCount(String path) {
    path = cleanPath(path);

    int count = 0;

    for (NavFile<?> file : getNavFiles()) {
      if (emp(path)) {
        count++;
      } else if (eq(file.getPath(), path) || file.getPath().startsWith(path + "/")) {
        count++;
      }
    }

    return count;
  }

  public List<NavFile> getFiles(String path) {

    String currentPath = cleanPath(path);
    List<NavFile> newItems = new ArrayList<>();

    for (NavFile<?> file : getNavFiles()) {
      if (file.getPath().matches(currentPath)) {
        newItems.add(file);
      }
    }

    return newItems;
  }

  public List<NavFile<?>> getNavFiles() {
    return navFiles;
  }

  public void setNavFiles(List<NavFile<?>> navFiles) {
    this.navFiles = navFiles;
  }

  // geeft alle NavFiles terug met een pad dat gelijk is aan of een uitbreiding is van
  // het meegegeven pad.

  /**
   * De laatste directorie boven de huidige
   *
   * @return Dir parentDir
   */
  public Dir getParentDir(String path) {

    path = cleanPath(path);
    Matcher m = Pattern.compile("/").matcher(path);

    int prev = 0;
    int next = 0;

    while (m.find()) {
      prev = next;
      next = m.start();
    }

    String lastPath = path.substring(0, next); // dit geeft de volledige padnaam van de bovenliggende directory
    String name = path.substring(prev, next);

    if (fil(path) && emp(name)) {
      name = "home";
    }

    return new Dir(lastPath, name, true);
  }

  // geeft alle NavFiles met meegegeven pad terug.

  /**
   * Deze functie geeft het pad van de file aan, relatief aan de meegegeven directorie parentDir.
   * We gaan er dus vanuit dat de file in de parentDir staat. Merk op dat we voor file ook een directorie
   * kunnen meegeven.
   *
   * @return String padnaam
   */

  public String getPath(File file, File parentDir) {
    String path = null;

    if ((file != null) && isDir(parentDir)) {
      if (!file.getPath().startsWith(parentDir.getPath())) {
        throw new ProException(WARNING, "Fout bij het opvragen van padnaam.");
      }

      String home = cleanPath(parentDir.getPath());
      path = cleanPath(cleanPath(file.getPath()).replaceFirst(home, ""));
      path = getRelativePath(path);
    }

    return path;
  }

  private void addNewDir(String path, String name, String dirFiles, Set<Dir> newDirs) {

    if (dirFiles != null) {
      newDirs.add(new Dir(path + "/" + name, name, dirFiles));
    } else {
      newDirs.add(new Dir(path + "/" + name, name));
    }

  }

  private String getRelativePath(String path) {
    int lastSlash = path.lastIndexOf('/');

    return (lastSlash >= 0) ? path.substring(0, lastSlash) : "";
  }

  private boolean isDir(File file) {

    boolean check = false;

    if (file != null) {
      check = file.isDirectory();
    }

    return check;
  }

  public class Dir implements Comparable<Dir> {

    private String  path      = "";
    private String  name      = "";   // de string na de laatste / van path
    private boolean parentDir = false;
    private File    file      = null;

    Dir(String path, String name) {
      this(path, name, false);
    }

    private Dir(String path, String name, boolean parentDir) {
      this(path, name, null, parentDir);
    }

    private Dir(String path, String name, String dirFiles) {
      this(path, name, dirFiles, false);
    }

    private Dir(String path, String name, String dirFiles, boolean parentDir) {

      String dirPath = dirFiles + "/" + path;

      setPath(cleanPath(path));
      setName(cleanPath(name));
      setFile(new File(cleanPath(dirPath)));
      setParentDir(parentDir);
    }

    @Override
    public int compareTo(Dir o) {
      return getName().toLowerCase().compareTo(o.getName().toLowerCase());
    }

    @Override
    public boolean equals(Object obj) {
      boolean isEqual = false;

      if (obj instanceof Dir) {
        Dir dir = (Dir) obj;

        if (checkPath(dir) && checkName(dir)) {
          isEqual = true;
        }
      }

      return isEqual;
    }

    public File getFile() {
      return file;
    }

    public void setFile(File file) {
      this.file = file;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getPath() {
      return path;
    }

    public void setPath(String path) {
      this.path = path;
    }

    @Override
    public int hashCode() {
      return path.length();
    }

    public boolean isParentDir() {
      return parentDir;
    }

    public void setParentDir(boolean parentDir) {
      this.parentDir = parentDir;
    }

    @Override
    public String toString() {
      return "Dir [name=" + name + ", path=" + path + "]";
    }

    private boolean checkName(Dir dir) {
      boolean isEqual = false;

      if (dir != null) {
        isEqual = eq(name, dir.getName());
      }

      return isEqual;
    }

    private boolean checkPath(Dir dir) {
      boolean isEqual = false;

      if (dir != null) {
        isEqual = eq(path, dir.getPath());
      }

      return isEqual;
    }
  }

  public class NavFile<T> {

    private String path = "";
    private T      obj  = null;

    private NavFile(String path, T obj) {

      setPath(cleanPath(path));
      setObj(obj);
    }

    @Override
    public boolean equals(Object obj) {

      boolean isEqual = false;

      if (obj instanceof NavFile) {
        NavFile navFile = (NavFile) obj;

        if (checkPath(navFile) && checkObject(navFile)) {
          isEqual = true;
        }
      }

      return isEqual;

    }

    public T getObj() {
      return obj;
    }

    public void setObj(T obj) {
      this.obj = obj;
    }

    public String getPath() {
      return path;
    }

    public void setPath(String path) {
      this.path = cleanPath(path);
    }

    @Override
    public int hashCode() {
      return path.length();
    }

    @Override
    public String toString() {
      return "NavFile [obj=" + obj + ", path=" + path + "]";
    }

    private boolean checkObject(NavFile navFile) {
      boolean isEqual = false;

      if (navFile != null) {
        isEqual = obj.equals(navFile.getObj());
      }

      return isEqual;
    }

    private boolean checkPath(NavFile navFile) {
      boolean isEqual = false;

      if (navFile != null) {
        isEqual = eq(path, navFile.getPath());
      }

      return isEqual;
    }

  }
}
