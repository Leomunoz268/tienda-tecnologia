package dominio;

import dominio.repositorio.RepositorioProducto;
import dominio.excepcion.GarantiaExtendidaException;
import dominio.repositorio.RepositorioGarantiaExtendida;

import java.util.Calendar;
import java.util.Date;

public class Vendedor {
	
	/**
	 * Declaracion de las constantes que servirán como mensajes cuando alguna condición no se cumpla.
	 */
    public static final String EL_PRODUCTO_TIENE_GARANTIA = "El producto ya cuenta con una garantia extendida";
    public static final String EL_PRODUCTO_NO_EXISTE = "El producto no existe";
    public static final String EL_PRODUCTO_TIENE_TRES_VOCALES = "Este producto no cuenta con garantía extendida";
    public static final String NOMBRE_CLIENTE_NO_INGRESADO = "No se ingresó el nombre del cliente";
 
    private RepositorioProducto repositorioProducto;
    private RepositorioGarantiaExtendida repositorioGarantia;

    public Vendedor(RepositorioProducto repositorioProducto, RepositorioGarantiaExtendida repositorioGarantia) {
        this.repositorioProducto = repositorioProducto;
        this.repositorioGarantia = repositorioGarantia;

    }
    /**
     * Permite generar la garantía a un producto.
     * @version 1.1 se agrega el parametro del nombre del cliente
     * @author 1.1 Leonardo Muñoz
     * @param codigo codigo del producto al cual se le va a generar la garantía
     * @param nombreCliente nombre del cliente al que se le va a realizar la garantía
     * 
     */
    public void generarGarantia(String codigo,String nombreCliente) {
    	
        Producto producto = repositorioProducto.obtenerPorCodigo(codigo);
        double valorGarantia, valorProducto;
        int diasExtension;
        
        /**
         * Si el nombre del cliente no se ha ingresado, no se puede generar la garantía.
         */
        if(nombreCliente == null) {
        	throw new GarantiaExtendidaException(NOMBRE_CLIENTE_NO_INGRESADO);
        }
        
        /**
         * Si no se ingresa el código del producto, no se puede generar la garantía.
         */
        if(producto == null) {
        	throw new GarantiaExtendidaException(EL_PRODUCTO_NO_EXISTE);
        }
        
        /**
         * Si el producto ingresado ya posee garantía, no se puede generar otra.
         */
    	if(tieneGarantia(codigo)) {
    		throw new GarantiaExtendidaException(EL_PRODUCTO_TIENE_GARANTIA);
    	}
    	
    	/**
    	 * Si el producto posee 3 vocales, no se puede generar garantía.
    	 */
		if(repositorioProducto.validarTresVocales(codigo)) {
			throw new GarantiaExtendidaException(EL_PRODUCTO_TIENE_TRES_VOCALES);
		}
		
		valorProducto = producto.getPrecio() ;
		
		/**
		 * si el costo del producto es mayor a 500000, el valor de la garantia sera del 20% y los dias serán 200.
		 * De lo contrario el valor de la garantía será del 10% y los dias serán 100.
		 */
		if(valorProducto > 500000) {
			valorGarantia = valorProducto*0.2;
			diasExtension=200;
		}else {
			valorGarantia = valorProducto*0.1;
			diasExtension =100;
		}
        
		/**
		 * Se calcula la fecha en la cual finalizará la garantía.
		 */
		Date fechaFinGarantia = calcularFechaFinGarantia(new Date(), diasExtension);
		
		/**
		 * Se genera la garantía con los valores correspondientes.
		 */
		GarantiaExtendida garantiaExt = new GarantiaExtendida(producto, new Date(), fechaFinGarantia, valorGarantia, nombreCliente);
		repositorioGarantia.agregar(garantiaExt);
    }

    /**
	 * Busca si el producto ya posee garantia
	 * 
	 * @param codigo codigo del producto
	 * @returns True: si ya posee garantía
	 *  False: si no posee garantía.
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
	 * @param fechaInicial fecha de inicio de la garantía;
	 * @param diasGarantia dias de la garantía
	 * @return la fecha de finalización de la garantía
	 */
    public Date calcularFechaFinGarantia(Date fechaInicial, int diasGarantia) {
    	
    	Calendar calendario = Calendar.getInstance();
    	calendario.setTime(fechaInicial);
    	int dias =0;
    	
    	/**
    	 * si el día de la fecha es un Lunes no se tiene en cuenta en el cálculo de lo días
    	 */
    	while(dias<diasGarantia) {
    		if (calendario.get(Calendar.DAY_OF_WEEK) != 2) {
    			dias++;
    		}
    		calendario.add(calendario.DAY_OF_YEAR, 1);
    	}
    	
    	/**
    	 * si la fecha de finalización cae un domingo, se suman otros dos dias ya que los lunes no se cuentan.
    	 * @deprecated
    	 * Nota: El requerimiento decía que fuera al siguiente día hábil, este metodo es temporal mientras se crea un maestro
    	 * 		de festivos, ya que los festivos dependen del pais, del año y la idea es que la aplicación no dependa de estos valores
    	 * 		por defecto, sino ser independiente del pais, año. etc. El maestro de festivos podria servir para otro proyecto, o ya estar implementado. 
    	 */
    	if(calendario.get(Calendar.DAY_OF_WEEK) == 1) {
    		calendario.add(calendario.DAY_OF_YEAR, 2);
    	}
    	
    	return calendario.getTime();
    }


}
