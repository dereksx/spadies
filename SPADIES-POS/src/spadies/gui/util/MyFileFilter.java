/*
 * Centro de Estudios sobre Desarrollo Econ�mico (CEDE) - Universidad de los Andes
 * Octubre 18 de 2006
 *
 *********************************************************
 * SPADIES                                               *
 * Sistema para la Prevenci�n y An�lisis de la Deserci�n *
 * en las Instituciones de Educaci�n Superior            *
 *********************************************************
 * Autores del c�digo fuente (�ltima versi�n):           *
 *  Alejandro Sotelo Ar�valo   alejandrosotelo@gmail.com *
 *  Andr�s C�rdoba Melani      acordoba@gmail.com        *
 *********************************************************
 *
 * Para informaci�n de los participantes del proyecto v�ase el "Acerca De" de la aplicaci�n.
 * 
 * La modificaci�n del c�digo fuente est� prohibida sin permiso expl�cito por parte de
 * los autores o del Ministerio de Educaci�n Nacional de la Rep�blica de Colombia.
 *
 */
package spadies.gui.util;

import java.io.*;

public final class MyFileFilter extends javax.swing.filechooser.FileFilter {
  public final String extension;
  public final String descripcion;
  public MyFileFilter(String pExtension, String pDescripcion) {
    extension=pExtension;
    descripcion=pDescripcion;
  }
  public boolean accept(File f) {
    return (f!=null && (extension==null || f.isDirectory() || f.getName().toLowerCase().endsWith(extension.toLowerCase())));
  }
  public String getDescription() {
    return descripcion;
  }
}
