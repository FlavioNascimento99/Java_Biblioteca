package Util;

import com.db4o.*;
import com.db4o.config.*;
import com.db4o.ta.TransparentPersistenceSupport;

import Entities.Cliente;
import Entities.ItemVenda;
import Entities.Venda;

public class Util {
	// Criamos atributo do tipo ObjectContainer.
	private static ObjectContainer database;
	// Criamos método do tipo ObjectContainer.
	public static ObjectContainer openDatabase() {
		// SE O BANCO NÃO EXISTIR!!!, iremos criar um e configurá-lo.
		if(database == null || database.ext().isClosed()) {
			// Criamos o objeto de configuração zerado.
			EmbeddedConfiguration config = Db4oEmbedded.newConfiguration();
			// Adicionamos a sua configuração, suporte à Persistência Transparente.
			config.common().add(new TransparentPersistenceSupport());
			// Adicionamos a sua configuração, Cascata de Atualizações, para salvar automaticamente objetos relacionados.
			config.common().objectClass(Venda.class).cascadeOnUpdate(true);
			config.common().objectClass(Cliente.class).cascadeOnUpdate(true);
			config.common().objectClass(ItemVenda.class).cascadeOnUpdate(true);
			// Por fim, abrimos o banco com as configurações estabelecidas.
			database = Db4oEmbedded.openFile("databaseBiblioteca.db4o");
		}
		// Retornamos o banco de dados aberto.
		return database;
	}
	
	public static void closeDatabase() {
		if (database != null && !database.ext().isClosed()) {
			database.close();
		}
	}
}

