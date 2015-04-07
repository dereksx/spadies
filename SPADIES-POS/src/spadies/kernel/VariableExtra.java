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
package spadies.kernel;

import spadies.io.*;

public class VariableExtra {
  public byte[] nombre={};
  public byte[][] nombresValores=new byte[0][];
  public static VariableExtra cargar(MyDataInputStream is) throws Exception {
    VariableExtra ve=new VariableExtra();
    ve.nombre=is.readByteArray(true,-1);
    int t=is.readByte();
    ve.nombresValores=new byte[t][];
    for (int i=0; i<t; i++) ve.nombresValores[i]=is.readByteArray(true,-1);
    return ve;
  }
  public static void guardar(MyDataOutputStream os, VariableExtra ve) throws Exception {
    os.writeByteArray(true,ve.nombre);
    int t=Math.min(ve.nombresValores.length,100);
    os.writeByte((byte)t);
    for (int i=0; i<t; i++) os.writeByteArray(true,ve.nombresValores[i]);
  }  
  public int getTamanhoEnBytes() {
    int r=0,t=Math.min(nombresValores.length,100);
    r+=2+nombre.length+1;
    for (int i=0; i<t; i++) r+=2+nombresValores[i].length;
    return r;
  }
}
