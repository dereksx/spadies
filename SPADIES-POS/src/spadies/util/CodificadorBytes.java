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
package spadies.util;

public abstract class CodificadorBytes {
  public abstract int numCodigos();
  public abstract int getCodigo(byte b);
  public byte[] getCodigos(String s) {
    s=limpiarString(s);
    byte[] b=s.getBytes();
    int t=b.length;
    byte[] r=new byte[t];
    for (int i=0; i<t; i++) r[i]=(byte)(getCodigo(b[i]));
    return r;
  }
  public abstract byte getCharacter(int c);
  public String getCharacters(byte[] b) {
    String s="";
    for (byte a:b) s+=(char)(getCharacter(a));
    return s;
  }
  public abstract String limpiarString(String s);
}
