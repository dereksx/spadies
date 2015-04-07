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
package spadies.server.kernel;

import java.io.*;
import java.util.*;
import spadies.server.util.*;
import static spadies.util.CajaDeHerramientas.*;

public final class TablaIES {
  private static final TablaIES instance=new TablaIES();
  private final Map<Integer,byte[][]> mapaPorCodigo=new TreeMap<Integer,byte[][]>();
  public static TablaIES getInstance() {return instance;}
  private TablaIES() {}
  public void preparar() throws Exception {
    long tmi=System.currentTimeMillis();
    System.out.println("LEYENDO LA LISTA DE IES ("+ConstantesServer.fIES.getName()+","+ConstantesServer.fIES_ESPECIALES.getName()+")");
    for (File f:new File[]{ConstantesServer.fIES,ConstantesServer.fIES_ESPECIALES}) {
      BufferedReader br=new BufferedReader(new FileReader(f));
      for (String sL=br.readLine(); (sL=br.readLine())!=null; ) {
        String wL[]=csvToString(sL,0,';');
        byte[] sCodigo=codifNumeros.limpiarString(wL[0]).getBytes(),sNombre=wL[1].trim().getBytes(),sCaracter=codifNumeros.limpiarString(wL[2]).getBytes(),sDepartamento=codifNumeros.limpiarString(wL[3]).getBytes(),sMunicipio=codifNumeros.limpiarString(wL[5]).getBytes(),sOrigen=codifNumeros.limpiarString(wL[4]).getBytes();
        mapaPorCodigo.put(Integer.parseInt(new String(sCodigo)),new byte[][]{sNombre,sDepartamento,sMunicipio,sOrigen,sCaracter});
      }
    }
    System.out.println("  "+ConstantesServer.fIES.getName()+","+ConstantesServer.fIES_ESPECIALES.getName()+" LEIDOS EN "+(System.currentTimeMillis()-tmi)+"ms");
  }
  public byte[][] getDatos(int codigo) {
    return mapaPorCodigo.get(codigo);
  }
}
