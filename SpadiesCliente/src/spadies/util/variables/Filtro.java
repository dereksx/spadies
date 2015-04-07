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
package spadies.util.variables;

import java.io.*;
import java.util.*;

public final class Filtro implements Serializable {
  private static final long serialVersionUID=-5893701405490339155L;
  public final Variable variable;
  public final Comparable[] filtro;
  public Filtro(Variable pVariable, Item[] pItems) {
    variable=pVariable;
    filtro=Item.getKeys(pItems);
  }
  @SuppressWarnings("unchecked")
  public boolean pasaFiltro(Object...args) {
    Object val=variable.getValor(args);
    if (val==null) return false;
    return Arrays.binarySearch(filtro,(Comparable)(variable.rango.getRango(val)))>=0;
  }
}
