package dominio.unitaria;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import dominio.Vendedor;
import dominio.Producto;
import dominio.repositorio.RepositorioProducto;
import dominio.repositorio.RepositorioGarantiaExtendida;
import testdatabuilder.ProductoTestDataBuilder;

public class VendedorTest {

	@Test
	public void productoYaTieneGarantiaTest() {
		
		// arrange
		ProductoTestDataBuilder productoTestDataBuilder = new ProductoTestDataBuilder();
		
		Producto producto = productoTestDataBuilder.build(); 
		
		RepositorioGarantiaExtendida repositorioGarantia = mock(RepositorioGarantiaExtendida.class);
		RepositorioProducto repositorioProducto = mock(RepositorioProducto.class);
		
		when(repositorioGarantia.obtenerProductoConGarantiaPorCodigo(producto.getCodigo())).thenReturn(producto);
		
		Vendedor vendedor = new Vendedor(repositorioProducto, repositorioGarantia);
		
		// act 
		boolean existeProducto = vendedor.tieneGarantia(producto.getCodigo());
		
		//assert
		assertTrue(existeProducto);
	}
	
	@Test
	public void productoNoTieneGarantiaTest() {
		
		// arrange
		ProductoTestDataBuilder productoestDataBuilder = new ProductoTestDataBuilder();
		
		Producto producto = productoestDataBuilder.build(); 
		
		RepositorioGarantiaExtendida repositorioGarantia = mock(RepositorioGarantiaExtendida.class);
		RepositorioProducto repositorioProducto = mock(RepositorioProducto.class);
		
		when(repositorioGarantia.obtenerProductoConGarantiaPorCodigo(producto.getCodigo())).thenReturn(null);
		
		Vendedor vendedor = new Vendedor(repositorioProducto, repositorioGarantia);
		
		// act 
		boolean existeProducto =  vendedor.tieneGarantia(producto.getCodigo());
		
		//assert
		assertFalse(existeProducto);
	}
	
	@Test
	public void calculoFechaFinGarantiaCaeDomingoTest() {
		
		// arrange
		ProductoTestDataBuilder productoestDataBuilder = new ProductoTestDataBuilder();
		
		Producto producto = productoestDataBuilder.build(); 
		
		RepositorioGarantiaExtendida repositorioGarantia = mock(RepositorioGarantiaExtendida.class);
		RepositorioProducto repositorioProducto = mock(RepositorioProducto.class);
		
		when(repositorioGarantia.obtenerProductoConGarantiaPorCodigo(producto.getCodigo())).thenReturn(null);
		
		Vendedor vendedor = new Vendedor(repositorioProducto, repositorioGarantia);
		
		Calendar calFechaInicial = Calendar.getInstance();
		calFechaInicial.set(2019, 06, 11);
		
		Date FechaFinalReal =  vendedor.calcularFechaFinGarantia(calFechaInicial.getTime(),4);
		
		Calendar calFechaEsperada = Calendar.getInstance();
		calFechaEsperada.set(2019, 06, 16);

		Date fechaEsperada = calFechaEsperada.getTime();
		
		SimpleDateFormat formatoDeFecha = new SimpleDateFormat("dd/MM/dd");
		assertEquals(formatoDeFecha.format(FechaFinalReal), formatoDeFecha.format(fechaEsperada));
	}
	
	@Test
	public void calculoFechaFinGarantiaSinContarLunesTest() {
		
		// arrange
		ProductoTestDataBuilder productoestDataBuilder = new ProductoTestDataBuilder();
		
		Producto producto = productoestDataBuilder.build(); 
		
		RepositorioGarantiaExtendida repositorioGarantia = mock(RepositorioGarantiaExtendida.class);
		RepositorioProducto repositorioProducto = mock(RepositorioProducto.class);
		
		when(repositorioGarantia.obtenerProductoConGarantiaPorCodigo(producto.getCodigo())).thenReturn(null);
		
		Vendedor vendedor = new Vendedor(repositorioProducto, repositorioGarantia);
		
		Calendar calFechaInicial = Calendar.getInstance();
		calFechaInicial.set(2019, 06, 11);
		
		Date FechaFinalReal =  vendedor.calcularFechaFinGarantia(calFechaInicial.getTime(),6);
		
		Calendar calFechaEsperada = Calendar.getInstance();
		calFechaEsperada.set(2019, 06, 17);

		Date fechaEsperada = calFechaEsperada.getTime();
		
		SimpleDateFormat formatoDeFecha = new SimpleDateFormat("dd/MM/dd");
		assertEquals(formatoDeFecha.format(FechaFinalReal), formatoDeFecha.format(fechaEsperada));
	}
	
