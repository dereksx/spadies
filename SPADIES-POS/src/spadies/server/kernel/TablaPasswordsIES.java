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

public final class TablaPasswordsIES {
  private static final TablaPasswordsIES instance=new TablaPasswordsIES();
  private Map<Integer,String> passwords=new TreeMap<Integer,String>();
  public static TablaPasswordsIES getInstance() {return instance;}
  private TablaPasswordsIES() {}
  public void preparar() throws Exception {
    long tmi=System.currentTimeMillis();
    System.out.println("LEYENDO LOS PASSWORDS DE LAS IES ("+ConstantesServer.fPASSWORDS_IES.getName()+")");
    passwords = lecturaPasswords();
    System.out.println("  "+ConstantesServer.fPASSWORDS_IES.getName()+" LEIDO EN "+(System.currentTimeMillis()-tmi)+"ms");
  }
  public String getPasswordIES(int codigo) {
    return passwords.get(codigo);
  }
  private static Map<Integer,String>lecturaPasswords() throws NumberFormatException, IOException {
    Map<Integer,String> res = new TreeMap<Integer, String>();
    BufferedReader br=new BufferedReader(new FileReader(ConstantesServer.fPASSWORDS_IES));
    for (String sL=br.readLine(); (sL=br.readLine())!=null; ) {
      String wL[]=csvToString(sL,0,';'),sCodigo=codifNumeros.limpiarString(wL[0]),sPassword=wL[1].trim();
      res.put(Integer.parseInt(sCodigo),sPassword);
    }
    br.close();
    return Collections.unmodifiableMap(res);
  }
}
