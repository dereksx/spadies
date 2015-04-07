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
package spadies.gui.imagenes;

import java.net.*;
import javax.swing.*;

public enum Imagenes {
  IM_ICONO_APLICACION("otros/iconoAplicacion.png"),
  IM_MINISTERIO("otros/ministerio.jpg"),
  IM_SIGUIENTE("jlfgr/Forward24.gif"),
  IM_ANTERIOR("jlfgr/Back24.gif"),
  IM_HOME("jlfgr/Home24.gif"),
  IM_ESCUDO_UNIVERSIDAD("otros/_ImEscudoUniversidad.png"),
  IM_ESCUDO_COLOMBIA("otros/_ImEscudoColombia.png"),
  IM_PRESENTACION01("otros/_ImPresentacion01.png"),
  IM_PRESENTACION02("otros/_ImPresentacion02.png"),
  IM_PRESENTACION03("otros/_ImPresentacion03.png"),
  IM_PRESENTACION04("otros/_ImPresentacion04.png");
  private final String nombreImagen;
  private Imagenes(String pNombre) {
    nombreImagen=pNombre;
    URLImagenes.map.put(nombreImagen.substring(nombreImagen.lastIndexOf('/')+1),getURL());
  }
  public URL getURL() {
    return Imagenes.class.getResource(nombreImagen);
  }
  public ImageIcon getImagen() {
    URL urlImagen=getURL();
    return (urlImagen==null)?null:new ImageIcon(urlImagen);
  }
}
