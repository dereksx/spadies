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

import java.util.*;

public final class Item implements Comparable<Item> {
  public final Comparable key;
  public final String value;
  public final String valueHTML;
  public Item(Comparable pKey, String pValue, String pValueHTML) {
    key=pKey;
    value=pValue;
    valueHTML=pValueHTML;
  }
  @SuppressWarnings("unchecked")
  public int compareTo(Item it) {
    return key.compareTo(it.key);
  }
  public static Comparable[] getKeys(Item[] items) {
    int n=items.length;
    Comparable[] res=new Comparable[n];
    for (int i=0; i<n; i++) res[i]=items[i].key;
    Arrays.sort(res);
    return res;
  }
  public String toString() {
    return value;
  }
  public String toStringHTML() {
    return valueHTML;
  }
}
