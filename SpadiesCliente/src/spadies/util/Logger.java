/*
 * Centro de Estudios sobre Desarrollo Econ�mico (CEDE) - Universidad de los Andes
 *
 *********************************************************
 * SPADIES                                               *
 * Sistema para la Prevenci�n y An�lisis de la Deserci�n *
 * en las Instituciones de Educaci�n Superior            *
 *********************************************************
 * Autores del c�digo fuente (�ltima versi�n):           *
 *  Andr�s C�rdoba Melani      acordoba@gmail.com        *
 *********************************************************
 *
 * Para informaci�n de los participantes del proyecto v�ase el "Acerca De" de la aplicaci�n.
 * 
 * La modificaci�n del c�digo fuente est� prohibida sin permiso expl�cito por parte de
 * los autores o del Ministerio de Educaci�n Nacional de la Rep�blica de Colombia.
 *
 */
package spadies.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
  private static final DateFormat formatoTiempo = new SimpleDateFormat("yyyy/MM/dd_HH:mm"); 
  private File logFile = null; 
  private PrintStream ps = null;
  public Logger(File f) {
    logFile = f;
  }
  public void init() throws FileNotFoundException {
    ps = new PrintStream(logFile);
  }
  public void log(String msg) {
    ps.println(tiempo() + " " + msg);
  }
  public void log(Throwable th) {
    ps.println(tiempo());
    th.printStackTrace(ps);
  }
  public void log(String msg,Throwable th) {
    ps.println(tiempo()+" "+msg);
    th.printStackTrace(ps);
  }
  private String tiempo() {
    return formatoTiempo.format(new Date());
  }
}
