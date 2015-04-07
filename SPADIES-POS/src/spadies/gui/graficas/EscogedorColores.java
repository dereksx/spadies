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
package spadies.gui.graficas;

import java.awt.*;
import java.util.*;

public final class EscogedorColores<T extends Comparable> {
  private Random random=new Random(0);
  private LinkedList<Color> listaColores=new LinkedList<Color>(Arrays.asList(
    new Color(0,0,255),
    new Color(255,0,0),
    new Color(0,255,0),
    new Color(255,0,255),
    new Color(0,255,255),
    new Color(100,100,100),
    new Color(127,127,255),
    new Color(255,127,127),
    new Color(50,128,255),
    new Color(255,128,50),
    new Color(50,255,128),
    new Color(0,0,100),
    new Color(100,0,0),
    new Color(0,100,0),
    new Color(100,0,100),
    new Color(0,100,100),
    new Color(100,100,0)
  ));
  private final Map<T,Color> mapaColores=new TreeMap<T,Color>();
  public EscogedorColores() {}
  public Color next() {
    if (!listaColores.isEmpty()) return listaColores.removeFirst().brighter();
    int cR=random.nextInt(256),cG=random.nextInt(256),cB=random.nextInt(256);
    if (cR<80) cR+=80;
    if (cG<80) cG+=80;
    if (cB<80) cB+=80;
    Color c=new Color(cR,cG,cB);
    if (cR+cG+cB>600) return c.darker().darker().darker();
    return c;
  }
  public Color next(T llave) {
    Color c=mapaColores.get(llave);
    if (c==null) mapaColores.put(llave,c=next());
    return c;
  }
}
