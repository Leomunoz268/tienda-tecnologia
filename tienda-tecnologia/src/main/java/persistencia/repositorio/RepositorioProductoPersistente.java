package persistencia.repositorio;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import dominio.Producto;
import dominio.repositorio.RepositorioProducto;
import persistencia.builder.ProductoBuilder;
import persistencia.entitad.ProductoEntity;
import persistencia.repositorio.jpa.RepositorioProductoJPA;

public class RepositorioProductoPersistente implements RepositorioProducto, RepositorioProductoJPA {

	private static final String CODIGO = "codigo";
	private static final String PRODUCTO_FIND_BY_CODIGO = "Producto.findByCodigo";
	
	private EntityManager entityManager;

	public RepositorioProductoPersistente(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public Producto obtenerPorCodigo(String codigo) {
		
		ProductoEntity productoEntity = obtenerProductoEntityPorCodigo(codigo);
		return ProductoBuilder.convertirADominio(productoEntity);
	}
	
	@Override
	public ProductoEntity obtenerProductoEntityPorCodigo(String codigo) {
		
		Query query = entityManager.createNamedQuery(PRODUCTO_FIND_BY_CODIGO);
		query.setParameter(CODIGO, codigo);

		return (ProductoEntity) query.getSingleResult();
	}

	@Override
	public void agregar(Producto producto) {
		entityManager.persist(ProductoBuilder.convertirAEntity(producto));
	}	
	/**
	 * 
	 * @param codigo Código del articulo a evaluar
	 * @return true si el codigo posee 3 vocales de lo contrario false
	 */
	public boolean validarTresVocales(String codigo) {
		int contador = 0;
		for(int x=0;x<codigo.length();x++) {
			  if ((codigo.charAt(x)=='a') || (codigo.charAt(x)=='e') || (codigo.charAt(x)=='i') || (codigo.charAt(x)=='o') || (codigo.charAt(x)=='u')){
			    contador++;
			  }
			}
		return (contador == 3);
	};

}
