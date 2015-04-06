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
package spadies.server.util;

import java.util.*;
import spadies.io.*;
import spadies.util.*;

public final class MySortedArray {
  /**
   * Arreglo de cadenas de texto
   */
  public final byte[][] datos;
  /**
   * Arreglo que contiene en cada posicion, las ubicaciones donde aparece un unico valor de la cadena(posiciones en datos)
   */
  public final int[][] values;
  public final Comparator<byte[]> comparator;
  public MySortedArray(byte[][] pDatos, Comparator<byte[]> pComparator, Collection<List<Integer>> pValues) {
    datos=pDatos;
    comparator=pComparator;
    int i=0,t=pValues.size();
    values=new int[t][];
    for (List<Integer> p:pValues) values[i++]=CajaDeHerramientas.toIntArray(p);
    Arrays.sort(values,new Comparator<int[]>() {
      public int compare(int[] val1, int[] val2) {
        return comparator.compare(datos[val1[0]],datos[val2[0]]);
      }
    });
  }
  public MySortedArray(byte[][] pDatos, Comparator<byte[]> pComparator, MyDataInputStream is) throws Exception {
    datos=pDatos;
    comparator=pComparator;
    int t=is.readInt();
    values=new int[t][];
    for (int i=0; i<t; i++) {
      int u=is.readShort(),array[]=new int[u];
      for (int j=0; j<u; j++) array[j]=is.readLittleInt();
      values[i]=array;
    }
  }
  public void write(MyDataOutputStream os) throws Exception {
    os.writeInt(values.length);
    for (int[] val:values) {
      int u=Math.min(val.length,Short.MAX_VALUE);
      os.writeShort((short)u);
      for (int j=0; j<u; j++) os.writeLittleInt(val[j]);
    }
  }
  public int search(byte[] key) { // M�todo basado en el c�digo fuente de Sun Microsystems, Inc.
    int pi=0,pf=values.length-1;
    while (pi<=pf) {
      int m=(pi+pf)>>1,c=comparator.compare(datos[values[m][0]],key);
      if      (c<0) pi=m+1;
      else if (c>0) pf=m-1;
      else          return m;
    }
    return -(pi+1);
  }
  public int[] searchLimits(byte[] key1, byte[] key2) {
    int i1=search(key1),i2=search(key2);
    return new int[]{(i1<0)?(-i1-1):i1,(i2<0)?(-i2-1):i2};
  }
}
