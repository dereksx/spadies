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

import spadies.kernel.*;
import spadies.util.*;

public final class RangoVariableExtra extends RangoByte<Byte> {
  private final int indiceVariableExtra;
  private final boolean dinamica;
  public RangoVariableExtra(int pIndiceVariableExtra, boolean pDinamica) {
    indiceVariableExtra=pIndiceVariableExtra;
    this.dinamica = pDinamica;
  }
  public Byte[] getRango() {
    VariableExtra ve=dinamica?
        AmbienteVariables.getInstance().getVariablesExtrasD()[indiceVariableExtra]
        :AmbienteVariables.getInstance().getVariablesExtras()[indiceVariableExtra];
    int t=(ve!=null)?(ve.nombresValores.length):0;
    if (t==0) return new Byte[0];
    Byte[] res=new Byte[t+1];
    res[0]=-1;
    for (int i=0; i<t; i++) res[i+1]=(byte)(i);
    return res;
  }
  public Byte getRango(Byte val) {
    return val;
  }
  public String toString(Byte val) {
    if (val==-1) return Constantes.S_DESCONOCIDO;
    String res = dinamica?
        new String(AmbienteVariables.getInstance().getVariablesExtrasD()[indiceVariableExtra].nombresValores[val])
        :new String(AmbienteVariables.getInstance().getVariablesExtras()[indiceVariableExtra].nombresValores[val]);
    return res;
  }
}
