package dominio;

import dominio.repositorio.RepositorioProducto;
import dominio.excepcion.GarantiaExtendidaException;
import dominio.repositorio.RepositorioGarantiaExtendida;

import java.util.Calendar;
import java.util.Date;

public class Vendedor {
	
	/**
	 * Declaracion de las constantes que servir�n como mensajes cuando alguna condici�n no se cumpla.
	 */
    public static final String EL_PRODUCTO_TIENE_GARANTIA = "El producto ya cuenta con una garantia extendida";
    public static final String EL_PRODUCTO_NO_EXISTE = "El producto no existe";
    public static final String EL_PRODUCTO_TIENE_TRES_VOCALES = "Este producto no cuenta con garant�a extendida";
    public static final String NOMBRE_CLIENTE_NO_INGRESADO = "No se ingres� el nombre del cliente";
 
    private RepositorioProducto repositorioProducto;
    private RepositorioGarantiaExtendida repositorioGarantia;

    public Vendedor(RepositorioProducto repositorioProducto, RepositorioGarantiaExtendida repositorioGarantia) {
        this.repositorioProducto = repositorioProducto;
        this.repositorioGarantia = repositorioGarantia;

    }
    /**
     * Permite generar la garant�a a un producto.
     * @version 1.1 se agrega el parametro del nombre del cliente
     * @author 1.1 Leonardo Mu�oz
     * @param codigo codigo del producto al cual se le va a generar la garant�a
     * @param nombreCliente nombre del cliente al que se le va a realizar la garant�a
     * 
     */
    public void generarGarantia(String codigo,String nombreCliente) {
    	
        Producto producto = repositorioProducto.obtenerPorCodigo(codigo);
        double valorGarantia, valorProducto;
        int diasExtension;
        
        /**
         * Si el nombre del cliente no se ha ingresado, no se puede generar la garant�a.
         */
        if(nombreCliente == null) {
        	throw new GarantiaExtendidaException(NOMBRE_CLIENTE_NO_INGRESADO);
        }
        
        /**
         * Si no se ingresa el c�digo del producto, no se puede generar la garant�a.
         */
        if(producto == null) {
        	throw new GarantiaExtendidaException(EL_PRODUCTO_NO_EXISTE);
        }
        
        /**
         * Si el producto ingresado ya posee garant�a, no se puede generar otra.
         */
    	if(tieneGarantia(codigo)) {
    		throw new GarantiaExtendidaException(EL_PRODUCTO_TIENE_GARANTIA);
    	}
    	
    	/**
    	 * Si el producto posee 3 vocales, no se puede generar garant�a.
    	 */
		if(repositorioProducto.validarTresVocales(codigo)) {
			throw new GarantiaExtendidaException(EL_PRODUCTO_TIENE_TRES_VOCALES);
		}
		
		valorProducto = producto.getPrecio() ;
		
		/**
		 * si el costo del producto es mayor a 500000, el valor de la garantia sera del 20% y los dias ser�n 200.
		 * De lo contrario el valor de la garant�a ser� del 10% y los dias ser�n 100.
		 */
		if(valorProducto > 500000) {
			valorGarantia = valorProducto*0.2;
			diasExtension=200;
		}else {
			valorGarantia = valorProducto*0.1;
			diasExtension =100;
		}
        
		/**
		 * Se calcula la fecha en la cual finalizar� la garant�a.
		 */
		Date fechaFinGarantia = calcularFechaFinGarantia(new Date(), diasExtension);
		
		/**
		 * Se genera la garant�a con los valores correspondientes.
		 */
		GarantiaExtendida garantiaExt = new GarantiaExtendida(producto, new Date(), fechaFinGarantia, valorGarantia, nombreCliente);
		repositorioGarantia.agregar(garantiaExt);
    }

    /**
	 * Busca si el producto ya posee garantia
	 * 
	 * @param codigo codigo del producto
	 * @returns True: si ya posee garant�a
	 *  False: si no posee garant�a.
	 */
    public boolean tieneGarantia(String codigo) {
    	
    	Producto producto = repositorioGarantia.obtenerProductoConGarantiaPorCodigo(codigo);
    	return (producto != null );
    }
    
    public void crearProducto(Producto producto) {
    	this.repositorioProducto.agregar(producto);
    	
    }
    
    /**
	 * Busca si el producto ya posee garantia
	 * 
	 * @param fechaInicial fecha de inicio de la garant�a;
	 * @param diasGarantia dias de la garant�a
	 * @return la fecha de finalizaci�n de la garant�a
	 */
    public Date calcularFechaFinGarantia(Date fechaInicial, int diasGarantia) {
    	
    	Calendar calendario = Calendar.getInstance();
    	calendario.setTime(fechaInicial);
    	int dias =0;
    	
    	/**
    	 * si el d�a de la fecha es un Lunes no se tiene en cuenta en el c�lculo de lo d�as
    	 */
    	while(dias<diasGarantia) {
    		if (calendario.get(Calendar.DAY_OF_WEEK) != 2) {
    			dias++;
    		}
    		calendario.add(calendario.DAY_OF_YEAR, 1);
    	}
    	
    	/**
    	 * si la fecha de finalizaci�n cae un domingo, se suman otros dos dias ya que los lunes no se cuentan.
    	 * @deprecated
    	 * Nota: El requerimiento dec�a que fuera al siguiente d�a h�bil, este metodo es temporal mientras se crea un maestro
    	 * 		de festivos, ya que los festivos dependen del pais, del a�o y la idea es que la aplicaci�n no dependa de estos valores
    	 * 		por defecto, sino ser independiente del pais, a�o. etc. El maestro de festivos podria servir para otro proyecto, o ya estar implementado. 
    	 */
    	if(calendario.get(Calendar.DAY_OF_WEEK) == 1) {
    		calendario.add(calendario.DAY_OF_YEAR, 2);
    	}
    	
    	return calendario.getTime();
    }


}