	@Test
	public void calculoFechaFinGarantiaUnDiaCaeDomingoTest() {
		
		// arrange
		ProductoTestDataBuilder productoestDataBuilder = new ProductoTestDataBuilder();
		
		Producto producto = productoestDataBuilder.build(); 
		
		RepositorioGarantiaExtendida repositorioGarantia = mock(RepositorioGarantiaExtendida.class);
		RepositorioProducto repositorioProducto = mock(RepositorioProducto.class);
		
		when(repositorioGarantia.obtenerProductoConGarantiaPorCodigo(producto.getCodigo())).thenReturn(null);
		
		Vendedor vendedor = new Vendedor(repositorioProducto, repositorioGarantia);
		
		Calendar calFechaInicial = Calendar.getInstance();
		calFechaInicial.set(2019, 06, 21);
		
		Date FechaFinalReal =  vendedor.calcularFechaFinGarantia(calFechaInicial.getTime(),1);
		
		Calendar calFechaEsperada = Calendar.getInstance();
		calFechaEsperada.set(2019, 06,23 );

		Date fechaEsperada = calFechaEsperada.getTime();
		
		SimpleDateFormat formatoDeFecha = new SimpleDateFormat("dd/MM/dd");
		assertEquals(formatoDeFecha.format(FechaFinalReal), formatoDeFecha.format(fechaEsperada));
	}
	
	@Test
	public void calculoContarFechasCorrectamenteTest() {
		
		// arrange
		ProductoTestDataBuilder productoestDataBuilder = new ProductoTestDataBuilder();
		
		Producto producto = productoestDataBuilder.build(); 
		
		RepositorioGarantiaExtendida repositorioGarantia = mock(RepositorioGarantiaExtendida.class);
		RepositorioProducto repositorioProducto = mock(RepositorioProducto.class);
		
		when(repositorioGarantia.obtenerProductoConGarantiaPorCodigo(producto.getCodigo())).thenReturn(null);
		
		Vendedor vendedor = new Vendedor(repositorioProducto, repositorioGarantia);
		
		Calendar calFechaInicial = Calendar.getInstance();
		calFechaInicial.set(2019, 06, 17);
		
		Date FechaFinalReal =  vendedor.calcularFechaFinGarantia(calFechaInicial.getTime(),4);
		
		Calendar calFechaEsperada = Calendar.getInstance();
		calFechaEsperada.set(2019, 06,20 );

		Date fechaEsperada = calFechaEsperada.getTime();
		
		SimpleDateFormat formatoDeFecha = new SimpleDateFormat("dd/MM/dd");
		assertEquals(formatoDeFecha.format(FechaFinalReal), formatoDeFecha.format(fechaEsperada));
	}
	
	@Test
	public void calculoFechaFinGarantiaUnDiaCaeLunesTest() {
		
		// arrange
		ProductoTestDataBuilder productoestDataBuilder = new ProductoTestDataBuilder();
		
		Producto producto = productoestDataBuilder.build(); 
		
		RepositorioGarantiaExtendida repositorioGarantia = mock(RepositorioGarantiaExtendida.class);
		RepositorioProducto repositorioProducto = mock(RepositorioProducto.class);
		
		when(repositorioGarantia.obtenerProductoConGarantiaPorCodigo(producto.getCodigo())).thenReturn(null);
		
		Vendedor vendedor = new Vendedor(repositorioProducto, repositorioGarantia);
		
		Calendar calFechaInicial = Calendar.getInstance();
		calFechaInicial.set(2019, 06, 22);
		
		Date FechaFinalReal =  vendedor.calcularFechaFinGarantia(calFechaInicial.getTime(),1);
		
		Calendar calFechaEsperada = Calendar.getInstance();
		calFechaEsperada.set(2019, 06,23 );

		Date fechaEsperada = calFechaEsperada.getTime();
		
		SimpleDateFormat formatoDeFecha = new SimpleDateFormat("dd/MM/dd");
		assertEquals(formatoDeFecha.format(FechaFinalReal), formatoDeFecha.format(fechaEsperada));
	}
}
